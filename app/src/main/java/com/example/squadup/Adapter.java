package com.example.squadup;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.squadup.MainActivity.sharedPreferences;


public class Adapter extends BaseAdapter implements Filterable {

    Context context;
    private String listTitles[] = {"Badminton", "Basketball", "Beach Volleyball",  "Beer Tasting", "Bicycling", "Billiards", "Board Games", "Bouldering", "Bowling", "Chess", "Chilling", "Clubbing", "Coffee", "Concerts", "Cooking", "Dancing", "Drawing", "Fast Food", "Fishing", "Foodie", "Golf", "Guitar", "Gyming", "Hiking", "Jam Session", "Knitting", "Movies", "Partying", "Pet Play Dates", "Photography", "Piano", "Picnics", "Potlucks", "Raving", "Reading", "Road Trips", "Rock Climbing", "Running", "Shopping", "Singing", "Skateboarding", "Skating", "Skiing", "Snowboarding", "Soccer", "Spikeball", "Studying", "Swimming", "Tennis", "Theatre", "Thrifting", "Video Games"};
    ArrayList<Interest> titles;
    CustomFilter filter;
    ArrayList<Interest> filterList;
    private Set<String> userInterests = new HashSet<>();


    public Adapter(Context context, ArrayList<Interest> titles, Set<String> userInterests) {
        this.context = context;
        this.titles = titles;
        this.filterList = titles;
        this.userInterests = userInterests;

    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return titles.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_interests, null);
        }

        SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = sharedPreferences.edit();


        final Switch interestSwitch = convertView.findViewById(R.id.switchInterest);
        if (userInterests.contains(titles.get(position).getTitle())) {
            if (!interestSwitch.isChecked()){
                interestSwitch.setChecked(true);
            }
        }

        else if (!userInterests.contains(titles.get(position).getTitle()) && interestSwitch.isChecked()){
            interestSwitch.setChecked(false);
        }

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

        TextView name = convertView.findViewById(R.id.tvInterestTitle);
        ImageView imageview = convertView.findViewById(R.id.imgInterest);

        name.setText(titles.get(position).getTitle());

        Glide.with(context)
                .load(titles.get(position).getImage())
                .into(imageview);

        return convertView;

    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0){
                constraint = constraint.toString().toLowerCase();

                ArrayList<Interest> filteredTitles = new ArrayList<Interest>();

                for(int i = 0; i < filterList.size(); i++){
                    if(filterList.get(i).getTitle().toLowerCase().contains(constraint)){
                        Interest interest = new Interest(filterList.get(i).getTitle(), filterList.get(i).getImage());
                        filteredTitles.add(interest);
                    }
                }
                filterResults.count = filteredTitles.size();
                filterResults.values = filteredTitles;
            }
            else{
                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            titles = (ArrayList<Interest>) results.values;
            notifyDataSetChanged();
        }
    }
}
