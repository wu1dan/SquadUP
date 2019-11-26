package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import java.util.Set;

public class PendingEvent extends AppCompatActivity {

    public static String tempID;
    public static String tempName;
    public static String tempCategories = "Doing CPEN";
    public static String tempDescription;
    public static String tempTime;
    public static String tempDate;
    public static String tempLocation;
    public static String tempTotalSpots;
    public static String eventUsers = "";
    public static String storedEventUsers = "";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor sharedPreferencesEditor;
    public static ArrayList<String> userList = new ArrayList<String>();
    public static TextView tvPName;
    public static TextView tvPCategories;
    public static TextView tvPDescription;
    public static TextView tvPTime;
    public static TextView tvPDate;
    public static TextView tvPLocation;
    public static TextView tvPTotalSpots;


    //tv stands for TextView, P distinguishes they are for the Pending Event (Current event will use very similar names)

    public void onBackPressed()
    {
        Intent intent = new Intent(PendingEvent.this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_event);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        Button btnYes = (Button) findViewById(R.id.btnYes);
        Button btnNo = (Button) findViewById(R.id.btnNo);

        //btnYes.setEnabled(false);
        //btnNo.setEnabled(false);

        tvPName = (TextView) findViewById(R.id.tvPName);
        tvPCategories = (TextView) findViewById(R.id.tvPCategories);
        tvPDescription = (TextView) findViewById(R.id.tvPDescription);
        tvPTime = (TextView) findViewById(R.id.tvPTime);
        tvPDate = (TextView) findViewById(R.id.tvPDate);
        tvPLocation = (TextView) findViewById(R.id.tvPLocation);
        tvPTotalSpots = (TextView) findViewById(R.id.tvPTotalSpots);

        String defValue = " ";

        //sharedPreferencesEditor.putString("tempID","5ddcca075235c507581e619c");
        //sharedPreferencesEditor.commit();

        Log.d("tempID", sharedPreferences.getString("tempID", "default string value"));

        //if (!sharedPreferences.getString("tempID", defValue).equals("")) { //If they have a pending event, the "tempID" value should be equal to the eventID from the notification

            btnYes.setEnabled(true);
            btnNo.setEnabled(true);

            getJSON(); //this grabs the needed strings to set the text boxes

            tvPName.setText(tempName);
            tvPCategories.setText(tempCategories);
            tvPDescription.setText(tempDescription);
            tvPTime.setText(tempTime);
            tvPDate.setText(tempDate);
            tvPLocation.setText(tempLocation);
            tvPTotalSpots.setText(tempTotalSpots);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getUsers();

                    storedEventUsers = eventUsers;

                    getUsers();

                    if(!storedEventUsers.equals(eventUsers)){ //checks for possible ASYNC issues
                        Toast.makeText(PendingEvent.this, "Error joining the event, please try again.", Toast.LENGTH_SHORT).show();
                    }else {

                        Log.d("List before adding: ", userList.toString());
                        userList.add(sharedPreferences.getString("id", ""));
                        Log.d("List after adding: ", userList.toString());
                        Log.d("User ID: ", sharedPreferences.getString("id", ""));
                        putJSON();
                        pendingToCurrent(); //this updates shared prefs

                        Intent intent = new Intent(PendingEvent.this, CurrentEvent.class);
                        startActivity(intent);
                    }
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(PendingEvent.this, "Successfully rejected the event!", Toast.LENGTH_SHORT).show();

                    deleteTempPrefs();

                    Intent intent = new Intent(PendingEvent.this, MainActivity.class);
                    startActivity(intent);
                }
            });


    /*    }else{
            //they have no pending event
            tvPName.setText("You have no pending events! Update your interests or turn off Ghost Mode to get a match sooner.");
        } */
    }

    private void pendingToCurrent() {

        Toast.makeText(PendingEvent.this, "Successfully joined the event!", Toast.LENGTH_SHORT).show();
        sharedPreferencesEditor.putString("EventID", tempID); //put in the event id that was used to fetch the json
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventName", tempName);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventCategories", tempCategories);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventDescription", tempDescription);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventTime", tempTime);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventDate", tempDate);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventLocation", tempLocation);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("TotalSpots", tempTotalSpots);
        sharedPreferencesEditor.apply();

        deleteTempPrefs();
    }

    private void deleteTempPrefs(){
        sharedPreferencesEditor.remove("tempID");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempName");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempCategories");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempDescription");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempTime");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempDate");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempLocation");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.remove("tempTotalSpots");
        sharedPreferencesEditor.apply();
    }

    //retrieve event data
    public void getJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(PendingEvent.this);

        String databaseURL = "http://20.43.19.13:3000/Events/";
        StringBuilder sb = new StringBuilder(databaseURL);
        String url = sb.append(sharedPreferences.getString("tempID", "")).toString(); //adds the event ID, which was received from the notification, to the end of the URL

        Log.d("URL: ", url);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Process the JSON

                        try {
                            JSONObject eventObj = response.getJSONObject(0);
                            tempName = eventObj.getString("EventName");
                            Log.d("Event Name: ", eventObj.getString("EventName"));
                            Log.d("tempName: ", tempName);
                            String interestsAsArray = eventObj.getString("Interests");
                            String interestsAsList = interestsAsArray.replace("[","").replace("]","");
                            tempCategories = "The event categories are: " + interestsAsList;
                            Log.d("Interests: ", tempCategories);
                            tempDescription = eventObj.getString("Description");
                            tempTime = eventObj.getString("Time");
                            tempDate = eventObj.getString("Date");
                            tempLocation = eventObj.getString("Location");
                            tempTotalSpots = "Total spots in event: " + eventObj.getString("TotalSpots");

                            tvPName = (TextView) findViewById(R.id.tvPName);
                            tvPCategories = (TextView) findViewById(R.id.tvPCategories);
                            tvPDescription = (TextView) findViewById(R.id.tvPDescription);
                            tvPTime = (TextView) findViewById(R.id.tvPTime);
                            tvPDate = (TextView) findViewById(R.id.tvPDate);
                            tvPLocation = (TextView) findViewById(R.id.tvPLocation);
                            tvPTotalSpots = (TextView) findViewById(R.id.tvPTotalSpots);


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("VolleyError", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }

    public void getUsers() {
        RequestQueue requestQueue = Volley.newRequestQueue(PendingEvent.this);

        String databaseURL = "http://20.43.19.13:3000/Events/";
        StringBuilder sb = new StringBuilder(databaseURL);
        String url = sb.append(sharedPreferences.getString("tempID", "")).toString(); //adds the event ID, which was received from the notification, to the end of the URL

        Log.d("URL: ", url);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Process the JSON

                        try {
                            JSONObject eventObj = response.getJSONObject(0);

                            String usersAsString = eventObj.getString("Users")
                                    .replace("]","").replace("[",""); //.replace("\"", "")
                                    //.replace("id","").replace("{","").replace("}","")
                                    //.replace(":","");
                            String[] usersAsArray = usersAsString.split(", ");
                            for(int z = 0; z < usersAsArray.length; z++){
                                userList.add(usersAsArray[z]);
                                Log.d("User: ", usersAsArray[z]);
                            }

/*
                            JSONArray jsonUsers = eventObj.getJSONArray("Users");
                            String[] usersArray = new String[jsonUsers.length()];
                            for (int y = 0; y < jsonUsers.length(); y++){
                                usersArray[y] = jsonUsers.getString(y);
                                userList.add(jsonUsers.getString(y));
                                Log.d("User " + y + 1 + ": ", userList.get(y));
                            }

                            Log.d("yeet", "entered here");
                            eventUsers = Arrays.toString(usersArray);
*/
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("VolleyError", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }

    public void putJSON() {
        RequestQueue queue = Volley.newRequestQueue(PendingEvent.this);

        String databaseURL = "http://20.43.19.13:3000/Events/";
        StringBuilder sb = new StringBuilder(databaseURL);
        String url = sb.append(sharedPreferences.getString("tempID", "")).toString(); //adds the event ID, which was received from the notification, to the end of the URL

        Log.d("URL: ", url);

        try {
            JSONObject eventJSON = new JSONObject();

            eventJSON.put("Users", userList);

            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, eventJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(PendingEvent.this, "Event joined successfully!", Toast.LENGTH_SHORT).show();
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
}
