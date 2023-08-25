/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc.movie;

import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.api.IMovieRepository;
import com.borak.kinweb.backend.repository.sql.SQLMovie;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class MovieRepositoryJDBC implements IMovieRepository<MovieJDBC, GenreJDBC, DirectorJDBC, WriterJDBC, ActorJDBC, ActingJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//========================================================================================================================  
    @Override
    public List<MovieJDBC> findAllNoRelationships() throws DatabaseException {
        try {
            return jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllRelationshipGenres() throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
            for (MovieJDBC movie : movies) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                movie.setGenres(genres);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public Optional<MovieJDBC> findByIdNoRelationships(Long id) throws DatabaseException {
        try {
            MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
            return Optional.of(movie);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving movie with id: " + id, ex);
        }
    }

    @Override
    public List<GenreJDBC> findByIdGenres(Long id) throws DatabaseException {
        try {
            return jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving genres of movie with id: " + id, ex);
        }
    }

    @Override
    public List<DirectorJDBC> findByIdDirectors(Long id) throws DatabaseException {
        try {
            return jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving directors of movie with id: " + id, ex);
        }
    }

    @Override
    public List<WriterJDBC> findByIdWriters(Long id) throws DatabaseException {
        try {
            return jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving writers of movie with id: " + id, ex);
        }
    }

    @Override
    public List<ActorJDBC> findByIdActors(Long id) throws DatabaseException {
        try {
            return jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actorRM);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving actors of movie with id: " + id, ex);
        }
    }

    @Override
    public List<ActingJDBC> findByIdActorsWithRoles(Long id) throws DatabaseException {
        try {
            List<ActingJDBC> actings = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actingActorRM);
            for (ActingJDBC acting : actings) {
                List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                acting.setRoles(roles);
            }
            return actings;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving actors and their roles of movie with id: " + id, ex);
        }
    }

    @Override
    public MovieJDBC save(MovieJDBC movie) throws DatabaseException {
        try {
            if (movie.getId() == null || !performDoesMovieIdExist(movie.getId())) {
                //Perform INSERT
                performInsert(movie);
            } else {
                //Perform UPDATE
                performUpdate(movie);
            }
            return movie;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while saving movie", e);
        }
    }

    @Override
    public List<MovieJDBC> saveAll(List<MovieJDBC> movies) throws DatabaseException {
        int i = 0;
        try {
            for (i = 0; i < movies.size(); i++) {
                if (movies.get(i).getId() == null || !performDoesMovieIdExist(movies.get(i).getId())) {
                    //Perform INSERT
                    performInsert(movies.get(i));
                } else {
                    //Perform UPDATE
                    performUpdate(movies.get(i));
                }
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while saving movies. Error occured while saving movie number: " + i, e);
        }
    }

    @Override
    public MovieJDBC insert(MovieJDBC movie) throws DatabaseException {
        try {
            performInsert(movie);
            return movie;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while inserting movie", e);
        }
    }

    @Override
    public List<MovieJDBC> insertAll(List<MovieJDBC> movies) throws DatabaseException {
        int i = 0;
        try {
            for (i = 0; i < movies.size(); i++) {
                performInsert(movies.get(i));
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while inserting movies. Error occured while inserting movie number: " + i, e);
        }
    }

    @Override
    public MovieJDBC update(MovieJDBC movie) throws DatabaseException {
        try {
            performUpdate(movie);
            return movie;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating movie with id: " + movie.getId(), e);
        }
    }

    @Override
    public List<MovieJDBC> updateAll(List<MovieJDBC> movies) throws DatabaseException {
        int i = 0;
        try {
            for (i = 0; i < movies.size(); i++) {
                performUpdate(movies.get(i));
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating movies. Error occured while updating movie number: " + i + " with id: " + movies.get(i).getId(), e);
        }
    }

    @Override
    public Optional<MovieJDBC> findById(Long id) throws DatabaseException {
        try {
            MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
            List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
            List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
            List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
            List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
            List<ActingJDBC> actings = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actingActorRM);
            for (ActingJDBC acting : actings) {
                acting.setMedia(movie);
                List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                acting.setRoles(roles);
            }
            movie.setGenres(genres);
            movie.setDirectors(directors);
            movie.setWriters(writers);
            movie.setActings(actings);
            movie.setCritiques(critiques);
            return Optional.of(movie);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searing for movie with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException {
        try {
            return performDoesMovieIdExist(id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if movie with id: " + id + " exists", e);
        }
    }

    @Override
    public List<MovieJDBC> findAll() throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
            for (MovieJDBC movie : movies) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
                List<ActingJDBC> actings = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ACTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.actingActorRM);
                for (ActingJDBC acting : actings) {
                    acting.setMedia(movie);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{movie.getId(), acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                    acting.setRoles(roles);
                }
                movie.setGenres(genres);
                movie.setDirectors(directors);
                movie.setWriters(writers);
                movie.setActings(actings);
                movie.setCritiques(critiques);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving all movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllById(List<Long> ids) throws DatabaseException {
        try {
            List<MovieJDBC> movies = new ArrayList<>(ids.size());
            for (Long id : ids) {
                MovieJDBC movie;
                try {
                    movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
                } catch (IncorrectResultSizeDataAccessException ex) {
                    //if movie not found, continue with next id
                    continue;
                }
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
                List<ActingJDBC> actings = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actingActorRM);
                for (ActingJDBC acting : actings) {
                    acting.setMedia(movie);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                    acting.setRoles(roles);
                }
                movie.setGenres(genres);
                movie.setDirectors(directors);
                movie.setWriters(writers);
                movie.setActings(actings);
                movie.setCritiques(critiques);
                movies.add(movie);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searing for movies with given ids", e);
        }
    }

    @Override
    public long count() throws DatabaseException {
        try {
            return jdbcTemplate.queryForObject(SQLMovie.COUNT_S, Long.class);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving number of movies", e);
        }
    }

    @Override
    public void deleteById(Long id) throws DatabaseException {
        try {
            jdbcTemplate.update(SQLMovie.DELETE_MEDIA_PS, new Object[]{id}, new int[]{Types.BIGINT});
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting movie with id: " + id, e);
        }
    }

    @Override
    public void delete(MovieJDBC entity) throws DatabaseException {
        try {
            jdbcTemplate.update(SQLMovie.DELETE_MEDIA_PS, new Object[]{entity.getId()}, new int[]{Types.BIGINT});
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting movie with id: " + entity.getId(), e);
        }
    }

    @Override
    public void deleteAllById(List<Long> ids) throws DatabaseException {
        try {
            List<Object[]> list = new ArrayList<>(ids.size());
            for (Long id : ids) {
                list.add(new Object[]{id});
            }
            jdbcTemplate.batchUpdate(SQLMovie.DELETE_MEDIA_PS, list, new int[]{Types.BIGINT});
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting movies with given ids", e);
        }
    }

    @Override
    public void deleteAll(List<MovieJDBC> entities) throws DatabaseException {
        try {
            List<Object[]> list = new ArrayList<>(entities.size());
            for (MovieJDBC movie : entities) {
                list.add(new Object[]{movie.getId()});
            }
            jdbcTemplate.batchUpdate(SQLMovie.DELETE_MEDIA_PS, list, new int[]{Types.BIGINT});
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting movies with given ids", e);
        }
    }

    @Override
    public void deleteAll() throws DatabaseException {
        try {
            jdbcTemplate.update(SQLMovie.DELETE_ALL_MEDIA_S);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting all movies", e);
        }
    }

    @Override
    public Optional<String> findByIdCoverImageUrl(Long id) throws DatabaseException {
        try {
            String url = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_COVER_IMAGE_URL_PS, new Object[]{id}, new int[]{Types.BIGINT}, String.class);
            return Optional.ofNullable(url);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movie cover image url", e);
        }

    }

//=====================================================================================================================
//==============================PRIVATE METHODS========================================================================
//=====================================================================================================================
    private boolean performDoesMovieIdExist(Long id) {
        try {
            jdbcTemplate.queryForObject(SQLMovie.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    private void performInsert(MovieJDBC movie) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQLMovie.INSERT_MEDIA_PS);
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getCoverImage());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setInt(5, movie.getAudienceRating());
            return ps;
        }, keyHolder);
        movie.setId((long) keyHolder.getKey());
        jdbcTemplate.update(SQLMovie.INSERT_MEDIA_MOVIE_PS, new Object[]{movie.getId(), movie.getLength()}, new int[]{Types.BIGINT, Types.INTEGER});
        performInsertGenrePivot(movie.getGenres(), movie.getId());
        performInsertDirectorPivot(movie.getDirectors(), movie.getId());
        performInsertWriterPivot(movie.getWriters(), movie.getId());
        performInsertActorPivot(movie.getActings(), movie.getId());
    }

    private void performUpdate(MovieJDBC movie) {
        jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_PS, new Object[]{movie.getTitle(), Date.valueOf(movie.getReleaseDate()), movie.getCoverImage(), movie.getDescription(), movie.getAudienceRating(), movie.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
        jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_MOVIE_PS, new Object[]{movie.getLength(), movie.getId()}, new int[]{Types.INTEGER, Types.BIGINT});
        performUpdateGenre(movie.getGenres(), movie.getId());
        performUpdateDirector(movie.getDirectors(), movie.getId());
        performUpdateWriter(movie.getWriters(), movie.getId());
        performUpdateActors(movie.getActings(), movie.getId());
    }

    private void performUpdateGenre(List<GenreJDBC> genres, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertGenrePivot(genres, id);
    }

    private void performUpdateDirector(List<DirectorJDBC> directors, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertDirectorPivot(directors, id);
    }

    private void performUpdateWriter(List<WriterJDBC> writers, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertWriterPivot(writers, id);
    }

    private void performUpdateActors(List<ActingJDBC> actings, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertActorPivot(actings, id);
    }

    private void performInsertGenrePivot(List<GenreJDBC> genres, Long id) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_GENRE_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id);
                ps.setLong(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        }
        );
    }

    private void performInsertDirectorPivot(List<DirectorJDBC> directors, Long id) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_DIRECTOR_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id);
                ps.setLong(2, directors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        }
        );
    }

    private void performInsertWriterPivot(List<WriterJDBC> writers, Long id) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_WRITERS_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id);
                ps.setLong(2, writers.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return writers.size();
            }
        }
        );
    }

    private void performInsertActorPivot(List<ActingJDBC> actings, Long id) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_ACTINGS_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id);
                ps.setLong(2, actings.get(i).getActor().getId());
                ps.setBoolean(3, actings.get(i).isStarring());
            }

            @Override
            public int getBatchSize() {
                return actings.size();
            }
        }
        );
    }

    @Override
    public List<MovieJDBC> findAllNoRelationshipsPaginated(int page, int size) throws DatabaseException {
        try {
            return jdbcTemplate.query(SQLMovie.FIND_ALL_PAGINATED_PS, new Object[]{size, page}, new int[]{Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllRelationshipGenresPaginated(int page, int size) throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_PAGINATED_PS, new Object[]{size, page}, new int[]{Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
            for (MovieJDBC movie : movies) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                movie.setGenres(genres);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllByAudienceRatingRelationshipGenresPaginated(int page, int size, int ratingThresh) throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_BY_RATING_PAGINATED_PS, new Object[]{ratingThresh, size, page}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
            for (MovieJDBC movie : movies) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                movie.setGenres(genres);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllByReleaseYearRelationshipGenresPaginated(int page, int size, int year) throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_BY_YEAR_PAGINATED_PS, new Object[]{year, size, page}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
            for (MovieJDBC movie : movies) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                movie.setGenres(genres);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllPaginated(int page, int size) throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_PAGINATED_PS, new Object[]{page, size}, new int[]{Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
            for (MovieJDBC movie : movies) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
                List<ActingJDBC> actings = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ACTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.actingActorRM);
                for (ActingJDBC acting : actings) {
                    acting.setMedia(movie);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{movie.getId(), acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                    acting.setRoles(roles);
                }
                movie.setGenres(genres);
                movie.setDirectors(directors);
                movie.setWriters(writers);
                movie.setActings(actings);
                movie.setCritiques(critiques);
            }
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving all movies", e);
        }
    }

}
