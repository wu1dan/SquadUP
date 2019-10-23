package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateEvent extends AppCompatActivity{

    private Button btnCreateEvent, btnLocation, btnPickDate;
    TextView date; //location;
    private EditText eventName, categories, description, time, spotsTotal, location;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String sName, sCategories, sDescription, sTime, sLocation, sSpotsTotal, sDate;
    int totalSpots;

    String defValue = "defValue";

    /*
    ti prefix means "Text Input"
    tv prefix means "Text View"
    btn prefix means "button"
    s prefix means "String" (lets me re-use names such as Name, Categories, etc)
     */

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(CreateEvent.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        MainActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.editor = MainActivity.sharedPreferences.edit();
        MainActivity.editor.putString("Event ID", "0");
        MainActivity.editor.apply();

        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);            //Create Event Button
        btnLocation = (Button) findViewById(R.id.btnLocation);                  //Pick Location Button
        btnPickDate = (Button) findViewById(R.id.btnPickDate);                  //Pick Data Button
        eventName = (EditText) findViewById(R.id.tiEventName);                  //Event Name Text Input
        categories = (EditText) findViewById(R.id.tiCategories);                //Event Categories Text Input
        description = (EditText) findViewById(R.id.tiDescription);              //Event Description Text Input
        time = (EditText) findViewById(R.id.tiTime);                            //Time Text Input
        //location = (TextView) findViewById(R.id.tiLocation);                  //Location Text View
        location = (EditText) findViewById(R.id.tiLocation);                    //Location Text Input
        spotsTotal = (EditText) findViewById(R.id.tiSpotsTotal);                //Total Spots Text Input

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEvent.this, LocationPickerMap.class);
                startActivity(intent);
            }
        });

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog eventDatePicker = new DatePickerDialog(CreateEvent.this, android.R.style.Theme_Black,
                        mDateSetListener, year, month, day);
                eventDatePicker.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1; //january is 0
                String eventDate = month + "/" + dayOfMonth + "/" + year;
                date = (TextView)findViewById(R.id.tvDate);
                date.setText(eventDate);
            }
        };

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sName = eventName.getText().toString();
                 sCategories = categories.getText().toString();
                 sDescription = description.getText().toString();
                 sTime = time.getText().toString();
                 sLocation = location.getText().toString();
                 sSpotsTotal = spotsTotal.getText().toString();
                 sDate = date.getText().toString();

                 String[] aCategories = sCategories.split("\\W"); //turns the string of categories into an array that splits categories by non-words (ie spaces, commas, etc)

                if(!MainActivity.sharedPreferences.getString("Event Name", defValue).equals(defValue)){ //if it doesn't equal defValue that means they're already in an actual event
                    Toast.makeText(CreateEvent.this, "You are already in an event and may not create one!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateEvent.this, MainActivity.class);

                }else if(!MainActivity.sharedPreferences.getString("Pending Event", defValue).equals(defValue)) { //if it doesn't equal defValue that means they DO have a pending event
                    Toast.makeText(CreateEvent.this, "Please reject your current pending event before creating a new one.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateEvent.this, PendingEvent.class);

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

                    if (sSpotsTotal.length() == 0 || Integer.valueOf(sSpotsTotal) == null) { //the null thing doesn't actually work, need to catch NumberFormatException
                        Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (Integer.valueOf(sSpotsTotal) < 2) {
                        Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
                        return;
                    } else
                        totalSpots = Integer.valueOf(sSpotsTotal);             //converts total spot string into integers

                    Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_LONG).show();

                    //use this space to assign event with all its above parameters into the database



                    //Updating shared preferences so that this event they just made is put into their current event
                    //Event ID will be generated, hardcoded 0 is just a placeholder

                    MainActivity.editor.putString("Event ID", "96");
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Event Name", sName);
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Event Categories", sCategories);
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Event Description", sDescription);
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Event Time", sTime);
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Event Date", sDate);
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Event Location", sLocation);
                    MainActivity.editor.apply();
                    MainActivity.editor.putString("Total Spots", sSpotsTotal);
                    MainActivity.editor.apply();

                    Intent intent = new Intent(CreateEvent.this, CurrentEvent.class);
                    startActivity(intent);
                }
                //leave this empty
            }

        }); //end of CreateEvent button
    }

}
