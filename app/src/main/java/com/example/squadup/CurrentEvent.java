package com.example.squadup;

import android.content.Intent;
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

        MainActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.editor = MainActivity.sharedPreferences.edit();
        
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

        currentName = MainActivity.sharedPreferences.getString("Event Name", defValue);
        currentCategories = MainActivity.sharedPreferences.getString("Event Categories", defValue);
        currentDescription = MainActivity.sharedPreferences.getString("Event Description", defValue);
        currentTime = MainActivity.sharedPreferences.getString("Event Time", defValue);
        currentDate = MainActivity.sharedPreferences.getString("Event Date", defValue);
        currentLocation = MainActivity.sharedPreferences.getString("Event Location", defValue);
        currentTotalSpots = MainActivity.sharedPreferences.getString("Total Spots", defValue);
        
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
                MainActivity.editor.remove("Event Name");
                MainActivity.editor.apply();
                MainActivity.editor.remove("Event Categories");
                MainActivity.editor.apply();
                MainActivity.editor.remove("Event Description");
                MainActivity.editor.apply();
                MainActivity.editor.remove("Event Time");
                MainActivity.editor.apply();
                MainActivity.editor.remove("Event Date");
                MainActivity.editor.apply();
                MainActivity.editor.remove("Event Location");
                MainActivity.editor.apply();
                MainActivity.editor.remove("Total Spots");
                MainActivity.editor.apply();

                Intent intent = new Intent(CurrentEvent.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }


}
