/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActorDTO;
import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
public interface IMovieService {

    //Returns: all movies
    //Included: Genres
    //Excluded: Directors, Writers, Actors, Acting roles
    public List<MovieDTO> getAllMovies();

    //Returns: all movies
    //Included: Genres,Directors, Writers, Actors, Acting roles
    //Excluded:
    public List<MovieDTO> getAllMoviesWithDetails();

    //Returns: specific movie with given id
    //Included: Genres
    //Excluded: Directors, Writers, Actors, Acting roles
    public List<MovieDTO> getMovie(Long id);

    //Returns: specific movie with given id
    //Included: Genres,Directors, Writers, Actors, Acting roles
    //Excluded:
    public List<MovieDTO> getMovieWithDetails(Long id);

    //Returns: directors of specific movie with given id
    //Included:
    //Excluded:
    public List<DirectorDTO> getMovieDirectors(Long id);

    //Returns: writers of specific movie with given id
    //Included:
    //Excluded:
    public List<WriterDTO> getMovieWriters(Long id);

    //Returns: actors of specific movie with given id
    //Included:
    //Excluded:  Acting roles
    public List<ActorDTO> getMovieActors(Long id);

    //Returns: actors of specific movie with given id
    //Included: Acting roles
    //Excluded:
    public List<ActingDTO> getMovieActorsWithRoles(Long id);

}
