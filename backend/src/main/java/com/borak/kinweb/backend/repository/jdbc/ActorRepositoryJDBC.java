/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.api.IActorRepository;
import com.borak.kinweb.backend.repository.sql.SQLActor;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class ActorRepositoryJDBC implements IActorRepository<ActorJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ActorJDBC> findAllByMediaId(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            List<ActorJDBC> actors = jdbcTemplate.query(SQLActor.FIND_ALL_BY_MEDIA_PS, new Object[]{id}, new int[]{Types.INTEGER}, SQLActor.actorRM);
            return actors;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving actors of media with id: " + id, e);
        }
    }

    @Override
    public ActorJDBC insert(ActorJDBC entity) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(ActorJDBC entity) throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<ActorJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            jdbcTemplate.queryForObject(SQLActor.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if actor with id: " + id + " exists", e);
        }
    }

    @Override
    public List<ActorJDBC> findAll() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ActorJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
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

}
