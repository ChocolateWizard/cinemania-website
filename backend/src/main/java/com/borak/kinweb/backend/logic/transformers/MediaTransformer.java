/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.media.MediaResponseDTO;
import com.borak.kinweb.backend.domain.enums.MediaType;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
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

    public MediaResponseDTO toMediaResponseDTO(MediaJDBC mediaJDBC) throws IllegalArgumentException {
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

    public List<MediaResponseDTO> toMediaResponseDTO(List<MediaJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MediaResponseDTO> list = new ArrayList<>();
        for (MediaJDBC jd : jdbcList) {
            list.add(toMediaResponseDTO(jd));
        }
        return list;
    }

}
