/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers.serializers.media;

import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class MediaDirectorsJsonSerializer extends JsonSerializer<List<DirectorDTO>> {

    @Override
    public void serialize(List<DirectorDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartArray();
        for (DirectorDTO directorDTO : t) {
            jg.writeStartObject();
            jg.writeNumberField("id", directorDTO.getId());
            jg.writeStringField("first_name", directorDTO.getFirstName());
            jg.writeStringField("last_name", directorDTO.getLastName());
            jg.writeStringField("profile_photo_url", directorDTO.getProfilePhotoURL());
            jg.writeStringField("gender", directorDTO.getGender().toString());
            jg.writeEndObject();
        }
        jg.writeEndArray();

    }

}
