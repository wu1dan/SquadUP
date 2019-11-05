package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationPage extends AppCompatActivity {

    private EditText Email, VerifyEmail, Password, VerifyPassword, FirstName, LastName;

    @Override
    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(RegistrationPage.this,Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        Button btnRegistration = (Button) findViewById(R.id.btnrpRegisterNow);   //Register now Button
        Email = (EditText) findViewById(R.id.tbEmail);    //Email Textbox
        VerifyEmail = (EditText) findViewById(R.id.tbVerifiedEmail);  //Verify Email Textbox
        Password = (EditText) findViewById(R.id.tbPassword);  //Password Textbox
        VerifyPassword = (EditText) findViewById(R.id.tbVerifiedPassword);    //Verify Password Textbox
        FirstName = (EditText) findViewById(R.id.tbFirstName);    //First name textbox
        LastName = (EditText) findViewById(R.id.tbLastName);  //Last name textbox

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = FirstName.getText().toString();  //dont really know how to add name in as firebase api is only for email and pass
                String lName = LastName.getText().toString();
                String email = Email.getText().toString();
                String verifyemail = VerifyEmail.getText().toString();
                String password = Password.getText().toString();
                String verifypassword = VerifyPassword.getText().toString();
                if (fName.length() == 0){                   //ERROR MESSAGES IF MISSING INFORMATION OR VERIFIED DOES NOT MATCH ORIGINAL
                    Toast.makeText(RegistrationPage.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lName.length() == 0){
                    Toast.makeText(RegistrationPage.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.length() == 0 || verifyemail.length() == 0){
                    Toast.makeText(RegistrationPage.this, "Please fill in a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.equals(verifyemail)){
                    Toast.makeText(RegistrationPage.this, "Your emails did not match, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() == 0 || verifypassword.length() == 0){
                    Toast.makeText(RegistrationPage.this, "Please fill in a valid password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!verifypassword.equals(password)){
                    Toast.makeText(RegistrationPage.this, "Your passwords did not match, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });


    }


}
