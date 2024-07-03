/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.WriterResponseDTO;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class WriterTransformer {

    @Autowired
    private ConfigProperties config;

    public WriterResponseDTO jdbcToWriterResponse(WriterJDBC writer) {
        WriterResponseDTO response = new WriterResponseDTO();
        response.setId(writer.getId());
        response.setFirstName(writer.getFirstName());
        response.setLastName(writer.getLastName());
        response.setGender(writer.getGender());
        if (writer.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getProfilePhoto());
        }
        return response;
    }

    public WriterResponseDTO jpaToWriterResponse(WriterJPA writer) {
        WriterResponseDTO response = new WriterResponseDTO();
        response.setId(writer.getPersonId());
        response.setFirstName(writer.getPerson().getFirstName());
        response.setLastName(writer.getPerson().getLastName());
        response.setGender(writer.getPerson().getGender());
        if (writer.getPerson().getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getPerson().getProfilePhoto());
        }
        return response;
    }

//=================================================================================================================================
    public List<WriterResponseDTO> jdbcToWriterResponse(List<WriterJDBC> writers) {
        List<WriterResponseDTO> list = new ArrayList<>(writers.size());
        for (WriterJDBC writer : writers) {
            list.add(jdbcToWriterResponse(writer));
        }
        return list;
    }

    public List<WriterResponseDTO> jpaToWriterResponse(List<WriterJPA> writers) {
        List<WriterResponseDTO> list = new ArrayList<>(writers.size());
        for (WriterJPA writer : writers) {
            list.add(jpaToWriterResponse(writer));
        }
        return list;
    }

}
