package com.company.verbzz_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.company.verbzz_app.Activities.Authentication_Page;
import com.company.verbzz_app.Activities.Conjugate_Activity;
import com.company.verbzz_app.Activities.GradedPractice;
import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class LessonsFragment extends Fragment {

    ImageButton languageMenu, logOut;
    Button conjugations, gradedPractice, memorization;

    DrawerLayout drawerLayout;
    DatabaseAccess databaseAccess = new DatabaseAccess();

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

        //Sets the language flag at the top of the fragment by accessing database;
        databaseAccess.checkCurrentLanguage(language -> databaseAccess.setBackgroundFlag(language, languageMenu));

        //Opens the drawer that allows language change;
        languageMenu.setOnClickListener(v -> openDrawer(drawerLayout));

        //Opens the page that allows for conjugation of all verbs;
        conjugations.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), Conjugate_Activity.class);
            startActivity(i);
        });

        gradedPractice.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), GradedPractice.class);
            startActivity(i);
        });

        memorization.setOnClickListener(v -> {

            //Create "Sporcle-like" quizzes to memorize main verbs and their equivalents in french/english;

        });

        //Log out button - design it better;
        logOut.setOnClickListener(v -> logOut());

        return view;
        //end of onCreateView
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