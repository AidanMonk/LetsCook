package com.example.letscook;

public enum Measurement {
    Cups,
    Tablespoons,
    Teaspoons;

    public static String[] getMeasurementStrings() {
        Measurement[] measurements = Measurement.values();
        String[] measurementStrings = new String[measurements.length];
        for (int i = 0; i < measurements.length; i++) {
            measurementStrings[i] = measurements[i].toString();
        }
        return measurementStrings;
    }
}


