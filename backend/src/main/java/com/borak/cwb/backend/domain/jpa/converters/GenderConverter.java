/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.domain.jpa.converters;

import com.borak.cwb.backend.domain.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Mr. Poyo
 */
@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender attribute) {
        if (attribute != null) {
            return attribute.getSymbol();
        }
        return null;
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        return Gender.parseGender(String.valueOf(dbData));
    }

}
