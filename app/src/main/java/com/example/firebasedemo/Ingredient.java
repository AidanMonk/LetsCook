package com.example.firebasedemo;

//Ingredient class defines a simple ingredient, plan to initialize these ingredient objects from the database
public class Ingredient{
    private String name;
    private IngredientCategory category;
    //image?

    public Ingredient(String name, IngredientCategory category){
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IngredientCategory getCategory() {
        return category;
    }

    public void setCategory(IngredientCategory category) {
        this.category = category;
    }




}