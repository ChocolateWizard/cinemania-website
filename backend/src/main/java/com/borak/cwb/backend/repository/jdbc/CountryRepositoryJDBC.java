/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.jdbc;

import com.borak.cwb.backend.domain.jdbc.CountryJDBC;
import com.borak.cwb.backend.exceptions.DatabaseException;
import com.borak.cwb.backend.repository.api.ICountryRepository;
import com.borak.cwb.backend.repository.query.SQLCountry;
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
public class CountryRepositoryJDBC implements ICountryRepository<CountryJDBC, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public CountryJDBC insert(CountryJDBC entity) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(CountryJDBC entity) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<CountryJDBC> findById(Long id) throws DatabaseException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) throws DatabaseException, IllegalArgumentException {
        try {
            if (id == null || id < 1) {
                throw new IllegalArgumentException("Invalid parameter: id must be non-null and greater than 0");
            }
            jdbcTemplate.queryForObject(SQLCountry.FIND_ID_PS, new Object[]{id}, new int[]{Types.BIGINT}, Long.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while checking if country with id: " + id + " exists", e);
        }
    }

    @Override
    public List<CountryJDBC> findAll() throws DatabaseException {
        try {
            List<CountryJDBC> countries = jdbcTemplate.query(SQLCountry.FIND_ALL_S, SQLCountry.countryRM);
            return countries;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while retreiving countries", e);
        }
    }

    @Override
    public List<CountryJDBC> findAllPaginated(int page, int size) throws DatabaseException, IllegalArgumentException {
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
