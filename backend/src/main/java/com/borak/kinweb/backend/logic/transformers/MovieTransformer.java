/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.constants.Constants;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class MovieTransformer implements GenericTransformer<MovieDTO, MovieJDBC, MovieJPA> {

    @Autowired
    private Environment env;
    
    
    @Override
    public MovieDTO jdbcToDto(MovieJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }

        MovieDTO movie = new MovieDTO();

        movie.setId(jdbc.getId());
        movie.setTitle(jdbc.getTitle());
        if(jdbc.getCoverImage()!=null && !jdbc.getCoverImage().isEmpty()){
            movie.setCoverImageUrl(Constants.MEDIA_IMAGES_BASE_URL+jdbc.getCoverImage());
        }       
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
                    critic.setProfileImage(critique.getCritic().getProfileImage());
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
                movie.getDirectors().add(new DirectorDTO(director.getId(), director.getFirstName(), director.getLastName(), director.getGender(), director.getProfilePhoto()));

            }

        }
        for (WriterJDBC writer : jdbc.getWriters()) {
            if (writer != null) {
                movie.getWriters().add(new WriterDTO(writer.getId(), writer.getFirstName(), writer.getLastName(), writer.getGender(), writer.getProfilePhoto()));

            }
        }

        for (ActingJDBC acting : jdbc.getActings()) {
            if (acting != null) {
                ActingDTO a = new ActingDTO();
                a.setMedia(movie);
                a.setStarring(acting.isStarring());
                if (acting.getActor() != null) {
                    a.setActor(new ActorDTO(acting.getActor().getId(), acting.getActor().getFirstName(), acting.getActor().getLastName(), acting.getActor().getGender(), acting.getActor().getProfilePhoto(), acting.getActor().isStar()));
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
        throw new UnsupportedOperationException("Not supported!");
    }
}
