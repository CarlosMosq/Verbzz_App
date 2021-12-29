package com.company.verbzz_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.company.verbzz_app.Activities.Authentication_Page;
import com.company.verbzz_app.Activities.Conjugate_Activity;
import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LessonsFragment extends Fragment {

    ArrayAdapter<CharSequence> adapter;
    Spinner spinner;
    String numberOfVerbs = "10";
    Button languageMenu;
    String currentLanguage;
    Button conjugations;
    Button gradedPractice;
    Button memorization;
    Button logOut;

    DrawerLayout drawerLayout;

    public LessonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View inflated required in Fragment structures, which is returned at the end of this method;
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        languageMenu = view.findViewById(R.id.menuDrawerButton);
        conjugations = view.findViewById(R.id.conjugations);
        gradedPractice = view.findViewById(R.id.gradedPractice);
        memorization = view.findViewById(R.id.memorization);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        logOut = view.findViewById(R.id.logOut);

        //Sets current language and the language flag at the top of the fragment by accessing database;
        checkCurrentLanguage();

        languageMenu.setOnClickListener(v -> openDrawer(drawerLayout));

        //Opens the page that allow for conjugation of all verbs;
        conjugations.setOnClickListener(v -> {
            Conjugate_Activity conjugate = (Conjugate_Activity) getActivity();
            Intent i = new Intent(getActivity(), Conjugate_Activity.class);
            assert conjugate != null;
            conjugate.takeData(currentLanguage);
            startActivity(i);
        });

        gradedPractice.setOnClickListener(v -> {

            //Code the page for practicing conjugations with graded content

        });

        memorization.setOnClickListener(v -> {

            //Create "Sporcle-like" quizzes to memorize main verbs and their equivalents in french/english;

        });

        logOut.setOnClickListener(v -> logOut());

        //Sets the dropdown menu that determines the number of verbs for practice;
        spinner = view.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(getContext()
                , R.array.numberOfVerbs
                , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfVerbs = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
        //end of onCreateView
    }

    public void checkCurrentLanguage() {
        FirebaseDatabase databaseLanguage = FirebaseDatabase.getInstance();
        DatabaseReference languageReference = databaseLanguage.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        final String userUID = user.getUid();
        languageReference
                .child("Languages")
                .child("Current Language")
                .child(userUID)
                .child("Current Language")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        currentLanguage = (String) task.getResult().getValue();
                        setBackgroundFlag(currentLanguage, languageMenu);
                    }
                });
    }

    public void setBackgroundFlag(String language, Button flag) {
        if(language == null) flag.setBackgroundResource(R.drawable.non_chosen_language);
        else if(language.equals("English")) {
            flag.setBackgroundResource(R.drawable.english_language);
        }
        else if(language.equals("French")) {
            flag.setBackgroundResource(R.drawable.french_language);
        }
    }

    public void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void logOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent i = new Intent(getActivity(), Authentication_Page.class);
        startActivity(i);
        if(getActivity() != null) getActivity().finish();
    }

}