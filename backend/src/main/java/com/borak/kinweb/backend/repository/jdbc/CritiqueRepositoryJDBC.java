/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.repository.api.ICritiqueRepository;
import com.borak.kinweb.backend.repository.sql.SQLCritique;
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
public class CritiqueRepositoryJDBC implements ICritiqueRepository<CritiqueJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean exists(CritiqueJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            validateIds(entity);
            jdbcTemplate.queryForObject(SQLCritique.FIND_ID_PS, new Object[]{entity.getCritic().getId(), entity.getMedia().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if critique with userId:" + entity.getCritic().getId() + ", and mediaId: " + entity.getMedia().getId() + " exists", e);
        }
    }

    @Override
    public Optional<CritiqueJDBC> find(CritiqueJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            validateIds(entity);
            CritiqueJDBC critique = jdbcTemplate.queryForObject(SQLCritique.FIND_BY_ID_PS, new Object[]{entity.getCritic().getId(), entity.getMedia().getId()}, new int[]{Types.BIGINT, Types.BIGINT}, SQLCritique.critiqueRM);
            return Optional.of(critique);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving critique with userId: " + entity.getCritic().getId() + ", and mediaId: " + entity.getMedia().getId(), e);
        }
    }

    @Override
    public CritiqueJDBC insert(CritiqueJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            validateIds(entity);
            performInsert(entity);
            return entity;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while inserting critique", e);
        }
    }

    @Override
    public void update(CritiqueJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            validateIds(entity);
            performUpdate(entity);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating critique", e);
        }
    }

    @Override
    public Optional<CritiqueJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CritiqueJDBC> findAll() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CritiqueJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
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
    public void delete(CritiqueJDBC entity) throws DatabaseException, IllegalArgumentException {
        try {
            validateIds(entity);
            int i = jdbcTemplate.update(SQLCritique.DELETE_CRITIQUE_PS, new Object[]{entity.getCritic().getId(), entity.getMedia().getId()}, new int[]{Types.BIGINT, Types.BIGINT});
            if (i < 1) {
                throw new DatabaseException("No critique found with userId: " + entity.getCritic().getId() + ", and mediaId: " + entity.getMedia().getId());
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting users critique for media with id: " + entity.getMedia().getId(), e);
        }
    }

//=====================================================================================================================
//=========================================PRIVATE METHODS=============================================================
//=====================================================================================================================
    private void validateIds(CritiqueJDBC entity) throws IllegalArgumentException {
        if (entity == null || entity.getCritic() == null || entity.getMedia() == null
                || entity.getCritic().getId() == null || entity.getCritic().getId() < 1
                || entity.getMedia().getId() == null || entity.getMedia().getId() < 1) {
            throw new IllegalArgumentException("Invalid parameter: critique, media and critic must not be null, and must have non-null ids greater than 0");
        }
    }

    private void performInsert(CritiqueJDBC critique) {
        jdbcTemplate.update(SQLCritique.INSERT_CRITIQUE_PS, new Object[]{critique.getCritic().getId(),
            critique.getMedia().getId(), critique.getDescription(), critique.getRating()}, new int[]{
            Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.INTEGER
        });
    }

    private void performUpdate(CritiqueJDBC critique) throws DatabaseException {
        int i = jdbcTemplate.update(SQLCritique.UPDATE_CRITIQUE_PS, new Object[]{
            critique.getDescription(), critique.getRating(), critique.getCritic().getId(),
            critique.getMedia().getId()}, new int[]{
            Types.VARCHAR, Types.INTEGER, Types.BIGINT, Types.BIGINT
        });
        if (i < 1) {
            throw new DatabaseException("No critique found with userId: " + critique.getCritic().getId() + ", and mediaId: " + critique.getMedia().getId());
        }

    }

}
