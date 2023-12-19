/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.tv;

import com.borak.kinweb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.ResourceNotFoundException;
import com.borak.kinweb.backend.logic.transformers.ActingTransformer;
import com.borak.kinweb.backend.logic.transformers.ActorTransformer;
import com.borak.kinweb.backend.logic.transformers.DirectorTransformer;
import com.borak.kinweb.backend.logic.transformers.TVShowTransformer;
import com.borak.kinweb.backend.logic.transformers.WriterTransformer;
import com.borak.kinweb.backend.repository.api.IActingRepository;
import com.borak.kinweb.backend.repository.api.IActorRepository;
import com.borak.kinweb.backend.repository.api.IDirectorRepository;
import com.borak.kinweb.backend.repository.api.IGenreRepository;
import com.borak.kinweb.backend.repository.api.ITVShowRepository;
import com.borak.kinweb.backend.repository.api.IWriterRepository;
import com.borak.kinweb.backend.repository.util.FileRepository;
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
public class TVShowService implements ITVShowService<TVShowRequestDTO> {

    private static final int POPULARITY_TRESHOLD = 80;

    @Autowired
    private ITVShowRepository<TVShowJDBC, Long> tvShowRepo;
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
    private TVShowTransformer tvShowTransformer;
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
    public ResponseEntity getAllTVShowsWithGenresPaginated(int page, int size) {
        List<TVShowResponseDTO> tvShows = tvShowTransformer.toTVShowResponseDTO(tvShowRepo.findAllWithGenresPaginated(page, size));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenresPopularPaginated(int page, int size) {
        List<TVShowResponseDTO> tvShows = tvShowTransformer.toTVShowResponseDTO(tvShowRepo.findAllByAudienceRatingWithGenresPaginated(page, size, POPULARITY_TRESHOLD));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenresCurrentPaginated(int page, int size) {
        int year = Year.now().getValue();
        List<TVShowResponseDTO> tvShows = tvShowTransformer.toTVShowResponseDTO(tvShowRepo.findAllByReleaseYearWithGenresPaginated(page, size, year));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithDetailsPaginated(int page, int size) {
        List<TVShowResponseDTO> tvShows = tvShowTransformer.toTVShowResponseDTO(tvShowRepo.findAllWithRelationsPaginated(page, size));
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getTVShowWithGenres(Long id) {
        Optional<TVShowJDBC> tvShow = tvShowRepo.findByIdWithGenres(id);
        if (tvShow.isPresent()) {
            return new ResponseEntity<>(tvShowTransformer.toTVShowResponseDTO(tvShow.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getAllTVShowsWithGenres() {
        List<TVShowResponseDTO> tvShow = tvShowTransformer.toTVShowResponseDTO(tvShowRepo.findAllWithGenres());
        return new ResponseEntity<>(tvShow, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllTVShowsWithDetails() {
        List<TVShowResponseDTO> tvShows = tvShowTransformer.toTVShowResponseDTO(tvShowRepo.findAllWithRelations());
        return new ResponseEntity<>(tvShows, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getTVShowWithDetails(Long id) {
        Optional<TVShowJDBC> tvShow = tvShowRepo.findByIdWithRelations(id);
        if (tvShow.isPresent()) {
            return new ResponseEntity<>(tvShowTransformer.toTVShowResponseDTO(tvShow.get()), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowDirectors(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<DirectorJDBC> directors = directorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(directorTransformer.toTVShowDirectorResponseDTO(directors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowWriters(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<WriterJDBC> writers = writerRepo.findAllByMediaId(id);
            return new ResponseEntity<>(writerTransformer.toTVShowWriterResponseDTO(writers), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowActors(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<ActorJDBC> actors = actorRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actorTransformer.toTVShowActorResponseDTO(actors), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No tv show found with id: " + id);
    }

    @Override
    public ResponseEntity getTVShowActorsWithRoles(Long id) {
        if (tvShowRepo.existsById(id)) {
            List<ActingJDBC> actings = actingRepo.findAllByMediaId(id);
            return new ResponseEntity<>(actingTransformer.toTVShowActorResponseDTO(actings), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("No movie found with id: " + id);
    }

    @Override
    public ResponseEntity deleteTVShowById(long id) {
        Optional<TVShowJDBC> tvShow = tvShowRepo.findByIdWithRelations(id);
        if (tvShow.isEmpty()) {
            throw new ResourceNotFoundException("No tv show found with id: " + id);
        }
        tvShowRepo.deleteById(id);
        if (tvShow.get().getCoverImage() != null && !tvShow.get().getCoverImage().isEmpty()) {
            fileRepo.deleteIfExistsMediaCoverImage(tvShow.get().getCoverImage());
        }
        return new ResponseEntity(tvShowTransformer.toTVShowResponseDTO(tvShow.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity postTVShow(TVShowRequestDTO tvShowClient) {
        for (Long genre : tvShowClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : tvShowClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : tvShowClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (TVShowRequestDTO.Actor actor : tvShowClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        TVShowJDBC tvShowJDBC = tvShowTransformer.toTVShowJDBC(tvShowClient);
        tvShowJDBC.setCoverImage(null);
        TVShowJDBC tvShowDB = tvShowRepo.insert(tvShowJDBC);
        Optional<TVShowJDBC> tvShow;
        if (tvShowClient.getCoverImage() != null) {
            tvShowClient.getCoverImage().setName("" + tvShowDB.getId());
            tvShowRepo.updateCoverImage(tvShowDB.getId(), tvShowClient.getCoverImage().getFullName());
            tvShow = tvShowRepo.findById(tvShowDB.getId());
            fileRepo.saveMediaCoverImage(tvShowClient.getCoverImage());
        } else {
            tvShow = tvShowRepo.findById(tvShowDB.getId());
        }
        return new ResponseEntity<>(tvShowTransformer.toTVShowResponseDTO(tvShow.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity putTVShow(TVShowRequestDTO tvShowClient) {
        if (!tvShowRepo.existsById(tvShowClient.getId())) {
            throw new ResourceNotFoundException("TV show with id: " + tvShowClient.getId() + " does not exist in database!");
        }
        for (Long genre : tvShowClient.getGenres()) {
            if (!genreRepo.existsById(genre)) {
                throw new ResourceNotFoundException("Genre with id: " + genre + " does not exist in database!");
            }
        }
        for (Long director : tvShowClient.getDirectors()) {
            if (!directorRepo.existsById(director)) {
                throw new ResourceNotFoundException("Director with id: " + director + " does not exist in database!");
            }
        }
        for (Long writer : tvShowClient.getWriters()) {
            if (!writerRepo.existsById(writer)) {
                throw new ResourceNotFoundException("Writer with id: " + writer + " does not exist in database!");
            }
        }
        for (TVShowRequestDTO.Actor actor : tvShowClient.getActors()) {
            if (!actorRepo.existsById(actor.getId())) {
                throw new ResourceNotFoundException("Actor with id: " + actor.getId() + " does not exist in database!");
            }
        }
        Optional<TVShowJDBC> tvShow;
        if (tvShowClient.getCoverImage() != null) {
            //replace cover image
            tvShowClient.getCoverImage().setName("" + tvShowClient.getId());
            TVShowJDBC tvShowJDBC = tvShowTransformer.toTVShowJDBC(tvShowClient);
            tvShowRepo.update(tvShowJDBC);
            tvShow = tvShowRepo.findById(tvShowClient.getId());
            fileRepo.saveMediaCoverImage(tvShowClient.getCoverImage());
        } else {
            //delete cover image
            Optional<String> coverImage = tvShowRepo.findByIdCoverImage(tvShowClient.getId());
            TVShowJDBC tvShowJDBC = tvShowTransformer.toTVShowJDBC(tvShowClient);
            tvShowRepo.update(tvShowJDBC);
            tvShow = tvShowRepo.findById(tvShowClient.getId());
            if (coverImage.isPresent()) {
                fileRepo.deleteIfExistsMediaCoverImage(coverImage.get());
            }
        }
        return new ResponseEntity<>(tvShowTransformer.toTVShowResponseDTO(tvShow.get()), HttpStatus.OK);
    }

}
