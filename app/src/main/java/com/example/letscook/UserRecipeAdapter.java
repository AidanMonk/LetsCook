package com.example.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscook.Models.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.UserRecipeViewHolder> {

    private List<Recipe> userRecipes;
    private Context context;

    public UserRecipeAdapter(Context context, List<Recipe> userRecipes) {
        this.context = context;
        this.userRecipes = userRecipes;
    }

    @NonNull
    @Override
    public UserRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_recipe, parent, false);
        return new UserRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecipeViewHolder holder, int position) {
        Recipe recipe = userRecipes.get(position);

        holder.recipeNameText.setText(recipe.getName());

        // Delete button
        holder.deleteRecipeButton.setOnClickListener(v -> {
            DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes")
                    .child(recipe.getId());
            recipeRef.removeValue().addOnSuccessListener(aVoid -> {
                userRecipes.remove(position);
                notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                // Error feedback wait a seocnd
            });
        });

        // Jump to details later
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return userRecipes.size();
    }

    static class UserRecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameText;
        Button deleteRecipeButton;

        public UserRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameText = itemView.findViewById(R.id.recipeNameText);
            deleteRecipeButton = itemView.findViewById(R.id.deleteRecipeButton);
        }
    }
}
