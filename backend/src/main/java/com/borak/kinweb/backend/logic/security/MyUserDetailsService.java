/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.logic.security;

import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.domain.security.SecurityUser;
import com.borak.kinweb.backend.logic.transformers.UserTransformer;
import com.borak.kinweb.backend.repository.api.IUserRepository;
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

    @Autowired
    private IUserRepository<UserJDBC, Long> userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJDBC userDB = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
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

}
