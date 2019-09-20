package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;

    private Button btnLogin;
    private Button btnRegistration;
    private SignInButton googlesigninbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);  //Signin button
        btnRegistration = (Button) findViewById(R.id.btnRegisternow);   //Registration button
        btnRegistration.setOnClickListener(new View.OnClickListener() {  //Sends you to registration page
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistrationPage.class); //next step is registration page
                startActivity(intent);
                finish();
                return;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //request user data
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);  //get the google sign in client

        googlesigninbutton = findViewById(R.id.btngsi);
        googlesigninbutton.setOnClickListener(new View.OnClickListener() {      //when button is pressed
            @Override
            public void onClick(View v) {           //when google sign in button pressed, start up signIn class
                switch (v.getId()) {
                    case R.id.btngsi:
                        signIn();
                        break;
                    // ...
                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 69);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 69) {           //if the right code is found (6942049), it'll handle the sign in
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(Login.this, "User has signed in successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}


