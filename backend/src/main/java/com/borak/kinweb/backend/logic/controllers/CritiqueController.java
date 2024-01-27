/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.kinweb.backend.logic.services.critique.ICritiqueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mr. Poyo
 */
//@CrossOrigin(originPatterns = {"http://localhost:*"},maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping(path = "api/critiques")
@Validated
public class CritiqueController {

    @Autowired
    private ICritiqueService critiqueService;

    //=========================POST MAPPINGS==================================
    @PostMapping(path = "/{id}")
    public ResponseEntity postCritique(@PathVariable @Min(value = 1, message = "Media id must be greater than or equal to 1") long id, @Valid @RequestBody CritiqueRequestDTO critiqueRequest) {
        critiqueRequest.setMediaId(id);
        return critiqueService.postCritique(critiqueRequest);
    }

    //=========================PUT MAPPINGS==================================
    @PutMapping(path = "/{id}")
    public ResponseEntity putMovie(@PathVariable @Min(value = 1, message = "Media id must be greater than or equal to 1") long id, @Valid @RequestBody CritiqueRequestDTO critiqueRequest) {
        critiqueRequest.setMediaId(id);
        return critiqueService.putCritique(critiqueRequest);
    }

    //=========================DELETE MAPPINGS==================================
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteCritique(@PathVariable @Min(value = 1, message = "Media id must be greater than or equal to 1") long id) {
        return critiqueService.deleteCritique(id);
    }

}
