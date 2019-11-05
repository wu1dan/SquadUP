package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    private final String TAG = "Homepage";
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    public void onBackPressed()       //CODE FOR CHANGING BACK BUTTON FUNCTIONALITY MAKE SURE EDITED PER ACTIVITY TO RETURN TO CORRECT ONE
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        LoginButton btnfb;
        btnfb = findViewById(R.id.btnfb);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            Intent intent = new Intent(Login.this, Facebook.class); //next step is homepage
            startActivity(intent);
            finish();
        }

        btnfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "User has successfully logged in");
                Intent intent = new Intent(Login.this, MainActivity.class); //next step is homepage
                startActivity(intent);
                finish();
                return;
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "User has cancelled the login process");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "There was an error. Please try again");
            }
        });


        //Button btnLogin = (Button) findViewById(R.id.btnLogin);  //Signin button, might be deleting self registration later
        Button btnRegistration = (Button) findViewById(R.id.btnRegisternow);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistrationPage.class); //next step is registration page
                startActivity(intent);
                finish();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //request user data
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);  //get the google sign in client

        SignInButton googlesigninbutton = findViewById(R.id.btngsi);
        googlesigninbutton.setOnClickListener(new View.OnClickListener() {      //when button is pressed
            @Override
            public void onClick(View v) {           //when google sign in button pressed, start up signIn class
                switch (v.getId()) {
                    case R.id.btngsi:
                        signIn();
                        break;
                    // ...
                    default:
                        Toast.makeText(Login.this, "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 69);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 69) {           //if the right code is found (6942049), it'll handle the sign in
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount googleAcct = signInResult.getSignInAccount();

            saveGoogleInfo(googleAcct);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
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

    private void saveGoogleInfo(GoogleSignInAccount googleAcct) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        String googleFirstName = googleAcct.getGivenName();       //self explanatory
        String googleLastName = googleAcct.getFamilyName();     //self explanatory
        String googleEmail = googleAcct.getEmail();       //self explanatory
        String personID = googleAcct.getId();          //unique id
        String IDToken = googleAcct.getIdToken();     //id token can be sent to server
        Uri personPhoto = googleAcct.getPhotoUrl();       //self explanatory

        if (!sharedPreferences.contains("FirstName")) {
            sharedPreferencesEditor.putString("FirstName", googleFirstName);
            sharedPreferencesEditor.apply();
        }
        if (!sharedPreferences.contains("LastName")) {
            sharedPreferencesEditor.putString("LastName", googleLastName);
            sharedPreferencesEditor.apply();
        }
        if (!sharedPreferences.contains("Email")) {
            sharedPreferencesEditor.putString("Email", googleEmail);
            sharedPreferencesEditor.apply();
        }
        if (!sharedPreferences.contains("ProfilePicture")) {
            if (personPhoto != null) {
                sharedPreferencesEditor.putString("ProfilePicture", personPhoto.toString());
                sharedPreferencesEditor.apply();
            }
        }
        if (!sharedPreferences.contains("GoogleID")) {
            if (personPhoto != null) {
                sharedPreferencesEditor.putString("GoogleID", personID);
                sharedPreferencesEditor.apply();
            }
        }
        if (!sharedPreferences.contains("GoogleIDToken")) {
            if (personPhoto != null) {
                sharedPreferencesEditor.putString("GoogleIDToken", IDToken);
                sharedPreferencesEditor.apply();
            }
        }
    }
}


