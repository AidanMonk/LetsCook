package com.example.letscook;

//Defines a quantity and measurement to pair with an ingredient
public class RecipeIngredient {
    private Ingredient ingredient;
    private double quantity;
    private Measurement measurement;

    public RecipeIngredient(Ingredient ingredient, double quantity, Measurement measurement) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public String toString() {
        //ex: "1 cup of flour"
        return quantity + " " + measurement + " of " + ingredient;
    }
}
