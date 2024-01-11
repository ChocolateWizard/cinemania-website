/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.domain.jdbc.classes.CountryJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLUser {

    public static final String FIND_BY_USERNAME_PS = """
                                      SELECT user.id,user.first_name,user.last_name,user.gender,user.profile_name,user.profile_image,
                                       user.username,user.email,user.password,user.role,user.created_at,user.updated_at,
                                       user.country_id,country.name,country.official_state_name,country.code 
                                       FROM USER JOIN country ON(user.country_id=country.id) 
                                       WHERE user.username=?;                                                    
                                       """;

    public static final String FIND_BY_ID_PS = """
                                      SELECT user.id,user.first_name,user.last_name,user.gender,user.profile_name,user.profile_image,
                                       user.username,user.email,user.password,user.role,user.created_at,user.updated_at,
                                       user.country_id,country.name,country.official_state_name,country.code 
                                       FROM USER JOIN country ON(user.country_id=country.id) 
                                       WHERE user.id=?;                                                    
                                       """;

    public static final String FIND_ALL_MEDIA_BY_USER_ID_PS = """
                                      SELECT media.id,media.title,media.release_date,media.cover_image,media.description,media.audience_rating,
                                      media.critic_rating,movie.media_id AS movie_id,tv_show.media_id AS tv_show_id 
                                      FROM user_media JOIN media ON(user_media.media_id=media.id) LEFT JOIN movie ON(media.id=movie.media_id) LEFT JOIN tv_show ON(media.id=tv_show.media_id) 
                                      WHERE user_media.user_id=?;                                                    
                                       """;

    public static final String FIND_ALL_CRITIQUES_BY_USER_ID_PS = """
                                      SELECT critique.description AS critique_description,critique.rating,
                                      media.id,media.title,media.release_date,
                                      media.cover_image,media.description AS media_description,media.audience_rating,
                                      media.critic_rating,movie.media_id AS movie_id,tv_show.media_id AS tv_show_id 
                                      FROM critique JOIN media ON(critique.media_id=media.id) LEFT JOIN movie ON(media.id=movie.media_id) LEFT JOIN tv_show ON(media.id=tv_show.media_id) 
                                      WHERE critique.user_critic_id=?;                                                      
                                       """;

    public static final String FIND_ALL_GENRES_PS = """
                                       SELECT genre.id,genre.name 
                                       FROM genre JOIN media_genres ON(genre.id=media_genres.genre_id) 
                                       WHERE media_genres.media_id=?;
                                       """;

    public static final String FIND_ID_BY_USERNAME_PS = """
                                                      SELECT id 
                                                      FROM user 
                                                      WHERE username=?;                                                    
                                                      """;

    public static final String FIND_ID_BY_EMAIL_PS = """
                                                      SELECT id
                                                      FROM user
                                                      WHERE email=?;                                                
                                                      """;

    public static final String FIND_ID_BY_PROFILE_NAME_PS = """                                                    
                                                     SELECT id
                                                     FROM user
                                                     WHERE profile_name=?;
                                                      """;

    public static final String INSERT_USER_PS = """                                                    
                                         INSERT INTO USER(first_name,last_name,gender,profile_name,
                                                profile_image,username,email,password,role,created_at,updated_at,country_id) 
                                                VALUES(?,?,?,?,?,?,?,?,?,?,?,?);                                                 
                                                """;

    public static final String INSERT_MEDIA_PIVOT_PS = """                                                    
                                         INSERT INTO user_media(user_id,media_id) 
                                          VALUES(?,?);                                                 
                                                """;
    public static final String DELETE_MEDIA_PIVOT_PS = """                                                    
                                         DELETE FROM user_media 
                                         WHERE user_id=? AND media_id=?;                                                 
                                                """;
    public static final String EXISTS_MEDIA_IN_LIBRARY = """                                                    
                                         SELECT user_id 
                                          FROM user_media 
                                          WHERE user_id=? AND media_id=?;                                            
                                                """;

    public static final RowMapper<UserJDBC> userRM = (rs, num) -> {
        UserJDBC user = new UserJDBC();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setProfileName(rs.getString("profile_name"));
        user.setProfileImage(rs.getString("profile_image"));
        user.setGender(Gender.parseGender(rs.getString("gender")));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(UserRole.parseUserRole(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        user.setCountry(new CountryJDBC(rs.getLong("country_id"), rs.getString("name"), rs.getString("official_state_name"), rs.getString("code")));

        return user;
    };

    public static final RowMapper<MediaJDBC> mediaRM = (rs, num) -> {
        Long movieID = rs.getObject("movie_id", Long.class);
        Long tvShowID = rs.getObject("tv_show_id", Long.class);
        if (movieID != null) {
            MovieJDBC movie = new MovieJDBC();
            movie.setId(rs.getLong("id"));
            movie.setTitle(rs.getString("title"));
            movie.setCoverImage(rs.getString("cover_image"));
            movie.setDescription(rs.getString("description"));
            movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
            movie.setAudienceRating(rs.getInt("audience_rating"));
            movie.setCriticRating(rs.getObject("critic_rating", Integer.class));
            return movie;
        }
        if (tvShowID != null) {
            TVShowJDBC tvShow = new TVShowJDBC();
            tvShow.setId(rs.getLong("id"));
            tvShow.setTitle(rs.getString("title"));
            tvShow.setCoverImage(rs.getString("cover_image"));
            tvShow.setDescription(rs.getString("description"));
            tvShow.setReleaseDate(rs.getDate("release_date").toLocalDate());
            tvShow.setAudienceRating(rs.getInt("audience_rating"));
            tvShow.setCriticRating(rs.getObject("critic_rating", Integer.class));
            return tvShow;
        }
        MediaJDBC media = new MediaJDBC();
        media.setId(rs.getLong("id"));
        media.setTitle(rs.getString("title"));
        media.setCoverImage(rs.getString("cover_image"));
        media.setDescription(rs.getString("description"));
        media.setReleaseDate(rs.getDate("release_date").toLocalDate());
        media.setAudienceRating(rs.getInt("audience_rating"));
        media.setCriticRating(rs.getObject("critic_rating", Integer.class));
        return media;

    };

    public static final RowMapper<CritiqueJDBC> critiqueRM = (rs, num) -> {
        CritiqueJDBC critique = new CritiqueJDBC();
        critique.setDescription(rs.getString("critique_description"));
        critique.setRating(rs.getInt("rating"));

        Long movieID = rs.getObject("movie_id", Long.class);
        Long tvShowID = rs.getObject("tv_show_id", Long.class);
        MediaJDBC media;
        if (movieID != null) {
            media = new MovieJDBC();
            media.setId(rs.getLong("id"));
            media.setTitle(rs.getString("title"));
            media.setCoverImage(rs.getString("cover_image"));
            media.setDescription(rs.getString("media_description"));
            media.setReleaseDate(rs.getDate("release_date").toLocalDate());
            media.setAudienceRating(rs.getInt("audience_rating"));
            media.setCriticRating(rs.getObject("critic_rating", Integer.class));
        } else if (tvShowID != null) {
            media = new TVShowJDBC();
            media.setId(rs.getLong("id"));
            media.setTitle(rs.getString("title"));
            media.setCoverImage(rs.getString("cover_image"));
            media.setDescription(rs.getString("media_description"));
            media.setReleaseDate(rs.getDate("release_date").toLocalDate());
            media.setAudienceRating(rs.getInt("audience_rating"));
            media.setCriticRating(rs.getObject("critic_rating", Integer.class));
        } else {
            media = new MediaJDBC();
            media.setId(rs.getLong("id"));
            media.setTitle(rs.getString("title"));
            media.setCoverImage(rs.getString("cover_image"));
            media.setDescription(rs.getString("media_description"));
            media.setReleaseDate(rs.getDate("release_date").toLocalDate());
            media.setAudienceRating(rs.getInt("audience_rating"));
            media.setCriticRating(rs.getObject("critic_rating", Integer.class));
        }
        critique.setMedia(media);
        return critique;

    };

    public static final RowMapper<GenreJDBC> genreRM = (rs, num) -> {
        GenreJDBC genre = new GenreJDBC();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

}
