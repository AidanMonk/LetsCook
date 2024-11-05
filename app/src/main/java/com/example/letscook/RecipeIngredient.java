package com.example.letscook;

//Defines a quantity and measurement to pair with an ingredient
public class RecipeIngredient {
    private Ingredient ingredient;
    private int quantity;
    private Fraction fraction;
    private Measurement measurement;

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
        if (this.quantity <= 1) {
            return measurement.toString().toLowerCase().substring(0, measurement.toString().length() - 1);
        }
        else{
            return measurement.toString().toLowerCase();
        }
    }
    @Override
    public String toString(){
        if (this.measurement == Measurement.UNITS){
            return quantity + " " + fraction.toString() + " " + ingredient.getName();
        }
        else{
            return quantity + " " + fraction.toString() + " " + measurementToString() + " of " + ingredient.getName();
        }
    }

}
