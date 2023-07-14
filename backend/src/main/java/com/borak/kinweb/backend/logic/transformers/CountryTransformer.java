/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import com.borak.kinweb.backend.domain.pojo.classes.CountryPOJO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class CountryTransformer {

    public CountryDTO pojoToDto(CountryPOJO pojo) {
        return new CountryDTO(pojo.getId(), pojo.getName(), pojo.getOfficialStateName(), pojo.getCode());
    }

    public List<CountryDTO> pojoToDto(List<CountryPOJO> pojoList) {
        return pojoList.stream().map((pojo) -> {
            return new CountryDTO(pojo.getId(), pojo.getName(), pojo.getOfficialStateName(), pojo.getCode());
        }).collect(Collectors.toList());
    }

}
