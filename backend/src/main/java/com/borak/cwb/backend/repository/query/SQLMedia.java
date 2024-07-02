/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.query;

import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.MovieJDBC;
import com.borak.cwb.backend.domain.jdbc.TVShowJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLMedia {

    public static final String FIND_ID_PS = """
                                       SELECT id 
                                       FROM media 
                                       WHERE id=?;
                                       """;

    public static final String FIND_ALL_BY_TITLE_PAGINATED_PS = """
                                       SELECT media.id,media.title,media.release_date,media.cover_image,media.description,media.audience_rating,media.critic_rating,movie.media_id AS movie_id,movie.length,tv_show.media_id AS tv_show_id,tv_show.number_of_seasons 
                                       FROM media LEFT JOIN movie ON(media.id=movie.media_id) LEFT JOIN tv_show ON(media.id=tv_show.media_id) 
                                       WHERE title LIKE ? LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ALL_GENRES_PS = """
                                       SELECT genre.id,genre.name 
                                       FROM genre JOIN media_genres ON(genre.id=media_genres.genre_id) 
                                       WHERE media_genres.media_id=?;
                                       """;

//================================================================================================
//========================================RowMappers==============================================
//================================================================================================
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
            movie.setLength(rs.getInt("length"));
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
            tvShow.setNumberOfSeasons(rs.getInt("number_of_seasons"));
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
    public static final RowMapper<GenreJDBC> genreRM = (rs, num) -> {
        GenreJDBC genre = new GenreJDBC();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

}
