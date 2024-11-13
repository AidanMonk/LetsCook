package com.example.letscook.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.letscook.api.model.Recipe;
import com.example.letscook.api.model.Root;

public interface apiservice {
    @GET("recipes/random")
    Call<Root> getRecipe(
            @Query("apiKey") String apiKey, @Query("number") int number
    );
}
