package com.example.squadup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.app.NotificationChannel;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    Button btnCreateEvent;
    Button btnCurrentEvent;

    Button btnSettings;
    Button btnProfile;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){            //add cases depending on buttons
        switch(item.getItemId()){
            case R.id.btnSettings:
                Intent intent = new Intent(this, Settings.class);
                this.startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "AIzaSyB2QJ7cSMQMcGSXTCYaA4ZirrInEJXekdo";

        if (apiKey.equals("")) {
            Toast.makeText(this, getString(R.string.error_api_key), Toast.LENGTH_LONG).show();
            return;
        }
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //only need to create a channel on android oreo and above
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel =
                    new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(false);

            mNotificationManager.createNotificationChannel(mChannel);

        }

        btnCreateEvent = (Button)findViewById(R.id.btnCreateEvent);                                     //CREATE EVENT
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        btnCurrentEvent = (Button)findViewById(R.id.btnCurrentEvent);                                     //BROWSE EVENTS
        btnCurrentEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CurrentEvent.class);
                startActivity(intent);
            }
        });

        btnProfile = (Button)findViewById(R.id.btnProfile);                                     //PROFILE
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PendingEvent.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.contains("DateofBirth")){
            Intent intent = new Intent(MainActivity.this, createprofile.class);
            Toast.makeText(MainActivity.this, "Please create a profile to get started!", Toast.LENGTH_SHORT).show();
            editor = sharedPreferences.edit();
            editor.putBoolean("Ghost Mode", false);
            editor.apply();
            startActivity(intent);
        }
    }
}
