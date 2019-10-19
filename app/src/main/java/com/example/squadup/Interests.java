package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Interests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        String interestsTag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Button btnSaveInterests;

        btnSaveInterests = findViewById(R.id.btnSaveChanges);
        btnSaveInterests.setOnClickListener(new View.OnClickListener() {
            // ADD CODE TO ACTUALLY SAVE THE INTERESTS
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Interests.this, createprofile.class);
                startActivity(intent);
            }
        });


    }



}
