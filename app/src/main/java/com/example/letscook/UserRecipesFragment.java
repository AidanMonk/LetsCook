package com.example.letscook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRecipesFragment extends Fragment {

    private RecyclerView userRecipesRecyclerView;
    private UserRecipeAdapter userRecipeAdapter;
    private List<String> recipeNames; // List to store recipe names
    private List<String> recipeIds;  // List to store recipe IDs

    private DatabaseReference userRef;
    private SharedPreferences sharedPreferences;
    private String loggedInEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_recipes, container, false);

        // Initialize RecyclerView and lists
        userRecipesRecyclerView = view.findViewById(R.id.userRecipesRecyclerView);
        userRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeNames = new ArrayList<>();
        recipeIds = new ArrayList<>();

        // Set up adapter
        userRecipeAdapter = new UserRecipeAdapter(getContext(), recipeNames, recipeIds);
        userRecipesRecyclerView.setAdapter(userRecipeAdapter);

        // Retrieve logged-in user's email from SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE);
        loggedInEmail = sharedPreferences.getString(LoginView.KEY_EMAIL, null);

        // Load user recipes if logged in
        if (!TextUtils.isEmpty(loggedInEmail)) {
            loadUserRecipes();
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    /**
     * Load user recipes from Firebase
     */
    private void loadUserRecipes() {
        // Replace "." in email with "," for Firebase key
        String emailKey = loggedInEmail.replace(".", ",");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(emailKey).child("recipes");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeNames.clear();
                recipeIds.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                        String recipeName = recipeSnapshot.child("name").getValue(String.class);
                        String recipeId = recipeSnapshot.getKey(); // Get the recipe ID

                        if (recipeName != null && recipeId != null) {
                            recipeNames.add(recipeName);
                            recipeIds.add(recipeId); // Store the recipe ID for deletion
                        }
                    }
                    userRecipeAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No recipes found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load recipes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
