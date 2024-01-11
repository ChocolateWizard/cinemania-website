/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.controllers;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.user.UserLoginDTO;
import com.borak.kinweb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.kinweb.backend.logic.services.user.IUserService;
import com.borak.kinweb.backend.logic.services.validation.DomainValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mr. Poyo
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private IUserService<UserRegisterDTO, UserLoginDTO> userService;

    @Autowired
    private DomainValidationService domainValidator;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestPart(name = "user") UserRegisterDTO registerForm, @RequestPart(name = "profile_image", required = false) MultipartFile profileImage) {
        domainValidator.validate(registerForm, profileImage);
        if (profileImage != null) {
            registerForm.setProfileImage(new MyImage(profileImage));
        }
        return userService.register(registerForm);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return userService.logout();
    }

}
