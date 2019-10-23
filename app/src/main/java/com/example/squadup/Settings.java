package com.example.squadup;

import com.example.squadup.AuxFunctions;
import com.example.squadup.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ApiException;

import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Settings extends AppCompatActivity {
    private Button btnsignout, btnFirebase, btnGhostMode;

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

        btnsignout = (Button)findViewById(R.id.btnSignOut);                                     //PROFILE
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Settings.this, Login.class);
                startActivity(intent);
            }
        });

        btnFirebase = (Button)findViewById(R.id.btnFirebase);                                     //BROWSE EVENTS
        btnFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Settings.this, FirebaseToken.class);
                startActivity(intent);
            }
        });

        btnGhostMode = (Button)findViewById(R.id.btnGhostMode);
        btnGhostMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(AuxFunctions.isGhostMode()){ //if GhostMode is on/true, turn it off
                    btnGhostMode.setText("Toggle Ghost Mode (OFF)");

                    FirebaseMessaging.getInstance().subscribeToTopic("events")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.msg_subscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.msg_subscribe_failed);
                                    }
                                    Log.d(TAG, msg);
                                    Toast.makeText(Settings.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    btnGhostMode.setText("Toggle Ghost Mode (ON)");

                    FirebaseMessaging.getInstance().unsubscribeFromTopic("events")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.msg_unsubscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.msg_subscribe_failed);
                                    }
                                    Log.d(TAG, msg);
                                    Toast.makeText(Settings.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                MainActivity.switchGhostMode(); //turns ghostMode variable to true or false, depending on what it was
            }
        });

    }





}
