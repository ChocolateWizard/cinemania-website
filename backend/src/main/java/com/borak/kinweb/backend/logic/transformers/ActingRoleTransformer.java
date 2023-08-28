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
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.JDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.domain.jpa.classes.ActingRoleJPA;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;

/**
 *
 * @author Mr. Poyo
 */
public class ActingRoleTransformer implements GenericTransformer<ActingRoleDTO, ActingRoleJDBC, ActingRoleJPA> {
    
    @Override
    public ActingRoleDTO jdbcToDto(ActingRoleJDBC jdbc) throws IllegalArgumentException {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        ActingRoleDTO role = new ActingRoleDTO();
        role.setId(jdbc.getId());
        role.setName(jdbc.getName());
        if (jdbc.getActing() != null) {
            //Convert ActingJDBC to ActingDTO
//==================================================================================
            ActingDTO acting = new ActingDTO();
            
            if (jdbc.getActing().getMedia() != null) {
                //------------------------------------------------------------------------------------------------
                MediaDTO media = null;
                if (jdbc.getActing().getMedia() instanceof MovieJDBC movieJDBC) {
                    MovieDTO movie = new MovieDTO();
                    movie.setLength(movieJDBC.getLength());
                    media = movie;
                }
                if (jdbc.getActing().getMedia() instanceof TVShowJDBC tvShowJDBC) {
                    TVShowDTO show = new TVShowDTO();
                    show.setNumberOfSeasons(tvShowJDBC.getNumberOfSeasons());
                    media = show;
                }
                if (media == null) {
                    throw new IllegalArgumentException("Acting Media unknown type");
                }
                media.setId(jdbc.getActing().getMedia().getId());
                media.setTitle(jdbc.getActing().getMedia().getTitle());
                media.setCoverImageUrl(jdbc.getActing().getMedia().getCoverImage());
                media.setDescription(jdbc.getActing().getMedia().getDescription());
                media.setReleaseDate(jdbc.getActing().getMedia().getReleaseDate());
                media.setAudienceRating(jdbc.getActing().getMedia().getAudienceRating());
                media.setCriticRating(jdbc.getActing().getMedia().getCriticRating());
                acting.setMedia(media);
            }
            if (jdbc.getActing().getActor() != null) {
                acting.setActor(new ActorDTO(jdbc.getActing().getActor().getId(), jdbc.getActing().getActor().getFirstName(), jdbc.getActing().getActor().getLastName(), jdbc.getActing().getActor().getGender(), jdbc.getActing().getActor().getProfilePhoto(), jdbc.getActing().getActor().isStar()));
            }
            acting.setStarring(jdbc.getActing().isStarring());
            role.setActing(acting);
//==================================================================================
        }
        return role;
    }
    
    @Override
    public ActingRoleDTO jpaToDto(ActingRoleJPA jpa) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
