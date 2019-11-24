package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        btnYes.setEnabled(false);
        btnNo.setEnabled(false);

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

        String defValue = "";

        if (sharedPreferences.getString("tempID", defValue).equals(defValue)) { //they have a pending event

            btnYes.setEnabled(true);
            btnNo.setEnabled(true);

            //Have code here for using the event id to extract all the event information out of the json
            //Here's some placeholder for now:

            tempID = "96";
            tempName = "Pick-up Basketball";
            tempCategories = "Sports, Basketball";
            tempDescription = "Just some classic court-side comraderie with the lads (boys only!!!!!!!)";
            tempTime = "3:00 pm";
            tempDate = "October 23rd 2019";
            tempLocation = "SRC";
            tempTotalSpots = "6 (5 left!)";

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
}
