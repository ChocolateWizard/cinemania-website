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

    public MediaResponseDTO jdbcToMediaResponse(MediaJDBC media) {
        MediaResponseDTO response = new MediaResponseDTO();
        response.setId(media.getId());
        response.setTitle(media.getTitle());
        if (media.getCoverImage() != null) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + media.getCoverImage());
        }
        response.setDescription(media.getDescription());
        response.setReleaseDate(media.getReleaseDate());
        response.setAudienceRating(media.getAudienceRating());
        response.setCriticsRating(media.getCriticRating());
        for (GenreJDBC genre : media.getGenres()) {
            response.getGenres().add(new MediaResponseDTO.Genre(genre.getId(), genre.getName()));
        }
        if (media instanceof MovieJDBC) {
            response.setMediaType(MediaType.MOVIE);
        } else if (media instanceof TVShowJDBC) {
            response.setMediaType(MediaType.TV_SHOW);
        }
        return response;
    }

    public MediaResponseDTO jpaToMediaResponse(MediaJPA media) {
        MediaResponseDTO response = new MediaResponseDTO();
        response.setId(media.getId());
        response.setTitle(media.getTitle());
        if (media.getCoverImage() != null) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + media.getCoverImage());
        }
        response.setDescription(media.getDescription());
        response.setReleaseDate(media.getReleaseDate());
        response.setAudienceRating(media.getAudienceRating());
        response.setCriticsRating(media.getCriticRating());
        for (GenreJPA genre : media.getGenres()) {
            response.getGenres().add(new MediaResponseDTO.Genre(genre.getId(), genre.getName()));
        }
        if (media instanceof MovieJPA) {
            response.setMediaType(MediaType.MOVIE);
        } else if (media instanceof TVShowJPA) {
            response.setMediaType(MediaType.TV_SHOW);
        }
        return response;
    }
//=================================================================================================================================

    public List<MediaResponseDTO> jdbcToMediaResponse(List<MediaJDBC> medias) {
        List<MediaResponseDTO> list = new ArrayList<>(medias.size());
        for (MediaJDBC media : medias) {
            list.add(jdbcToMediaResponse(media));
        }
        return list;
    }

    public List<MediaResponseDTO> jpaToMediaResponse(List<? extends MediaJPA> medias) {
        List<MediaResponseDTO> list = new ArrayList<>(medias.size());
        for (MediaJPA media : medias) {
            list.add(jpaToMediaResponse(media));
        }
        return list;
    }

}
