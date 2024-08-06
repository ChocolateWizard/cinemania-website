/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.movie;

import com.borak.cwb.backend.domain.dto.DTO;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 * @param <D> movie request object
 */
public interface IMovieService<D extends DTO> {

    //GET
    ResponseEntity getAllMoviesWithGenresPaginated(int page, int size);

    ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size);

    ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size);

    ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size);

    ResponseEntity getMovieWithGenres(long id);

    ResponseEntity getMovieWithDetails(long id);

    //POST
    ResponseEntity postMovie(D movie);

    //PUT
    ResponseEntity putMovie(D movie);

    //DELETE
    ResponseEntity deleteMovieById(long id);

}
