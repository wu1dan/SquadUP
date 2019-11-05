package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

import java.util.Calendar;

public class CreateEvent extends AppCompatActivity{

    private TextView date; //, location;
    private EditText eventName;
    private EditText categories;
    private EditText description;
    private EditText time;
    private EditText spotsTotal;
    private EditText location;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
   // private String[] aCategories;

    private Intent intent;

    private String tempDate = "Your Date:";

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

    private void createEvent(){

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("Event ID", "0");
        sharedPreferencesEditor.apply();

        String sName = eventName.getText().toString();
        String sCategories = categories.getText().toString();
        String sDescription = description.getText().toString();
        String sTime = time.getText().toString();
        String sLocation = location.getText().toString();
        String sSpotsTotal = spotsTotal.getText().toString();
        String sDate = date.getText().toString();


      //  aCategories = sCategories.split("\\W"); //turns the string of categories into an array that splits categories by non-words (ie spaces, commas, etc)

        String defValue = "defValue";
        if(!sharedPreferences.getString("Event Name", defValue).equals(defValue)){ //if it doesn't equal defValue that means they're already in an actual event
            Toast.makeText(CreateEvent.this, "You are already in an event and may not create one!", Toast.LENGTH_SHORT).show();
            intent = new Intent(CreateEvent.this, MainActivity.class);

        }else if(!sharedPreferences.getString("Pending Event", defValue).equals(defValue)) { //if it doesn't equal defValue that means they DO have a pending event
            Toast.makeText(CreateEvent.this, "Please reject your current pending event before creating a new one.", Toast.LENGTH_SHORT).show();
            intent = new Intent(CreateEvent.this, PendingEvent.class);

        }else{
            if (sName.length() == 0) {                   //ERROR MESSAGES IF MISSING INFORMATION OR VERIFIED DOES NOT MATCH ORIGINAL
                Toast.makeText(CreateEvent.this, "Please enter a valid event name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sCategories.length() == 0) {
                Toast.makeText(CreateEvent.this, "Please enter at least one relevant event category", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sDescription.length() == 0) {
                Toast.makeText(CreateEvent.this, "Please enter an event description", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sTime.length() == 0) {
                Toast.makeText(CreateEvent.this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sLocation.length() == 0) {
                Toast.makeText(CreateEvent.this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
                return;
            }

            if(sDate.equals(tempDate)){
                Toast.makeText(CreateEvent.this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
                return;
            }


            if (sSpotsTotal.length() == 0 || Integer.valueOf(sSpotsTotal) == null) { //the null thing doesn't actually work, need to catch NumberFormatException
                Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
                return;
            } else if (Integer.valueOf(sSpotsTotal) < 2) {
                Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_LONG).show();

            //use this space to assign event with all its above parameters into the database



            //Updating shared preferences so that this event they just made is put into their current event
            //Event ID will be generated, hardcoded 0 is just a placeholder

            sharedPreferencesEditor.putString("Event ID", "96");
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Event Name", sName);
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Event Categories", sCategories);
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Event Description", sDescription);
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Event Time", sTime);
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Event Date", sDate);
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Event Location", sLocation);
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.putString("Total Spots", sSpotsTotal);
            sharedPreferencesEditor.apply();

            intent = new Intent(CreateEvent.this, CurrentEvent.class);
            startActivity(intent);
        }
        //leave this empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);            //Create Event Button
        Button btnLocation = (Button) findViewById(R.id.btnLocation);                  //Pick Location Button
        Button btnPickDate = (Button) findViewById(R.id.btnPickDate);                  //Pick Data Button
        eventName = (EditText) findViewById(R.id.tiEventName);                  //Event Name Text Input
        categories = (EditText) findViewById(R.id.tiCategories);                //Event Categories Text Input
        description = (EditText) findViewById(R.id.tiDescription);              //Event Description Text Input
        time = (EditText) findViewById(R.id.tiTime);                            //Time Text Input
        //location = (TextView) findViewById(R.id.tiLocation);                  //Location Text View
        location = (EditText) findViewById(R.id.tiLocation);                    //Location Text Input
        spotsTotal = (EditText) findViewById(R.id.tiSpotsTotal);                //Total Spots Text Input
        date = (TextView)findViewById(R.id.tvDate);                             //Date Text View
        date.setText(tempDate);                                                 //for if condition later

        btnLocation.setOnClickListener(v -> {
            intent = new Intent(CreateEvent.this, LocationPickerMap.class);
            startActivity(intent);
        });

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
                date.setText(eventDate);
            }
        };

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createEvent();

            }

        }); //end of CreateEvent button
    }

}
