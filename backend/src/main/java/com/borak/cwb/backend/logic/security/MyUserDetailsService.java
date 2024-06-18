/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.logic.security;

import com.borak.cwb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.domain.security.SecurityUser;
import com.borak.cwb.backend.repository.api.IUserRepository;
import com.borak.cwb.backend.repository.jpa.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

//    @Autowired
//    private IUserRepository<UserJDBC, Long, MediaJDBC, Long> userRepo;
    @Autowired
    private UserRepositoryJPA userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJPA userDB = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return toSecurityUser(userDB);
    }

//==========================================================================================================    
    private SecurityUser toSecurityUser(UserJDBC userJDBC) throws IllegalArgumentException {
        SecurityUser user = new SecurityUser();
        user.setId(userJDBC.getId());
        user.setFirstName(userJDBC.getFirstName());
        user.setLastName(userJDBC.getLastName());
        user.setGender(userJDBC.getGender());
        user.setRole(userJDBC.getRole());
        user.setProfileName(userJDBC.getProfileName());
        user.setProfileImage(userJDBC.getProfileImage());
        user.setUsername(userJDBC.getUsername());
        user.setPassword(userJDBC.getPassword());
        user.setEmail(userJDBC.getEmail());
        user.setCreatedAt(userJDBC.getCreatedAt());
        user.setUpdatedAt(userJDBC.getUpdatedAt());
        user.setCountry(new SecurityUser.Country(userJDBC.getCountry().getId(),
                userJDBC.getCountry().getName(),
                userJDBC.getCountry().getOfficialStateName(),
                userJDBC.getCountry().getCode()));
        return user;
    }

    private SecurityUser toSecurityUser(UserJPA userJPA) throws IllegalArgumentException {
        SecurityUser user = new SecurityUser();
        user.setId(userJPA.getId());
        user.setFirstName(userJPA.getFirstName());
        user.setLastName(userJPA.getLastName());
        user.setGender(userJPA.getGender());
        user.setRole(userJPA.getRole());
        user.setProfileName(userJPA.getProfileName());
        user.setProfileImage(userJPA.getProfileImage());
        user.setUsername(userJPA.getUsername());
        user.setPassword(userJPA.getPassword());
        user.setEmail(userJPA.getEmail());
        user.setCreatedAt(userJPA.getCreatedAt());
        user.setUpdatedAt(userJPA.getUpdatedAt());
        user.setCountry(new SecurityUser.Country(userJPA.getCountry().getId(),
                userJPA.getCountry().getName(),
                userJPA.getCountry().getOfficialStateName(),
                userJPA.getCountry().getCode()));
        return user;
    }

}
