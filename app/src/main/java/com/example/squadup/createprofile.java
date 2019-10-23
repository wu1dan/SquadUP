package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
                                Toast.makeText(createprofile.this, "You must be 18 to use squadUP. :(", Toast.LENGTH_SHORT).show();
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


                GetJSON();


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
        sharedPreferencesEditor.putString("ProfilePicture", MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
        sharedPreferencesEditor.apply();
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

    public void GetJSON() {
        RequestQueue queue = Volley.newRequestQueue(createprofile.this);
        final String url = "20.43.19.13";  //    "https://api.myjson.com/bins/au48c";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject returnedJsonObject = response.getJSONObject("User");
                    String jsonFirstName = returnedJsonObject.getString("FirstName");
                    String jsonLastName = returnedJsonObject.getString("LastName");
                    String jsonEmail = returnedJsonObject.getString("Email");
                    String jsonDateofBirth = returnedJsonObject.getString("DateofBirth");
                    String jsonUserID = returnedJsonObject.getString("UserID");
                    String jsonGender = returnedJsonObject.getString("Gender");

                    if (!sharedPreferences.getString("FirstName", "").equals(jsonFirstName)) {
                        sharedPreferencesEditor.putString("FirstName", jsonFirstName);
                        sharedPreferencesEditor.apply();
                    }
                    if (!sharedPreferences.getString("LastName", "").equals(jsonLastName)) {
                        sharedPreferencesEditor.putString("LastName", jsonLastName);
                        sharedPreferencesEditor.apply();
                    }
                    if (!sharedPreferences.getString("Email", "").equals(jsonEmail)) {
                        sharedPreferencesEditor.putString("Email", jsonEmail);
                        sharedPreferencesEditor.apply();
                    }
                    if (!sharedPreferences.getString("DateofBirth", "").equals(jsonDateofBirth)) {
                        sharedPreferencesEditor.putString("DateofBirth", jsonDateofBirth);
                        sharedPreferencesEditor.apply();
                    }
                    if (!sharedPreferences.getString("Gender", "").equals(jsonGender)) {
                        sharedPreferencesEditor.putString("Gender", jsonGender);
                        sharedPreferencesEditor.apply();
                    }
                    sharedPreferencesEditor.putString("UserID", jsonUserID);
                    sharedPreferencesEditor.apply();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(getRequest);
    }

    public void PostJSON() {
        RequestQueue queue = Volley.newRequestQueue(createprofile.this);
        try {
            String url = "20.43.19.13";
            JSONObject userJSON = new JSONObject();
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("UserID", sharedPreferences.getString("UserID", ""));


            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, userJSON, new Response.Listener<JSONObject>() {
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
                    headers.put("FirstName", sharedPreferences.getString("FirstName", ""));
                    headers.put("LastName", sharedPreferences.getString("LastName", ""));
                    headers.put("Email", sharedPreferences.getString("Email", ""));
                    headers.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
                    headers.put("Gender", sharedPreferences.getString("Gender", ""));
                    headers.put("UserID", sharedPreferences.getString("UserID", ""));
                    return headers;
                }
            };
            queue.add(postRequest);


        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    public void PutJSON() {
        RequestQueue queue = Volley.newRequestQueue(createprofile.this);
        try {
            String url = "20.43.19.13";
            JSONObject userJSON = new JSONObject();
            userJSON.put("FirstName", sharedPreferences.getString("FirstName", ""));
            userJSON.put("LastName", sharedPreferences.getString("LastName", ""));
            userJSON.put("Email", sharedPreferences.getString("Email", ""));
            userJSON.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
            userJSON.put("Gender", sharedPreferences.getString("Gender", ""));
            userJSON.put("UserID", sharedPreferences.getString("UserID", ""));


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
                    headers.put("FirstName", sharedPreferences.getString("FirstName", ""));
                    headers.put("LastName", sharedPreferences.getString("LastName", ""));
                    headers.put("Email", sharedPreferences.getString("Email", ""));
                    headers.put("DateofBirth", sharedPreferences.getString("DateofBirth", ""));
                    headers.put("Gender", sharedPreferences.getString("Gender", ""));
                    headers.put("UserID", sharedPreferences.getString("UserID", ""));
                    return headers;
                }
            };
            queue.add(putRequest);


        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
