package com.example.letscook;

import java.util.ArrayList;
import java.util.List;

public enum Fraction {
    NONE,
    ONE_HALF,
    ONE_THIRD,
    TWO_THIRDS,
    ONE_QUARTER,
    THREE_QUARTERS;

    //to display in spinner
    public String altToString() {
        switch (this) {
            case ONE_HALF:
                return "1/2";
            case ONE_THIRD:
                return "1/3";
            case TWO_THIRDS:
                return "2/3";
            case ONE_QUARTER:
                return "1/4";
            case THREE_QUARTERS:
                return "3/4";
            default:
                return "None";
        }
    }
    @Override
    public String toString() {

        switch (this) {
            case ONE_HALF:
                return "½";
            case ONE_THIRD:
                return "⅓";
            case TWO_THIRDS:
                return "⅔";
            case ONE_QUARTER:
                return "¼";
            case THREE_QUARTERS:
                return "¾";
            default:
                return "";
        }
    }
    public static List<String> getFractionStrings() {
        List<String> fractions = new ArrayList<>();
        for (Fraction fraction : Fraction.values()) {
            fractions.add(fraction.altToString());
        }
        return fractions;
    }

    public static Fraction fromString(String fractionString) {
        switch (fractionString) {
            case "1/2":
                return ONE_HALF;
            case "1/3":
                return ONE_THIRD;
            case "2/3":
                return TWO_THIRDS;
            case "1/4":
                return ONE_QUARTER;
            case "3/4":
                return THREE_QUARTERS;
            default:
                return NONE;
        }
    }
}
