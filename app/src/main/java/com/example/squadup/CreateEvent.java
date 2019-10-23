package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

import org.json.JSONObject;

public class CreateEvent extends AppCompatActivity {

    private Button btnCreateEvent;
    private EditText eventName, categories, description, time, location, spotsTotal;
    private CheckBox cbAM, cbPM;
    JSONObject jsonObject;

    String sName, sCategories, sDescription, sTime, sLocation, sSpotsTotal;
    int totalSpots;


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

        btnCreateEvent = (Button)findViewById(R.id.btnCreateEvent);            //Create Event Button
        eventName = (EditText)findViewById(R.id.tiEventName);                  //Event Name Text Input
        categories = (EditText)findViewById(R.id.tiCategories);                //Event Categories Text Input
        categories = (EditText)findViewById(R.id.tiDescription);               //Event Description Text Input
        time = (EditText)findViewById(R.id.tiTime);                            //Time Text Input
        location = (EditText)findViewById(R.id.tiLocation);                    //Location Text Input
        spotsTotal = (EditText)findViewById(R.id.tiSpotsTotal);                //Total Spots Text Input

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sName = eventName.getText().toString();
                 sCategories = categories.getText().toString();
                 sTime = time.getText().toString();
                 sLocation = location.getText().toString();
                 sSpotsTotal = spotsTotal.getText().toString();

                 String[] aCategories = sCategories.split("\\W"); //turns the string of categories into an array that splits categories by non-words (ie spaces, commas, etc)
                 totalSpots = Integer.valueOf(sSpotsTotal);             //converts total spot string into integers


                if (sName.length() == 0){                   //ERROR MESSAGES IF MISSING INFORMATION OR VERIFIED DOES NOT MATCH ORIGINAL
                    Toast.makeText(CreateEvent.this, "Please enter a valid event name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sCategories.length() == 0){
                    Toast.makeText(CreateEvent.this, "Please enter at least one relevant event category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sDescription.length() == 0){
                    Toast.makeText(CreateEvent.this, "Please enter an event description", Toast.LENGTH_SHORT).show();
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

                //use this space to assign event with all its above parameters into the database

            }
        }); //end of CreateEvent button
    }
}
