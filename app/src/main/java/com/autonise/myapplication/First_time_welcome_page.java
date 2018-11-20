package com.autonise.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
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

import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class First_time_welcome_page extends AppCompatActivity{

    int RC_SIGN_IN = 0;
    String TAG = "tag:0";
    String ERRORTAG = "errortag:0";
    String url="http://10.0.2.2:6012";
    int success = 1;
//    String url="http://stackoverflow.com";

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

            Log.d(TAG, "ONactivityresult");
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

        String filename = getApplicationContext().getFilesDir().toString()+"/User_information.json";

        JSONObject obj = new JSONObject();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        Log.d("tag", formattedDate);

        try {
            obj.put("DisplayName", account.getDisplayName());
            obj.put("Email", account.getEmail());
            obj.put("Id", account.getId());
            obj.put("PhotoURL", account.getPhotoUrl());
            obj.put("Date", formattedDate);
        }
        catch (JSONException e) {
            success = 0;
            Log.d(TAG+"0", e.getMessage());
        }

        try {
            FileWriter file = new FileWriter(filename);
            file.write(obj.toString());
            file.flush();

        } catch (IOException e) {
            Log.d(TAG+"1", e.getMessage());
            success = 0;
        }
        Log.d(TAG, "getting into http");


        new send_data().execute(obj.toString());

    }

    public class send_data extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {

                URL object = new URL(url);
                try {
                    HttpURLConnection con = (HttpURLConnection) object.openConnection();
                    try
                    {
                        con.setRequestMethod("POST");
                    }
                    catch (java.net.ProtocolException e) {
                        Log.d(TAG + "1", e.getMessage());
                        success = 0;
                    }
                    con.setDoOutput(true);

                    OutputStream asdf = con.getOutputStream();

                    OutputStreamWriter wr = new OutputStreamWriter(asdf);
                    Log.d(TAG + "1", params[0]);
                    wr.write(params[0]);
                    wr.flush();
                    wr.close();
                    asdf.close();
                    int responseCode = con.getResponseCode();
                    Log.d(TAG + "1", Integer.toString(responseCode));
                }
                catch (IOException e) {

                    Log.d(TAG + "01", e.getMessage());
                    success = 0;
                }
            }
            catch(java.net.MalformedURLException e)
            {
                Log.d(TAG+"1", e.getMessage());
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
