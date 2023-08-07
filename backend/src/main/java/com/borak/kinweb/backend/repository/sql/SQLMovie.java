/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLMovie {

    private static final String INSERT_MEDIA_PS = """
                                       INSERT INTO media(title,cover_image_url,description,release_date,audience_rating) 
                                       VALUES(?,?,?,?,?);
                                       """;
    private static final String INSERT_MEDIA_MOVIE_PS = """
                                       INSERT INTO movie(media_id) 
                                       VALUES(?);
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
                                       WHERE media.id=?;
                                       """;
    private static final String FIND_ID_PS = """
                                       SELECT media.id 
                                       FROM media JOIN movie ON(media.id=movie.media_id)
                                       WHERE media.id=?;
                                       """;
    private static final String COUNT_S = """
                                       SELECT COUNT(media.id) 
                                       FROM media JOIN movie ON(media.id=movie.media_id);
                                       """;
    private static final String DELETE_PS = """
                                       DELETE FROM media WHERE media.id=(SELECT movie.media_id
                                       FROM movie
                                       WHERE movie.media_id=?);
                                       """;
    private static final String DELETE_ALL_S = """
                                       DELETE FROM media WHERE media.id=(SELECT movie.media_id
                                       FROM movie);
                                       """;

}
