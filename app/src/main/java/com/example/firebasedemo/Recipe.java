package com.example.firebasedemo;


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
}