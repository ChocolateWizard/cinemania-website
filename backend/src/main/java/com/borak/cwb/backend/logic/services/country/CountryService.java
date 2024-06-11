/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.country;

import com.borak.cwb.backend.domain.jdbc.classes.CountryJDBC;
import com.borak.cwb.backend.logic.transformers.CountryTransformer;
import com.borak.cwb.backend.repository.api.ICountryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class CountryService implements ICountryService {

    @Autowired
    private CountryTransformer countryTransformer;

    @Autowired
    private ICountryRepository<CountryJDBC, Long> countryRepo;

    @Override
    public ResponseEntity getAll() {
        List<CountryJDBC> countries = countryRepo.findAll();
        return new ResponseEntity(countryTransformer.toCountryResponseDTO(countries), HttpStatus.OK);
    }

}
