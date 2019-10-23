package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Interests extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private ArrayAdapter arrayAdapter;
    final String listTitles[] = {"Badminton", "Basketball", "Beach Volleyball",  "Beer Tasting", "Bicycling", "Billiards", "Board Games", "Bouldering", "Bowling", "Chess", "Chilling", "Clubbing", "Coffee", "Concerts", "Cooking", "Dancing", "Drawing", "Fast Food", "Fishing", "Foodie", "Golf", "Guitar", "Gyming", "Hiking", "Jam Session", "Knitting", "Movie", "Partying", "Pet Play Dates", "Photography", "Piano", "Picnics", "Potlucks", "Raving", "Reading", "Road Trips", "Rock Climbing", "Running", "Shopping", "Singing", "Skateboarding", "Skating", "Skiing", "Snowboarding", "Soccer", "Spikeball", "Studying", "Swimming", "Tennis", "Theatre", "Thrifting", "Video Games"};
    int listImages[]= {R.drawable.badminton, R.drawable.basketball, R.drawable.beachvolleyball, R.drawable.beer, R.drawable.bicycling, R.drawable.billiards, R.drawable.boardgames, R.drawable.bouldering, R.drawable.bowling, R.drawable.chess, R.drawable.chilling, R.drawable.clubbing, R.drawable.coffee, R.drawable.concert, R.drawable.cooking, R.drawable.dancing, R.drawable.drawing, R.drawable.fastfood, R.drawable.fishing, R.drawable.foodie, R.drawable.golf, R.drawable.guitar, R.drawable.gym, R.drawable.hiking, R.drawable.jamsession, R.drawable.knitting, R.drawable.movie, R.drawable.partying, R.drawable.petplaydate, R.drawable.photography, R.drawable.piano, R.drawable.picnic, R.drawable.potluck, R.drawable.raving, R.drawable.reading, R.drawable.roadtrip, R.drawable.rockclimbing, R.drawable.running, R.drawable.shopping, R.drawable.singing, R.drawable.skateboarding, R.drawable.skating, R.drawable.skiing, R.drawable.snowboarding, R.drawable.soccer, R.drawable.spikeball, R.drawable.studying, R.drawable.swimming, R.drawable.tennis, R.drawable.theatre, R.drawable.thrifting, R.drawable.videogames};
    ArrayList filterList = new ArrayList<String>();
    ArrayList filterImages = new ArrayList<Integer>();
    String filteredInterest;
    int filteredImage;

    Context context = Interests.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        final ListView listInterests;
        final EditText txtSearchInterests;
        final ListAdapter listAdapter = new ListAdapter();
        listInterests = findViewById(R.id.listInterests);
        listInterests.setAdapter(listAdapter);

        final SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = sharedPreferences.edit();
        final Set<String> updatedInterests;
        updatedInterests= sharedPreferences.getStringSet("Interests", null);


        txtSearchInterests = findViewById(R.id.txtSearchInterests);
        txtSearchInterests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*String counter = txtSearchInterests.getText().toString();
                if (counter.length() == 0){
                    filterList.clear();
                    filterImages.clear();
                    Collections.addAll(filterList, listTitles);
                    Collections.addAll(filterImages, listImages);
                }*/
                    listAdapter.Filter(txtSearchInterests.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button btnSaveInterests = findViewById(R.id.btnSaveInterests);
        btnSaveInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesEditor.putStringSet("Interests", updatedInterests);
                sharedPreferencesEditor.apply();
                Intent intent = new Intent(Interests.this, Profile.class);
                startActivity(intent);
            }
        });


    }

    class ListAdapter extends BaseAdapter {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.list_interests,null);

            final SharedPreferences sharedPreferences;
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor sharedPreferencesEditor;
            sharedPreferencesEditor = sharedPreferences.edit();

            Set<String> userInterests;
            userInterests = sharedPreferences.getStringSet("Interests", null);

            final Switch interestSwitch = view.findViewById(R.id.switchInterest);

            if (userInterests.contains(listTitles[position])) {
                if (!interestSwitch.isChecked()){
                    interestSwitch.setChecked(true);
                }
            }

            else if (!userInterests.contains(listTitles[position])){
                if (interestSwitch.isChecked()){
                    interestSwitch.setChecked(false);
                }
            }

            ImageView imgInterest = view.findViewById(R.id.imgInterest);
            TextView tvInterestTitle = view.findViewById(R.id.tvInterestTitle);
            Glide.with(context)
                    .load(listImages[position])
                    .into(imgInterest);

            tvInterestTitle.setText(listTitles[position]);

            interestSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Set<String> updatedInterests;
                    updatedInterests = sharedPreferences.getStringSet("Interests", null);
                    if(interestSwitch.isChecked()){
                        updatedInterests.add(listTitles[position]);
                    }
                    else if (!interestSwitch.isChecked()){
                        if(updatedInterests.contains(listTitles[position])){
                            updatedInterests.remove(listTitles[position]);
                        }
                    }
                }
            });

            return view;
        }

        public void Filter(String search ){
            /*search = search.toLowerCase();
            if(search.length() == 0){
                Collections.addAll(filterList, listTitles);
                Collections.addAll(filterImages, listImages);
            }
            else{
                for(int i = 0; i < listTitles.length; i++){
                    filteredInterest = listTitles[i];
                    filteredImage = listImages[i];

                    if (filteredInterest.contains(search)){
                        filterList.add(filteredInterest);
                        filterImages.add(filteredImage);
                    }
                }
            }*/
            notifyDataSetChanged();
        }
    }

}
