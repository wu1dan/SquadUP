package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Homepage extends AppCompatActivity {

    Button btnCreateEvent;
    Button btnBrowseEvent;

    Button btnSettings;
    Button btnFriendsList;
    Button btnPastEvents;
    Button btnProfile;

    Intent intent;


    public class BrowseEvents extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_browse_events);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        btnCreateEvent = (Button)findViewById(R.id.btnCreateEvent);                                     //CREATE EVENT
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(Homepage.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        btnBrowseEvent = (Button)findViewById(R.id.btnBrowseEvent);                                     //BROWSE EVENTS
        btnBrowseEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(Homepage.this, BrowseEvents.class);
                startActivity(intent);
            }
        });

        btnSettings = (Button)findViewById(R.id.btnSettings);                                     //SETTINGS
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(Homepage.this, Homepage.class);
                startActivity(intent);
            }
        });


        btnProfile = (Button)findViewById(R.id.btnProfile);                                     //PROFILE
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(Homepage.this, Profile.class);
                startActivity(intent);
            }
        });

    }
}