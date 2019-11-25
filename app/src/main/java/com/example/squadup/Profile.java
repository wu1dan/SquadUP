package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.HashSet;
import java.util.Set;

public class Profile extends AppCompatActivity {

    //private Uri uriProfilePicture;

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        Button btnEditInterests = findViewById(R.id.btnInterests);
        btnEditInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Interests.class);
                startActivity(intent);
            }
        });

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, CreateProfile.class);
                startActivity(intent);
            }
        });

        TextView tvFirstName = findViewById(R.id.tvFirstName);
        tvFirstName.setPaintFlags(tvFirstName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvFirstName.setText(sharedPreferences.getString("FirstName", ""));

        TextView tvLastName = findViewById(R.id.tvLastName);
        tvLastName.setPaintFlags(tvLastName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLastName.setText(sharedPreferences.getString("LastName", ""));

        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setPaintFlags(tvEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvEmail.setText(sharedPreferences.getString("Email", ""));

        TextView tvDateofBirth = findViewById(R.id.tvDateofBirth);
        tvDateofBirth.setPaintFlags(tvDateofBirth.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDateofBirth.setText((sharedPreferences.getString("DateofBirth", "")));


        ImageView imgProfilePicture = findViewById(R.id.imgProfilePicture);
        String profilePictureURL = sharedPreferences.getString("ProfilePicture", "");
        Uri pfp = Uri.parse(profilePictureURL);

        Glide
                .with(Profile.this)
                .load(pfp)
                .into(imgProfilePicture);

        TextView tvGender = findViewById(R.id.tvGender);
        tvGender.setPaintFlags(tvGender.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvGender.setText((sharedPreferences.getString("Gender","")));

        TextView tvtest = findViewById(R.id.testest);
        Set<String> settt =((sharedPreferences.getStringSet("Interests", new HashSet<String>())));
        tvtest.setText(settt.toString());

    }


}
