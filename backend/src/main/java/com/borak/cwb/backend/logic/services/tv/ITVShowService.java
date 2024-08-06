/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.tv;

import com.borak.cwb.backend.domain.dto.DTO;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 * @param <D> tv show request object
 */
public interface ITVShowService<D extends DTO> {

    //GET
    ResponseEntity getAllTVShowsWithGenresPaginated(int page, int size);

    ResponseEntity getAllTVShowsWithGenresPopularPaginated(int page, int size);

    ResponseEntity getAllTVShowsWithGenresCurrentPaginated(int page, int size);

    ResponseEntity getAllTVShowsWithDetailsPaginated(int page, int size);

    ResponseEntity getTVShowWithGenres(long id);

    ResponseEntity getTVShowWithDetails(long id);

    //POST
    ResponseEntity postTVShow(D tvShow);

    //PUT
    ResponseEntity putTVShow(D tvShow);

    //DELETE
    ResponseEntity deleteTVShowById(long id);

}
