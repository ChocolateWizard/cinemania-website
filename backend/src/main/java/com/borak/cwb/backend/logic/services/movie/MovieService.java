/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.movie;

import com.borak.cwb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.logic.transformers.ActingTransformer;
import com.borak.cwb.backend.logic.transformers.ActorTransformer;
import com.borak.cwb.backend.logic.transformers.DirectorTransformer;
import com.borak.cwb.backend.logic.transformers.MovieTransformer;
import com.borak.cwb.backend.logic.transformers.WriterTransformer;
import com.borak.cwb.backend.repository.api.IActingRepository;
import com.borak.cwb.backend.repository.api.IActorRepository;
import com.borak.cwb.backend.repository.api.IDirectorRepository;
import com.borak.cwb.backend.repository.api.IGenreRepository;
import com.borak.cwb.backend.repository.api.IMovieRepository;
import com.borak.cwb.backend.repository.api.IWriterRepository;
import com.borak.cwb.backend.repository.file.FileRepository;
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
//@Service
//@Transactional
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
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJDBC(movieRepo.findAllWithGenresPaginated(page, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresPopularPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJDBC(movieRepo.findAllByAudienceRatingWithGenresPaginated(page, size, POPULARITY_TRESHOLD));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue() - 1;
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJDBC(movieRepo.findAllByReleaseYearWithGenresPaginated(page, size, year));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllMoviesWithDetailsPaginated(int page, int size) {
        List<MovieResponseDTO> movies = movieTransformer.toResponseFromJDBC(movieRepo.findAllWithRelationsPaginated(page, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getMovieWithGenres(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdWithGenres(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.toResponseFromJDBC(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWithDetails(Long id) {
        Optional<MovieJDBC> movie = movieRepo.findByIdWithRelations(id);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movieTransformer.toResponseFromJDBC(movie.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieDirectors(Long id) {
        if (movieRepo.existsById(id)) {
            List<DirectorJDBC> directors = directorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(directorTransformer.toMovieDirectorResponseFromJDBC(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieWriters(Long id) {
        if (movieRepo.existsById(id)) {
            List<WriterJDBC> writers = writerRepo.findAllByMediaId(id);
            return new ResponseEntity<>(writerTransformer.toMovieWriterResponseFromJDBC(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActors(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActorJDBC> actors = actorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actorTransformer.toMovieActorResponseFromJDBC(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity getMovieActorsWithRoles(Long id) {
        if (movieRepo.existsById(id)) {
            List<ActingJDBC> actings = actingRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actingTransformer.toMovieActorResponseFromJDBC(actings), HttpStatus.OK);
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
        return new ResponseEntity(movieTransformer.toResponseFromJDBC(movie.get()), HttpStatus.OK);

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
            movie = movieRepo.findByIdWithRelations(movieDB.getId());
            fileRepo.saveMediaCoverImage(movieClient.getCoverImage());
        } else {
            movie = movieRepo.findByIdWithRelations(movieDB.getId());
        }
        return new ResponseEntity<>(movieTransformer.toResponseFromJDBC(movie.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putMovie(MovieRequestDTO request) {
        if (!movieRepo.existsById(request.getId())) {
            throw new ResourceNotFoundException("Movie with id: " + request.getId() + " does not exist in database!");
        }
        for (Long genre : request.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : request.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : request.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (MovieRequestDTO.Actor actor : request.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        Optional<MovieJDBC> response;
        Optional<String> beforeUpdateCoverImage = movieRepo.findByIdCoverImage(request.getId());
        if (request.getCoverImage() != null) {
            //MyImage != null
            //replace cover image
            request.getCoverImage().setName("" + request.getId());
            MovieJDBC movieToStore = movieTransformer.toMovieJDBC(request);
            movieRepo.update(movieToStore);
            response = movieRepo.findByIdWithRelations(request.getId());
            if (beforeUpdateCoverImage.isPresent()) {
                fileRepo.deleteIfExistsMediaCoverImage(beforeUpdateCoverImage.get());
            }
            fileRepo.saveMediaCoverImage(request.getCoverImage());
        } else {
            //MyImage == null
            //delete cover image          
            MovieJDBC movieToStore = movieTransformer.toMovieJDBC(request);
            movieRepo.update(movieToStore);
            response = movieRepo.findByIdWithRelations(request.getId());
            if (beforeUpdateCoverImage.isPresent()) {
                fileRepo.deleteIfExistsMediaCoverImage(beforeUpdateCoverImage.get());
            }
        }
        return new ResponseEntity<>(movieTransformer.toResponseFromJDBC(response.get()), HttpStatus.OK);

    }

}
