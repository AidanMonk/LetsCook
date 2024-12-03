package com.example.letscook.EdamamApi;

import java.util.HashMap;
import java.util.Map;

public class NutritionalInfo {

    private Map<String, Double> nutrientMap;

    public NutritionalInfo() {
        this.nutrientMap = new HashMap<>();
        this.nutrientMap.put("calories", 0.0);
        this.nutrientMap.put("protein", 0.0);
        this.nutrientMap.put("fat", 0.0);
        this.nutrientMap.put("saturatedFat", 0.0);
        this.nutrientMap.put("transFat", 0.0);
        this.nutrientMap.put("carbohydrates", 0.0);
        this.nutrientMap.put("fibre", 0.0);
        this.nutrientMap.put("sugars", 0.0);
        this.nutrientMap.put("cholesterol", 0.0);
        this.nutrientMap.put("sodium", 0.0);
    }

    public void addNutrient(String nutrientName, double nutrientValue) {
        nutrientMap.merge(nutrientName, nutrientValue, Double::sum);
    }

    public Map<String, Double> getNutrientMap() {
        return nutrientMap;
    }

    public void setNutrientMap(Map<String, Double> nutrientMap) {
        this.nutrientMap = nutrientMap;
    }

}
