/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.query;

import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.jdbc.PersonJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public class SQLPerson {

    public static final String FIND_ALL_PAGINATED_PS = """
                                       SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo 
                                       FROM person LIMIT ? OFFSET ?;
                                       """;

    public static final String FIND_ID_PS = """
                                          SELECT id 
                                          FROM person 
                                          WHERE id=?;
                                          """;

    public static final String FIND_BY_ID_PS = """
                                       SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo 
                                       FROM person 
                                       WHERE person.id=?;
                                       """;
    public static final String FIND_BY_ID_PROFILE_PHOTO_PS = """
                                       SELECT profile_photo  
                                       FROM person 
                                       WHERE id=?;
                                       """;

    public static final String DELETE_PERSON_PS = """
                                       DELETE FROM person WHERE person.id=?;
                                       """;
    public static final String UPDATE_PERSON_PROFILE_PHOTO_PS = """
                                       UPDATE person 
                                       SET profile_photo = ? 
                                       WHERE id=?;
                                       """;

    public static final RowMapper<PersonJDBC> personRM = (rs, num) -> {
        PersonJDBC person = new PersonJDBC();
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setGender(Gender.parseGender(rs.getString("gender")));
        person.setProfilePhoto(rs.getString("profile_photo"));
        return person;
    };

}
