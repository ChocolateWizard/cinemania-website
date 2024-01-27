/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.kinweb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.logic.services.person.IPersonService;
import com.borak.kinweb.backend.logic.services.validation.DomainValidationService;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
//@CrossOrigin(originPatterns = {"http://localhost:*"},maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping(path = "api/persons")
@Validated
public class PersonController {

    @Autowired
    private IPersonService personService;

    @Autowired
    private DomainValidationService domainValidator;

    //=========================GET MAPPINGS==================================  
    @GetMapping
    @JsonView(JsonVisibilityViews.Lite.class)
    public ResponseEntity getPersons(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return personService.getAllPersonsPaginated(page, size);
    }

    @GetMapping(path = "/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getPersonsDetails(
            @RequestParam(name = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page number has to be greater than or equal to 1") int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size number has to be greater than or equal to 1") int size) {
        return personService.getAllPersonsWithDetailsPaginated(page, size);
    }

    @GetMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Medium.class)
    public ResponseEntity getPersonById(@PathVariable @Min(value = 1, message = "Person id must be greater than or equal to 1") long id) {
        return personService.getPersonWithProfessions(id);
    }

    @GetMapping(path = "/{id}/details")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity getPersonByIdDetails(@PathVariable @Min(value = 1, message = "Person id must be greater than or equal to 1") long id) {
        return personService.getPersonWithDetails(id);
    }

    //=========================POST MAPPINGS==================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity postPerson(@RequestPart("person") PersonRequestDTO person, @RequestPart(name = "profile_photo", required = false) MultipartFile profilePhoto) {
        domainValidator.validate(person, profilePhoto, RequestMethod.POST);
        if (profilePhoto != null) {
            person.setProfilePhoto(new MyImage(profilePhoto));
        }
        return personService.postPerson(person);
    }

    //=========================PUT MAPPINGS===================================
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity putPerson(@PathVariable @Min(value = 1, message = "Person id must be greater than or equal to 1") long id, @RequestPart("person") PersonRequestDTO person, @RequestPart(name = "profile_photo", required = false) MultipartFile profilePhoto) {
        person.setId(id);
        domainValidator.validate(person, profilePhoto, RequestMethod.PUT);
        if (profilePhoto != null) {
            person.setProfilePhoto(new MyImage(profilePhoto));
        }
        return personService.putPerson(person);
    }

    //=========================DELETE MAPPINGS================================
    @DeleteMapping(path = "/{id}")
    @JsonView(JsonVisibilityViews.Heavy.class)
    public ResponseEntity deletePersonById(@PathVariable @Min(value = 1, message = "Person id must be greater than or equal to 1") long id) {
        return personService.deletePersonById(id);
    }

}
