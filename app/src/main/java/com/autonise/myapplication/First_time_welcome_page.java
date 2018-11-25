package com.autonise.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Calendar;

import java.net.URL;
import java.net.URLEncoder;

import java.text.SimpleDateFormat;

public class First_time_welcome_page extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    String TAG = "tag:0";
    String url="https://10.0.2.2:6012";
    int success = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPlayServices();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_welcome_page);
        TextView change_text = findViewById(R.id.textView);
        change_text.setText(getResources().getString(R.string.first_welcome));
        Log.d("tag:0", "BeforeClick");

        final SignInButton button = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        final Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag:1", "Click");
                signIn(signInIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(First_time_welcome_page.this);
    }
    public void checkPlayServices() {
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
    }

    private void signIn(Intent signInIntent) {

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Log.d(TAG, "OnActivityResult");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            }
            catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                success = 0;
            }

    }
    private void handleSignInResult(GoogleSignInAccount account) {
        Log.d(TAG, "handleSignInResult");
        try {
            String filename = getApplicationContext().getFilesDir().toString()+"/User_information.json";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Map<String,Object> obj_map = new LinkedHashMap<>();

            obj_map.put("DisplayName", account.getDisplayName());
            obj_map.put("Email", account.getEmail());
            obj_map.put("Id", account.getId());
            obj_map.put("PhotoURL", account.getPhotoUrl());
            obj_map.put("Date", formattedDate);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : obj_map.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            Log.d(TAG, "getting into http");


            new send_data().execute(postData.toString());

            Secure_writer writer = new Secure_writer();
            writer.write(obj_map.toString(), account.getDisplayName(), filename);
//            Secure_reader reader = new Secure_reader();
//            Log.d(TAG+"msg", reader.read(account.getDisplayName(), filename));


        }
        catch (java.io.UnsupportedEncodingException e)
        {
            Log.d(TAG+"0", e.getMessage());
        }

    }

    public class send_data extends AsyncTask<String, Void, String>{

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = new BufferedInputStream(getAssets().open("cert.crt"));
                Certificate ca;
                ca = cf.generateCertificate(caInput);
                caInput.close();
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);
                HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        // ToDo Actually verify the host here
                        return true;
                    }
                };


                URL object = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) object.openConnection();

                con.setSSLSocketFactory(context.getSocketFactory());
                con.setHostnameVerifier(hostnameVerifier);

                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Log.d(TAG, "got into https");

                OutputStream asdf = con.getOutputStream();

                OutputStreamWriter wr = new OutputStreamWriter(asdf);
                Log.d(TAG + "1", params[0]);

                wr.write(params[0]);
                wr.flush();
                wr.close();
                asdf.close();
                int responseCode = con.getResponseCode();
                Log.d(TAG + "1", Integer.toString(responseCode));

            } catch (java.security.cert.CertificateException e) {
                Log.d(TAG, e.getMessage());
                success = 0;
            } catch (java.io.FileNotFoundException e) {
                Log.d(TAG, e.getMessage());
                success = 0;
            } catch (java.io.IOException e) {
                Log.d(TAG, e.getMessage());
                success = 0;
            } catch (java.security.KeyStoreException e) {
                Log.d(TAG, e.getMessage());
                success = 0;
            } catch (java.security.NoSuchAlgorithmException e) {
                Log.d(TAG, e.getMessage());
                success = 0;
            } catch (java.security.KeyManagementException e) {
                Log.d(TAG, e.getMessage());
                success = 0;
            }
            return url;
        }

        protected void onPostExecute(String imageUrl) {
            if(success==1)
                finish();
            else
                {
                    success = 1;
                Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), "Login Failed! Is Internet On?", 3000);
                mySnackbar.show();
            }

        }

    }
}
