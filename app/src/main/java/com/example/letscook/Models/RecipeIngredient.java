package com.example.letscook.Models;

//Defines a quantity and measurement to pair with an ingredient
public class RecipeIngredient {
    private Ingredient ingredient;
    private int quantity;
    private Fraction fraction;
    private Measurement measurement;

    public RecipeIngredient(){}

    public RecipeIngredient(Ingredient ingredient, int quantity, Fraction fraction, Measurement measurement) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.fraction = fraction;
        this.measurement = measurement;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Fraction getFraction() {
        return fraction;
    }

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public String measurementToString() {

        if (quantity > 1) {
            return measurement.toString().toLowerCase();
        } else {
            // Handles all cases where quantity is 1 or less.
            return measurement.toString().toLowerCase().substring(0, measurement.toString().length() - 1);
        }
    }

    @Override
    public String toString() {
        if (this.quantity == 0){
            //don't show quantity and change fraction display
            if (this.measurement == Measurement.UNITS) {
                return fraction.altToString() + " " + ingredient.getName();
            } else {
                return fraction.altToString() + " " + measurementToString() + " of " + ingredient.getName();
            }
        }
        else{
            //show quantity
            if (this.measurement == Measurement.UNITS) {
                return quantity + fraction.toString() + " " + ingredient.getName();
            } else {
                return quantity + fraction.toString() + " " + measurementToString() + " of " + ingredient.getName();
            }
        }
    }

}
