package com.example.letscook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //testing Recipe

        //initialize ingredients
        Map<String,Ingredient> ingredients = initializeIngredients();

        //initialize ingredient quantities
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredient(ingredients.get("flour"), 2, Measurement.Cups));
        recipeIngredients.add(new RecipeIngredient(ingredients.get("sugar"), 1, Measurement.Tablespoons));
        recipeIngredients.add(new RecipeIngredient(ingredients.get("salt"), 1, Measurement.Teaspoons));

        //initialize recipe steps
        List<String> recipeSteps = new ArrayList<>();
        recipeSteps.add("add flour, sugar, and salt to a bowl");
        recipeSteps.add("mix");
        recipeSteps.add("enjoy");

        Recipe recipe = new Recipe("bag o powder", "fred frickerton", "you're going to need a glass of water after this one", recipeIngredients, recipeSteps);

    }

    //temp class for testing, will want to initialize ingredients from a table in the database
    private Map<String,Ingredient> initializeIngredients(){
        Map<String,Ingredient> ingredients = new HashMap<>();
        ingredients.put("flour", new Ingredient("flour", IngredientCategory.Grain));
        ingredients.put("sugar", new Ingredient("sugar", IngredientCategory.Grain));
        ingredients.put("salt", new Ingredient("salt", IngredientCategory.Spice));

        return ingredients;
    }
}