package com.example.letscook.Models;

import java.util.List;
import java.util.UUID;

//Recipe class defines a single recipe including ingredients, steps, and recipe image
public class Recipe {
    private String name;
    private String author;
    private String description;
    private String imageId;
    private List<RecipeIngredient> ingredients;
    private List<String> steps;

    public Recipe(){}

    public Recipe(String name, String author, String description, List<RecipeIngredient> ingredients, List<String> steps){
        this.name = name;
        this.author = author;
        this.description = description;
        this.imageId = UUID.randomUUID().toString();
        this.ingredients = ingredients;
        this.steps = steps;
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

    public void setImageId(String imageId) {
        this.imageId = imageId;
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


}
