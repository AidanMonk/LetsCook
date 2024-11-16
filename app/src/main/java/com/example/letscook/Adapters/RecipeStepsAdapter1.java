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
 * Adapter meant to display a list of recipe steps, includes a delete button for use on the create recipe page
 */

public class RecipeStepsAdapter1 extends RecyclerView.Adapter<RecipeStepsAdapter1.IngredientStepsViewHolder> {
    Context context;
    List<String> steps;

    public RecipeStepsAdapter1(Context context, List<String> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public RecipeStepsAdapter1.IngredientStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_input_view, parent, false);
        return new IngredientStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter1.IngredientStepsViewHolder holder, int position) {
        holder.stepNumberTV.setText(String.valueOf(position + 1) + ": ");
        holder.recipeStepTV.setText(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class IngredientStepsViewHolder extends RecyclerView.ViewHolder {
        TextView recipeStepTV, stepNumberTV;
        Button removeStepBtn;

        public IngredientStepsViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeStepTV = itemView.findViewById(R.id.recipeStepTV);
            stepNumberTV = itemView.findViewById(R.id.stepNumberTV);
            removeStepBtn = itemView.findViewById(R.id.removeStepBtn);
            removeStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    steps.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
