/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.borak.kinweb.backend.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Mr Poyo
 */
public enum Gender {
    MALE('M', "male"), FEMALE('F', "female"), OTHER('O', "other");

    private final char symbol;
    private final String text;

    private Gender(char symbol, String text) {
        this.symbol = symbol;
        this.text = text;
    }

    public static Gender parseGender(String gender) throws UnsupportedOperationException {
        if (gender == null) {
            return null;
        }
        switch (gender.toLowerCase()) {
            case "m":
            case "male":
                return MALE;
            case "f":
            case "female":
                return FEMALE;
            case "o":
            case "other":
                return OTHER;
            default:
                throw new UnsupportedOperationException("The gender " + gender + " is not supported!");
        }
    }

    public char getSymbol() {
        return symbol;
    }

    @JsonValue
    @Override
    public String toString() {
        return text;
    }

}
