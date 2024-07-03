/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.domain.dto.genre.GenreResponseDTO;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class GenreTransformer {

    public GenreResponseDTO jpaToGenreResponse(GenreJPA genre) {
        return new GenreResponseDTO(genre.getId(), genre.getName());
    }

//=================================================================================================================================
    public List<GenreResponseDTO> jpaToGenreResponse(List<GenreJPA> genres) {
        List<GenreResponseDTO> list = new ArrayList<>(genres.size());
        for (GenreJPA genre : genres) {
            list.add(jpaToGenreResponse(genre));
        }
        return list;
    }

}
