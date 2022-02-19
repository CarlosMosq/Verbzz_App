package com.company.verbzz_app.Classes;

import com.company.verbzz_app.Classes.EnglishModelClasses.ModelClassEnglish;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPIEnglish {

    @GET("CarlosMosq/English-Verbs-Conjugates/master/verbs-conjugations.json")
    Call<List<ModelClassEnglish>> getModelClass();
}
