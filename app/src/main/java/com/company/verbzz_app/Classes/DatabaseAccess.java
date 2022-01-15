package com.company.verbzz_app.Classes;

import android.widget.Button;

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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

    private final int[] frenchStart = {0, 898, 1477, 2561, 4076, 5288, 5668, 5991, 6146, 6433, 6507, 6520, 6702, 7155, 7271, 7395, 8182, 8207, 9528, 10184, 10650, 10672, 10865, 10866, 10868, 10875};
    private final int[] frenchEnd = {897, 1476, 2560, 4075, 5287, 5667, 5990, 6145, 6432, 6506, 6519, 6701, 7154, 7270, 7394, 8181, 8206, 9527, 10183, 10649, 10671, 10864, 10865, 10867, 10874, 10895};
    private final int[] englishStart = {0, 45, 124, 212, 271, 313, 358, 389, 426, 461, 474, 483, 515, 551, 561, 577, 655, 660, 726, 882, 938, 953, 963, 1003, 1004, 1010};
    private final int[] englishEnd = {44, 123, 211, 270, 312, 357, 388, 425, 460, 473, 482, 514, 550, 560, 576, 654, 659, 725, 881, 937, 952, 962, 1002, 1003, 1009, 1010};

    public void checkCurrentLanguage(OnLanguageLoaded onLanguageLoaded) {
        assert user != null;
        final String userUID = user.getUid();
        languageReference
                .child("Languages")
                .child("Current Language")
                .child(userUID)
                .child("Current Language")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentLanguage = snapshot.getValue(String.class);
                        onLanguageLoaded.onLanguageLoaded(currentLanguage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void setBackgroundFlag(String language, Button flag) {
        if(language == null) flag.setBackgroundResource(R.drawable.non_chosen_language);
        else if(language.equals("English")) {
            flag.setBackgroundResource(R.drawable.english_language);
        }
        else if(language.equals("French")) {
            flag.setBackgroundResource(R.drawable.french_language);
        }
    }

    public int returnEnglishVerbPosition(List<ModelClassEnglish> data, String verb) {
        int index = (int)(verb.toLowerCase().charAt(0)) - 97;
        int result = -1;
        for (int i = englishStart[index]; i <= englishEnd[index]; i++) {
            if(data.get(i).getInfinitive().get(0).equals(verb)) {
                result = i;
            }
        }
        return result;
    }

    public int returnFrenchVerbPosition(List<ModelClassFrench> data, String verb) {
        int index = (int)(verb.toLowerCase().charAt(0)) - 97;
        int result = -1;
        for (int i = frenchStart[index]; i <= frenchEnd[index]; i++) {
            if(data.get(i).getInfinitif().getPrSent().get(0).equals(verb)) {
                result = i;
            }
        }
        return result;
    }


    public void callRetrofitFrench(OnFrenchDataLoaded onFrenchDataLoaded) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPIFrench retrofitAPIFrench = retrofit.create(RetrofitAPIFrench.class);

        Call<List<ModelClassFrench>> call = retrofitAPIFrench.getModelClass();
        call.enqueue(new Callback<List<ModelClassFrench>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelClassFrench>> call, @NonNull Response<List<ModelClassFrench>> response) {
                List<ModelClassFrench> data = response.body();
                onFrenchDataLoaded.onFrenchDataLoaded(data);
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelClassFrench>> call, @NonNull Throwable t) {

            }
        });
    }

    public void callRetrofitEnglish(OnEnglishDataLoaded onEnglishDataLoaded) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPIEnglish retrofitAPIEnglish = retrofit.create(RetrofitAPIEnglish.class);

        Call<List<ModelClassEnglish>> call = retrofitAPIEnglish.getModelClass();
        call.enqueue(new Callback<List<ModelClassEnglish>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelClassEnglish>> call, @NonNull Response<List<ModelClassEnglish>> response) {
                List<ModelClassEnglish> data = response.body();
                onEnglishDataLoaded.onEnglishDataLoaded(data);
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelClassEnglish>> call, @NonNull Throwable t) {

            }
        });
    }


}
