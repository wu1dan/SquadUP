package com.example.squadup;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.NotificationChannel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private final String TAG = "MainActivity";


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
            default:
                Toast.makeText(MainActivity.this, "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //only need to create a channel on android oreo and above
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel =
                    new NotificationChannel(ConstantsUtil.CHANNEL_ID, ConstantsUtil.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription(ConstantsUtil.CHANNEL_DESCRIPTION);
            mChannel.enableLights(false);

            mNotificationManager.createNotificationChannel(mChannel);

        }

        Button btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);                                     //CREATE EVENT
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        Button btnCurrentEvent = (Button) findViewById(R.id.btnCurrentEvent);                                     //BROWSE EVENTS
        btnCurrentEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CurrentEvent.class);
                startActivity(intent);
            }
        });

        Button btnProfile = (Button) findViewById(R.id.btnProfile);                                     //PROFILE
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
        editor = sharedPreferences.edit();

        //retrieve firebase token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Put user token into sharedpreferences
                        editor.putString("FirebaseToken", token);
                        editor.apply();

                        // Log it
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                    }
                });

        if (!sharedPreferences.contains("DateofBirth")){
            Intent intent = new Intent(MainActivity.this, CreateProfile.class);
            postJSON();
            Toast.makeText(MainActivity.this, "Please create a profile to get started!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    private void postJSON() {

        try {
            String URL = "http://20.43.19.13:3000/Users";
            JSONObject userJSON = new JSONObject();

            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("UserID", "tempID");
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            userJSON.put("Interests", sharedPreferences.getStringSet("Interests", null));
            userJSON.put("GoogleIDToken", sharedPreferences.getString("GoogleIDToken", ""));


            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(), "Welcome to SquadUP!", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(postRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
