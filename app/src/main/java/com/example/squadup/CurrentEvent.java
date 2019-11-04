package com.example.squadup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CurrentEvent extends AppCompatActivity {

    private String currentID;

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

        Button btnComplete = (Button) findViewById(R.id.btnComplete);
        TextView tvCName = (TextView) findViewById(R.id.tvCName);
        TextView tvCCategories = (TextView) findViewById(R.id.tvCCategories);
        TextView tvCDescription = (TextView) findViewById(R.id.tvCDescription);
        TextView tvCTime = (TextView) findViewById(R.id.tvCTime);
        TextView tvCDate = (TextView) findViewById(R.id.tvCDate);
        TextView tvCLocation = (TextView) findViewById(R.id.tvCLocation);
        TextView tvCTotalSpots = (TextView) findViewById(R.id.tvCTotalSpots);

        String currentName = sharedPreferences.getString("Event Name", defValue);
        String currentCategories = sharedPreferences.getString("Event Categories", defValue);
        String currentDescription = sharedPreferences.getString("Event Description", defValue);
        String currentTime = sharedPreferences.getString("Event Time", defValue);
        String currentDate = sharedPreferences.getString("Event Date", defValue);
        String currentLocation = sharedPreferences.getString("Event Location", defValue);
        String currentTotalSpots = sharedPreferences.getString("Total Spots", defValue);
        
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
