/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.country;

import com.borak.kinweb.backend.repository.intf.ICountryRepository;
import com.borak.kinweb.backend.domain.dto.classes.CountryDTO;
import com.borak.kinweb.backend.domain.pojo.classes.CountryPOJO;
import com.borak.kinweb.backend.domain.enums.ExceptionType;
import com.borak.kinweb.backend.exceptions.ResponseException;
import com.borak.kinweb.backend.exceptions.ExceptionHandler;
import com.borak.kinweb.backend.logic.transformers.CountryTransformer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mr. Poyo
 */
@Service
public class CountryService extends TemplateCountryService<CountryDTO, Long> {

    @Autowired
    private ICountryRepository<CountryPOJO, Long> countryRepo;

    @Autowired
    private CountryTransformer countryTransformer;

    @Override
    public List<CountryDTO> getAllCountriesImpl() throws ResponseException {
        List<CountryPOJO> countries = countryRepo.getAll();
        return countryTransformer.pojoToDto(countries);

    }

}
