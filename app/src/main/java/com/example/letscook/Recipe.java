package com.example.letscook;

import java.util.List;

//defines a single whole recipe
public class Recipe {
    private String name;
    //make author a user object later
    private String author;
    private String description;
    //add image parameter later
    private List<RecipeIngredient> ingredients;
    private List<String> steps;
}
