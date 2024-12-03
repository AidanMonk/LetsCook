package com.example.letscook.EdamamApi;

import java.util.List;
import java.util.Map;

public class NutrientApiResponse {

    private String uri;
    private int calories;
    private double totalCO2Emissions;
    private String co2EmissionsClass;
    private double totalWeight;
    private List<String> dietLabels;
    private List<String> healthLabels;
    private List<String> cautions;
    private Map<String, Nutrient> totalNutrients;
    private Map<String, DailyNutrient> totalDaily;
    private List<Ingredient> ingredients;
    private NutrientCalories totalNutrientsKCal;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getTotalCO2Emissions() {
        return totalCO2Emissions;
    }

    public void setTotalCO2Emissions(double totalCO2Emissions) {
        this.totalCO2Emissions = totalCO2Emissions;
    }

    public String getCo2EmissionsClass() {
        return co2EmissionsClass;
    }

    public void setCo2EmissionsClass(String co2EmissionsClass) {
        this.co2EmissionsClass = co2EmissionsClass;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(List<String> dietLabels) {
        this.dietLabels = dietLabels;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(List<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public List<String> getCautions() {
        return cautions;
    }

    public void setCautions(List<String> cautions) {
        this.cautions = cautions;
    }

    public Map<String, Nutrient> getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(Map<String, Nutrient> totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    public Map<String, DailyNutrient> getTotalDaily() {
        return totalDaily;
    }

    public void setTotalDaily(Map<String, DailyNutrient> totalDaily) {
        this.totalDaily = totalDaily;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public NutrientCalories getTotalNutrientsKCal() {
        return totalNutrientsKCal;
    }

    public void setTotalNutrientsKCal(NutrientCalories totalNutrientsKCal) {
        this.totalNutrientsKCal = totalNutrientsKCal;
    }


    public static class Nutrient {
        private String label;
        private double quantity;
        private String unit;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class DailyNutrient {
        private String label;
        private double quantity;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        private String unit;

    }

    public static class Ingredient {
        private String text;
        private List<ParsedIngredient> parsed;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<ParsedIngredient> getParsed() {
            return parsed;
        }

        public void setParsed(List<ParsedIngredient> parsed) {
            this.parsed = parsed;
        }

        public static class ParsedIngredient {
            private double quantity;
            private String measure;
            private String foodMatch;
            private String food;
            private String foodId;
            private double weight;
            private double retainedWeight;
            private Map<String, Nutrient> nutrients;
            private String measureURI;
            private String status;

            // Getters and Setters
        }
    }

    public static class NutrientCalories {
        private Nutrient energy;
        private Nutrient protein;
        private Nutrient fat;

        public Nutrient getEnergy() {
            return energy;
        }

        public void setEnergy(Nutrient energy) {
            this.energy = energy;
        }

        public Nutrient getProtein() {
            return protein;
        }

        public void setProtein(Nutrient protein) {
            this.protein = protein;
        }

        public Nutrient getFat() {
            return fat;
        }

        public void setFat(Nutrient fat) {
            this.fat = fat;
        }

        public Nutrient getCarbohydrates() {
            return carbohydrates;
        }

        public void setCarbohydrates(Nutrient carbohydrates) {
            this.carbohydrates = carbohydrates;
        }

        private Nutrient carbohydrates;
    }
}

