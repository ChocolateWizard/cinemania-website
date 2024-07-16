/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.domain.jpa.ActingJPA;
import com.borak.cwb.backend.domain.jpa.ActingRoleJPA;
import com.borak.cwb.backend.domain.jpa.ActorJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
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
public final class MovieTransformer {

    @Autowired
    private ConfigProperties config;

    public MovieResponseDTO jdbcToMovieResponse(MovieJDBC movie) {
        MovieResponseDTO response = new MovieResponseDTO();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + movie.getCoverImage());
        }
        response.setDescription(movie.getDescription());
        response.setReleaseDate(movie.getReleaseDate());
        response.setLength(movie.getLength());
        response.setAudienceRating(movie.getAudienceRating());
        response.setCriticsRating(movie.getCriticRating());

        for (GenreJDBC genre : movie.getGenres()) {
            response.getGenres().add(new MovieResponseDTO.Genre(genre.getId(), genre.getName()));
        }

        for (CritiqueJDBC critique : movie.getCritiques()) {
            MovieResponseDTO.Critique.Critic criticResponse = new MovieResponseDTO.Critique.Critic();
            criticResponse.setProfileName(critique.getCritic().getProfileName());
            if (critique.getCritic().getProfileImage() != null && !critique.getCritic().getProfileImage().isEmpty()) {
                criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getCritic().getProfileImage());
            }
            response.getCritiques().add(new MovieResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
        }

        for (DirectorJDBC director : movie.getDirectors()) {
            MovieResponseDTO.Director directorResponse = new MovieResponseDTO.Director();
            directorResponse.setId(director.getId());
            directorResponse.setFirstName(director.getFirstName());
            directorResponse.setLastName(director.getLastName());
            directorResponse.setGender(director.getGender());
            if (director.getProfilePhoto() != null) {
                directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getProfilePhoto());
            }
            response.getDirectors().add(directorResponse);
        }
        for (WriterJDBC writer : movie.getWriters()) {
            MovieResponseDTO.Writer writerResponse = new MovieResponseDTO.Writer();
            writerResponse.setId(writer.getId());
            writerResponse.setFirstName(writer.getFirstName());
            writerResponse.setLastName(writer.getLastName());
            writerResponse.setGender(writer.getGender());
            if (writer.getProfilePhoto() != null) {
                writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getProfilePhoto());
            }
            response.getWriters().add(writerResponse);
        }
        for (ActingJDBC acting : movie.getActings()) {
            MovieResponseDTO.Actor actorResponse = new MovieResponseDTO.Actor();
            actorResponse.setId(acting.getActor().getId());
            actorResponse.setFirstName(acting.getActor().getFirstName());
            actorResponse.setLastName(acting.getActor().getLastName());
            actorResponse.setGender(acting.getActor().getGender());
            actorResponse.setStar(acting.getActor().isStar());
            actorResponse.setStarring(acting.isStarring());
            if (acting.getActor().getProfilePhoto() != null) {
                actorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + acting.getActor().getProfilePhoto());
            }
            for (ActingRoleJDBC role : acting.getRoles()) {
                actorResponse.getRoles().add(new MovieResponseDTO.Actor.Role(role.getId(), role.getName()));
            }
            response.getActors().add(actorResponse);
        }

        return response;
    }

    public MovieResponseDTO jpaToMovieResponse(MovieJPA movie, String option) throws IllegalArgumentException {
        MovieResponseDTO response = new MovieResponseDTO();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {
            response.setCoverImageUrl(config.getMediaImagesBaseUrl() + movie.getCoverImage());
        }
        response.setDescription(movie.getDescription());
        response.setReleaseDate(movie.getReleaseDate());
        response.setLength(movie.getLength());
        response.setAudienceRating(movie.getAudienceRating());
        response.setCriticsRating(movie.getCriticRating());

        switch (option) {
            case "genres" -> {
                for (GenreJPA genre : movie.getGenres()) {
                    response.getGenres().add(new MovieResponseDTO.Genre(genre.getId(), genre.getName()));
                }
                return response;
            }
            case "details" -> {
                for (GenreJPA genre : movie.getGenres()) {
                    response.getGenres().add(new MovieResponseDTO.Genre(genre.getId(), genre.getName()));
                }
                for (DirectorJPA director : movie.getDirectors()) {
                    MovieResponseDTO.Director directorResponse = new MovieResponseDTO.Director();
                    directorResponse.setId(director.getPersonId());
                    directorResponse.setFirstName(director.getPerson().getFirstName());
                    directorResponse.setLastName(director.getPerson().getLastName());
                    directorResponse.setGender(director.getPerson().getGender());
                    if (director.getPerson().getProfilePhoto() != null) {
                        directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getPerson().getProfilePhoto());
                    }
                    response.getDirectors().add(directorResponse);
                }
                for (WriterJPA writer : movie.getWriters()) {
                    MovieResponseDTO.Writer writerResponse = new MovieResponseDTO.Writer();
                    writerResponse.setId(writer.getPersonId());
                    writerResponse.setFirstName(writer.getPerson().getFirstName());
                    writerResponse.setLastName(writer.getPerson().getLastName());
                    writerResponse.setGender(writer.getPerson().getGender());
                    if (writer.getPerson().getProfilePhoto() != null) {
                        writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getPerson().getProfilePhoto());
                    }
                    response.getWriters().add(writerResponse);
                }
                for (ActingJPA acting : movie.getActings()) {
                    MovieResponseDTO.Actor actorResponse = new MovieResponseDTO.Actor();
                    ActorJPA actor = acting.getActor();
                    actorResponse.setId(actor.getPersonId());
                    actorResponse.setFirstName(actor.getPerson().getFirstName());
                    actorResponse.setLastName(actor.getPerson().getLastName());
                    actorResponse.setGender(actor.getPerson().getGender());
                    actorResponse.setStar(actor.getStar());
                    actorResponse.setStarring(acting.getStarring());
                    if (actor.getPerson().getProfilePhoto() != null) {
                        actorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + actor.getPerson().getProfilePhoto());
                    }
                    for (ActingRoleJPA role : acting.getRoles()) {
                        actorResponse.getRoles().add(new MovieResponseDTO.Actor.Role(role.getId().getId(), role.getName()));
                    }
                    response.getActors().add(actorResponse);
                }
                for (CritiqueJPA critique : movie.getCritiques()) {
                    MovieResponseDTO.Critique.Critic criticResponse = new MovieResponseDTO.Critique.Critic();
                    UserJPA critic = critique.getId().getCritic();
                    criticResponse.setProfileName(critic.getProfileName());
                    if (critic.getProfileImage() != null) {
                        criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critic.getProfileImage());
                    }
                    response.getCritiques().add(new MovieResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
                }
                return response;
            }
            default ->
                throw new IllegalArgumentException("Invalid option set!");
        }
    }

    public MovieJDBC toMovieJDBC(MovieRequestDTO request) {
        request.setGenres(Util.sortAsc(request.getGenres()));
        request.setDirectors(Util.sortAsc(request.getDirectors()));
        request.setWriters(Util.sortAsc(request.getWriters()));
        List<MovieRequestDTO.Actor> pom = new ArrayList<>(request.getActors());
        pom.sort(Comparator.comparingLong(MovieRequestDTO.Actor::getId));
        request.setActors(pom);

        MovieJDBC movie = new MovieJDBC();
        movie.setId(request.getId());
        movie.setTitle(request.getTitle());
        if (request.getCoverImage() != null) {
            movie.setCoverImage(request.getCoverImage().getFullName());
        }
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setAudienceRating(request.getAudienceRating());
        movie.setLength(request.getLength());
        for (Long genre : request.getGenres()) {
            movie.getGenres().add(new GenreJDBC(genre));
        }
        for (Long director : request.getDirectors()) {
            movie.getDirectors().add(new DirectorJDBC(director));
        }
        for (Long writer : request.getWriters()) {
            movie.getWriters().add(new WriterJDBC(writer));
        }
        for (MovieRequestDTO.Actor actor : request.getActors()) {
            ActingJDBC acting = new ActingJDBC(movie, new ActorJDBC(actor.getId()), actor.getStarring());
            long i = 1;
            for (String role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJDBC(acting, i, role));
                i++;
            }
            movie.getActings().add(acting);
        }
        return movie;
    }

    public MovieJPA toMovieJPA(MovieRequestDTO request) {
        request.setGenres(Util.sortAsc(request.getGenres()));
        request.setDirectors(Util.sortAsc(request.getDirectors()));
        request.setWriters(Util.sortAsc(request.getWriters()));
        List<MovieRequestDTO.Actor> pom = new ArrayList<>(request.getActors());
        pom.sort(Comparator.comparingLong(MovieRequestDTO.Actor::getId));
        request.setActors(pom);

        MovieJPA movie = new MovieJPA();
        movie.setId(request.getId());
        movie.setTitle(request.getTitle());
        if (request.getCoverImage() != null) {
            movie.setCoverImage(request.getCoverImage().getFullName());
        }
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setAudienceRating(request.getAudienceRating());
        movie.setLength(request.getLength());
        for (Long genre : request.getGenres()) {
            movie.getGenres().add(new GenreJPA(genre));
        }
        for (Long director : request.getDirectors()) {
            movie.getDirectors().add(new DirectorJPA(director));
        }
        for (Long writer : request.getWriters()) {
            movie.getWriters().add(new WriterJPA(writer));
        }
        for (MovieRequestDTO.Actor actor : request.getActors()) {
            ActingJPA acting = new ActingJPA(movie, new ActorJPA(actor.getId()), actor.getStarring());
            long i = 1;
            for (String role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJPA(new ActingRoleJPA.ID(acting, i), role));
                i++;
            }
            movie.getActings().add(acting);
        }
        return movie;
    }

//=================================================================================================================================
    public List<MovieResponseDTO> jdbcToMovieResponse(List<MovieJDBC> movies) {
        List<MovieResponseDTO> list = new ArrayList<>(movies.size());
        for (MovieJDBC movie : movies) {
            list.add(jdbcToMovieResponse(movie));
        }
        return list;
    }

    public List<MovieResponseDTO> jpaToMovieResponse(List<MovieJPA> movies, String option) throws IllegalArgumentException {
        List<MovieResponseDTO> list = new ArrayList<>(movies.size());
        for (MovieJPA movie : movies) {
            list.add(jpaToMovieResponse(movie, option));
        }
        return list;
    }

}
