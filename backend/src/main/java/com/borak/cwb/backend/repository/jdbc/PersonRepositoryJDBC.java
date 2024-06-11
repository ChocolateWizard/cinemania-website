/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.jdbc;

import com.borak.cwb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.cwb.backend.exceptions.DatabaseException;
import com.borak.cwb.backend.repository.api.IPersonRepository;
import com.borak.cwb.backend.repository.sql.SQLPerson;
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
public class PersonRepositoryJDBC implements IPersonRepository<PersonJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PersonJDBC insert(PersonJDBC entity) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(PersonJDBC entity) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<PersonJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            PersonJDBC person = jdbcTemplate.queryForObject(SQLPerson.FIND_BY_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, SQLPerson.personRM);
            return Optional.of(person);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while searching for person with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            jdbcTemplate.queryForObject(SQLPerson.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if person with id: " + id + " exists", e);
        }
    }

    @Override
    public List<PersonJDBC> findAll() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PersonJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
        try {
            if (page < 1 || size < 0) {
                throw new IllegalArgumentException("Invalid parameters: page must be greater than 0 and size must be non-negative");
            }
            int offset;
            try {
                offset = Math.multiplyExact(size, (page - 1));
            } catch (ArithmeticException e) {
                offset = Integer.MAX_VALUE;
            }
            List<PersonJDBC> persons = jdbcTemplate.query(SQLPerson.FIND_ALL_PAGINATED_PS, new Object[]{size, offset}, new int[]{Types.INTEGER, Types.INTEGER}, SQLPerson.personRM);
            return persons;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving persons", e);
        }
    }

    @Override
    public long count() throws DatabaseException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            int i = jdbcTemplate.update(SQLPerson.DELETE_PERSON_PS, new Object[]{id}, new int[]{Types.BIGINT});
            if (i <= 0) {
                throw new DatabaseException("Error while deleting person with id: " + id + ". No person found with given id");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while deleting person with id: " + id, e);
        }
    }

    @Override
    public void updateProfilePhoto(Long id, String profilePhoto) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            int i = jdbcTemplate.update(SQLPerson.UPDATE_PERSON_PROFILE_PHOTO_PS, new Object[]{profilePhoto, id}, new int[]{Types.VARCHAR, Types.BIGINT});
            if (i <= 0) {
                throw new DatabaseException("Error while updating profile photo for person with id: " + id + ". No person found with given id");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating profile photo for person with id: " + id, e);
        }
    }

    @Override
    public Optional<String> findByIdProfilePhoto(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            String profilePhoto = jdbcTemplate.queryForObject(SQLPerson.FIND_BY_ID_PROFILE_PHOTO_PS, new Object[]{id}, new int[]{Types.BIGINT}, String.class);
            return Optional.ofNullable(profilePhoto);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DatabaseException("No person found with given id: " + id, e);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error while retreiving person profile photo", ex);
        }
    }

}
