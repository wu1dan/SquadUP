package com.example.squadup;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class InterestsBrowserFragment extends Fragment {
/*
    private ArrayAdapter arrayAdapter;

    String listTitles[] = {"Badminton", "Basketball", "Beach Volleyball",  "Beer Tasting", "Bicycling", "Billiards", "Board Games", "Bouldering", "Bowling", "Chess", "Chilling", "Clubbing", "Coffee", "Concerts", "Cooking", "Dancing", "Drawing", "Fast Food", "Fishing", "Foodie", "Golf", "Guitar", "Gyming", "Hiking", "Jam Session", "Knitting", "Movie", "Partying", "Pet Play Dates", "Photography", "Piano", "Picnics", "Potlucks", "Raving", "Reading", "Road Trips", "Rock Climbing", "Running", "Shopping", "Singing", "Skateboarding", "Skating", "Skiing", "Snowboarding", "Soccer", "Spikeball", "Studying", "Swimming", "Tennis", "Theatre", "Thrifting", "Video Games"};
    int listImages[]= {R.drawable.badminton, R.drawable.basketball, R.drawable.beachvolleyball, R.drawable.beer, R.drawable.bicycling, R.drawable.billiards, R.drawable.boardgames, R.drawable.bouldering, R.drawable.bowling, R.drawable.chess, R.drawable.chilling, R.drawable.clubbing, R.drawable.coffee, R.drawable.concert, R.drawable.cooking, R.drawable.dancing, R.drawable.drawing, R.drawable.fastfood, R.drawable.fishing, R.drawable.foodie, R.drawable.golf, R.drawable.guitar, R.drawable.gym, R.drawable.hiking, R.drawable.jamsession, R.drawable.knitting, R.drawable.movie, R.drawable.partying, R.drawable.petplaydate, R.drawable.photography, R.drawable.piano, R.drawable.picnic, R.drawable.potluck, R.drawable.raving, R.drawable.reading, R.drawable.roadtrip, R.drawable.rockclimbing, R.drawable.running, R.drawable.shopping, R.drawable.singing, R.drawable.skateboarding, R.drawable.skating, R.drawable.skiing, R.drawable.snowboarding, R.drawable.soccer, R.drawable.spikeball, R.drawable.studying, R.drawable.swimming, R.drawable.tennis, R.drawable.theatre, R.drawable.thrifting, R.drawable.videogames};

    public InterestsBrowserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interests_browser, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor sharedPreferencesEditor;

        Set setInterests = new HashSet();

        setInterests = sharedPreferences.getStringSet("Interests", new HashSet<String>());

        EditText txtSearchInterests;
        txtSearchInterests = getActivity().findViewById(R.id.txtSearchInterests);

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.list_interests, listTitles);

        txtSearchInterests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (InterestsBrowserFragment.this).arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ListView listViewInterests = getActivity().findViewById(R.id.listInterests);
        listViewInterests.setAdapter(new BaseAdapter() {
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
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_interests, null);

                ImageView imgInterest = view.findViewById(R.id.imgInterest);
                TextView tvInterestTitle = view.findViewById(R.id.tvInterestTitle);
                Glide.with(getActivity())
                        .load(listImages[position])
                        .into(imgInterest);

                tvInterestTitle.setText(listTitles[position]);
                return view;
            }
        });

        listViewInterests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferencesEditor.putStringSet("Interests", )
            }
        })
    }
    */
}
