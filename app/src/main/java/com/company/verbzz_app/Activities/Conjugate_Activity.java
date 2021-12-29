package com.company.verbzz_app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.company.verbzz_app.Fragments.EnglishFragment;
import com.company.verbzz_app.Fragments.FrenchFragment;
import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Conjugate_Activity extends AppCompatActivity {

    private final EnglishFragment englishFragment = new EnglishFragment();
    private final FrenchFragment frenchFragment = new FrenchFragment();
    Button languageChange;
    EditText verbInput;
    Button conjugateButton;
    String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjugate);

        languageChange = findViewById(R.id.languageChange);
        verbInput = findViewById(R.id.verbInput);
        conjugateButton = findViewById(R.id.conjugateButton);

        //Opens first fragment based on language currently set as "Current Language" on database;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.conjugatedView, setFirstFragment(currentLanguage));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        languageChange.setOnClickListener(v -> {

        });

        conjugateButton.setOnClickListener(v -> {
            String verb = verbInput.getText().toString();



            /*Set intent to send data from activity to fragment so that the
            fragment can search for the database conjugations and return the view updated */
        });


    }

    public Fragment setFirstFragment(String language) {
        if(language == null) {
            languageChange.setBackgroundResource(R.drawable.non_chosen_language);
            return englishFragment;
        }
        else if(language.equals("English")) {
            languageChange.setBackgroundResource(R.drawable.english_language);
            return englishFragment;
        }
        else {
            languageChange.setBackgroundResource(R.drawable.french_language);
            return frenchFragment;
        }
    }

    public void changeFragment(Fragment fragment) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        FirebaseUser user = auth.getCurrentUser();

        //set up the rest

    }

    public void takeData(String language) {
        currentLanguage = language;
    }

}