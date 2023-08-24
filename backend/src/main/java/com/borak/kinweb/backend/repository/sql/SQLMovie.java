/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class SQLMovie {

//================================================================================================
//========================================QUERIES=================================================
//================================================================================================
    public static final String INSERT_MEDIA_PS = """
                                       INSERT INTO media(title,cover_image,description,release_date,audience_rating) 
                                       VALUES(?,?,?,?,?);
                                       """;
    public static final String INSERT_MEDIA_MOVIE_PS = """
                                       INSERT INTO movie(media_id,length) 
                                       VALUES(?,?);
                                       """;
    public static final String INSERT_MEDIA_GENRE_PS = """
                                       INSERT INTO media_genres(media_id,genre_id) 
                                       VALUES(?,?);
                                       """;
    public static final String INSERT_MEDIA_DIRECTOR_PS = """
                                       INSERT INTO media_directors(media_id,director_id) 
                                       VALUES(?,?);
                                       """;
    public static final String INSERT_MEDIA_WRITERS_PS = """
                                       INSERT INTO media_writers(media_id,writer_id) 
                                       VALUES(?,?);
                                       """;
    public static final String INSERT_MEDIA_ACTINGS_PS = """
                                       INSERT INTO acting(media_id,actor_id,is_starring) 
                                       VALUES(?,?,?);
                                       """;
    public static final String UPDATE_MEDIA_PS = """
                                       UPDATE media
                                       SET title = ?, release_date = ?, cover_image = ?,description = ?,audience_rating=?
                                       WHERE media.id=(SELECT movie.media_id FROM movie WHERE movie.media_id=?);
                                       """;
    public static final String UPDATE_MEDIA_MOVIE_PS = """
                                       UPDATE movie
                                       SET length = ?
                                       WHERE movie.media_id=?;
                                       """;

    public static final String FIND_ALL_S = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id);
                                       """;

    public static final String FIND_ALL_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id) LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_BY_RATING_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id) WHERE media.audience_rating>=? LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_BY_YEAR_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id) WHERE YEAR(media.release_date)>=? LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_GENRES_PS = """
                                                     SELECT genre.id,genre.name 
                                                     FROM genre JOIN media_genres ON(genre.id=media_genres.genre_id) 
                                                     WHERE media_genres.media_id=(SELECT movie.media_id FROM movie WHERE movie.media_id=?);
                                                     """;
    public static final String FIND_ALL_DIRECTORS_PS = """
                                                        SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo 
                                                        FROM media_directors JOIN director ON(media_directors.director_id=director.person_id) JOIN person ON(person.id=director.person_id) 
                                                        WHERE media_directors.media_id=(SELECT movie.media_id FROM movie WHERE movie.media_id=?);
                                                        """;
    public static final String FIND_ALL_WRITERS_PS = """
                                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo 
                                                      FROM media_writers JOIN writer ON(media_writers.writer_id=writer.person_id) JOIN person ON(person.id=writer.person_id) 
                                                      WHERE media_writers.media_id=(SELECT movie.media_id FROM movie WHERE movie.media_id=?);
                                                      """;
    public static final String FIND_ALL_ACTING_ACTORS_PS = """
                                                     SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo,actor.is_star,acting.is_starring  
                                                     FROM acting JOIN actor ON(acting.actor_id=actor.person_id) JOIN person ON(actor.person_id=person.id) 
                                                     WHERE acting.media_id=(SELECT movie.media_id FROM movie WHERE movie.media_id=?);
                                                     """;
    public static final String FIND_ALL_ACTING_ROLES_PS = """
                                                           SELECT acting_role.id,acting_role.name 
                                                           FROM acting_role 
                                                           WHERE acting_role.acting_media_id=? AND acting_role.acting_actor_id=?;
                                                           """;

    public static final String FIND_ALL_CRITIQUES_PS = """
                                                           SELECT user.username,user.profile_image_url,critique.description,critique.rating 
                                                           FROM critique JOIN user_critic ON(critique.user_critic_id=user_critic.user_id) JOIN USER ON(user_critic.user_id=user.id) 
                                                           WHERE media_id=?;
                                                           """;

    public static final String FIND_BY_ID_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,movie.length 
                                       FROM media JOIN movie ON(media.id=movie.media_id)
                                       WHERE movie.media_id=?;
                                       """;

    public static final String FIND_BY_ID_COVER_IMAGE_URL_PS = """
                                       SELECT media.cover_image 
                                       FROM media JOIN movie ON(media.id=movie.media_id)
                                       WHERE movie.media_id=?;
                                       """;

    public static final String FIND_ID_PS = """
                                       SELECT media_id 
                                       FROM movie 
                                       WHERE media_id=?;
                                       """;
    public static final String COUNT_S = """
                                       SELECT COUNT(media.id) 
                                       FROM media JOIN movie ON(media.id=movie.media_id);
                                       """;
    public static final String DELETE_MEDIA_PS = """
                                       DELETE FROM media WHERE media.id=(SELECT movie.media_id
                                       FROM movie
                                       WHERE movie.media_id=?);
                                       """;
    public static final String DELETE_ALL_MEDIA_S = """
                                       DELETE FROM media WHERE media.id=(SELECT movie.media_id
                                       FROM movie);
                                       """;
    public static final String DELETE_ALL_MOVIE_GENRES_PS = """
                                       DELETE FROM media_genres WHERE media_genres.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;
    public static final String DELETE_ALL_MOVIE_DIRECTORS_PS = """
                                       DELETE FROM media_directors WHERE media_directors.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;
    public static final String DELETE_ALL_MOVIE_WRITERS_PS = """
                                       DELETE FROM media_writers WHERE media_writers.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;
    public static final String DELETE_ALL_MOVIE_ACTORS_PS = """
                                       DELETE FROM acting WHERE acting.media_id=(SELECT movie.media_id
                                       FROM movie WHERE movie.media_id=?);
                                       """;

//================================================================================================
//========================================RowMappers==============================================
//================================================================================================
    public static final RowMapper<MovieJDBC> movieRM = (rs, num) -> {
        MovieJDBC movie = new MovieJDBC();
        movie.setId(rs.getLong("id"));
        movie.setTitle(rs.getString("title"));
        movie.setCoverImage(rs.getString("cover_image"));
        movie.setDescription(rs.getString("description"));
        movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
        movie.setAudienceRating(rs.getInt("audience_rating"));
        movie.setCriticRating(rs.getObject("critic_rating", Integer.class));
        movie.setLength(rs.getInt("length"));
        return movie;
    };
    public static final RowMapper<GenreJDBC> genreRM = (rs, num) -> {
        GenreJDBC genre = new GenreJDBC();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

    public static final RowMapper<DirectorJDBC> directorRM = (rs, num) -> {
        DirectorJDBC director = new DirectorJDBC();
        director.setId(rs.getLong("id"));
        director.setFirstName(rs.getString("first_name"));
        director.setLastName(rs.getString("last_name"));
        director.setGender(Gender.parseGender(rs.getString("gender")));
        director.setProfilePhoto(rs.getString("profile_photo"));
        return director;
    };

    public static final RowMapper<WriterJDBC> writerRM = (rs, num) -> {
        WriterJDBC writer = new WriterJDBC();
        writer.setId(rs.getLong("id"));
        writer.setFirstName(rs.getString("first_name"));
        writer.setLastName(rs.getString("last_name"));
        writer.setGender(Gender.parseGender(rs.getString("gender")));
        writer.setProfilePhoto(rs.getString("profile_photo"));
        return writer;
    };

    public static final RowMapper<ActorJDBC> actorRM = (rs, num) -> {
        ActorJDBC actor = new ActorJDBC();
        actor.setId(rs.getLong("id"));
        actor.setFirstName(rs.getString("first_name"));
        actor.setLastName(rs.getString("last_name"));
        actor.setGender(Gender.parseGender(rs.getString("gender")));
        actor.setProfilePhoto(rs.getString("profile_photo"));
        actor.setStar(rs.getBoolean("is_star"));
        return actor;
    };

    public static final RowMapper<ActingJDBC> actingActorRM = (rs, num) -> {
        ActingJDBC acting = new ActingJDBC();
        ActorJDBC actor = new ActorJDBC();
        actor.setId(rs.getLong("id"));
        actor.setFirstName(rs.getString("first_name"));
        actor.setLastName(rs.getString("last_name"));
        actor.setGender(Gender.parseGender(rs.getString("gender")));
        actor.setProfilePhoto(rs.getString("profile_photo"));
        actor.setStar(rs.getBoolean("is_star"));
        acting.setStarring(rs.getBoolean("is_starring"));
        acting.setActor(actor);
        return acting;
    };

    public static final RowMapper<ActingRoleJDBC> actingRoleRM = (rs, num) -> {
        ActingRoleJDBC role = new ActingRoleJDBC();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        return role;
    };

    public static final RowMapper<CritiqueJDBC> critiqueRM = (rs, num) -> {
        CritiqueJDBC critique = new CritiqueJDBC();
        UserCriticJDBC critic = new UserCriticJDBC();
        critic.setUsername(rs.getString("username"));
        critic.setProfileImage(rs.getString("profile_image"));
        critique.setCritic(critic);
        critique.setDescription(rs.getString("description"));
        critique.setRating(rs.getInt("rating"));
        return critique;
    };

}
