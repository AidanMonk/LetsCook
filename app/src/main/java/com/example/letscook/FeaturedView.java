package com.example.letscook;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscook.Adapters.RecipeListAdapter;
import com.example.letscook.Models.Recipe;

import java.util.List;

public class FeaturedView extends AppCompatActivity {

    RecyclerView featuredRecipesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_featured_view);

        featuredRecipesRecyclerView = findViewById(R.id.featuredRecipesRecyclerView);

        //just use all the recipes for now, implement a get for featured recipes later
        Database.GetRecipes(recipes -> {
            RecipeListAdapter recipeListAdapter = new RecipeListAdapter(this, recipes);
            featuredRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            featuredRecipesRecyclerView.setAdapter(recipeListAdapter);
        });

    }
}