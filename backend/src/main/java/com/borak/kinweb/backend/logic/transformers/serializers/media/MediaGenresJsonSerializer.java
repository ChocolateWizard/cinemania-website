/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers.serializers.media;

import com.borak.kinweb.backend.domain.dto.classes.GenreDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class MediaGenresJsonSerializer extends JsonSerializer<List<GenreDTO>> {

    @Override
    public void serialize(List<GenreDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartArray();
        for (GenreDTO genre : t) {
            jg.writeStartObject();
            jg.writeNumberField("id", genre.getId());
            jg.writeStringField("name", genre.getName());
            jg.writeEndObject();
        }
        jg.writeEndArray();
    }

}
