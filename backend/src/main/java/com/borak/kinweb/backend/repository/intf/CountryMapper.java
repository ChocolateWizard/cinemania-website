/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.intf;

import com.borak.kinweb.backend.domain.pojo.classes.CountryPOJO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mr. Poyo
 */
public class CountryMapper implements RowMapper<CountryPOJO> {

    @Override
    public CountryPOJO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CountryPOJO c = new CountryPOJO();
        c.setId(rs.getLong(1));
        c.setName(rs.getString(2));
        c.setOfficialStateName(rs.getString(3));
        c.setCode(rs.getString(4));
        return c;
    }

}
