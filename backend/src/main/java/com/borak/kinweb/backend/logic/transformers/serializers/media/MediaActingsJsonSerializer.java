/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers.serializers.media;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActingRoleDTO;
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
public class MediaActingsJsonSerializer extends JsonSerializer<List<ActingDTO>> {

    @Override
    public void serialize(List<ActingDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartArray();
        for (ActingDTO acting : t) {
            jg.writeStartObject();
            jg.writeNumberField("id", acting.getActor().getId());
            jg.writeStringField("first_name", acting.getActor().getFirstName());
            jg.writeStringField("last_name", acting.getActor().getLastName());
            jg.writeStringField("profile_photo_url", acting.getActor().getProfilePhoto());
            jg.writeStringField("gender", acting.getActor().getGender().toString());
            jg.writeBooleanField("is_star", acting.getActor().isStar());
            jg.writeBooleanField("is_starring", acting.isStarring());
            jg.writeArrayFieldStart("roles");
            for (ActingRoleDTO role : acting.getRoles()) {
                jg.writeStartObject();
                jg.writeNumberField("id", role.getId());
                jg.writeStringField("name", role.getName());
                jg.writeEndObject();
            }
            jg.writeEndArray();
            jg.writeEndObject();
        }
        jg.writeEndArray();
    }

}
