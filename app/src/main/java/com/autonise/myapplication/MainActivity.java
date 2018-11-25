package com.autonise.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = new File(getApplicationContext().getFilesDir()+"/User_information.json");
        if(!f.exists()) {
            first_time_welcome();
        }
//        else
//        {
//            f.delete();
//            first_time_welcome();
//        }
    }
    public void first_time_welcome() {
        Intent intent = new Intent(this, First_time_welcome_page.class);
        startActivity(intent);
    }
}




