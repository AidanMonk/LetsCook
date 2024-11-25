package com.example.letscook;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.letscook.api.RetrofitClient;
//import com.example.letscook.api.apiservice;
//import com.example.letscook.api.model.Root;
//import com.example.letscook.api.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Homepage extends Base_activity {

//    private RecyclerView recipesRecycle;
//    private com.example.letscook.RecipeAdapter recipeAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_homepage);
//
//        // Initialize RecyclerView
//        recipesRecycle = findViewById(R.id.recipeRecyclerView);
//        recipesRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//
//        // Get Retrofit instance
//        Retrofit retrofit = RetrofitClient.getClient();
//
//        // Create ApiService instance
//        apiservice apService = retrofit.create(apiservice.class);
//
//
//        Call<Root> call = apService.getRecipe("5b94bfffee97410eae2659dde306f93c", 50);
//
//
//        call.enqueue(new Callback<Root>() {
//            @Override
//            public void onResponse(Call<Root> call, Response<Root> response) {
//                if (response.isSuccessful()) {
//                    Root root = response.body();
//                    if (root != null && root.recipes != null) {
//                        ArrayList<Recipe> recipes = root.recipes;
//                        recipeAdapter = new com.example.letscook.RecipeAdapter(recipes);
//                        recipesRecycle.setAdapter(recipeAdapter);
//                    } else {
//                        Toast.makeText(Homepage.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(Homepage.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Root> call, Throwable t) {
//                Toast.makeText(Homepage.this, "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
