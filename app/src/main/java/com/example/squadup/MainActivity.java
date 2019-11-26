package com.example.squadup;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.NotificationChannel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.squadup.PendingEvent.sharedPreferencesEditor;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private double lat;
    private float flat;
    private float flong;
    private double longitude;
    private FusedLocationProviderClient client;

    private final String TAG = "MainActivity";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {            //add cases depending on buttons
        switch (item.getItemId()) {
            case R.id.btnGhostmode:
                Toast.makeText(MainActivity.this, "Please go into Settings to toggle Ghost Mode.", Toast.LENGTH_SHORT).show();
              /*  MainActivity.editor = MainActivity.sharedPreferences.edit();
                if (sharedPreferences.getBoolean("Ghost Mode", false)) { //if ghost mode is on
                    editor.putBoolean("Ghost Mode", false); //turn it off
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Ghost Mode is OFF", Toast.LENGTH_SHORT).show();
                } else { //if ghost mode is off
                    editor.putBoolean("Ghost Mode", true); //turn it on
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Ghost Mode is ON", Toast.LENGTH_SHORT).show();
                }
                return true;*/
            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, Settings.class);
                this.startActivity(intentSettings);
                return true;
            case R.id.btnHelp:
                Intent intentHelp = new Intent(this, Help.class);
                this.startActivity(intentHelp);
                return true;
            case R.id.btnSignout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;
            case R.id.btnViewprofile:
                Intent intentProfile = new Intent(this, Profile.class);
                this.startActivity(intentProfile);
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        String defValue = "defValue";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //only need to create a channel on android oreo and above
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
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        Button btnCurrentEvent = (Button) findViewById(R.id.btnCurrentEvent);                                     //BROWSE EVENTS
        btnCurrentEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sharedPreferences.getString("tempID", defValue).equals(defValue)) { //if they have a pending event

                    Intent intent = new Intent(MainActivity.this, PendingEvent.class);
                    startActivity(intent);

                } else { //the only other option when this button is clicked is if they have a current event

                    Intent intent = new Intent(MainActivity.this, CurrentEvent.class);
                    startActivity(intent);

                }
            }
        });

        Button btnProfile = (Button) findViewById(R.id.btnProfile);//PROFILE
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        if (!sharedPreferences.getString("EventName", defValue).equals(defValue)) { //if they are already in an event, "EventName" will not be the default
            btnCreateEvent.setEnabled(false);
            btnCreateEvent.setText("Cannot create an event when in an event!");
            btnCurrentEvent.setText("Current Event");
            //Toast.makeText(MainActivity.this,"create" + sharedPreferences.getString("EventName", defValue), Toast.LENGTH_SHORT).show();
        } else if (!sharedPreferences.getString("tempID", defValue).equals(defValue)) { //this means that they do have a pending event
            btnCreateEvent.setEnabled(false);
            btnCreateEvent.setText("Cannot create an event when in an event!");
            btnCurrentEvent.setText("Pending Event");
        } else { //if they don't have a current or pending event
            btnCreateEvent.setEnabled(true);
            btnCreateEvent.setText("Create Event");
            btnCurrentEvent.setText("No Current or Pending Events!");
            btnCurrentEvent.setEnabled(false);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PendingEvent.class);
                startActivity(intent);
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
        timer.schedule(fiveMinuteTask, 01, 5000);

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

        if (!sharedPreferences.contains("DateofBirth")) {
            Intent intent = new Intent(MainActivity.this, CreateProfile.class);
            Toast.makeText(MainActivity.this, "Please create a profile to get started!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        if (sharedPreferences.contains("Interests") && sharedPreferences.getStringSet("Interests", null).size() == 0) {
            Intent intent = new Intent(MainActivity.this, Interests.class);
            Toast.makeText(MainActivity.this, "Please select some interests before continuing.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        if (sharedPreferences.contains("id")) {
            putJSON();
        } else {
            postJSON();
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
            userJSON.put("latdec", sharedPreferences.getFloat("longdec", 0.0f));
            userJSON.put("longdec", sharedPreferences.getFloat("longdec", 0.0f));

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

    public void putJSON() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        try {
            String url = "http://20.43.19.13:3000/Users/5ddb734416976566b576b2d8";
            JSONObject userJSON = new JSONObject();
            userJSON.put("id", sharedPreferences.getString("id", ""));
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            float userlat = sharedPreferences.getFloat("latdec", flat);
            userJSON.put("latdec", lat);
            userJSON.put("longdec", longitude);

            Set<String> setInterests = sharedPreferences.getStringSet("Interests", null);
            if (sharedPreferences.contains("Submission")) {
                userJSON.put("Submission", null);
            }
            JSONArray interests = new JSONArray();
            if (setInterests != null) {
                for (String interest : setInterests) {
                    interests.put(interest);
                }
            }
            userJSON.put("Interests", interests);

            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //
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

    protected void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Toast.makeText(MainActivity.this, "Please enable Location Services to use SquadUP!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Login.class);
            this.startActivity(intent);
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double dlong = location.getLongitude();
            double dlat = location.getLatitude();
            flong = (float) dlong;
            flat = (float) dlat;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            lat = location.getLatitude();
            flong = (float) longitude;
            flat = (float) lat;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //
        }

        @Override
        public void onProviderEnabled(String provider) {
            //
        }

        @Override
        public void onProviderDisabled(String provider) {
            //
        }
    };

    Timer timer  = new Timer ();
    TimerTask fiveMinuteTask = new TimerTask() {
        @Override
        public void run() {
            getLocation();
            putJSON();
        }
    };

}
