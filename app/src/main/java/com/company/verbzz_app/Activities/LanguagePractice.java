package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.EnglishModelClasses.ModelClassEnglish;
import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;
import com.company.verbzz_app.Classes.RandomizeVerbsAndTenses;
import com.company.verbzz_app.R;

import java.util.List;

public class LanguagePractice extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000;
    private long timeLeftInMilLis = START_TIME_IN_MILLIS;
    ImageView statistics, timerImage;
    TextView pronounAndVerb, status, timerCount, percentageCount, fractionCount, practiceTitle;
    EditText answerEntry;
    Button checkAnswer;
    String tense, nbrOfVerbs, time, currentLanguage, pronoun, verb;
    CountDownTimer timeToPlay;
    int pronounIndex, verbCount, rightAnswers, wrongAnswers;
    double percentage;
    private final DatabaseAccess databaseAccess = new DatabaseAccess();
    private final RandomizeVerbsAndTenses randomizeVerbsAndTenses = new RandomizeVerbsAndTenses();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_practice);

        statistics = findViewById(R.id.statistics);
        pronounAndVerb = findViewById(R.id.pronounAndVerb);
        status = findViewById(R.id.status);
        answerEntry = findViewById(R.id.answerEditText);
        checkAnswer = findViewById(R.id.checkAnswer);
        timerImage = findViewById(R.id.timerImage);
        timerCount = findViewById(R.id.timerCount);
        percentageCount = findViewById(R.id.percentageCount);
        fractionCount = findViewById(R.id.fractionCount);
        practiceTitle = findViewById(R.id.practiceTitle);

        Intent i = getIntent();
        tense = i.getStringExtra("tense");
        nbrOfVerbs = i.getStringExtra("nbrOfVerbs") ;
        time = i.getStringExtra("time");
        currentLanguage = i.getStringExtra("currentLanguage");

        practiceTitle.setText(tense);
        pronounAndVerb.setText(setPronounAndVerb());
        fractionCount.setText(formatFraction());
        percentageCount.setText(formatPercentage());

        //receives time info and sets timer if set to 5min
        if(time.equals("off")) {
            timerCount.setText(time);
            timerImage.setImageResource(R.drawable.ic_baseline_timer_off_24);
        }
        else {
            timerImage.setImageResource(R.drawable.ic_baseline_timer_24);
            timeToPlay = new CountDownTimer(timeLeftInMilLis, 1000) {
                @Override
                public void onTick(long l) {
                    String minutes = String.valueOf(timeLeftInMilLis / 1000 / 60);
                    String secsToFinish = String.valueOf(l / 1000).length() > 1
                            ? String.valueOf(l / 1000)
                            : "0" + (l / 1000);
                    timeLeftInMilLis = l;
                    timerCount.setText(String.format("%s:%s", minutes, secsToFinish));
                }

                @Override
                public void onFinish() {
                    goToStatistics();
                }
            }.start();
        }

        //Directs user to statistics fragment in main activity page
        statistics.setOnClickListener(view -> goToStatistics());

        //enables check answer button based on whether an answer was passed to field
        answerEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(status.getVisibility() == View.VISIBLE) status.setVisibility(View.INVISIBLE);
                checkAnswer.setEnabled(charSequence.toString().trim().length() != 0);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(status.getVisibility() == View.VISIBLE) status.setVisibility(View.INVISIBLE);
                checkAnswer.setEnabled(charSequence.toString().trim().length() != 0);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //checks if answer is right and resets components
        checkAnswer.setOnClickListener(view -> {
            String answer = answerEntry.getText().toString().trim();
            answerEntry.setText("");
            if (currentLanguage.equals("English")) {
                databaseAccess.callRetrofitEnglish(data -> {
                    int verbIndexOnDatabase = databaseAccess.returnEnglishVerbPosition(data, verb);
                    ModelClassEnglish verbData = data.get(verbIndexOnDatabase);
                    List<String> list = databaseAccess.returnVerbListEnglish(verbData, transformEnglishTense(tense));
                    formatComponents(answer, list);
                });
            }
            else {
                databaseAccess.callRetrofitFrench(data -> {
                    int verbIndexOnDatabase = databaseAccess.returnFrenchVerbPosition(data, verb);
                    ModelClassFrench verbData = data.get(verbIndexOnDatabase);
                    List<String> list = databaseAccess.returnVerbListFrench(verbData, transformEnglishTense(tense));
                    formatComponents(answer, list);
                });
            }
        });
        //end of onCreate
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeToPlay.cancel();
    }

    //formats components to green or red based on whether answer passed by user is right or wrong
    private void formatComponents(String answer, List<String> list) {
        //some verbs in the database have two forms, so there's not necessarily 6 options to choose
        if(answer.equals(list.size() <= 6 ? list.get(pronounIndex) : list.get(pronounIndex * 2))) {
            status.setText(R.string.correct);
            status.setTextColor(Color.GREEN);
            status.setVisibility(View.VISIBLE);
            rightAnswers++;
        }
        else {
            status.setText(String.format("%s %s", getString(R.string.status)
                    , list.size() <= 6? list.get(pronounIndex) : list.get(pronounIndex * 2)));
            status.setTextColor(Color.RED);
            status.setVisibility(View.VISIBLE);
            wrongAnswers++;
        }
        fractionCount.setText(formatFraction());
        percentageCount.setText(formatPercentage());
        pronounAndVerb.setText(setPronounAndVerb());
    }

    private String setPronounAndVerb() {
        //there are 6 pronouns, so index can be from 0 to 5 and here a random index is returned
        pronounIndex = randomizeVerbsAndTenses.returnRandomIndex(5);
        //sets a random number within the range of verbs available for a specific tense list;
        int verbIndex = randomizeVerbsAndTenses.returnRandomIndex(Integer.parseInt(nbrOfVerbs));

        if(currentLanguage.equals("English")) {
            pronoun = databaseAccess.returnPronounListEnglish(transformEnglishTense(tense))[pronounIndex];
            verb = tense.equals("Past")
                    ? randomizeVerbsAndTenses.getFiftyIrregularVerbsEnglish()[verbIndex]
                    : randomizeVerbsAndTenses.getHundredMostCommonEnglish()[verbIndex];
        }
        else {
            switch (tense) {
                case "Présent (-er)":
                    verb = randomizeVerbsAndTenses.getMostCommonErFrench()[verbIndex];
                case "Présent (-ir)" :
                    verb = randomizeVerbsAndTenses.getMostCommonIrFrench()[verbIndex];
                case "Présent (-re)":
                    verb = randomizeVerbsAndTenses.getMostCommonReFrench()[verbIndex];
                case "Présent (Réguliers)":
                    verb = randomizeVerbsAndTenses.getAllRegularsFrench()[verbIndex];
                case "Présent (Irréguliers)":
                    verb = randomizeVerbsAndTenses.getMostCommonIrregularsFrench()[verbIndex];
                case "Présent (Réfléchis)":
                case "Passé Composé (Réfléchis)":
                    verb  = randomizeVerbsAndTenses.getMostCommonReflexiveFrench()[verbIndex];
                case "Passé Composé (avoir)":
                    verb = randomizeVerbsAndTenses.getMostCommonAvoir()[verbIndex];
                case "Passé Composé (être)":
                    verb = randomizeVerbsAndTenses.getMostCommonEtre()[verbIndex];
                default: verb = randomizeVerbsAndTenses.getHundredMostCommonFrench()[verbIndex];

                pronoun = databaseAccess.returnPronounListFrench(transformFrenchTense(tense), verb)[pronounIndex];
            }
        }
        verbCount++;
        return String.format("%s%s", pronoun, verb);
    }

    /*conjugation lessons separates a few special cases for better user experience,
    this method makes these options comply again with the normal tenses set in the database */
    private String transformEnglishTense(String tense) {
        if (tense.equals("Past (irr)")) {
            return "Past";
        }
        return tense;
    }

    /*conjugation lessons separates a few special cases for better user experience,
    this method makes these options comply again with the normal tenses set in the database */
    private String transformFrenchTense(String tense) {
         switch (tense) {
            case "Présent (-er)":
            case "Présent (-ir)":
            case "Présent (-re)":
            case "Présent (Réguliers)":
            case "Présent (Irréguliers)":
            case "Présent (Réfléchis)":
            case  "Présent (Tous)":
                return "Présent";
            case "Passé Composé (avoir)":
            case "Passé Composé (être)":
            case "Passé Composé (Réfléchis)":
            case "Passé Composé (Tous)":
                return "Passé Composé";
            default: return tense;
        }
    }

    //formats the text that is displayed in the rightAnswer/totalVerbs at the corner of the screen
    private String formatFraction() {
        return String.format("%s/%s", rightAnswers, verbCount);
    }

    //Formats percentage of right verbs
    private String formatPercentage() {
        percentage = ((double) rightAnswers / (double) verbCount) * 100;
        return String.format("%s%s", (int) percentage, "%");
    }

    private void goToStatistics() {
        Intent intent = new Intent(LanguagePractice.this, MainActivity.class);
        intent.putExtra("setStatistics", true);
        startActivity(intent);
        finish();
    }

    //sends stats to database to be displayed in statistics page;
    private void sendDataToStats() {
        //set this method on database access
    }


}