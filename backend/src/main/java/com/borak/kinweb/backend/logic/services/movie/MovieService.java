/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.movie;

import com.borak.kinweb.backend.config.ConfigProperties;
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
import com.borak.kinweb.backend.repository.api.IMovieRepository;
import com.borak.kinweb.backend.repository.jpa.ActorRepositoryJPA;
import com.borak.kinweb.backend.repository.jpa.DirectorRepositoryJPA;
import com.borak.kinweb.backend.repository.jpa.GenreRepositoryJPA;
import com.borak.kinweb.backend.repository.jpa.WriterRepositoryJPA;
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
    ConfigProperties config;

    @Autowired
    private IMovieRepository<MovieJDBC, GenreJDBC, DirectorJDBC, WriterJDBC, ActorJDBC, ActingJDBC, Long> movieRepo;
    @Autowired
    private GenreRepositoryJPA genreRepo;
    @Autowired
    private DirectorRepositoryJPA directorRepo;
    @Autowired
    private WriterRepositoryJPA writerRepo;
    @Autowired
    private ActorRepositoryJPA actorRepo;

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
        List<MovieResponseDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllRelationshipGenresPaginated(page - 1, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllByAudienceRatingRelationshipGenresPaginated(page - 1, size, POPULARITY_TRESHOLD));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue();
        List<MovieResponseDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllByReleaseYearRelationshipGenresPaginated(page - 1, size, year));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllPaginated(page - 1, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getMovieWithGenres(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdNoRelationships(id);
        if (movie.isPresent()) {
            List<GenreJDBC> genres = movieRepo.findByIdGenres(id);
            movie.get().setGenres(genres);
            return new ResponseEntity<>(movieTransformer.jdbcToDto(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWithDetails(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findById(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.jdbcToDto(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenres() {
        List<MovieResponseDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAllRelationshipGenres());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetails() {
        List<MovieResponseDTO> movies = movieTransformer.jdbcToDto(movieRepo.findAll());
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getMovieDirectors(Long id) {
        if (movieRepo.existsById(id)) {
            List<DirectorJDBC> directors = movieRepo.findByIdDirectors(id);
            return new ResponseEntity<>(directorTransformer.jdbcToDto(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWriters(Long id) {
        if (movieRepo.existsById(id)) {
            List<WriterJDBC> writers = movieRepo.findByIdWriters(id);
            return new ResponseEntity<>(writerTransformer.jdbcToDto(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActors(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActorJDBC> actors = movieRepo.findByIdActors(id);
            return new ResponseEntity<>(actorTransformer.jdbcToDto(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActorsWithRoles(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActingJDBC> actings = movieRepo.findByIdActorsWithRoles(id);
            return new ResponseEntity<>(actingTransformer.jdbcToDto(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity deleteMovieById(long id) {
        Optional<MovieJDBC> movie = movieRepo.findById(id);
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("No movie found with id: " + id);
        }
        movieRepo.deleteById(id);
        if (movie.get().getCoverImage() != null && !movie.get().getCoverImage().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(movie.get().getCoverImage());
        }
        return new ResponseEntity(movieTransformer.jdbcToDto(movie.get()), HttpStatus.OK);

    }

    @Override
    public ResponseEntity postMovie(MoviePOSTRequestDTO movieClient) {
        List<String> errorMessages = new LinkedList<>();
        for (Long genre : movieClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                errorMessages.add("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : movieClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                errorMessages.add("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : movieClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                errorMessages.add("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (MoviePOSTRequestDTO.Actor actor : movieClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                errorMessages.add("Actor with id: " + actor + " does not exist in database!");
            }
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages.toArray(String[]::new));
        }

        for (MoviePOSTRequestDTO.Actor actor : movieClient.getActors()) {
            long i = 1;
            for (MoviePOSTRequestDTO.Actor.Role role : actor.getRoles()) {
                role.setId(i);
                i++;
            }
        }
        MovieJDBC movieJDBC = movieTransformer.dtoToJdbc(movieClient);
        MovieJDBC movieDB = movieRepo.insert(movieJDBC);
        if (movieClient.getCoverImage() != null){
            movieClient.getCoverImage().setName(""+movieDB.getId());
            fileRepo.saveMediaCoverImage(movieClient.getCoverImage());
//            movieClient.getCoverImage().setName(""+movieDB.getId());
//            fileRepo.storeMediaCoverImage(movieClient.getCoverImage());
//            String imageExtension=FilenameUtils.getExtension(movieClient.getCoverImage().getOriginalFilename());
//            String imageFullName=movieDB.getId() + "." + imageExtension;
//            movieDB.setCoverImage(imageFullName);
//            movieRepo.updateCoverImage(movieDB.getId(), movieDB.getCoverImage());
//            Image image=new Image(""+movieDB.getId(), imageExtension, movieDB.getId()+"."+imageExtension, movieClient.getCoverImage());
//            fileRepo.saveMediaCoverImage(image);
        }

        return new ResponseEntity<>(movieTransformer.jdbcToDto(movieDB), HttpStatus.OK);
    }

}
