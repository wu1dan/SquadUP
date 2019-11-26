package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;



import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.maps.GeocodingApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateEvent extends AppCompatActivity{

    private TextView date; //, location;
    private EditText eventName;
    private EditText categories;
    private EditText description;
    private EditText time;
    private EditText spotsTotal;
    private EditText location;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String sName;
    private String sCategories;
    private String sDescription;
    private String sTime;
    private String sLocation;
    private String sSpotsTotal;
    private String sDate;
    private String aCategories[];
    private String dateToSend;
    private ArrayList<String> lCategories = new ArrayList<String>();
    private double lat = 0;
    private double longitude= 0;
    private ArrayList<String> users = new ArrayList<String>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private Intent intent;

    public static String dateToParse = "";
    private String tempDate = "Your Date:";
    public static String timeToParse = "";
    public static LocalDateTime dateTime = LocalDateTime.now();

    public static String latLng = "";

    /*
    ti prefix means "Text Input"
    tv prefix means "Text View"
    btn prefix means "button"
    s prefix means "String" (lets me re-use names such as Name, Categories, etc)
     */

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        intent = new Intent(CreateEvent.this, MainActivity.class);
        startActivity(intent);
    }

    private void createEvent() throws JSONException {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("EventID", "0");
        sharedPreferencesEditor.apply();

        sName = eventName.getText().toString();
        sCategories = categories.getText().toString();
        sDescription = description.getText().toString();
        sTime = time.getText().toString();

        StringBuilder sb = new StringBuilder(sTime);
        timeToParse = sb.append(":00").toString();

        sLocation = location.getText().toString();
        sSpotsTotal = spotsTotal.getText().toString();
        sDate = date.getText().toString();

        Boolean areParamsFilled;

        aCategories = sCategories.split("\\W+");
        for(String s : aCategories){
            lCategories.add(s);
        }

        String defValue = "defValue";
        if(!sharedPreferences.getString("EventName", defValue).equals(defValue)){ //if it doesn't equal defValue that means they're already in an actual event
            Toast.makeText(CreateEvent.this, "You are already in an event and may not create one!", Toast.LENGTH_SHORT).show();
            intent = new Intent(CreateEvent.this, MainActivity.class);
            startActivity(intent);

        }else if(!sharedPreferences.getString("PendingEvent", defValue).equals(defValue)) { //if it doesn't equal defValue that means they DO have a pending event
            Toast.makeText(CreateEvent.this, "Please reject your current pending event before creating a new one.", Toast.LENGTH_SHORT).show();
            intent = new Intent(CreateEvent.this, PendingEvent.class);
            startActivity(intent);

        }else{
            areParamsFilled = checkAllInputs();

            if(!areParamsFilled)
                return;

            Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_LONG).show();

            //Create the event on the database

            postJSON();

            //Updating shared preferences so that this event they just made is put into their current event
            //Event ID will be generated, hardcoded 0 is just a placeholder

            updateSharedPrefs();

            intent = new Intent(CreateEvent.this, CurrentEvent.class);
            startActivity(intent);
        }
        //leave this empty
    }

    protected Boolean checkAllInputs() throws JSONException {
        Boolean params = true;

        params = checkName();
        if(!params)
            return params;

        params = checkCategories();
        if(!params)
            return params;

        params = checkDescription();
        if(!params)
            return params;

        params = checkTime();
        if(!params)
            return params;

        params = checkLocation();
        if(!params)
            return params;

        params = checkDate();
        if(!params)
            return params;

        params = checkSpots();
        if(!params)
            return params;

        return params;
    }

    //BELOW ARE ALL HELPER METHODS USED ABOVE

    protected Boolean checkName(){
        if (sName.length() == 0) {                   //ERROR MESSAGES IF MISSING INFORMATION OR VERIFIED DOES NOT MATCH ORIGINAL
            Toast.makeText(CreateEvent.this, "Please enter a valid event name", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected Boolean checkCategories(){
        if (sCategories.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter at least one relevant event category", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected Boolean checkDescription(){
        if (sDescription.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter an event description", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected Boolean checkTime(){
        if (sTime.length() != 5 || sTime.charAt(2) != ':') {
            Toast.makeText(CreateEvent.this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected Boolean checkLocation() throws JSONException {
        if (sLocation.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
            return false;
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCqeDrgugKAdMEn6HlEBYSkqlV-UsI9Q4o")
                .build();
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(context,
                    sLocation).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(results[0].geometry);

        //JSONArray root = new JSONArray(gson.toJson(results[0].geometry));
        JSONObject geometryObj = new JSONObject(jsonString);
        JSONObject locationObj = geometryObj.getJSONObject("location");
        lat = locationObj.getDouble("lat");
        longitude = locationObj.getDouble("lng");

        //Toast.makeText(CreateEvent.this, "lat: " + lat + " long: " + longitude, Toast.LENGTH_SHORT).show();

        return true;
    }

    protected Boolean checkDate(){
        if(sDate.equals(tempDate)){
            Toast.makeText(CreateEvent.this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected Boolean checkSpots(){

        try {
            if (sSpotsTotal.length() == 0 || Integer.valueOf(sSpotsTotal) < 2) { //the null thing doesn't actually work, need to catch NumberFormatException
                Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (NumberFormatException e) {
            Toast.makeText(CreateEvent.this, "Please enter a valid number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected void updateSharedPrefs(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("EventID", "0");
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventName", sName);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventCategories", sCategories);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventDescription", sDescription);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventTime", sTime);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventDate", dateToSend);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("EventLocation", sLocation);
        sharedPreferencesEditor.apply();
        sharedPreferencesEditor.putString("TotalSpots", sSpotsTotal);
        sharedPreferencesEditor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);            //Create Event Button
        Button btnPickDate = (Button) findViewById(R.id.btnPickDate);                  //Pick Data Button
        eventName = (EditText) findViewById(R.id.tiEventName);                         //Event Name Text Input
        categories = (EditText) findViewById(R.id.tiCategories);                       //Event Categories Text Input
        description = (EditText) findViewById(R.id.tiDescription);                     //Event Description Text Input
        time = (EditText) findViewById(R.id.tiTime);                                   //Time Text Input
        //location = (TextView) findViewById(R.id.tiLocation);                         //Location Text View
        location = (EditText) findViewById(R.id.tiLocation);                           //Location Text Input
        spotsTotal = (EditText) findViewById(R.id.tiSpotsTotal);                       //Total Spots Text Input
        date = (TextView)findViewById(R.id.tvDate);                                    //Date Text View
        date.setText(tempDate);                                                        //for if condition later

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog eventDatePicker = new DatePickerDialog(CreateEvent.this, android.R.style.Theme_Black,
                        mDateSetListener, year, month, day);
                eventDatePicker.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int pickerMonth, int dayOfMonth) {
                int pMonth = pickerMonth + 1; //january is 0
                String eventDate = pMonth + "/" + dayOfMonth + "/" + year;
                date = (TextView)findViewById(R.id.tvDate);
                date.setText("Your date: " + eventDate);

                dateToSend = eventDate;
                dateToParse = year + "-" + pMonth + "-" + dayOfMonth;

            }
        };

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    createEvent();
                } catch (JSONException e) {
                    System.out.println(e);
                }

            }

        }); //end of CreateEvent button
    }

    public void parseDateTime(){

        String dateTimeToParse = dateToParse + "T" + timeToParse;
        Log.d("Date Time", dateTimeToParse);

        dateTime = LocalDateTime.parse(dateTimeToParse);

    }
    /*

        JSON Schema:

        {"EventName":"string"}
        {"Categories":"ArrayList"}
        {"Description":"string"}
        {"Time":"string"}
        {"Date":"string"}
        {"Lat":"double"}
        {"Long":"double"}
        {"TotalSpots":"int"}
        {"Users":"ArrayList"}
     */

    //update an event -- add users by ID
    /* public void putJSON() {
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);
        try {
            String url = "http://20.43.19.13:3000/Events";
            JSONObject eventJSON = new JSONObject();
            eventJSON.put("EventName", eventName);
            eventJSON.put("Categories", aCategories);
            eventJSON.put("Description", description);
            eventJSON.put("Time", sharedPreferences.getString("EventTime", ""));
            eventJSON.put("Date", sharedPreferences.getString("EventDate", ""));
            eventJSON.put("Location", sharedPreferences.getString("EventLocation", ""));
            eventJSON.put("TotalSpots", sharedPreferences.getString("TotalSpots", ""));

            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, eventJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(CreateEvent.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
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
    } */

    //create an event
    private void postJSON() {
        String URL = "http://20.43.19.13:3000/Events";
        JSONObject eventJSON = new JSONObject();
        int totalSpots = Integer.valueOf(sSpotsTotal);
        users.add(sharedPreferences.getString("id", ""));
        //String[] testArray = {"Basketball", "Soccer", "Baseball"};

        try {
            eventJSON.put("EventName", sName);
            eventJSON.put("Interests", lCategories);
            eventJSON.put("Description", sDescription);
            eventJSON.put("Time", sTime);
            eventJSON.put("Date", dateToSend);

            parseDateTime();
            eventJSON.put("LocalDateTime", dateTime);

            eventJSON.put("Location", sLocation);
            eventJSON.put("Lat", lat);
            eventJSON.put("Long", longitude);
            eventJSON.put("TotalSpots", totalSpots);
            eventJSON.put("Users", users);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, eventJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getApplicationContext(), "Event created successfully.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "There was an error. Please try again.", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CreateEvent.this);
        requestQueue.add(postRequest);

    }
}


