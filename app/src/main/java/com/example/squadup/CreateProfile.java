package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import static com.example.squadup.MainActivity.sharedPreferences;

public class CreateProfile extends AppCompatActivity {

    private TextView tvDateofBirth;

    private String Date = "69";
    private String spinnerGender[] = {"", "Male", "Female", "Other", "Rather not say"};
    private LocalDate currentDate = LocalDate.now();
    private int currentYear = currentDate.getYear();
    private int currentMonth = currentDate.getMonthValue();
    private int currentDay = currentDate.getDayOfMonth();
    private Uri uriImage;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private int Image = 1;
    @Override
    public void onBackPressed() {
        if(sharedPreferences.contains("Interests")) {
            if (sharedPreferences.contains("id")) {
                putJSON();
                Intent intent = new Intent(CreateProfile.this, Profile.class);
                startActivity(intent);
            } else {
                postJSON();
                Intent intent = new Intent(CreateProfile.this, Profile.class);
                startActivity(intent);
            }
        }
        Intent intent = new Intent(CreateProfile.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createprofile);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        Spinner spinGender = findViewById(R.id.spinnerGender);
        spinGender.setPrompt("Gender");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateProfile.this,
                android.R.layout.simple_list_item_1, spinnerGender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(arrayAdapter);

        ImageView imgProfilePicture = findViewById(R.id.imgProfilePicture);
        String profilePictureURL = sharedPreferences.getString("ProfilePicture", "");
        Uri pfp = Uri.parse(profilePictureURL);

        Glide
                .with(CreateProfile.this)
                .load(pfp)
                .into(imgProfilePicture);



        calendar();

        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges();
                String Gender = spinGender.getSelectedItem().toString();

                if (!"".equals(Gender)) {
                    sharedPreferencesEditor.putString("Gender", Gender);
                    sharedPreferencesEditor.apply();
                }

                if (!sharedPreferences.contains("Gender")) {
                    Toast.makeText(CreateProfile.this, "Please fill out the Gender field.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sharedPreferences.contains("Interests")) {
                    if (sharedPreferences.contains("id")) {
                        putJSON();
                        Intent intent = new Intent(CreateProfile.this, Profile.class);
                        startActivity(intent);
                    } else {
                        postJSON();
                        Intent intent = new Intent(CreateProfile.this, Profile.class);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(CreateProfile.this, "Please choose some interests to get started!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateProfile.this, Interests.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        /*Button btnEditProfilePicture = findViewById(R.id.btnEditProfilePicture);
        btnEditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });*/

    }

    /*private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, Image);
    }*/

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 69 && requestCode == Image) {
            uriImage = data.getData();
            ImageView imgProfilePicture = findViewById(R.id.imgProfilePicture);
            imgProfilePicture.setImageURI(uriImage);
            sharedPreferencesEditor.putString("ProfilePicture", uriImage.toString());
            sharedPreferencesEditor.apply();
        }
    }*/

    /*public void okhttpGetRequest(){

        OkHttpClient client = new OkHttpClient();

        String url = "http://20.43.19.13:3000/Users";

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    final String sresponse = response.body().string();

                    CreateProfile.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });

    }*/

    /*public void getJSON() {
            RequestQueue queue = Volley.newRequestQueue(CreateProfile.this);
            final String url = "http://20.43.19.13:3000/Users";
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                        TextView textView69 = findViewById(R.id.textView69);
                        Toast.makeText(getApplication(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray Users = response.getJSONArray("Users");
                            for (int i = 0; i < Users.length(); i++){

                                org.json.JSONObject user = Users.getJSONObject(i);
                                String fName = user.getString("FirstName)");
                                String lName = user.getString("LastName");
                                String email = user.getString("Email");

                                textView69.append(fName + "," + lName + "," + email + "\n");

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
                //Empty
            });

            queue.add(getRequest);
    }*/

