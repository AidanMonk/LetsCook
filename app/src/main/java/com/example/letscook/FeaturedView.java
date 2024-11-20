package com.example.letscook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscook.Adapters.RecipeListAdapter;

public class FeaturedView extends AppCompatActivity {

    SearchView searchView;
    RecyclerView featuredRecipesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_featured_view);

        searchView = findViewById(R.id.searchView);
        featuredRecipesRecyclerView = findViewById(R.id.featuredRecipesRecyclerView);

        //just use all the recipes for now, implement a get for featured recipes later
        Database.getRecipes(recipes -> {
            RecipeListAdapter recipeListAdapter = new RecipeListAdapter(this, recipes);
            featuredRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            featuredRecipesRecyclerView.setAdapter(recipeListAdapter);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(FeaturedView.this, SearchResultsView.class);
                intent.putExtra("query", query);
                startActivity(intent);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}