/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.transformers;

import com.borak.cwb.backend.domain.dto.country.CountryResponseDTO;
import com.borak.cwb.backend.domain.jdbc.CountryJDBC;
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

    public CountryResponseDTO jdbcToCountryResponse(CountryJDBC country) {
        CountryResponseDTO response = new CountryResponseDTO();
        response.setId(country.getId());
        response.setName(country.getName());
        response.setOfficialStateName(country.getOfficialStateName());
        response.setCode(country.getCode());
        return response;
    }

    public CountryResponseDTO jpaToCountryResponse(CountryJPA country) {
        CountryResponseDTO response = new CountryResponseDTO();
        response.setId(country.getId());
        response.setName(country.getName());
        response.setOfficialStateName(country.getOfficialStateName());
        response.setCode(country.getCode());
        return response;
    }
//=================================================================================================================================

    public List<CountryResponseDTO> jdbcToCountryResponse(List<CountryJDBC> countries) {
        List<CountryResponseDTO> list = new ArrayList<>(countries.size());
        for (CountryJDBC country : countries) {
            list.add(jdbcToCountryResponse(country));
        }
        return list;
    }

    public List<CountryResponseDTO> jpaToCountryResponse(List<CountryJPA> countries) {
        List<CountryResponseDTO> list = new ArrayList<>(countries.size());
        for (CountryJPA country : countries) {
            list.add(jpaToCountryResponse(country));
        }
        return list;
    }

}
