/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.borak.cwb.backend.domain.enums;

import java.util.Arrays;

/**
 *
 * @author User
 */
public enum SortOption {

    ASC("asc"), DESC("desc");

    private final String text;
    public static final String AVAILABLE_VALUES = Arrays.toString(values());// Cache the values array

    private SortOption(String text) {
        this.text = text;
    }

    public static SortOption parseSortOption(String sortOption) throws IllegalArgumentException {
        if (sortOption == null) {
            return null;
        }
        switch (sortOption.toLowerCase()) {
            case "ascending":
            case "asc":
                return ASC;
            case "descending":
            case "desc":
                return DESC;
            default:
                throw new IllegalArgumentException("Unknown sorting option. Available options are: " + AVAILABLE_VALUES);
        }
    }

    @Override
    public String toString() {
        return text;
    }

}
