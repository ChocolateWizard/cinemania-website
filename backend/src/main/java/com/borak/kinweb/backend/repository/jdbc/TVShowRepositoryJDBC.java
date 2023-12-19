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
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.api.ITVShowRepository;
import com.borak.kinweb.backend.repository.sql.SQLTVShow;
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

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class TVShowRepositoryJDBC implements ITVShowRepository<TVShowJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TVShowJDBC> findAllWithGenres() throws DatabaseException {
        try {
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_S, SQLTVShow.tvShowRM);
            for (TVShowJDBC tvShow : tvShows) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
                tvShow.setGenres(genres);
            }
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAllWithRelations() throws DatabaseException {
        try {
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_S, SQLTVShow.tvShowRM);
            for (TVShowJDBC tvShow : tvShows) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLTVShow.FIND_ALL_CRITIQUES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(SQLTVShow.FIND_ALL_DIRECTORS_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(SQLTVShow.FIND_ALL_WRITERS_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.writerRM);
                List<ActingJDBC> actings = jdbcTemplate.query(SQLTVShow.FIND_ALL_ACTING_ACTORS_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.actingActorRM);
                for (ActingJDBC acting : actings) {
                    acting.setMedia(tvShow);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLTVShow.FIND_ALL_ACTING_ROLES_PS, new Object[]{tvShow.getId(), acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLTVShow.actingRoleRM);
                    for (ActingRoleJDBC role : roles) {
                        role.setActing(acting);
                    }
                    acting.setRoles(roles);
                }
                tvShow.setGenres(genres);
                tvShow.setDirectors(directors);
                tvShow.setWriters(writers);
                tvShow.setActings(actings);
                tvShow.setCritiques(critiques);
            }
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAllWithGenresPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_PAGINATED_PS, new Object[]{size, offset}, new int[]{Types.INTEGER, Types.INTEGER}, SQLTVShow.tvShowRM);
            for (TVShowJDBC tvShow : tvShows) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
                tvShow.setGenres(genres);
            }
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAllWithRelationsPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_PAGINATED_PS, new Object[]{size, offset}, new int[]{Types.INTEGER, Types.INTEGER}, SQLTVShow.tvShowRM);
            for (TVShowJDBC tvShow : tvShows) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLTVShow.FIND_ALL_CRITIQUES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(SQLTVShow.FIND_ALL_DIRECTORS_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(SQLTVShow.FIND_ALL_WRITERS_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.writerRM);
                List<ActingJDBC> actings = jdbcTemplate.query(SQLTVShow.FIND_ALL_ACTING_ACTORS_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.actingActorRM);
                for (ActingJDBC acting : actings) {
                    acting.setMedia(tvShow);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLTVShow.FIND_ALL_ACTING_ROLES_PS, new Object[]{tvShow.getId(), acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLTVShow.actingRoleRM);
                    for (ActingRoleJDBC role : roles) {
                        role.setActing(acting);
                    }
                    acting.setRoles(roles);
                }
                tvShow.setGenres(genres);
                tvShow.setDirectors(directors);
                tvShow.setWriters(writers);
                tvShow.setActings(actings);
                tvShow.setCritiques(critiques);
            }
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAllByAudienceRatingWithGenresPaginated(int page, int size, int ratingThresh) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0 || ratingThresh < 0 || ratingThresh > 100) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0, size must be non-negative, and ratingTresh must be between 0 and 100 (inclusive)");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_BY_RATING_PAGINATED_PS, new Object[]{ratingThresh, size, offset}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER}, SQLTVShow.tvShowRM);
            for (TVShowJDBC tvShow : tvShows) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
                tvShow.setGenres(genres);
            }
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAllByReleaseYearWithGenresPaginated(int page, int size, int year) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0 || year < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and the size and year must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_BY_YEAR_PAGINATED_PS, new Object[]{year, size, offset}, new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER}, SQLTVShow.tvShowRM);
            for (TVShowJDBC tvShow : tvShows) {
                List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{tvShow.getId()}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
                tvShow.setGenres(genres);
            }
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public Optional<String> findByIdCoverImage(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            String coverImage = jdbcTemplate.queryForObject(SQLTVShow.FIND_BY_ID_COVER_IMAGE_URL_PS, new Object[]{id}, new int[]{Types.BIGINT}, String.class);
            return Optional.ofNullable(coverImage);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DatabaseException("No tv show found with given id: " + id, e);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving tv show cover image", ex);
        }
    }

    @Override
    public Optional<TVShowJDBC> findByIdWithGenres(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            TVShowJDBC tvShow = jdbcTemplate.queryForObject(SQLTVShow.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.tvShowRM);
            List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
            tvShow.setGenres(genres);
            return Optional.of(tvShow);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searching for tv show with id: " + id, e);
        }
    }

    @Override
    public Optional<TVShowJDBC> findByIdWithRelations(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            TVShowJDBC tvShow = jdbcTemplate.queryForObject(SQLTVShow.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.tvShowRM);
            List<GenreJDBC> genres = jdbcTemplate.query(SQLTVShow.FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.genreRM);
            List<CritiqueJDBC> critiques = jdbcTemplate.query(SQLTVShow.FIND_ALL_CRITIQUES_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.critiqueRM);
            List<DirectorJDBC> directors = jdbcTemplate.query(SQLTVShow.FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.directorRM);
            List<WriterJDBC> writers = jdbcTemplate.query(SQLTVShow.FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.writerRM);
            List<ActingJDBC> actings = jdbcTemplate.query(SQLTVShow.FIND_ALL_ACTING_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.actingActorRM);
            for (ActingJDBC acting : actings) {
                acting.setMedia(tvShow);
                List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLTVShow.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLTVShow.actingRoleRM);
                for (ActingRoleJDBC role : roles) {
                    role.setActing(acting);
                }
                acting.setRoles(roles);
            }
            tvShow.setGenres(genres);
            tvShow.setDirectors(directors);
            tvShow.setWriters(writers);
            tvShow.setActings(actings);
            tvShow.setCritiques(critiques);
            return Optional.of(tvShow);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searching for tv show with id: " + id, e);
        }
    }

    @Override
    public void updateCoverImage(Long id, String coverImage) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            int i = jdbcTemplate.update(SQLTVShow.UPDATE_MEDIA_COVER_IMAGE_PS, new Object[]{coverImage, id}, new int[]{Types.VARCHAR, Types.BIGINT});
            if (i <= 0) {
                throw new DatabaseException("Error while updating cover image for tv show with id: " + id + ". No tv show found with given id");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating cover image for tv show with id: " + id, e);
        }
    }

    @Override
    public TVShowJDBC insert(TVShowJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Invalid parameter: entity must be non-null");
            }
            performInsert(entity);
            return entity;
        } catch (NullPointerException | DataAccessException e) {
            throw new DatabaseException("Error while inserting tv show", e);
        }
    }

    @Override
    public void update(TVShowJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Invalid parameter: entity must be non-null");
            }
            performUpdate(entity);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error while updating tv show with id: " + entity.getId() + ". " + e.getMessage(), e);
        } catch (NullPointerException | DataAccessException e) {
            throw new DatabaseException("Error while updating tv show with id: " + entity.getId(), e);
        }
    }

    @Override
    public Optional<TVShowJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            TVShowJDBC tvShow = jdbcTemplate.queryForObject(SQLTVShow.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLTVShow.tvShowRM);
            return Optional.of(tvShow);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searching for tv show with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            jdbcTemplate.queryForObject(SQLTVShow.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if tv show with id: " + id + " exists", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAll() throws DatabaseException {
        try {
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_S, SQLTVShow.tvShowRM);
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public List<TVShowJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<TVShowJDBC> tvShows = jdbcTemplate.query(SQLTVShow.FIND_ALL_PAGINATED_PS, new Object[]{size, offset}, new int[]{Types.INTEGER, Types.INTEGER}, SQLTVShow.tvShowRM);
            return tvShows;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving tv shows", e);
        }
    }

    @Override
    public long count() throws DatabaseException {
        try {
            return jdbcTemplate.queryForObject(SQLTVShow.COUNT_S, Long.class);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving number of tv shows", e);
        }
    }

    @Override
    public void deleteById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            int i = jdbcTemplate.update(SQLTVShow.DELETE_MEDIA_PS, new Object[]{id}, new int[]{Types.BIGINT});
            if (i <= 0) {
                throw new DatabaseException("Error while deleting tv show with id: " + id + ". No tv show found with given id");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting tv show with id: " + id, e);
        }
    }

