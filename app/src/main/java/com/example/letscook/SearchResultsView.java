package com.example.letscook;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.letscook.Adapters.RecipeSearchListAdapter;
import com.example.letscook.Models.Recipe;
import com.example.letscook.R;

import com.example.letscook.Adapters.RecipeListAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsView extends Base_activity {
    private RecyclerView recipeListRecycler;
    private RecipeSearchListAdapter recipeListAdapter;
    TextView noResultsTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results_view);

        recipeListRecycler = findViewById(R.id.recipeListRecycler);
        noResultsTV = findViewById(R.id.noResultsTV);

        //get recipe name query from intent
        String recipeNameQuery = getIntent().getStringExtra("query");

        Database.getRecipes(recipes -> {
            //filter recipes based on recipe name
            List<Recipe> filteredRecipes = recipes.stream()
                    .filter(recipe -> recipe.getName().toLowerCase().contains(recipeNameQuery.toLowerCase())).collect(Collectors.toList());

            if (filteredRecipes.isEmpty()) {
                recipeListRecycler.setVisibility(RecyclerView.GONE);
                noResultsTV.setVisibility(TextView.VISIBLE);
            }
            else{
                recipeListAdapter = new RecipeSearchListAdapter(this, filteredRecipes);
                recipeListRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                recipeListRecycler.setAdapter(recipeListAdapter);
            }
        });

    }
}