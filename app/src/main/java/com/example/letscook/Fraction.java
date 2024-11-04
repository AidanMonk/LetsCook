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
    @Override
    public String toString() {
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
    public String altToString() {

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
    public static List<Fraction> getFractions() {
        List<Fraction> fractions = new ArrayList<>();
        fractions.add(NONE);
        fractions.add(ONE_HALF);
        fractions.add(ONE_THIRD);
        fractions.add(TWO_THIRDS);
        fractions.add(ONE_QUARTER);
        fractions.add(THREE_QUARTERS);
        return fractions;
    }
}
