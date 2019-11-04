package com.example.squadup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.facebook.login.LoginManager;

public class Facebook extends AppCompatActivity {

    private Button fblogout;
    private Button fbcont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        fblogout = (Button)findViewById(R.id.btnfblogout);                                     //PROFILE
        fblogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Facebook.this, Login.class);
                startActivity(intent);
            }
        });

        fbcont = (Button)findViewById(R.id.btnfbcont);                                     //PROFILE
        fbcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Facebook.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
