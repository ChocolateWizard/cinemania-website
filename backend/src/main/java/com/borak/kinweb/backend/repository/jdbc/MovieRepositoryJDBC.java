/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
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
public class MovieRepositoryJDBC implements IMovieRepository<MovieJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//===========================================================================================
    @Override
    public MovieJDBC save(MovieJDBC movie) throws DataAccessException, IllegalArgumentException {
        try {
            if (movie.getId() == null) {
                //Perform INSERT
                //Insert media info first, then movie ID, and then relationship data
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQLMovie.INSERT_MEDIA_PS);
                    ps.setString(1, movie.getTitle());
                    ps.setString(2, movie.getCoverImageUrl());
                    ps.setString(3, movie.getDescription());
                    ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
                    ps.setInt(5, movie.getAudienceRating());
                    return ps;
                }, keyHolder);
                movie.setId((long) keyHolder.getKey());
                jdbcTemplate.update(SQLMovie.INSERT_MEDIA_MOVIE_PS, new Object[]{movie.getId(), movie.getLength()}, new int[]{Types.BIGINT, Types.INTEGER});
                insertGenrePivot(movie.getGenres(), movie.getId());
                insertDirectorPivot(movie.getDirectors(), movie.getId());
                insertWriterPivot(movie.getWriters(), movie.getId());
                insertActorPivot(movie.getActings(), movie.getId());
                //---------------------------------------------------------------------------------------
            } else {
                //Perform UPDATE
                jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_PS, new Object[]{movie.getTitle(), Date.valueOf(movie.getReleaseDate()), movie.getCoverImageUrl(), movie.getDescription(), movie.getAudienceRating(), movie.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
                jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_MOVIE_PS, new Object[]{movie.getLength(), movie.getId()}, new int[]{Types.INTEGER, Types.BIGINT});
                updateGenre(movie.getGenres(), movie.getId());
                updateDirector(movie.getDirectors(), movie.getId());
                updateWriter(movie.getWriters(), movie.getId());
                updateActors(movie.getActings(), movie.getId());
            }
            return movie;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid movie data.");
        }

    }

    @Override
    public List<MovieJDBC> saveAll(List<MovieJDBC> movies) throws DataAccessException, IllegalArgumentException {
        int i = 0;
        try {
            for (i = 0; i < movies.size(); i++) {
                MovieJDBC movie = movies.get(i);
                if (movie.getId() == null) {
                    KeyHolder keyHolder = new GeneratedKeyHolder();
                    jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(SQLMovie.INSERT_MEDIA_PS);
                        ps.setString(1, movie.getTitle());
                        ps.setString(2, movie.getCoverImageUrl());
                        ps.setString(3, movie.getDescription());
                        ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
                        ps.setInt(5, movie.getAudienceRating());
                        return ps;
                    }, keyHolder);
                    movie.setId((long) keyHolder.getKey());
                    jdbcTemplate.update(SQLMovie.INSERT_MEDIA_MOVIE_PS, new Object[]{movie.getId(), movie.getLength()}, new int[]{Types.BIGINT, Types.INTEGER});
                    insertGenrePivot(movie.getGenres(), movie.getId());
                    insertDirectorPivot(movie.getDirectors(), movie.getId());
                    insertWriterPivot(movie.getWriters(), movie.getId());
                    insertActorPivot(movie.getActings(), movie.getId());
                } else {
                    jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_PS, new Object[]{movie.getTitle(), Date.valueOf(movie.getReleaseDate()), movie.getCoverImageUrl(), movie.getDescription(), movie.getAudienceRating(), movie.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
                    jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_MOVIE_PS, new Object[]{movie.getLength(), movie.getId()}, new int[]{Types.INTEGER, Types.BIGINT});
                    updateGenre(movie.getGenres(), movie.getId());
                    updateDirector(movie.getDirectors(), movie.getId());
                    updateWriter(movie.getWriters(), movie.getId());
                    updateActors(movie.getActings(), movie.getId());
                }
                movies.set(i, movie);
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid movie data at movie number: " + i);
        }
        return movies;
    }

    @Override
    public Optional<MovieJDBC> findById(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
        if (movie != null) {
            List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
            List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
            List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
            List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
            List<ActorJDBC> actors = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actorRM);
            List<ActingJDBC> actings = new ArrayList<>();
            for (ActorJDBC actor : actors) {
                ActingJDBC acting = new ActingJDBC();
                acting.setActor(actor);
                List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                acting.setRoles(roles);
                actings.add(acting);
            }
            movie.setGenres(genres);
            movie.setDirectors(directors);
            movie.setWriters(writers);
            movie.setActings(actings);
            movie.setCritiques(critiques);
        }
        return Optional.ofNullable(movie);
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        Long dbId = jdbcTemplate.queryForObject(SQLMovie.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
        return dbId != null;
    }

    @Override
    public List<MovieJDBC> findAll() {
        List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
        for (MovieJDBC movie : movies) {
            List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
            List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
            List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
            List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
            List<ActorJDBC> actors = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.actorRM);
            List<ActingJDBC> actings = new ArrayList<>();
            for (ActorJDBC actor : actors) {
                ActingJDBC acting = new ActingJDBC();
                acting.setActor(actor);
                List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{movie.getId(), actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                acting.setRoles(roles);
                actings.add(acting);
            }
            movie.setGenres(genres);
            movie.setDirectors(directors);
            movie.setWriters(writers);
            movie.setActings(actings);
            movie.setCritiques(critiques);
        }
        return movies;
    }

    @Override
    public List<MovieJDBC> findAllById(List<Long> ids) {
        List<MovieJDBC> movies = new ArrayList<>();
        for (Long id : ids) {
            MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
            if (movie != null) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLMovie.FIND_ALL_CRITIQUES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
                List<ActorJDBC> actors = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.actorRM);
                List<ActingJDBC> actings = new ArrayList<>();
                for (ActorJDBC actor : actors) {
                    ActingJDBC acting = new ActingJDBC();
                    acting.setActor(actor);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{movie.getId(), actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
                    acting.setRoles(roles);
                    actings.add(acting);
                }
                movie.setGenres(genres);
                movie.setDirectors(directors);
                movie.setWriters(writers);
                movie.setActings(actings);
                movie.setCritiques(critiques);
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public long count() {
        return jdbcTemplate.queryForObject(SQLMovie.COUNT_S, Long.class);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_MEDIA_PS, new Object[]{id}, new int[]{Types.BIGINT});
    }

    @Override
    public void delete(MovieJDBC movie) {
        jdbcTemplate.update(SQLMovie.DELETE_MEDIA_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT});
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        List<Object[]> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(new Object[]{id});
        }
        jdbcTemplate.batchUpdate(SQLMovie.DELETE_MEDIA_PS, list, new int[]{Types.BIGINT});
    }

    @Override
    public void deleteAll(List<MovieJDBC> movies) {
        List<Object[]> list = new ArrayList<>();
        for (MovieJDBC movie : movies) {
            list.add(new Object[]{movie.getId()});
        }
        jdbcTemplate.batchUpdate(SQLMovie.DELETE_MEDIA_PS, list, new int[]{Types.BIGINT});
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MEDIA_S);
    }

    @Override
    public List<MovieJDBC> findAllNoRelationships() throws DataAccessException {
        List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
        return movies;
    }

    @Override
    public List<MovieJDBC> findAllRelationshipGenres() throws DataAccessException {
        List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
        for (MovieJDBC movie : movies) {
            List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
            movie.setGenres(genres);
        }
        return movies;
    }

    @Override
    public Optional<MovieJDBC> findByIdNoRelationships(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
        return Optional.ofNullable(movie);
    }

    @Override
    public MovieJDBC findByIdGenres(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
        MovieJDBC movie = new MovieJDBC();
        movie.setGenres(genres);

        return movie;
    }

    @Override
    public MovieJDBC findByIdDirectors(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        List<DirectorJDBC> directors = jdbcTemplate.query(SQLMovie.FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.directorRM);
        MovieJDBC movie = new MovieJDBC();
        movie.setDirectors(directors);
        return movie;
    }

    @Override
    public MovieJDBC findByIdWriters(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        List<WriterJDBC> writers = jdbcTemplate.query(SQLMovie.FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.writerRM);
        MovieJDBC movie = new MovieJDBC();
        movie.setWriters(writers);
        return movie;
    }

    @Override
    public MovieJDBC findByIdActors(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        MovieJDBC movie = new MovieJDBC();
        List<ActorJDBC> actors = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actorRM);

        for (ActorJDBC actor : actors) {
            movie.getActings().add(new ActingJDBC(movie, actor));
        }
        return movie;
    }

    @Override
    public MovieJDBC findByIdActorsWithRoles(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        MovieJDBC movie = new MovieJDBC();
        List<ActorJDBC> actors = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.actorRM);
        for (ActorJDBC actor : actors) {
            ActingJDBC acting = new ActingJDBC(movie, actor);
            List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLMovie.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLMovie.actingRoleRM);
            acting.setRoles(roles);
            movie.getActings().add(acting);
        }
        return movie;
    }

