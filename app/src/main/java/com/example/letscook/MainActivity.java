package com.example.letscook;

import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscook.Exceptions.InvalidRecipeDescException;
import com.example.letscook.Exceptions.InvalidRecipeIngredientException;
import com.example.letscook.Exceptions.InvalidRecipeNameException;
import com.example.letscook.Exceptions.InvalidRecipeStepException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI components
    EditText recipeNameET, recipeDescET, quantityET, ingredientET, enterStepET;
    Button addIngredientBtn, addStepBtn, addRecipeBtn;
    Spinner measurementSpinner, fractionSpinner;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;

    // Firebase
    FirebaseDatabase db;
    DatabaseReference ref;

    // Constants
    int maxRecipeNameLength = 50;
    int maxRecipeDescLength = 500;

    // Adapters and Data Lists
    RecipeIngredientAdapter recipeIngredientAdapter;
    RecipeStepsAdapter recipeStepsAdapter;
    List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    List<String> recipeSteps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_recipe);

        initializeViews();
        setupAdapters();

        // Add recipe button
        addRecipeBtn.setOnClickListener(v -> {
            try {
                String recipeName = recipeNameET.getText().toString();
                String recipeDesc = recipeDescET.getText().toString();
                String author = "dummy user";

                validateRecipeFields(recipeName, recipeDesc);

                Recipe newRecipe = new Recipe(recipeName, author, recipeDesc, recipeIngredients, recipeSteps);

                // Add recipe to DB
                db = FirebaseDatabase.getInstance();
                ref = db.getReference();
                ref.child("recipes").child(newRecipe.getName()).setValue(newRecipe);

                Toast.makeText(this, "Recipe added!", Toast.LENGTH_SHORT).show();

                // Clear fields and reset adapters
                clearRecipeFields();
            } catch (InvalidRecipeNameException | InvalidRecipeDescException | InvalidRecipeIngredientException | InvalidRecipeStepException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        recipeNameET = findViewById(R.id.recipeNameET);
        recipeDescET = findViewById(R.id.recipeDescET);
        quantityET = findViewById(R.id.quantityET);
        ingredientET = findViewById(R.id.ingredientET);
        enterStepET = findViewById(R.id.enterStepET);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        addStepBtn = findViewById(R.id.addStepBtn);
        addRecipeBtn = findViewById(R.id.addRecipeBtn);
        ingredientsRecyclerView = findViewById(R.id.recipeIngredientsRV);
        stepsRecyclerView = findViewById(R.id.recipeStepsRV);
        measurementSpinner = findViewById(R.id.measurementSpinner);
        fractionSpinner = findViewById(R.id.fractionSpinner);

        // Populate spinners
        measurementSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Measurement.getMeasurementStrings()));
        fractionSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Fraction.getFractionStrings()));
    }

    private void setupAdapters() {
        recipeIngredientAdapter = new RecipeIngredientAdapter(this, recipeIngredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter);

        recipeStepsAdapter = new RecipeStepsAdapter(this, recipeSteps);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(recipeStepsAdapter);

        addIngredientBtn.setOnClickListener(v -> addIngredient());
        addStepBtn.setOnClickListener(v -> addStep());
    }

    private void addIngredient() {
        try {
            if (ingredientET.getText().toString().isEmpty()) {
                throw new InvalidRecipeIngredientException("Ingredient cannot be empty");
            }

            int quantity = Integer.parseInt(quantityET.getText().toString());
            Measurement measurement = Measurement.valueOf(measurementSpinner.getSelectedItem().toString().toUpperCase());
            Fraction fraction = Fraction.fromString(fractionSpinner.getSelectedItem().toString());
            Ingredient ingredient = new Ingredient(ingredientET.getText().toString(), IngredientCategory.GRAIN); // Hardcoded category

            recipeIngredients.add(new RecipeIngredient(ingredient, quantity, fraction, measurement));
            recipeIngredientAdapter.notifyItemInserted(recipeIngredients.size() - 1);

            quantityET.setText("");
            ingredientET.setText("");
            measurementSpinner.setSelection(0);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be an integer", Toast.LENGTH_SHORT).show();
        } catch (InvalidRecipeIngredientException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addStep() {
        try {
            String stepText = enterStepET.getText().toString();
            if (stepText.isEmpty()) {
                throw new InvalidRecipeStepException("Step cannot be empty");
            }
            recipeSteps.add(stepText);
            recipeStepsAdapter.notifyItemInserted(recipeSteps.size() - 1);
            enterStepET.setText("");
        } catch (InvalidRecipeStepException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void validateRecipeFields(String name, String desc) throws InvalidRecipeNameException, InvalidRecipeDescException, InvalidRecipeIngredientException, InvalidRecipeStepException {
        String invalidCharactersRegex = "[^a-zA-Z0-9 ]";
        if (name.matches(invalidCharactersRegex)) throw new InvalidRecipeNameException("Invalid characters in recipe name");
        if (name.length() > maxRecipeNameLength) throw new InvalidRecipeNameException("Recipe name too long");
        if (desc.matches(invalidCharactersRegex)) throw new InvalidRecipeDescException("Invalid characters in description");
        if (desc.length() > maxRecipeDescLength) throw new InvalidRecipeDescException("Description too long");
        if (recipeIngredients.isEmpty()) throw new InvalidRecipeIngredientException("At least one ingredient required");
        if (recipeSteps.isEmpty()) throw new InvalidRecipeStepException("At least one step required");
    }

    private void clearRecipeFields() {
        recipeNameET.setText("");
        recipeDescET.setText("");
        quantityET.setText("");
        ingredientET.setText("");
        enterStepET.setText("");
        measurementSpinner.setSelection(0);
        recipeIngredients.clear();
        recipeSteps.clear();
        recipeIngredientAdapter.notifyDataSetChanged();
        recipeStepsAdapter.notifyDataSetChanged();
    }
}