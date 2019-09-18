package com.example.squadup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationPage extends AppCompatActivity {

    private Button btnRegistration;
    private EditText Email, VerifyEmail, Password, VerifyPassword, FirstName, LastName;
    private FirebaseAuth Authenticator; //authenticates login
    private FirebaseAuth.AuthStateListener AuthenticatorListener; //checks login success

    @Override
    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        Intent intent = new Intent(RegistrationPage.this,Registration_or_Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        btnRegistration = (Button) findViewById(R.id.btnrpRegisterNow);   //Register now Button
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
                Authenticator.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationPage.this, new OnCompleteListener<AuthResult>() {    //authenticator creates user with inputted email and pass
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegistrationPage.this, "There was an error. Please try again.", Toast.LENGTH_SHORT).show();  //error message if authentication unsuccessful
                        }
                    }
                });

            }
        });

        Authenticator = FirebaseAuth.getInstance();
        AuthenticatorListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                if (fbuser != null) {
                    Intent intent = new Intent(RegistrationPage.this, Login.class); //next step is return to login page
                }
                else{

                }
            }
        };

    }

    public void Startup(){  //start authenticator listener on startup of page
        Startup();
        Authenticator.addAuthStateListener(AuthenticatorListener);
    }

    public void Shutdown(){ //stop authenticator listener when page is closed
        Shutdown();
        Authenticator.removeAuthStateListener(AuthenticatorListener);
    }
}
