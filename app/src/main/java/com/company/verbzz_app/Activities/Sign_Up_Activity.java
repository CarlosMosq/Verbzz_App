package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up_Activity extends AppCompatActivity {

    TextView emailField;
    TextView passwordField;
    Button createAccount;
    ProgressBar progressBar;
    Spinner languageList;
    ArrayAdapter<CharSequence> languageAdapter;

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
        languageList = findViewById(R.id.languageList);

        progressBar.setVisibility(View.INVISIBLE);

        createAccount.setOnClickListener(v -> {
            createAccount.setClickable(false);
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            signUpWithFirebase(email, password);
        });

        //creates and adapts dropdown list of languages to choose from
        languageAdapter = ArrayAdapter.createFromResource(this
                , R.array.languages
                , android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageList.setAdapter(languageAdapter);

        languageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentLanguage = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    // Sets the first language to be used and saves it to Database
    public void saveToDatabase(String language) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userUID = user.getUid();
            reference.child("Languages").child("Current Language").child(userUID).child("Current Language").setValue(language);
            reference.child("Languages").child("Languages Chosen").child(userUID).child(language).setValue(language);
        }
    }
}