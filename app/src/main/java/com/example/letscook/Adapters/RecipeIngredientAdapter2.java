package com.example.letscook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscook.Models.RecipeIngredient;
import com.example.letscook.R;

import java.util.List;

/***********************
 * Adapter meant to display a list of recipe ingredients, for use on the recipe view page
 */

public class RecipeIngredientAdapter2 extends RecyclerView.Adapter<RecipeIngredientAdapter2.RecipeIngredientViewHolder> {
    Context context;
    List<RecipeIngredient> recipeIngredients;

    public RecipeIngredientAdapter2(Context context, List<RecipeIngredient> recipeIngredients) {
        this.context = context;
        this.recipeIngredients = recipeIngredients;
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_view, parent, false);

        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        holder.ingredientTV.setText(String.valueOf(recipeIngredients.get(position).toString()));
    }

    @Override
    public int getItemCount() {
        return recipeIngredients.size();
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTV;

        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientTV = itemView.findViewById(R.id.ingredientTV);
        }
    }
}