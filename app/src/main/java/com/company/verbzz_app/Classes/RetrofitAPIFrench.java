package com.company.verbzz_app.Classes;

import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPIFrench {

    @GET("Drulac/Verbes-Francais-Conjugues/master/verbes_lowercase.json")
    Call<List<ModelClassFrench>> getModelClass();

}
