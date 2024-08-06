/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.media.MediaResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserCommentLikeDislikesDTO;
import com.borak.cwb.backend.domain.dto.user.UserCountryResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserCritiqueLikeDislikesDTO;
import com.borak.cwb.backend.domain.dto.user.UserLibraryMediaGenreResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserLibraryMediaResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserLibraryResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.cwb.backend.domain.dto.user.UserResponseDTO;
import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.domain.jdbc.CountryJDBC;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.jpa.CommentJPA;
import com.borak.cwb.backend.domain.jpa.CommentLikeDislikeJPA;
import com.borak.cwb.backend.domain.jpa.CountryJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueLikeDislikeJPA;
import com.borak.cwb.backend.domain.jpa.GenreJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.TVShowJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class UserTransformer {

    private final String MEDIA_IMAGES_BASE_URL;
    private final String USER_IMAGES_BASE_URL;
    private final PasswordEncoder encoder;

    @Autowired
    public UserTransformer(ConfigProperties config, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.MEDIA_IMAGES_BASE_URL = config.getMediaImagesBaseUrl();
        this.USER_IMAGES_BASE_URL = config.getUserImagesBaseUrl();
    }

//=================================================================================================================================
//JPA
    public UserJPA toUserJPA(UserRegisterDTO registerForm) {
        UserJPA user = new UserJPA();
        user.setFirstName(registerForm.getFirstName());
        user.setLastName(registerForm.getLastName());
        user.setGender(registerForm.getGender());
        user.setRole(registerForm.getRole());
        user.setProfileName(registerForm.getProfileName());
        if (registerForm.getProfileImage() != null) {
            user.setProfileImage(registerForm.getProfileName() + "." + registerForm.getProfileImage().getExtension());
        }
        user.setUsername(registerForm.getUsername());
        user.setPassword(encoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setCountry(new CountryJPA(registerForm.getCountryId()));
        return user;
    }

    public UserResponseDTO jpaToUserResponse(UserJPA user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setGender(user.getGender());
        response.setProfileName(user.getProfileName());
        if (user.getProfileImage() != null) {
            response.setProfileImageUrl(USER_IMAGES_BASE_URL + user.getProfileImage());
        }
        response.setRole(user.getRole());
        response.setCountry(new UserCountryResponseDTO(
                user.getCountry().getId(),
                user.getCountry().getName(),
                user.getCountry().getOfficialStateName(),
                user.getCountry().getCode()));

        //set users library
        UserLibraryResponseDTO library = new UserLibraryResponseDTO();
        for (MediaJPA media : user.getMedias()) {
            UserLibraryMediaResponseDTO libraryMedia = new UserLibraryMediaResponseDTO();
            libraryMedia.setId(media.getId());
            libraryMedia.setTitle(media.getTitle());
            libraryMedia.setReleaseDate(media.getReleaseDate());
            if (media.getCoverImage() != null) {
                libraryMedia.setCoverImageUrl(MEDIA_IMAGES_BASE_URL + media.getCoverImage());
            }
            libraryMedia.setAudienceRating(media.getAudienceRating());
            for (GenreJPA genre : media.getGenres()) {
                libraryMedia.getGenres().add(new UserLibraryMediaGenreResponseDTO(genre.getId(), genre.getName()));
            }
            if (media instanceof MovieJPA) {
                library.getMovies().add(libraryMedia);
            } else {
                library.getTvShows().add(libraryMedia);
            }
        }
        response.setLibrary(library);

        //set users critiques
        if (user.getRole() == UserRole.CRITIC || user.getRole() == UserRole.ADMINISTRATOR) {
            for (CritiqueJPA critique : user.getCritiques()) {
                response.getCritiques().add(critique.getId());
            }
        }
        if (user.getRole() == UserRole.REGULAR || user.getRole() == UserRole.ADMINISTRATOR) {
            for (CommentJPA comment : user.getComments()) {
                response.getComments().add(comment.getId());
            }
        }
        for (CritiqueLikeDislikeJPA critiqueLikeDislike : user.getCritiqueLikeDislikes()) {
            response.getCritiquesLikeDislikes().add(
                    new UserCritiqueLikeDislikesDTO(
                            critiqueLikeDislike.getId().getCritique().getId(),
                            critiqueLikeDislike.getIsLike()
                    )
            );
        }
        for (CommentLikeDislikeJPA commentsLikeDislike : user.getCommentsLikeDislikes()) {
            response.getCommentsLikeDislikes().add(
                    new UserCommentLikeDislikesDTO(
                            commentsLikeDislike.getId().getComment().getId(),
                            commentsLikeDislike.getIsLike()
                    )
            );
        }
        return response;
    }
//=================================================================================================================================
//JDBC

    public UserJDBC toUserJDBC(UserRegisterDTO registerForm) {
        UserJDBC user = new UserJDBC();
        user.setFirstName(registerForm.getFirstName());
        user.setLastName(registerForm.getLastName());
        user.setGender(registerForm.getGender());
        user.setRole(registerForm.getRole());
        user.setProfileName(registerForm.getProfileName());
        if (registerForm.getProfileImage() != null) {
            user.setProfileImage(registerForm.getProfileName() + "." + registerForm.getProfileImage().getExtension());
        }
        user.setUsername(registerForm.getUsername());
        user.setPassword(encoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        LocalDateTime nowTime = LocalDateTime.now();
        user.setCreatedAt(nowTime);
        user.setCountry(new CountryJDBC(registerForm.getCountryId()));
        return user;
    }

    public UserResponseDTO jdbcToUserResponse(UserJDBC userJDBC) {
        throw new UnsupportedOperationException("Not supported!");
    }

}
