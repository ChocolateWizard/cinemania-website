/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers.serializers.acting;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActingRoleDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public class ActingJsonSerializer extends JsonSerializer<ActingDTO> {

    @Override
    public void serialize(ActingDTO t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartObject();
            jg.writeNumberField("id", t.getActor().getId());
            jg.writeStringField("first_name", t.getActor().getFirstName());
            jg.writeStringField("last_name", t.getActor().getLastName());
            jg.writeStringField("profile_photo_url", t.getActor().getProfilePhotoURL());
            jg.writeStringField("gender", t.getActor().getGender().toString());
            jg.writeBooleanField("is_star", t.getActor().isStar());
            jg.writeBooleanField("is_starring", t.isStarring());
            jg.writeArrayFieldStart("roles");
            for (ActingRoleDTO role : t.getRoles()) {
                jg.writeStartObject();
                jg.writeNumberField("id", role.getId());
                jg.writeStringField("name", role.getName());
                jg.writeEndObject();
            }
            jg.writeEndArray();
            jg.writeEndObject();
    }

}
