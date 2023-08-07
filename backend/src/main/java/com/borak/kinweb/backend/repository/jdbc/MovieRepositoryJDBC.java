/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserCriticJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.repository.api.IMovieRepository;
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
import org.springframework.jdbc.core.RowMapper;
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

    //===================================QUERIES===============================================
    private static final String INSERT_MEDIA_PS = """
                                       INSERT INTO media(title,cover_image_url,description,release_date,audience_rating) 
                                       VALUES(?,?,?,?,?);
                                       """;
    private static final String INSERT_MEDIA_MOVIE_PS = """
                                       INSERT INTO movie(media_id,length) 
                                       VALUES(?,?);
                                       """;
    private static final String INSERT_MEDIA_GENRE_PS = """
                                       INSERT INTO media_genres(media_id,genre_id) 
                                       VALUES(?,?);
                                       """;
    private static final String INSERT_MEDIA_DIRECTOR_PS = """
                                       INSERT INTO media_directors(media_id,director_id) 
                                       VALUES(?,?);
                                       """;
    private static final String INSERT_MEDIA_WRITERS_PS = """
                                       INSERT INTO media_writers(media_id,writer_id) 
                                       VALUES(?,?);
                                       """;
    private static final String INSERT_MEDIA_ACTINGS_PS = """
                                       INSERT INTO acting(media_id,actor_id) 
                                       VALUES(?,?);
                                       """;
    private static final String UPDATE_MEDIA_PS = """
                                       UPDATE media
                                       SET title = ?, release_date = ?, cover_image_url = ?,description = ?,audience_rating=?
                                       WHERE media.id=(SELECT movie.id FROM movie WHERE movie.id=?);
                                       """;
    private static final String UPDATE_MEDIA_MOVIE_PS = """
                                       UPDATE movie
                                       SET length = ?
                                       WHERE movie.media_id=?;
                                       """;

    private static final String FIND_ALL_S = """
                                       SELECT media.id,media.title,media.cover_image_url,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id);
                                       """;
    private static final String FIND_ALL_GENRES_PS = """
                                                     SELECT genre.id,genre.name 
                                                     FROM genre JOIN media_genres ON(genre.id=media_genres.genre_id) 
                                                     WHERE media_genres.media_id=?;
                                                     """;
    private static final String FIND_ALL_DIRECTORS_PS = """
                                                        SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo_url 
                                                        FROM media_directors JOIN director ON(media_directors.director_id=director.person_id) JOIN person ON(person.id=director.person_id) 
                                                        WHERE media_directors.media_id=?;
                                                        """;
    private static final String FIND_ALL_WRITERS_PS = """
                                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo_url 
                                                      FROM media_writers JOIN writer ON(media_writers.writer_id=writer.person_id) JOIN person ON(person.id=writer.person_id) 
                                                      WHERE media_writers.media_id=?;
                                                      """;
    private static final String FIND_ALL_ACTORS_PS = """
                                                     SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo_url,actor.is_star 
                                                     FROM acting JOIN actor ON(acting.actor_id=actor.person_id) JOIN person ON(actor.person_id=person.id) 
                                                     WHERE acting.media_id=?;
                                                     """;
    private static final String FIND_ALL_ACTING_ROLES_PS = """
                                                           SELECT acting_role.id,acting_role.name 
                                                           FROM acting_role 
                                                           WHERE acting_role.acting_media_id=? AND acting_role.acting_actor_id=?;
                                                           """;

    private static final String FIND_ALL_CRITIQUES_PS = """
                                                           SELECT user.username,user.profile_image_url,critique.description,critique.rating 
                                                           FROM critique JOIN user_critic ON(critique.user_critic_id=user_critic.user_id) JOIN USER ON(user_critic.user_id=user.id) 
                                                           WHERE media_id=?;
                                                           """;

    private static final String FIND_BY_ID_PS = """
                                       SELECT media.id,media.title,media.cover_image_url,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id)
                                       WHERE movie.media_id=?;
                                       """;
    private static final String FIND_ID_PS = """
                                       SELECT media_id 
                                       FROM movie 
                                       WHERE media_id=?;
                                       """;
    private static final String COUNT_S = """
                                       SELECT COUNT(media.id) 
                                       FROM media JOIN movie ON(media.id=movie.media_id);
                                       """;
    private static final String DELETE_MEDIA_PS = """
                                       DELETE FROM media WHERE media.id=(SELECT movie.media_id
                                       FROM movie
                                       WHERE movie.media_id=?);
                                       """;
    private static final String DELETE_ALL_MEDIA_S = """
                                       DELETE FROM media WHERE media.id=(SELECT movie.media_id
                                       FROM movie);
                                       """;
    private static final String DELETE_ALL_MOVIE_GENRES_PS = """
                                       DELETE FROM media_genres WHERE media_genres.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;
    private static final String DELETE_ALL_MOVIE_DIRECTORS_PS = """
                                       DELETE FROM media_directors WHERE media_directors.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;
    private static final String DELETE_ALL_MOVIE_WRITERS_PS = """
                                       DELETE FROM media_writers WHERE media_writers.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;
    private static final String DELETE_ALL_MOVIE_ACTORS_PS = """
                                       DELETE FROM acting WHERE acting.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;

