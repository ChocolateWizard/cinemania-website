/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.borak.kinweb.backend.domain.enums;

/**
 *
 * @author Mr Poyo
 */
public enum Gender {
    MALE('M', "Male"), FEMALE('F', "Female"), OTHER('O', "Other");

    private final char symbol;
    private final String text;

    private Gender(char symbol, String text) {
        this.symbol = symbol;
        this.text = text;
    }

    public static Gender parseGender(String gender) {
        switch (gender) {
            case "M":
            case "m":
            case "Male":
                return MALE;
            case "F":
            case "f":
            case "Female":
                return FEMALE;
            case "O":
            case "o":
            case "Other":
                return OTHER;
            default:
                throw new UnsupportedOperationException(
                        "The gender " + gender + " is not supported!"
                );
        }
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return text;
    }

}
