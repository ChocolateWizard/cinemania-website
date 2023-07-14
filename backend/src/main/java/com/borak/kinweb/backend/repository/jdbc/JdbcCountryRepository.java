/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.jdbc;

import com.borak.kinweb.backend.domain.enums.ExceptionType;
import com.borak.kinweb.backend.repository.intf.ICountryRepository;
import com.borak.kinweb.backend.domain.pojo.classes.CountryPOJO;
import com.borak.kinweb.backend.exceptions.ResponseException;
import com.borak.kinweb.backend.repository.intf.CountryMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mr. Poyo
 */
@Repository
public class JdbcCountryRepository implements ICountryRepository<CountryPOJO, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL = "select * from country";

    public JdbcCountryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CountryPOJO> getAll() throws ResponseException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new CountryMapper());
            
        } catch (Exception ex) {
            throw new ResponseException(ExceptionType.GET_ALL_COUNTRIES_ERROR, "Unable to retreive all countries");
        }
    }

}
