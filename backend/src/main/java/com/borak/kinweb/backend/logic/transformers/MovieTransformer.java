/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActingRoleDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActorDTO;
import com.borak.kinweb.backend.domain.dto.classes.CritiqueDTO;
import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.borak.kinweb.backend.domain.dto.classes.GenreDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.UserCriticDTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.DirectorJPA;
import com.borak.kinweb.backend.domain.jpa.classes.GenreJPA;
import com.borak.kinweb.backend.domain.jpa.classes.MovieJPA;
import com.borak.kinweb.backend.domain.jpa.classes.WriterJPA;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class MovieTransformer implements GenericTransformer<MovieDTO, MovieJDBC, MovieJPA> {

    @Override
    public MovieDTO jdbcToDto(MovieJDBC jdbc) {
        MovieDTO movie = new MovieDTO();

        movie.setId(jdbc.getId());
        movie.setTitle(jdbc.getTitle());
        movie.setCoverImageUrl(jdbc.getCoverImageUrl());
        movie.setDescription(jdbc.getDescription());
        movie.setReleaseDate(jdbc.getReleaseDate());
        movie.setAudienceRating(jdbc.getAudienceRating());
        movie.setCriticRating(jdbc.getCriticRating());
        movie.setLength(jdbc.getLength());

        jdbc.getGenres().forEach((genre) -> {
            movie.getGenres().add(new GenreDTO(genre.getId(), genre.getName()));
        });

        jdbc.getCritiques().forEach((critique) -> {
            CritiqueDTO c = new CritiqueDTO();
            UserCriticDTO critic = new UserCriticDTO();
            critic.setUsername(critique.getCritic().getUsername());
            critic.setProfileImageUrl(critique.getCritic().getProfileImageUrl());
            c.setCritic(critic);
            c.setMedia(movie);
            c.setDescription(critique.getDescription());
            c.setRating(critique.getRating());

            movie.getCritiques().add(c);
        });

        jdbc.getDirectors().forEach((director) -> {
            movie.getDirectors().add(new DirectorDTO(director.getId(), director.getFirstName(), director.getLastName(), director.getGender(), director.getProfilePhotoURL()));
        });

        jdbc.getWriters().forEach((writer) -> {
            movie.getWriters().add(new WriterDTO(writer.getId(), writer.getFirstName(), writer.getLastName(), writer.getGender(), writer.getProfilePhotoURL()));
        });

        jdbc.getActings().forEach((acting) -> {
            ActingDTO a = new ActingDTO();
            a.setMedia(movie);
            a.setActor(new ActorDTO(acting.getActor().getId(), acting.getActor().getFirstName(), acting.getActor().getLastName(), acting.getActor().getGender(), acting.getActor().getProfilePhotoURL(), acting.getActor().isIsStar()));
            acting.getRoles().forEach((role) -> {
                a.getRoles().add(new ActingRoleDTO(a, role.getId(), role.getName()));
            });
            movie.getActings().add(a);
        });
        return movie;
    }

    @Override
    public MovieDTO jpaToDto(MovieJPA jpa) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<MovieDTO> jdbcToDto(List<MovieJDBC> jdbcList) {
        List<MovieDTO> movies = new ArrayList<>();
        jdbcList.forEach((jdbc) -> {
            MovieDTO movie = new MovieDTO();
            movie.setId(jdbc.getId());
            movie.setTitle(jdbc.getTitle());
            movie.setCoverImageUrl(jdbc.getCoverImageUrl());
            movie.setDescription(jdbc.getDescription());
            movie.setReleaseDate(jdbc.getReleaseDate());
            movie.setAudienceRating(jdbc.getAudienceRating());
            movie.setCriticRating(jdbc.getCriticRating());
            movie.setLength(jdbc.getLength());

            jdbc.getGenres().forEach((genre) -> {
                movie.getGenres().add(new GenreDTO(genre.getId(), genre.getName()));
            });

            jdbc.getCritiques().forEach((critique) -> {
                CritiqueDTO c = new CritiqueDTO();
                UserCriticDTO critic = new UserCriticDTO();
                critic.setUsername(critique.getCritic().getUsername());
                critic.setProfileImageUrl(critique.getCritic().getProfileImageUrl());
                c.setCritic(critic);
                c.setMedia(movie);
                c.setDescription(critique.getDescription());
                c.setRating(critique.getRating());

                movie.getCritiques().add(c);
            });

            jdbc.getDirectors().forEach((director) -> {
                movie.getDirectors().add(new DirectorDTO(director.getId(), director.getFirstName(), director.getLastName(), director.getGender(), director.getProfilePhotoURL()));
            });

            jdbc.getWriters().forEach((writer) -> {
                movie.getWriters().add(new WriterDTO(writer.getId(), writer.getFirstName(), writer.getLastName(), writer.getGender(), writer.getProfilePhotoURL()));
            });

            jdbc.getActings().forEach((acting) -> {
                ActingDTO a = new ActingDTO();
                a.setMedia(movie);
                a.setActor(new ActorDTO(acting.getActor().getId(), acting.getActor().getFirstName(), acting.getActor().getLastName(), acting.getActor().getGender(), acting.getActor().getProfilePhotoURL(), acting.getActor().isIsStar()));
                acting.getRoles().forEach((role) -> {
                   a.getRoles().add(new ActingRoleDTO(a, role.getId(), role.getName()));
                });
                movie.getActings().add(a);
            });
            movies.add(movie);
        });
        return movies;
    }

    @Override
    public List<MovieDTO> jpaToDto(List<MovieJPA> jpaList) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
