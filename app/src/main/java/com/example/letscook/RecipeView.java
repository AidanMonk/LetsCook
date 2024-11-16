package com.example.letscook;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

public class RecipeView extends AppCompatActivity {

    TextView recipeName, recipeDesc;
    ImageView imagePreview;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    RecipeIngredientAdapter2 recipeIngredientAdapter2;
    RecipeStepsAdapter2 recipeStepsAdapter2;

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

        recipeIngredientAdapter2 = new RecipeIngredientAdapter2(this, recipe.getIngredients());
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter2);

        recipeStepsAdapter2 = new RecipeStepsAdapter2(this, recipe.getSteps());
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepsRecyclerView.setAdapter(recipeStepsAdapter2);

    }

    private void initializeViews(){
        recipeName = findViewById(R.id.recipeName);
        recipeDesc = findViewById(R.id.recipeDesc);
        imagePreview = findViewById(R.id.imagePreview);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        stepsRecyclerView = findViewById(R.id.stepsRecyclerView);
    }
}