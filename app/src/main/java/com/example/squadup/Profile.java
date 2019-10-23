package com.example.squadup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class Profile extends AppCompatActivity {
    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

    Button btnEditInterests;
    Button btnEditProfile;
    TextView tvFirstName;
    TextView tvLastName;
    TextView tvEmail;
    TextView tvDateofBirth;
    TextView tvGender;
    ImageView imgProfilePicture;
    Uri uriProfilePicture;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        btnEditInterests = findViewById(R.id.btnInterests);
        btnEditInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Interests.class);
                startActivity(intent);
            }
        });

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, createprofile.class);
                startActivity(intent);
            }
        });

        tvFirstName = findViewById(R.id.tvFirstName);
        tvFirstName.setPaintFlags(tvFirstName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvFirstName.setText(sharedPreferences.getString("FirstName", ""));

        tvLastName = findViewById(R.id.tvLastName);
        tvLastName.setPaintFlags(tvLastName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLastName.setText(sharedPreferences.getString("LastName", ""));

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setPaintFlags(tvEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvEmail.setText(sharedPreferences.getString("Email", ""));

        tvDateofBirth = findViewById(R.id.tvDateofBirth);
        tvDateofBirth.setPaintFlags(tvDateofBirth.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDateofBirth.setText((sharedPreferences.getString("DateofBirth", "")));


        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        uriProfilePicture = Uri.parse(sharedPreferences.getString("ProfilePicture", ""));
        imgProfilePicture.setImageURI(uriProfilePicture);

        tvGender = findViewById(R.id.tvGender);
        tvGender.setPaintFlags(tvGender.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvGender.setText((sharedPreferences.getString("Gender","")));

        TextView tvtest = findViewById(R.id.testest);
        Set<String> settt =((sharedPreferences.getStringSet("Interests", new HashSet<String>())));
        tvtest.setText(settt.toString());






        /* GoogleSignInAccount googleacct = GoogleSignIn.getLastSignedInAccount(this);       //We get to steal their info from google
        if (googleacct != null) {
            String personName = googleacct.getDisplayName();      //self explanatory
            String personGivenName = googleacct.getGivenName();       //self explanatory
            String personFamilyName = googleacct.getFamilyName();     //self explanatory
            String personEmail = googleacct.getEmail();       //self explanatory
            String personId = googleacct.getId();     //unique id
            String IdToken = googleacct.getIdToken();     //id token can be sent to server
            Uri personPhoto = googleacct.getPhotoUrl();       //self explanatory
        }
        */


    }


}
