/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.media.MediaResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.cwb.backend.domain.dto.user.UserResponseDTO;
import com.borak.cwb.backend.domain.enums.MediaType;
import com.borak.cwb.backend.domain.jdbc.CountryJDBC;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.jpa.CountryJPA;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
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

    @Autowired
    private ConfigProperties config;

    @Autowired
    private PasswordEncoder encoder;

    public UserJDBC toUserJDBC(UserRegisterDTO registerForm) throws IllegalArgumentException {
        if (registerForm == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
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
        user.setUpdatedAt(nowTime);
        user.setCountry(new CountryJDBC(registerForm.getCountryId()));
        return user;
    }

    public UserJPA toUserJPA(UserRegisterDTO registerForm) throws IllegalArgumentException {
        if (registerForm == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
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
        LocalDateTime nowTime = LocalDateTime.now();
        user.setCreatedAt(nowTime);
        user.setUpdatedAt(nowTime);
        user.setCountry(new CountryJPA(registerForm.getCountryId()));
        return user;
    }

    public UserResponseDTO toUserResponseDTO(UserJDBC userJDBC) throws IllegalArgumentException {
        if (userJDBC == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        UserResponseDTO response = new UserResponseDTO();
        response.setFirstName(userJDBC.getFirstName());
        response.setLastName(userJDBC.getLastName());
        response.setProfileName(userJDBC.getProfileName());
        if (userJDBC.getProfileImage() != null) {
            response.setProfileImageUrl(config.getUserImagesBaseUrl() + userJDBC.getProfileImage());
        }
        response.setGender(userJDBC.getGender());
        response.setRole(userJDBC.getRole());

        UserResponseDTO.Country country = new UserResponseDTO.Country();
        country.setId(userJDBC.getCountry().getId());
        country.setName(userJDBC.getCountry().getName());
        country.setOfficialStateName(userJDBC.getCountry().getOfficialStateName());
        country.setCode(userJDBC.getCountry().getCode());
        response.setCountry(country);

        for (MediaJDBC media : userJDBC.getMedias()) {
            response.getMedias().add(toMediaResponseDTO(media));
        }
        for (CritiqueJDBC critique : userJDBC.getCritiques()) {
            UserResponseDTO.Critique critiqueResponse = new UserResponseDTO.Critique();
            critiqueResponse.setDescription(critique.getDescription());
            critiqueResponse.setRating(critique.getRating());
            critiqueResponse.setMedia(toMediaResponseDTO(critique.getMedia()));
            response.getCritiques().add(critiqueResponse);
        }

        return response;
    }

    public UserResponseDTO toUserResponseDTO(UserJPA userJPA) throws IllegalArgumentException {
        if (userJPA == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        UserResponseDTO response = new UserResponseDTO();
        response.setFirstName(userJPA.getFirstName());
        response.setLastName(userJPA.getLastName());
        response.setProfileName(userJPA.getProfileName());
        if (userJPA.getProfileImage() != null) {
            response.setProfileImageUrl(config.getUserImagesBaseUrl() + userJPA.getProfileImage());
        }
        response.setGender(userJPA.getGender());
        response.setRole(userJPA.getRole());

        UserResponseDTO.Country country = new UserResponseDTO.Country();
        country.setId(userJPA.getCountry().getId());
        country.setName(userJPA.getCountry().getName());
        country.setOfficialStateName(userJPA.getCountry().getOfficialStateName());
        country.setCode(userJPA.getCountry().getCode());
        response.setCountry(country);

        for (MediaJPA media : userJPA.getMedias()) {
            response.getMedias().add(toMediaResponseDTO(media));
        }
        for (CritiqueJPA critique : userJPA.getCritiques()) {
            UserResponseDTO.Critique critiqueResponse = new UserResponseDTO.Critique();
            critiqueResponse.setDescription(critique.getDescription());
            critiqueResponse.setRating(critique.getRating());
            critiqueResponse.setMedia(toMediaResponseDTO(critique.getId().getMedia()));
            response.getCritiques().add(critiqueResponse);
        }

        return response;
    }

//===================================================================================================================
    private MediaResponseDTO toMediaResponseDTO(MediaJDBC media) {
        MediaResponseDTO mediaResponse = new MediaResponseDTO();
        mediaResponse.setId(media.getId());
        mediaResponse.setTitle(media.getTitle());
        mediaResponse.setReleaseDate(media.getReleaseDate());
        mediaResponse.setDescription(media.getDescription());
        mediaResponse.setAudienceRating(media.getAudienceRating());
        mediaResponse.setCriticsRating(media.getCriticRating());
        if (media.getCoverImage() != null) {
            mediaResponse.setCoverImageUrl(config.getMediaImagesBaseUrl() + media.getCoverImage());
        }
        for (GenreJDBC genre : media.getGenres()) {
            mediaResponse.getGenres().add(new MediaResponseDTO.Genre(genre.getId(), genre.getName()));
        }
        if (media instanceof MovieJDBC) {
            mediaResponse.setMediaType(MediaType.MOVIE);
        } else if (media instanceof TVShowJDBC) {
            mediaResponse.setMediaType(MediaType.TV_SHOW);
        } else {
            throw new IllegalArgumentException("Unknown media type!");
        }
        return mediaResponse;
    }

    private MediaResponseDTO toMediaResponseDTO(MediaJPA media) {
        MediaResponseDTO mediaResponse = new MediaResponseDTO();
        mediaResponse.setId(media.getId());
        mediaResponse.setTitle(media.getTitle());
        mediaResponse.setReleaseDate(media.getReleaseDate());
        mediaResponse.setDescription(media.getDescription());
        mediaResponse.setAudienceRating(media.getAudienceRating());
        mediaResponse.setCriticsRating(media.getCriticRating());
        if (media.getCoverImage() != null) {
            mediaResponse.setCoverImageUrl(config.getMediaImagesBaseUrl() + media.getCoverImage());
        }
        for (GenreJPA genre : media.getGenres()) {
            mediaResponse.getGenres().add(new MediaResponseDTO.Genre(genre.getId(), genre.getName()));
        }
        if (media instanceof MovieJPA) {
            mediaResponse.setMediaType(MediaType.MOVIE);
        } else if (media instanceof TVShowJPA) {
            mediaResponse.setMediaType(MediaType.TV_SHOW);
        } else {
            throw new IllegalArgumentException("Unknown media type!");
        }
        return mediaResponse;
    }

}
