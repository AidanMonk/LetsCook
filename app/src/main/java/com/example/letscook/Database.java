package com.example.letscook;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.letscook.Models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final Database instance = new Database();

    private static FirebaseDatabase database;
    private static DatabaseReference recipesRef;

    private Database() {
        database = FirebaseDatabase.getInstance();
        recipesRef = database.getReference("recipes");
    }

    public static Database getInstance() {
        return instance;
    }

    public static void GetRecipes(RecipesCallback callback) {
        List<Recipe> recipes = new ArrayList<>();

        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Recipe recipe = data.getValue(Recipe.class);
                    if (recipe != null) {
                        recipes.add(recipe);
                    }
                }
                // Pass the recipes list to the callback
                callback.onCallback(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error retrieving recipes", error.toException());
            }
        });
    }

    public interface RecipesCallback {
        void onCallback(List<Recipe> recipes);
    }
}
