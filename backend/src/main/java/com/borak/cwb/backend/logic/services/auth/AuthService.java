/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.services.auth;

import com.borak.cwb.backend.domain.dto.MessageResponseDTO;
import com.borak.cwb.backend.domain.dto.user.UserLoginDTO;
import com.borak.cwb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.cwb.backend.domain.dto.user.UserResponseDTO;
import com.borak.cwb.backend.domain.jdbc.CountryJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.exceptions.EmailTakenException;
import com.borak.cwb.backend.exceptions.ProfileNameTakenException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.exceptions.UsernameTakenException;
import com.borak.cwb.backend.logic.security.JwtUtils;
import com.borak.cwb.backend.logic.transformers.UserTransformer;
import com.borak.cwb.backend.repository.api.ICountryRepository;
import com.borak.cwb.backend.repository.api.IUserRepository;
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
//@Service
//@Transactional
public class AuthService implements IAuthService<UserRegisterDTO, UserLoginDTO> {

    @Autowired
    private IUserRepository<UserJDBC, Long, MediaJDBC, Long> userRepo;
    @Autowired
    private ICountryRepository<CountryJDBC, Long> countryRepo;

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
        if (userRepo.existsUsername(registerForm.getUsername())) {
            throw new UsernameTakenException("Username is already taken!");
        }
        if (userRepo.existsEmail(registerForm.getEmail())) {
            throw new EmailTakenException("Email is already in use!");
        }
        if (userRepo.existsProfileName(registerForm.getProfileName())) {
            throw new ProfileNameTakenException("Profile name is already taken!");
        }
        if (!countryRepo.existsById(registerForm.getCountryId())) {
            throw new ResourceNotFoundException("Country with id: " + registerForm.getCountryId() + " does not exist in database!");
        }
        UserJDBC userJDBC = userTransformer.toUserJDBC(registerForm);
        userRepo.insert(userJDBC);
        if (registerForm.getProfileImage() != null) {
            registerForm.getProfileImage().setName(userJDBC.getProfileName());
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

        Optional<UserJDBC> userDB = userRepo.findByIdWithRelations(userDetails.getId());

        UserResponseDTO userInfoResponse = userTransformer.jdbcToUserResponse(userDB.get());
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
