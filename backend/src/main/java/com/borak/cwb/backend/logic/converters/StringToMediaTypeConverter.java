/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.converters;

import com.borak.cwb.backend.domain.enums.MediaType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class StringToMediaTypeConverter implements Converter<String, MediaType> {

    @Override
    public MediaType convert(String source) {
        return MediaType.parseMediaType(source);
    }

}
