/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.domain.constants.Constants;
import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.ActorDTO;
import com.borak.kinweb.backend.domain.dto.classes.DirectorDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.WriterDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.InvalidInputException;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.logic.transformers.ActingTransformer;
import com.borak.kinweb.backend.logic.transformers.ActorTransformer;
import com.borak.kinweb.backend.logic.transformers.DirectorTransformer;
import com.borak.kinweb.backend.logic.transformers.MovieTransformer;
import com.borak.kinweb.backend.logic.transformers.WriterTransformer;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.borak.kinweb.backend.repository.api.IMovieRepository;
import com.borak.kinweb.backend.repository.util.FileRepository;
import com.fasterxml.jackson.annotation.JsonView;
import java.awt.Image;
import java.time.Year;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class MovieService implements IMovieService {

    @Autowired
    private IMovieRepository<MovieJDBC, GenreJDBC, DirectorJDBC, WriterJDBC, ActorJDBC, ActingJDBC, Long> movieRepo;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private MovieTransformer movieTransformer;
    @Autowired
    private DirectorTransformer directorTransformer;
    @Autowired
    private WriterTransformer writerTransformer;
    @Autowired
    private ActorTransformer actorTransformer;
    @Autowired
    private ActingTransformer actingTransformer;
//----------------------------------------------------------------------------------------------------

    //movies
    @Override
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenres() {
        List<MovieDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllRelationshipGenres());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    //movies/details
    @Override
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithDetails() {
        List<MovieDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAll());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    //movies/{id}
    @Override
    public ResponseEntity<MovieDTO> getMovieWithGenres(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdNoRelationships(id);
        if (movie.isPresent()) {
            List<GenreJDBC> genres = movieRepo.findByIdGenres(id);
            movie.get().setGenres(genres);
            return new ResponseEntity<>(movieTransformer.jdbcToDto(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);

    }

    //movies/{id}/details
    @Override
    public ResponseEntity<MovieDTO> getMovieWithDetails(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.jdbcToDto(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    //movies/{id}/directors
    @Override
    public ResponseEntity<List<DirectorDTO>> getMovieDirectors(Long id) {
        if (movieRepo.existsById(id)) {
            List<DirectorJDBC> directors = movieRepo.findByIdDirectors(id);
            return new ResponseEntity<>(directorTransformer.jdbcToDto(directors), HttpStatus.OK);

        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    //movies/{id}/writers
    @Override
    public ResponseEntity<List<WriterDTO>> getMovieWriters(Long id) {
        if (movieRepo.existsById(id)) {
            List<WriterJDBC> writers = movieRepo.findByIdWriters(id);
            return new ResponseEntity<>(writerTransformer.jdbcToDto(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    //movies/{id}/actors
    @Override
    public ResponseEntity<List<ActorDTO>> getMovieActors(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActorJDBC> actors = movieRepo.findByIdActors(id);
            return new ResponseEntity<>(actorTransformer.jdbcToDto(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    //movies/{id}/actors/roles
    @Override
    public ResponseEntity<List<ActingDTO>> getMovieActorsWithRoles(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActingJDBC> actings = movieRepo.findByIdActorsWithRoles(id);
            return new ResponseEntity<>(actingTransformer.jdbcToDto(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresPaginated(int page, int size) {
        List<MovieDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllRelationshipGenresPaginated(page - 1, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresPopularPaginated(int page, int size) {
        int rating = 80;
        List<MovieDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllByAudienceRatingRelationshipGenresPaginated(page - 1, size, rating));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<MovieDTO>> getAllMoviesWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue();
        List<MovieDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllByReleaseYearRelationshipGenresPaginated(page - 1, size, year));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteMovieById(long id) {
        if (movieRepo.existsById(id)) {
            Optional<String> cover = movieRepo.findByIdCoverImageUrl(id);
            movieRepo.deleteById(id);
            if (cover.isPresent() && fileRepo.doesFileExist(Constants.MEDIA_IMAGES_FOLDER_PATH + cover.get())) {
                fileRepo.deleteIfExists(Constants.MEDIA_IMAGES_FOLDER_PATH + cover.get());
            }
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

}
