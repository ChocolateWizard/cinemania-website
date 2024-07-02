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

    @Autowired
    private Util util;

    public TVShowResponseDTO toResponseFromJDBC(TVShowJDBC tvShowJdbc) throws IllegalArgumentException {
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
            criticResponse.setProfileName(critique.getCritic().getProfileName());
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

    public TVShowResponseDTO toResponseFromJPA(TVShowJPA tvShowJpa, Class<?>[] classArray) throws IllegalArgumentException {
        if (tvShowJpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        TVShowResponseDTO tvShowResponse = new TVShowResponseDTO();
        tvShowResponse.setId(tvShowJpa.getId());
        tvShowResponse.setTitle(tvShowJpa.getTitle());
        if (tvShowJpa.getCoverImage() != null && !tvShowJpa.getCoverImage().isEmpty()) {
            tvShowResponse.setCoverImageUrl(config.getMediaImagesBaseUrl() + tvShowJpa.getCoverImage());
        }
        tvShowResponse.setDescription(tvShowJpa.getDescription());
        tvShowResponse.setReleaseDate(tvShowJpa.getReleaseDate());
        tvShowResponse.setNumberOfSeasons(tvShowJpa.getNumberOfSeasons());
        tvShowResponse.setAudienceRating(tvShowJpa.getAudienceRating());
        tvShowResponse.setCriticsRating(tvShowJpa.getCriticRating());

        for (Class<?> clazz : classArray) {
            if (clazz.equals(GenreJPA.class)) {
                for (GenreJPA genre : tvShowJpa.getGenres()) {
                    tvShowResponse.getGenres().add(new TVShowResponseDTO.Genre(genre.getId(), genre.getName()));
                }
                continue;
            }
            if (clazz.equals(DirectorJPA.class)) {
                for (DirectorJPA director : tvShowJpa.getDirectors()) {
                    TVShowResponseDTO.Director directorResponse = new TVShowResponseDTO.Director();
                    directorResponse.setId(director.getPersonId());
                    directorResponse.setFirstName(director.getPerson().getFirstName());
                    directorResponse.setLastName(director.getPerson().getLastName());
                    directorResponse.setGender(director.getPerson().getGender());
                    if (director.getPerson().getProfilePhoto() != null && !director.getPerson().getProfilePhoto().isEmpty()) {
                        directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getPerson().getProfilePhoto());
                    }
                    tvShowResponse.getDirectors().add(directorResponse);
                }
                continue;
            }
            if (clazz.equals(WriterJPA.class)) {
                for (WriterJPA writer : tvShowJpa.getWriters()) {
                    TVShowResponseDTO.Writer writerResponse = new TVShowResponseDTO.Writer();
                    writerResponse.setId(writer.getPersonId());
                    writerResponse.setFirstName(writer.getPerson().getFirstName());
                    writerResponse.setLastName(writer.getPerson().getLastName());
                    writerResponse.setGender(writer.getPerson().getGender());
                    if (writer.getPerson().getProfilePhoto() != null && !writer.getPerson().getProfilePhoto().isEmpty()) {
                        writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getPerson().getProfilePhoto());
                    }
                    tvShowResponse.getWriters().add(writerResponse);
                }
                continue;
            }
            if (clazz.equals(ActingJPA.class)) {
                for (ActingJPA acting : tvShowJpa.getActings()) {
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
                    tvShowResponse.getActors().add(actorResponse);
                }
                continue;
            }
            if (clazz.equals(CritiqueJPA.class)) {
                for (CritiqueJPA critique : tvShowJpa.getCritiques()) {
                    TVShowResponseDTO.Critique.Critic criticResponse = new TVShowResponseDTO.Critique.Critic();
                    criticResponse.setProfileName(critique.getId().getCritic().getProfileName());
                    if (critique.getId().getCritic().getProfileImage() != null && !critique.getId().getCritic().getProfileImage().isEmpty()) {
                        criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getId().getCritic().getProfileImage());
                    }
                    tvShowResponse.getCritiques().add(new TVShowResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
                }
            }
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

    public TVShowJPA toTVShowJPA(TVShowRequestDTO tvShow) throws IllegalArgumentException {
        if (tvShow == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        tvShow.setGenres(util.sortAsc(tvShow.getGenres()));
        tvShow.setDirectors(util.sortAsc(tvShow.getDirectors()));
        tvShow.setWriters(util.sortAsc(tvShow.getWriters()));
        List<TVShowRequestDTO.Actor> pom = new ArrayList<>(tvShow.getActors());
        pom.sort(Comparator.comparingLong(TVShowRequestDTO.Actor::getId));
        tvShow.setActors(pom);

        TVShowJPA jpa = new TVShowJPA();
        jpa.setId(tvShow.getId());
        jpa.setTitle(tvShow.getTitle());
        if (tvShow.getCoverImage() != null) {
            jpa.setCoverImage(tvShow.getCoverImage().getFullName());
        }
        jpa.setDescription(tvShow.getDescription());
        jpa.setReleaseDate(tvShow.getReleaseDate());
        jpa.setAudienceRating(tvShow.getAudienceRating());
        jpa.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        for (Long genre : tvShow.getGenres()) {
            jpa.getGenres().add(new GenreJPA(genre));
        }
        for (Long director : tvShow.getDirectors()) {
            jpa.getDirectors().add(new DirectorJPA(director));
        }
        for (Long writer : tvShow.getWriters()) {
            jpa.getWriters().add(new WriterJPA(writer));
        }
        for (TVShowRequestDTO.Actor actor : tvShow.getActors()) {
            ActingJPA acting = new ActingJPA(jpa, new ActorJPA(actor.getId()), actor.getStarring());
            long i = 1;
            for (String role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJPA(new ActingRoleJPA.ID(acting, i), role));
                i++;
            }
            jpa.getActings().add(acting);
        }
        return jpa;
    }
//---------------------------------------------------------------------------------------------------------------------------------

    public List<TVShowResponseDTO> toResponseFromJDBC(List<TVShowJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowResponseDTO> list = new ArrayList<>();
        for (TVShowJDBC jd : jdbcList) {
            list.add(TVShowTransformer.this.toResponseFromJDBC(jd));
        }
        return list;
    }

    public List<TVShowResponseDTO> toResponseFromJPA(List<TVShowJPA> jpaList, Class<?>[] classArray) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<TVShowResponseDTO> list = new ArrayList<>();
        for (TVShowJPA jp : jpaList) {
            list.add(toResponseFromJPA(jp, classArray));
        }
        return list;
    }

}
