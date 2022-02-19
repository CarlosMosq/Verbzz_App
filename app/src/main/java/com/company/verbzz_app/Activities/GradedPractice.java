package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.RandomizeVerbsAndTenses;
import com.company.verbzz_app.R;

public class GradedPractice extends AppCompatActivity {

    //Arrays to be used by dropdown menus so that the user can choose a specific tense to practice and amount of verbs
    private final String[] englishTenses = {"Present", "Past", "Past (irr)", "Future", "Present Perfect", "Past Perfect", "Future Perfect"
            , "Present Conditional", "Perfect Conditional", "Present Subjunctive", "Perfect Subjunctive"};
    private final String[] frenchTenses = {"Tous", "Présent (-er)", "Présent (-ir)", "Présent (-re)"
            , "Présent (Réguliers)", "Présent (Irréguliers)", "Présent (Réfléchis)"
            , "Présent (Tous)", "Imparfait", "Futur Simple", "Passé Simple", "Passé Composé (avoir)"
            , "Passé Composé (être)", "Passé Composé (Réfléchis)", "Passé Composé (Tous)"
            , "Plus-Que-Parfait", "Futur Antérieur", "Passé Antérieur", "Présent Conditionnel"
            , "Présent Subjonctif","Passé Subjonctif", "Imparfait Subjonctif", "Plus-que-parfait Subjonctif"};
    private final String[] verbCount = {"10", "20", "30", "40", "50", "100"};

    AutoCompleteTextView autoCompleteTenses, autoCompleteCount;
    ArrayAdapter<String> tensesAdapter, countAdapter;
    SwitchCompat timeSwitch;
    TextView timeTextView;
    Button start;

    int nbrOfVerbs = 10;
    String currentLanguage;
    String tense = "Present";
    String time = "5";
    private final DatabaseAccess databaseAccess = new DatabaseAccess();
    private final RandomizeVerbsAndTenses randomizeVerbsAndTenses = new RandomizeVerbsAndTenses();

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
            Intent intent = new Intent(GradedPractice.this, LanguagePractice.class);
            intent.putExtra("currentLanguage", currentLanguage);
            intent.putExtra("tense", tense);
            intent.putExtra("nbrOfVerbs", returnMin(nbrOfVerbs, tense, currentLanguage));
            intent.putExtra("time", time);
            startActivity(intent);
        });
    //end of onCreate
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDropDownMenus();
    }

    //Checks the current language in the database and sets dropdown menus accordingly
    //lists were not updating after selection until I switched to setOnItemClickListener instead of onItemSelected
    //if user does not click in anything, variables are set to default values of "10" and "Present"
    public void setDropDownMenus() {
        databaseAccess.checkCurrentLanguage(language -> {
            setCurrentLanguage(language);
            //sets the adapter for number of verbs options
            countAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown_list, verbCount);
            autoCompleteCount.setAdapter(countAdapter);
            autoCompleteCount.setOnItemClickListener((adapterView, view, i, l) -> nbrOfVerbs = Integer.parseInt(adapterView.getItemAtPosition(i).toString()));

            //sets the adapter for tense options
            tensesAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown_list, getTenseArray(language));
            autoCompleteTenses.setAdapter(tensesAdapter);
            autoCompleteTenses.setOnItemClickListener((adapterView, view, i, l) -> tense = adapterView.getItemAtPosition(i).toString());

        });
    }

    private void setCurrentLanguage(String language) {
        this.currentLanguage = language;
    }

    //returns array with appropriate language tenses
    private String[] getTenseArray(String language) {
        if(language.equals("English")) return englishTenses;
        else return frenchTenses;
    }

    /*Used to check the minimum value between the number of verbs the user chooses and the
    number of verbs available for a specific tense */
    private int returnMin(int nbr, String tense, String language) {
        int arraySize;
        if(language.equals("English")) {
            arraySize = tense.equals("Past (irr)")
                    ? randomizeVerbsAndTenses.getFiftyIrregularVerbsEnglish().length
                    : randomizeVerbsAndTenses.getHundredMostCommonEnglish().length;
        }
        else {
            switch (tense) {
                case "Présent (-er)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonErFrench().length;
                    break;
                case "Présent (-ir)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonIrFrench().length;
                    break;
                case "Présent (-re)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonReFrench().length;
                    break;
                case "Présent (Réguliers)":
                    arraySize = randomizeVerbsAndTenses.getAllRegularsFrench().length;
                    break;
                case "Présent (Irréguliers)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonIrregularsFrench().length;
                    break;
                case "Présent (Réfléchis)":
                case "Passé Composé (Réfléchis)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonReflexiveFrench().length;
                    break;
                case "Passé Composé (avoir)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonAvoir().length;
                    break;
                case "Passé Composé (être)":
                    arraySize = randomizeVerbsAndTenses.getMostCommonEtre().length;
                    break;
                default: arraySize = randomizeVerbsAndTenses.getHundredMostCommonFrench().length;
            }
        }
        return Math.min(nbr, arraySize);
    }

}