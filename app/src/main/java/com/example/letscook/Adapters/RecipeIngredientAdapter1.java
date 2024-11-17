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
 * Adapter meant to display a list of recipe ingredients, includes a delete button for use on the create recipe page
 */

public class RecipeIngredientAdapter1 extends RecyclerView.Adapter<RecipeIngredientAdapter1.RecipeIngredientViewHolder> {
    Context context;
    List<RecipeIngredient> recipeIngredients;

    public RecipeIngredientAdapter1(Context context, List<RecipeIngredient> recipeIngredients) {
        this.context = context;
        this.recipeIngredients = recipeIngredients;
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_input_view, parent, false);

        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        holder.recipeTV.setText(String.valueOf(recipeIngredients.get(position).toString()));
    }

    @Override
    public int getItemCount() {
        return recipeIngredients.size();
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
        Button removeIngredientBtn;
        TextView recipeTV;

        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            removeIngredientBtn = itemView.findViewById(R.id.removeIngredientBtn);
            recipeTV = itemView.findViewById(R.id.recipeTV);

            removeIngredientBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeIngredients.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }
}
