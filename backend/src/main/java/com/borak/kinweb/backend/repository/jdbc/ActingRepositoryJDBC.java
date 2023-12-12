/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.api.IActingRepository;
import com.borak.kinweb.backend.repository.sql.SQLActing;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class ActingRepositoryJDBC implements IActingRepository<ActingJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ActingJDBC insert(ActingJDBC entity) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(ActingJDBC entity) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<ActingJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ActingJDBC> findAll() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ActingJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long count() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ActingJDBC> findAllByMediaId(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            List<ActingJDBC> actings = jdbcTemplate.query(SQLActing.FIND_ALL_ACTING_ACTORS_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLActing.actingActorRM);
            for (ActingJDBC acting : actings) {
                List<ActingRoleJDBC> roles = jdbcTemplate.query(SQLActing.FIND_ALL_ACTING_ROLES_PS, new Object[]{id, acting.getActor().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLActing.actingRoleRM);
                for (ActingRoleJDBC role : roles) {
                    role.setActing(acting);
                }
                acting.setRoles(roles);
            }
            return actings;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving actors and their roles of movie with id: " + id, ex);
        }
    }

    @Override
    public List<ActingJDBC> insertAll(List<ActingJDBC> entities) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
