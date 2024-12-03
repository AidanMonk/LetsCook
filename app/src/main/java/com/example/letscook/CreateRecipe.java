package com.example.letscook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.apache.commons.lang3.StringUtils;

import com.bumptech.glide.Glide;
import com.example.letscook.Adapters.RecipeIngredientAdapter1;
import com.example.letscook.Adapters.RecipeStepsAdapter1;
import com.example.letscook.EdamamApi.NutrientService;
import com.example.letscook.EdamamApi.NutritionalInfo;
import com.example.letscook.Exceptions.InvalidDietaryException;
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
import com.example.letscook.Models.RecipeCategory;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipe extends Base_activity {

    // UI components
    EditText recipeNameET, recipeDescET, quantityET, ingredientET, enterStepET, servingsET;
    Button addIngredientBtn, addStepBtn, addRecipeBtn, imageAddBtn;
    Spinner measurementSpinner, fractionSpinner, recipeCategorySpinner;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    Uri selectedImageUri;
    ImageView imagePreview;
    LinearLayout checkBoxLayout;
    RadioGroup dietaryRadioGroup;
    RadioButton yesRadioButton, noRadioButton;
    CheckBox veganCheckBox, vegetarianCheckBox, dairyFreeCheckBox, nutFreeCheckBox, glutenFreeCheckBox;

    // Firebase
    FirebaseDatabase db;
    DatabaseReference ref;

    // Constants
    int maxRecipeNameLength = 50;
    int maxRecipeDescLength = 500;

    // Adapters and Data Lists
    RecipeIngredientAdapter1 recipeIngredientAdapter1;
    RecipeStepsAdapter1 recipeStepsAdapter1;
    List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    List<String> recipeSteps = new ArrayList<>();
    List<String> dietaryCategory = new ArrayList<>();

    // SharedPreferences
    SharedPreferences sharedPreferences;
    public static final String PREFERENCES_NAME = "UserSession";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PREMIUM = "premium";
    public static final String KEY_USERNAME = "username";

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
        sharedPreferences = getSharedPreferences(LoginView.PREFERENCES_NAME, MODE_PRIVATE);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_create_recipe);
        getLayoutInflater().inflate(R.layout.activity_create_recipe, findViewById(R.id.frame_layout));
        EdgeToEdge.enable(this);
        initializeViews();
        setupAdapters();
        setupActionListeners();
        //to populate the dietaryTags
        getDietaryCategories();


        // Add recipe button
        addRecipeBtn.setOnClickListener(v -> {
            try {
                String recipeName = recipeNameET.getText().toString();
                String recipeDesc = recipeDescET.getText().toString();
                String author = sharedPreferences.getString(LoginView.KEY_USERNAME, "unknown");

                // shared ref
                // Retrieve current user details
                String username = sharedPreferences.getString(LoginView.KEY_USERNAME, "unknown");
               // boolean isPremium = sharedPreferences.getBoolean(LoginView.KEY_PREMIUM, false);
                String emailKey = sharedPreferences.getString(LoginView.KEY_EMAIL, "").replace(".", ",");

                int servingSize = Integer.parseInt(servingsET.getText().toString());

                //set the recipe category (spinner)
                RecipeCategory selectedRecipeCategory = RecipeCategory.valueOf(recipeCategorySpinner.getSelectedItem().toString().toUpperCase());
                validateRecipeFields(recipeName, recipeDesc);

                String[] ingredients = new String[recipeIngredients.size()];

                //call API to get nutritional info
                for(int i =0; i < recipeIngredients.size(); i++){
                    ingredients[i] = recipeIngredients.get(i).getIngredient().toString();
                }

                NutrientService.getNutritionalInfo(ingredients, new NutrientService.NutritionalInfoCallback() {
                    @Override
                    public void onComplete(NutritionalInfo nutritionalInfo) {

                        Recipe newRecipe = new Recipe(recipeName, author, recipeDesc, recipeIngredients, recipeSteps, selectedRecipeCategory, dietaryCategory, servingSize, nutritionalInfo);

                        //upload the selected image once the recipe is added
                        uploadImage(newRecipe.getImageId(), selectedImageUri);

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(emailKey);
                        //Add the recipe under the user.
                        userRef.child("recipes").child(newRecipe.getId()).setValue(newRecipe)
                                .addOnSuccessListener(aVoid -> {
                                    // Add recipe to common database if successful
                                    Database.addRecipe(newRecipe);
                                    Toast.makeText(CreateRecipe.this, "Recipe added!", Toast.LENGTH_SHORT).show();
                                    clearRecipeFields();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(CreateRecipe.this, "Failed to add recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });

            } catch (InvalidRecipeNameException | InvalidRecipeDescException | InvalidDietaryException|
                     InvalidRecipeIngredientException | InvalidRecipeStepException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActionListeners() {
        //set up listeners to change the edit text focus every time a field is entered

        recipeNameET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                recipeDescET.requestFocus(); // Set focus to the next EditText
                return true;
            }
            return false;
        });

        recipeDescET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                servingsET.requestFocus(); // Set focus to the next EditText
                return true;
            }
            return false;
        });

        servingsET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                quantityET.requestFocus(); // Set focus to the next EditText
                return true;
            }
            return false;
        });

        quantityET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                ingredientET.requestFocus(); // Set focus to the next EditText
                return true;
            }
            return false;
        });

        ingredientET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                enterStepET.requestFocus(); // Set focus to the next EditText
                return true;
            }
            return false;
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
        servingsET = findViewById(R.id.servingsET);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        addStepBtn = findViewById(R.id.addStepBtn);
        addRecipeBtn = findViewById(R.id.addRecipeBtn);
        ingredientsRecyclerView = findViewById(R.id.recipeIngredientsRV);
        stepsRecyclerView = findViewById(R.id.recipeStepsRV);
        measurementSpinner = findViewById(R.id.measurementSpinner);
        fractionSpinner = findViewById(R.id.fractionSpinner);
        imageAddBtn = findViewById(R.id.addImageBtn);
        imagePreview = findViewById(R.id.imagePreview);
        recipeCategorySpinner = findViewById(R.id.recipeCategorySpinner);

        dietaryRadioGroup = findViewById(R.id.dietaryRadioGroup);
        yesRadioButton = findViewById(R.id.yesRadioButton);
        noRadioButton = findViewById(R.id.noRadioButton);

        //layout
        checkBoxLayout = findViewById(R.id.layoutCheckBoxes);
        //checkboxes
        veganCheckBox = findViewById(R.id.veganCheckbox);
        vegetarianCheckBox = findViewById(R.id.vegeterianCheckbox);
        glutenFreeCheckBox = findViewById(R.id.glutenFreeCheckbox);
        dairyFreeCheckBox = findViewById(R.id.dairyFreeCheckbox);
        nutFreeCheckBox = findViewById(R.id.NutFreeCheckbox);


        // Populate spinners
        measurementSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Measurement.getMeasurementStrings()));
        fractionSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Fraction.getFractionStrings()));
        recipeCategorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, RecipeCategory.getRecipeCategoryStrings()));
    }

    private void setupAdapters() {
        recipeIngredientAdapter1 = new RecipeIngredientAdapter1(this, recipeIngredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter1);

        recipeStepsAdapter1 = new RecipeStepsAdapter1(this, recipeSteps);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(recipeStepsAdapter1);

        addIngredientBtn.setOnClickListener(v -> addIngredient());
        addStepBtn.setOnClickListener(v -> addStep());
        imageAddBtn.setOnClickListener(v -> openGallery());
    }

    private void addIngredient() {
        try {
            if (ingredientET.getText().toString().isEmpty()) {
                throw new InvalidRecipeIngredientException("Ingredient cannot be empty");
            }
            int quantity;
            if (quantityET.getText().toString().isEmpty()) {
                quantity = 0;
            }
            else{
                quantity = Integer.parseInt(quantityET.getText().toString());
            }

            Measurement measurement = Measurement.valueOf(measurementSpinner.getSelectedItem().toString().toUpperCase());
            Fraction fraction = Fraction.fromString(fractionSpinner.getSelectedItem().toString());

            if (quantity <= 0 && fraction == Fraction.NONE){
                throw new InvalidRecipeIngredientException("Quantity must be input");
            }

            Ingredient ingredient = new Ingredient(ingredientET.getText().toString());
            recipeIngredients.add(new RecipeIngredient(ingredient, quantity, fraction, measurement));
            recipeIngredientAdapter1.notifyItemInserted(recipeIngredients.size() - 1);

            quantityET.setText("");
            ingredientET.setText("");
            measurementSpinner.setSelection(0);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be input", Toast.LENGTH_SHORT).show();
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
            recipeStepsAdapter1.notifyItemInserted(recipeSteps.size() - 1);
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

    private void validateRecipeFields(String name, String desc) throws InvalidRecipeNameException, InvalidRecipeDescException, InvalidRecipeIngredientException, InvalidRecipeStepException, InvalidDietaryException {
        String invalidCharactersRegex = "[^a-zA-Z0-9 ]";
        if (name.matches(invalidCharactersRegex))
            throw new InvalidRecipeNameException("Invalid characters in recipe name");
        if (name.length() > maxRecipeNameLength)
            throw new InvalidRecipeNameException("Recipe name too long");
        if (desc.matches(invalidCharactersRegex))
            throw new InvalidRecipeDescException("Invalid characters in description");
        if (desc.length() > maxRecipeDescLength)
            throw new InvalidRecipeDescException("Description too long");
        if (recipeIngredients.isEmpty())
            throw new InvalidRecipeIngredientException("At least one ingredient required");
        if (recipeSteps.isEmpty())
            throw new InvalidRecipeStepException("At least one step required");
        if (selectedImageUri == null) throw new InvalidRecipeStepException("Image required");
        if (servingsET.getText().toString().isEmpty() || servingsET.getText().toString().equals("0") || !StringUtils.isNumeric(servingsET.getText().toString())){
            throw new InvalidRecipeStepException("Quantity required");
        }
        int selectedRadioId = dietaryRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioId == -1) {throw new InvalidDietaryException("Please select yes or no for dietary options ");}
        if (dietaryRadioGroup.getCheckedRadioButtonId() == R.id.yesRadioButton && dietaryCategory.isEmpty()) {
            throw new InvalidDietaryException("Please select at least one dietary option if you chose yes.");
        }
    }

    private void clearRecipeFields() {
        recipeNameET.setText("");
        recipeDescET.setText("");
        servingsET.setText("");
        quantityET.setText("");
        ingredientET.setText("");
        enterStepET.setText("");
        measurementSpinner.setSelection(0);
        fractionSpinner.setSelection(0);
        recipeIngredients.clear();
        recipeSteps.clear();
        selectedImageUri = null;
        imagePreview.setImageDrawable(null);
        imagePreview.setVisibility(View.GONE);
        imageAddBtn.setText("add image");
        recipeIngredientAdapter1.notifyDataSetChanged();
        recipeStepsAdapter1.notifyDataSetChanged();
        checkBoxLayout.setVisibility(View.GONE);
        dietaryRadioGroup.clearCheck();
        recipeCategorySpinner.setSelection(0);
        UncheckCheckBOx();
    }

    private void getDietaryCategories()  {
        checkBoxLayout.setVisibility(View.GONE);

        // Set up RadioGroup listener to toggle checkboxes visibility
        dietaryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.yesRadioButton) {
                checkBoxLayout.setVisibility(View.VISIBLE);
                dietaryCategory.clear(); // Reset dietary selection list

            } else {
                checkBoxLayout.setVisibility(View.GONE);
                dietaryCategory.clear();
                dietaryCategory.add("None");
                UncheckCheckBOx();
            }
        });

        // Set up individual checkbox listeners
        setUpCheckBoxListener(veganCheckBox, dietaryCategory, "Vegan");
        setUpCheckBoxListener(vegetarianCheckBox, dietaryCategory, "Vegetarian");
        setUpCheckBoxListener(glutenFreeCheckBox, dietaryCategory, "Gluten Free");
        setUpCheckBoxListener(dairyFreeCheckBox, dietaryCategory, "Dairy Free");
        setUpCheckBoxListener(nutFreeCheckBox, dietaryCategory, "Nut Free");
    }

    private void setUpCheckBoxListener(CheckBox checkBox, List<String> dietaryCategory, String category) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!dietaryCategory.contains(category)) {
                    dietaryCategory.add(category);
                }
            } else {
                dietaryCategory.remove(category);
            }
        });
    }

    private void UncheckCheckBOx(){
        veganCheckBox.setChecked(false);
        vegetarianCheckBox.setChecked(false);
        dairyFreeCheckBox.setChecked(false);
        nutFreeCheckBox.setChecked(false);
        glutenFreeCheckBox.setChecked(false);
    }



}

