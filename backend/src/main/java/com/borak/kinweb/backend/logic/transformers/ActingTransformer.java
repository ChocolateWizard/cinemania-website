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
import com.borak.kinweb.backend.domain.dto.classes.GenreDTO;
import com.borak.kinweb.backend.domain.dto.classes.MediaDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.TVShowDTO;
import com.borak.kinweb.backend.domain.dto.classes.UserCriticDTO;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.ActingJPA;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class ActingTransformer implements GenericTransformer<ActingDTO, ActingJDBC, ActingJPA> {

    @Override
    public ActingDTO jdbcToDto(ActingJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        ActingDTO acting = new ActingDTO();
        acting.setStarring(jdbc.isStarring());
        if (jdbc.getMedia() != null) {
            //------------------------------------------------------------------------------------------------
            MediaDTO media = null;
            if (jdbc.getMedia() instanceof MovieJDBC movieJDBC) {
                MovieDTO movie = new MovieDTO();
                movie.setLength(movieJDBC.getLength());
                media = movie;
            }
            if (jdbc.getMedia() instanceof TVShowJDBC tvShowJDBC) {
                TVShowDTO show = new TVShowDTO();
                show.setNumberOfSeasons(tvShowJDBC.getNumberOfSeasons());
                media = show;
            }
            if (media == null) {
                throw new IllegalArgumentException("Acting Media unknown type");
            }
            media.setId(jdbc.getMedia().getId());
            media.setTitle(jdbc.getMedia().getTitle());
            media.setCoverImageUrl(jdbc.getMedia().getCoverImage());
            media.setDescription(jdbc.getMedia().getDescription());
            media.setReleaseDate(jdbc.getMedia().getReleaseDate());
            media.setAudienceRating(jdbc.getMedia().getAudienceRating());
            media.setCriticRating(jdbc.getMedia().getCriticRating());

            for (GenreJDBC genre : jdbc.getMedia().getGenres()) {
                if (genre != null) {
                    media.getGenres().add(new GenreDTO(genre.getId(), genre.getName()));
                }
            }

            for (CritiqueJDBC critique : jdbc.getMedia().getCritiques()) {
                if (critique != null) {
                    CritiqueDTO c = new CritiqueDTO();
                    if (critique.getCritic() != null) {
                        UserCriticDTO critic = new UserCriticDTO();
                        critic.setUsername(critique.getCritic().getUsername());
                        critic.setProfileImage(critique.getCritic().getProfileImage());
                        c.setCritic(critic);
                    }
                    c.setMedia(media);
                    c.setDescription(critique.getDescription());
                    c.setRating(critique.getRating());

                    media.getCritiques().add(c);
                }
            }

            for (DirectorJDBC director : jdbc.getMedia().getDirectors()) {
                if (director != null) {
                    media.getDirectors().add(new DirectorDTO(director.getId(), director.getFirstName(), director.getLastName(), director.getGender(), director.getProfilePhoto()));
                }
            }

            for (WriterJDBC writer : jdbc.getMedia().getWriters()) {
                if (writer != null) {
                    media.getWriters().add(new WriterDTO(writer.getId(), writer.getFirstName(), writer.getLastName(), writer.getGender(), writer.getProfilePhoto()));
                }
            }

            //iterate over actings of media of main acting
            boolean isAdded = false;
            for (ActingJDBC mediaActing : jdbc.getMedia().getActings()) {
                if (mediaActing != null) {
                    ActingDTO a = new ActingDTO();
                    a.setMedia(media);
                    if (mediaActing.getActor() != null) {
                        if (jdbc.getActor() != null && mediaActing.getActor().equals(jdbc.getActor())) {
                            isAdded = true;
                            media.getActings().add(acting);
                            continue;
                        } else {
                            a.setActor(new ActorDTO(mediaActing.getActor().getId(), mediaActing.getActor().getFirstName(), mediaActing.getActor().getLastName(), mediaActing.getActor().getGender(), mediaActing.getActor().getProfilePhoto(), mediaActing.getActor().isStar()));
                        }
                    }

                    for (ActingRoleJDBC mediaActingRole : mediaActing.getRoles()) {
                        if (mediaActingRole != null) {
                            a.getRoles().add(new ActingRoleDTO(a, mediaActingRole.getId(), mediaActingRole.getName()));
                        }
                    }
                    media.getActings().add(a);
                }
            }
            if (isAdded == false) {
                media.getActings().add(acting);
            }
            acting.setMedia(media);
        }

        if (jdbc.getActor() != null) {
            acting.setActor(new ActorDTO(jdbc.getActor().getId(), jdbc.getActor().getFirstName(), jdbc.getActor().getLastName(), jdbc.getActor().getGender(), jdbc.getActor().getProfilePhoto(), jdbc.getActor().isStar()));
        }

        for (ActingRoleJDBC roleDB : jdbc.getRoles()) {
            if (roleDB != null) {
                acting.getRoles().add(new ActingRoleDTO(acting, roleDB.getId(), roleDB.getName()));
            }
        }
        return acting;
    }

    @Override
    public ActingDTO jpaToDto(ActingJPA jpa) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
//---------------------------------------------------------------------------------------------------------------

}
