/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.validation;

import com.borak.kinweb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.kinweb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.kinweb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.exceptions.ValidationException;
import com.borak.kinweb.backend.logic.util.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
@Service
public class DomainValidationService {

    @Autowired
    private Util util;

    public void validate(MovieRequestDTO movie, MultipartFile coverImage, RequestMethod requestType) throws ValidationException {
        if (movie == null) {
            throw new ValidationException("Movie must not be null!");
        }
        List<String> messages = new LinkedList<>();
        switch (requestType) {
            case PUT:
                if (movie.getId() == null) {
                    messages.add("Movie id must not be null!");
                } else if (movie.getId() <= 0) {
                    messages.add("Movie id must be greater than or equal to 1!");
                }
                break;
            case POST:
                break;
            default:
                throw new IllegalArgumentException("Unsupported requestType!");
        }
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            messages.add("Movie title must not be null or empty!");
        } else if (movie.getTitle().length() > 300) {
            messages.add("Movie title must have less than 300 characters!");
        }
        if (movie.getReleaseDate() == null) {
            messages.add("Movie release date must not be null!");
        }
        if (coverImage != null) {
            try {
                new MyImage(coverImage);
            } catch (IllegalArgumentException e) {
                messages.add("Movie cover image - " + e.getMessage());
            }
        }
        if (movie.getDescription() == null || movie.getDescription().isBlank()) {
            messages.add("Movie description must not be null or empty!");
        } else if (movie.getDescription().length() > 1000) {
            messages.add("Movie description must have less than 1000 characters!");
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
            if (util.duplicatesExistIgnoreNullAndNonNatural(movie.getGenres())) {
                messages.add("Genres field must not contain duplicate id values!");
            }
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
            if (util.duplicatesExistIgnoreNullAndNonNatural(movie.getDirectors())) {
                messages.add("Directors field must not contain duplicate id values!");
            }
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
            if (util.duplicatesExistIgnoreNullAndNonNatural(movie.getWriters())) {
                messages.add("Writers field must not contain duplicate id values!");
            }
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
            if (util.duplicatesExistIgnoreNullAndNonNatural(movie.getActors().stream().map((actor) -> {
                return (actor == null) ? null : actor.getId();
            }).collect(Collectors.toList()))) {
                messages.add("Actors field must not contain duplicate id values!");
            }
            int i = 1;
            for (MovieRequestDTO.Actor actor : movie.getActors()) {
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
                        messages.add("Invalid actor roles field at actor number: " + i + ". Actor roles field must not be null or empty!");
                    } else {
                        int j = 1;
                        for (String role : actor.getRoles()) {
                            if (role == null || role.isBlank()) {
                                messages.add("Invalid name of role number: " + j + " at actor number: " + i + ". Actor role name must not be null or empty!");
                            } else if (role.length() > 300) {
                                messages.add("Invalid name of role number: " + j + " at actor number: " + i + ". Actor role name must have less than 300 characters!");
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

    public void validate(TVShowRequestDTO tvShow, MultipartFile coverImage, RequestMethod requestType) throws ValidationException {
        if (tvShow == null) {
            throw new ValidationException("TV show must not be null!");
        }
        List<String> messages = new LinkedList<>();
        switch (requestType) {
            case PUT:
                if (tvShow.getId() == null) {
                    messages.add("TV show id must not be null!");
                } else if (tvShow.getId() <= 0) {
                    messages.add("TV show id must be greater than or equal to 1!");
                }
                break;
            case POST:
                break;
            default:
                throw new IllegalArgumentException("Unsupported requestType!");
        }
        if (tvShow.getTitle() == null || tvShow.getTitle().isBlank()) {
            messages.add("TV show title must not be null or empty!");
        } else if (tvShow.getTitle().length() > 300) {
            messages.add("TV show title must have less than 300 characters!");
        }
        if (tvShow.getReleaseDate() == null) {
            messages.add("TV show release date must not be null!");
        }
        if (coverImage != null) {
            try {
                new MyImage(coverImage);
            } catch (IllegalArgumentException e) {
                messages.add("TV show cover image - " + e.getMessage());
            }
        }
        if (tvShow.getDescription() == null || tvShow.getDescription().isBlank()) {
            messages.add("TV show description must not be null or empty!");
        } else if (tvShow.getDescription().length() > 1000) {
            messages.add("TV show description must have less than 1000 characters!");
        }
        if (tvShow.getAudienceRating() == null) {
            messages.add("TV show audience rating must not be null!");
        } else if (tvShow.getAudienceRating() < 0 || tvShow.getAudienceRating() > 100) {
            messages.add("TV show audience rating must be between 0 and 100!");
        }
        if (tvShow.getNumberOfSeasons() == null) {
            messages.add("TV show number of seasons must not be null!");
        } else if (tvShow.getNumberOfSeasons() <= 0) {
            messages.add("TV show number of seasons must be greater than 0!");
        }
        if (tvShow.getGenres() == null || tvShow.getGenres().isEmpty()) {
            messages.add("Genres field must not be null or empty!");
        } else {
            if (util.duplicatesExistIgnoreNullAndNonNatural(tvShow.getGenres())) {
                messages.add("Genres field must not contain duplicate id values!");
            }
            int i = 1;
            for (Long genre : tvShow.getGenres()) {
                if (genre == null) {
                    messages.add("Invalid genre id at genre number: " + i + ". Genre id must not be null!");
                } else if (genre <= 0) {
                    messages.add("Invalid genre id at genre number: " + i + ". Genre id must be greater than 0!");
                }
                i++;
            }
        }
        if (tvShow.getDirectors() == null || tvShow.getDirectors().isEmpty()) {
            messages.add("Directors field must not be null or empty!");
        } else {
            if (util.duplicatesExistIgnoreNullAndNonNatural(tvShow.getDirectors())) {
                messages.add("Directors field must not contain duplicate id values!");
            }
            int i = 1;
            for (Long director : tvShow.getDirectors()) {
                if (director == null) {
                    messages.add("Invalid director id at director number: " + i + ". Director id must not be null!");
                } else if (director <= 0) {
                    messages.add("Invalid director id at director number: " + i + ". Director id must be greater than 0!");
                }
                i++;
            }
        }
        if (tvShow.getWriters() == null || tvShow.getWriters().isEmpty()) {
            messages.add("Writers field must not be null or empty!");
        } else {
            if (util.duplicatesExistIgnoreNullAndNonNatural(tvShow.getWriters())) {
                messages.add("Writers field must not contain duplicate id values!");
            }
            int i = 1;
            for (Long writer : tvShow.getWriters()) {
                if (writer == null) {
                    messages.add("Invalid writer id at writer number: " + i + ". Writer id must not be null!");
                } else if (writer <= 0) {
                    messages.add("Invalid writer id at writer number: " + i + ". Writer id must be greater than 0!");
                }
                i++;
            }
        }
        if (tvShow.getActors() == null || tvShow.getActors().isEmpty()) {
            messages.add("Actors field must not be null or empty!");
        } else {
            if (util.duplicatesExistIgnoreNullAndNonNatural(tvShow.getActors().stream().map((actor) -> {
                return (actor == null) ? null : actor.getId();
            }).collect(Collectors.toList()))) {
                messages.add("Actors field must not contain duplicate id values!");
            }
            int i = 1;
            for (TVShowRequestDTO.Actor actor : tvShow.getActors()) {
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
                        messages.add("Invalid actor roles field at actor number: " + i + ". Actor roles field must not be null or empty!");
                    } else {
                        int j = 1;
                        for (String role : actor.getRoles()) {
                            if (role == null || role.isBlank()) {
                                messages.add("Invalid name of role number: " + j + " at actor number: " + i + ". Actor role name must not be null or empty!");
                            } else if (role.length() > 300) {
                                messages.add("Invalid name of role number: " + j + " at actor number: " + i + ". Actor role name must have less than 300 characters!");
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

    public void validate(PersonRequestDTO person, MultipartFile profilePhoto, RequestMethod requestType) throws ValidationException {
        if (person == null) {
            throw new ValidationException("Person must not be null!");
        }
        List<String> messages = new LinkedList<>();
        switch (requestType) {
            case PUT:
                if (person.getId() == null) {
                    messages.add("Person id must not be null!");
                } else if (person.getId() <= 0) {
                    messages.add("Person id must be greater than or equal to 1!");
                }
                break;
            case POST:
                break;
            default:
                throw new IllegalArgumentException("Unsupported requestType!");
        }
        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            messages.add("Person first name must not be null or empty!");
        } else if (person.getFirstName().length() > 100) {
            messages.add("Person first name must have less than 100 characters!");
        }
        if (person.getLastName() == null || person.getLastName().isBlank()) {
            messages.add("Person last name must not be null or empty!");
        } else if (person.getLastName().length() > 100) {
            messages.add("Person last name must have less than 100 characters!");
        }
        if (person.getGender() == null) {
            messages.add("Person gender must not be null or empty!");
        }
        if (profilePhoto != null) {
            try {
                new MyImage(profilePhoto);
            } catch (IllegalArgumentException e) {
                messages.add("Person profile photo - " + e.getMessage());
            }
        }
        if (!person.getProfessions().isEmpty()) {
            int numberOfDirectors = 0;
            int numberOfWriters = 0;
            int numberOfActors = 0;
            for (int i = 0; i < person.getProfessions().size(); i++) {
                if (person.getProfessions().get(i) == null) {
                    messages.add("Invalid profession at profession number: " + (i + 1) + ". Profession must not be null!");
                } else if (person.getProfessions().get(i) instanceof PersonRequestDTO.Director director) {
                    numberOfDirectors++;
                    if (!director.getWorkedOn().isEmpty()) {
                        if (util.duplicatesExistIgnoreNullAndNonNatural(director.getWorkedOn())) {
                            messages.add("Persons director profession worked on medias field must not contain duplicate media id values!");
                        }
                        int mI = 1;
                        for (Long mediaId : director.getWorkedOn()) {
                            if (mediaId == null) {
                                messages.add("Invalid persons director profession! Worked on media id at media number: " + mI + " must not be null!");
                            } else if (mediaId <= 0) {
                                messages.add("Invalid persons director profession! Worked on media id at media number: " + mI + " must be greater than 0!");
                            }
                            mI++;
                        }
                    }
                } else if (person.getProfessions().get(i) instanceof PersonRequestDTO.Writer writer) {
                    numberOfWriters++;
                    if (!writer.getWorkedOn().isEmpty()) {
                        if (util.duplicatesExistIgnoreNullAndNonNatural(writer.getWorkedOn())) {
                            messages.add("Persons writer profession worked on medias field must not contain duplicate media id values!");
                        }
                        int mI = 1;
                        for (Long mediaId : writer.getWorkedOn()) {
                            if (mediaId == null) {
                                messages.add("Invalid persons writer profession! Worked on media id at media number: " + mI + " must not be null!");
                            } else if (mediaId <= 0) {
                                messages.add("Invalid persons writer profession! Worked on media id at media number: " + mI + " must be greater than 0!");
                            }
                            mI++;
                        }
                    }
                } else if (person.getProfessions().get(i) instanceof PersonRequestDTO.Actor actor) {
                    numberOfActors++;
                    if (actor.isStar() == null) {
                        messages.add("Persons actor profession star status must not be null!");
                    }
                    if (!actor.getWorkedOn().isEmpty()) {
                        if (util.duplicatesExistIgnoreNullAndNonNatural(actor.getWorkedOn().stream().map((acting) -> {
                            return (acting == null) ? null : acting.getMediaId();
                        }).collect(Collectors.toList()))) {
                            messages.add("Persons actor profession worked on medias field must not contain duplicate media id values!");
                        }
                        int mI = 1;
                        for (PersonRequestDTO.Actor.Acting mediInfo : actor.getWorkedOn()) {
                            if (mediInfo == null) {
                                messages.add("Invalid persons actor profession! Worked on media info at media number: " + mI + " must not be null!");
                            } else {
                                if (mediInfo.getMediaId() == null) {
                                    messages.add("Invalid persons actor profession! Worked on media id at media info number: " + mI + " must not be null!");
                                } else if (mediInfo.getMediaId() <= 0) {
                                    messages.add("Invalid persons actor profession! Worked on media id at media info number: " + mI + " must be greater than 0!");
                                }
                                if (mediInfo.isStarring() == null) {
                                    messages.add("Invalid persons actor profession! Worked on starring status at media info number: " + mI + " must not be null!");
                                }
                                if (mediInfo.getRoles().isEmpty()) {
                                    messages.add("Invalid persons actor profession! Worked on roles at media info number: " + mI + " must not be null or empty!");
                                } else {
                                    int rI = 1;
                                    for (String role : mediInfo.getRoles()) {
                                        if (role == null || role.isBlank()) {
                                            messages.add("Invalid persons actor profession! Worked on roles name number:" + rI + " at media info number: " + mI + " must not be null or empty!");
                                        } else if (role.length() > 300) {
                                            messages.add("Invalid persons actor profession! Worked on roles name number:" + rI + " at media info number: " + mI + " must have less than 300 characters!");
                                        }
                                        rI++;
                                    }
                                }
                            }
                            mI++;
                        }
                    }
                } else {
                    throw new ValidationException("Unexpected exception. Profession not supported!");
                }
            }
            if (numberOfDirectors > 1) {
                throw new ValidationException("Duplicate director profession! Only one profession info per type can be specified!");
            }
            if (numberOfWriters > 1) {
                throw new ValidationException("Duplicate writer profession! Only one profession info per type can be specified!");
            }
            if (numberOfActors > 1) {
                throw new ValidationException("Duplicate actor profession! Only one profession info per type can be specified!");
            }
        }
        if (!messages.isEmpty()) {
            throw new ValidationException(messages.toArray(String[]::new));
        }

    }

    public void validate(UserRegisterDTO registerForm, MultipartFile coverImage) throws ValidationException {
        if (registerForm == null) {
            throw new ValidationException("Invalid user info!");
        }
        if (registerForm.getProfileName().contains(" ")) {
            throw new ValidationException("Profile name must not contain blank spaces!");
        }
        if (registerForm.getProfileName().equals("default")) {
            throw new ValidationException("Profile name is already reserved!");
        }
        //ADMINISTRATOR roles should be given manually, not by client choise
        if (registerForm.getRole() == UserRole.ADMINISTRATOR) {
            throw new ValidationException("Special permision is needed for an ADMINISTRATOR role!");
        }
        if (coverImage != null) {
            try {
                new MyImage(coverImage);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("User profile image - " + e.getMessage());
            }
        }

    }

    public void validate(CritiqueRequestDTO critique) throws ValidationException {
        if (critique == null) {
            throw new ValidationException("Invalid critique info!");
        }
        List<String> messages = new LinkedList<>();
        if (critique.getRating() == null) {
            messages.add("Critique rating must not be null!");
        } else if (critique.getRating() < 0 || critique.getRating() > 100) {
            messages.add("Critique rating must be between 0 and 100!");
        }
        if (critique.getDescription() == null || critique.getDescription().isBlank()) {
            messages.add("Critique description must not be null or empty!");
        } else if (critique.getDescription().length() > 500) {
            messages.add("Critique description must have less than 500 characters!");
        }
        if (!messages.isEmpty()) {
            throw new ValidationException(messages.toArray(String[]::new));
        }
    }

//---------------------------------------------------------------------------------------------------------
}
