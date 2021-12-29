package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    TextView emailField;
    Button sendLink;
    TextView backLink;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.emailInput3);
        sendLink = findViewById(R.id.sendLink);
        backLink = findViewById(R.id.backLink);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);

        sendLink.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = emailField.getText().toString();
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(Forgot_Password.this, "Link Successfully Sent. Please verify your E-mail", Toast.LENGTH_SHORT).show();
                    goToLogin();
                }
                else{
                    Toast.makeText(Forgot_Password.this, "Error, Please Try Again", Toast.LENGTH_SHORT).show();
                    emailField.setText("");
                }
                progressBar.setVisibility(View.INVISIBLE);
            });

        });

        backLink.setOnClickListener(v -> goToLogin());


    }

    public void goToLogin() {
        Intent i = new Intent(Forgot_Password.this, Login_Activity.class);
        startActivity(i);
        finish();
    }

}