//=====================================================================================================================
//=========================================PRIVATE METHODS=============================================================
//=====================================================================================================================
    private void performUpdate(TVShowJDBC tvShow) throws DatabaseException {
        int i = jdbcTemplate.update(SQLTVShow.UPDATE_MEDIA_PS, new Object[]{tvShow.getTitle(), Date.valueOf(tvShow.getReleaseDate()), tvShow.getCoverImage(), tvShow.getDescription(), tvShow.getAudienceRating(), tvShow.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
        if (i <= 0) {
            throw new DatabaseException("No tv show found with given id");
        }
        jdbcTemplate.update(SQLTVShow.UPDATE_MEDIA_TV_SHOW_PS, new Object[]{tvShow.getNumberOfSeasons(), tvShow.getId()}, new int[]{Types.INTEGER, Types.BIGINT});
        performUpdateGenre(tvShow.getGenres(), tvShow.getId());
        performUpdateDirector(tvShow.getDirectors(), tvShow.getId());
        performUpdateWriter(tvShow.getWriters(), tvShow.getId());
        performUpdateActors(tvShow.getActings(), tvShow.getId());
        performInsertActingRoles(tvShow.getActings(), tvShow.getId());
    }

    private void performUpdateGenre(List<GenreJDBC> genres, Long id) {
        jdbcTemplate.update(SQLTVShow.DELETE_ALL_TV_SHOW_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertGenrePivot(genres, id);
    }

    private void performUpdateDirector(List<DirectorJDBC> directors, Long id) {
        jdbcTemplate.update(SQLTVShow.DELETE_ALL_TV_SHOW_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertDirectorPivot(directors, id);
    }

    private void performUpdateWriter(List<WriterJDBC> writers, Long id) {
        jdbcTemplate.update(SQLTVShow.DELETE_ALL_TV_SHOW_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertWriterPivot(writers, id);
    }

    private void performUpdateActors(List<ActingJDBC> actings, Long id) {
        jdbcTemplate.update(SQLTVShow.DELETE_ALL_TV_SHOW_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        performInsertActorPivot(actings, id);
    }

    private void performInsert(TVShowJDBC tvShow) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQLTVShow.INSERT_MEDIA_PS, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tvShow.getTitle());
            if (tvShow.getCoverImage() == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, tvShow.getCoverImage());
            }
            ps.setString(3, tvShow.getDescription());
            ps.setDate(4, Date.valueOf(tvShow.getReleaseDate()));
            ps.setInt(5, tvShow.getAudienceRating());
            return ps;
        }, keyHolder);
        tvShow.setId(keyHolder.getKey().longValue());
        jdbcTemplate.update(SQLTVShow.INSERT_MEDIA_TV_SHOW_PS, new Object[]{tvShow.getId(), tvShow.getNumberOfSeasons()}, new int[]{Types.BIGINT, Types.INTEGER});
        performInsertGenrePivot(tvShow.getGenres(), tvShow.getId());
        performInsertDirectorPivot(tvShow.getDirectors(), tvShow.getId());
        performInsertWriterPivot(tvShow.getWriters(), tvShow.getId());
        performInsertActorPivot(tvShow.getActings(), tvShow.getId());
        performInsertActingRoles(tvShow.getActings(), tvShow.getId());
    }

    private void performInsertGenrePivot(List<GenreJDBC> genres, Long id) {
        jdbcTemplate.batchUpdate(SQLTVShow.INSERT_MEDIA_GENRE_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(SQLTVShow.INSERT_MEDIA_DIRECTOR_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(SQLTVShow.INSERT_MEDIA_WRITERS_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(SQLTVShow.INSERT_MEDIA_ACTINGS_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(SQLTVShow.INSERT_MEDIA_ACTING_ROLES_PS, new BatchPreparedStatementSetter() {
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
