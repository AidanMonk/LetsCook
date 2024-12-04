package com.example.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.UserRecipeViewHolder> {

    private List<String> recipeNames; // List of recipe names
    private List<String> recipeIds;  // List of recipe IDs
    private Context context;

    public UserRecipeAdapter(Context context, List<String> recipeNames, List<String> recipeIds) {
        this.context = context;
        this.recipeNames = recipeNames;
        this.recipeIds = recipeIds;
    }

    @NonNull
    @Override
    public UserRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_recipe, parent, false);
        return new UserRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecipeViewHolder holder, int position) {
        String recipeName = recipeNames.get(position);
        String recipeId = recipeIds.get(position);

        // Set the recipe name
        holder.recipeNameText.setText(recipeName);

        // Set delete button functionality
        holder.deleteRecipeButton.setOnClickListener(v -> {
            // Get Firebase reference for the specific recipe
            DatabaseReference recipeRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(context.getSharedPreferences(LoginView.PREFERENCES_NAME, Context.MODE_PRIVATE)
                            .getString(LoginView.KEY_EMAIL, "").replace(".", ","))
                    .child("recipes")
                    .child(recipeId); // Target only the specific recipe

            // Remove the recipe from Firebase
            recipeRef.removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(context, "Recipe deleted: " + recipeName, Toast.LENGTH_SHORT).show();
                recipeNames.remove(position); // Remove from the list
                recipeIds.remove(position);   // Remove the corresponding ID
                notifyItemRemoved(position);  // Notify RecyclerView to update
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Failed to delete recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        // Set item click listener for future detail page navigation
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + recipeName, Toast.LENGTH_SHORT).show();
            // Add code to navigate to the recipe details page if needed
        });
    }

    @Override
    public int getItemCount() {
        return recipeNames.size();
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
