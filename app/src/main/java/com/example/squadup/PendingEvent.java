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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PendingEvent extends AppCompatActivity {

    private String tempID;
    private String tempName;
    private String tempCategories;
    private String tempDescription;
    private String tempTime;
    private String tempDate;
    private String tempLocation;
    private String tempTotalSpots;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

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

        TextView tvPName = (TextView) findViewById(R.id.tvPName);
        TextView tvPCategories = (TextView) findViewById(R.id.tvPCategories);
        TextView tvPDescription = (TextView) findViewById(R.id.tvPDescription);
        TextView tvPTime = (TextView) findViewById(R.id.tvPTime);
        TextView tvPDate = (TextView) findViewById(R.id.tvPDate);
        TextView tvPLocation = (TextView) findViewById(R.id.tvPLocation);
        TextView tvPTotalSpots = (TextView) findViewById(R.id.tvPTotalSpots);

        tvPName.setText(FirebaseMessaging.notificationTitle);
        tvPCategories.setText("");
        tvPDescription.setText("");
        tvPTime.setText("");
        tvPDate.setText("");
        tvPLocation.setText("");
        tvPTotalSpots.setText("");

        String defValue = " ";

        getJSON();

        tvPName.setText(tempName);
        tvPCategories.setText(tempCategories);
        tvPDescription.setText(tempDescription);
        tvPTime.setText(tempTime);
        tvPDate.setText(tempDate);
        tvPLocation.setText(tempLocation);
        tvPTotalSpots.setText(tempTotalSpots);

        if (sharedPreferences.getString("tempID", defValue).equals(FirebaseMessaging.eventID)) { //If they have a pending event, the "tempID" value should be equal to the eventID from the notification

            btnYes.setEnabled(true);
            btnNo.setEnabled(true);

            //Have code here for using the event id to extract all the event information out of the json
            //Here's some placeholder for now:

            getJSON(); //this turns all the text boxes into the relevant information

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

                    pendingToCurrent();

                    Intent intent = new Intent(PendingEvent.this, CurrentEvent.class);
                    startActivity(intent);
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


        }else{
            //they have no pending event
            tvPName.setText("You have no pending events! Update your interests or turn off Ghost Mode to get a match sooner.");
        }
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

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://20.43.19.13:3000/Events/5ddb650c16976566b576b295/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Process the JSON

                        try {
                            JSONObject eventObj = response.getJSONObject(0);
                            tempID = eventObj.getString("_id");
                            tempName = eventObj.getString("EventName");
                            JSONArray jsonCategories = eventObj.getJSONArray("Interests");

                            String[] categoriesArray = new String[jsonCategories.length()];
                            for (int x = 0; x < jsonCategories.length(); x++){
                                categoriesArray[x] = jsonCategories.getString(x);
                            }

                            tempCategories = categoriesArray.toString();
                            tempDescription = eventObj.getString("Description");
                            tempTime = eventObj.getString("Time");
                            tempDate = eventObj.getString("Date");
                            tempLocation = eventObj.getString("Location");
                            tempTotalSpots = eventObj.getString("TotalSpots");


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
            Set<String> setInterests = sharedPreferences.getStringSet("Interests", null);
            JSONArray interests = new JSONArray();
            for (String interest : setInterests){
                interests.put(interest);
            }
            userJSON.put("Interests", interests);


            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, userJSON, new Response.Listener<JSONObject>() {
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
