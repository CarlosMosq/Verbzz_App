package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Adapters.Language_Drawer_Adapter;
import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.EventBusClasses.VerbEventBus;
import com.company.verbzz_app.Fragments.BlankHoneyFragment;
import com.company.verbzz_app.Fragments.EnglishFragment;
import com.company.verbzz_app.Fragments.FrenchFragment;
import com.company.verbzz_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Conjugate_Activity extends AppCompatActivity {

    private final EnglishFragment englishFragment = new EnglishFragment();
    private final FrenchFragment frenchFragment = new FrenchFragment();
    private final BlankHoneyFragment blankHoneyFragment = new BlankHoneyFragment();
    ImageButton languageChange, conjugateButton;
    FloatingActionButton mainMenu;
    ProgressBar progressBar;
    private EditText verbInput;
    private Fragment languageFragment;
    private String currentLanguage;
    private DrawerLayout drawerLayout;
    private final ArrayList<String> languages = new ArrayList<>();
    private final DatabaseAccess databaseAccess = new DatabaseAccess();
    NavigationView navigationView;
    RecyclerView recyclerView;
    Language_Drawer_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjugate);

        languageChange = findViewById(R.id.languageChange);
        verbInput = findViewById(R.id.verbInput);
        conjugateButton = findViewById(R.id.conjugateButton);
        drawerLayout = findViewById(R.id.drawerLayoutConjugate);
        navigationView = findViewById(R.id.navigationConjugate);
        mainMenu = findViewById(R.id.floatingActionButton);
        progressBar = findViewById(R.id.progressBarConjugate);

        recyclerView = findViewById(R.id.conjugateRecycler);
        adapter = new Language_Drawer_Adapter(languages, this);
        progressBar.setVisibility(View.INVISIBLE);

        //Sets the language flag at the top of the fragment by accessing database and sets Fragment that will be used;
        setInitialFragment();
        checkCurrentLanguage();

        //Opens drawer that allows language change;
        languageChange.setOnClickListener(v -> openDrawer(drawerLayout));

        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            checkCurrentLanguage();
            return true;
        });

        //collects verb provided by user and does call to retrofit with according language
        conjugateButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String verb = verbInput.getText().toString();
            verbInput.setText("");
            callToRetrofit(currentLanguage, verb);
        });

        //Allows easier access to main menu
        mainMenu.setOnClickListener(v -> {
            Intent i = new Intent(Conjugate_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    /*Sets flag at the left corner, the current language and fragment, and closes the drawer if
    it's open, changing the fragment to initial state*/
    private void checkCurrentLanguage() {
        databaseAccess.checkCurrentLanguage(language -> {
            databaseAccess.setBackgroundFlag(language, languageChange);
            languageFragment = returnLanguageFragment(language);
            setCurrentLanguage(language);
            closeDrawer();
            changeFragment(blankHoneyFragment);
        });
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

    //Method to be used to set firstFragment
    private void setInitialFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.conjugatedView, blankHoneyFragment);
        fragmentTransaction.commit();
    }

    //Method to be used when fragment needs to be changed
    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conjugatedView, fragment);
        fragmentTransaction.commit();
    }

    //Sets variable currentLanguage with data collected from database
    private void setCurrentLanguage(String language) {
        currentLanguage = language;
    }

    //calls the API through Retrofit methods in DatabaseAccess class to collect language data;
    private void callToRetrofit(String currentLanguage, String verb) {
        CompletableFuture.runAsync(() -> {
            if(currentLanguage == null)
                Toast.makeText(Conjugate_Activity.this, getText(R.string.languageNotSet), Toast.LENGTH_SHORT).show();
            else if(currentLanguage.equals("English")) {
                databaseAccess.callRetrofitEnglish(data -> {
                    int index = databaseAccess.returnEnglishVerbPosition(data, verb);
                    if(index >= 0) {
                        EventBus.getDefault().postSticky(new VerbEventBus(verb, index));
                        changeFragment(languageFragment);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else
                        Toast.makeText(Conjugate_Activity.this
                                , getText(R.string.verbNotFound)
                                , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
            else {
                databaseAccess.callRetrofitFrench(data -> {
                    int index = databaseAccess.returnFrenchVerbPosition(data, verb);
                    if(index >= 0) {
                        EventBus.getDefault().postSticky(new VerbEventBus(verb, index));
                        changeFragment(languageFragment);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else
                        Toast.makeText(Conjugate_Activity.this
                                , getText(R.string.verbNotFound)
                                , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

    //opens drawer
    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
        currentLanguages(languages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    //closes drawer
    private void closeDrawer() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        verbInput.setText("");
    }

    private void currentLanguages(ArrayList<String> languages) {
        languages.add("English");
        languages.add("Français");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        changeFragment(blankHoneyFragment);
    }
}