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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    TextInputEditText emailField;
    TextInputEditText passwordField;
    Button logIn;
    TextView noAccount, forgotPassword;
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

        //Login method which collects e-mail and password from fields and passes to firebase method
        logIn.setOnClickListener(v -> {
            logIn.setClickable(false);
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            signInWithFirebase(email, password);
        });

        //Directs user to different activity if he doesn't have an account and clicked on login by mistake
        noAccount.setOnClickListener(v -> {
            Intent i = new Intent(Login_Activity.this, Sign_Up_Activity.class);
            startActivity(i);
        });

        //Directs user to activity for resetting their password
        forgotPassword.setOnClickListener(v -> {
            Intent i = new Intent(Login_Activity.this, Forgot_Password.class);
            startActivity(i);
        });
    }

    //Method that allows sign in with firebase, based on e-mail and password used previously
    public void signInWithFirebase(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                if(user.isEmailVerified()) {
                    Toast.makeText(Login_Activity.this, getText(R.string.accessGranted), Toast.LENGTH_SHORT).show();
                    goToMain();
                }
                else{
                    user
                            .sendEmailVerification()
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(Login_Activity.this
                                            , getText(R.string.verifyEmail)
                                            , Toast.LENGTH_SHORT).show());
                }

                progressBar.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(Login_Activity.this, getText(R.string.incorrect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //directs user to main activity after login is finished
    public void goToMain() {
        Intent i = new Intent(Login_Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}