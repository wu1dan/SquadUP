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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Profile extends AppCompatActivity {
    //private Uri uriProfilePicture;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        if(sharedPreferences.contains("Interests")) {
            if (sharedPreferences.contains("id")) {
                putJSON();
                Intent intent = new Intent(Profile.this, Profile.class);
                startActivity(intent);
            } else {
                postJSON();
                Intent intent = new Intent(Profile.this, Profile.class);
                startActivity(intent);
            }
        }
        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        Button btnEditInterests = findViewById(R.id.btnInterests);
        btnEditInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Interests.class);
                startActivity(intent);
                finish();
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

    public void putJSON() {
        RequestQueue queue = Volley.newRequestQueue(Profile.this);
        try {
            String id = sharedPreferences.getString("id", "");
            String url = "http://20.43.19.13:3000/Users/5ddb734416976566b576b2d8";
            JSONObject userJSON = new JSONObject();
            userJSON.put("id", sharedPreferences.getString("id", ""));
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            Set<String> setInterests = sharedPreferences.getStringSet("Interests", null);
            JSONArray interests = new JSONArray();
            for (String interest : setInterests){
                interests.put(interest);
            }
            userJSON.put("Interests", interests);


            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //yeet
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            queue.add(putRequest);


        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private void postJSON() {
        String URL = "http://20.43.19.13:3000/Users";
        JSONObject userJSON = new JSONObject();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();

        try {
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            Set<String> setInterests = sharedPreferences.getStringSet("Interests", null);
            JSONArray interests = new JSONArray();
            for (String interest : setInterests){
                interests.put(interest);
            }
            userJSON.put("Interests", interests);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, userJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String userID = response.toString();
                spEditor.putString("id", userID);
                spEditor.apply();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)   {
                //xd
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(postRequest);

    }
}
