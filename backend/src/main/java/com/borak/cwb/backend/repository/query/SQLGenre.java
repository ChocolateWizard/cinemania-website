/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.query;

import com.borak.cwb.backend.domain.jdbc.GenreJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLGenre {

    public static final String FIND_ID_PS = """
                                       SELECT id 
                                       FROM genre 
                                       WHERE id=?;
                                       """;

    public static final RowMapper<GenreJDBC> genreRM = (rs, num) -> {
        GenreJDBC genre = new GenreJDBC();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

}
