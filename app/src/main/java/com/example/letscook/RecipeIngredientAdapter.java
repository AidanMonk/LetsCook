package com.example.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder> {
    Context context;
    List<RecipeIngredient> recipeIngredients;

    public RecipeIngredientAdapter(Context context, List<RecipeIngredient> recipeIngredients) {
        this.context = context;
        this.recipeIngredients = recipeIngredients;
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_input, parent, false);

        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        holder.quantityTV.setText(String.valueOf(recipeIngredients.get(position).getQuantity()));
        holder.fractionTV.setText(recipeIngredients.get(position).getFraction().altToString());
        holder.measurementTV.setText(recipeIngredients.get(position).getMeasurement().toString());
        holder.ingredientTV.setText(recipeIngredients.get(position).getIngredient().getName());
    }

    @Override
    public int getItemCount() {
        return recipeIngredients.size();
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
        Button removeIngredientBtn;
        TextView quantityTV, fractionTV, measurementTV, ingredientTV;

        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            removeIngredientBtn = itemView.findViewById(R.id.removeIngredientBtn);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            fractionTV = itemView.findViewById(R.id.fractionTV);
            measurementTV = itemView.findViewById(R.id.measurementTV);
            ingredientTV = itemView.findViewById(R.id.ingredientTV);

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
