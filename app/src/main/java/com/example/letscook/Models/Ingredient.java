package com.example.letscook.Models;

//Ingredient class defines a simple ingredient, plan to initialize these ingredient objects from the database
public class Ingredient{
    private String name;
    //image?

    public Ingredient(){}

    public Ingredient(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}