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
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class createprofile extends AppCompatActivity {

    Button btnDateofBirth;
    Button btnSaveChanges;
    Button btnInterests;
    Button btnEditProfilePicture;
    EditText tbFirstName;
    EditText tbLastName;
    EditText tbEmail;
    TextView tvDateofBirth;
    String firstName;
    String lastName;
    String Email;
    String Date = "69";
    String Gender;
    String spinnerGender[] = {"", "Male", "Female", "Other", "Rather not say"};
    Spinner spinGender;
    ImageView imgProfilePicture;
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    int currentMonth = currentDate.getMonthValue();
    int currentDay = currentDate.getDayOfMonth();
    Uri uriImage;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    int Image = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createprofile);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button button = findViewById(R.id.button69);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okhttpGetRequest();
            }
        });

        btnDateofBirth = findViewById(R.id.btnDateofBirth);
        btnDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dateofbirthpicker = new DatePickerDialog(createprofile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String sday = Integer.toString(day);
                        String smonth = Integer.toString(month);
                        String syear = Integer.toString(year);

                        if (currentYear - year <= 18) {
                            if (currentMonth - month == 0) {
                                if (currentDay - day < 0) {
                                    Toast.makeText(createprofile.this, "You must be 18 to use squadUP. :(", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (currentDay - day >= 0) {
                                    Date = (smonth + "/" + sday + "/" + syear);
                                    tvDateofBirth = findViewById(R.id.tvDateofBirth);
                                    tvDateofBirth.setText(Date);
                                }
                            }
                            if (currentMonth - month > 0) {
                                Date = (smonth + "/" + sday + "/" + syear);
                                tvDateofBirth = findViewById(R.id.tvDateofBirth);
                                tvDateofBirth.setText(Date);
                            } else if (currentMonth - month < 0) {
                                Toast.makeText(createprofile.this, "You must be 18 to use squadUP.", Toast.LENGTH_SHORT).show();
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


        spinGender = findViewById(R.id.spinnerGender);
        spinGender.setPrompt("Gender");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(createprofile.this,
                android.R.layout.simple_list_item_1, spinnerGender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(arrayAdapter);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tbFirstName = findViewById(R.id.tbFirstName);
                firstName = tbFirstName.getText().toString();
                tbLastName = findViewById(R.id.tbLastName);
                lastName = tbLastName.getText().toString();
                tbEmail = findViewById(R.id.tbEmail);
                Email = tbEmail.getText().toString();
                Gender = spinGender.getSelectedItem().toString();
                sharedPreferencesEditor = sharedPreferences.edit();


                if (!firstName.equals("")) {
                    sharedPreferencesEditor.putString("FirstName", firstName);
                    sharedPreferencesEditor.apply();
                }
                if (!lastName.equals("")) {
                    sharedPreferencesEditor.putString("LastName", lastName);
                    sharedPreferencesEditor.apply();
                }
                if (!Email.equals("")) {
                    sharedPreferencesEditor.putString("Email", Email);
                    sharedPreferencesEditor.apply();
                }
                if (!Date.equals("69")) {
                    sharedPreferencesEditor.putString("DateofBirth", Date);
                    sharedPreferencesEditor.apply();
                }

                if (!Gender.equals("")) {
                    sharedPreferencesEditor.putString("Gender", Gender);
                    sharedPreferencesEditor.apply();
                }

                if (!sharedPreferences.contains("FirstName")) {
                    Toast.makeText(createprofile.this, "Please fill in a valid First Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!sharedPreferences.contains("LastName")) {
                    Toast.makeText(createprofile.this, "Please fill in a valid Last Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!sharedPreferences.contains("Email")) {
                    Toast.makeText(createprofile.this, "Please fill in a valid Email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!sharedPreferences.contains("DateofBirth")) {
                    Toast.makeText(createprofile.this, "Please fill in a valid Date of Birth .", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!sharedPreferences.contains("Gender")) {
                    Toast.makeText(createprofile.this, "Please fill out the Gender field.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!sharedPreferences.contains("UserID")) {
                    sharedPreferencesEditor.putString("UserID", "69");
                    sharedPreferencesEditor.apply();
                }

                ParseJSON();


                /*if(userInfoJSON == null) {
                    userInfoJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
                    userInfoJSON.put("LastName", sharedPreferences.getString("LastName", ""));
                    userInfoJSON.put("Email", sharedPreferences.getString("Email", ""));
                    userInfoJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
                    userInfoJSON.put("Gender", sharedPreferences.getString("Gender", ""));
                }*/

// ...
                Intent intent = new Intent(createprofile.this, Profile.class);
                startActivity(intent);
            }
        });

        btnEditProfilePicture = findViewById(R.id.btnEditProfilePicture);
        btnEditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, Image);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 69 && requestCode == Image) {
            uriImage = data.getData();
            imgProfilePicture = findViewById(R.id.imgProfilePicture);
            imgProfilePicture.setImageURI(uriImage);
            sharedPreferencesEditor.putString("ProfilePicture", uriImage.toString());
            sharedPreferencesEditor.apply();
        }
    }

    public void okhttpGetRequest(){
        TextView tv69 = findViewById(R.id.textView69);

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

                    createprofile.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv69.setText(sresponse);
                        }
                    });
                }
            }
        });

    }

    public void ParseJSON() {
        RequestQueue queue = Volley.newRequestQueue(createprofile.this);
        final String url = "20.43.19.13:3000/Users";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplication(), "Changes saved successfully", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(getRequest);
    }

    private void PostJSON() {

        try {
            String URL = "20.43.19.13:3000/Users";
            JSONObject userJSON = new JSONObject();

            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("UserID", sharedPreferences.getString("UserID", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(createprofile.this);
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void PutJSON() {
        RequestQueue queue = Volley.newRequestQueue(createprofile.this);
        try {
            String url = "20.43.19.13:3000/Users";
            JSONObject userJSON = new JSONObject();
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("UserID", sharedPreferences.getString("UserID", ""));
            userJSON.put("FirebaseToken", sharedPreferences.getString("FirebaseToken", ""));
            userJSON.put("Interests", sharedPreferences.getStringSet("Interests", null));



            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, userJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(createprofile.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
}
