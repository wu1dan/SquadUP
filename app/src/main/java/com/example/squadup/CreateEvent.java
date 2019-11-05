package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.*;

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

    private String sName;
    private String sCategories;
    private String sDescription;
    private String sTime;
    private String sLocation;
    private String sSpotsTotal;
    private String sDate;
   // private String[] aCategories;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("Event ID", "0");
        sharedPreferencesEditor.apply();

        sName = eventName.getText().toString();
        sCategories = categories.getText().toString();
        sDescription = description.getText().toString();
        sTime = time.getText().toString();
        sLocation = location.getText().toString();
        sSpotsTotal = spotsTotal.getText().toString();
        sDate = date.getText().toString();

        Boolean areParamsFilled;


      //  aCategories = sCategories.split("\\W"); //turns the string of categories into an array that splits categories by non-words (ie spaces, commas, etc)

        String defValue = "defValue";
        if(!sharedPreferences.getString("Event Name", defValue).equals(defValue)){ //if it doesn't equal defValue that means they're already in an actual event
            Toast.makeText(CreateEvent.this, "You are already in an event and may not create one!", Toast.LENGTH_SHORT).show();
            intent = new Intent(CreateEvent.this, MainActivity.class);

        }else if(!sharedPreferences.getString("Pending Event", defValue).equals(defValue)) { //if it doesn't equal defValue that means they DO have a pending event
            Toast.makeText(CreateEvent.this, "Please reject your current pending event before creating a new one.", Toast.LENGTH_SHORT).show();
            intent = new Intent(CreateEvent.this, PendingEvent.class);

        }else{
            areParamsFilled = checkAllInputs();

            if(!areParamsFilled)
                return;

            Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_LONG).show();

            //use this space to assign event with all its above parameters into the database


            //Updating shared preferences so that this event they just made is put into their current event
            //Event ID will be generated, hardcoded 0 is just a placeholder

            updateSharedPrefs();

            intent = new Intent(CreateEvent.this, CurrentEvent.class);
            startActivity(intent);
        }
        //leave this empty
    }

    protected Boolean checkAllInputs(){
        if (sName.length() == 0) {                   //ERROR MESSAGES IF MISSING INFORMATION OR VERIFIED DOES NOT MATCH ORIGINAL
            Toast.makeText(CreateEvent.this, "Please enter a valid event name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sCategories.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter at least one relevant event category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sDescription.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter an event description", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sTime.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sLocation.length() == 0) {
            Toast.makeText(CreateEvent.this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(sDate.equals(tempDate)){
            Toast.makeText(CreateEvent.this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sSpotsTotal.length() == 0 || Integer.valueOf(sSpotsTotal) == null || Integer.valueOf(sSpotsTotal) < 2) { //the null thing doesn't actually work, need to catch NumberFormatException
            Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected void updateSharedPrefs(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("Event ID", "0");
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);            //Create Event Button
        Button btnLocation = (Button) findViewById(R.id.btnLocation);                  //Pick Location Button
        btnLocation.setEnabled(false);
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
