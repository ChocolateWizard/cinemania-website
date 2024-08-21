/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.tv.TVShowActorResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowActorRoleResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowCritiqueCommentResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowCritiqueResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowCritiqueUserResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowDirectorResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowGenreResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.cwb.backend.domain.dto.tv.TVShowWriterResponseDTO;
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
import com.borak.cwb.backend.domain.jpa.CommentJPA;
import com.borak.cwb.backend.domain.jpa.CommentLikeDislikeJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueLikeDislikeJPA;
import com.borak.cwb.backend.domain.jpa.DirectorJPA;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
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
public final class TVShowTransformer {

    private final String MEDIA_IMAGES_BASE_URL;
    private final String PERSON_IMAGES_BASE_URL;
    private final String USER_IMAGES_BASE_URL;

    @Autowired
    public TVShowTransformer(ConfigProperties config) {
        this.MEDIA_IMAGES_BASE_URL = config.getMediaImagesBaseUrl();
        this.PERSON_IMAGES_BASE_URL = config.getPersonImagesBaseUrl();
        this.USER_IMAGES_BASE_URL = config.getUserImagesBaseUrl();
    }

//=================================================================================================================================
//JPA   
    public TVShowResponseDTO jpaToTVShowResponseWithGenres(TVShowJPA tvShow) {
        TVShowResponseDTO response = new TVShowResponseDTO();
        response.setId(tvShow.getId());
        response.setTitle(tvShow.getTitle());
        if (tvShow.getCoverImage() != null) {
            response.setCoverImageUrl(MEDIA_IMAGES_BASE_URL + tvShow.getCoverImage());
        }
        response.setDescription(tvShow.getDescription());
        response.setReleaseDate(tvShow.getReleaseDate());
        response.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        response.setAudienceRating(tvShow.getAudienceRating());
        response.setCriticsRating(tvShow.getCriticsRating());

        for (GenreJPA genre : tvShow.getGenres()) {
            response.getGenres().add(new TVShowGenreResponseDTO(genre.getId(), genre.getName()));
        }
        return response;

    }

