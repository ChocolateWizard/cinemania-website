/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.query;

import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLTVShow {

    //================================================================================================
//========================================QUERIES=================================================
//================================================================================================
    public static final String INSERT_MEDIA_PS = """
                                       INSERT INTO media(title,cover_image,description,release_date,audience_rating) 
                                       VALUES(?,?,?,?,?);
                                       """;
    public static final String INSERT_MEDIA_TV_SHOW_PS = """
                                       INSERT INTO tv_show(media_id,number_of_seasons) VALUES(?,?);
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
    public static final String INSERT_MEDIA_ACTING_ROLES_PS = """
                                       INSERT INTO acting_role(acting_id,id,name) 
                                       VALUES((SELECT acting.id FROM acting WHERE acting.media_id=? AND acting.actor_id=?),?,?);
                                       """;
    public static final String UPDATE_MEDIA_PS = """
                                       UPDATE media
                                       SET title = ?, release_date = ?, cover_image = ?,description = ?,audience_rating=?
                                       WHERE media.id=(SELECT tv_show.media_id FROM tv_show WHERE tv_show.media_id=?);
                                       """;
    public static final String UPDATE_MEDIA_TV_SHOW_PS = """
                                       UPDATE tv_show
                                       SET number_of_seasons = ?
                                       WHERE tv_show.media_id=?;
                                       """;
    public static final String UPDATE_MEDIA_COVER_IMAGE_PS = """
                                       UPDATE media
                                       SET cover_image = ? 
                                       WHERE media.id=(SELECT tv_show.media_id FROM tv_show WHERE tv_show.media_id=?);
                                       """;

    public static final String FIND_ALL_S = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,tv_show.number_of_seasons 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id);
                                       """;

    public static final String FIND_ALL_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,tv_show.number_of_seasons 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id) LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_BY_RATING_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,tv_show.number_of_seasons 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id) WHERE media.audience_rating>=? ORDER BY media.audience_rating DESC LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_BY_YEAR_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,tv_show.number_of_seasons 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id) WHERE YEAR(media.release_date)>=? ORDER BY media.release_date ASC LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_GENRES_PS = """
                                                     SELECT genre.id,genre.name 
                                                     FROM genre JOIN media_genres ON(genre.id=media_genres.genre_id) 
                                                     WHERE media_genres.media_id=(SELECT tv_show.media_id FROM tv_show WHERE tv_show.media_id=?);
                                                     """;
    public static final String FIND_ALL_DIRECTORS_PS = """
                                                        SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo 
                                                        FROM media_directors JOIN director ON(media_directors.director_id=director.person_id) JOIN person ON(person.id=director.person_id) 
                                                        WHERE media_directors.media_id=(SELECT tv_show.media_id FROM tv_show WHERE tv_show.media_id=?);
                                                        """;
    public static final String FIND_ALL_WRITERS_PS = """
                                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo 
                                                      FROM media_writers JOIN writer ON(media_writers.writer_id=writer.person_id) JOIN person ON(person.id=writer.person_id) 
                                                      WHERE media_writers.media_id=(SELECT tv_show.media_id FROM tv_show WHERE tv_show.media_id=?);
                                                      """;
    public static final String FIND_ALL_ACTING_ACTORS_PS = """
                                                     SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo,actor.is_star,acting.is_starring  
                                                     FROM acting JOIN actor ON(acting.actor_id=actor.person_id) JOIN person ON(actor.person_id=person.id) 
                                                     WHERE acting.media_id=(SELECT tv_show.media_id FROM tv_show WHERE tv_show.media_id=?);
                                                     """;
    public static final String FIND_ALL_ACTING_ROLES_PS = """
                                                           SELECT acting_role.id,acting_role.name 
                                                           FROM acting_role 
                                                           WHERE acting_role.acting_id=(SELECT acting.id FROM acting WHERE acting.media_id=? AND acting.actor_id=?);
                                                           """;

    public static final String FIND_ALL_CRITIQUES_PS = """
                                                           SELECT user.profile_name,user.profile_image,critique.description,critique.rating 
                                                           FROM critique JOIN user ON(critique.user_critic_id=user.id) 
                                                           WHERE media_id=?;
                                                           """;

    public static final String FIND_BY_ID_PS = """
                                       SELECT media.id,media.title,media.cover_image,media.description,media.release_date,media.audience_rating,media.critic_rating,tv_show.number_of_seasons 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id)
                                       WHERE tv_show.media_id=?;
                                       """;

    public static final String FIND_BY_ID_COVER_IMAGE_PS = """
                                       SELECT media.cover_image 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id)
                                       WHERE tv_show.media_id=?;
                                       """;

    public static final String FIND_ID_PS = """
                                       SELECT media_id 
                                       FROM tv_show 
                                       WHERE media_id=?;
                                       """;
    public static final String COUNT_S = """
                                       SELECT COUNT(media.id) 
                                       FROM media JOIN tv_show ON(media.id=tv_show.media_id);
                                       """;
    public static final String DELETE_MEDIA_PS = """
                                       DELETE FROM media WHERE media.id=(SELECT tv_show.media_id
                                       FROM tv_show
                                       WHERE tv_show.media_id=?);
                                       """;
    public static final String DELETE_ALL_MEDIA_S = """
                                       DELETE FROM media WHERE media.id=(SELECT tv_show.media_id
                                       FROM tv_show);
                                       """;
    public static final String DELETE_ALL_TV_SHOW_GENRES_PS = """
                                       DELETE FROM media_genres WHERE media_genres.media_id=(SELECT tv_show.media_id
                                       FROM tv_show WHERE tv_show.media_id=?);
                                       """;
    public static final String DELETE_ALL_TV_SHOW_DIRECTORS_PS = """
                                       DELETE FROM media_directors WHERE media_directors.media_id=(SELECT tv_show.media_id
                                       FROM tv_show WHERE tv_show.media_id=?);
                                       """;
    public static final String DELETE_ALL_TV_SHOW_WRITERS_PS = """
                                       DELETE FROM media_writers WHERE media_writers.media_id=(SELECT tv_show.media_id
                                       FROM tv_show WHERE tv_show.media_id=?);
                                       """;
    public static final String DELETE_ALL_TV_SHOW_ACTORS_PS = """
                                       DELETE FROM acting WHERE acting.media_id=(SELECT tv_show.media_id
                                       FROM tv_show WHERE tv_show.media_id=?);
                                       """;

//================================================================================================
//========================================RowMappers==============================================
//================================================================================================
    public static final RowMapper<TVShowJDBC> tvShowRM = (rs, num) -> {
        TVShowJDBC tvShow = new TVShowJDBC();
        tvShow.setId(rs.getLong("id"));
        tvShow.setTitle(rs.getString("title"));
        tvShow.setCoverImage(rs.getString("cover_image"));
        tvShow.setDescription(rs.getString("description"));
        tvShow.setReleaseDate(rs.getDate("release_date").toLocalDate());
        tvShow.setAudienceRating(rs.getInt("audience_rating"));
        tvShow.setCriticRating(rs.getObject("critic_rating", Integer.class));
        tvShow.setNumberOfSeasons(rs.getInt("number_of_seasons"));
        return tvShow;
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
        UserJDBC critic = new UserJDBC();
        critic.setRole(UserRole.CRITIC);
        critic.setProfileName(rs.getString("profile_name"));
        critic.setProfileImage(rs.getString("profile_image"));
        critique.setCritic(critic);
        critique.setDescription(rs.getString("description"));
        critique.setRating(rs.getInt("rating"));
        return critique;
    };
}
