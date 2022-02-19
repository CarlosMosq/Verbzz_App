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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Activities.Authentication_Page;
import com.company.verbzz_app.Activities.Conjugate_Activity;
import com.company.verbzz_app.Activities.GradedPractice;
import com.company.verbzz_app.Activities.TranslationPractice;
import com.company.verbzz_app.Adapters.Language_Drawer_Adapter;
import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LessonsFragment extends Fragment {

    ImageButton languageMenu, logOut;
    Button conjugations, gradedPractice, translation;
    private String currentLanguage;

    private DrawerLayout drawerLayout;
    NavigationView navigationViewLessons;
    private RecyclerView recyclerView;
    private final DatabaseAccess databaseAccess = new DatabaseAccess();
    private final ArrayList<String> languages = new ArrayList<>();
    private Language_Drawer_Adapter adapter;

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
        translation = view.findViewById(R.id.memorization);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        navigationViewLessons = view.findViewById(R.id.navigationViewLessons);
        logOut = view.findViewById(R.id.logOut);

        recyclerView = view.findViewById(R.id.lessonsFlagRecycler);
        adapter = new Language_Drawer_Adapter(languages, view.getContext());

        //Ads set up
        MobileAds.initialize(view.getContext(), initializationStatus -> {

        });

        AdView mAdView = view.findViewById(R.id.adViewLessons);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Sets the language flag at the top of the fragment by accessing database;
        checkCurrentLanguage();

        //Opens the drawer that allows language change;
        languageMenu.setOnClickListener(v -> openDrawer(drawerLayout));

        //Opens the page that allows for conjugation of all verbs;
        conjugations.setOnClickListener(v -> {
            Intent conjugationIntent = new Intent(getActivity(), Conjugate_Activity.class);
            startActivity(conjugationIntent);
        });

        //Opens the page that allows for conjugation practice;
        gradedPractice.setOnClickListener(v -> {
            Intent gradedIntent = new Intent(getActivity(), GradedPractice.class);
            startActivity(gradedIntent);
        });

        //Opens the page that allows for translation practice;
        translation.setOnClickListener(v -> {
            Intent translationIntent = new Intent(getActivity(), TranslationPractice.class);
            translationIntent.putExtra("currentLanguage", currentLanguage);
            startActivity(translationIntent);
        });

        navigationViewLessons.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            checkCurrentLanguage();
            return true;
        });

        //Log out button - design it better;
        logOut.setOnClickListener(v -> logOut());

        return view;
        //end of onCreateView
    }

    public void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
        currentLanguages(languages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void closeDrawer() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //Log out through firebase
    public void logOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent logOutIntent = new Intent(getActivity(), Authentication_Page.class);
        startActivity(logOutIntent);
        if(getActivity() != null) getActivity().finish();
    }

    private void checkCurrentLanguage() {
        databaseAccess.checkCurrentLanguage(language -> {
            databaseAccess.setBackgroundFlag(language, languageMenu);
            setCurrentLanguage(language);
            closeDrawer();
        });
    }

    private void setCurrentLanguage(String language) {
        this.currentLanguage = language;
    }

    private void currentLanguages(ArrayList<String> languages) {
        languages.add("English");
        languages.add("Français");
    }

}