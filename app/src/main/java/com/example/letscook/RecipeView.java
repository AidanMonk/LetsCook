package com.example.letscook;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letscook.Adapters.RecipeIngredientAdapter1;
import com.example.letscook.Adapters.RecipeIngredientAdapter2;
import com.example.letscook.Adapters.RecipeStepsAdapter2;
import com.example.letscook.Models.Recipe;

import java.util.Map;

public class RecipeView extends AppCompatActivity {

    TextView recipeName, recipeDesc, caloriesTV, fatTV, carbsTV, proteinTV, fibreTV, cholesterolTV, sodiumTV, servingsET, noNutritionTV;
    ImageView imagePreview;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    RecipeIngredientAdapter2 recipeIngredientAdapter2;
    RecipeStepsAdapter2 recipeStepsAdapter2;
    LinearLayout nutritionBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_view);
        initializeViews();

        String recipeId = getIntent().getSerializableExtra("recipeId").toString();

        Database.getRecipe(recipeId, new Database.RecipeCallback() {
            @Override
            public void onCallback(Recipe recipe) {
                if (recipe != null) {
                    //after recipe is accessed from database
                    Database.getImageUrl(recipe.getImageId(), new Database.UrlCallback(){
                        @Override
                        public void onUrlLoaded(String url) {
                            Glide.with(imagePreview.getContext())
                                    .load(url)
                                    .into(imagePreview);
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.d("RecipeView", "Image ID: " + recipe.getImageId());
                        }
                    });
                    setTextViews(recipe);
                }
            }
        });
    }

    private void setTextViews(Recipe recipe) {
        recipeName.setText(recipe.getName());
        recipeDesc.setText(recipe.getDescription());
        servingsET.setText(String.valueOf(recipe.getServings()));

        recipeIngredientAdapter2 = new RecipeIngredientAdapter2(this, recipe.getIngredients());
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter2);

        recipeStepsAdapter2 = new RecipeStepsAdapter2(this, recipe.getSteps());
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(recipeStepsAdapter2);

        try {
            int servings = recipe.getServings();
            if (servings == 0) servings = 1; // Prevent division by zero

            Map<String, Double> nutrientMap = recipe.getNutritionalInfo().getNutrientMap();

            double calories = nutrientMap.getOrDefault("calories", 0.0);
            double fat = nutrientMap.getOrDefault("fat", 0.0);
            double carbs = nutrientMap.getOrDefault("carbs", 0.0);
            double protein = nutrientMap.getOrDefault("protein", 0.0);
            double fibre = nutrientMap.getOrDefault("fibre", 0.0);
            double cholesterol = nutrientMap.getOrDefault("cholesterol", 0.0);
            double sodium = nutrientMap.getOrDefault("sodium", 0.0);

            caloriesTV.setText(String.valueOf(Math.round(calories / servings)));
            fatTV.setText("Fat: " + String.valueOf(Math.round(fat / servings)) + "g");
            carbsTV.setText("Carbs: " + String.valueOf(Math.round(carbs / servings))+ "g");
            proteinTV.setText("Protein: " + String.valueOf(Math.round(protein / servings))+ "g");
            fibreTV.setText("Fibre: " + String.valueOf(Math.round(fibre / servings))+ "g");
            cholesterolTV.setText("Cholesterol: " + String.valueOf(Math.round(cholesterol / servings))+ "mg");
            sodiumTV.setText("Sodium: " + String.valueOf(Math.round(sodium / servings))+ "mg");
        }
        catch (Exception e){
            nutritionBlock.setVisibility(View.GONE);
            noNutritionTV.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Error getting nutritional info", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews(){
        recipeName = findViewById(R.id.recipeName);
        recipeDesc = findViewById(R.id.recipeDesc);
        imagePreview = findViewById(R.id.imagePreview);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        stepsRecyclerView = findViewById(R.id.stepsRecyclerView);
        caloriesTV = findViewById(R.id.caloriesTV);
        fatTV = findViewById(R.id.fatTV);
        carbsTV = findViewById(R.id.carbsTV);
        proteinTV = findViewById(R.id.proteinTV);
        fibreTV = findViewById(R.id.fibreTV);
        cholesterolTV = findViewById(R.id.cholesterolTV);
        sodiumTV = findViewById(R.id.sodiumTV);
        nutritionBlock = findViewById(R.id.NutritionBlock);
        noNutritionTV = findViewById(R.id.noNutritionTV);
        servingsET = findViewById(R.id.servingsET);
    }
}