/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActorDTO;
import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.borak.kinweb.backend.logic.services.movie.IMovieService;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "movies")
@Validated
public class MovieController {

    @Autowired
    private final IMovieService movieService;

    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }

    //=========================GET MAPPINGS==================================  
    @GetMapping
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity<List<MovieDTO>> getMovies(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithGenresPaginated(page, size);
    }

    @GetMapping(path = "/popular")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity<List<MovieDTO>> getMoviesPopular(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithGenresPopularPaginated(page, size);
    }

    @GetMapping(path = "/current")
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity<List<MovieDTO>> getMoviesCurrent(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return movieService.getAllMoviesWithGenresCurrentPaginated(page, size);
    }

    @GetMapping(path = "/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity<List<MovieDTO>> getMoviesDetails() {
        return movieService.getAllMoviesWithDetails();
    }

    @GetMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Medium.class)
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieWithGenres(id);
    }

    @GetMapping(path = "/{id}/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity<MovieDTO> getMovieByIdDetails(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieWithDetails(id);
    }

    @GetMapping(path = "/{id}/directors")
    public ResponseEntity<List<DirectorDTO>> getMovieByIdDirectors(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieDirectors(id);
    }

    @GetMapping(path = "/{id}/writers")
    public ResponseEntity<List<WriterDTO>> getMovieByIdWriters(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieWriters(id);
    }

    @GetMapping(path = "/{id}/actors")
    public ResponseEntity<List<ActorDTO>> getMovieByIdActors(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieActors(id);
    }

    @GetMapping(path = "/{id}/actors/roles")
    public ResponseEntity<List<ActingDTO>> getMovieByIdActorsWithRoles(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.getMovieActorsWithRoles(id);
    }

    //=========================POST MAPPINGS==================================
    //=========================PUT MAPPINGS===================================
    //=========================DELETE MAPPINGS================================
    
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMovieById(@PathVariable @Min(value = 1, message = "Movie id must be greater than or equal to 1") long id) {
        return movieService.deleteMovieById(id);
    }
    
    
}
