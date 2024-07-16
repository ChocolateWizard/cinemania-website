/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.ActingRoleJPA;
import com.borak.cwb.backend.domain.jpa.ActorJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
import com.borak.cwb.backend.logic.util.Util;
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

    public TVShowResponseDTO toResponseFromJDBC(TVShowJDBC tvShow) {
        TVShowResponseDTO response = new TVShowResponseDTO();
        response.setId(tvShow.getId());
        response.setTitle(tvShow.getTitle());
        if (tvShow.getCoverImage() != null) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + tvShow.getCoverImage());
        }
        response.setDescription(tvShow.getDescription());
        response.setReleaseDate(tvShow.getReleaseDate());
        response.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        response.setAudienceRating(tvShow.getAudienceRating());
        response.setCriticsRating(tvShow.getCriticRating());

        for (GenreJDBC genre : tvShow.getGenres()) {
            response.getGenres().add(new TVShowResponseDTO.Genre(genre.getId(), genre.getName()));
        }

        for (CritiqueJDBC critique : tvShow.getCritiques()) {
            TVShowResponseDTO.Critique.Critic criticResponse = new TVShowResponseDTO.Critique.Critic();
            criticResponse.setProfileName(critique.getCritic().getProfileName());
            if (critique.getCritic().getProfileImage() != null && !critique.getCritic().getProfileImage().isEmpty()) {
                criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getCritic().getProfileImage());
            }
            response.getCritiques().add(new TVShowResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
        }

        for (DirectorJDBC director : tvShow.getDirectors()) {
            TVShowResponseDTO.Director directorResponse = new TVShowResponseDTO.Director();
            directorResponse.setId(director.getId());
            directorResponse.setFirstName(director.getFirstName());
            directorResponse.setLastName(director.getLastName());
            directorResponse.setGender(director.getGender());
            if (director.getProfilePhoto() != null && !director.getProfilePhoto().isEmpty()) {
                directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getProfilePhoto());
            }
            response.getDirectors().add(directorResponse);
        }
        for (WriterJDBC writer : tvShow.getWriters()) {
            TVShowResponseDTO.Writer writerResponse = new TVShowResponseDTO.Writer();
            writerResponse.setId(writer.getId());
            writerResponse.setFirstName(writer.getFirstName());
            writerResponse.setLastName(writer.getLastName());
            writerResponse.setGender(writer.getGender());
            if (writer.getProfilePhoto() != null && !writer.getProfilePhoto().isEmpty()) {
                writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getProfilePhoto());
            }
            response.getWriters().add(writerResponse);
        }
        for (ActingJDBC acting : tvShow.getActings()) {
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
            response.getActors().add(actorResponse);
        }

        return response;
    }

    public TVShowResponseDTO toResponseFromJPA(TVShowJPA tvShow, String option) throws IllegalArgumentException {
        TVShowResponseDTO response = new TVShowResponseDTO();
        response.setId(tvShow.getId());
        response.setTitle(tvShow.getTitle());
        if (tvShow.getCoverImage() != null) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + tvShow.getCoverImage());
        }
        response.setDescription(tvShow.getDescription());
        response.setReleaseDate(tvShow.getReleaseDate());
        response.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        response.setAudienceRating(tvShow.getAudienceRating());
        response.setCriticsRating(tvShow.getCriticRating());

        switch (option) {
            case "genres" -> {
                for (GenreJPA genre : tvShow.getGenres()) {
                    response.getGenres().add(new TVShowResponseDTO.Genre(genre.getId(), genre.getName()));
                }
                return response;
            }
            case "details" -> {
                for (GenreJPA genre : tvShow.getGenres()) {
                    response.getGenres().add(new TVShowResponseDTO.Genre(genre.getId(), genre.getName()));
                }
                for (DirectorJPA director : tvShow.getDirectors()) {
                    TVShowResponseDTO.Director directorResponse = new TVShowResponseDTO.Director();
                    directorResponse.setId(director.getPersonId());
                    directorResponse.setFirstName(director.getPerson().getFirstName());
                    directorResponse.setLastName(director.getPerson().getLastName());
                    directorResponse.setGender(director.getPerson().getGender());
                    if (director.getPerson().getProfilePhoto() != null && !director.getPerson().getProfilePhoto().isEmpty()) {
                        directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getPerson().getProfilePhoto());
                    }
                    response.getDirectors().add(directorResponse);
                }
                for (WriterJPA writer : tvShow.getWriters()) {
                    TVShowResponseDTO.Writer writerResponse = new TVShowResponseDTO.Writer();
                    writerResponse.setId(writer.getPersonId());
                    writerResponse.setFirstName(writer.getPerson().getFirstName());
                    writerResponse.setLastName(writer.getPerson().getLastName());
                    writerResponse.setGender(writer.getPerson().getGender());
                    if (writer.getPerson().getProfilePhoto() != null && !writer.getPerson().getProfilePhoto().isEmpty()) {
                        writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getPerson().getProfilePhoto());
                    }
                    response.getWriters().add(writerResponse);
                }
                for (ActingJPA acting : tvShow.getActings()) {
                    TVShowResponseDTO.Actor actorResponse = new TVShowResponseDTO.Actor();
                    actorResponse.setId(acting.getActor().getPersonId());
                    actorResponse.setFirstName(acting.getActor().getPerson().getFirstName());
                    actorResponse.setLastName(acting.getActor().getPerson().getLastName());
                    actorResponse.setGender(acting.getActor().getPerson().getGender());
                    actorResponse.setStar(acting.getActor().getStar());
                    actorResponse.setStarring(acting.getStarring());
                    if (acting.getActor().getPerson().getProfilePhoto() != null && !acting.getActor().getPerson().getProfilePhoto().isEmpty()) {
                        actorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + acting.getActor().getPerson().getProfilePhoto());
                    }
                    for (ActingRoleJPA role : acting.getRoles()) {
                        actorResponse.getRoles().add(new TVShowResponseDTO.Actor.Role(role.getId().getId(), role.getName()));
                    }
                    response.getActors().add(actorResponse);
                }
                for (CritiqueJPA critique : tvShow.getCritiques()) {
                    TVShowResponseDTO.Critique.Critic criticResponse = new TVShowResponseDTO.Critique.Critic();
                    criticResponse.setProfileName(critique.getId().getCritic().getProfileName());
                    if (critique.getId().getCritic().getProfileImage() != null && !critique.getId().getCritic().getProfileImage().isEmpty()) {
                        criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getId().getCritic().getProfileImage());
                    }
                    response.getCritiques().add(new TVShowResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
                }
                return response;
            }
            default ->
                throw new IllegalArgumentException("Invalid option set!");
        }
    }

    public TVShowJDBC toTVShowJDBC(TVShowRequestDTO request) {
        request.setGenres(Util.sortAsc(request.getGenres()));
        request.setDirectors(Util.sortAsc(request.getDirectors()));
        request.setWriters(Util.sortAsc(request.getWriters()));
        List<TVShowRequestDTO.Actor> pom = new ArrayList<>(request.getActors());
        pom.sort(Comparator.comparingLong(TVShowRequestDTO.Actor::getId));
        request.setActors(pom);

        TVShowJDBC tvShow = new TVShowJDBC();
        tvShow.setId(request.getId());
        tvShow.setTitle(request.getTitle());
        if (request.getCoverImage() != null) {
            tvShow.setCoverImage(request.getCoverImage().getFullName());
        }
        tvShow.setDescription(request.getDescription());
        tvShow.setReleaseDate(request.getReleaseDate());
        tvShow.setAudienceRating(request.getAudienceRating());
        tvShow.setNumberOfSeasons(request.getNumberOfSeasons());
        for (Long genre : request.getGenres()) {
            tvShow.getGenres().add(new GenreJDBC(genre));
        }
        for (Long director : request.getDirectors()) {
            tvShow.getDirectors().add(new DirectorJDBC(director));
        }
        for (Long writer : request.getWriters()) {
            tvShow.getWriters().add(new WriterJDBC(writer));
        }
        for (TVShowRequestDTO.Actor actor : request.getActors()) {
            ActingJDBC acting = new ActingJDBC(tvShow, new ActorJDBC(actor.getId()), actor.getStarring());
            long i = 1;
            for (String role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJDBC(acting, i, role));
                i++;
            }
            tvShow.getActings().add(acting);
        }
        return tvShow;
    }

    public TVShowJPA toTVShowJPA(TVShowRequestDTO request) {
        request.setGenres(Util.sortAsc(request.getGenres()));
        request.setDirectors(Util.sortAsc(request.getDirectors()));
        request.setWriters(Util.sortAsc(request.getWriters()));
        List<TVShowRequestDTO.Actor> pom = new ArrayList<>(request.getActors());
        pom.sort(Comparator.comparingLong(TVShowRequestDTO.Actor::getId));
        request.setActors(pom);

        TVShowJPA tvShow = new TVShowJPA();
        tvShow.setId(request.getId());
        tvShow.setTitle(request.getTitle());
        if (request.getCoverImage() != null) {
            tvShow.setCoverImage(request.getCoverImage().getFullName());
        }
        tvShow.setDescription(request.getDescription());
        tvShow.setReleaseDate(request.getReleaseDate());
        tvShow.setAudienceRating(request.getAudienceRating());
        tvShow.setNumberOfSeasons(request.getNumberOfSeasons());
        for (Long genre : request.getGenres()) {
            tvShow.getGenres().add(new GenreJPA(genre));
        }
        for (Long director : request.getDirectors()) {
            tvShow.getDirectors().add(new DirectorJPA(director));
        }
        for (Long writer : request.getWriters()) {
            tvShow.getWriters().add(new WriterJPA(writer));
        }
        for (TVShowRequestDTO.Actor actor : request.getActors()) {
            ActingJPA acting = new ActingJPA(tvShow, new ActorJPA(actor.getId()), actor.getStarring());
            long i = 1;
            for (String role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJPA(new ActingRoleJPA.ID(acting, i), role));
                i++;
            }
            tvShow.getActings().add(acting);
        }
        return tvShow;
    }

//=================================================================================================================================
    public List<TVShowResponseDTO> jdbcToTVShowResponse(List<TVShowJDBC> tvShows) {
        List<TVShowResponseDTO> list = new ArrayList<>(tvShows.size());
        for (TVShowJDBC show : tvShows) {
            list.add(toResponseFromJDBC(show));
        }
        return list;
    }

    public List<TVShowResponseDTO> jpaToTVShowResponse(List<TVShowJPA> tvShows, String option) throws IllegalArgumentException {
        List<TVShowResponseDTO> list = new ArrayList<>(tvShows.size());
        for (TVShowJPA show : tvShows) {
            list.add(toResponseFromJPA(show, option));
        }
        return list;
    }

}
