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

    @Autowired
    private Util util;

    public MovieResponseDTO toResponseFromJDBC(MovieJDBC movieJdbc) throws IllegalArgumentException {
        if (movieJdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        MovieResponseDTO movieResponse = new MovieResponseDTO();
        movieResponse.setId(movieJdbc.getId());
        movieResponse.setTitle(movieJdbc.getTitle());
        if (movieJdbc.getCoverImage() != null && !movieJdbc.getCoverImage().isEmpty()) {
            movieResponse.setCoverImageUrl(config.getMediaImagesBaseUrl() + movieJdbc.getCoverImage());
        }
        movieResponse.setDescription(movieJdbc.getDescription());
        movieResponse.setReleaseDate(movieJdbc.getReleaseDate());
        movieResponse.setLength(movieJdbc.getLength());
        movieResponse.setAudienceRating(movieJdbc.getAudienceRating());
        movieResponse.setCriticsRating(movieJdbc.getCriticRating());

        for (GenreJDBC genre : movieJdbc.getGenres()) {
            movieResponse.getGenres().add(new MovieResponseDTO.Genre(genre.getId(), genre.getName()));
        }

        for (CritiqueJDBC critique : movieJdbc.getCritiques()) {
            MovieResponseDTO.Critique.Critic criticResponse = new MovieResponseDTO.Critique.Critic();
            criticResponse.setProfileName(critique.getCritic().getProfileName());
            if (critique.getCritic().getProfileImage() != null && !critique.getCritic().getProfileImage().isEmpty()) {
                criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critique.getCritic().getProfileImage());
            }
            movieResponse.getCritiques().add(new MovieResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
        }

        for (DirectorJDBC director : movieJdbc.getDirectors()) {
            MovieResponseDTO.Director directorResponse = new MovieResponseDTO.Director();
            directorResponse.setId(director.getId());
            directorResponse.setFirstName(director.getFirstName());
            directorResponse.setLastName(director.getLastName());
            directorResponse.setGender(director.getGender());
            if (director.getProfilePhoto() != null && !director.getProfilePhoto().isEmpty()) {
                directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getProfilePhoto());
            }
            movieResponse.getDirectors().add(directorResponse);
        }
        for (WriterJDBC writer : movieJdbc.getWriters()) {
            MovieResponseDTO.Writer writerResponse = new MovieResponseDTO.Writer();
            writerResponse.setId(writer.getId());
            writerResponse.setFirstName(writer.getFirstName());
            writerResponse.setLastName(writer.getLastName());
            writerResponse.setGender(writer.getGender());
            if (writer.getProfilePhoto() != null && !writer.getProfilePhoto().isEmpty()) {
                writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getProfilePhoto());
            }
            movieResponse.getWriters().add(writerResponse);
        }
        for (ActingJDBC acting : movieJdbc.getActings()) {
            MovieResponseDTO.Actor actorResponse = new MovieResponseDTO.Actor();
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
                actorResponse.getRoles().add(new MovieResponseDTO.Actor.Role(role.getId(), role.getName()));
            }
            movieResponse.getActors().add(actorResponse);
        }

        return movieResponse;
    }

    public MovieResponseDTO toResponseFromJPA(MovieJPA movieJpa, Class<?>[] classArray) throws IllegalArgumentException {
        if (movieJpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        MovieResponseDTO movieResponse = new MovieResponseDTO();
        movieResponse.setId(movieJpa.getId());
        movieResponse.setTitle(movieJpa.getTitle());
        if (movieJpa.getCoverImage() != null && !movieJpa.getCoverImage().isEmpty()) {
            movieResponse.setCoverImageUrl(config.getMediaImagesBaseUrl() + movieJpa.getCoverImage());
        }
        movieResponse.setDescription(movieJpa.getDescription());
        movieResponse.setReleaseDate(movieJpa.getReleaseDate());
        movieResponse.setLength(movieJpa.getLength());
        movieResponse.setAudienceRating(movieJpa.getAudienceRating());
        movieResponse.setCriticsRating(movieJpa.getCriticRating());

        for (Class<?> clazz : classArray) {
            if (clazz.equals(GenreJPA.class)) {
                for (GenreJPA genre : movieJpa.getGenres()) {
                    movieResponse.getGenres().add(new MovieResponseDTO.Genre(genre.getId(), genre.getName()));
                }
                continue;
            }
            if (clazz.equals(DirectorJPA.class)) {
                for (DirectorJPA director : movieJpa.getDirectors()) {
                    MovieResponseDTO.Director directorResponse = new MovieResponseDTO.Director();
                    directorResponse.setId(director.getPersonId());
                    directorResponse.setFirstName(director.getPerson().getFirstName());
                    directorResponse.setLastName(director.getPerson().getLastName());
                    directorResponse.setGender(director.getPerson().getGender());
                    if (director.getPerson().getProfilePhoto() != null && !director.getPerson().getProfilePhoto().isEmpty()) {
                        directorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + director.getPerson().getProfilePhoto());
                    }
                    movieResponse.getDirectors().add(directorResponse);
                }
                continue;
            }
            if (clazz.equals(WriterJPA.class)) {
                for (WriterJPA writer : movieJpa.getWriters()) {
                    MovieResponseDTO.Writer writerResponse = new MovieResponseDTO.Writer();
                    writerResponse.setId(writer.getPersonId());
                    writerResponse.setFirstName(writer.getPerson().getFirstName());
                    writerResponse.setLastName(writer.getPerson().getLastName());
                    writerResponse.setGender(writer.getPerson().getGender());
                    if (writer.getPerson().getProfilePhoto() != null && !writer.getPerson().getProfilePhoto().isEmpty()) {
                        writerResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + writer.getPerson().getProfilePhoto());
                    }
                    movieResponse.getWriters().add(writerResponse);
                }
                continue;
            }
            if (clazz.equals(ActingJPA.class)) {
                for (ActingJPA acting : movieJpa.getActings()) {
                    MovieResponseDTO.Actor actorResponse = new MovieResponseDTO.Actor();
                    ActorJPA actor = acting.getActor();
                    actorResponse.setId(actor.getPersonId());
                    actorResponse.setFirstName(actor.getPerson().getFirstName());
                    actorResponse.setLastName(actor.getPerson().getLastName());
                    actorResponse.setGender(actor.getPerson().getGender());
                    actorResponse.setStar(actor.getStar());
                    actorResponse.setStarring(acting.getStarring());
                    if (actor.getPerson().getProfilePhoto() != null && !actor.getPerson().getProfilePhoto().isEmpty()) {
                        actorResponse.setProfilePhotoUrl(config.getPersonImagesBaseUrl() + actor.getPerson().getProfilePhoto());
                    }
                    for (ActingRoleJPA role : acting.getRoles()) {
                        actorResponse.getRoles().add(new MovieResponseDTO.Actor.Role(role.getId().getId(), role.getName()));
                    }
                    movieResponse.getActors().add(actorResponse);
                }
                continue;
            }
            if (clazz.equals(CritiqueJPA.class)) {
                for (CritiqueJPA critique : movieJpa.getCritiques()) {
                    MovieResponseDTO.Critique.Critic criticResponse = new MovieResponseDTO.Critique.Critic();
                    UserJPA critic = critique.getId().getCritic();
                    criticResponse.setProfileName(critic.getProfileName());
                    if (critic.getProfileImage() != null && !critic.getProfileImage().isEmpty()) {
                        criticResponse.setProfileImageUrl(config.getUserImagesBaseUrl() + critic.getProfileImage());
                    }
                    movieResponse.getCritiques().add(new MovieResponseDTO.Critique(criticResponse, critique.getRating(), critique.getDescription()));
                }
            }
        }
        return movieResponse;
    }

    public MovieJDBC toMovieJDBC(MovieRequestDTO movie) throws IllegalArgumentException {
        if (movie == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        movie.setGenres(util.sortAsc(movie.getGenres()));
        movie.setDirectors(util.sortAsc(movie.getDirectors()));
        movie.setWriters(util.sortAsc(movie.getWriters()));
        List<MovieRequestDTO.Actor> pom = new ArrayList<>(movie.getActors());
        pom.sort(Comparator.comparingLong(MovieRequestDTO.Actor::getId));
        movie.setActors(pom);

        MovieJDBC jdbc = new MovieJDBC();
        jdbc.setId(movie.getId());
        jdbc.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {
            jdbc.setCoverImage(movie.getCoverImage().getFullName());
        }
        jdbc.setDescription(movie.getDescription());
        jdbc.setReleaseDate(movie.getReleaseDate());
        jdbc.setAudienceRating(movie.getAudienceRating());
        jdbc.setLength(movie.getLength());
        for (Long genre : movie.getGenres()) {
            jdbc.getGenres().add(new GenreJDBC(genre));
        }
        for (Long director : movie.getDirectors()) {
            jdbc.getDirectors().add(new DirectorJDBC(director));
        }
        for (Long writer : movie.getWriters()) {
            jdbc.getWriters().add(new WriterJDBC(writer));
        }
        for (MovieRequestDTO.Actor actor : movie.getActors()) {
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

    public MovieJPA toMovieJPA(MovieRequestDTO movie) throws IllegalArgumentException {
        if (movie == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        movie.setGenres(util.sortAsc(movie.getGenres()));
        movie.setDirectors(util.sortAsc(movie.getDirectors()));
        movie.setWriters(util.sortAsc(movie.getWriters()));
        List<MovieRequestDTO.Actor> pom = new ArrayList<>(movie.getActors());
        pom.sort(Comparator.comparingLong(MovieRequestDTO.Actor::getId));
        movie.setActors(pom);

        MovieJPA jpa = new MovieJPA();
        jpa.setId(movie.getId());
        jpa.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {
            jpa.setCoverImage(movie.getCoverImage().getFullName());
        }
        jpa.setDescription(movie.getDescription());
        jpa.setReleaseDate(movie.getReleaseDate());
        jpa.setAudienceRating(movie.getAudienceRating());
        jpa.setLength(movie.getLength());
        for (Long genre : movie.getGenres()) {
            jpa.getGenres().add(new GenreJPA(genre));
        }
        for (Long director : movie.getDirectors()) {
            jpa.getDirectors().add(new DirectorJPA(director));
        }
        for (Long writer : movie.getWriters()) {
            jpa.getWriters().add(new WriterJPA(writer));
        }
        for (MovieRequestDTO.Actor actor : movie.getActors()) {
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
    public List<MovieResponseDTO> toResponseFromJDBC(List<MovieJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieResponseDTO> list = new ArrayList<>();
        for (MovieJDBC jd : jdbcList) {
            list.add(toResponseFromJDBC(jd));
        }
        return list;
    }

    public List<MovieResponseDTO> toResponseFromJPA(List<MovieJPA> jpaList, Class<?>[] classArray) throws IllegalArgumentException {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieResponseDTO> list = new ArrayList<>();
        for (MovieJPA jp : jpaList) {
            list.add(toResponseFromJPA(jp, classArray));
        }
        return list;
    }

}
