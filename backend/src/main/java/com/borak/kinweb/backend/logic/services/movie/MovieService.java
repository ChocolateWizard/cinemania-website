/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.domain.dto.movie.MovieRequestDTO;
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
public class MovieService implements IMovieService<MovieRequestDTO> {

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
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllWithGenresPaginated(page, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllByAudienceRatingWithGenresPaginated(page, size, POPULARITY_TRESHOLD));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue();
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllByReleaseYearWithGenresPaginated(page, size, year));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toMovieResponseDTO(movieRepo.findAllWithRelationsPaginated(page, size));
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
    public ResponseEntity postMovie(MovieRequestDTO movieClient) {
        for (Long genre : movieClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : movieClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : movieClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (MovieRequestDTO.Actor actor : movieClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        MovieJDBC movieJDBC = movieTransformer.toMovieJDBC(movieClient);
        movieJDBC.setCoverImage(null);
        MovieJDBC movieDB = movieRepo.insert(movieJDBC);
        Optional<MovieJDBC> movie;
        if (movieClient.getCoverImage() != null) {
            movieClient.getCoverImage().setName("" + movieDB.getId());
            movieRepo.updateCoverImage(movieDB.getId(), movieClient.getCoverImage().getFullName());
            movie = movieRepo.findById(movieDB.getId());
            fileRepo.saveMediaCoverImage(movieClient.getCoverImage());
        } else {
            movie = movieRepo.findById(movieDB.getId());
        }
        return new ResponseEntity<>(movieTransformer.toMovieResponseDTO(movie.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putMovie(MovieRequestDTO movieClient) {
        if (!movieRepo.existsById(movieClient.getId())) {
            throw new ResourceNotFoundException("Movie with id: " + movieClient.getId() + " does not exist in database!");
        }
        for (Long genre : movieClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : movieClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : movieClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (MovieRequestDTO.Actor actor : movieClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        Optional<MovieJDBC> movie;
        if (movieClient.getCoverImage() != null) {
            //replace cover image
            movieClient.getCoverImage().setName("" + movieClient.getId());
            MovieJDBC movieJDBC = movieTransformer.toMovieJDBC(movieClient);
            movieRepo.update(movieJDBC);
            movie = movieRepo.findById(movieClient.getId());
            fileRepo.saveMediaCoverImage(movieClient.getCoverImage());
        } else {
            //delete cover image
            Optional<String> coverImage = movieRepo.findByIdCoverImage(movieClient.getId());
            MovieJDBC movieJDBC = movieTransformer.toMovieJDBC(movieClient);
            movieRepo.update(movieJDBC);
            movie = movieRepo.findById(movieClient.getId());
            if (coverImage.isPresent()) {
                fileRepo.deleteIfExistsMediaCoverImage(coverImage.get());
            }
        }
        return new ResponseEntity<>(movieTransformer.toMovieResponseDTO(movie.get()), HttpStatus.OK);

    }

}
