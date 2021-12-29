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

public class Login_Activity extends AppCompatActivity {

    TextView emailField;
    TextView passwordField;
    Button logIn;
    TextView noAccount;
    TextView forgotPassword;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailInput2);
        passwordField = findViewById(R.id.passwordInput2);
        logIn = findViewById(R.id.logIn);
        noAccount = findViewById(R.id.noAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        logIn.setOnClickListener(v -> {
            logIn.setClickable(false);
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            signInWithFirebase(email, password);
        });

        noAccount.setOnClickListener(v -> {
            Intent i = new Intent(Login_Activity.this, Sign_Up_Activity.class);
            startActivity(i);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent i = new Intent(Login_Activity.this, Forgot_Password.class);
            startActivity(i);
        });
    }

    public void signInWithFirebase(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(Login_Activity.this, "Access Granted", Toast.LENGTH_SHORT).show();
                goToMain();
                progressBar.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(Login_Activity.this, "E-mail or Password Incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToMain() {
        Intent i = new Intent(Login_Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}