package com.example.letscook.EdamamApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NutrientApi {
    @GET("nutrition-data")
    Call<NutrientApiResponse> getNutritionData(
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("nutrition-type") String nutritionType,
            @Query("ingr") String ingredient
    );
}

