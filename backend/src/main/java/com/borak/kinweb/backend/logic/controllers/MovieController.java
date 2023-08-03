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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "movies")
public class MovieController {

    @Autowired
    private final IMovieService movieService;

    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }
    
    

    //=========================GET MAPPINGS==================================
    //Returns: all movies
    //Included: Genres
    //Excluded: Directors, Writers, Actors, Acting roles
    @GetMapping
    public List<MovieDTO> getAllMovies() {
        return movieService.getAllMovies();
    }

    //Returns: all movies
    //Included: Genres,Directors, Writers, Actors, Acting roles
    //Excluded:
    @GetMapping(path = "/details")
    public List<MovieDTO> getAllMoviesWithDetails() {
        return movieService.getAllMoviesWithDetails();
    }

    //Returns: specific movie with given id
    //Included: Genres
    //Excluded: Directors, Writers, Actors, Acting roles
    @GetMapping(path = "/{id}")
    public List<MovieDTO> getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    //Returns: specific movie with given id
    //Included: Genres,Directors, Writers, Actors, Acting roles
    //Excluded:
    @GetMapping(path = "/{id}/details")
    public List<MovieDTO> getMovieWithDetails(@PathVariable Long id) {
        return movieService.getMovieWithDetails(id);
    }

    //Returns: directors of specific movie with given id
    //Included:
    //Excluded:
    @GetMapping(path = "/{id}/directors")
    public List<DirectorDTO> getMovieDirectors(@PathVariable Long id) {
        return movieService.getMovieDirectors(id);
    }

    //Returns: writers of specific movie with given id
    //Included:
    //Excluded:
    @GetMapping(path = "/{id}/writers")
    public List<WriterDTO> getMovieWriters(@PathVariable Long id) {
        return movieService.getMovieWriters(id);
    }

    //Returns: actors of specific movie with given id
    //Included:
    //Excluded:  Acting roles
    @GetMapping(path = "/{id}/actors")
    public List<ActorDTO> getMovieActors(@PathVariable Long id) {
        return movieService.getMovieActors(id);
    }

    //Returns: actors of specific movie with given id
    //Included: Acting roles
    //Excluded:
    @GetMapping(path = "/{id}/actors/roles")
    public List<ActingDTO> getMovieActorsWithRoles(@PathVariable Long id) {
        return movieService.getMovieActorsWithRoles(id);
    }

    //=========================POST MAPPINGS==================================
    //=========================PUT MAPPINGS===================================
    //=========================DELETE MAPPINGS================================
}
