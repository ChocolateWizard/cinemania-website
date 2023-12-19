/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.tv;

import com.borak.kinweb.backend.domain.dto.DTO;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface ITVShowService<D extends DTO> {

    public ResponseEntity getAllTVShowsWithGenresPaginated(int page, int size);

    public ResponseEntity getAllTVShowsWithGenresPopularPaginated(int page, int size);

    public ResponseEntity getAllTVShowsWithGenresCurrentPaginated(int page, int size);

    public ResponseEntity getAllTVShowsWithDetailsPaginated(int page, int size);

    public ResponseEntity getTVShowWithGenres(Long id);

    public ResponseEntity getAllTVShowsWithGenres();

    public ResponseEntity getAllTVShowsWithDetails();

    public ResponseEntity getTVShowWithDetails(Long id);

    public ResponseEntity getTVShowDirectors(Long id);

    public ResponseEntity getTVShowWriters(Long id);

    public ResponseEntity getTVShowActors(Long id);

    public ResponseEntity getTVShowActorsWithRoles(Long id);

    public ResponseEntity deleteTVShowById(long id);

    public ResponseEntity postTVShow(D tvShow);

    public ResponseEntity putTVShow(D tvShow);

}
