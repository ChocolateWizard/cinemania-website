/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLDirector {

    public static final String FIND_ID_PS = """
                                      SELECT person_id 
                                      FROM director 
                                      WHERE person_id=?;
                                       """;
    public static final String FIND_ALL_BY_MEDIA_PS = """
                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo  
                                      FROM person JOIN director ON(person.id=director.person_id) JOIN media_directors ON(director.person_id=media_directors.director_id) 
                                      WHERE media_directors.media_id=?;
                                       """;

    public static final RowMapper<DirectorJDBC> directorRM = (rs, num) -> {
        DirectorJDBC director = new DirectorJDBC();
        director.setId(rs.getLong("id"));
        director.setFirstName(rs.getString("first_name"));
        director.setLastName(rs.getString("last_name"));
        director.setGender(Gender.parseGender(rs.getString("gender")));
        director.setProfilePhoto(rs.getString("profile_photo"));
        return director;
    };

}
