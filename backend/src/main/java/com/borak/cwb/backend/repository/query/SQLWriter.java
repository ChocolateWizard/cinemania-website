/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.query;

import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLWriter {

    public static final String FIND_ID_PS = """
                                      SELECT person_id 
                                      FROM writer 
                                      WHERE person_id=?;
                                       """;

    public static final String FIND_ALL_BY_MEDIA_PS = """
                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo  
                                      FROM person JOIN writer ON(person.id=writer.person_id) JOIN media_writers ON(writer.person_id=media_writers.writer_id) 
                                      WHERE media_writers.media_id=?;
                                       """;

    public static final RowMapper<WriterJDBC> writerRM = (rs, num) -> {
        WriterJDBC writer = new WriterJDBC();
        writer.setId(rs.getLong("id"));
        writer.setFirstName(rs.getString("first_name"));
        writer.setLastName(rs.getString("last_name"));
        writer.setGender(Gender.parseGender(rs.getString("gender")));
        writer.setProfilePhoto(rs.getString("profile_photo"));
        return writer;
    };

}
