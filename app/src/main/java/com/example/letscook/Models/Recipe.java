package com.example.letscook.Models;

import java.util.List;
import java.util.UUID;

//Recipe class defines a single recipe including ingredients, steps, and recipe image
public class Recipe {
    private String id;
    private String name;
    private String author;
    private String description;
    private String imageId;
    private List<RecipeIngredient> ingredients;
    private List<String> steps;
    private RecipeCategory recipeCategory;
    private List<String> dietaryCategory;

    private float averageRating;
    private int ratingCount;

    public Recipe(){}

    public Recipe(String name, String author, String description, List<RecipeIngredient> ingredients, List<String> steps, RecipeCategory recipeCategory , List<String> dietaryCategory){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.author = author;
        this.description = description;
        this.imageId = UUID.randomUUID().toString();
        this.ingredients = ingredients;
        this.steps = steps;
        this.recipeCategory = recipeCategory;
        this.dietaryCategory = dietaryCategory;
        this.averageRating = 0.0f;
        this.ratingCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageId() {
        return imageId;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public RecipeCategory getRecipeCategory() {return recipeCategory;}

    public void setRecipeCategory(RecipeCategory recipeCategory) {this.recipeCategory = recipeCategory;}

    public List<String> getDietaryCategory() {return dietaryCategory;}

    public void setDietaryCategory(List<String> dietaryCategory) {this.dietaryCategory = dietaryCategory;}

    public float getAverageRating() {
        return averageRating;
    }


    public int getRatingCount() {
        return ratingCount;
    }


    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }


    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }


}
