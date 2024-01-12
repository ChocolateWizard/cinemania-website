/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

import com.borak.kinweb.backend.domain.jdbc.classes.CountryJDBC;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public class SQLCountry {

    public static final String FIND_ID_PS = """
                                       SELECT id 
                                       FROM country 
                                       WHERE id=?;
                                       """;

    public static final String FIND_ALL_S = """
                                       SELECT id,name,official_state_name,code  
                                       FROM country;                                     
                                       """;

    public static final RowMapper<CountryJDBC> countryRM = (rs, num) -> {
        return new CountryJDBC(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("official_state_name"),
                rs.getString("code"));
    };

}
