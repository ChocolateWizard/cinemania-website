/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.borak.kinweb.backend.logic.services.user.IUserService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Mr. Poyo
 */
@RestController
@RequestMapping(path = "api/users")
@Validated
public class UserController {

    @Autowired
    private IUserService<Long> userService;

    //=========================POST MAPPINGS==================================
    @PostMapping(path = "/library/{id}")
    public ResponseEntity postMediaIntoLibrary(@PathVariable @Min(value = 1, message = "Media id must be greater than or equal to 1") long id) {
        return userService.postMediaIntoLibrary(id);
    }

    //=========================PUT MAPPINGS==================================  
    //=========================DELETE MAPPINGS==================================
    @DeleteMapping(path = "/library/{id}")
    public ResponseEntity deleteMediaFromLibrary(@PathVariable @Min(value = 1, message = "Media id must be greater than or equal to 1") long id) {
        return userService.deleteMediaFromLibrary(id);
    }

}