    public TVShowResponseDTO jpaToTVShowResponseWithDetails(TVShowJPA tvShow) {
        TVShowResponseDTO response = new TVShowResponseDTO();
        response.setId(tvShow.getId());
        response.setTitle(tvShow.getTitle());
        if (tvShow.getCoverImage() != null) {
            response.setCoverImageUrl(MEDIA_IMAGES_BASE_URL + tvShow.getCoverImage());
        }
        response.setDescription(tvShow.getDescription());
        response.setReleaseDate(tvShow.getReleaseDate());
        response.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        response.setAudienceRating(tvShow.getAudienceRating());
        response.setCriticsRating(tvShow.getCriticsRating());

        for (GenreJPA genre : tvShow.getGenres()) {
            response.getGenres().add(new TVShowGenreResponseDTO(genre.getId(), genre.getName()));
        }
        for (DirectorJPA director : tvShow.getDirectors()) {
            TVShowDirectorResponseDTO directorResponse = new TVShowDirectorResponseDTO();
            directorResponse.setId(director.getPersonId());
            directorResponse.setFirstName(director.getPerson().getFirstName());
            directorResponse.setLastName(director.getPerson().getLastName());
            directorResponse.setGender(director.getPerson().getGender());
            if (director.getPerson().getProfilePhoto() != null) {
                directorResponse.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + director.getPerson().getProfilePhoto());
            }
            response.getDirectors().add(directorResponse);
        }
        for (WriterJPA writer : tvShow.getWriters()) {
            TVShowWriterResponseDTO writerResponse = new TVShowWriterResponseDTO();
            writerResponse.setId(writer.getPersonId());
            writerResponse.setFirstName(writer.getPerson().getFirstName());
            writerResponse.setLastName(writer.getPerson().getLastName());
            writerResponse.setGender(writer.getPerson().getGender());
            if (writer.getPerson().getProfilePhoto() != null) {
                writerResponse.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + writer.getPerson().getProfilePhoto());
            }
            response.getWriters().add(writerResponse);
        }
        for (ActingJPA acting : tvShow.getActings()) {
            TVShowActorResponseDTO actorResponse = new TVShowActorResponseDTO();
            ActorJPA actor = acting.getActor();
            actorResponse.setId(actor.getPersonId());
            actorResponse.setFirstName(actor.getPerson().getFirstName());
            actorResponse.setLastName(actor.getPerson().getLastName());
            actorResponse.setGender(actor.getPerson().getGender());
            actorResponse.setStar(actor.getStar());
            actorResponse.setStarring(acting.getStarring());
            if (actor.getPerson().getProfilePhoto() != null) {
                actorResponse.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + actor.getPerson().getProfilePhoto());
            }
            for (ActingRoleJPA role : acting.getRoles()) {
                actorResponse.getRoles().add(new TVShowActorRoleResponseDTO(role.getId().getOrderNumber(), role.getName()));
            }
            response.getActors().add(actorResponse);
        }
        for (CritiqueJPA critJpa : tvShow.getCritiques()) {
            TVShowCritiqueResponseDTO critResp = new TVShowCritiqueResponseDTO();
            critResp.setId(critJpa.getId());
            critResp.setDescription(critJpa.getDescription());
            critResp.setRating(critJpa.getRating());
            critResp.setCreatedAt(critJpa.getCreatedAt());

            TVShowCritiqueUserResponseDTO critic = new TVShowCritiqueUserResponseDTO();
            critic.setProfileName(critJpa.getUser().getProfileName());
            if (critJpa.getUser().getProfileImage() != null) {
                critic.setProfileImageUrl(USER_IMAGES_BASE_URL + critJpa.getUser().getProfileImage());
            }
            critResp.setCritic(critic);

            int numberOfLikes = 0;
            int numberOfDislikes = 0;

            for (CritiqueLikeDislikeJPA likeDislike : critJpa.getLikeDislikes()) {
                if (likeDislike.getIsLike()) {
                    numberOfLikes++;
                } else {
                    numberOfDislikes++;
                }
            }
            critResp.setNumberOfLikes(numberOfLikes);
            critResp.setNumberOfDislikes(numberOfDislikes);

            for (CommentJPA commJpa : critJpa.getComments()) {
                TVShowCritiqueCommentResponseDTO commRes = new TVShowCritiqueCommentResponseDTO();
                commRes.setId(commJpa.getId());
                commRes.setContent(commJpa.getContent());
                commRes.setCreatedAt(commJpa.getCreatedAt());

                TVShowCritiqueUserResponseDTO user = new TVShowCritiqueUserResponseDTO();
                user.setProfileName(commJpa.getUser().getProfileName());
                if (commJpa.getUser().getProfileImage() != null) {
                    user.setProfileImageUrl(USER_IMAGES_BASE_URL + commJpa.getUser().getProfileImage());
                }
                commRes.setUser(user);

                int numOfLikes = 0;
                int numOfDislikes = 0;

                for (CommentLikeDislikeJPA likeDislike : commJpa.getLikeDislikes()) {
                    if (likeDislike.getIsLike()) {
                        numOfLikes++;
                    } else {
                        numOfDislikes++;
                    }
                }
                commRes.setNumberOfLikes(numOfLikes);
                commRes.setNumberOfDislikes(numOfDislikes);
                critResp.getComments().add(commRes);
            }
            response.getCritiques().add(critResp);
        }
        return response;
    }

    public TVShowJPA toTVShowJPA(TVShowRequestDTO request) {
        List<Long> genreIds=Util.sortAsc(request.getGenres());
        List<Long> directorIds=Util.sortAsc(request.getDirectors());
        List<Long> writerIds=Util.sortAsc(request.getWriters());
        List<TVShowRequestDTO.Actor> actors = new ArrayList<>(request.getActors());
        actors.sort(Comparator.comparingLong(TVShowRequestDTO.Actor::getId));

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
        for (Long genre : genreIds) {
            tvShow.getGenres().add(new GenreJPA(genre));
        }
        for (Long director : directorIds) {
            tvShow.getDirectors().add(new DirectorJPA(director));
        }
        for (Long writer : writerIds) {
            tvShow.getWriters().add(new WriterJPA(writer));
        }
        for (TVShowRequestDTO.Actor actor : actors) {
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

//---------------------------------------------------------------------------------------------------------------------------------    
    public List<TVShowResponseDTO> jpaToTVShowResponseWithGenres(List<TVShowJPA> tvShows) {
        List<TVShowResponseDTO> list = new ArrayList<>(tvShows.size());
        for (TVShowJPA show : tvShows) {
            list.add(jpaToTVShowResponseWithGenres(show));
        }
        return list;
    }

    public List<TVShowResponseDTO> jpaToTVShowResponseWithDetails(List<TVShowJPA> tvShows) {
        List<TVShowResponseDTO> list = new ArrayList<>(tvShows.size());
        for (TVShowJPA show : tvShows) {
            list.add(jpaToTVShowResponseWithDetails(show));
        }
        return list;
    }

//=================================================================================================================================
//JDBC
    public TVShowResponseDTO toResponseFromJDBC(TVShowJDBC tvShow) {
        throw new UnsupportedOperationException("Not supported!");
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

//---------------------------------------------------------------------------------------------------------------------------------    
    public List<TVShowResponseDTO> jdbcToTVShowResponse(List<TVShowJDBC> tvShows) {
        List<TVShowResponseDTO> list = new ArrayList<>(tvShows.size());
        for (TVShowJDBC show : tvShows) {
            list.add(toResponseFromJDBC(show));
        }
        return list;
    }

}
