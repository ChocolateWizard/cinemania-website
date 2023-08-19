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
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface IMovieService {

    /**
     * Get main information of every movie with their respective genres.
     *
     * @return <div>List of movies with main attributes and list of genres. If
     * no movie present, returns an empty list.
     * <ul>
     * <li>Included: Genres</li>
     * <li>Excluded: Directors, Writers, Actors, Acting roles, Critiques</li>
     * </ul>
     * </div>
     */
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenres();
    
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresPaginated(int page,int size);
    
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresPopularPaginated(int page,int size);
    
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresCurrentPaginated(int page,int size);

    /**
     * Get every information of every movie.
     *
     * @return <div>List of movies with all of their relationships. Critic in
     * Critiques will only contain username as its attribute. If no movie
     * present, returns an empty list.
     * <ul>
     * <li>Included: Genres,Directors, Writers, Actors, Acting roles,
     * Critiques</li>
     * <li>Excluded: Every other Critic information except username</li>
     * </ul>
     * </div>
     */
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithDetails();

    /**
     * Get main information of a specific movie with its respective genres.
     *
     * @return <div>Specific movie with given id. If no movie present, returns
     * {@literal null}
     * <ul>
     * <li>Included: Genres</li>
     * <li>Excluded: Directors, Writers, Actors, Acting roles,Critiques</li>
     * </ul>
     * </div>
     */
    public ResponseEntity<MovieDTO> getMovieWithGenres(Long id);

    /**
     * Get every information of a specific movie.
     *
     * @return <div>Movie with all of its relationships. Critic in Critiques
     * will only contain username as its attribute. If no movie present, returns
     * {@literal null}
     * <ul>
     * <li>Included: Genres,Directors, Writers, Actors, Acting roles,
     * Critiques</li>
     * <li>Excluded: Every other Critic information except username</li>
     * </ul>
     * </div>
     */
    public ResponseEntity<MovieDTO> getMovieWithDetails(Long id);

    /**
     * Directors of a specific movie.
     *
     * @return List of directors of a movie with given ID. If no director
     * present, returns empty list.
     */
    public ResponseEntity<List<DirectorDTO>> getMovieDirectors(Long id);

    /**
     * Writers of a specific movie.
     *
     * @return List of writers of a movie with given ID. If no writer present,
     * returns empty list.
     */
    public ResponseEntity<List<WriterDTO>> getMovieWriters(Long id);

    /**
     * Actors of a specific movie.
     *
     * @return List of actors of a movie with given ID. If no actor present,
     * returns empty list.
     */
    public ResponseEntity<List<ActorDTO>> getMovieActors(Long id);

    /**
     * Actors and their respective roles in a specific movie.
     *
     * @return <div>List of actors of a movie with given ID. If no acting
     * present, returns empty list.
     * <ul>
     * <li>Included: Actors, Acting roles</li>
     * </ul>
     * </div>
     */
    public ResponseEntity<List<ActingDTO>> getMovieActorsWithRoles(Long id);

}
