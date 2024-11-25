package com.example.letscook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscook.R;

import java.util.List;

/**********************
 * Adapter meant to display a list of recipe steps, meant for use on the recipe view page
 */

public class RecipeStepsAdapter2 extends RecyclerView.Adapter<RecipeStepsAdapter2.IngredientStepsViewHolder> {
    Context context;
    List<String> steps;

    public RecipeStepsAdapter2(Context context, List<String> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public RecipeStepsAdapter2.IngredientStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_view, parent, false);
        return new IngredientStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter2.IngredientStepsViewHolder holder, int position) {
        holder.stepNumberTV.setText(String.valueOf(position + 1) + ": ");
        holder.recipeStepTV.setText(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class IngredientStepsViewHolder extends RecyclerView.ViewHolder {
        TextView recipeStepTV, stepNumberTV;

        public IngredientStepsViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeStepTV = itemView.findViewById(R.id.recipeStepTV);
            stepNumberTV = itemView.findViewById(R.id.stepNumberTV);
        }
    }
}
