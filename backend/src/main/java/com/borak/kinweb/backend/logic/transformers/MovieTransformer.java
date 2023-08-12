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
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.ActingJPA;
import com.borak.kinweb.backend.domain.jpa.classes.ActingRoleJPA;
import com.borak.kinweb.backend.domain.jpa.classes.CritiqueJPA;
import com.borak.kinweb.backend.domain.jpa.classes.DirectorJPA;
import com.borak.kinweb.backend.domain.jpa.classes.GenreJPA;
import com.borak.kinweb.backend.domain.jpa.classes.MovieJPA;
import com.borak.kinweb.backend.domain.jpa.classes.WriterJPA;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class MovieTransformer implements GenericTransformer<MovieDTO, MovieJDBC, MovieJPA> {

    @Override
    public MovieDTO jdbcToDto(MovieJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        MovieDTO movie = new MovieDTO();

        movie.setId(jdbc.getId());
        movie.setTitle(jdbc.getTitle());
        movie.setCoverImageUrl(jdbc.getCoverImageUrl());
        movie.setDescription(jdbc.getDescription());
        movie.setReleaseDate(jdbc.getReleaseDate());
        movie.setAudienceRating(jdbc.getAudienceRating());
        movie.setCriticRating(jdbc.getCriticRating());
        movie.setLength(jdbc.getLength());

        for (GenreJDBC genre : jdbc.getGenres()) {
            if (genre != null) {
                movie.getGenres().add(new GenreDTO(genre.getId(), genre.getName()));
            }
        }

        for (CritiqueJDBC critique : jdbc.getCritiques()) {
            if (critique != null) {
                CritiqueDTO c = new CritiqueDTO();
                if (critique.getCritic() != null) {
                    UserCriticDTO critic = new UserCriticDTO();
                    critic.setUsername(critique.getCritic().getUsername());
                    critic.setProfileImageUrl(critique.getCritic().getProfileImageUrl());
                    c.setCritic(critic);
                }
                c.setMedia(movie);
                c.setDescription(critique.getDescription());
                c.setRating(critique.getRating());

                movie.getCritiques().add(c);
            }
        }

        for (DirectorJDBC director : jdbc.getDirectors()) {
            if (director != null) {
                movie.getDirectors().add(new DirectorDTO(director.getId(), director.getFirstName(), director.getLastName(), director.getGender(), director.getProfilePhotoURL()));

            }

        }
        for (WriterJDBC writer : jdbc.getWriters()) {
            if (writer != null) {
                movie.getWriters().add(new WriterDTO(writer.getId(), writer.getFirstName(), writer.getLastName(), writer.getGender(), writer.getProfilePhotoURL()));

            }
        }

        for (ActingJDBC acting : jdbc.getActings()) {
            if (acting != null) {
                ActingDTO a = new ActingDTO();
                a.setMedia(movie);
                a.setStarring(acting.isStarring());
                if (acting.getActor() != null) {
                    a.setActor(new ActorDTO(acting.getActor().getId(), acting.getActor().getFirstName(), acting.getActor().getLastName(), acting.getActor().getGender(), acting.getActor().getProfilePhotoURL(), acting.getActor().isIsStar()));
                }
                for (ActingRoleJDBC role : acting.getRoles()) {
                    if (role != null) {
                        a.getRoles().add(new ActingRoleDTO(a, role.getId(), role.getName()));
                    }
                }
                movie.getActings().add(a);
            }
        }
        return movie;
    }

    @Override
    public MovieDTO jpaToDto(MovieJPA jpa) throws IllegalArgumentException {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        MovieDTO movie = new MovieDTO();

        movie.setId(jpa.getId());
        movie.setTitle(jpa.getTitle());
        movie.setCoverImageUrl(jpa.getCoverImageUrl());
        movie.setDescription(jpa.getDescription());
        movie.setReleaseDate(jpa.getReleaseDate());
        movie.setAudienceRating(jpa.getAudienceRating());
        movie.setCriticRating(jpa.getCriticRating());
        movie.setLength(jpa.getLength());

        for (GenreJPA genre : jpa.getGenres()) {
            if (genre != null) {
                movie.getGenres().add(new GenreDTO(genre.getId(), genre.getName()));
            }
        }

        for (CritiqueJPA critique : jpa.getCritiques()) {
            if (critique != null) {
                CritiqueDTO c = new CritiqueDTO();
                if (critique.getCritic() != null) {
                    UserCriticDTO critic = new UserCriticDTO();
                    critic.setUsername(critique.getCritic().getUsername());
                    critic.setProfileImageUrl(critique.getCritic().getProfileImageUrl());
                    c.setCritic(critic);
                }
                c.setMedia(movie);
                c.setDescription(critique.getDescription());
                c.setRating(critique.getRating());

                movie.getCritiques().add(c);
            }
        }

        for (DirectorJPA director : jpa.getDirectors()) {
            if (director != null) {
                movie.getDirectors().add(new DirectorDTO(director.getId(), director.getFirstName(), director.getLastName(), director.getGender(), director.getProfilePhotoURL()));

            }

        }
        for (WriterJPA writer : jpa.getWriters()) {
            if (writer != null) {
                movie.getWriters().add(new WriterDTO(writer.getId(), writer.getFirstName(), writer.getLastName(), writer.getGender(), writer.getProfilePhotoURL()));

            }
        }

        for (ActingJPA acting : jpa.getActings()) {
            if (acting != null) {
                ActingDTO a = new ActingDTO();
                a.setMedia(movie);
                if (acting.getActor() != null) {
                    a.setActor(new ActorDTO(acting.getActor().getId(), acting.getActor().getFirstName(), acting.getActor().getLastName(), acting.getActor().getGender(), acting.getActor().getProfilePhotoURL(), acting.getActor().isIsStar()));
                }
                for (ActingRoleJPA role : acting.getRoles()) {
                    if (role != null) {
                        a.getRoles().add(new ActingRoleDTO(a, role.getId(), role.getName()));
                    }
                }
                movie.getActings().add(a);
            }
        }
        return movie;
    }
}
