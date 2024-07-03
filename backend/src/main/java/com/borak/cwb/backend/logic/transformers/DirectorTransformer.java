/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.person.DirectorResponseDTO;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class DirectorTransformer {

    @Autowired
    private ConfigProperties config;

    public DirectorResponseDTO jdbcToDirectorResponse(DirectorJDBC director) {
        DirectorResponseDTO response = new DirectorResponseDTO();
        response.setId(director.getId());
        response.setFirstName(director.getFirstName());
        response.setLastName(director.getLastName());
        response.setGender(director.getGender());
        if (director.getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getProfilePhoto());
        }
        return response;
    }

    public DirectorResponseDTO jpaToDirectorResponse(DirectorJPA director) {
        DirectorResponseDTO response = new DirectorResponseDTO();
        response.setId(director.getPersonId());
        response.setFirstName(director.getPerson().getFirstName());
        response.setLastName(director.getPerson().getLastName());
        response.setGender(director.getPerson().getGender());
        if (director.getPerson().getProfilePhoto() != null) {
            response.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getPerson().getProfilePhoto());
        }
        return response;
    }

//=================================================================================================================================
    public List<DirectorResponseDTO> jdbcToDirectorResponse(List<DirectorJDBC> directors) {
        List<DirectorResponseDTO> list = new ArrayList<>(directors.size());
        for (DirectorJDBC director : directors) {
            list.add(jdbcToDirectorResponse(director));
        }
        return list;
    }

    public List<DirectorResponseDTO> jpaToDirectorResponse(List<DirectorJPA> directors) {
        List<DirectorResponseDTO> list = new ArrayList<>(directors.size());
        for (DirectorJPA director : directors) {
            list.add(jpaToDirectorResponse(director));
        }
        return list;
    }

}
