package com.example.letscook;

import java.util.List;

//Recipe class defines a single recipe including ingredients, steps, and recipe image
public class Recipe {
    private String name;
    private String author;
    private String description;
    //add image parameter, tbd how we will store these
    private List<RecipeIngredient> ingredients;
    private List<String> steps;

    public Recipe(String name, String author, String description, List<RecipeIngredient> ingredients, List<String> steps){
        this.name = name;
        this.author = author;
        this.description = description;
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
