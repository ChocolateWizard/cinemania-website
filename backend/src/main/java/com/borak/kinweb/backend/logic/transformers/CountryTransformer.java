/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.country.CountryResponseDTO;
import com.borak.kinweb.backend.domain.jdbc.classes.CountryJDBC;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CountryTransformer {

    public CountryResponseDTO toCountryResponseDTO(CountryJDBC jdbc) {
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

    public List<CountryResponseDTO> toCountryResponseDTO(List<CountryJDBC> jdbcList) {
        if (jdbcList == null) {
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<CountryResponseDTO> list = new ArrayList<>();
        for (CountryJDBC jd : jdbcList) {
            list.add(toCountryResponseDTO(jd));
        }
        return list;
    }

}
