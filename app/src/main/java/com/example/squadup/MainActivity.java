package com.example.squadup;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnCreateEvent;
    Button btnBrowseEvent;

    Button btnSettings;
    Button btnFriendsList;
    Button btnPastEvents;
    Button btnProfile;

    Intent intent;


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


        btnCreateEvent = (Button)findViewById(R.id.btnCreateEvent);                                     //CREATE EVENT
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        btnBrowseEvent = (Button)findViewById(R.id.btnBrowseEvent);                                     //BROWSE EVENTS
        btnBrowseEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(MainActivity.this, BrowseEvents.class);
                startActivity(intent);
            }
        });

        btnSettings = (Button)findViewById(R.id.btnSettings);                                     //SETTINGS
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });


        btnProfile = (Button)findViewById(R.id.btnProfile);                                     //PROFILE
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }




}
