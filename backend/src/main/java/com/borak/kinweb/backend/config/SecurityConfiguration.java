/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.config;

import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.logic.security.AuthEntryPointJwt;
import com.borak.kinweb.backend.logic.security.AuthTokenFilter;
import com.borak.kinweb.backend.logic.security.MyUserDetailsService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

    //this tells spring security to ignore provided path. Its not advised to do it like this
    //but it works
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/images/**");
//    }
    //CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:[*]"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Cookie"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600l);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
//this is if you wish to set up your own cors filter
//    @Bean
//    public CorsFilter corsFilter() {
//        return new CorsFilter(corsConfigurationSource());
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers(HttpMethod.GET,
                        "/api/medias/**",
                        "/api/movies/**",
                        "/api/tv/**",
                        "/api/persons/**",
                        "/api/countries/**",
                        "/images/**"
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
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//                .addFilterBefore(corsFilter(), AuthTokenFilter.class);

        return http.build();
    }

}
