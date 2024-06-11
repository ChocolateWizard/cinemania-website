/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.sql;

import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.WriterJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public class SQLPersonWrapper {

    public static final String FIND_ALL_PAGINATED_PS = """
                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo,director.person_id AS director_id,writer.person_id AS writer_id,actor.person_id AS actor_id,actor.is_star 
                                      FROM person 
                                      LEFT JOIN director ON(person.id=director.person_id) 
                                      LEFT JOIN writer ON(person.id=writer.person_id) 
                                      LEFT JOIN actor ON(person.id=actor.person_id) LIMIT ? OFFSET ?;
                                            """;
    public static final String FIND_ALL_MEDIA_ID_BY_DIRECTOR_PS = """
                                      SELECT media_id 
                                      FROM media_directors 
                                      WHERE director_id=?;
                                            """;
    public static final String FIND_ALL_MEDIA_ID_BY_WRITER_PS = """
                                      SELECT media_id 
                                      FROM media_writers 
                                      WHERE writer_id=?;
                                            """;
    public static final String FIND_ALL_ACTING_BY_ACTOR_PS = """
                                      SELECT media_id,is_starring 
                                      FROM acting 
                                      WHERE actor_id=?;
                                            """;
    public static final String FIND_ALL_ACTING_ROLES_PS = """
                                      SELECT acting_role.id,acting_role.name 
                                      FROM acting_role 
                                      WHERE acting_role.acting_id=(SELECT acting.id FROM acting WHERE acting.media_id=? AND acting.actor_id=?);
                                            """;
    public static final String FIND_BY_ID_PS = """
                                      SELECT person.id,person.first_name,person.last_name,person.gender,person.profile_photo,director.person_id AS director_id,writer.person_id AS writer_id,actor.person_id AS actor_id,actor.is_star 
                                      FROM person 
                                      LEFT JOIN director ON(person.id=director.person_id) 
                                      LEFT JOIN writer ON(person.id=writer.person_id) 
                                      LEFT JOIN actor ON(person.id=actor.person_id) WHERE person.id=?;
                                            """;
    public static final String INSERT_PERSON_PS = """
                                      INSERT INTO person(first_name,last_name,gender,profile_photo) 
                                      VALUES(?,?,?,?);
                                            """;
    public static final String INSERT_PERSON_DIRECTOR_PS = """
                                      INSERT INTO director(person_id) 
                                      VALUES(?);
                                            """;
    public static final String INSERT_PERSON_WRITER_PS = """
                                      INSERT INTO writer(person_id) 
                                      VALUES(?);
                                            """;
    public static final String INSERT_PERSON_ACTOR_PS = """
                                      INSERT INTO actor(person_id,is_star) 
                                      VALUES(?,?);
                                            """;
    public static final String INSERT_DIRECTOR_MEDIA_PS = """
                                      INSERT INTO media_directors(media_id,director_id) 
                                      VALUES(?,?);
                                            """;
    public static final String INSERT_WRITER_MEDIA_PS = """
                                      INSERT INTO media_writers(media_id,writer_id) 
                                      VALUES(?,?);
                                            """;
    public static final String INSERT_ACTOR_ACTING_PS = """
                                      INSERT INTO acting(media_id,actor_id,is_starring) 
                                      VALUES(?,?,?);
                                            """;
    public static final String INSERT_ACTOR_ACTING_ROLES_PS = """
                                       INSERT INTO acting_role(acting_id,id,name) 
                                       VALUES((SELECT acting.id FROM acting WHERE acting.media_id=? AND acting.actor_id=?),?,?);
                                            """;
    public static final String UPDATE_PERSON_PS = """
                                       UPDATE person 
                                       SET first_name = ?,last_name = ?,gender = ?,profile_photo=? 
                                       WHERE id=?;
                                       """;
    public static final String DELETE_PERSON_DIRECTOR_PS = """
                                       DELETE FROM director WHERE person_id=?;
                                       """;
    public static final String DELETE_PERSON_WRITER_PS = """
                                       DELETE FROM writer WHERE person_id=?;
                                       """;
    public static final String DELETE_PERSON_ACTOR_PS = """
                                       DELETE FROM actor WHERE person_id=?;
                                       """;

    public static final RowMapper<PersonWrapperJDBC> personWrapperRM = (rs, num) -> {
        PersonJDBC person = new PersonJDBC();
        DirectorJDBC director = null;
        WriterJDBC writer = null;
        ActorJDBC actor = null;
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setGender(Gender.parseGender(rs.getString("gender")));
        person.setProfilePhoto(rs.getString("profile_photo"));
        Long directorId = rs.getObject("director_id", Long.class);
        Long writerId = rs.getObject("writer_id", Long.class);
        Long actorId = rs.getObject("actor_id", Long.class);
        if (directorId != null) {
            director = new DirectorJDBC(directorId);
        }
        if (writerId != null) {
            writer = new WriterJDBC(writerId);
        }
        if (actorId != null) {
            actor = new ActorJDBC(actorId);
            actor.setStar(rs.getBoolean("is_star"));
        }
        return new PersonWrapperJDBC(person, director, writer, actor);
    };

    public static final RowMapper<MediaJDBC> mediaRM = (rs, num) -> {
        return new MediaJDBC(rs.getLong("media_id"));
    };

    public static final RowMapper<ActingJDBC> actingRM = (rs, num) -> {
        return new ActingJDBC(new MediaJDBC(rs.getLong("media_id")), rs.getBoolean("is_starring"));
    };

    public static final RowMapper<ActingRoleJDBC> actingRoleRM = (rs, num) -> {
        ActingRoleJDBC role = new ActingRoleJDBC();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        return role;
    };

}
