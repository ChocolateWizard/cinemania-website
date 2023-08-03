/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import com.borak.kinweb.backend.domain.jpa.classes.CountryJPA;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;
import com.borak.kinweb.backend.domain.jdbc.classes.CountryJDBC;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.borak.kinweb.backend.domain.jdbc.classes.JDBC;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CountryTransformer implements GenericTransformer<CountryDTO, CountryJDBC, CountryJPA> {

    @Override
    public CountryDTO jdbcToDto(CountryJDBC jdbc) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CountryDTO jpaToDto(CountryJPA jpa) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CountryDTO> jdbcToDto(List<CountryJDBC> jdbcList) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CountryDTO> jpaToDto(List<CountryJPA> jpaList) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

}
