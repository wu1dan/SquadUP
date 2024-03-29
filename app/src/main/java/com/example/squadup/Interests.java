package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.squadup.MainActivity.sharedPreferences;

public class Interests extends AppCompatActivity {
    private String listTitles[] = {"Badminton", "Basketball", "Beach Volleyball",  "Beer Tasting", "Bicycling", "Billiards", "Board Games", "Bouldering", "Bowling", "Chess", "Chilling", "Clubbing", "Coffee", "Concerts", "Cooking", "Dancing", "Drawing", "Fast Food", "Fishing", "Foodie", "Golf", "Guitar", "Gyming", "Hiking", "Jam Session", "Knitting", "Movies", "Partying", "Pet Play Dates", "Photography", "Piano", "Picnics", "Potlucks", "Raving", "Reading", "Road Trips", "Rock Climbing", "Running", "Shopping", "Singing", "Skateboarding", "Skating", "Skiing", "Snowboarding", "Soccer", "Spikeball", "Studying", "Swimming", "Tennis", "Theatre", "Thrifting", "Video Games"};
    private int listImages[]= {R.drawable.badminton, R.drawable.basketball, R.drawable.beachvolleyball, R.drawable.beer, R.drawable.bicycling, R.drawable.billiards, R.drawable.boardgames, R.drawable.bouldering, R.drawable.bowling, R.drawable.chess, R.drawable.chilling, R.drawable.clubbing, R.drawable.coffee, R.drawable.concert, R.drawable.cooking, R.drawable.dancing, R.drawable.drawing, R.drawable.fastfood, R.drawable.fishing, R.drawable.foodie, R.drawable.golf, R.drawable.guitar, R.drawable.gym, R.drawable.hiking, R.drawable.jamsession, R.drawable.knitting, R.drawable.movie, R.drawable.partying, R.drawable.petplaydate, R.drawable.photography, R.drawable.piano, R.drawable.picnic, R.drawable.potluck, R.drawable.raving, R.drawable.reading, R.drawable.roadtrip, R.drawable.rockclimbing, R.drawable.running, R.drawable.shopping, R.drawable.singing, R.drawable.skateboarding, R.drawable.skating, R.drawable.skiing, R.drawable.snowboarding, R.drawable.soccer, R.drawable.spikeball, R.drawable.studying, R.drawable.swimming, R.drawable.tennis, R.drawable.theatre, R.drawable.thrifting, R.drawable.videogames};
    //private List<String> filterInterestTitles = Arrays.asList(listTitles);


    private Context context = Interests.this;

    private Set<String> userInterests = new HashSet<>();

    @Override
    public void onBackPressed() {

        if (sharedPreferences.getStringSet("Interests",null).size() == 0){
            Toast.makeText(Interests.this, "Please select at least 1 interest to get started.", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.apply();

        Intent intent = new Intent(Interests.this, Profile.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        final SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = sharedPreferences.edit();

        final ListView listInterests;
        final ListAdapter listAdapter = new ListAdapter();
        listInterests = findViewById(R.id.listInterests);

        if (sharedPreferences.contains("Interests")) {
            userInterests.addAll(sharedPreferences.getStringSet("Interests", null));
        }
        else {
            Set<String> emptySet = new HashSet<>();
            sharedPreferencesEditor.putStringSet("Interests", emptySet);
            sharedPreferencesEditor.apply();
        }

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextSubmit = findViewById(R.id.etSubmit);
                if (sharedPreferences.contains("Submission")) {
                    Set<String> Submission = sharedPreferences.getStringSet("Submission", null);
                    Submission.add(editTextSubmit.getText().toString());
                    sharedPreferencesEditor.putStringSet("Submission", Submission);
                    sharedPreferencesEditor.apply();
                }
                else{
                    Set<String> Submission = new HashSet<String>();
                    Submission.add(editTextSubmit.getText().toString());
                    sharedPreferencesEditor.putStringSet("Submission", Submission);
                    sharedPreferencesEditor.apply();
                }
            }
        });


        listInterests.setAdapter(listAdapter);

    }


    class ListAdapter extends BaseAdapter implements Filterable{
        @Override
        public int getCount() {
            return listTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Filter getFilter(){
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    ArrayList<String> filteredInterests = new ArrayList<>();

                    String formattedConstraint = constraint.toString().toLowerCase().trim();
                    for(int i = 0; i < listTitles.length; i++){
                        String stringInterest = listTitles[i];
                        if (stringInterest.toLowerCase().startsWith(formattedConstraint)){
                            filteredInterests.add(stringInterest);
                        }
                    }
                    filterResults.count = filteredInterests.size();
                    filterResults.values = filteredInterests;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    //ArrayList filterList = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.list_interests,null);

            SharedPreferences.Editor sharedPreferencesEditor;
            sharedPreferencesEditor = sharedPreferences.edit();

            final Switch interestSwitch = view.findViewById(R.id.switchInterest);
            if (userInterests.contains(listTitles[position])) {
                if (!interestSwitch.isChecked()){
                    interestSwitch.setChecked(true);
                }
            }

            else if (!userInterests.contains(listTitles[position]) && interestSwitch.isChecked()){
                interestSwitch.setChecked(false);
            }

            ImageView imgInterest = view.findViewById(R.id.imgInterest);
            TextView tvInterestTitle = view.findViewById(R.id.tvInterestTitle);
            Glide.with(context)
                    .load(listImages[position])
                    .into(imgInterest);

            tvInterestTitle.setText(listTitles[position]);

            interestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        userInterests.add(listTitles[position]);
                        sharedPreferencesEditor.putStringSet("Interests", userInterests);
                        sharedPreferencesEditor.apply();
                    }
                    if(!isChecked) {
                        userInterests.remove(listTitles[position]);
                        sharedPreferencesEditor.putStringSet("Interests", userInterests);
                        sharedPreferencesEditor.apply();
                    }
                }
            });

            return view;
        }

    }

    public List<String> filterInterests(List<String> interestTitles, String searchParam){
        List<String> filteredInterests = new ArrayList<>();
        for (String title:interestTitles){
            if(title.contains(searchParam)){
                filteredInterests.add(title);
            }
        }
        return filteredInterests;
    }

}