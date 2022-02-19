package com.company.verbzz_app.Classes;

import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPIFrench {

    @GET("CarlosMosq/Verbes-Francais-Conjugues/master/verbs_without_signs.json")
    Call<List<ModelClassFrench>> getModelClass();

}
