/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.media.MediaResponseDTO;
import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class MediaTransformer {

    @Autowired
    private ConfigProperties config;

    public MediaResponseDTO toResponseFromJDBC(MediaJDBC mediaJDBC) throws IllegalArgumentException {
        if (mediaJDBC == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MediaResponseDTO response = new MediaResponseDTO();
        response.setId(mediaJDBC.getId());
        response.setTitle(mediaJDBC.getTitle());
        if (mediaJDBC.getCoverImage() != null && !mediaJDBC.getCoverImage().isEmpty()) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + mediaJDBC.getCoverImage());
        }
        response.setDescription(mediaJDBC.getDescription());
        response.setReleaseDate(mediaJDBC.getReleaseDate());
        response.setAudienceRating(mediaJDBC.getAudienceRating());
        response.setCriticsRating(mediaJDBC.getCriticRating());
        for (GenreJDBC genre : mediaJDBC.getGenres()) {
            response.getGenres().add(new MediaResponseDTO.Genre(genre.getId(), genre.getName()));
        }
        if (mediaJDBC instanceof MovieJDBC) {
            response.setMediaType(MediaType.MOVIE);
        } else if (mediaJDBC instanceof TVShowJDBC) {
            response.setMediaType(MediaType.TV_SHOW);
        } else {
            throw new IllegalArgumentException("Unknown media type!");
        }
        return response;
    }

    public List<MediaResponseDTO> toResponseFromJDBC(List<MediaJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MediaResponseDTO> list = new ArrayList<>();
        for (MediaJDBC jd : jdbcList) {
            list.add(toResponseFromJDBC(jd));
        }
        return list;
    }

    public MediaResponseDTO toResponseFromJPA(MediaJPA mediaJPA) throws IllegalArgumentException {
        if (mediaJPA == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MediaResponseDTO response = new MediaResponseDTO();
        response.setId(mediaJPA.getId());
        response.setTitle(mediaJPA.getTitle());
        if (mediaJPA.getCoverImage() != null && !mediaJPA.getCoverImage().isEmpty()) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + mediaJPA.getCoverImage());
        }
        response.setDescription(mediaJPA.getDescription());
        response.setReleaseDate(mediaJPA.getReleaseDate());
        response.setAudienceRating(mediaJPA.getAudienceRating());
        response.setCriticsRating(mediaJPA.getCriticRating());
        for (GenreJPA genre : mediaJPA.getGenres()) {
            response.getGenres().add(new MediaResponseDTO.Genre(genre.getId(), genre.getName()));
        }
        if (mediaJPA instanceof MovieJPA) {
            response.setMediaType(MediaType.MOVIE);
        } else if (mediaJPA instanceof TVShowJPA) {
            response.setMediaType(MediaType.TV_SHOW);
        } else {
            throw new IllegalArgumentException("Unknown media type!");
        }
        return response;
    }

    public List<MediaResponseDTO> toResponseFromJPA(List<MediaJPA> jpaList) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MediaResponseDTO> list = new ArrayList<>();
        for (MediaJPA jp : jpaList) {
            list.add(toResponseFromJPA(jp));
        }
        return list;
    }

}
