package com.example.letscook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letscook.Database;
import com.example.letscook.Models.Recipe;
import com.example.letscook.R;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    Context context;
    List<Recipe> recipes;

    public RecipeListAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.RecipeViewHolder holder, int position) {
        holder.recipeNameTV.setText(recipes.get(position).getName());
        holder.authorTV.setText(recipes.get(position).getAuthor());

        Database.getImageUrl(recipes.get(position).getImageId(), new Database.UrlCallback(){
            @Override
            public void onUrlLoaded(String url) {
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.recipeImage);
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTV;
        TextView authorTV;
        ImageView recipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeNameTV = itemView.findViewById(R.id.recipeNameTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            recipeImage = itemView.findViewById(R.id.myImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, recipeNameTV + " by " + authorTV, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
