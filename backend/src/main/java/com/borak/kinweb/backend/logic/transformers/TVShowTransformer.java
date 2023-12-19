/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.kinweb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.logic.util.Util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class TVShowTransformer {

    @Autowired
    private ConfigProperties config;

    @Autowired
    private Util util;

    public TVShowResponseDTO toTVShowResponseDTO(TVShowJDBC tvShowJdbc) throws IllegalArgumentException {
        if (tvShowJdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        TVShowResponseDTO tvShowResponse = new TVShowResponseDTO();
        tvShowResponse.setId(tvShowJdbc.getId());
        tvShowResponse.setTitle(tvShowJdbc.getTitle());
        if (tvShowJdbc.getCoverImage() != null && !tvShowJdbc.getCoverImage().isEmpty()) {
            tvShowResponse.setCoverImageUrl(config.getMediaImagesBaseUrl() + tvShowJdbc.getCoverImage());
        }
        tvShowResponse.setDescription(tvShowJdbc.getDescription());
        tvShowResponse.setReleaseDate(tvShowJdbc.getReleaseDate());
        tvShowResponse.setNumberOfSeasons(tvShowJdbc.getNumberOfSeasons());
        tvShowResponse.setAudienceRating(tvShowJdbc.getAudienceRating());
        tvShowResponse.setCriticsRating(tvShowJdbc.getCriticRating());

        for (GenreJDBC genre : tvShowJdbc.getGenres()) {
            tvShowResponse.getGenres().add(new TVShowResponseDTO.Genre(genre.getId(), genre.getName()));
        }

        for (CritiqueJDBC critique : tvShowJdbc.getCritiques()) {
            TVShowResponseDTO.Critique.Critic criticResponse = new TVShowResponseDTO.Critique.Critic();
            criticResponse.setUsername(critique.getCritic().getUsername());
            if (critique.getCritic().getProfileImage() != null && !critique.getCritic().getProfileImage().isEmpty()) {
                criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getCritic().getProfileImage());
            }
            tvShowResponse.getCritiques().add(new TVShowResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
        }

        for (DirectorJDBC director : tvShowJdbc.getDirectors()) {
            TVShowResponseDTO.Director directorResponse = new TVShowResponseDTO.Director();
            directorResponse.setId(director.getId());
            directorResponse.setFirstName(director.getFirstName());
            directorResponse.setLastName(director.getLastName());
            directorResponse.setGender(director.getGender());
            if (director.getProfilePhoto() != null && !director.getProfilePhoto().isEmpty()) {
                directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getProfilePhoto());
            }
            tvShowResponse.getDirectors().add(directorResponse);
        }
        for (WriterJDBC writer : tvShowJdbc.getWriters()) {
            TVShowResponseDTO.Writer writerResponse = new TVShowResponseDTO.Writer();
            writerResponse.setId(writer.getId());
            writerResponse.setFirstName(writer.getFirstName());
            writerResponse.setLastName(writer.getLastName());
            writerResponse.setGender(writer.getGender());
            if (writer.getProfilePhoto() != null && !writer.getProfilePhoto().isEmpty()) {
                writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getProfilePhoto());
            }
            tvShowResponse.getWriters().add(writerResponse);
        }
        for (ActingJDBC acting : tvShowJdbc.getActings()) {
            TVShowResponseDTO.Actor actorResponse = new TVShowResponseDTO.Actor();
            actorResponse.setId(acting.getActor().getId());
            actorResponse.setFirstName(acting.getActor().getFirstName());
            actorResponse.setLastName(acting.getActor().getLastName());
            actorResponse.setGender(acting.getActor().getGender());
            actorResponse.setStar(acting.getActor().isStar());
            actorResponse.setStarring(acting.isStarring());
            if (acting.getActor().getProfilePhoto() != null && !acting.getActor().getProfilePhoto().isEmpty()) {
                actorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + acting.getActor().getProfilePhoto());
            }
            for (ActingRoleJDBC role : acting.getRoles()) {
                actorResponse.getRoles().add(new TVShowResponseDTO.Actor.Role(role.getId(), role.getName()));
            }
            tvShowResponse.getActors().add(actorResponse);
        }

        return tvShowResponse;
    }

    public TVShowJDBC toTVShowJDBC(TVShowRequestDTO tvShow) throws IllegalArgumentException {
        if (tvShow == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        tvShow.setGenres(util.sortAsc(tvShow.getGenres()));
        tvShow.setDirectors(util.sortAsc(tvShow.getDirectors()));
        tvShow.setWriters(util.sortAsc(tvShow.getWriters()));
        List<TVShowRequestDTO.Actor> pom = new ArrayList<>(tvShow.getActors());
        pom.sort(Comparator.comparingLong(TVShowRequestDTO.Actor::getId));
        tvShow.setActors(pom);

        TVShowJDBC jdbc = new TVShowJDBC();
        jdbc.setId(tvShow.getId());
        jdbc.setTitle(tvShow.getTitle());
        if (tvShow.getCoverImage() != null) {
            jdbc.setCoverImage(tvShow.getCoverImage().getFullName());
        }
        jdbc.setDescription(tvShow.getDescription());
        jdbc.setReleaseDate(tvShow.getReleaseDate());
        jdbc.setAudienceRating(tvShow.getAudienceRating());
        jdbc.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        for (Long genre : tvShow.getGenres()) {
            jdbc.getGenres().add(new GenreJDBC(genre));
        }
        for (Long director : tvShow.getDirectors()) {
            jdbc.getDirectors().add(new DirectorJDBC(director));
        }
        for (Long writer : tvShow.getWriters()) {
            jdbc.getWriters().add(new WriterJDBC(writer));
        }
        for (TVShowRequestDTO.Actor actor : tvShow.getActors()) {
            ActingJDBC acting = new ActingJDBC(jdbc, new ActorJDBC(actor.getId()), actor.getStarring());
            long i = 1;
            for (String role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJDBC(acting, i, role));
                i++;
            }
            jdbc.getActings().add(acting);
        }
        return jdbc;
    }
//---------------------------------------------------------------------------------------------------------------------------------

    public List<TVShowResponseDTO> toTVShowResponseDTO(List<TVShowJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowResponseDTO> list = new ArrayList<>();
        for (TVShowJDBC jd : jdbcList) {
            list.add(toTVShowResponseDTO(jd));
        }
        return list;
    }
}
