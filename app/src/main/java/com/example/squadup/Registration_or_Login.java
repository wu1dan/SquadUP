package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registration_or_Login extends AppCompatActivity {

    private Button btnSignIn, btnRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_or__login);

        btnSignIn = (Button) findViewById(R.id.btnSignin);  //Signin button
        btnRegistration = (Button) findViewById(R.id.btnRegisternow);   //Registration button

        btnSignIn.setOnClickListener(new View.OnClickListener() {    //Sends you to main activity(To be edited)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_or_Login.this, Login.class); //next step is main activity
                startActivity(intent);
                finish();
                return;
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {  //Sends you to registration page
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_or_Login.this, RegistrationPage.class); //next step is registration page
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
