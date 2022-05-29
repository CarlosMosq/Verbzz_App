package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;

public class Sign_Up_Activity extends AppCompatActivity {

    String[] languages = {"English", "Français"};
    String currentLanguage = "English";

    TextInputEditText emailField, passwordField;
    Button createAccount;
    ProgressBar progressBar;
    AutoCompleteTextView languageList;
    ArrayAdapter<String> languageAdapter;

    FirebaseAuth auth = FirebaseAuth.getInstance();

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
        //lists were not updating after selection until I switched to setOnItemClickListener
        //if user does not click in anything, variable is set to default value of "English"
        languageAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown_list, languages);
        languageList.setAdapter(languageAdapter);
        languageList.setOnItemClickListener((adapterView, view, i, l) -> currentLanguage = adapterView.getItemAtPosition(i).toString());
    }

    //Code that enables signUp with e-mail and password through firebase;
    public void signUpWithFirebase(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                //Send verification link
                FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                user
                        .sendEmailVerification()
                        .addOnSuccessListener(unused ->
                                Toast.makeText(Sign_Up_Activity.this
                                , getText(R.string.verifySent)
                                , Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Log.e("error", e.getMessage()));

                saveToDatabase(currentLanguage);
                Intent i = new Intent(Sign_Up_Activity.this, Login_Activity.class);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(i);
                finish();
            }
            else {
                Toast.makeText(Sign_Up_Activity.this, getText(R.string.tryAgain), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Saves the first language to be used into the Database
    public void saveToDatabase(String language) {
        DatabaseAccess databaseAccess = new DatabaseAccess();
        databaseAccess.saveCurrentLanguageToDatabase(language);
    }
}