package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.R;

public class GradedPractice extends AppCompatActivity {

    String currentLanguage;
    //Arrays to be used by dropdown menu so that the user can choose a specific tense to practice and amount of verbs
    String[] englishTenses = {"Present", "Past", "Past (irr)", "Future", "Present Perfect", "Past Perfect", "Future Perfect"
            , "Present Conditional", "Perfect Conditional", "Present Subjunctive", "Perfect Subjunctive"};
    String[] frenchTenses = {"Tous", "Présent (-er)", "Présent (-ir)", "Présent (-re)"
            , "Présent (Réguliers)", "Présent (Irréguliers)", "Présent (Réfléchis)"
            , "Présent (Tous)", "Imparfait", "Futur Simple", "Passé Simple", "Passé Composé (avoir)"
            , "Passé Composé (être)", "Passé Composé (Réfléchis)", "Passé Composé (Tous)"
            , "Plus-Que-Parfait", "Futur Antérieur", "Passé Antérieur", "Présent Conditionnel"
            , "Présent Subjonctif","Passé Subjonctif", "Imparfait Subjonctif", "Plus-que-parfait Subjonctif"};
    String[] verbCount = {"10", "20", "30", "40", "50", "100"};

    AutoCompleteTextView autoCompleteTenses, autoCompleteCount;
    ArrayAdapter<String> tensesAdapter, countAdapter;
    SwitchCompat timeSwitch;
    TextView timeTextView;
    Button start;

    String tense, nbrOfVerbs;
    String time = "5";

    DatabaseAccess databaseAccess = new DatabaseAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graded_practice);

        autoCompleteTenses = findViewById(R.id.autoCompleteView);
        autoCompleteCount = findViewById(R.id.autoCompleteLanguage);
        timeSwitch = findViewById(R.id.timeSwitch);
        timeTextView = findViewById(R.id.timeTextView);
        start = findViewById(R.id.startButton);

        //Checks the current language in the database and sets dropdown menus accordingly
        setDropDownMenus();

        //Sets time limit for practice as either 5min or 0/off
        timeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                time = "5";
                timeTextView.setText(getString(R.string.fivemin));
            }
            else {
                time = "off";
                timeTextView.setText(getString(R.string.off));
            }
        });

        start.setOnClickListener(v -> {
            Intent i = new Intent(GradedPractice.this, LanguagePractice.class);
            i.putExtra("currentLanguage", currentLanguage);
            i.putExtra("tense", tense);
            i.putExtra("nbrOfVerbs", validateMax(nbrOfVerbs));
            i.putExtra("time", time);
            startActivity(i);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setDropDownMenus();
    }

    //Checks the current language in the database and sets dropdown menus accordingly
    public void setDropDownMenus() {
        databaseAccess.checkCurrentLanguage(language -> {
            setLanguage(language);
            tensesAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown_list, getTenseArray(language));
            autoCompleteTenses.setAdapter(tensesAdapter);
            autoCompleteTenses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tense = adapterView.getItemAtPosition(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    tense = adapterView.getItemAtPosition(0).toString();
                }
            });
            autoCompleteTenses.setAdapter(tensesAdapter);

            countAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown_list, verbCount);
            autoCompleteCount.setAdapter(countAdapter);
            autoCompleteCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    nbrOfVerbs = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    nbrOfVerbs = adapterView.getItemAtPosition(0).toString();
                }
            });
            autoCompleteCount.setAdapter(countAdapter);
        });
    }

    //returns array with appropriate language tenses
    private String[] getTenseArray(String language) {
        if(language.equals("English")) return englishTenses;
        else return frenchTenses;
    }

    //Sets current language as the language passed to the function
    public void setLanguage(String language) {
        currentLanguage = language;
    }

    /*Used to check the minimum value between the number of verbs the user chooses and the
    number of verbs available for a specific tense */
    private String validateMax(String nbr) {
        return String.valueOf(Math.min(Integer.parseInt(nbr), getTenseArray(currentLanguage).length));
    }

}