//================================================================================================
//========================================RowMappers==============================================
//================================================================================================
    private RowMapper<MovieJDBC> movieRM = (rs, num) -> {
        MovieJDBC movie = new MovieJDBC();
        movie.setId(rs.getLong("id"));
        movie.setTitle(rs.getString("title"));
        movie.setCoverImageUrl(rs.getString("cover_image_url"));
        movie.setDescription(rs.getString("description"));
        movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
        movie.setAudienceRating(rs.getInt("audience_rating"));
        movie.setCriticRating(rs.getObject("critic_rating", Integer.class));
        movie.setLength(rs.getInt("length"));
        return movie;
    };
    private RowMapper<GenreJDBC> genreRM = (rs, num) -> {
        GenreJDBC genre = new GenreJDBC();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

    private RowMapper<DirectorJDBC> directorRM = (rs, num) -> {
        DirectorJDBC director = new DirectorJDBC();
        director.setId(rs.getLong("id"));
        director.setFirstName(rs.getString("first_name"));
        director.setLastName(rs.getString("last_name"));
        director.setGender(Gender.parseGender(rs.getString("gender")));
        director.setProfilePhotoURL(rs.getString("profile_photo_url"));
        return director;
    };

    private RowMapper<WriterJDBC> writerRM = (rs, num) -> {
        WriterJDBC writer = new WriterJDBC();
        writer.setId(rs.getLong("id"));
        writer.setFirstName(rs.getString("first_name"));
        writer.setLastName(rs.getString("last_name"));
        writer.setGender(Gender.parseGender(rs.getString("gender")));
        writer.setProfilePhotoURL(rs.getString("profile_photo_url"));
        return writer;
    };

    private RowMapper<ActorJDBC> actorRM = (rs, num) -> {
        ActorJDBC actor = new ActorJDBC();
        actor.setId(rs.getLong("id"));
        actor.setFirstName(rs.getString("first_name"));
        actor.setLastName(rs.getString("last_name"));
        actor.setGender(Gender.parseGender(rs.getString("gender")));
        actor.setProfilePhotoURL(rs.getString("profile_photo_url"));
        actor.setIsStar(rs.getBoolean("is_star"));
        return actor;
    };

    private RowMapper<ActingRoleJDBC> actingRoleRM = (rs, num) -> {
        ActingRoleJDBC role = new ActingRoleJDBC();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        return role;
    };

    private RowMapper<CritiqueJDBC> critiqueRM = (rs, num) -> {
        CritiqueJDBC critique = new CritiqueJDBC();
        UserCriticJDBC critic = new UserCriticJDBC();
        critic.setUsername(rs.getString("username"));
        critic.setProfileImageUrl(rs.getString("profile_image_url"));
        critique.setCritic(critic);
        critique.setDescription(rs.getString("description"));
        critique.setRating(rs.getInt("rating"));
        return critique;
    };

//===========================================================================================
//===========================================================================================
//===========================================================================================
    @Override
    public MovieJDBC save(MovieJDBC movie) throws DataAccessException, IllegalArgumentException {
        try {
            if (movie.getId() == null) {
                //Perform INSERT
                //Insert media info first, then movie ID, and then relationship data
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_MEDIA_PS);
                    ps.setString(1, movie.getTitle());
                    ps.setString(2, movie.getCoverImageUrl());
                    ps.setString(3, movie.getDescription());
                    ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
                    ps.setInt(5, movie.getAudienceRating());
                    return ps;
                }, keyHolder);
                movie.setId((long) keyHolder.getKey());
                jdbcTemplate.update(INSERT_MEDIA_MOVIE_PS, new Object[]{movie.getId(), movie.getLength()}, new int[]{Types.BIGINT, Types.INTEGER});
                insertGenrePivot(movie.getGenres(), movie.getId());
                insertDirectorPivot(movie.getDirectors(), movie.getId());
                insertWriterPivot(movie.getWriters(), movie.getId());
                insertActorPivot(movie.getActings(), movie.getId());
                //---------------------------------------------------------------------------------------
            } else {
                //Perform UPDATE
                jdbcTemplate.update(UPDATE_MEDIA_PS, new Object[]{movie.getTitle(), Date.valueOf(movie.getReleaseDate()), movie.getCoverImageUrl(), movie.getDescription(), movie.getAudienceRating(), movie.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
                jdbcTemplate.update(UPDATE_MEDIA_MOVIE_PS, new Object[]{movie.getLength(), movie.getId()}, new int[]{Types.INTEGER, Types.BIGINT});
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
                        PreparedStatement ps = connection.prepareStatement(INSERT_MEDIA_PS);
                        ps.setString(1, movie.getTitle());
                        ps.setString(2, movie.getCoverImageUrl());
                        ps.setString(3, movie.getDescription());
                        ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
                        ps.setInt(5, movie.getAudienceRating());
                        return ps;
                    }, keyHolder);
                    movie.setId((long) keyHolder.getKey());
                    jdbcTemplate.update(INSERT_MEDIA_MOVIE_PS, new Object[]{movie.getId(), movie.getLength()}, new int[]{Types.BIGINT, Types.INTEGER});
                    insertGenrePivot(movie.getGenres(), movie.getId());
                    insertDirectorPivot(movie.getDirectors(), movie.getId());
                    insertWriterPivot(movie.getWriters(), movie.getId());
                    insertActorPivot(movie.getActings(), movie.getId());
                } else {
                    jdbcTemplate.update(UPDATE_MEDIA_PS, new Object[]{movie.getTitle(), Date.valueOf(movie.getReleaseDate()), movie.getCoverImageUrl(), movie.getDescription(), movie.getAudienceRating(), movie.getId()}, new int[]{Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BIGINT});
                    jdbcTemplate.update(UPDATE_MEDIA_MOVIE_PS, new Object[]{movie.getLength(), movie.getId()}, new int[]{Types.INTEGER, Types.BIGINT});
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

    //TODO: improve performance
    @Override
    public Optional<MovieJDBC> findById(Long id) throws DataAccessException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        MovieJDBC movie = jdbcTemplate.queryForObject(FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, movieRM);
        if (movie != null) {
            List<GenreJDBC> genres = jdbcTemplate.query(FIND_ALL_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT}, genreRM);
            List<CritiqueJDBC> critiques = jdbcTemplate.query(FIND_ALL_CRITIQUES_PS, new Object[]{id}, new int[]{Types.BIGINT}, critiqueRM);
            List<DirectorJDBC> directors = jdbcTemplate.query(FIND_ALL_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, directorRM);
            List<WriterJDBC> writers = jdbcTemplate.query(FIND_ALL_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT}, writerRM);
            List<ActorJDBC> actors = jdbcTemplate.query(FIND_ALL_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, actorRM);
            List<ActingJDBC> actings = new ArrayList<>();
            for (ActorJDBC actor : actors) {
                ActingJDBC acting = new ActingJDBC();
                acting.setActor(actor);
                List<ActingRoleJDBC> roles = jdbcTemplate.query(FIND_ALL_ACTING_ROLES_PS, new Object[]{id, actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, actingRoleRM);
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
        Long dbId = jdbcTemplate.queryForObject(FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
        return dbId != null;
    }

    @Override
    public List<MovieJDBC> findAll() {
        List<MovieJDBC> movies = jdbcTemplate.query(FIND_ALL_S, movieRM);
        for (MovieJDBC movie : movies) {
            List<GenreJDBC> genres = jdbcTemplate.query(FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, genreRM);
            List<CritiqueJDBC> critiques = jdbcTemplate.query(FIND_ALL_CRITIQUES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, critiqueRM);
            List<DirectorJDBC> directors = jdbcTemplate.query(FIND_ALL_DIRECTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, directorRM);
            List<WriterJDBC> writers = jdbcTemplate.query(FIND_ALL_WRITERS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, writerRM);
            List<ActorJDBC> actors = jdbcTemplate.query(FIND_ALL_ACTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, actorRM);
            List<ActingJDBC> actings = new ArrayList<>();
            for (ActorJDBC actor : actors) {
                ActingJDBC acting = new ActingJDBC();
                acting.setActor(actor);
                List<ActingRoleJDBC> roles = jdbcTemplate.query(FIND_ALL_ACTING_ROLES_PS, new Object[]{movie.getId(), actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, actingRoleRM);
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
            MovieJDBC movie = jdbcTemplate.queryForObject(FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, movieRM);
            if (movie != null) {
                List<GenreJDBC> genres = jdbcTemplate.query(FIND_ALL_GENRES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, genreRM);
                List<CritiqueJDBC> critiques = jdbcTemplate.query(FIND_ALL_CRITIQUES_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, critiqueRM);
                List<DirectorJDBC> directors = jdbcTemplate.query(FIND_ALL_DIRECTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, directorRM);
                List<WriterJDBC> writers = jdbcTemplate.query(FIND_ALL_WRITERS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, writerRM);
                List<ActorJDBC> actors = jdbcTemplate.query(FIND_ALL_ACTORS_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT}, actorRM);
                List<ActingJDBC> actings = new ArrayList<>();
                for (ActorJDBC actor : actors) {
                    ActingJDBC acting = new ActingJDBC();
                    acting.setActor(actor);
                    List<ActingRoleJDBC> roles = jdbcTemplate.query(FIND_ALL_ACTING_ROLES_PS, new Object[]{movie.getId(), actor.getId()}, new int[]{Types.BIGINT, Types.BIGINT}, actingRoleRM);
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
        return jdbcTemplate.queryForObject(COUNT_S, Long.class);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_MEDIA_PS, new Object[]{id}, new int[]{Types.BIGINT});
    }

    @Override
    public void delete(MovieJDBC movie) {
        jdbcTemplate.update(DELETE_MEDIA_PS, new Object[]{movie.getId()}, new int[]{Types.BIGINT});
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        List<Object[]> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(new Object[]{id});
        }
        jdbcTemplate.batchUpdate(DELETE_MEDIA_PS, list, new int[]{Types.BIGINT});
    }

    @Override
    public void deleteAll(List<MovieJDBC> movies) {
        List<Object[]> list = new ArrayList<>();
        for (MovieJDBC movie : movies) {
            list.add(new Object[]{movie.getId()});
        }
        jdbcTemplate.batchUpdate(DELETE_MEDIA_PS, list, new int[]{Types.BIGINT});
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_MEDIA_S);
    }
//-----------------------------------------------------------------------------------------------------

    private void updateGenre(List<GenreJDBC> genres, Long id) {
        jdbcTemplate.update(DELETE_ALL_MOVIE_GENRES_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertGenrePivot(genres, id);
    }

    private void updateDirector(List<DirectorJDBC> directors, Long id) {
        jdbcTemplate.update(DELETE_ALL_MOVIE_DIRECTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertDirectorPivot(directors, id);
    }

    private void updateWriter(List<WriterJDBC> writers, Long id) {
        jdbcTemplate.update(DELETE_ALL_MOVIE_WRITERS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertWriterPivot(writers, id);
    }

    private void updateActors(List<ActingJDBC> actings, Long id) {
        jdbcTemplate.update(DELETE_ALL_MOVIE_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT});
        insertActorPivot(actings, id);
    }

    private void insertGenrePivot(List<GenreJDBC> genres, Long primaryKey) {
        jdbcTemplate.batchUpdate(INSERT_MEDIA_GENRE_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(INSERT_MEDIA_DIRECTOR_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(INSERT_MEDIA_WRITERS_PS, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(INSERT_MEDIA_ACTINGS_PS, new BatchPreparedStatementSetter() {
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
