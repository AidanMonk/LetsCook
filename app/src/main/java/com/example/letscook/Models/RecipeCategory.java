package com.example.letscook.Models;

import java.util.ArrayList;
import java.util.List;

public enum RecipeCategory {
    NONE,
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    DESSERT;
    public static List<String> getRecipeCategoryStrings() {
        List<String> categoryList = new ArrayList<>();
        for (RecipeCategory category : RecipeCategory.values()) {
            categoryList.add(category.name());
        }
        return categoryList;
    }
}
