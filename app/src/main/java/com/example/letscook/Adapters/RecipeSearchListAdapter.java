package com.example.letscook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.letscook.Database;
import com.example.letscook.Models.Recipe;
import com.example.letscook.R;
import com.example.letscook.RecipeView;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

public class RecipeSearchListAdapter extends RecyclerView.Adapter<RecipeSearchListAdapter.ViewHolder> {

    Context context;
    List<Recipe> recipes;

    public RecipeSearchListAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeSearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_search_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchListAdapter.ViewHolder holder, int position) {
        holder.recipeNameTV.setText(recipes.get(position).getName());

        Database.getImageUrl(recipes.get(position).getImageId(), new Database.UrlCallback() {
            @Override
            public void onUrlLoaded(String url) {
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.recipeImage);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Error loading image URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView recipeNameTV;
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            recipeNameTV = itemView.findViewById(R.id.recipeNameTV);
            recipeImage = itemView.findViewById(R.id.recipeImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeView.class);
                    intent.putExtra("recipeId", recipes.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
