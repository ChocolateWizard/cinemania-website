/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.domain.dto.DTO;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface IMovieService<D extends DTO> {

    public ResponseEntity getAllMoviesWithGenresPaginated(int page, int size);

    public ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size);

    public ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size);

    public ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size);
    
    public ResponseEntity getMovieWithGenres(Long id);

    public ResponseEntity getAllMoviesWithGenres();

    public ResponseEntity getAllMoviesWithDetails(); 

    public ResponseEntity getMovieWithDetails(Long id);

    public ResponseEntity getMovieDirectors(Long id);

    public ResponseEntity getMovieWriters(Long id);

    public ResponseEntity getMovieActors(Long id);

    public ResponseEntity getMovieActorsWithRoles(Long id);

    public ResponseEntity deleteMovieById(long id);

    public ResponseEntity postMovie(D movie);

}
