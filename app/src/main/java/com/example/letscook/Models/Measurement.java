package com.example.letscook.Models;

public enum Measurement {
    CUPS,
    TBSP,
    TSP,
    TEASPOONS,
    TABLESPOONS,
    UNITS,
    LBS,
    G;

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
            case TBSP:
                return "tbsp";
            case TSP:
                return "tsp";
            case UNITS:
                return "units";
            case LBS:
                return "lbs";
            case TABLESPOONS:
                return "tablespoons";
            case TEASPOONS: return "teaspoons";
            case G:
                return "g";
            default:
                return "";
        }
    }
}


