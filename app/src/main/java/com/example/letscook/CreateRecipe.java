package com.example.letscook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letscook.Adapters.RecipeIngredientAdapter;
import com.example.letscook.Adapters.RecipeStepsAdapter;
import com.example.letscook.Exceptions.InvalidRecipeDescException;
import com.example.letscook.Exceptions.InvalidRecipeIngredientException;
import com.example.letscook.Exceptions.InvalidRecipeNameException;
import com.example.letscook.Exceptions.InvalidRecipeStepException;
import com.example.letscook.Models.Fraction;
import com.example.letscook.Models.Ingredient;
import com.example.letscook.Models.IngredientCategory;
import com.example.letscook.Models.Measurement;
import com.example.letscook.Models.Recipe;
import com.example.letscook.Models.RecipeIngredient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipe extends AppCompatActivity {

    // UI components
    EditText recipeNameET, recipeDescET, quantityET, ingredientET, enterStepET;
    Button addIngredientBtn, addStepBtn, addRecipeBtn, imageAddBtn;
    Spinner measurementSpinner, fractionSpinner;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    Uri selectedImageUri;
    ImageView imagePreview;

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

    //create gallery launcher to handle image selection
    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImageUri = imageUri;

                        //update form to reflect image has been added
                        imagePreview.setVisibility(View.VISIBLE);
                        imageAddBtn.setText("change image");
                        Glide.with(this).load(imageUri).into(imagePreview);
                    }
                }
            });

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

                //upload the selected image once the recipe is added
                uploadImage(newRecipe.getImageId(), selectedImageUri);

                //add the recipe to the database
                Database.addRecipe(newRecipe);

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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
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
        imageAddBtn = findViewById(R.id.addImageBtn);
        imagePreview = findViewById(R.id.imagePreview);

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
        imageAddBtn.setOnClickListener(v -> openGallery());
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

    private void uploadImage(String imageId, Uri imageUri) {
        if (imageUri != null) {
            Database.uploadImage(imageId, imageUri);
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
        if (selectedImageUri == null) throw new InvalidRecipeStepException("Image required");
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
        selectedImageUri = null;
        imagePreview.setImageDrawable(null);
        imagePreview.setVisibility(View.GONE);
        imageAddBtn.setText("add image");
        recipeIngredientAdapter.notifyDataSetChanged();
        recipeStepsAdapter.notifyDataSetChanged();
    }
}