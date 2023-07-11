/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Mr Poyo
 */
@Converter
public class UserPasswordConverter implements AttributeConverter<char[], char[]> {

    @Override
    public char[] convertToDatabaseColumn(char[] attribute) {
        return attribute;

    }

    @Override
    public char[] convertToEntityAttribute(char[] dbData) {
        return dbData;
    }

}
