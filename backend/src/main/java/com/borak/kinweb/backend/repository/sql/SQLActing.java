/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public final class SQLActing {

    public static final String FIND_ALL_ACTING_ACTORS_PS = """
                                                     SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo,actor.is_star,acting.is_starring  
                                                     FROM acting JOIN actor ON(acting.actor_id=actor.person_id) JOIN person ON(actor.person_id=person.id) 
                                                     WHERE acting.media_id=?;
                                                     """;
    public static final String FIND_ALL_ACTING_ROLES_PS = """
                                                     SELECT acting_role.id,acting_role.name 
                                                     FROM acting_role 
                                                     WHERE acting_role.acting_id=(SELECT acting.id FROM acting WHERE acting.media_id=? AND acting.actor_id=?);
                                                          """;

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

}
