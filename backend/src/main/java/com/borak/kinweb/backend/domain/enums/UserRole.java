/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.borak.kinweb.backend.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Mr. Poyo
 */
public enum UserRole {

    REGULAR("REGULAR"), CRITIC("CRITIC"), ADMINISTRATOR("ADMINISTRATOR");

    private String name;

    private UserRole(String name) {
        this.name = name;
    }

    public static UserRole parseUserRole(String userRole) throws UnsupportedOperationException {
        if (userRole == null) {
            return null;
        }
        switch (userRole.toLowerCase()) {
            case "regular":
                return REGULAR;
            case "critic":
                return CRITIC;
            case "administrator":
                return ADMINISTRATOR;
            default:
                throw new UnsupportedOperationException("The user role " + userRole + " is not supported!");
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return name;
    }

}
