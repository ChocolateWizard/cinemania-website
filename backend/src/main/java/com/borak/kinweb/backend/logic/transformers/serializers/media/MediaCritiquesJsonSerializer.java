/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers.serializers.media;

import com.borak.kinweb.backend.domain.dto.classes.CritiqueDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class MediaCritiquesJsonSerializer extends JsonSerializer<List<CritiqueDTO>> {

    @Override
    public void serialize(List<CritiqueDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartArray();
        for (CritiqueDTO critique : t) {
            jg.writeStartObject();
            jg.writeObjectFieldStart("critic");
            jg.writeStringField("username", critique.getCritic().getUsername());
            jg.writeStringField("profile_photo_url", critique.getCritic().getProfileImageUrl());
            jg.writeEndObject();
            jg.writeNumberField("rating", critique.getRating());
            jg.writeStringField("description", critique.getDescription());
            jg.writeEndObject();

        }
        jg.writeEndArray();
    }

}
