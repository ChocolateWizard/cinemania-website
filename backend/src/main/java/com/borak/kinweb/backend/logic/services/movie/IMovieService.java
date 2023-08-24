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

    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenres();

    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresPaginated(int page, int size);

    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresPopularPaginated(int page, int size);

    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresCurrentPaginated(int page, int size);

    public ResponseEntity<List<MovieDTO>> getAllMoviesWithDetails();

    public ResponseEntity<MovieDTO> getMovieWithGenres(Long id);

    public ResponseEntity<MovieDTO> getMovieWithDetails(Long id);

    public ResponseEntity<List<DirectorDTO>> getMovieDirectors(Long id);

    public ResponseEntity<List<WriterDTO>> getMovieWriters(Long id);

    public ResponseEntity<List<ActorDTO>> getMovieActors(Long id);

    public ResponseEntity<List<ActingDTO>> getMovieActorsWithRoles(Long id);
    
    public ResponseEntity deleteMovieById(long id);
    

}
