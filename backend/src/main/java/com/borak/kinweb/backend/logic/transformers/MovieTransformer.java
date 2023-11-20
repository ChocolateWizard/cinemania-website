/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.movie.MoviePOSTRequestDTO;
import com.borak.kinweb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import java.util.ArrayList;
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

    public MovieResponseDTO jdbcToDto(MovieJDBC movieJdbc) throws IllegalArgumentException {
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
            criticResponse.setUsername(critique.getCritic().getUsername());
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

    public MovieJDBC dtoToJdbc(MoviePOSTRequestDTO movie) throws IllegalArgumentException {
        if (movie == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        MovieJDBC jdbc = new MovieJDBC();
        jdbc.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {

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
        for (MoviePOSTRequestDTO.Actor actor : movie.getActors()) {
            ActingJDBC acting = new ActingJDBC(jdbc, new ActorJDBC(actor.getId()), actor.getStarring());
            for (MoviePOSTRequestDTO.Actor.Role role : actor.getRoles()) {
                acting.getRoles().add(new ActingRoleJDBC(acting, role.getId(), role.getName()));
            }
            jdbc.getActings().add(acting);
        }
        return jdbc;
    }

    public List<MovieResponseDTO> jdbcToDto(List<MovieJDBC> jdbcList) throws IllegalArgumentException {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<MovieResponseDTO> list = new ArrayList<>();
        for (MovieJDBC jd : jdbcList) {
            list.add(jdbcToDto(jd));
        }
        return list;
    }

}
