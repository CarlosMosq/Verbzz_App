package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.EnglishModelClasses.ModelClassEnglish;
import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;
import com.company.verbzz_app.Classes.RandomizeVerbsAndTenses;
import com.company.verbzz_app.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LanguagePractice extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000;
    private long timeLeftInMilLis = START_TIME_IN_MILLIS;
    ImageView statistics, timerImage, honeyCombPractice;
    TextView pronounAndVerb, status, timerCount, percentageCount, fractionCount, practiceTitle;
    EditText answerEntry;
    Button checkAnswer;
    String tense, time, currentLanguage, pronoun, verb;
    CountDownTimer timeToPlay;
    int pronounIndex, verbCount, rightAnswers, wrongAnswers, nbrOfVerbs;
    double percentage;
    private final DatabaseAccess databaseAccess = new DatabaseAccess();
    private final RandomizeVerbsAndTenses randomizeVerbsAndTenses = new RandomizeVerbsAndTenses();
    private MediaPlayer bellSound, errorSound;
    private InterstitialAd mInterstitialAd;

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
        honeyCombPractice = findViewById(R.id.honeyCombPractice);
        timerCount = findViewById(R.id.timerCount);
        percentageCount = findViewById(R.id.percentageCount);
        fractionCount = findViewById(R.id.fractionCount);
        practiceTitle = findViewById(R.id.practiceTitle);
        bellSound = MediaPlayer.create(this, R.raw.bell);
        errorSound = MediaPlayer.create(this, R.raw.error);

        Intent i = getIntent();
        time = i.getStringExtra("time");
        tense = i.getStringExtra("tense");
        currentLanguage = i.getStringExtra("currentLanguage");
        nbrOfVerbs = i.getIntExtra("nbrOfVerbs", 10);

        practiceTitle.setText(tense);
        pronounAndVerb.setText(setPronounAndVerb());
        fractionCount.setText(formatFraction());
        percentageCount.setText(formatPercentage());

        //Ads set up
        MobileAds.initialize(this, initializationStatus -> {

        });
        loadInterstitialAd();

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
                    timeLeftInMilLis = l;
                    int minutes = (int) timeLeftInMilLis / 1000 / 60;
                    int secsToFinish = (int) (l / 1000) % 60;
                    timerCount.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, secsToFinish));
                }

                @Override
                public void onFinish() {
                    showAd();
                }
            }.start();
        }

        //Directs user to statistics fragment in main activity page
        statistics.setOnClickListener(view -> showAd());

        //Directs the user to main fragment in main activity page
        honeyCombPractice.setOnClickListener(view -> goToMain());

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
                    List<String> list = databaseAccess.returnVerbListFrench(verbData, transformFrenchTense(tense));
                    formatComponents(answer, list);
                });
            }
        });

        //end of onCreate
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timeToPlay != null) {
            timeToPlay.cancel();
        }
    }

    //formats components to green or red based on whether answer passed by user is right or wrong
    private void formatComponents(String answer, List<String> list) {
        //some verbs in the database have two forms, so there's not necessarily 6 options to choose
        if(answer.equals(list.size() <= 6 ? list.get(pronounIndex) : list.get(pronounIndex * 2))) {
            status.setText(getString(R.string.correct));
            status.setTextColor(Color.GREEN);
            if(bellSound.isPlaying()) {
                bellSound.seekTo(0);
            }
            bellSound.start();
            status.setVisibility(View.VISIBLE);
            rightAnswers++;
        }
        else {
            status.setText(String.format("%s %s", getString(R.string.status)
                    , list.size() <= 6? list.get(pronounIndex) : list.get(pronounIndex * 2)));
            status.setTextColor(Color.RED);
            if(errorSound.isPlaying()) {
                errorSound.seekTo(0);
            }
            errorSound.start();
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
        int verbIndex = randomizeVerbsAndTenses.returnRandomIndex(nbrOfVerbs);

        if(currentLanguage.equals("English")) {
            pronoun = databaseAccess.returnPronounListEnglish(transformEnglishTense(tense))[pronounIndex];
            verb = tense.equals("Past (irr)")
                    ? randomizeVerbsAndTenses.getFiftyIrregularVerbsEnglish()[verbIndex]
                    : randomizeVerbsAndTenses.getHundredMostCommonEnglish()[verbIndex];
        }
        else {
            switch (tense) {
                case "Présent (-er)":
                    verb = randomizeVerbsAndTenses.getMostCommonErFrench()[verbIndex];
                    break;
                case "Présent (-ir)" :
                    verb = randomizeVerbsAndTenses.getMostCommonIrFrench()[verbIndex];
                    break;
                case "Présent (-re)":
                    verb = randomizeVerbsAndTenses.getMostCommonReFrench()[verbIndex];
                    break;
                case "Présent (Réguliers)":
                    verb = randomizeVerbsAndTenses.getAllRegularsFrench()[verbIndex];
                    break;
                case "Présent (Irréguliers)":
                    verb = randomizeVerbsAndTenses.getMostCommonIrregularsFrench()[verbIndex];
                    break;
                case "Présent (Réfléchis)":
                case "Passé Composé (Réfléchis)":
                    verb  = randomizeVerbsAndTenses.getMostCommonReflexiveFrench()[verbIndex];
                    break;
                case "Passé Composé (avoir)":
                    verb = randomizeVerbsAndTenses.getMostCommonAvoir()[verbIndex];
                    break;
                case "Passé Composé (être)":
                    verb = randomizeVerbsAndTenses.getMostCommonEtre()[verbIndex];
                    break;
                default: verb = randomizeVerbsAndTenses.getHundredMostCommonFrench()[verbIndex];
            }
            pronoun = databaseAccess.returnPronounListFrench(transformFrenchTense(tense), verb)[pronounIndex];
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

    //Formats percentage of right verbs that is displayed in the bottom right corner
    private String formatPercentage() {
        percentage = ((double) rightAnswers / (double) verbCount) * 100;
        return String.format("%s%s", (int) percentage, "%");
    }

    //Directs user to statistics page, while saving score to database;
    private void goToStatistics() {
        Intent intent = new Intent(LanguagePractice.this, MainActivity.class);
        intent.putExtra("setStatistics", true);
        sendDataToStats();
        startActivity(intent);
        finish();
    }

    //Directs user to main page, while saving score to database;
    private void goToMain() {
        Intent intent = new Intent(LanguagePractice.this, MainActivity.class);
        sendDataToStats();
        startActivity(intent);
        finish();
    }

    //sends stats to database to be displayed in statistics page;
    private void sendDataToStats() {
        String date = new Date().toString();
        databaseAccess.saveStatsToDatabase(tense
                , String.format("%s (%s)", formatFraction(), formatPercentage())
                , date,
                currentLanguage);
    }

    //Logic behind Ads set up to show after a lesson is finished
    public void showAd() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(LanguagePractice.this);
        }
        else {
            Log.d("Ad-test", "Ad was null");
        }
    }

    //Logic behind Ads set up to show after a lesson is finished
    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.test_Interstitial_Ad_ID), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.d("Ad-test", "Ad loaded successfully");
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Log.d("Ad-test", "Ad failed to show");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Log.d("Ad-test", "Ad shown");
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        goToStatistics();
                        Log.d("Ad-test", "Ad dismissed");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("Ad-test", "Ad failed");
                mInterstitialAd = null;
            }
        });
    }

}