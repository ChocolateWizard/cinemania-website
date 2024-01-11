/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.config;

import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.logic.security.AuthEntryPointJwt;
import com.borak.kinweb.backend.logic.security.AuthTokenFilter;
import com.borak.kinweb.backend.logic.security.MyUserDetailsService;
import com.borak.kinweb.backend.logic.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Mr. Poyo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

//==================================================================================================
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers(HttpMethod.GET,
                        "/api/medias/**",
                        "/api/movies/**",
                        "/api/tv/**",
                        "/api/persons/**"
                ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "api/critiques/*"
                        ).hasAnyAuthority(UserRole.CRITIC.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.PUT,
                                "api/critiques/*"
                        ).hasAnyAuthority(UserRole.CRITIC.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.DELETE,
                                "api/critiques/*"
                        ).hasAnyAuthority(UserRole.CRITIC.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.POST,
                                "/api/movies",
                                "/api/tv",
                                "/api/persons"
                        ).hasAuthority(UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.PUT,
                                "/api/movies/*",
                                "/api/tv/*",
                                "/api/persons/*"
                        ).hasAuthority(UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/movies/*",
                                "/api/tv/*",
                                "/api/persons/*"
                        ).hasAuthority(UserRole.ADMINISTRATOR.toString())
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/library/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/library/*").authenticated()
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