//-----------------------------------------------------------------------------------------------------
    private void updateGenre(List<GenreJDBC> genres, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertGenrePivot(genres, id);
    }

    private void updateDirector(List<DirectorJDBC> directors, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertDirectorPivot(directors, id);
    }

    private void updateWriter(List<WriterJDBC> writers, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertWriterPivot(writers, id);
    }

    private void updateActors(List<ActingJDBC> actings, Long id) {
        jdbcTemplate.update(SQLMovie.DELETE_ALL_MOVIE_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertActorPivot(actings, id);
    }

    private void insertGenrePivot(List<GenreJDBC> genres, Long primaryKey) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_GENRE_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, primaryKey);
                ps.setLong(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        }
        );
    }

    private void insertDirectorPivot(List<DirectorJDBC> directors, Long primaryKey) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_DIRECTOR_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, primaryKey);
                ps.setLong(2, directors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        }
        );
    }

    private void insertWriterPivot(List<WriterJDBC> writers, Long primaryKey) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_WRITERS_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, primaryKey);
                ps.setLong(2, writers.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return writers.size();
            }
        }
        );
    }

    private void insertActorPivot(List<ActingJDBC> actings, Long primaryKey) {
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_ACTINGS_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, primaryKey);
                ps.setLong(2, actings.get(i).getActor().getId());
            }

            @Override
            public int getBatchSize() {
                return actings.size();
            }
        }
        );
    }

}
