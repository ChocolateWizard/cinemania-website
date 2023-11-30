/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLActor {

    public static final String FIND_ID_PS = """
                                      SELECT person_id 
                                      FROM actor 
                                      WHERE person_id=?;
                                       """;
    public static final String FIND_ALL_BY_MEDIA_PS = """
                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo,actor.is_star  
                                      FROM person JOIN actor ON(person.id=actor.person_id) JOIN acting ON(actor.person_id=acting.actor_id) 
                                      WHERE acting.media_id=?;
                                       """;

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
}
