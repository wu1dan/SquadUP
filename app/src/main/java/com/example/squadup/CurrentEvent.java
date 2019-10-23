package com.example.squadup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CurrentEvent extends AppCompatActivity {

    String currentID, currentName, currentCategories, currentDescription, currentTime, currentDate, currentLocation, currentTotalSpots;
    TextView tvCName, tvCCategories, tvCDescription, tvCTime, tvCDate, tvCLocation, tvCTotalSpots;
    Button btnComplete;

    public void onBackPressed()
    {
        Intent intent = new Intent(CurrentEvent.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_event);

        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        
        String defValue = "defValue";
        
        //TODO: add time checker that automatically removes current event once the time has passed, the button will do for now
        
        btnComplete = (Button)findViewById(R.id.btnComplete);
        tvCName = (TextView)findViewById(R.id.tvCName);
        tvCCategories = (TextView)findViewById(R.id.tvCCategories);
        tvCDescription = (TextView)findViewById(R.id.tvCDescription);
        tvCTime = (TextView)findViewById(R.id.tvCTime);
        tvCDate = (TextView)findViewById(R.id.tvCDate);
        tvCLocation = (TextView)findViewById(R.id.tvCLocation);
        tvCTotalSpots = (TextView)findViewById(R.id.tvCTotalSpots);

        currentName = sharedPreferences.getString("Event Name", defValue);
        currentCategories = sharedPreferences.getString("Event Categories", defValue);
        currentDescription = sharedPreferences.getString("Event Description", defValue);
        currentTime = sharedPreferences.getString("Event Time", defValue);
        currentDate = sharedPreferences.getString("Event Date", defValue);
        currentLocation = sharedPreferences.getString("Event Location", defValue);
        currentTotalSpots = sharedPreferences.getString("Total Spots", defValue);
        
        tvCName.setText(currentName);
        tvCCategories.setText(currentCategories);
        tvCDescription.setText(currentDescription);
        tvCTime.setText(currentTime);
        tvCDate.setText(currentDate);
        tvCLocation.setText(currentLocation);
        tvCTotalSpots.setText(currentTotalSpots);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.editor.remove("Event ID");
                //MainActivity.editor.apply();
                sharedPreferencesEditor.remove("Event Name");
                sharedPreferencesEditor.apply();
                sharedPreferencesEditor.remove("Event Categories");
                sharedPreferencesEditor.apply();
                sharedPreferencesEditor.remove("Event Description");
                sharedPreferencesEditor.apply();
                sharedPreferencesEditor.remove("Event Time");
                sharedPreferencesEditor.apply();
                sharedPreferencesEditor.remove("Event Date");
                sharedPreferencesEditor.apply();
                sharedPreferencesEditor.remove("Event Location");
                sharedPreferencesEditor.apply();
                sharedPreferencesEditor.remove("Total Spots");
                sharedPreferencesEditor.apply();

                Intent intent = new Intent(CurrentEvent.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }


}
