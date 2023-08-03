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
import com.borak.kinweb.backend.repository.intf.IMovieRepository;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
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
//================================================================================================

    private RowMapper<MovieJDBC> movieRM = (rs, num) -> {
        MovieJDBC movie = new MovieJDBC();
        movie.setId(rs.getLong("id"));
        movie.setTitle(rs.getString("title"));
        movie.setCoverImageUrl(rs.getString("cover_image_url"));
        movie.setDescription(rs.getString("description"));
        movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
        movie.setAudienceRating(rs.getInt("audience_rating"));
        movie.setCriticRating(rs.getInt("critic_rating"));
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
    @Override
    public MovieJDBC save(MovieJDBC entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<MovieJDBC> saveAll(List<MovieJDBC> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<MovieJDBC> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(MovieJDBC entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll(List<MovieJDBC> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
