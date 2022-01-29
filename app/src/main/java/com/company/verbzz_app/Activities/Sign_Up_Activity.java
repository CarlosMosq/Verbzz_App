package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_Up_Activity extends AppCompatActivity {

    String[] languages = {"English", "French"};
    TextInputEditText emailField, passwordField;
    Button createAccount;
    ProgressBar progressBar;
    AutoCompleteTextView languageList;
    ArrayAdapter<String> languageAdapter;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String currentLanguage = "None";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.emailInput);
        passwordField = findViewById(R.id.passwordInput);
        createAccount = findViewById(R.id.buttonCreate);
        progressBar = findViewById(R.id.progressBar);
        languageList = findViewById(R.id.autoCompleteLanguage);

        progressBar.setVisibility(View.INVISIBLE);

        createAccount.setOnClickListener(v -> {
            createAccount.setClickable(false);
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            signUpWithFirebase(email, password);
        });

        //creates and adapts dropdown list of languages to choose from
        languageAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown_list, languages);
        languageList.setAdapter(languageAdapter);
        languageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentLanguage = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentLanguage = adapterView.getItemAtPosition(0).toString();
            }
        });
        languageList.setAdapter(languageAdapter);
    }

    //Code that enables signUp with e-mail and password through firebase;
    public void signUpWithFirebase(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                saveToDatabase(currentLanguage);
                Toast.makeText(Sign_Up_Activity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Sign_Up_Activity.this, Login_Activity.class);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(i);
                finish();
            }
            else {
                Toast.makeText(Sign_Up_Activity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Saves the first language to be used into the Database
    public void saveToDatabase(String language) {
        DatabaseAccess databaseAccess = new DatabaseAccess();
        databaseAccess.saveCurrentLanguageToDatabase(language);
    }
}