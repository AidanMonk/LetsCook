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

    EditText recipeNameET, recipeDescET, quantityET, ingredientET, enterStepET;
    Button addIngredientBtn, addStepBtn, addRecipeBtn;
    Spinner measurementSpinner, fractionSpinner;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;

    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        enterStepET = findViewById(R.id.enterStepET);
        recipeNameET = findViewById(R.id.recipeNameET);
        recipeDescET = findViewById(R.id.recipeDescET);
        quantityET = findViewById(R.id.quantityET);
        fractionSpinner = findViewById(R.id.fractionSpinner);
        measurementSpinner = findViewById(R.id.measurementSpinner);
        ingredientET = findViewById(R.id.ingredientET);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        addStepBtn = findViewById(R.id.addStepBtn);
        ingredientsRecyclerView = findViewById(R.id.recipeIngredientsRV);
        stepsRecyclerView = findViewById(R.id.recipeStepsRV);
        addRecipeBtn = findViewById(R.id.addRecipeBtn);

        //populate spinner with measurements
        measurementSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Measurement.getMeasurementStrings()));

        fractionSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (Fraction.getFractions())));

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(this, recipeIngredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter);

        //add ingredient button
        addIngredientBtn.setOnClickListener(v -> {

            //need to add error handling for empty fields and to check that it's an integer
            int quantity = Integer.parseInt(quantityET.getText().toString());

            Measurement measurement = Measurement.valueOf(measurementSpinner.getSelectedItem().toString());

            Fraction fraction = (Fraction) fractionSpinner.getSelectedItem();

            //need to make this a search in the database with autocomplete, for now just hardcode
            Ingredient ingredient = new Ingredient(ingredientET.getText().toString(), IngredientCategory.Grain); //always grain for now

            recipeIngredients.add(new RecipeIngredient(ingredient, quantity, fraction, measurement));
            recipeIngredientAdapter.notifyItemInserted(recipeIngredients.size() - 1);

            quantityET.setText("");
            measurementSpinner.setSelection(0);
            ingredientET.setText("");
        });

        List<String> recipeSteps = new ArrayList<>();

        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(this, recipeSteps);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(recipeStepsAdapter);

        addStepBtn.setOnClickListener(v -> {
            recipeSteps.add(enterStepET.getText().toString());
            recipeStepsAdapter.notifyItemInserted(recipeSteps.size() - 1);
            enterStepET.setText("");
        });

        addRecipeBtn.setOnClickListener(v -> {
            //using dummy user for now
            Recipe newrecipe = new Recipe(recipeNameET.getText().toString(), "dummy user", recipeDescET.getText().toString(), recipeIngredients, recipeSteps);

            //add recipe to DB:
            db = FirebaseDatabase.getInstance();
            ref = db.getReference();
            ref.child("recipes").child(newrecipe.getName()).setValue(newrecipe);

            Toast.makeText(this, "Recipe added!", Toast.LENGTH_SHORT).show();

            //clear fields:
            ClearRecipeFields();
            recipeSteps.clear();
            recipeIngredients.clear();
            recipeStepsAdapter.notifyDataSetChanged();
            recipeIngredientAdapter.notifyDataSetChanged();
        });
    }

    private void ClearRecipeFields() {
        recipeNameET.setText("");
        recipeDescET.setText("");
        quantityET.setText("");
        ingredientET.setText("");
        enterStepET.setText("");
        measurementSpinner.setSelection(0);
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