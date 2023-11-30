/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.sql.SQLMovie;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
import com.borak.kinweb.backend.repository.api.IMovieRepository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class MovieRepositoryJDBC implements IMovieRepository<MovieJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MovieJDBC> findAllWithGenres() throws DatabaseException {
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
    public List<MovieJDBC> findAllWithGenresPaginated(int page, int size) throws DatabaseException {
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
    public List<MovieJDBC> findAllWithRelations() throws DatabaseException {
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
                    for (ActingRoleJDBC role : roles) {
                        role.setActing(acting);
                    }
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
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllWithRelationsPaginated(int page, int size) throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_PAGINATED_PS, new Object[]{size, page}, new int[]{Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
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
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllByAudienceRatingWithGenresPaginated(int page, int size, int ratingThresh) throws DatabaseException {
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
    public List<MovieJDBC> findAllByReleaseYearWithGenresPaginated(int page, int size, int year) throws DatabaseException {
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
    public Optional<String> findByIdCoverImage(Long id) throws DatabaseException {
        try {
            String coverImage = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_COVER_IMAGE_URL_PS, new Object[]{id}, new int[]{Types.BIGINT}, String.class);
            return Optional.ofNullable(coverImage);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movie cover image", e);
        }
    }

    @Override
    public void updateCoverImage(Long id, String coverImage) throws DatabaseException {
        try {
            int i = jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_COVER_IMAGE_PS, new Object[]{coverImage, id}, new int[]{Types.VARCHAR, Types.BIGINT});
            if (i <= 0) {
                throw new DatabaseException("Unable to update cover image");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating cover image for movie with id: " + id, e);
        }
    }

    @Override
    public MovieJDBC insert(MovieJDBC entity) throws DatabaseException {
        try {
            performInsert(entity);
            return entity;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while inserting movie", e);
        }
    }

    @Override
    public void update(MovieJDBC entity) throws DatabaseException {
        try {
            performUpdate(entity);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating movie with id: " + entity.getId(), e);
        }
    }

    @Override
    public Optional<MovieJDBC> findById(Long id) throws DatabaseException {
        try {
            MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
            return Optional.of(movie);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searing for movie with id: " + id, e);
        }
    }

    @Override
    public Optional<MovieJDBC> findByIdWithGenres(Long id) throws DatabaseException {
        try {
            MovieJDBC movie = jdbcTemplate.queryForObject(SQLMovie.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.movieRM);
            if (movie == null) {
                return Optional.empty();
            }
            List<GenreJDBC> genres = jdbcTemplate.query(SQLMovie.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLMovie.genreRM);
            movie.setGenres(genres);
            return Optional.of(movie);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searing for movie with id: " + id, e);
        }
    }

    @Override
    public Optional<MovieJDBC> findByIdWithRelations(Long id) throws DatabaseException {
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
            jdbcTemplate.queryForObject(SQLMovie.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if movie with id: " + id + " exists", e);
        }
    }

    @Override
    public List<MovieJDBC> findAll() throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_S, SQLMovie.movieRM);
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
        }
    }

    @Override
    public List<MovieJDBC> findAllPaginated(int page, int size) throws DatabaseException {
        try {
            List<MovieJDBC> movies = jdbcTemplate.query(SQLMovie.FIND_ALL_PAGINATED_PS, new Object[]{size, page}, new int[]{Types.INTEGER, Types.INTEGER}, SQLMovie.movieRM);
            return movies;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving movies", e);
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

//=====================================================================================================================
//=========================================PRIVATE METHODS=============================================================
//=====================================================================================================================
    private void performUpdate(MovieJDBC movie) {
        int i = jdbcTemplate.update(SQLMovie.UPDATE_MEDIA_PS, new Object[]{movie.getTitle(), Date.valueOf(movie.getReleaseDate()), movie.getCoverImage(), movie.getDescription(), movie.getAudienceRating(), movie.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
        if (i <= 0) {
            throw new DatabaseException("Unable to update movie with id: " + movie.getId());
        }
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

    private void performInsert(MovieJDBC movie) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQLMovie.INSERT_MEDIA_PS, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, movie.getTitle());
            if (movie.getCoverImage() == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, movie.getCoverImage());
            }
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setInt(5, movie.getAudienceRating());
            return ps;
        }, keyHolder);

        movie.setId(keyHolder.getKey().longValue());
        jdbcTemplate.update(SQLMovie.INSERT_MEDIA_MOVIE_PS, new Object[]{movie.getId(), movie.getLength()}, new int[]{Types.BIGINT, Types.INTEGER});
        performInsertGenrePivot(movie.getGenres(), movie.getId());
        performInsertDirectorPivot(movie.getDirectors(), movie.getId());
        performInsertWriterPivot(movie.getWriters(), movie.getId());
        performInsertActorPivot(movie.getActings(), movie.getId());
        performInsertActingRoles(movie.getActings(), movie.getId());
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

    private void performInsertActingRoles(List<ActingJDBC> actings, Long id) {
        final int n = actings.stream().mapToInt(acting -> acting.getRoles().size()).sum();
        Object[][] data = new Object[n][4];
        int i = 0;
        for (ActingJDBC acting : actings) {
            for (ActingRoleJDBC role : acting.getRoles()) {
                data[i][0] = id;
                data[i][1] = acting.getActor().getId();
                data[i][2] = role.getId();
                data[i][3] = role.getName();
                i++;
            }
        }
        jdbcTemplate.batchUpdate(SQLMovie.INSERT_MEDIA_ACTING_ROLES_PS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id);
                ps.setLong(2, (Long) data[i][1]);
                ps.setLong(3, (Long) data[i][2]);
                ps.setString(4, (String) data[i][3]);
            }

            @Override
            public int getBatchSize() {
                return n;
            }
        }
        );

    }

}
