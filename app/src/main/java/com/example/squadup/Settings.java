package com.example.squadup;

import com.example.squadup.JSONParser;

//import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;
//import android.util.Log;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.facebook.login.LoginManager;

//import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApi;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;

public class Settings extends AppCompatActivity {
    private Button btnsignout, btnFirebase, btnGhostMode;
    Boolean ghostMode = null;

    private static final String TAG = "Settings";

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }


    GoogleSignInClient googleSignInClient;
    GoogleApi googleapiclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        MainActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.editor = MainActivity.sharedPreferences.edit();

        btnsignout = (Button) findViewById(R.id.btnSignOut);                                     //PROFILE
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Settings.this, Login.class);
                startActivity(intent);
            }
        });

        btnFirebase = (Button) findViewById(R.id.btnFirebase);                                     //BROWSE EVENTS
        btnFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONParser.sendDataToServer();

                //Intent intent = new Intent(Settings.this, FirebaseToken.class);
                //startActivity(intent);
            }
        });

        btnGhostMode = (Button) findViewById(R.id.btnGhostMode);
        btnGhostMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.editor = MainActivity.sharedPreferences.edit();
                if(MainActivity.sharedPreferences.getBoolean("Ghost Mode", false) == true){ //if ghost mode is on
                    MainActivity.editor.putBoolean("Ghost Mode", false); //turn it off
                    MainActivity.editor.apply();
                    btnGhostMode.setText("Ghost Mode: OFF");
                }else{ //if ghost mode is off
                    MainActivity.editor.putBoolean("Ghost Mode", true); //turn it on
                    MainActivity.editor.apply();
                    btnGhostMode.setText("Ghost Mode: ON");
                }

            }

        });
    }

}
