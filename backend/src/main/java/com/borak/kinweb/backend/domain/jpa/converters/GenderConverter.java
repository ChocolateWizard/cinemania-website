/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.jpa.converters;


import com.borak.kinweb.backend.domain.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Mr Poyo
 */
@Converter
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender attribute) {
        return attribute == null ? null : attribute.getSymbol();
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        return dbData == null ? null : Gender.parseGender(dbData.toString());
    }

}
