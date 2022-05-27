package com.company.verbzz_app.Classes;

import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.company.verbzz_app.Classes.EnglishModelClasses.ModelClassEnglish;
import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;
import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseAccess {
    private final FirebaseDatabase databaseLanguage = FirebaseDatabase.getInstance();
    private final DatabaseReference languageReference = databaseLanguage.getReference();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();

    /* indexes created to match the first letter of the verb sought for with the database
    so the function looks for a range and not the entire database. 26 indexes represent a-z */
    private final int[] frenchStart = {0, 898, 1477, 2561, 4076, 5288, 5668, 5991, 6146, 6433, 6507, 6520, 6702, 7155, 7271, 7395, 8182, 8207, 9528, 10184, 10650, 10672, 10865, 10866, 10868, 10875};
    private final int[] frenchEnd = {897, 1476, 2560, 4075, 5287, 5667, 5990, 6145, 6432, 6506, 6519, 6701, 7154, 7270, 7394, 8181, 8206, 9527, 10183, 10649, 10671, 10864, 10865, 10867, 10874, 10895};
    private final int[] englishStart = {0, 45, 124, 212, 271, 313, 358, 389, 426, 461, 474, 483, 515, 551, 561, 577, 655, 660, 726, 882, 938, 953, 963, 1003, 1004, 1010};
    private final int[] englishEnd = {44, 123, 211, 270, 312, 357, 388, 425, 460, 473, 482, 514, 550, 560, 576, 654, 659, 725, 881, 937, 952, 962, 1002, 1003, 1009, 1010};

    public void saveCurrentLanguageToDatabase(String language) {
        CompletableFuture.runAsync(() -> {
            if (user != null) {
                String userUID = user.getUid();
                languageReference.child("Languages").child("Current Language")
                    .child(userUID).child("Current Language").setValue(language);
                languageReference.child("Languages").child("Languages Chosen")
                    .child(userUID).child(language).setValue(language);
            }
        });
    }

    public void checkCurrentLanguage(OnLanguageLoaded onLanguageLoaded) {
        CompletableFuture.runAsync(() -> {
            assert user != null;
            final String userUID = user.getUid();
            languageReference
                    .child("Languages")
                    .child("Current Language")
                    .child(userUID)
                    .child("Current Language")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            onLanguageLoaded.onLanguageLoaded(snapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
    }

    public void setBackgroundFlag(String language, ImageButton flag) {
        if(language == null) flag.setBackgroundResource(R.drawable.non_chosen_language);
        else if(language.equals("English")) {
            flag.setBackgroundResource(R.drawable.english_language);
        }
        else if(language.equals("Français")) {
            flag.setBackgroundResource(R.drawable.french_language);
        }
    }

    public int returnEnglishVerbPosition(List<ModelClassEnglish> data, String verb) {
        int index = (int)(verb.toLowerCase().charAt(0)) - 97;
        int result = -1;
        for (int i = englishStart[index]; i <= englishEnd[index]; i++) {
            if(data.get(i).getInfinitive().get(0).equals(verb)) {
                result = i;
                break;
            }
        }
        return result;
    }

    /*some french verbs start with accent letters, such as être or établir
    It only happens with "ôter" and verbs with "e", so if index points to higher than
    a-z letters, it's accented, so return "e" index (4) or "o" (14)*/
    public int returnFrenchVerbPosition(List<ModelClassFrench> data, String verb) {
        int index = (int)(verb.toLowerCase().charAt(0)) - 97;
        if(verb.equals("ôter")) index = 14;
        else if(index > 25) index = 4;
        int result = -1;
        for (int i = frenchStart[index]; i <= frenchEnd[index]; i++) {
            if(data.get(i).getInfinitif().getPrSent().get(0).equals(verb)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void callRetrofitFrench(OnFrenchDataLoaded onFrenchDataLoaded) {
        CompletableFuture.runAsync(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitAPIFrench retrofitAPIFrench = retrofit.create(RetrofitAPIFrench.class);

            Call<List<ModelClassFrench>> call = retrofitAPIFrench.getModelClass();
            call.enqueue(new Callback<List<ModelClassFrench>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelClassFrench>> call, @NonNull Response<List<ModelClassFrench>> response) {
                    onFrenchDataLoaded.onFrenchDataLoaded(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelClassFrench>> call, @NonNull Throwable t) {

                }
            });
        });
    }

    public void callRetrofitEnglish(OnEnglishDataLoaded onEnglishDataLoaded) {
        CompletableFuture.runAsync(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitAPIEnglish retrofitAPIEnglish = retrofit.create(RetrofitAPIEnglish.class);

            Call<List<ModelClassEnglish>> call = retrofitAPIEnglish.getModelClass();
            call.enqueue(new Callback<List<ModelClassEnglish>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelClassEnglish>> call, @NonNull Response<List<ModelClassEnglish>> response) {
                    onEnglishDataLoaded.onEnglishDataLoaded(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelClassEnglish>> call, @NonNull Throwable t) {

                }
            });
        });
    }

    //returns a list of verbs conjugated, one item for each pronoun;
    public List<String> returnVerbListEnglish(ModelClassEnglish verbData, String tense) {
        switch (tense) {
            case "Present":
                //I work
                return verbData.getIndicative().getPresent();
            case "Past":
                //I worked
                return verbData.getIndicative().getImperfect();
            case "Future":
                //I will work
                return verbData.getIndicative().getFuture();
            case "Present Perfect":
                //I have worked
                return verbData.getIndicative().getPerfect();
            case "Past Perfect":
                //I had worked
                return verbData.getIndicative().getPlusperfect();
            case "Future Perfect":
                //I will have worked
                return verbData.getIndicative().getPreviousFuture();
            case "Present Conditional":
                //I would work
                return verbData.getConditional().getConditional();
            case "Perfect Conditional":
                //I would have worked
                return verbData.getConditional().getConditionalPerfect();
            case "Present Subjunctive":
                //that I work
                return verbData.getSubjuntive().getPresent();
            case "Perfect Subjunctive":
                //I may have worked
                return verbData.getSubjuntive().getPerfect();
        }
        return null;
    }

    //Returns pronouns list that applies to different english verb tenses;
    public String[] returnPronounListEnglish(String tense) {
        switch (tense) {
            case "Present":
            case "Past":
                //I work
                //I worked
                return new String[]{"I ", "You ", "He/she/it ", "We ", "You ", "They "};
            case "Future":
                //I will work
                return new String[]{"I will ", "You will ", "He/she/it will ", "We will ", "You will ", "They will "};
            case "Present Perfect":
                //I have worked
                return new String[]{"I have ", "You have ", "He/she/it has ", "We have ", "You have ", "They have "};
            case "Past Perfect":
                //I had worked
                return new String[]{"I had ", "You had ", "He/she/it had ", "We had ", "You had ", "They had "};
            case "Future Perfect":
                //I will have worked
                return new String[]{"I will have ", "You will have ", "He/she/it will have ", "We will have ", "You will have ", "They will have "};
            case "Present Conditional":
                //I would work
                return new String[]{"I would ", "You would ", "He/she/it would ", "We would ", "You would ", "They would "};
            case "Perfect Conditional":
                //I would have worked
                return new String[]{"I would have ", "You would have ", "He/she/it would have ", "We would have ", "You would have ", "They would have "};
            case "Present Subjunctive":
                //that I work
                return new String[]{"that I ", "that you ", "that he/she/it ", "that we ", "that you ", "that they "};
            case "Perfect Subjunctive":
                //I may have worked
                return new String[]{"I may have ", "You may have ", "He/she/it may have ", "We may have ", "You may have ", "They may have "};
        }
        return null;
    }

    //Returns pronouns list that applies to different french verb tenses;
    public String[] returnPronounListFrench(String tense, String verb) {
        if(returnAuxiliary(verb).equals("Avoir")) {
            switch (tense) {
                case "Présent":
                case "Imparfait":
                case "Passé Simple":
                case "Futur Simple":
                case "Présent Conditionnel":
                    return new String[]{"Je ", "Tu ", "Il/elle/on ", "Nous ", "Vous ", "Ils/elles "};
                case "Passé Composé":
                    return new String[]{"J'ai ", "Tu as ", "Il/elle/on a ", "Nous avons ", "Vous avez ", "Ils/elles ont "};
                case "Plus-Que-Parfait":
                    return new String[]{"J'avais ", "Tu avais ", "Il/elle/on avait ", "Nous avions ", "Vous aviez ", "Ils/elles avaient "};
                case "Passé Antérieur":
                    return new String[]{"J'eus ", "Tu eus ", "Il/elle/on eut ", "Nous eûmes ", "Vous eûtes ", "Ils/elles eurent "};
                case "Futur Antérieur":
                    return new String[]{"J'aurais ", "Tu auras ", "Il/elle/on aura ", "Nous aurons ", "Vous aurez ", "Ils/elles auront "};
                case "Passé première forme":
                    return new String[]{"J'aurais ", "Tu auras ", "Il/elle/on aura ", "Nous aurions ", "Vous auriez ", "Ils/elles auraient "};
                case "Passé deuxième forme":
                    return new String[]{"J'eusse ", "Tu eusses ", "Il/elle/on eût ", "Nous eussions ", "Vous eussiez ", "Ils/elles eussent "};
                case "Passé Subjonctif":
                    return new String[]{"que j'aie ", "que tu as ", "qu'il/elle/on ait ", "que nous ayons ", "que vous ayez ", "Ils/elles aient "};
                case "Présent Subjonctif":
                case "Imparfait Subjonctif":
                    return new String[]{"que je ", "que tu ", "qu'il/elle/on ", "que nous ", "que vous ", "qu'ils/elles "};
                case "Plus-que-parfait Subjonctif":
                    return new String[]{"que j'eusse ", "que tu eusses ", "qu'il/elle/on eût ", "que nous eussions ", "que vous eussiez ", "qu'ils/elles eussent "};
            }
        }
        else {
            switch (tense) {
                case "Présent":
                case "Imparfait":
                case "Passé Simple":
                case "Futur Simple":
                case "Présent Conditionnel":
                    return new String[]{"Je ", "Tu ", "Il/elle/on ", "Nous ", "Vous ", "Ils/elles "};
                case "Passé Composé":
                    return new String[]{"Je suis ", "Tu es ", "Il/elle/on est ", "Nous sommes ", "Vous êtes ", "Ils/elles sont "};
                case "Plus-Que-Parfait":
                    return new String[]{"J'étais ", "Tu étais ", "Il/elle/on était ", "Nous étions ", "Vous étiez ", "Ils/elles étaient "};
                case "Passé Antérieur":
                    return new String[]{"Je fus ", "Tu fus ", "Il/elle/on fut ", "Nous fûmes ", "Vous fûtes ", "Ils/elles furent "};
                case "Futur Antérieur":
                    return new String[]{"Je serai ", "Tu seras ", "Il/elle/on sera ", "Nous serons ", "Vous serez ", "Ils/elles seront "};
                case "Passé première forme":
                    return new String[]{"Je serais ", "Tu serais ", "Il/elle/on serait ", "Nous serions ", "Vous seriez ", "Ils/elles seraient "};
                case "Passé deuxième forme":
                    return new String[]{"Je fusse ", "Tu fusses ", "Il/elle/on fût ", "Nous fussions ", "Vous fussiez ", "Ils/elles fussent "};
                case "Passé Subjonctif":
                    return new String[]{"que je sois ", "que tu sois ", "qu'il/elle/on soit ", "que nous soyons ", "que vous soyez ", "Ils/elles soient "};
                case "Présent Subjonctif":
                case "Imparfait Subjonctif":
                    return new String[]{"que je ", "que tu ", "qu'il/elle/on ", "que nous ", "que vous ", "qu'ils/elles "};
                case "Plus-que-parfait Subjonctif":
                    return new String[]{"que je fusse ", "que tu fusses ", "qu'il/elle/on fût ", "que nous fussions ", "que vous fussiez ", "qu'ils/elles fussent "};
            }
        }
        return null;
    }

    public String returnAuxiliary(String verb) {
        switch (verb) {
            case "aller":
            case "arriver":
            case "descendre":
            case "redescendre":
            case "entrer":
            case "rentrer":
            case "monter":
            case "remonter":
            case "mourir":
            case "naître":
            case "renaître":
            case "partir":
            case "repartir":
            case "passer":
            case "rester":
            case "retourner":
            case "sortir":
            case "ressortir":
            case "tomber":
            case "retomber":
            case "venir":
            case "devenir":
            case "parvenir":
            case "revenir":
                return "Etre";
            default:
                return "Avoir";
        }
    }

    //returns a list of verbs conjugated, one item for each pronoun;
    public List<String> returnVerbListFrench(ModelClassFrench verbData, String tense) {
        switch (tense) {
            case "Présent":
                //Je travaille
                return verbData.getIndicatif().getPrSent();
            case "Passé Composé":
                //J'ai travaillé
                return verbData.getIndicatif().getPassCompos();
            case "Imparfait":
                //Je travaillais
                return verbData.getIndicatif().getImparfait();
            case "Plus-Que-Parfait":
                //J'avais travaillé
                return verbData.getIndicatif().getPlusQueParfait();
            case "Passé Simple":
                //Je travaillai
                return verbData.getIndicatif().getPassSimple();
            case "Passé Antérieur":
                //J'eus travaillé
                return verbData.getIndicatif().getPassAntRieur();
            case "Futur Simple":
                //Je travaillerai
                return verbData.getIndicatif().getFuturSimple();
            case "Futur Antérieur":
                //J'aurai travaillé
                return verbData.getIndicatif().getFuturAntRieur();
            case "Présent Conditionnel":
                //Je travaillerais
                return verbData.getConditionnel().getPrSent();
            case "Passé première forme":
                //J'aurais travaillé
                return verbData.getConditionnel().getPass1ReForme();
            case "Passé deuxième forme":
                //J'eusse travaillé
                return verbData.getConditionnel().getPass2MeForme();
            case "Présent Subjonctif":
                //que je travaille
                return verbData.getSubjonctif().getPrSent();
            case "Passé Subjonctif":
                //que j'aie travaillé
                return verbData.getSubjonctif().getPass();
            case "Imparfait Subjonctif":
                //que je travaillasse
                return verbData.getSubjonctif().getImparfait();
            case "Plus-que-parfait Subjonctif":
                //que j'eusse travaillé
                return verbData.getSubjonctif().getPlusQueParfait();
        }
        return null;
    }

    public void saveStatsToDatabase(String tense, String scores, String date, String language) {
        CompletableFuture.runAsync(() -> {
            assert user != null;
            String userUID = user.getUid();
            //generates a random key for each object in the user's score
            String timeStampKey = languageReference.child("Languages")
                    .child("Scores").child(userUID).push().getKey();
            //sets an order variable that will be used to organize data chronologically
            assert timeStampKey != null;
            languageReference.child("Languages").child("Scores").child(userUID)
                    .child(timeStampKey).child("order").setValue(ServerValue.TIMESTAMP);
            //turns that order value number into negative so data can be organized from newest to oldest;
            languageReference.child("Languages").child("Scores").child(userUID)
                    .child(timeStampKey).child("order").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        if (!(Long.parseLong(snapshot.getValue().toString()) < 0)) {
                            languageReference.child("Languages").child("Scores").child(userUID)
                                    .child(timeStampKey).child("order")
                                    .setValue(-Long.parseLong(snapshot.getValue().toString()));
                        }
                        //saves data passed as arguments into database variables
                        //variables saved with lowercase pattern to match with Stats model class
                        languageReference.child("Languages").child("Scores").child(userUID)
                                .child(timeStampKey).child("tense").setValue(tense);
                        languageReference.child("Languages").child("Scores").child(userUID)
                                .child(timeStampKey).child("score").setValue(scores);
                        languageReference.child("Languages").child("Scores").child(userUID)
                                .child(timeStampKey).child("date").setValue(date);
                        languageReference.child("Languages").child("Scores").child(userUID)
                                .child(timeStampKey).child("language").setValue(language);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

}
