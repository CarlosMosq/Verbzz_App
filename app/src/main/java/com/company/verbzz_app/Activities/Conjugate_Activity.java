package com.company.verbzz_app.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.VerbEventBus;
import com.company.verbzz_app.Fragments.BlankHoneyFragment;
import com.company.verbzz_app.Fragments.EnglishFragment;
import com.company.verbzz_app.Fragments.FrenchFragment;
import com.company.verbzz_app.R;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

public class Conjugate_Activity extends AppCompatActivity {

    private final EnglishFragment englishFragment = new EnglishFragment();
    private final FrenchFragment frenchFragment = new FrenchFragment();
    private final BlankHoneyFragment blankHoneyFragment = new BlankHoneyFragment();
    ImageButton languageChange, conjugateButton;
    EditText verbInput;
    Fragment languageFragment;
    String currentLanguage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    DatabaseAccess databaseAccess = new DatabaseAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjugate);

        languageChange = findViewById(R.id.languageChange);
        verbInput = findViewById(R.id.verbInput);
        conjugateButton = findViewById(R.id.conjugateButton);
        drawerLayout = findViewById(R.id.drawerLayoutConjugate);
        navigationView = findViewById(R.id.navigationConjugate);

        //Sets the language flag at the top of the fragment by accessing database and sets Fragment that will be used;
        checkCurrentLanguage();

        //Opens first fragment as a blank image until a verb is selected for conjugation;
        setInitialFragment();

        //Opens drawer that allows language change;
        languageChange.setOnClickListener(v -> openDrawer(drawerLayout));

        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            closeDrawer(drawerLayout);
            checkCurrentLanguage();
            return true;
        });

        //collects verb provided by user and does call to retrofit with according language
        conjugateButton.setOnClickListener(v -> {
            String verb = verbInput.getText().toString();
            callToRetrofit(currentLanguage, verb);
        });
    }

    private void checkCurrentLanguage() {
        databaseAccess.checkCurrentLanguage(language -> {
            databaseAccess.setBackgroundFlag(language, languageChange);
            languageFragment = returnLanguageFragment(language);
            setCurrentLanguage(language);
        });
    }

    //Opens first fragment as a blank image until a verb is selected for conjugation;
    private void setInitialFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.conjugatedView, blankHoneyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //Method that returns correct fragment depending on the language passed to it
    private Fragment returnLanguageFragment(String language) {
        if(language == null) {
            return blankHoneyFragment;
        }
        else if(language.equals("English")) {
            return englishFragment;
        }
        else {
            return frenchFragment;
        }
    }

    //Method to be used when fragment needs to be changed
    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conjugatedView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //Sets variable currentLanguage with data collected from database
    private void setCurrentLanguage(String language) {
        currentLanguage = language;
    }

    //calls the API through Retrofit methods in DatabaseAccess class to collect language data;
    private void callToRetrofit(String currentLanguage, String verb) {
        if(currentLanguage == null)
            Toast.makeText(Conjugate_Activity.this, "Current language not set", Toast.LENGTH_SHORT).show();
        else if(currentLanguage.equals("English")) {
            databaseAccess.callRetrofitEnglish(data -> {
                int index = databaseAccess.returnEnglishVerbPosition(data, verb);
                if(index >= 0) {
                    EventBus.getDefault().postSticky(new VerbEventBus(verb, index));
                    changeFragment(languageFragment);
                }
                else
                    Toast.makeText(Conjugate_Activity.this
                            , "Verb not found, please verify orthography or try a different one"
                            , Toast.LENGTH_SHORT).show();
            });
        }
        else {
            databaseAccess.callRetrofitFrench(data -> {
                int index = databaseAccess.returnFrenchVerbPosition(data, verb);
                if(index >= 0) {
                    EventBus.getDefault().postSticky(new VerbEventBus(verb, index));
                    changeFragment(languageFragment);
                }
                else
                    Toast.makeText(Conjugate_Activity.this
                            , "Verb not found, please verify orthography or try a different one"
                            , Toast.LENGTH_SHORT).show();
            });
        }
    }

    //opens drawer
    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //closes drawer
    private void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

}