        public void putJSON() {
        RequestQueue queue = Volley.newRequestQueue(CreateProfile.this);
        try {
            String id = sharedPreferences.getString("id", "");
            String url = "http://20.43.19.13:3000/Users/5ddb734416976566b576b2d8";
            JSONObject userJSON = new JSONObject();
            userJSON.put("id", sharedPreferences.getString("id", ""));
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            Set<String> setInterests = sharedPreferences.getStringSet("Interests", null);
            JSONArray interests = new JSONArray();
            for (String interest : setInterests){
                interests.put(interest);
            }
            userJSON.put("Interests", interests);


            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(CreateProfile.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //yeet
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            queue.add(putRequest);


        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private void calendar(){
        Button btnDateofBirth = findViewById(R.id.btnDateofBirth);
        btnDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dateofbirthpicker = new DatePickerDialog(CreateProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        int monthCorrection = month + 1;
                        String sday = Integer.toString(day);
                        String smonth = Integer.toString(month);
                        String syear = Integer.toString(year);

                        if (currentYear - year <= 18){
                            Toast.makeText(CreateProfile.this, "You must be 18 to use squadUP.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentYear - year == 18) {
                            if (currentMonth - monthCorrection == 0) {
                                if (currentDay - day < 0) {
                                    Toast.makeText(CreateProfile.this, "You must be 18 to use squadUP.", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (currentDay - day >= 0) {
                                    Date = (smonth + "/" + sday + "/" + syear);
                                    tvDateofBirth = findViewById(R.id.tvDateofBirth);
                                    tvDateofBirth.setText(Date);
                                }
                            }
                            if (currentMonth - monthCorrection > 0) {
                                Date = (smonth + "/" + sday + "/" + syear);
                                tvDateofBirth = findViewById(R.id.tvDateofBirth);
                                tvDateofBirth.setText(Date);
                            } else if (currentMonth - monthCorrection < 0) {
                                Toast.makeText(CreateProfile.this, "You must be 18 to use squadUP.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Date = (smonth + "/" + sday + "/" + syear);
                            tvDateofBirth = findViewById(R.id.tvDateofBirth);
                            tvDateofBirth.setText(Date);
                        }
                    }
                }, day, month, year);
                dateofbirthpicker.show();
            }

        });
    }

    private void saveChanges(){

        TextView tbFirstName = findViewById(R.id.tbFirstName);
        String firstName = tbFirstName.getText().toString();
        TextView tbLastName = findViewById(R.id.tbLastName);
        String lastName = tbLastName.getText().toString();
        TextView tbEmail = findViewById(R.id.tbEmail);
        String Email = tbEmail.getText().toString();
        sharedPreferencesEditor = sharedPreferences.edit();

        changesCheck(firstName, lastName, Email);
        changesReplace();

    }

    public void changesCheck(String firstName, String lastName, String Email){
        if (!"".equals(firstName)) {
            sharedPreferencesEditor.putString("FirstName", firstName);
            sharedPreferencesEditor.apply();
        }
        if (!"".equals(lastName)) {
            sharedPreferencesEditor.putString("LastName", lastName);
            sharedPreferencesEditor.apply();
        }
        if (!"".equals(Email)) {
            sharedPreferencesEditor.putString("Email", Email);
            sharedPreferencesEditor.apply();
        }
        if (!"69".equals(Date)) {
            sharedPreferencesEditor.putString("DateofBirth", Date);
            sharedPreferencesEditor.apply();
        }
    }

    public void changesReplace(){
        if (!sharedPreferences.contains("FirstName")) {
            Toast.makeText(CreateProfile.this, "Please fill in a valid First Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sharedPreferences.contains("LastName")) {
            Toast.makeText(CreateProfile.this, "Please fill in a valid Last Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sharedPreferences.contains("Email")) {
            Toast.makeText(CreateProfile.this, "Please fill in a valid Email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sharedPreferences.contains("DateofBirth")) {
            Toast.makeText(CreateProfile.this, "Please fill in a valid Date of Birth .", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!sharedPreferences.contains("UserID")) {
            sharedPreferencesEditor.putString("UserID", "69");
            sharedPreferencesEditor.apply();
        }
    }
    private void postJSON() {
        String URL = "http://20.43.19.13:3000/Users";
        JSONObject userJSON = new JSONObject();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();

        try {
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            Set<String> setInterests = sharedPreferences.getStringSet("Interests", null);
            JSONArray interests = new JSONArray();
            for (String interest : setInterests){
                interests.put(interest);
            }
            userJSON.put("Interests", interests);

        } catch (JSONException e) {
            e.printStackTrace();
        }

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        String userID = response.toString();
                        spEditor.putString("id", userID);
                        spEditor.apply();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)   {
                    Toast.makeText(getApplicationContext(), "There was an error. Please try again.", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(CreateProfile.this);
            requestQueue.add(postRequest);

    }
}
