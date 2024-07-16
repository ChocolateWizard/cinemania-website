/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.secured;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.MyImage;
import com.borak.cwb.backend.domain.dto.movie.MovieRequestDTO;
import com.borak.cwb.backend.domain.dto.movie.MovieResponseDTO;
import com.borak.cwb.backend.domain.jpa.MovieJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.helpers.TestUtil;
import com.borak.cwb.backend.helpers.repositories.MovieTestRepository;
import com.borak.cwb.backend.helpers.repositories.UserTestRepository;
import com.borak.cwb.backend.logic.security.JwtUtils;
import com.borak.cwb.backend.repository.file.FileRepository;
import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(9)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieSecuredRoutesTest {
    
    private final TestRestTemplate restTemplate;
    private final MovieTestRepository movieRepo;
    private final UserTestRepository userRepo;
    private final JwtUtils jwtUtils;
    private final FileRepository fileRepo;
    private final ConfigProperties config;
    private final TestUtil testUtil;
    
    @Autowired
    public MovieSecuredRoutesTest(TestRestTemplate restTemplate, MovieTestRepository movieRepo, UserTestRepository userRepo, JwtUtils jwtUtils, FileRepository fileRepo, ConfigProperties config, TestUtil testUtil) {
        this.restTemplate = restTemplate;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.fileRepo = fileRepo;
        this.config = config;
        this.testUtil = testUtil;
    }
    
    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/movies";
    
    static {
        TESTS_PASSED.put("postMovie_UnauthenticatedUser_DoesNotCreateMovieReturns401", false);
        TESTS_PASSED.put("postMovie_UnauthorizedUser_DoesNotCreateMovieReturns403", false);
        TESTS_PASSED.put("postMovie_InvalidInputData_DoesNotCreateMovieReturns400", false);
        TESTS_PASSED.put("postMovie_NonexistentDependencyData_DoesNotCreateMovieReturns404", false);
        TESTS_PASSED.put("postMovie_ValidInput_CreatesMovieReturns200", false);
        
        TESTS_PASSED.put("putMovie_UnauthenticatedUser_DoesNotUpdateMovieReturns401", false);
        TESTS_PASSED.put("putMovie_UnauthorizedUser_DoesNotUpdateMovieReturns403", false);
        TESTS_PASSED.put("putMovie_InvalidInputData_DoesNotUpdateMovieReturns400", false);
        TESTS_PASSED.put("putMovie_NonexistentDependencyData_DoesNotUpdateMovieReturns404", false);
        TESTS_PASSED.put("putMovie_ValidInput_UpdatesMovieReturns200", false);
        
        TESTS_PASSED.put("deleteMovie_UnauthenticatedUser_DoesNotDeleteMovieReturns401", false);
        TESTS_PASSED.put("deleteMovie_UnauthorizedUser_DoesNotDeleteMovieReturns403", false);
        TESTS_PASSED.put("deleteMovie_InvalidInputData_DoesNotDeleteMovieReturns400", false);
        TESTS_PASSED.put("deleteMovie_NonexistentDependencyData_DoesNotDeleteMovieReturns404", false);
        TESTS_PASSED.put("deleteMovie_ValidInput_DeletesMovieReturns200", false);
    }
    
    public static boolean didAllTestsPass() {
        for (boolean b : TESTS_PASSED.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

//=========================================================================================================
    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(TestResultsHelper.didMovieSecuredRoutesRequiredTestsPass());
    }

    //=========================================================================================================
    //POST
    @Test
    @Order(1)
    @DisplayName("Tests whether POST request to /api/movies with unauthenticated user did not create new movie and it returned 401")
    void postMovie_UnauthenticatedUser_DoesNotCreateMovieReturns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        MovieRequestDTO movieValid = getValidMovie(7l);
        MockMultipartFile imageValid = getValidCoverImage(TestUtil.getRandomString(20));
        int i = 0;
        try {
            for (String username : getNonExistentUsernames()) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();

                //no cookie
                request = constructRequest(movieValid, imageValid, null);
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(movieValid, imageValid, TestUtil.getRandomString(50));
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJPA> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(movieValid, imageValid, jwt);
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(movieValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("postMovie_UnauthenticatedUser_DoesNotCreateMovieReturns401", true);
    }
    
    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/movies with unauthorized but authenticated user did not create new movie and it returned 403")
    void postMovie_UnauthorizedUser_DoesNotCreateMovieReturns403() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        MovieRequestDTO movieValid = getValidMovie(7l);
        MockMultipartFile imageValid = getValidCoverImage(TestUtil.getRandomString(20));
        int i = 0;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                Optional<UserJPA> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(movieValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("postMovie_UnauthorizedUser_DoesNotCreateMovieReturns403", true);
    }
    
    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/movies with invalid movie data did not create new movie and it returned 400")
    void postMovie_InvalidInputData_DoesNotCreateMovieReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        
        int i = 0;
        try {
            for (Object[] input : getBadRequestMoviesAndImages(7l, TestUtil.getRandomString(20))) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest((MovieRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("postMovie_InvalidInputData_DoesNotCreateMovieReturns400", true);
    }
    
    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/movies with non-existent dependency objects did not create new movie and it returned 404")
    void postMovie_NonexistentDependencyData_DoesNotCreateMovieReturns404() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        
        int i = 0;
        try {
            for (Object[] input : getNonExistentDependencyMoviesAndImages(7l, TestUtil.getRandomString(20))) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest((MovieRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("postMovie_NonexistentDependencyData_DoesNotCreateMovieReturns404", true);
    }
    
    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to /api/movies with valid input did create new movie and it returned 200")
    void postMovie_ValidInput_CreatesMovieReturns200() {
        HttpEntity request;
        ResponseEntity<MovieResponseDTO> response;
        
        MovieRequestDTO movieValid1 = getValidMovie(7l);
        MovieRequestDTO movieValid2 = getValidMovie(null);
        
        MockMultipartFile imageValid1 = getValidCoverImage(TestUtil.getRandomString(20));
        MockMultipartFile imageValid2 = getValidCoverImage(TestUtil.getRandomString(20));
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where movieId is set
        assertThat(movieRepo.existsById(movieValid1.getId())).isFalse();
        assertThat(existsCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isFalse();
        
        request = constructRequest(movieValid1, imageValid1, cookie.toString());
        response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(movieValid1.getId())).isTrue();
        assertThat(existsCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        
        Optional<MovieJPA> actualDBMovie = movieRepo.findById(movieValid1.getId());
        Resource actualDBImage = fileRepo.getMediaCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(actualDBMovie).isNotNull();
        assertThat(actualDBMovie.isPresent()).isTrue();
        String coverImageUrl = config.getMediaImagesBaseUrl() + movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename());
        assertMoviesEqual(response.getBody(), movieValid1, coverImageUrl);
        assertMoviesEqual(actualDBMovie.get(), response.getBody());
        assertImagesEqual(actualDBImage, imageValid1);

        //----------------------------------------------------------------------------
        //second valid request where movieId is null
        request = constructRequest(movieValid2, imageValid2, cookie.toString());
        response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(response.getBody().getId())).isTrue();
        assertThat(existsCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()))).isTrue();
        
        actualDBMovie = movieRepo.findById(response.getBody().getId());
        actualDBImage = fileRepo.getMediaCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()));
        assertThat(actualDBMovie).isNotNull();
        assertThat(actualDBMovie.isPresent()).isTrue();
        movieValid2.setId(response.getBody().getId());
        coverImageUrl = config.getMediaImagesBaseUrl() + movieValid2.getId() + getExtensionWithDot(imageValid2.getOriginalFilename());
        assertMoviesEqual(response.getBody(), movieValid2, coverImageUrl);
        assertMoviesEqual(actualDBMovie.get(), response.getBody());
        assertImagesEqual(actualDBImage, imageValid2);
        
        TESTS_PASSED.put("postMovie_ValidInput_CreatesMovieReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(6)
    @DisplayName("Tests whether PUT request to /api/movies/{id} with unauthenticated user did not update movie and it returned 401")
    void putMovie_UnauthenticatedUser_DoesNotUpdateMovieReturns401() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        MovieRequestDTO movieValid = getValidMovie(7l);
        MockMultipartFile imageValid = getValidCoverImage(TestUtil.getRandomString(40));
        int i = 0;
        try {
            for (String username : getNonExistentUsernames()) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();

                //no cookie
                request = constructRequest(movieValid, imageValid, null);
                response = restTemplate.exchange(ROUTE + "/" + movieValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(movieValid, imageValid, TestUtil.getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + movieValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJPA> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(movieValid, imageValid, jwt);
                response = restTemplate.exchange(ROUTE + "/" + movieValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(movieValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + movieValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("putMovie_UnauthenticatedUser_DoesNotUpdateMovieReturns401", true);
    }
    
    @Test
    @Order(7)
    @DisplayName("Tests whether PUT request to /api/movies/{id} with unauthorized but authenticated user did not update movie and it returned 403")
    void putMovie_UnauthorizedUser_DoesNotUpdateMovieReturns403() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        MovieRequestDTO movieValid = getValidMovie(7l);
        MockMultipartFile imageValid = getValidCoverImage(TestUtil.getRandomString(40));
        int i = 0;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                Optional<UserJPA> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(movieValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + movieValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("putMovie_UnauthorizedUser_DoesNotUpdateMovieReturns403", true);
    }
    
    @Test
    @Order(8)
    @DisplayName("Tests whether PUT request to /api/movies/{id} with invalid movie data did not update movie and it returned 400")
    void putMovie_InvalidInputData_DoesNotUpdateMovieReturns400() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        
        int i = 0;
        try {
            for (Object[] input : getBadRequestMoviesAndImages(7l, TestUtil.getRandomString(40))) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                
                request = constructRequest((MovieRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + ((MovieRequestDTO) input[0]).getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            i = 0;
            MovieRequestDTO movieValid = getValidMovie(7l);
            MockMultipartFile imageValid = getValidCoverImage(TestUtil.getRandomString(40));
            for (long invalidId : new long[]{0l, -1l, -2l, -5l, -10l, -23l, Long.MIN_VALUE}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest(movieValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("putMovie_InvalidInputData_DoesNotUpdateMovieReturns400", true);
    }
    
    @Test
    @Order(9)
    @DisplayName("Tests whether PUT request to /api/movies/{id} with non-existent dependency objects did not update movie and it returned 404")
    void putMovie_NonexistentDependencyData_DoesNotUpdateMovieReturns404() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        
        int i = 0;
        try {
            for (Object[] input : getNonExistentDependencyMoviesAndImages(7l, TestUtil.getRandomString(40))) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest((MovieRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + ((MovieRequestDTO) input[0]).getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            i = 0;
            MovieRequestDTO movieValid = getValidMovie(7l);
            MockMultipartFile imageValid = getValidCoverImage(TestUtil.getRandomString(40));
            for (long invalidId : new long[]{3l, 5l, 6l, 50l, 51l, 101l, Long.MAX_VALUE}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest(movieValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("putMovie_NonexistentDependencyData_DoesNotUpdateMovieReturns404", true);
    }
    
    @Test
    @Order(10)
    @DisplayName("Tests whether PUT request to /api/movies/{id} with valid movie data did update movie and it returned 200")
    void putMovie_ValidInput_UpdatesMovieReturns200() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<MovieResponseDTO> response;
        
        MovieRequestDTO movieValid1 = getValidMovie(7l);
        MovieRequestDTO movieValid2 = getValidMovie(null);
        MovieRequestDTO movieValid3 = getValidMovie(8l);
        
        changeAttributes(movieValid1);
        changeAttributes(movieValid2);
        changeAttributes(movieValid3);
        
        MockMultipartFile imageValid1 = getValidCoverImage(TestUtil.getRandomString(40));
        MockMultipartFile imageValid2 = getValidCoverImage(TestUtil.getRandomString(50));
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where movie has different valid attributes and ID and image are set
        assertThat(movieRepo.existsById(movieValid1.getId())).isTrue();
        assertThat(existsCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        Optional<MovieJPA> actualDBMovie = movieRepo.findById(movieValid1.getId());
        Resource actualDBImage = fileRepo.getMediaCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(areMoviesEqual(actualDBMovie.get(), movieValid1)).isFalse();
        assertThat(areImagesEqual(actualDBImage, imageValid1)).isFalse();
        
        request = constructRequest(movieValid1, imageValid1, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + movieValid1.getId(), HttpMethod.PUT, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(movieValid1.getId())).isTrue();
        assertThat(existsCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        actualDBMovie = movieRepo.findById(movieValid1.getId());
        actualDBImage = fileRepo.getMediaCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(areMoviesEqual(actualDBMovie.get(), movieValid1)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid1)).isTrue();
        
        String coverImageUrl = config.getMediaImagesBaseUrl() + movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename());
        assertMoviesEqual(response.getBody(), movieValid1, coverImageUrl);
        assertMoviesEqual(actualDBMovie.get(), response.getBody());

        //----------------------------------------------------------------------------
        //second valid request where movie has different valid attributes and ID is null and image has more bytes than the previous updated one
        //check if movieValid1 object is present in database and is the same as movieValid2
        //and that imageValid2 is different than the database imageValid1
        assertThat(movieRepo.existsById(movieValid1.getId())).isTrue();
        assertThat(existsCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        actualDBMovie = movieRepo.findById(movieValid1.getId());
        actualDBImage = fileRepo.getMediaCoverImage(movieValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        movieValid2.setId(movieValid1.getId());
        assertThat(areMoviesEqual(actualDBMovie.get(), movieValid2)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid2)).isFalse();

        //make request
        movieValid2.setId(null);
        request = constructRequest(movieValid2, imageValid2, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + movieValid1.getId(), HttpMethod.PUT, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(response.getBody().getId())).isTrue();
        assertThat(existsCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()))).isTrue();
        
        actualDBMovie = movieRepo.findById(response.getBody().getId());
        actualDBImage = fileRepo.getMediaCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()));
        assertThat(actualDBMovie).isNotNull();
        assertThat(actualDBMovie.isPresent()).isTrue();
        movieValid2.setId(response.getBody().getId());
        assertThat(areMoviesEqual(actualDBMovie.get(), movieValid2)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid2)).isTrue();
        
        coverImageUrl = config.getMediaImagesBaseUrl() + movieValid2.getId() + getExtensionWithDot(imageValid2.getOriginalFilename());
        assertMoviesEqual(response.getBody(), movieValid2, coverImageUrl);
        assertMoviesEqual(actualDBMovie.get(), response.getBody());
        //----------------------------------------------------------------------------
        //third valid request where movie has different valid attributes, ID is 8 and image is null
        //check if image that was in database was deleted after successful PUT request
        assertThat(movieRepo.existsById(movieValid3.getId())).isTrue();
        actualDBMovie = movieRepo.findById(movieValid3.getId());
        assertThat(areMoviesEqual(actualDBMovie.get(), movieValid3)).isFalse();
        assertThat(existsCoverImage(actualDBMovie.get().getCoverImage())).isTrue();
        
        request = constructRequest(movieValid3, null, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + movieValid3.getId(), HttpMethod.PUT, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(movieValid3.getId())).isTrue();
        assertThat(existsCoverImage(actualDBMovie.get().getCoverImage())).isFalse();
        actualDBMovie = movieRepo.findById(movieValid3.getId());
        assertThat(areMoviesEqual(actualDBMovie.get(), movieValid3)).isTrue();
        
        assertMoviesEqual(response.getBody(), movieValid3, null);
        assertMoviesEqual(actualDBMovie.get(), response.getBody());
        
        TESTS_PASSED.put("putMovie_ValidInput_UpdatesMovieReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(11)
    @DisplayName("Tests whether DELETE request to /api/movies/{id} with unauthenticated user did not delete movie and it returned 401")
    void deleteMovie_UnauthenticatedUser_DoesNotDeleteMovieReturns401() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        Assumptions.assumeTrue(TESTS_PASSED.get("putMovie_ValidInput_UpdatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        int i = 0;
        long movieValidId = 7l;
        try {
            for (String username : getNonExistentUsernames()) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();

                //no cookie
                request = constructRequest(null);
                response = restTemplate.exchange(ROUTE + "/" + movieValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(TestUtil.getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + movieValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJPA> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(jwt);
                response = restTemplate.exchange(ROUTE + "/" + movieValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + movieValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("deleteMovie_UnauthenticatedUser_DoesNotDeleteMovieReturns401", true);
    }
    
    @Test
    @Order(12)
    @DisplayName("Tests whether DELETE request to /api/movies/{id} with unauthorized but authenticated user did not delete movie and it returned 403")
    void deleteMovie_UnauthorizedUser_DoesNotDeleteMovieReturns403() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        Assumptions.assumeTrue(TESTS_PASSED.get("putMovie_ValidInput_UpdatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        int i = 0;
        long movieValidId = 7l;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                Optional<UserJPA> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + movieValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("deleteMovie_UnauthorizedUser_DoesNotDeleteMovieReturns403", true);
    }
    
    @Test
    @Order(13)
    @DisplayName("Tests whether DELETE request to /api/movies/{id} with invalid input data did not delete movie and it returned 400")
    void deleteMovie_InvalidInputData_DoesNotDeleteMovieReturns400() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        Assumptions.assumeTrue(TESTS_PASSED.get("putMovie_ValidInput_UpdatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        
        int i = 0;
        try {
            for (long input : new long[]{0l, -1l, -2l, -5l, -10l, -23l, Long.MIN_VALUE}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + input, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("deleteMovie_InvalidInputData_DoesNotDeleteMovieReturns400", true);
    }
    
    @Test
    @Order(14)
    @DisplayName("Tests whether DELETE request to /api/movies/{id} with non-existent movie did not delete movie and it returned 404")
    void deleteMovie_NonexistentDependencyData_DoesNotDeleteMovieReturns404() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        Assumptions.assumeTrue(TESTS_PASSED.get("putMovie_ValidInput_UpdatesMovieReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<MovieJPA> moviesBefore;
        List<MovieJPA> moviesAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        
        int i = 0;
        try {
            for (long invalidId : new long[]{3l, 5l, 6l, 50l, 51l, 101l, Long.MAX_VALUE}) {
                moviesBefore = getAllMovies();
                imagesBefore = getAllCoverImages();
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                moviesAfter = getAllMovies();
                imagesAfter = getAllCoverImages();
                assertMoviesEqual(moviesAfter, moviesBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        
        TESTS_PASSED.put("deleteMovie_NonexistentDependencyData_DoesNotDeleteMovieReturns404", true);
    }
    
    @Test
    @Order(15)
    @DisplayName("Tests whether DELETE request to /api/movies/{id} with valid movie data did delete movie and it returned 200")
    void deleteMovie_ValidInput_DeletesMovieReturns200() {
        Assumptions.assumeTrue(TESTS_PASSED.get("postMovie_ValidInput_CreatesMovieReturns200"));
        Assumptions.assumeTrue(TESTS_PASSED.get("putMovie_ValidInput_UpdatesMovieReturns200"));
        
        HttpEntity request;
        ResponseEntity<MovieResponseDTO> response;
        
        long movieValidId1 = 7l;
        long movieValidId2 = 8l;
        
        Optional<UserJPA> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where movieId is 7
        assertThat(movieRepo.existsById(movieValidId1)).isTrue();
        Optional<MovieJPA> movieDB = movieRepo.findById(movieValidId1);
        assertThat(existsCoverImage(movieDB.get().getCoverImage())).isTrue();
        
        request = constructRequest(cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + movieValidId1, HttpMethod.DELETE, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(movieValidId1)).isFalse();
        assertThat(existsCoverImage(movieDB.get().getCoverImage())).isFalse();
        assertMoviesEqual(movieDB.get(), response.getBody());

        //----------------------------------------------------------------------------
        //second valid request where movieId is 8
        assertThat(movieRepo.existsById(movieValidId2)).isTrue();
        movieDB = movieRepo.findById(movieValidId2);
        assertThat(existsCoverImage(movieDB.get().getCoverImage())).isFalse();
        
        request = constructRequest(cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + movieValidId2, HttpMethod.DELETE, request, MovieResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        assertThat(movieRepo.existsById(movieValidId2)).isFalse();
        assertThat(existsCoverImage(movieDB.get().getCoverImage())).isFalse();
        assertMoviesEqual(movieDB.get(), response.getBody());
        
        TESTS_PASSED.put("deleteMovie_ValidInput_DeletesMovieReturns200", true);
    }
//=================================================================================================================================
//PRIVATE METHODS

    private String[] getNonExistentUsernames() {
        return new String[]{"dummy user 1", "dummy user 2", "dummy user 4", "admina", "critic1", "dummy user 6"};
    }
    
    private MovieRequestDTO getValidMovie(Long movieId) {
        MovieRequestDTO movie = new MovieRequestDTO();
        movie.setId(movieId);
        movie.setTitle("Dummy title");
        movie.setDescription("Dummy description");
        movie.setReleaseDate(LocalDate.now());
        movie.setAudienceRating(89);
        movie.setLength(120);
        movie.setGenres(new ArrayList<>() {
            {
                add(1l);
                add(4l);
                add(5l);
            }
        });
        movie.setDirectors(new ArrayList<>() {
            {
                add(1l);
                add(26l);
            }
        });
        movie.setWriters(new ArrayList<>() {
            {
                add(19l);
                add(26l);
                add(34l);
            }
        });
        MovieRequestDTO.Actor a1 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a2 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a3 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a4 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a5 = new MovieRequestDTO.Actor();
        a1.setId(2l);
        a1.setStarring(true);
        a1.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
                add("Dummy role 2");
            }
        });
        a2.setId(6l);
        a2.setStarring(true);
        a2.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
            }
        });
        a3.setId(11l);
        a3.setStarring(true);
        a3.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
            }
        });
        a4.setId(13l);
        a4.setStarring(false);
        a4.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
                add("Dummy role 2");
                add("Dummy role 3");
            }
        });
        a5.setId(36l);
        a5.setStarring(false);
        a5.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
                add("Dummy role 2");
            }
        });
        movie.getActors().add(a1);
        movie.getActors().add(a2);
        movie.getActors().add(a3);
        movie.getActors().add(a4);
        movie.getActors().add(a5);
        return movie;
    }
    
    private MockMultipartFile getValidCoverImage(String content) {
        return new MockMultipartFile("cover_image", "movie_routes.png", "image/png", TestUtil.getBytes(content));
    }
    
    private List<Object[]> getBadRequestMoviesAndImages(Long validMovieId, String validImageContent) {
        MovieRequestDTO mTitle1 = getValidMovie(validMovieId);
        MovieRequestDTO mTitle2 = getValidMovie(validMovieId);
        MovieRequestDTO mTitle3 = getValidMovie(validMovieId);
        MovieRequestDTO mTitle4 = getValidMovie(validMovieId);
        MovieRequestDTO mTitle5 = getValidMovie(validMovieId);
        mTitle1.setTitle(null);
        mTitle2.setTitle("");
        mTitle3.setTitle(" ");
        mTitle4.setTitle("         ");
        mTitle5.setTitle(TestUtil.getRandomString(301));
        
        MovieRequestDTO mDescription1 = getValidMovie(validMovieId);
        MovieRequestDTO mDescription2 = getValidMovie(validMovieId);
        MovieRequestDTO mDescription3 = getValidMovie(validMovieId);
        MovieRequestDTO mDescription4 = getValidMovie(validMovieId);
        MovieRequestDTO mDescription5 = getValidMovie(validMovieId);
        mDescription1.setDescription(null);
        mDescription2.setDescription("");
        mDescription3.setDescription(" ");
        mDescription4.setDescription("         ");
        mDescription5.setDescription(TestUtil.getRandomString(1001));
        
        MovieRequestDTO mReleaseDate1 = getValidMovie(validMovieId);
        mReleaseDate1.setReleaseDate(null);
        
        MovieRequestDTO mAudienceRating1 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating2 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating3 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating4 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating5 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating6 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating7 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating8 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating9 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating10 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating11 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating12 = getValidMovie(validMovieId);
        MovieRequestDTO mAudienceRating13 = getValidMovie(validMovieId);
        mAudienceRating1.setAudienceRating(null);
        mAudienceRating2.setAudienceRating(-1);
        mAudienceRating3.setAudienceRating(-2);
        mAudienceRating4.setAudienceRating(-5);
        mAudienceRating5.setAudienceRating(-10);
        mAudienceRating6.setAudienceRating(-26);
        mAudienceRating7.setAudienceRating(Integer.MIN_VALUE);
        mAudienceRating8.setAudienceRating(101);
        mAudienceRating9.setAudienceRating(102);
        mAudienceRating10.setAudienceRating(105);
        mAudienceRating11.setAudienceRating(110);
        mAudienceRating12.setAudienceRating(126);
        mAudienceRating13.setAudienceRating(Integer.MAX_VALUE);
        
        MovieRequestDTO mLength1 = getValidMovie(validMovieId);
        MovieRequestDTO mLength2 = getValidMovie(validMovieId);
        MovieRequestDTO mLength3 = getValidMovie(validMovieId);
        MovieRequestDTO mLength4 = getValidMovie(validMovieId);
        MovieRequestDTO mLength5 = getValidMovie(validMovieId);
        MovieRequestDTO mLength6 = getValidMovie(validMovieId);
        MovieRequestDTO mLength7 = getValidMovie(validMovieId);
        mLength1.setLength(null);
        mLength2.setLength(-1);
        mLength3.setLength(-2);
        mLength4.setLength(-5);
        mLength5.setLength(-10);
        mLength6.setLength(-25);
        mLength7.setLength(Integer.MIN_VALUE);
        
        MovieRequestDTO mGenres1 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres2 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres3 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres4 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres5 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres6 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres7 = getValidMovie(validMovieId);
        mGenres1.setGenres(null);
        mGenres2.setGenres(new ArrayList<>());
        mGenres3.setGenres(new ArrayList<>() {
            {
                add(null);
            }
        });
        mGenres4.setGenres(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        mGenres5.setGenres(new ArrayList<>() {
            {
                add(0l);
            }
        });
        mGenres6.setGenres(new ArrayList<>() {
            {
                add(0l);
                add(-1l);
                add(-2l);
            }
        });
        mGenres7.setGenres(new ArrayList<>() {
            {
                add(Long.MIN_VALUE);
                add(-1l);
                add(-2l);
            }
        });
        
        MovieRequestDTO mDir1 = getValidMovie(validMovieId);
        MovieRequestDTO mDir2 = getValidMovie(validMovieId);
        MovieRequestDTO mDir3 = getValidMovie(validMovieId);
        MovieRequestDTO mDir4 = getValidMovie(validMovieId);
        MovieRequestDTO mDir5 = getValidMovie(validMovieId);
        MovieRequestDTO mDir6 = getValidMovie(validMovieId);
        MovieRequestDTO mDir7 = getValidMovie(validMovieId);
        mDir1.setDirectors(null);
        mDir2.setDirectors(new ArrayList<>());
        mDir3.setDirectors(new ArrayList<>() {
            {
                add(null);
            }
        });
        mDir4.setDirectors(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        mDir5.setDirectors(new ArrayList<>() {
            {
                add(0l);
            }
        });
        mDir6.setDirectors(new ArrayList<>() {
            {
                add(0l);
                add(-1l);
                add(-2l);
            }
        });
        mDir7.setDirectors(new ArrayList<>() {
            {
                add(Long.MIN_VALUE);
                add(-1l);
                add(-2l);
            }
        });
        
        MovieRequestDTO mWri1 = getValidMovie(validMovieId);
        MovieRequestDTO mWri2 = getValidMovie(validMovieId);
        MovieRequestDTO mWri3 = getValidMovie(validMovieId);
        MovieRequestDTO mWri4 = getValidMovie(validMovieId);
        MovieRequestDTO mWri5 = getValidMovie(validMovieId);
        MovieRequestDTO mWri6 = getValidMovie(validMovieId);
        MovieRequestDTO mWri7 = getValidMovie(validMovieId);
        mWri1.setWriters(null);
        mWri2.setWriters(new ArrayList<>());
        mWri3.setWriters(new ArrayList<>() {
            {
                add(null);
            }
        });
        mWri4.setWriters(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        mWri5.setWriters(new ArrayList<>() {
            {
                add(0l);
            }
        });
        mWri6.setWriters(new ArrayList<>() {
            {
                add(0l);
                add(-1l);
                add(-2l);
            }
        });
        mWri7.setWriters(new ArrayList<>() {
            {
                add(Long.MIN_VALUE);
                add(-1l);
                add(-2l);
            }
        });
        
        MovieRequestDTO mAct1 = getValidMovie(validMovieId);
        MovieRequestDTO mAct2 = getValidMovie(validMovieId);
        MovieRequestDTO mAct3 = getValidMovie(validMovieId);
        MovieRequestDTO mAct4 = getValidMovie(validMovieId);
        MovieRequestDTO mAct5 = getValidMovie(validMovieId);
        MovieRequestDTO mAct6 = getValidMovie(validMovieId);
        MovieRequestDTO mAct7 = getValidMovie(validMovieId);
        MovieRequestDTO mAct8 = getValidMovie(validMovieId);
        MovieRequestDTO mAct9 = getValidMovie(validMovieId);
        MovieRequestDTO mAct10 = getValidMovie(validMovieId);
        MovieRequestDTO mAct11 = getValidMovie(validMovieId);
        MovieRequestDTO mAct12 = getValidMovie(validMovieId);
        MovieRequestDTO mAct13 = getValidMovie(validMovieId);
        MovieRequestDTO mAct14 = getValidMovie(validMovieId);
        MovieRequestDTO mAct15 = getValidMovie(validMovieId);
        MovieRequestDTO mAct16 = getValidMovie(validMovieId);
        mAct1.setActors(null);
        mAct2.setActors(new ArrayList<>());
        mAct3.setActors(new ArrayList<>() {
            {
                add(null);
            }
        });
        mAct4.setActors(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        mAct5.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(null, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct6.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(0l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct7.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(-1l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(-2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(-3l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct8.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(-1l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(-2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(Long.MIN_VALUE, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct9.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, null, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct10.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, null));
            }
        });
        mAct11.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>()));
            }
        });
        mAct12.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add(null);
                    }
                }));
            }
        });
        mAct13.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("");
                    }
                }));
            }
        });
        mAct14.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add(" ");
                    }
                }));
            }
        });
        mAct15.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("       ");
                    }
                }));
            }
        });
        mAct16.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add(TestUtil.getRandomString(301));
                    }
                }));
            }
        });
        List<Object[]> inputs = new ArrayList<>() {
            {
                //invalid title
                add(new Object[]{mTitle1, getValidCoverImage(validImageContent)});
                add(new Object[]{mTitle2, getValidCoverImage(validImageContent)});
                add(new Object[]{mTitle3, getValidCoverImage(validImageContent)});
                add(new Object[]{mTitle4, getValidCoverImage(validImageContent)});
                add(new Object[]{mTitle5, getValidCoverImage(validImageContent)});
                //invalid description
                add(new Object[]{mDescription1, getValidCoverImage(validImageContent)});
                add(new Object[]{mDescription2, getValidCoverImage(validImageContent)});
                add(new Object[]{mDescription3, getValidCoverImage(validImageContent)});
                add(new Object[]{mDescription4, getValidCoverImage(validImageContent)});
                add(new Object[]{mDescription5, getValidCoverImage(validImageContent)});
                //invalid release date
                add(new Object[]{mReleaseDate1, getValidCoverImage(validImageContent)});
                //invalid audience rating
                add(new Object[]{mAudienceRating1, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating2, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating3, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating4, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating5, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating6, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating7, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating8, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating9, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating10, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating11, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating12, getValidCoverImage(validImageContent)});
                add(new Object[]{mAudienceRating13, getValidCoverImage(validImageContent)});
                //invalid length
                add(new Object[]{mLength1, getValidCoverImage(validImageContent)});
                add(new Object[]{mLength2, getValidCoverImage(validImageContent)});
                add(new Object[]{mLength3, getValidCoverImage(validImageContent)});
                add(new Object[]{mLength4, getValidCoverImage(validImageContent)});
                add(new Object[]{mLength5, getValidCoverImage(validImageContent)});
                add(new Object[]{mLength6, getValidCoverImage(validImageContent)});
                add(new Object[]{mLength7, getValidCoverImage(validImageContent)});
                //invalid genres
                add(new Object[]{mGenres1, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres2, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres3, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres4, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres5, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres6, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres7, getValidCoverImage(validImageContent)});
                //invalid directors
                add(new Object[]{mDir1, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir2, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir3, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir4, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir5, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir6, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir7, getValidCoverImage(validImageContent)});
                //invalid writers
                add(new Object[]{mWri1, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri2, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri3, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri4, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri5, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri6, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri7, getValidCoverImage(validImageContent)});
                //invalid actors
                add(new Object[]{mAct1, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct2, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct3, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct4, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct5, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct6, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct7, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct8, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct9, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct10, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct11, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct12, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct13, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct14, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct15, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct16, getValidCoverImage(validImageContent)});
            }
        };

        //invalid file name i=68
        String[] invImageNames = {null, "", " ", "     ", "jpg", "aaaaaaaaa", "..", "...", " .   . ", ".", " . ", ".jpg.", ".png.", "jpg.",
            "..jpg", "png.", "http://www.website.com/images/.jpg.", ".jpg/", ".jpg/website", ".jpg/jpg", ".gpj", ".g", "aaaa.g", "  a  aa  aa.g",
            "http://www.google.com/images/aaaa.jgp", "/", "\\", "aaaaa aaaaa", "   aaaaaa", "aaaaaa  ", "aaaa.aaaa..aaa", "..", "/////",
            "/aaa/aaa/aa", "aaa\\aaaa\\aaaa", "/aaa..aaa..aa..aa", "http://www.google.com/aaaa. jpg",
            "http://www.website.com/images/.jpg.", "..jpg", "aaa aaa aaa..jpg",
            ".gpj", ".g", "aaaaa.g", "  a  aa  aa.g", "http://www.google.com/images/aaaa.jgp", "image.mp3", "image.exe", "image.mp4", "image.gif"};
        for (String invalidName : invImageNames) {
            try {
                MockMultipartFile pom = getValidCoverImage(validImageContent);
                MockMultipartFile invalidCoverImage = new MockMultipartFile(pom.getName(), invalidName, pom.getContentType(), pom.getBytes());
                inputs.add(new Object[]{getValidMovie(validMovieId), invalidCoverImage});
            } catch (IOException ex) {
                fail("getBytes() should not have failed!");
            }
        }
        //invalid file size
        MockMultipartFile pom = getValidCoverImage(validImageContent);
        MockMultipartFile invalidCoverImage = new MockMultipartFile(pom.getName(), pom.getOriginalFilename(), pom.getContentType(), new byte[8388998]);
        inputs.add(new Object[]{getValidMovie(validMovieId), invalidCoverImage});
        
        invalidCoverImage = new MockMultipartFile(pom.getName(), pom.getOriginalFilename(), pom.getContentType(), new byte[0]);
        inputs.add(new Object[]{getValidMovie(validMovieId), invalidCoverImage});
        
        return inputs;
    }
    
    private List<Object[]> getNonExistentDependencyMoviesAndImages(Long validMovieId, String validImageContent) {
        MovieRequestDTO mGenres1 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres2 = getValidMovie(validMovieId);
        MovieRequestDTO mGenres3 = getValidMovie(validMovieId);
        mGenres1.setGenres(new ArrayList<>() {
            {
                add(1l);
                add(2l);
                add(30l);
            }
        });
        mGenres2.setGenres(new ArrayList<>() {
            {
                add(59l);
            }
        });
        mGenres3.setGenres(new ArrayList<>() {
            {
                add(50l);
                add(1l);
            }
        });
        
        MovieRequestDTO mDir1 = getValidMovie(validMovieId);
        MovieRequestDTO mDir2 = getValidMovie(validMovieId);
        MovieRequestDTO mDir3 = getValidMovie(validMovieId);
        mDir1.setDirectors(new ArrayList<>() {
            {
                add(1l);
                add(14l);
                add(51l);
            }
        });
        mDir2.setDirectors(new ArrayList<>() {
            {
                add(1l);
                add(2l);
            }
        });
        mDir3.setDirectors(new ArrayList<>() {
            {
                add(140l);
            }
        });
        
        MovieRequestDTO mWri1 = getValidMovie(validMovieId);
        MovieRequestDTO mWri2 = getValidMovie(validMovieId);
        MovieRequestDTO mWri3 = getValidMovie(validMovieId);
        mWri1.setWriters(new ArrayList<>() {
            {
                add(1l);
                add(16l);
                add(51l);
            }
        });
        mWri2.setWriters(new ArrayList<>() {
            {
                add(1l);
                add(2l);
            }
        });
        mWri3.setWriters(new ArrayList<>() {
            {
                add(140l);
            }
        });
        
        MovieRequestDTO mAct1 = getValidMovie(validMovieId);
        MovieRequestDTO mAct2 = getValidMovie(validMovieId);
        MovieRequestDTO mAct3 = getValidMovie(validMovieId);
        mAct1.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(1l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct2.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(15l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        mAct3.setActors(new ArrayList<>() {
            {
                add(new MovieRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(3l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new MovieRequestDTO.Actor(89l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        
        return new ArrayList<>() {
            {
                //non-existent genres
                add(new Object[]{mGenres1, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres2, getValidCoverImage(validImageContent)});
                add(new Object[]{mGenres3, getValidCoverImage(validImageContent)});
                //non-existent directors
                add(new Object[]{mDir1, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir2, getValidCoverImage(validImageContent)});
                add(new Object[]{mDir3, getValidCoverImage(validImageContent)});
                //non-existent writers
                add(new Object[]{mWri1, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri2, getValidCoverImage(validImageContent)});
                add(new Object[]{mWri3, getValidCoverImage(validImageContent)});
                //non-existent actors
                add(new Object[]{mAct1, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct2, getValidCoverImage(validImageContent)});
                add(new Object[]{mAct3, getValidCoverImage(validImageContent)});
            }
        };
    }
    
    private HttpEntity<MultiValueMap<String, Object>> constructRequest(MovieRequestDTO movie, MockMultipartFile coverImage, String cookie) throws AssertionError {
        HttpHeaders requestHeader = new HttpHeaders();
        HttpHeaders movieHeader = new HttpHeaders();
        HttpHeaders imageHeader = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpEntity<MovieRequestDTO> movieBody;
        HttpEntity<Resource> imageBody;
        
        if (cookie != null) {
            requestHeader.set(HttpHeaders.COOKIE, cookie);
        }
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        movieHeader.setContentType(MediaType.APPLICATION_JSON);
        movieBody = new HttpEntity<>(movie, movieHeader);
        body.add("movie", movieBody);
        if (coverImage != null) {
            try {
                imageHeader.setContentType(MediaType.valueOf(coverImage.getContentType()));
                imageBody = new HttpEntity<>(coverImage.getResource(), imageHeader);
                body.add("cover_image", imageBody);
                
            } catch (Exception ex) {
                throw new AssertionError("Failed to set cover_image part of multipart form data of HttpEntity request", ex);
            }
        }
        
        return new HttpEntity<>(body, requestHeader);
    }
    
    private HttpEntity constructRequest(String cookie) throws AssertionError {
        HttpHeaders requestHeader = new HttpHeaders();
        if (cookie != null) {
            requestHeader.set(HttpHeaders.COOKIE, cookie);
        }
        return new HttpEntity<>(requestHeader);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
    //assert methods
    private void assertMoviesEqual(List<MovieJPA> actual, List<MovieJPA> expected) throws AssertionError {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(expected).isNotNull().isNotEmpty();
        assertThat(actual.size() == expected.size()).isTrue();
        
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getTitle()).isEqualTo(expected.get(i).getTitle());
            assertThat(actual.get(i).getCoverImage()).isEqualTo(expected.get(i).getCoverImage());
            assertThat(actual.get(i).getReleaseDate()).isEqualTo(expected.get(i).getReleaseDate());
            assertThat(actual.get(i).getDescription()).isEqualTo(expected.get(i).getDescription());
            assertThat(actual.get(i).getAudienceRating()).isEqualTo(expected.get(i).getAudienceRating());
            assertThat(actual.get(i).getLength()).isEqualTo(expected.get(i).getLength());
            assertThat(actual.get(i).getCriticRating()).isEqualTo(expected.get(i).getCriticRating());
            
            assertThat(actual.get(i).getGenres()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getGenres().size() == expected.get(i).getGenres().size()).isTrue();
            
            for (int j = 0; j < actual.get(i).getGenres().size(); j++) {
                assertThat(actual.get(i).getGenres().get(j)).isNotNull();
                assertThat(actual.get(i).getGenres().get(j).getId()).isEqualTo(expected.get(i).getGenres().get(j).getId());
                assertThat(actual.get(i).getGenres().get(j).getName()).isEqualTo(expected.get(i).getGenres().get(j).getName());
            }
            
            assertThat(actual.get(i).getDirectors()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getDirectors().size() == expected.get(i).getDirectors().size()).isTrue();
            for (int j = 0; j < actual.get(i).getDirectors().size(); j++) {
                assertThat(actual.get(i).getDirectors().get(j)).isNotNull();
                assertThat(actual.get(i).getDirectors().get(j).getPersonId()).isEqualTo(expected.get(i).getDirectors().get(j).getPersonId());
                assertThat(actual.get(i).getDirectors().get(j).getPerson().getFirstName()).isEqualTo(expected.get(i).getDirectors().get(j).getPerson().getFirstName());
                assertThat(actual.get(i).getDirectors().get(j).getPerson().getLastName()).isEqualTo(expected.get(i).getDirectors().get(j).getPerson().getLastName());
                assertThat(actual.get(i).getDirectors().get(j).getPerson().getGender()).isEqualTo(expected.get(i).getDirectors().get(j).getPerson().getGender());
                assertThat(actual.get(i).getDirectors().get(j).getPerson().getProfilePhoto()).isEqualTo(expected.get(i).getDirectors().get(j).getPerson().getProfilePhoto());
            }
            
            assertThat(actual.get(i).getWriters()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getWriters().size() == expected.get(i).getWriters().size()).isTrue();
            for (int j = 0; j < actual.get(i).getWriters().size(); j++) {
                assertThat(actual.get(i).getWriters().get(j)).isNotNull();
                assertThat(actual.get(i).getWriters().get(j).getPersonId()).isEqualTo(expected.get(i).getWriters().get(j).getPersonId());
                assertThat(actual.get(i).getWriters().get(j).getPerson().getFirstName()).isEqualTo(expected.get(i).getWriters().get(j).getPerson().getFirstName());
                assertThat(actual.get(i).getWriters().get(j).getPerson().getLastName()).isEqualTo(expected.get(i).getWriters().get(j).getPerson().getLastName());
                assertThat(actual.get(i).getWriters().get(j).getPerson().getGender()).isEqualTo(expected.get(i).getWriters().get(j).getPerson().getGender());
                assertThat(actual.get(i).getWriters().get(j).getPerson().getProfilePhoto()).isEqualTo(expected.get(i).getWriters().get(j).getPerson().getProfilePhoto());
            }
            
            assertThat(actual.get(i).getActings()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getActings().size() == expected.get(i).getActings().size()).isTrue();
            for (int j = 0; j < actual.get(i).getActings().size(); j++) {
                assertThat(actual.get(i).getActings().get(j)).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getStarring()).isEqualTo(expected.get(i).getActings().get(j).getStarring());
                
                assertThat(actual.get(i).getActings().get(j).getActor()).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getActor().getPersonId()).isEqualTo(expected.get(i).getActings().get(j).getActor().getPersonId());
                assertThat(actual.get(i).getActings().get(j).getActor().getPerson().getFirstName()).isEqualTo(expected.get(i).getActings().get(j).getActor().getPerson().getFirstName());
                assertThat(actual.get(i).getActings().get(j).getActor().getPerson().getLastName()).isEqualTo(expected.get(i).getActings().get(j).getActor().getPerson().getLastName());
                assertThat(actual.get(i).getActings().get(j).getActor().getPerson().getGender()).isEqualTo(expected.get(i).getActings().get(j).getActor().getPerson().getGender());
                assertThat(actual.get(i).getActings().get(j).getActor().getPerson().getProfilePhoto()).isEqualTo(expected.get(i).getActings().get(j).getActor().getPerson().getProfilePhoto());
                assertThat(actual.get(i).getActings().get(j).getActor().getStar()).isEqualTo(expected.get(i).getActings().get(j).getActor().getStar());
                
                assertThat(actual.get(i).getActings().get(j).getMedia()).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getMedia() == actual.get(i)).isTrue();
                
                assertThat(actual.get(i).getActings().get(j).getRoles()).isNotNull().isNotEmpty();
                assertThat(actual.get(i).getActings().get(j).getRoles().size() == expected.get(i).getActings().get(j).getRoles().size()).isTrue();
                for (int k = 0; k < actual.get(i).getActings().get(j).getRoles().size(); k++) {
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k)).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getId()).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getId().getActing()).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getId().getActing() == actual.get(i).getActings().get(j)).isTrue();
                    
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getId().getId()).isEqualTo(expected.get(i).getActings().get(j).getRoles().get(k).getId().getId());
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getName()).isEqualTo(actual.get(i).getActings().get(j).getRoles().get(k).getName());
                }
            }
            
            assertThat(actual.get(i).getCritiques()).isNotNull();
            assertThat(actual.get(i).getCritiques().size() == expected.get(i).getCritiques().size()).isTrue();
            for (int j = 0; j < actual.get(i).getCritiques().size(); j++) {
                assertThat(actual.get(i).getCritiques().get(j)).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getId()).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic()).isNotNull();
                
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getFirstName()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getFirstName());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getLastName()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getLastName());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getGender()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getGender());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getRole()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getRole());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getProfileName()).isNotEmpty().isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getProfileName());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getProfileImage()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getProfileImage());                
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getUsername()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getUsername());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getPassword()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getPassword());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getEmail()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getEmail());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getCreatedAt()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getCreatedAt());
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getUpdatedAt()).isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getUpdatedAt());
                
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getCountry()).isNotNull();
                assertThat(expected.get(i).getCritiques().get(j).getId().getCritic().getCountry()).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getId().getCritic().getCountry().getId()).isNotNull().isEqualTo(expected.get(i).getCritiques().get(j).getId().getCritic().getCountry().getId());                
                
                assertThat(actual.get(i).getCritiques().get(j).getId().getMedia()).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getId().getMedia() == actual.get(i)).isTrue();
                
                assertThat(actual.get(i).getCritiques().get(j).getDescription()).isNotEmpty().isEqualTo(expected.get(i).getCritiques().get(j).getDescription());
                assertThat(actual.get(i).getCritiques().get(j).getRating()).isNotNull().isEqualTo(expected.get(i).getCritiques().get(j).getRating());
            }
            
        }
    }
    
    private void assertMoviesEqual(MovieResponseDTO actual, MovieRequestDTO expected, String expectedCoverImageUrl) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getCoverImageUrl()).isEqualTo(expectedCoverImageUrl);
        assertThat(actual.getReleaseDate()).isEqualTo(expected.getReleaseDate());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getAudienceRating()).isEqualTo(expected.getAudienceRating());
        assertThat(actual.getLength()).isEqualTo(expected.getLength());
        assertThat(actual.getCriticsRating()).isNull();
        
        assertThat(actual.getGenres()).isNotNull().isNotEmpty();
        assertThat(actual.getGenres().size()).isEqualTo(expected.getGenres().size());
        
        for (int i = 0; i < actual.getGenres().size(); i++) {
            assertThat(actual.getGenres().get(i)).isNotNull();
            assertThat(actual.getGenres().get(i).getId()).isNotNull().isEqualTo(expected.getGenres().get(i));
        }
        
        assertThat(actual.getDirectors()).isNotNull().isNotEmpty();
        assertThat(actual.getDirectors().size()).isEqualTo(expected.getDirectors().size());
        for (int i = 0; i < actual.getDirectors().size(); i++) {
            assertThat(actual.getDirectors().get(i)).isNotNull();
            assertThat(actual.getDirectors().get(i).getId()).isNotNull().isEqualTo(expected.getDirectors().get(i));
        }
        
        assertThat(actual.getWriters()).isNotNull().isNotEmpty();
        assertThat(actual.getWriters().size()).isEqualTo(expected.getWriters().size());
        for (int i = 0; i < actual.getWriters().size(); i++) {
            assertThat(actual.getWriters().get(i)).isNotNull();
            assertThat(actual.getWriters().get(i).getId()).isNotNull().isEqualTo(expected.getWriters().get(i));
        }
        
        assertThat(actual.getActors()).isNotNull().isNotEmpty();
        assertThat(actual.getActors().size()).isEqualTo(expected.getActors().size());
        for (int i = 0; i < actual.getActors().size(); i++) {
            assertThat(actual.getActors().get(i)).isNotNull();
            assertThat(actual.getActors().get(i).getId()).isNotNull().isEqualTo(expected.getActors().get(i).getId());
            assertThat(actual.getActors().get(i).getStarring()).isNotNull().isEqualTo(expected.getActors().get(i).getStarring());
            assertThat(actual.getActors().get(i).getRoles()).isNotNull().isNotEmpty();
            assertThat(actual.getActors().get(i).getRoles().size()).isEqualTo(expected.getActors().get(i).getRoles().size());
            for (int j = 0; j < actual.getActors().get(i).getRoles().size(); j++) {
                assertThat(actual.getActors().get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.getActors().get(i).getRoles().get(j).getId()).isNotNull().isEqualTo(j + 1);
                assertThat(actual.getActors().get(i).getRoles().get(j).getName()).isNotEmpty().isEqualTo(expected.getActors().get(i).getRoles().get(j));
            }
        }
        assertThat(actual.getCritiques()).isNotNull().isEmpty();
    }
    
    private void assertMoviesEqual(MovieJPA actual, MovieResponseDTO expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        if (expected.getCoverImageUrl() == null) {
            assertThat(actual.getCoverImage()).isNull();
        } else {
            assertThat(config.getMediaImagesBaseUrl() + actual.getCoverImage()).isEqualTo(expected.getCoverImageUrl());
        }
        assertThat(actual.getReleaseDate()).isEqualTo(expected.getReleaseDate());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getAudienceRating()).isEqualTo(expected.getAudienceRating());
        assertThat(actual.getLength()).isEqualTo(expected.getLength());
        assertThat(actual.getCriticRating()).isNull();
        
        assertThat(actual.getGenres()).isNotNull().isNotEmpty();
        assertThat(actual.getGenres().size()).isEqualTo(expected.getGenres().size());
        for (int i = 0; i < actual.getGenres().size(); i++) {
            assertThat(actual.getGenres().get(i)).isNotNull();
            assertThat(actual.getGenres().get(i).getId()).isNotNull().isEqualTo(expected.getGenres().get(i).getId());
            assertThat(actual.getGenres().get(i).getName()).isNotNull().isEqualTo(expected.getGenres().get(i).getName());
        }
        
        assertThat(actual.getDirectors()).isNotNull().isNotEmpty();
        assertThat(actual.getDirectors().size()).isEqualTo(expected.getDirectors().size());
        for (int i = 0; i < actual.getDirectors().size(); i++) {
            assertThat(actual.getDirectors().get(i)).isNotNull();
            assertThat(actual.getDirectors().get(i).getPerson()).isNotNull();
            assertThat(actual.getDirectors().get(i).getPersonId()).isNotNull().isEqualTo(expected.getDirectors().get(i).getId());
            assertThat(actual.getDirectors().get(i).getPerson().getFirstName()).isEqualTo(expected.getDirectors().get(i).getFirstName());
            assertThat(actual.getDirectors().get(i).getPerson().getLastName()).isEqualTo(expected.getDirectors().get(i).getLastName());
            assertThat(actual.getDirectors().get(i).getPerson().getGender()).isEqualTo(expected.getDirectors().get(i).getGender());
            if (actual.getDirectors().get(i).getPerson().getProfilePhoto() == null) {
                assertThat(actual.getDirectors().get(i).getPerson().getProfilePhoto()).isEqualTo(expected.getDirectors().get(i).getProfilePhotoUrl());
            } else {
                assertThat(config.getPersonImagesBaseUrl() + actual.getDirectors().get(i).getPerson().getProfilePhoto()).isEqualTo(expected.getDirectors().get(i).getProfilePhotoUrl());
            }
        }
        
        assertThat(actual.getWriters()).isNotNull().isNotEmpty();
        assertThat(actual.getWriters().size()).isEqualTo(expected.getWriters().size());
        for (int i = 0; i < actual.getWriters().size(); i++) {
            assertThat(actual.getWriters().get(i)).isNotNull();
            assertThat(actual.getWriters().get(i).getPerson()).isNotNull();
            assertThat(actual.getWriters().get(i).getPersonId()).isNotNull().isEqualTo(expected.getWriters().get(i).getId());
            assertThat(actual.getWriters().get(i).getPerson().getFirstName()).isEqualTo(expected.getWriters().get(i).getFirstName());
            assertThat(actual.getWriters().get(i).getPerson().getLastName()).isEqualTo(expected.getWriters().get(i).getLastName());
            assertThat(actual.getWriters().get(i).getPerson().getGender()).isEqualTo(expected.getWriters().get(i).getGender());
            if (actual.getWriters().get(i).getPerson().getProfilePhoto() == null) {
                assertThat(actual.getWriters().get(i).getPerson().getProfilePhoto()).isEqualTo(expected.getWriters().get(i).getProfilePhotoUrl());
            } else {
                assertThat(config.getPersonImagesBaseUrl() + actual.getWriters().get(i).getPerson().getProfilePhoto()).isEqualTo(expected.getWriters().get(i).getProfilePhotoUrl());
            }
            
        }
        
        assertThat(actual.getActings()).isNotNull().isNotEmpty();
        assertThat(actual.getActings().size()).isEqualTo(expected.getActors().size());
        for (int i = 0; i < actual.getActings().size(); i++) {
            assertThat(actual.getActings().get(i)).isNotNull();
            assertThat(actual.getActings().get(i).getActor()).isNotNull();
            assertThat(actual.getActings().get(i).getActor().getPerson()).isNotNull();
            assertThat(actual.getActings().get(i).getActor().getPersonId()).isNotNull().isEqualTo(expected.getActors().get(i).getId());
            assertThat(actual.getActings().get(i).getActor().getPerson().getFirstName()).isEqualTo(expected.getActors().get(i).getFirstName());
            assertThat(actual.getActings().get(i).getActor().getPerson().getLastName()).isEqualTo(expected.getActors().get(i).getLastName());
            assertThat(actual.getActings().get(i).getActor().getPerson().getGender()).isEqualTo(expected.getActors().get(i).getGender());
            assertThat(actual.getActings().get(i).getActor().getStar()).isEqualTo(expected.getActors().get(i).getStar());
            if (actual.getActings().get(i).getActor().getPerson().getProfilePhoto() == null) {
                assertThat(actual.getActings().get(i).getActor().getPerson().getProfilePhoto()).isEqualTo(expected.getActors().get(i).getProfilePhotoUrl());
            } else {
                assertThat(config.getPersonImagesBaseUrl() + actual.getActings().get(i).getActor().getPerson().getProfilePhoto()).isEqualTo(expected.getActors().get(i).getProfilePhotoUrl());
            }
            
            assertThat(actual.getActings().get(i).getStarring()).isNotNull().isEqualTo(expected.getActors().get(i).getStarring());
            assertThat(actual.getActings().get(i).getRoles()).isNotNull().isNotEmpty();
            assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActors().get(i).getRoles().size());
            for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getId().getId()).isNotNull().isEqualTo(expected.getActors().get(i).getRoles().get(j).getId());
                assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isNotEmpty().isEqualTo(expected.getActors().get(i).getRoles().get(j).getName());
            }
        }
        assertThat(actual.getCritiques()).isNotNull().isEmpty();
        
    }
    
    private void assertMoviesEqual(MovieJPA actual, MovieRequestDTO expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isNotBlank().isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isNotBlank().isEqualTo(expected.getDescription());
        assertThat(actual.getReleaseDate()).isNotNull().isEqualTo(expected.getReleaseDate());
        assertThat(actual.getAudienceRating()).isNotNull().isEqualTo(expected.getAudienceRating());
        assertThat(actual.getLength()).isNotNull().isEqualTo(expected.getLength());
        
        assertThat(actual.getGenres()).isNotNull().isNotEmpty();
        assertThat(actual.getGenres().size()).isEqualTo(expected.getGenres().size());
        for (int i = 0; i < actual.getGenres().size(); i++) {
            assertThat(actual.getGenres().get(i)).isNotNull();
            assertThat(actual.getGenres().get(i).getId()).isNotNull().isEqualTo(expected.getGenres().get(i));
        }
        
        assertThat(actual.getDirectors()).isNotNull().isNotEmpty();
        assertThat(actual.getDirectors().size()).isEqualTo(expected.getDirectors().size());
        for (int i = 0; i < actual.getDirectors().size(); i++) {
            assertThat(actual.getDirectors().get(i)).isNotNull();
            assertThat(actual.getDirectors().get(i).getPersonId()).isNotNull().isEqualTo(expected.getDirectors().get(i));
        }
        
        assertThat(actual.getWriters()).isNotNull().isNotEmpty();
        assertThat(actual.getWriters().size()).isEqualTo(expected.getWriters().size());
        for (int i = 0; i < actual.getWriters().size(); i++) {
            assertThat(actual.getWriters().get(i)).isNotNull();
            assertThat(actual.getWriters().get(i).getPersonId()).isNotNull().isEqualTo(expected.getWriters().get(i));
        }
        
        assertThat(actual.getActings()).isNotNull().isNotEmpty();
        assertThat(actual.getActings().size()).isEqualTo(expected.getActors().size());
        for (int i = 0; i < actual.getActings().size(); i++) {
            assertThat(actual.getActings().get(i)).isNotNull();
            assertThat(actual.getActings().get(i).getActor()).isNotNull();
            assertThat(actual.getActings().get(i).getActor().getPersonId()).isNotNull().isEqualTo(expected.getActors().get(i).getId());
            
            assertThat(actual.getActings().get(i).getStarring()).isNotNull().isEqualTo(expected.getActors().get(i).getStarring());
            assertThat(actual.getActings().get(i).getRoles()).isNotNull().isNotEmpty();
            assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActors().get(i).getRoles().size());
            for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getId().getId()).isNotNull().isEqualTo(j + 1);
                assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isNotEmpty().isEqualTo(expected.getActors().get(i).getRoles().get(j));
            }
        }
        assertThat(actual.getCritiques()).isNotNull().isEmpty();
    }
    
    private void assertImagesEqual(List<Resource> actual, List<Resource> expected) throws AssertionError {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(expected).isNotNull().isNotEmpty();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(expected.get(i)).isNotNull();
            assertThat(actual.get(i).exists()).isTrue().isEqualTo(expected.get(i).exists());
            assertThat(actual.get(i).isFile()).isTrue().isEqualTo(expected.get(i).isFile());
            assertThat(actual.get(i).isReadable()).isTrue().isEqualTo(expected.get(i).isReadable());
            assertThat(actual.get(i).getFilename()).isNotBlank().isEqualTo(expected.get(i).getFilename());
            try {
                assertThat(actual.get(i).getContentAsByteArray()).isEqualTo(expected.get(i).getContentAsByteArray());
            } catch (IOException ex) {
                fail("Unable to read resource bytes", ex);
            }
        }
    }
    
    private void assertImagesEqual(Resource actual, MockMultipartFile expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.exists()).isTrue().isEqualTo(expected.getResource().exists());
        assertThat(actual.isReadable()).isTrue().isEqualTo(expected.getResource().isReadable());
        try {
            assertThat(actual.getContentAsByteArray()).isEqualTo(expected.getResource().getContentAsByteArray());
        } catch (IOException ex) {
            fail("Unable to read resource bytes", ex);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
    private boolean areImagesEqual(Resource actual, MockMultipartFile expected) {
        try {
            assertImagesEqual(actual, expected);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
    
    private boolean areMoviesEqual(MovieJPA actual, MovieRequestDTO expected) {
        try {
            assertMoviesEqual(actual, expected);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
    
    private boolean existsCoverImage(String filename) {
        if (filename != null) {
            try {
                Resource r = fileRepo.getMediaCoverImage(filename);
                return r.exists() && r.isReadable();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    private String getExtensionWithDot(String filename) throws IllegalArgumentException {
        return "." + MyImage.extractNameAndExtension(filename)[1];
    }
    
    private List<Resource> getAllCoverImages() {
        return testUtil.getAllMediaCoverImages();
    }
    
    private List<MovieJPA> getAllMovies() {
        return movieRepo.findAll();
    }
    
    private void changeAttributes(MovieRequestDTO movie) {
        movie.setTitle("Dummy PUT title");
        movie.setDescription("Dummy PUT description");
        movie.setReleaseDate(LocalDate.now());
        movie.setLength(90);
        movie.setAudienceRating(59);
        movie.setGenres(new ArrayList<>() {
            {
                add(1l);
                add(6l);
            }
        });
        movie.setDirectors(new ArrayList<>() {
            {
                add(32l);
                add(33l);
            }
        });
        movie.setWriters(new ArrayList<>() {
            {
                add(26l);
                add(27l);
            }
        });
        MovieRequestDTO.Actor a1 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a2 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a3 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a4 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a5 = new MovieRequestDTO.Actor();
        MovieRequestDTO.Actor a6 = new MovieRequestDTO.Actor();
        a1.setId(2l);
        a1.setStarring(true);
        a1.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
                add("Dummy PUT role 2");
                add("Dummy PUT role 3");
            }
        });
        a2.setId(6l);
        a2.setStarring(true);
        a2.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
                add("Dummy PUT role 2");
            }
        });
        a3.setId(12l);
        a3.setStarring(true);
        a3.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
            }
        });
        a4.setId(24l);
        a4.setStarring(false);
        a4.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
                add("Dummy PUT role 2");
                add("Dummy PUT role 3");
            }
        });
        a5.setId(36l);
        a5.setStarring(false);
        a5.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
                add("Dummy PUT role 2");
            }
        });
        a6.setId(44l);
        a6.setStarring(false);
        a6.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
            }
        });
        movie.setActors(new ArrayList<>() {
            {
                add(a1);
                add(a2);
                add(a3);
                add(a4);
                add(a5);
                add(a6);
            }
        });
    }
    
}
