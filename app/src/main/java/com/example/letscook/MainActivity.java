package com.example.letscook;

import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText recipeNameET, recipeDescET, quantityET, ingredientET;
    Button addIngredientBtn;
    Spinner measurementSpinner;
    RecyclerView ingredientsRecyclerView;

    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recipeNameET = findViewById(R.id.recipeNameET);
        recipeDescET = findViewById(R.id.recipeDescET);
        quantityET = findViewById(R.id.quantityET);
        measurementSpinner = findViewById(R.id.measurementSpinner);
        ingredientET = findViewById(R.id.ingredientET);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        //populate spinner
        measurementSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Measurement.getMeasurementStrings()));

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(this, recipeIngredients);
        ingredientsRecyclerView = findViewById(R.id.recipeIngredientsRV);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter);

        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter);

        //add ingredient button
        addIngredientBtn.setOnClickListener(v -> {

            //need to add error handling for empty fields and to check that it's an integer
            double quantity = Double.parseDouble(quantityET.getText().toString());

            Measurement measurement = Measurement.valueOf(measurementSpinner.getSelectedItem().toString());
            //need to make this a search in the database with autocomplete, for now just hardcode
            Ingredient ingredient = new Ingredient(ingredientET.getText().toString(), IngredientCategory.Grain); //always grain for now

            recipeIngredients.add(new RecipeIngredient(ingredient, quantity, measurement));
            recipeIngredientAdapter.notifyItemInserted(recipeIngredients.size() - 1);

            quantityET.setText("");
            measurementSpinner.setSelection(0);
            ingredientET.setText("");
        });



        //testing Recipe

//        //initialize ingredients
//        Map<String,Ingredient> ingredients = initializeIngredients();
//
//        //initialize ingredient quantities
//        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
//        recipeIngredients.add(new RecipeIngredient(ingredients.get("flour"), 2, Measurement.Cups));
//        recipeIngredients.add(new RecipeIngredient(ingredients.get("sugar"), 1, Measurement.Tablespoons));
//        recipeIngredients.add(new RecipeIngredient(ingredients.get("salt"), 1, Measurement.Teaspoons));
//
//        //initialize recipe steps
//        List<String> recipeSteps = new ArrayList<>();
//        recipeSteps.add("add flour, sugar, and salt to a bowl");
//        recipeSteps.add("mix");
//        recipeSteps.add("enjoy");
//
//        //completed single recipe ex:
//        Recipe recipe = new Recipe("bag o powder", "fred frickerton", "you're going to need a glass of water after this one", recipeIngredients, recipeSteps);
//
//        //add recipe to DB:
//        db = FirebaseDatabase.getInstance();
//        ref = db.getReference();
//        ref.child("recipes").child(recipe.getName()).setValue(recipe);
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