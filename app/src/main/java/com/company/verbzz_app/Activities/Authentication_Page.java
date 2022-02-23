package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication_Page extends AppCompatActivity {
    Button signUp;
    TextView login;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_page);

        signUp = findViewById(R.id.signUpButton);
        login = findViewById(R.id.loginButton);

        //listeners responsible for sending user to either login or sign up page
        signUp.setOnClickListener(v -> {
            Intent i = new Intent(Authentication_Page.this, Sign_Up_Activity.class);
            startActivity(i);
        });

        login.setOnClickListener(v -> {
            Intent i = new Intent(Authentication_Page.this, Login_Activity.class);
            startActivity(i);
        });

    }

    //if user is already logged in, go straight to main page
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null) goToMain();
    }

    //Intent directs user to main page
    public void goToMain() {
        Intent i = new Intent(Authentication_Page.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}