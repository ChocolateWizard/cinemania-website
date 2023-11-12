/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.validation;

import com.borak.kinweb.backend.domain.dto.classes.MoviePOSTRequestDTO;
import com.borak.kinweb.backend.exceptions.ValidationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mr. Poyo
 */
@Service
public class DomainValidationService {

    private static final String[] validImageExtentions = {"png", "jpg", "jpeg"};
    private static final long IMAGE_MAX_SIZE = 8388608L;

    public void validatePOST(MoviePOSTRequestDTO movie) {
        if (movie == null) {
            throw new ValidationException("Movie must not be null!");
        }
        List<String> messages = new LinkedList<>();
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            messages.add("Movie title must not be null or empty!");
        } else if (movie.getTitle().length() > 300) {
            messages.add("Movie title must have less than 300 characters!");
        }
        if (movie.getReleaseDate() == null) {
            messages.add("Movie release date must not be null!");
        }
        if (movie.getCoverImage() != null) {
            String extension = FilenameUtils.getExtension(movie.getCoverImage().getOriginalFilename());
            if (!Arrays.asList(validImageExtentions).contains(extension)) {
                messages.add("Movie cover image invalid type! Valid types are: " + Arrays.toString(validImageExtentions));
            }
            if (movie.getCoverImage().getSize() > IMAGE_MAX_SIZE) {
                messages.add("Movie cover image size too big! Max size is: " + IMAGE_MAX_SIZE + " bytes");
            }
        }
        if (movie.getDescription() == null || movie.getDescription().isBlank()) {
            messages.add("Movie description must not be null or empty!");
        } else if (movie.getDescription().length() > 500) {
            messages.add("Movie description must have less than 500 characters!");
        }
        if (movie.getAudienceRating() == null) {
            messages.add("Movie audience rating must not be null!");
        } else if (movie.getAudienceRating() < 0 || movie.getAudienceRating() > 100) {
            messages.add("Movie audience rating must be between 0 and 100!");
        }
        if (movie.getLength() == null) {
            messages.add("Movie length must not be null!");
        } else if (movie.getLength() <= 0) {
            messages.add("Movie length must be greater than 0!");
        }
        if (movie.getGenres() == null || movie.getGenres().isEmpty()) {
            messages.add("Genres field must not be null or empty!");
        } else {
            int i = 1;
            for (Long genre : movie.getGenres()) {
                if (genre == null) {
                    messages.add("Invalid genre id at genre number: " + i + ". Genre id must not be null!");
                } else if (genre <= 0) {
                    messages.add("Invalid genre id at genre number: " + i + ". Genre id must be greater than 0!");
                }
                i++;
            }
        }
        if (movie.getDirectors() == null || movie.getDirectors().isEmpty()) {
            messages.add("Directors field must not be null or empty!");
        } else {
            int i = 1;
            for (Long director : movie.getDirectors()) {
                if (director == null) {
                    messages.add("Invalid director id at director number: " + i + ". Director id must not be null!");
                } else if (director <= 0) {
                    messages.add("Invalid director id at director number: " + i + ". Director id must be greater than 0!");
                }
                i++;
            }
        }
        if (movie.getWriters() == null || movie.getWriters().isEmpty()) {
            messages.add("Writers field must not be null or empty!");
        } else {
            int i = 1;
            for (Long writer : movie.getWriters()) {
                if (writer == null) {
                    messages.add("Invalid writer id at writer number: " + i + ". Writer id must not be null!");
                } else if (writer <= 0) {
                    messages.add("Invalid writer id at writer number: " + i + ". Writer id must be greater than 0!");
                }
                i++;
            }
        }
        if (movie.getActors() == null || movie.getActors().isEmpty()) {
            messages.add("Actors field must not be null or empty!");
        } else {
            int i = 1;
            for (MoviePOSTRequestDTO.Actor actor : movie.getActors()) {
                if (actor == null) {
                    messages.add("Invalid actor at actor number: " + i + ". Actor must not be null!");
                } else {
                    if (actor.getId() == null) {
                        messages.add("Invalid actor id at actor number: " + i + ". Actor id must not be null!");
                    } else if (actor.getId() <= 0) {
                        messages.add("Invalid actor id at actor number: " + i + ". Actor id must be greater than 0!");
                    }
                    if (actor.getStarring() == null) {
                        messages.add("Invalid actor starring value at actor number: " + i + ". Actor starring value must not be null!");
                    }
                    if (actor.getRoles() == null || actor.getRoles().isEmpty()) {
                        messages.add("Invalid actors roles field at actor number: " + i + ". Actors roles field must not be null or empty!");
                    } else {
                        int j = 1;
                        for (MoviePOSTRequestDTO.Actor.Role role : actor.getRoles()) {
                            if (role.getName() == null || role.getName().isBlank()) {
                                messages.add("Invalid name field of role number: " + j + " at actor number: " + i + ". Actors role name must not be null or empty!");
                            } else if (role.getName().length() > 300) {
                                messages.add("Invalid name field of role number: " + j + " at actor number: " + i + ". Actors role name must have less than 300 characters!");
                            }
                            j++;
                        }
                    }
                }
                i++;
            }
        }

        if (!messages.isEmpty()) {
            throw new ValidationException(messages.toArray(String[]::new));
        }

    }

}
