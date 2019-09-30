package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

public class CreateEvent extends AppCompatActivity {

    private Button btnCreateEvent;
    private EditText EventName, Categories, Time, Location, SpotsTotal, SpotsAvailable;
    private CheckBox cbAM, cbPM;

    String sName, sCategories, sTime, sLocation, sSpotsTotal, sSpotsAvailable;
    int totalSpots, availableSpots;


    /*
    ti prefix means "Text Input"
    cb prefix means "Check Box"
    s prefix means "String" (lets me re-use names such as Name, Categories, etc)
     */

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(CreateEvent.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);            //Create Event Button
        EventName = (EditText) findViewById(R.id.tiEventName);                  //Event Name Text Input
        Categories = (EditText) findViewById(R.id.tiCategories);                //Event Categories Text Input
        Time = (EditText) findViewById(R.id.tiTime);                            //Time Text Input
        Location = (EditText) findViewById(R.id.tiLocation);                    //Location Text Input
        SpotsTotal = (EditText) findViewById(R.id.tiSpotsTotal);                //Total Spots Text Input
        SpotsAvailable = (EditText) findViewById(R.id.tiSpotsAvailable);        //Available Spots Text Input

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sName = EventName.getText().toString();
                 sCategories = Categories.getText().toString();
                 sTime = Time.getText().toString();
                 sLocation = Location.getText().toString();
                 sSpotsTotal = SpotsTotal.getText().toString();
                 sSpotsAvailable = SpotsAvailable.getText().toString();

                 String[] aCategories = sCategories.split("\\W"); //turns the string of categories into an array that splits categories by non-words (ie spaces, commas, etc)
                 totalSpots = Integer.valueOf(sSpotsTotal);
                 availableSpots = Integer.valueOf(sSpotsAvailable); //converts total and available spot strings into integers


                if (sName.length() == 0){                   //ERROR MESSAGES IF MISSING INFORMATION OR VERIFIED DOES NOT MATCH ORIGINAL
                    Toast.makeText(CreateEvent.this, "Please enter a valid event name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sCategories.length() == 0){
                    Toast.makeText(CreateEvent.this, "Please enter at least one relevant event category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sTime.length() == 0){
                    Toast.makeText(CreateEvent.this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sLocation.length() == 0){
                    Toast.makeText(CreateEvent.this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (totalSpots < 2){
                    Toast.makeText(CreateEvent.this, "Please allow at least 2 total spots", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (availableSpots == 0 || availableSpots > totalSpots){
                    Toast.makeText(CreateEvent.this, "Please allow at least one available spot", Toast.LENGTH_SHORT).show();
                    return;
                }

                //use this space to assign event with all its above parameters into the database

            }
        }); //end of CreateEvent button
    }
}
