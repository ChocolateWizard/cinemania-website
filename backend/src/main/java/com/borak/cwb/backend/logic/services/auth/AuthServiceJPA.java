/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.auth;

import com.borak.cwb.backend.domain.dto.MessageResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserLoginDTO;
import com.borak.cwb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.cwb.backend.domain.dto.user.UserResponseDTO;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.exceptions.EmailTakenException;
import com.borak.cwb.backend.exceptions.ProfileNameTakenException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.exceptions.UsernameTakenException;
import com.borak.cwb.backend.logic.security.JwtUtils;
import com.borak.cwb.backend.logic.transformers.UserTransformer;
import com.borak.cwb.backend.repository.jpa.CountryRepositoryJPA;
import com.borak.cwb.backend.repository.jpa.UserRepositoryJPA;
import com.borak.cwb.backend.repository.file.FileRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional()
public class AuthServiceJPA implements IAuthService<UserRegisterDTO, UserLoginDTO> {

    @Autowired
    private UserRepositoryJPA userRepo;
    
    @Autowired
    private CountryRepositoryJPA countryRepo;

    @Autowired
    private UserTransformer userTransformer;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
//==========================================================================================

    @Override
    public ResponseEntity register(UserRegisterDTO registerForm) {
        if (userRepo.existsByUsername(registerForm.getUsername())) {
            throw new UsernameTakenException("Username is already taken!");
        }
        if (userRepo.existsByEmail(registerForm.getEmail())) {
            throw new EmailTakenException("Email is already in use!");
        }
        if (userRepo.existsByProfileName(registerForm.getProfileName())) {
            throw new ProfileNameTakenException("Profile name is already taken!");
        }
        if (!countryRepo.existsById(registerForm.getCountryId())) {
            throw new ResourceNotFoundException("Country with id: " + registerForm.getCountryId() + " does not exist in database!");
        }
        UserJPA userJPA = userTransformer.toUserJPA(registerForm);
        userRepo.save(userJPA);
        if (registerForm.getProfileImage() != null) {
            registerForm.getProfileImage().setName(userJPA.getProfileName());
            fileRepo.saveUserProfileImage(registerForm.getProfileImage());
        }
        return new ResponseEntity<>(new MessageResponseDTO("User registered successfully!"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity login(UserLoginDTO loginForm) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginForm.getUsername(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        Optional<UserJPA> userDB = userRepo.findById(userDetails.getId());

        UserResponseDTO userInfoResponse = userTransformer.jpaToUserResponse(userDB.get());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userInfoResponse);
    }

    @Override
    public ResponseEntity logout() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponseDTO("You've been logged out!"));
    }
}
