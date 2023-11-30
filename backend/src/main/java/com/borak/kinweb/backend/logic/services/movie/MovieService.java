/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.domain.dto.movie.MoviePOSTRequestDTO;
import com.borak.kinweb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.exceptions.ValidationException;
import com.borak.kinweb.backend.logic.transformers.ActingTransformer;
import com.borak.kinweb.backend.logic.transformers.ActorTransformer;
import com.borak.kinweb.backend.logic.transformers.DirectorTransformer;
import com.borak.kinweb.backend.logic.transformers.MovieTransformer;
import com.borak.kinweb.backend.logic.transformers.WriterTransformer;
import com.borak.kinweb.backend.repository.api.IActingRepository;
import com.borak.kinweb.backend.repository.api.IActorRepository;
import com.borak.kinweb.backend.repository.api.IDirectorRepository;
import com.borak.kinweb.backend.repository.api.IGenreRepository;
import com.borak.kinweb.backend.repository.api.IMovieRepository;
import com.borak.kinweb.backend.repository.api.IWriterRepository;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.time.Year;
import java.util.LinkedList;

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
public class MovieService implements IMovieService<MoviePOSTRequestDTO> {

    private static final int POPULARITY_TRESHOLD = 80;

    @Autowired
    private IMovieRepository<MovieJDBC, Long> movieRepo;
    @Autowired
    private IGenreRepository<GenreJDBC, Long> genreRepo;
    @Autowired
    private IDirectorRepository<DirectorJDBC, Long> directorRepo;
    @Autowired
    private IWriterRepository<WriterJDBC, Long> writerRepo;
    @Autowired
    private IActorRepository<ActorJDBC, Long> actorRepo;
    @Autowired
    private IActingRepository<ActingJDBC, Long> actingRepo;
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

    @Override
    public ResponseEntity getAllMoviesWithGenresPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllWithGenresPaginated(page - 1, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllByAudienceRatingWithGenresPaginated(page - 1, size, POPULARITY_TRESHOLD));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue();
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllByReleaseYearWithGenresPaginated(page - 1, size, year));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllWithRelationsPaginated(page - 1, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getMovieWithGenres(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdWithGenres(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.toMovieResponseDTO(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWithDetails(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdWithRelations(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.toMovieResponseDTO(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenres() {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllWithGenres());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetails() {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllWithRelations());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getMovieDirectors(Long id) {
        if (movieRepo.existsById(id)) {
            List<DirectorJDBC> directors = directorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(directorTransformer.toMovieDirectorResponseDTO(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWriters(Long id) {
        if (movieRepo.existsById(id)) {
            List<WriterJDBC> writers = writerRepo.findAllByMediaId(id);
            return new ResponseEntity<>(writerTransformer.toMovieWriterResponseDTO(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActors(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActorJDBC> actors = actorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actorTransformer.toMovieActorResponseDTO(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActorsWithRoles(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActingJDBC> actings = actingRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actingTransformer.toMovieActorResponseDTO(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity deleteMovieById(long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdWithRelations(id);
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("No movie found with id: " + id);
        }
        movieRepo.deleteById(id);
        if (movie.get().getCoverImage() != null && !movie.get().getCoverImage().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(movie.get().getCoverImage());
        }
        return new ResponseEntity(movieTransformer.toMovieResponseDTO(movie.get()), HttpStatus.OK);

    }

    @Override
    public ResponseEntity postMovie(MoviePOSTRequestDTO movieClient) {
        MovieJDBC movieJDBC = movieTransformer.toMovieJDBC(movieClient);
        List<String> errorMessages = new LinkedList<>();
        for (GenreJDBC genre : movieJDBC.getGenres()) {
            if (!genreRepo.existsById(genre.getId())) {
                errorMessages.add("Genre with id: " + genre.getId() + " does not exist in database!");
            }
        }
        for (DirectorJDBC director : movieJDBC.getDirectors()) {
            if (!directorRepo.existsById(director.getId())) {
                errorMessages.add("Director with id: " + director.getId() + " does not exist in database!");
            }
        }
        for (WriterJDBC writer : movieJDBC.getWriters()) {
            if (!writerRepo.existsById(writer.getId())) {
                errorMessages.add("Writer with id: " + writer.getId() + " does not exist in database!");
            }
        }
        for (ActingJDBC acting : movieJDBC.getActings()) {
            if (!actorRepo.existsById(acting.getActor().getId())) {
                errorMessages.add("Actor with id: " + acting.getActor().getId() + " does not exist in database!");
            }
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages.toArray(String[]::new));
        }
        movieJDBC.setCoverImage(null);
        MovieJDBC movieDB = movieRepo.insert(movieJDBC);
        if (movieClient.getCoverImage() != null) {
            movieClient.getCoverImage().setName("" + movieDB.getId());
            movieRepo.updateCoverImage(movieDB.getId(), movieClient.getCoverImage().getFullName());
            fileRepo.saveMediaCoverImage(movieClient.getCoverImage());
        }

        return new ResponseEntity<>(movieTransformer.toMovieResponseDTO(movieDB), HttpStatus.OK);
    }

}
