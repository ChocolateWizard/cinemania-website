/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.config;

import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.logic.security.AuthEntryPointJwt;
import com.borak.cwb.backend.logic.security.AuthTokenFilter;
import com.borak.cwb.backend.logic.security.MyUserDetailsService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //CORS configuration
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOrigins(List.of("http://localhost:8090"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Cookie"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600l);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth
                        -> auth.
                        requestMatchers(HttpMethod.GET,
                                "/api/medias/**",
                                "/api/movies/**",
                                "/api/tv/**",
                                "/api/genres/**",
                                "/api/persons/**",
                                "/api/countries/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "api/critiques"
                        ).hasAnyAuthority(UserRole.CRITIC.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.DELETE,
                                "api/critiques/*"
                        ).hasAnyAuthority(UserRole.CRITIC.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.POST,
                                "api/critiques/*/likes",
                                "api/critiques/*/dislikes",
                                "api/critiques/*/comments",
                                "api/critiques/*/comments/*/likes",
                                "api/critiques/*/comments/*/dislikes"
                        ).hasAnyAuthority(UserRole.REGULAR.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.PUT,
                                "api/critiques/*/likes",
                                "api/critiques/*/dislikes",
                                "api/critiques/*/comments/*/likes",
                                "api/critiques/*/comments/*/dislikes"
                        ).hasAnyAuthority(UserRole.REGULAR.toString(), UserRole.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.DELETE,
                                "api/critiques/*/likes",
                                "api/critiques/*/dislikes",
                                "api/critiques/*/comments",
                                "api/critiques/*/comments/*/likes",
                                "api/critiques/*/comments/*/dislikes"
                        ).hasAnyAuthority(UserRole.REGULAR.toString(), UserRole.ADMINISTRATOR.toString())
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
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                );
        
        http.authenticationProvider(authenticationProvider());
        
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
}
