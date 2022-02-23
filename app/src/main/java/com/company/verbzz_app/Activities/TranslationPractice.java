package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.company.verbzz_app.Classes.DatabaseAccess;
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
import java.util.Locale;

public class TranslationPractice extends AppCompatActivity {

    String[] englishVerbs = {"to be", "to have", "to be able (can)", "to do", "to put, to place"
            , "to say", "to have to, must, to owe", "to take, to catch, to capture", "to give"
            , "to go", "to want, to wish", "to know", "to have to", "to see", "to ask, to request"
            , "to find, to discover", "to return (smth), to give back", "to come", "to pass"
            , "to understand", "to stay", "to hold, to keep", "to carry", "to speak", "to show"
            , "to continue", "to think", "to follow", "to know (sb)", "to believe", "to begin, to start"
            , "to count", "to hear", "to wait", "to put back", "to call", "to allow", "to occupy"
            , "to become", "to leave", "to decide", "to arrive", "to serve", "to seem", "to return"
            , "to let", "to receive", "to answer", "to live", "to call back", "to present"
            , "to accept", "to act", "to put down", "to play, to act", "to recognize", "to choose"
            , "to touch", "to love", "to find, to regain", "to lose", "to explain", "to consider"
            , "to open", "to win", "to exist", "to refuse", "to read", "to succeed", "to change"
            , "to work", "to represent", "to assure", "to try", "to prevent", "to go out, to exit"
            , "to resume", "to lead", "to belong", "to risk", "to concern", "to learn, to teach"
            , "to meet", "to create", "to obtain, to get", "to look for, to seek", "to enter"
            , "to suggest", "to bring", "to use", "to reach, to achieve", "to tempt"
            , "to matter", "to add", "to produce", "to prepare", "to raise, to stand up, to pick up"
            , "to write", "to defend", "to pull"};

    private final String[] frenchVerbs = {"être", "avoir", "pouvoir", "faire"
            , "mettre", "dire", "devoir", "prendre", "donner", "aller", "vouloir", "savoir"
            , "falloir", "voir", "demander", "trouver", "rendre", "venir", "passer"
            , "comprendre", "rester", "tenir", "porter", "parler", "montrer", "continuer"
            , "penser", "suivre", "connaître", "croire", "commencer", "compter", "entendre"
            , "attendre", "remettre", "appeler", "permettre", "occuper", "devenir", "partir"
            , "décider", "arriver", "servir", "sembler", "revenir", "laisser", "recevoir"
            , "répondre", "vivre", "rappeler", "présenter", "accepter", "agir", "poser"
            , "jouer", "reconnaître", "choisir", "toucher", "aimer", "retrouver", "perdre"
            , "expliquer", "considérer", "ouvrir", "gagner", "exister", "refuser", "lire"
            , "réussir", "changer", "travailler", "représenter", "assurer", "essayer"
            , "empêcher", "sortir", "reprendre", "mener", "appartenir", "risquer"
            , "concerner", "apprendre", "rencontrer", "créer", "obtenir", "chercher", "entrer"
            , "proposer", "apporter", "utiliser", "atteindre", "tenter", "importer", "ajouter"
            , "produire", "préparer", "relever", "écrire", "défendre", "tirer"};

