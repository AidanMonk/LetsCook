package com.example.letscook.Models;

public enum Measurement {
    CUPS,
    TABLESPOONS,
    TEASPOONS,
    UNITS;

    public static String[] getMeasurementStrings() {
        Measurement[] measurements = Measurement.values();
        String[] measurementStrings = new String[measurements.length];
        for (int i = 0; i < measurements.length; i++) {
            measurementStrings[i] = measurements[i].toString();
        }
        return measurementStrings;
    }

    @Override
    public String toString() {
        switch (this) {
            case CUPS:
                return "cups";
            case TABLESPOONS:
                return "tablespoons";
            case TEASPOONS:
                return "teaspoons";
            case UNITS:
                return "units";
            default:
                return "";
        }
    }
}


