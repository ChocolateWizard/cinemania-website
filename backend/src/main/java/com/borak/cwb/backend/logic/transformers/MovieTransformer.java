/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.movie.MovieActorResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieActorRoleResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieCritiqueCommentResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieCritiqueResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieCritiqueUserResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieDirectorResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieGenreResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieWriterResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
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
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.WriterJPA;
import com.borak.cwb.backend.logic.util.Util;
import java.time.LocalDateTime;
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

    private final String MEDIA_IMAGES_BASE_URL;
    private final String PERSON_IMAGES_BASE_URL;
    private final String USER_IMAGES_BASE_URL;

    @Autowired
    public MovieTransformer(ConfigProperties config) {
        this.MEDIA_IMAGES_BASE_URL = config.getMediaImagesBaseUrl();
        this.PERSON_IMAGES_BASE_URL = config.getPersonImagesBaseUrl();
        this.USER_IMAGES_BASE_URL = config.getUserImagesBaseUrl();
    }

//=================================================================================================================================
//JPA 
    public MovieResponseDTO jpaToMovieResponseWithGenres(MovieJPA movie) {
        MovieResponseDTO response = new MovieResponseDTO();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {
            response.setCoverImageUrl(MEDIA_IMAGES_BASE_URL + movie.getCoverImage());
        }
        response.setDescription(movie.getDescription());
        response.setReleaseDate(movie.getReleaseDate());
        response.setLength(movie.getLength());
        response.setAudienceRating(movie.getAudienceRating());
        response.setCriticsRating(movie.getCriticsRating());

        for (GenreJPA genre : movie.getGenres()) {
            response.getGenres().add(new MovieGenreResponseDTO(genre.getId(), genre.getName()));
        }
        return response;
    }

    public MovieResponseDTO jpaToMovieResponseWithDetails(MovieJPA movie) {
        MovieResponseDTO response = new MovieResponseDTO();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        if (movie.getCoverImage() != null) {
            response.setCoverImageUrl(MEDIA_IMAGES_BASE_URL + movie.getCoverImage());
        }
        response.setDescription(movie.getDescription());
        response.setReleaseDate(movie.getReleaseDate());
        response.setLength(movie.getLength());
        response.setAudienceRating(movie.getAudienceRating());
        response.setCriticsRating(movie.getCriticsRating());

        for (GenreJPA genre : movie.getGenres()) {
            response.getGenres().add(new MovieGenreResponseDTO(genre.getId(), genre.getName()));
        }
        for (DirectorJPA director : movie.getDirectors()) {
            MovieDirectorResponseDTO directorResponse = new MovieDirectorResponseDTO();
            directorResponse.setId(director.getPersonId());
            directorResponse.setFirstName(director.getPerson().getFirstName());
            directorResponse.setLastName(director.getPerson().getLastName());
            directorResponse.setGender(director.getPerson().getGender());
            if (director.getPerson().getProfilePhoto() != null) {
                directorResponse.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + director.getPerson().getProfilePhoto());
            }
            response.getDirectors().add(directorResponse);
        }
        for (WriterJPA writer : movie.getWriters()) {
            MovieWriterResponseDTO writerResponse = new MovieWriterResponseDTO();
            writerResponse.setId(writer.getPersonId());
            writerResponse.setFirstName(writer.getPerson().getFirstName());
            writerResponse.setLastName(writer.getPerson().getLastName());
            writerResponse.setGender(writer.getPerson().getGender());
            if (writer.getPerson().getProfilePhoto() != null) {
                writerResponse.setProfilePhotoUrl(PERSON_IMAGES_BASE_URL + writer.getPerson().getProfilePhoto());
            }
            response.getWriters().add(writerResponse);
        }
        for (ActingJPA acting : movie.getActings()) {
            MovieActorResponseDTO actorResponse = new MovieActorResponseDTO();
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
                actorResponse.getRoles().add(new MovieActorRoleResponseDTO(role.getId().getOrderNumber(), role.getName()));
            }
            response.getActors().add(actorResponse);
        }
        for (CritiqueJPA critJpa : movie.getCritiques()) {
            MovieCritiqueResponseDTO critResp = new MovieCritiqueResponseDTO();
            critResp.setId(critJpa.getId());
            critResp.setDescription(critJpa.getDescription());
            critResp.setRating(critJpa.getRating());
            critResp.setCreatedAt(critJpa.getCreatedAt());

            MovieCritiqueUserResponseDTO critic = new MovieCritiqueUserResponseDTO();
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
                MovieCritiqueCommentResponseDTO commRes = new MovieCritiqueCommentResponseDTO();
                commRes.setId(commJpa.getId());
                commRes.setContent(commJpa.getContent());
                commRes.setCreatedAt(commJpa.getCreatedAt());

                MovieCritiqueUserResponseDTO user = new MovieCritiqueUserResponseDTO();
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

    public MovieJPA toMovieJPA(MovieRequestDTO request) {
        List<Long> genreIds = Util.sortAsc(request.getGenres());
        List<Long> directorIds = Util.sortAsc(request.getDirectors());
        List<Long> writerIds = Util.sortAsc(request.getWriters());
        List<MovieRequestDTO.Actor> actors = new ArrayList<>(request.getActors());
        actors.sort(Comparator.comparingLong(MovieRequestDTO.Actor::getId));

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
        for (Long genre : genreIds) {
            movie.getGenres().add(new GenreJPA(genre));
        }
        for (Long director : directorIds) {
            movie.getDirectors().add(new DirectorJPA(director));
        }
        for (Long writer : writerIds) {
            movie.getWriters().add(new WriterJPA(writer));
        }
        for (MovieRequestDTO.Actor actor : actors) {
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

//---------------------------------------------------------------------------------------------------------------------------------
    public List<MovieResponseDTO> jpaToMovieResponseWithGenres(List<MovieJPA> movies) {
        List<MovieResponseDTO> list = new ArrayList<>(movies.size());
        for (MovieJPA movie : movies) {
            list.add(jpaToMovieResponseWithGenres(movie));
        }
        return list;
    }

    public List<MovieResponseDTO> jpaToMovieResponseWithDetails(List<MovieJPA> movies) {
        List<MovieResponseDTO> list = new ArrayList<>(movies.size());
        for (MovieJPA movie : movies) {
            list.add(jpaToMovieResponseWithDetails(movie));
        }
        return list;
    }

//=================================================================================================================================
//JDBC
    public MovieResponseDTO jdbcToMovieResponse(MovieJDBC movie) {
        throw new UnsupportedOperationException("Not supported!");
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

//---------------------------------------------------------------------------------------------------------------------------------
    public List<MovieResponseDTO> jdbcToMovieResponse(List<MovieJDBC> movies) {
        List<MovieResponseDTO> list = new ArrayList<>(movies.size());
        for (MovieJDBC movie : movies) {
            list.add(jdbcToMovieResponse(movie));
        }
        return list;
    }

}