    private static final long START_TIME_IN_MILLIS = 300000;
    private long timeLeftInMilLis = START_TIME_IN_MILLIS;
    private static final int oneHundredVerbs = 99;
    ImageView statistics, timerImage, honeyCombPractice;
    TextView verb, status, timerCount, percentageCount, fractionCount;
    EditText answerEntry;
    Button checkAnswer;
    private String currentLanguage;
    CountDownTimer timeToPlay;
    SwitchCompat languageSwitch;
    boolean switchLanguage = false;
    int verbCount, rightAnswers, wrongAnswers, verbIndex;
    double percentage;
    private final DatabaseAccess databaseAccess = new DatabaseAccess();
    private final RandomizeVerbsAndTenses randomizeVerbsAndTenses = new RandomizeVerbsAndTenses();
    private MediaPlayer bellSound, errorSound;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_practice);

        Intent i = getIntent();
        currentLanguage = i.getStringExtra("currentLanguage");

        statistics = findViewById(R.id.statisticsTranslation);
        verb = findViewById(R.id.pronounAndVerbTranslation);
        status = findViewById(R.id.statusTranslation);
        answerEntry = findViewById(R.id.answerEditTextTranslation);
        checkAnswer = findViewById(R.id.checkAnswerTranslation);
        timerImage = findViewById(R.id.timerImageTranslation);
        honeyCombPractice = findViewById(R.id.honeyCombTranslation);
        timerCount = findViewById(R.id.timerCountTranslation);
        percentageCount = findViewById(R.id.percentageCountTranslation);
        fractionCount = findViewById(R.id.fractionCountTranslation);
        languageSwitch = findViewById(R.id.languageSwitch);
        bellSound = MediaPlayer.create(this, R.raw.bell);
        errorSound = MediaPlayer.create(this, R.raw.error);

        //Ads set up
        MobileAds.initialize(this, initializationStatus -> {

        });
        loadInterstitialAd();

        //Switches which language to translate from
        languageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            switchLanguage = b;
            formatViews();
        });

        verb.setText(setVerb(switchLanguage));
        fractionCount.setText(formatFraction());
        percentageCount.setText(formatPercentage());

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
            formatComponents(answer, returnOppositeList(currentLanguage, switchLanguage));
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
    private void formatComponents(String answer, String[] toCompare) {
        if(answer.equals(toCompare[verbIndex])) {
            status.setText(R.string.correct);
            status.setTextColor(Color.GREEN);
            if(bellSound.isPlaying()) {
                bellSound.seekTo(0);
            }
            bellSound.start();
            status.setVisibility(View.VISIBLE);
            rightAnswers++;
        }
        else if (toCompare[verbIndex].contains(answer)) {
            status.setText(toCompare[verbIndex]);
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
                    , toCompare[verbIndex]));
            status.setTextColor(Color.RED);
            if(errorSound.isPlaying()) {
                errorSound.seekTo(0);
            }
            errorSound.start();
            status.setVisibility(View.VISIBLE);
            wrongAnswers++;
        }
        formatViews();
    }

    private void formatViews() {
        fractionCount.setText(formatFraction());
        percentageCount.setText(formatPercentage());
        verb.setText(setVerb(switchLanguage));
    }

    private String setVerb(boolean whichLanguage) {
        //this activity works with exactly 100 verbs
        verbIndex = randomizeVerbsAndTenses.returnRandomIndex(oneHundredVerbs);
        verbCount++;
        if(currentLanguage.equals("English") && !whichLanguage) {
            return englishVerbs[verbIndex];
        }
        else if(currentLanguage.equals("Français") && whichLanguage) {
            return englishVerbs[verbIndex];
        }
        else return frenchVerbs[verbIndex];
    }

    private String[] returnOppositeList(String language, boolean whichLanguage) {
        if(language.equals("English") && !whichLanguage) {
            return frenchVerbs;
        }
        else if(language.equals("Français") && whichLanguage) {
            return frenchVerbs;
        }
        else return englishVerbs;
    }

    private void goToStatistics() {
        Intent intent = new Intent(TranslationPractice.this, MainActivity.class);
        intent.putExtra("setStatistics", true);
        sendDataToStats();
        startActivity(intent);
        finish();
    }

    private void goToMain() {
        Intent intent = new Intent(TranslationPractice.this, MainActivity.class);
        sendDataToStats();
        startActivity(intent);
        finish();
    }

    //sends stats to database to be displayed in statistics page;
    private void sendDataToStats() {
        verbCount--;
        String date = new Date().toString();
        databaseAccess.saveStatsToDatabase("Translation"
                , String.format("%s (%s)", formatFraction(), formatPercentage())
                , date,
                currentLanguage);
    }

    //formats the text that is displayed in the rightAnswer/totalVerbs at the corner of the screen
    private String formatFraction() {
        return String.format("%s/%s", rightAnswers, verbCount);
    }

    //Formats percentage of right translations
    private String formatPercentage() {
        percentage = ((double) rightAnswers / (double) verbCount) * 100;
        return String.format("%s%s", (int) percentage, "%");
    }

    public void showAd() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(TranslationPractice.this);
        }
    }

    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.test_Interstitial_Ad_ID), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        goToStatistics();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
            }
        });
    }

}