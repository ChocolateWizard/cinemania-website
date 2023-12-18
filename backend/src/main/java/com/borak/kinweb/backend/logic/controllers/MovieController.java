/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.logic.services.movie.IMovieService;
import com.borak.kinweb.backend.logic.services.validation.DomainValidationService;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "api/movies")
@Validated
public class MovieController {

    @Autowired
    private final IMovieService movieService;

    @Autowired
    private DomainValidationService domainValidator;

//    @Autowired
//    ConfigProperties properties;
    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }

    //=========================GET MAPPINGS==================================  
    @GetMapping
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getMovies(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithGenresPaginated(page, size);
    }

    @GetMapping(path = "/popular")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getMoviesPopular(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithGenresPopularPaginated(page, size);
    }

    @GetMapping(path = "/current")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getMoviesCurrent(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithGenresCurrentPaginated(page, size);
    }

    @GetMapping(path = "/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getMoviesDetails(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithDetailsPaginated(page, size);
    }

    @GetMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Medium.class)
    public ResponseEntity getMovieById(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieWithGenres(id);
    }

    @GetMapping(path = "/{id}/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getMovieByIdDetails(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieWithDetails(id);
    }

    @GetMapping(path = "/{id}/directors")
    public ResponseEntity getMovieByIdDirectors(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieDirectors(id);
    }

    @GetMapping(path = "/{id}/writers")
    public ResponseEntity getMovieByIdWriters(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieWriters(id);
    }

    @GetMapping(path = "/{id}/actors")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getMovieByIdActors(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieActors(id);
    }

    @GetMapping(path = "/{id}/actors/roles")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getMovieByIdActorsWithRoles(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieActorsWithRoles(id);
    }

    //=========================POST MAPPINGS==================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity postMovie(@RequestPart("movie") MovieRequestDTO movie, @RequestPart(name = "cover_image", required = false) MultipartFile coverImage) {
        domainValidator.validate(movie, coverImage, RequestMethod.POST);
        if (coverImage != null) {
            movie.setCoverImage(new MyImage(coverImage));
        }
        return movieService.postMovie(movie);
    }

    //=========================PUT MAPPINGS===================================
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity putMovie(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id, @RequestPart("movie") MovieRequestDTO movie, @RequestPart(name = "cover_image", required = false) MultipartFile coverImage) {
        movie.setId(id);
        domainValidator.validate(movie, coverImage, RequestMethod.PUT);
        if (coverImage != null) {
            movie.setCoverImage(new MyImage(coverImage));
        }
        return movieService.putMovie(movie);
    }

    //=========================DELETE MAPPINGS================================
    @DeleteMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity deleteMovieById(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.deleteMovieById(id);
    }
//=========================TESTING=============================================

}
