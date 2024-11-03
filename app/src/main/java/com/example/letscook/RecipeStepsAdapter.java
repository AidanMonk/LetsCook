package com.example.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.IngredientStepsViewHolder> {
    Context context;
    List<String> steps;

    public RecipeStepsAdapter(Context context, List<String> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public RecipeStepsAdapter.IngredientStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_input, parent, false);
        return new IngredientStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter.IngredientStepsViewHolder holder, int position) {
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
