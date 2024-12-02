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

import com.example.letscook.Models.Recipe;
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
    private List<Recipe> userRecipeList;

    private DatabaseReference recipesRef;
    private SharedPreferences sharedPreferences;
    private String loggedInEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_recipes, container, false);

        userRecipesRecyclerView = view.findViewById(R.id.userRecipesRecyclerView);
        userRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userRecipeList = new ArrayList<>();
        userRecipeAdapter = new UserRecipeAdapter(getContext(), userRecipeList);
        userRecipesRecyclerView.setAdapter(userRecipeAdapter);

        sharedPreferences = getActivity().getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE);
        loggedInEmail = sharedPreferences.getString(LoginView.KEY_EMAIL, null);

        recipesRef = FirebaseDatabase.getInstance().getReference("recipes");

        if (!TextUtils.isEmpty(loggedInEmail)) {
            loadUserRecipes();
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadUserRecipes() {
        recipesRef.orderByChild("author").equalTo(loggedInEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRecipeList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        if (recipe != null) {
                            userRecipeList.add(recipe);
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
