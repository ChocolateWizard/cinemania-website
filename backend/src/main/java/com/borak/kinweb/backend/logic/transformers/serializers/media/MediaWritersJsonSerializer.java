/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers.serializers.media;

import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class MediaWritersJsonSerializer extends JsonSerializer<List<WriterDTO>> {

    @Override
    public void serialize(List<WriterDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartArray();
        for (WriterDTO writerDTO : t) {
            jg.writeStartObject();
            jg.writeNumberField("id", writerDTO.getId());
            jg.writeStringField("first_name", writerDTO.getFirstName());
            jg.writeStringField("last_name", writerDTO.getLastName());
            jg.writeStringField("profile_photo_url", writerDTO.getProfilePhotoURL());
            jg.writeStringField("gender", writerDTO.getGender().toString());
            jg.writeEndObject();
        }
        jg.writeEndArray();
    }

}
