/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.domain.dto.country.CountryResponseDTO;
import com.borak.cwb.backend.domain.jdbc.classes.CountryJDBC;
import com.borak.cwb.backend.domain.jpa.CountryJPA;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CountryTransformer {

    public CountryResponseDTO toResponseFromJDBC(CountryJDBC jdbc) {
        if (jdbc == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        CountryResponseDTO response = new CountryResponseDTO();
        response.setId(jdbc.getId());
        response.setName(jdbc.getName());
        response.setOfficialStateName(jdbc.getOfficialStateName());
        response.setCode(jdbc.getCode());
        return response;
    }

    public CountryResponseDTO toResponseFromJPA(CountryJPA jpa) {
        if (jpa == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        CountryResponseDTO response = new CountryResponseDTO();
        response.setId(jpa.getId());
        response.setName(jpa.getName());
        response.setOfficialStateName(jpa.getOfficialStateName());
        response.setCode(jpa.getCode());
        return response;
    }

    public List<CountryResponseDTO> toResponseFromJDBC(List<CountryJDBC> jdbcList) {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<CountryResponseDTO> list = new ArrayList<>();
        for (CountryJDBC jd : jdbcList) {
            list.add(toResponseFromJDBC(jd));
        }
        return list;
    }

    public List<CountryResponseDTO> toResponseFromJPA(List<CountryJPA> jpaList) {
        if (jpaList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<CountryResponseDTO> list = new ArrayList<>();
        for (CountryJPA jd : jpaList) {
            list.add(toResponseFromJPA(jd));
        }
        return list;
    }

}
