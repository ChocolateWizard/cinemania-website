/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.tv.TVShowRequestDTO;
import com.borak.kinweb.backend.domain.dto.tv.TVShowResponseDTO;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.logic.security.JwtUtils;
import com.borak.kinweb.backend.repository.jdbc.TVShowRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.UserRepositoryJDBC;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import org.springframework.core.io.UrlResource;
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

//static imports
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(10)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TVShowSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TVShowRepositoryJDBC tvShowRepo;
    @Autowired
    private UserRepositoryJDBC userRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private FileRepository fileRepo;
    @Autowired
    private ConfigProperties config;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/tv";

    static {
        testsPassed.put("postTVShow_UnauthenticatedUser_DoesNotCreateTVShowReturns401", false);
        testsPassed.put("postTVShow_UnauthorizedUser_DoesNotCreateTVShowReturns403", false);
        testsPassed.put("postTVShow_InvalidInputData_DoesNotCreateTVShowReturns400", false);
        testsPassed.put("postTVShow_NonexistentDependencyData_DoesNotCreateTVShowReturns404", false);
        testsPassed.put("postTVShow_ValidInput_CreatesTVShowReturns200", false);

        testsPassed.put("putTVShow_UnauthenticatedUser_DoesNotUpdateTVShowReturns401", false);
        testsPassed.put("putTVShow_UnauthorizedUser_DoesNotUpdateTVShowReturns403", false);
        testsPassed.put("putTVShow_InvalidInputData_DoesNotUpdateTVShowReturns400", false);
        testsPassed.put("putTVShow_NonexistentDependencyData_DoesNotUpdateTVShowReturns404", false);
        testsPassed.put("putTVShow_ValidInput_UpdatesTVShowReturns200", false);

        testsPassed.put("deleteTVShow_UnauthenticatedUser_DoesNotDeleteTVShowReturns401", false);
        testsPassed.put("deleteTVShow_UnauthorizedUser_DoesNotDeleteTVShowReturns403", false);
        testsPassed.put("deleteTVShow_InvalidInputData_DoesNotDeleteTVShowReturns400", false);
        testsPassed.put("deleteTVShow_NonexistentDependencyData_DoesNotDeleteTVShowReturns404", false);
        testsPassed.put("deleteTVShow_ValidInput_DeletesTVShowReturns200", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

//=========================================================================================================
    @BeforeEach
    void beforeEach() {
//        Assumptions.assumeTrue(TestResultsHelper.didAuthRoutesTestPass());
    }

    //=========================================================================================================
    //POST
    @Test
    @Order(1)
    @DisplayName("Tests whether POST request to /api/tv with unauthenticated user did not create new tv show and it returned 401")
    void postTVShow_UnauthenticatedUser_DoesNotCreateTVShowReturns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        TVShowRequestDTO tvShowValid = getValidTVShow(9l);
        MockMultipartFile imageValid = getValidCoverImage(getRandomString(20));
        int i = 0;
        try {
            for (String username : getNonExistentUsernames()) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();

                //no cookie
                request = constructRequest(tvShowValid, imageValid, null);
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(tvShowValid, imageValid, getRandomString(50));
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(tvShowValid, imageValid, jwt);
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(tvShowValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postTVShow_UnauthenticatedUser_DoesNotCreateTVShowReturns401", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/tv with authenticated but not authorized user did not create new tv show and it returned 403")
    void postTVShow_UnauthorizedUser_DoesNotCreateTVShowReturns403() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        TVShowRequestDTO tvShowValid = getValidTVShow(9l);
        MockMultipartFile imageValid = getValidCoverImage(getRandomString(20));
        int i = 0;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(tvShowValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postTVShow_UnauthorizedUser_DoesNotCreateTVShowReturns403", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/tv with invalid input data did not create new tv show and it returned 400")
    void postTVShow_InvalidInputData_DoesNotCreateTVShowReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        int i = 0;
        try {
            for (Object[] input : getBadRequestTVShowsAndImages(9l, getRandomString(20))) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest((TVShowRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postTVShow_InvalidInputData_DoesNotCreateTVShowReturns400", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/tv with non-existent dependency objects did not create new tv show and it returned 404")
    void postTVShow_NonexistentDependencyData_DoesNotCreateTVShowReturns404() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        int i = 0;
        try {
            for (Object[] input : getNonExistentDependencyTVShowsAndImages(9l, getRandomString(20))) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest((TVShowRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postTVShow_NonexistentDependencyData_DoesNotCreateTVShowReturns404", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to /api/tv with valid input data did create new tv show and it returned 200")
    void postTVShow_ValidInput_CreatesTVShowReturns200() {
        HttpEntity request;
        ResponseEntity<TVShowResponseDTO> response;

        TVShowRequestDTO tvShowValid1 = getValidTVShow(9l);
        TVShowRequestDTO tvShowValid2 = getValidTVShow(null);

        MockMultipartFile imageValid1 = getValidCoverImage(getRandomString(20));
        MockMultipartFile imageValid2 = getValidCoverImage(getRandomString(20));

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where tvShowId is set
        assertThat(tvShowRepo.existsById(tvShowValid1.getId())).isFalse();
        assertThat(existsCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isFalse();

        request = constructRequest(tvShowValid1, imageValid1, cookie.toString());
        response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(tvShowValid1.getId())).isTrue();
        assertThat(existsCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();

        Optional<TVShowJDBC> actualDBTVShow = tvShowRepo.findByIdWithRelations(tvShowValid1.getId());
        Resource actualDBImage = fileRepo.getMediaCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(actualDBTVShow).isNotNull();
        assertThat(actualDBTVShow.isPresent()).isTrue();
        String coverImageUrl = config.getMediaImagesBaseUrl() + tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename());
        assertTVShowsEqual(response.getBody(), tvShowValid1, coverImageUrl);
        assertTVShowsEqual(actualDBTVShow.get(), response.getBody());
        assertImagesEqual(actualDBImage, imageValid1);

        //----------------------------------------------------------------------------
        //second valid request where tvShowId is null
        request = constructRequest(tvShowValid2, imageValid2, cookie.toString());
        response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(response.getBody().getId())).isTrue();
        assertThat(existsCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()))).isTrue();

        actualDBTVShow = tvShowRepo.findByIdWithRelations(response.getBody().getId());
        actualDBImage = fileRepo.getMediaCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()));
        assertThat(actualDBTVShow).isNotNull();
        assertThat(actualDBTVShow.isPresent()).isTrue();
        tvShowValid2.setId(response.getBody().getId());
        coverImageUrl = config.getMediaImagesBaseUrl() + tvShowValid2.getId() + getExtensionWithDot(imageValid2.getOriginalFilename());
        assertTVShowsEqual(response.getBody(), tvShowValid2, coverImageUrl);
        assertTVShowsEqual(actualDBTVShow.get(), response.getBody());
        assertImagesEqual(actualDBImage, imageValid2);

        testsPassed.put("postTVShow_ValidInput_CreatesTVShowReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(6)
    @DisplayName("Tests whether PUT request to /api/tv with unauthenticated user did not update tv show and it returned 401")
    void putTVShow_UnauthenticatedUser_DoesNotUpdateTVShowReturns401() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        TVShowRequestDTO tvShowValid = getValidTVShow(9l);
        MockMultipartFile imageValid = getValidCoverImage(getRandomString(40));
        int i = 0;
        try {
            for (String username : getNonExistentUsernames()) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();

                //no cookie
                request = constructRequest(tvShowValid, imageValid, null);
                response = restTemplate.exchange(ROUTE + "/" + tvShowValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(tvShowValid, imageValid, getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + tvShowValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(tvShowValid, imageValid, jwt);
                response = restTemplate.exchange(ROUTE + "/" + tvShowValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(tvShowValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + tvShowValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putTVShow_UnauthenticatedUser_DoesNotUpdateTVShowReturns401", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests whether PUT request to /api/tv with authenticated but unauthorized user did not update tv show and it returned 403")
    void putTVShow_UnauthorizedUser_DoesNotUpdateTVShowReturns403() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        TVShowRequestDTO tvShowValid = getValidTVShow(9l);
        MockMultipartFile imageValid = getValidCoverImage(getRandomString(40));
        int i = 0;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(tvShowValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + tvShowValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }

        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putTVShow_UnauthorizedUser_DoesNotUpdateTVShowReturns403", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether PUT request to /api/tv with invalid input data did not update tv show and it returned 400")
    void putTVShow_InvalidInputData_DoesNotUpdateTVShowReturns400() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        int i = 0;
        try {
            for (Object[] input : getBadRequestTVShowsAndImages(9l, getRandomString(40))) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest((TVShowRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + ((TVShowRequestDTO) input[0]).getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            i = 0;
            TVShowRequestDTO tvShowValid = getValidTVShow(9l);
            MockMultipartFile imageValid = getValidCoverImage(getRandomString(40));
            for (long invalidId : new long[]{0l, -1l, -2l, -5l, -10l, -23l, Long.MIN_VALUE}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest(tvShowValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putTVShow_InvalidInputData_DoesNotUpdateTVShowReturns400", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether PUT request to /api/tv with non-existent dependency objects did not update tv show and it returned 404")
    void putTVShow_NonexistentDependencyData_DoesNotUpdateTVShowReturns404() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        int i = 0;
        try {
            for (Object[] input : getNonExistentDependencyTVShowsAndImages(9l, getRandomString(40))) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest((TVShowRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + ((TVShowRequestDTO) input[0]).getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            i = 0;
            TVShowRequestDTO tvShowValid = getValidTVShow(9l);
            MockMultipartFile imageValid = getValidCoverImage(getRandomString(40));
            for (long invalidId : new long[]{4l, 1l, 2l, 50l, 51l, 101l, Long.MAX_VALUE}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest(tvShowValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putTVShow_NonexistentDependencyData_DoesNotUpdateTVShowReturns404", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests whether PUT request to /api/tv with valid input data did update tv show and it returned 200")
    void putTVShow_ValidInput_UpdatesTVShowReturns200() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));

        HttpEntity request;
        ResponseEntity<TVShowResponseDTO> response;

        TVShowRequestDTO tvShowValid1 = getValidTVShow(9l);
        TVShowRequestDTO tvShowValid2 = getValidTVShow(null);
        TVShowRequestDTO tvShowValid3 = getValidTVShow(10l);

        changeAttributes(tvShowValid1);
        changeAttributes(tvShowValid2);
        changeAttributes(tvShowValid3);

        MockMultipartFile imageValid1 = getValidCoverImage(getRandomString(40));
        MockMultipartFile imageValid2 = getValidCoverImage(getRandomString(50));

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where tv show has different valid attributes and ID and image are set
        assertThat(tvShowRepo.existsById(tvShowValid1.getId())).isTrue();
        assertThat(existsCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        Optional<TVShowJDBC> actualDBTVShow = tvShowRepo.findByIdWithRelations(tvShowValid1.getId());
        Resource actualDBImage = fileRepo.getMediaCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(areTVShowsEqual(actualDBTVShow.get(), tvShowValid1)).isFalse();
        assertThat(areImagesEqual(actualDBImage, imageValid1)).isFalse();

        request = constructRequest(tvShowValid1, imageValid1, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + tvShowValid1.getId(), HttpMethod.PUT, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(tvShowValid1.getId())).isTrue();
        assertThat(existsCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        actualDBTVShow = tvShowRepo.findByIdWithRelations(tvShowValid1.getId());
        actualDBImage = fileRepo.getMediaCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(areTVShowsEqual(actualDBTVShow.get(), tvShowValid1)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid1)).isTrue();

        String coverImageUrl = config.getMediaImagesBaseUrl() + tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename());
        assertTVShowsEqual(response.getBody(), tvShowValid1, coverImageUrl);
        assertTVShowsEqual(actualDBTVShow.get(), response.getBody());

        //----------------------------------------------------------------------------
        //second valid request where tv show has different valid attributes and ID is null and image has more bytes than the previous updated one
        //check if tvShowValid1 object is present in database and is the same as tvShowValid2
        //and that imageValid2 is different than the database imageValid1
        assertThat(tvShowRepo.existsById(tvShowValid1.getId())).isTrue();
        assertThat(existsCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        actualDBTVShow = tvShowRepo.findByIdWithRelations(tvShowValid1.getId());
        actualDBImage = fileRepo.getMediaCoverImage(tvShowValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        tvShowValid2.setId(tvShowValid1.getId());
        assertThat(areTVShowsEqual(actualDBTVShow.get(), tvShowValid2)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid2)).isFalse();

        //make request
        tvShowValid2.setId(null);
        request = constructRequest(tvShowValid2, imageValid2, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + tvShowValid1.getId(), HttpMethod.PUT, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(response.getBody().getId())).isTrue();
        assertThat(existsCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()))).isTrue();

        actualDBTVShow = tvShowRepo.findByIdWithRelations(response.getBody().getId());
        actualDBImage = fileRepo.getMediaCoverImage(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()));
        assertThat(actualDBTVShow).isNotNull();
        assertThat(actualDBTVShow.isPresent()).isTrue();
        tvShowValid2.setId(response.getBody().getId());
        assertThat(areTVShowsEqual(actualDBTVShow.get(), tvShowValid2)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid2)).isTrue();

        coverImageUrl = config.getMediaImagesBaseUrl() + tvShowValid2.getId() + getExtensionWithDot(imageValid2.getOriginalFilename());
        assertTVShowsEqual(response.getBody(), tvShowValid2, coverImageUrl);
        assertTVShowsEqual(actualDBTVShow.get(), response.getBody());
        //----------------------------------------------------------------------------
        //third valid request where tv show has different valid attributes, ID is 10 and image is null
        //check if image that was in database was deleted after successful PUT request
        assertThat(tvShowRepo.existsById(tvShowValid3.getId())).isTrue();
        actualDBTVShow = tvShowRepo.findByIdWithRelations(tvShowValid3.getId());
        assertThat(areTVShowsEqual(actualDBTVShow.get(), tvShowValid3)).isFalse();
        assertThat(existsCoverImage(actualDBTVShow.get().getCoverImage())).isTrue();

        request = constructRequest(tvShowValid3, null, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + tvShowValid3.getId(), HttpMethod.PUT, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(tvShowValid3.getId())).isTrue();
        assertThat(existsCoverImage(actualDBTVShow.get().getCoverImage())).isFalse();
        actualDBTVShow = tvShowRepo.findByIdWithRelations(tvShowValid3.getId());
        assertThat(areTVShowsEqual(actualDBTVShow.get(), tvShowValid3)).isTrue();

        assertTVShowsEqual(response.getBody(), tvShowValid3, null);
        assertTVShowsEqual(actualDBTVShow.get(), response.getBody());

        testsPassed.put("putTVShow_ValidInput_UpdatesTVShowReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(11)
    @DisplayName("Tests whether DELETE request to /api/tv with unauthenticated user did not delete tv show and it returned 401")
    void deleteTVShow_UnauthenticatedUser_DoesNotDeleteTVShowReturns401() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putTVShow_ValidInput_UpdatesTVShowReturns200"));

        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        int i = 0;
        long tvShowValidId = 9l;
        try {
            for (String username : getNonExistentUsernames()) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();

                //no cookie
                request = constructRequest(null);
                response = restTemplate.exchange(ROUTE + "/" + tvShowValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + tvShowValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(jwt);
                response = restTemplate.exchange(ROUTE + "/" + tvShowValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + tvShowValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteTVShow_UnauthenticatedUser_DoesNotDeleteTVShowReturns401", true);
    }

    @Test
    @Order(12)
    @DisplayName("Tests whether DELETE request to /api/tv with authenticated but unauthorized user did not delete tv show and it returned 403")
    void deleteTVShow_UnauthorizedUser_DoesNotDeleteTVShowReturns403() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putTVShow_ValidInput_UpdatesTVShowReturns200"));

        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        int i = 0;
        long tvShowValidId = 9l;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + tvShowValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteTVShow_UnauthorizedUser_DoesNotDeleteTVShowReturns403", true);
    }

    @Test
    @Order(13)
    @DisplayName("Tests whether DELETE request to /api/tv with invalid input data did not delete tv show and it returned 400")
    void deleteTVShow_InvalidInputData_DoesNotDeleteTVShowReturns400() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putTVShow_ValidInput_UpdatesTVShowReturns200"));

        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        int i = 0;
        try {
            for (long input : new long[]{0l, -1l, -2l, -5l, -10l, -23l, Long.MIN_VALUE}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + input, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteTVShow_InvalidInputData_DoesNotDeleteTVShowReturns400", true);
    }

    @Test
    @Order(14)
    @DisplayName("Tests whether DELETE request to /api/tv with non-existent dependency objects did not delete tv show and it returned 404")
    void deleteTVShow_NonexistentDependencyData_DoesNotDeleteTVShowReturns404() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putTVShow_ValidInput_UpdatesTVShowReturns200"));

        HttpEntity request;
        ResponseEntity<String> response;
        List<TVShowJDBC> tvShowsBefore;
        List<TVShowJDBC> tvShowsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        int i = 0;
        try {
            for (long invalidId : new long[]{4l, 1l, 2l, 50l, 51l, 101l, Long.MAX_VALUE}) {
                tvShowsBefore = getAllTVShows();
                imagesBefore = getAllCoverImages();
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                tvShowsAfter = getAllTVShows();
                imagesAfter = getAllCoverImages();
                assertTVShowsEqual(tvShowsAfter, tvShowsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteTVShow_NonexistentDependencyData_DoesNotDeleteTVShowReturns404", true);
    }

    @Test
    @Order(15)
    @DisplayName("Tests whether DELETE request to /api/tv with valid input data did delete tv show and it returned 200")
    void deleteTVShow_ValidInput_DeletesTVShowReturns200() {
        Assumptions.assumeTrue(testsPassed.get("postTVShow_ValidInput_CreatesTVShowReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putTVShow_ValidInput_UpdatesTVShowReturns200"));

        HttpEntity request;
        ResponseEntity<TVShowResponseDTO> response;

        long tvShowValidId1 = 9l;
        long tvShowValidId2 = 10l;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where movieId is 7
        assertThat(tvShowRepo.existsById(tvShowValidId1)).isTrue();
        Optional<TVShowJDBC> tvShowDB = tvShowRepo.findByIdWithRelations(tvShowValidId1);
        assertThat(existsCoverImage(tvShowDB.get().getCoverImage())).isTrue();

        request = constructRequest(cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + tvShowValidId1, HttpMethod.DELETE, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(tvShowValidId1)).isFalse();
        assertThat(existsCoverImage(tvShowDB.get().getCoverImage())).isFalse();
        assertTVShowsEqual(tvShowDB.get(), response.getBody());

        //----------------------------------------------------------------------------
        //second valid request where movieId is 8
        assertThat(tvShowRepo.existsById(tvShowValidId2)).isTrue();
        tvShowDB = tvShowRepo.findByIdWithRelations(tvShowValidId2);
        assertThat(existsCoverImage(tvShowDB.get().getCoverImage())).isFalse();

        request = constructRequest(cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + tvShowValidId2, HttpMethod.DELETE, request, TVShowResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(tvShowRepo.existsById(tvShowValidId2)).isFalse();
        assertThat(existsCoverImage(tvShowDB.get().getCoverImage())).isFalse();
        assertTVShowsEqual(tvShowDB.get(), response.getBody());

        testsPassed.put("deleteTVShow_ValidInput_DeletesTVShowReturns200", true);
    }

    //=========================================================================================================
    //PRIVATE METHODS
    private String[] getNonExistentUsernames() {
        return new String[]{"dummy user 1", "dummy user 2", "dummy user 4", "admina", "critic1", "dummy user 6"};
    }

    private String getRandomString(int length) {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private TVShowRequestDTO getValidTVShow(Long tvShowId) {
        TVShowRequestDTO tvShow = new TVShowRequestDTO();
        tvShow.setId(tvShowId);
        tvShow.setTitle("Dummy title");
        tvShow.setDescription("Dummy description");
        tvShow.setReleaseDate(LocalDate.now());
        tvShow.setAudienceRating(89);
        tvShow.setNumberOfSeasons(2);
        tvShow.setGenres(new ArrayList<>() {
            {
                add(1l);
                add(4l);
                add(5l);
            }
        });
        tvShow.setDirectors(new ArrayList<>() {
            {
                add(1l);
                add(26l);
            }
        });
        tvShow.setWriters(new ArrayList<>() {
            {
                add(19l);
                add(26l);
                add(34l);
            }
        });
        TVShowRequestDTO.Actor a1 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a2 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a3 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a4 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a5 = new TVShowRequestDTO.Actor();
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
        tvShow.getActors().add(a1);
        tvShow.getActors().add(a2);
        tvShow.getActors().add(a3);
        tvShow.getActors().add(a4);
        tvShow.getActors().add(a5);
        return tvShow;
    }

    private MockMultipartFile getValidCoverImage(String content) {
        return new MockMultipartFile("cover_image", "tv_show_routes.png", "image/png", content.getBytes(StandardCharsets.UTF_8));
    }

    private List<Object[]> getBadRequestTVShowsAndImages(Long validTVShowId, String validImageContent) {
        TVShowRequestDTO tvTitle1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvTitle2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvTitle3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvTitle4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvTitle5 = getValidTVShow(validTVShowId);
        tvTitle1.setTitle(null);
        tvTitle2.setTitle("");
        tvTitle3.setTitle(" ");
        tvTitle4.setTitle("         ");
        tvTitle5.setTitle(getRandomString(301));

        TVShowRequestDTO tvDescription1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDescription2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDescription3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDescription4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDescription5 = getValidTVShow(validTVShowId);
        tvDescription1.setDescription(null);
        tvDescription2.setDescription("");
        tvDescription3.setDescription(" ");
        tvDescription4.setDescription("         ");
        tvDescription5.setDescription(getRandomString(1001));

        TVShowRequestDTO tvReleaseDate1 = getValidTVShow(validTVShowId);
        tvReleaseDate1.setReleaseDate(null);

        TVShowRequestDTO tvAudienceRating1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating5 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating6 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating7 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating8 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating9 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating10 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating11 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating12 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAudienceRating13 = getValidTVShow(validTVShowId);
        tvAudienceRating1.setAudienceRating(null);
        tvAudienceRating2.setAudienceRating(-1);
        tvAudienceRating3.setAudienceRating(-2);
        tvAudienceRating4.setAudienceRating(-5);
        tvAudienceRating5.setAudienceRating(-10);
        tvAudienceRating6.setAudienceRating(-26);
        tvAudienceRating7.setAudienceRating(Integer.MIN_VALUE);
        tvAudienceRating8.setAudienceRating(101);
        tvAudienceRating9.setAudienceRating(102);
        tvAudienceRating10.setAudienceRating(105);
        tvAudienceRating11.setAudienceRating(110);
        tvAudienceRating12.setAudienceRating(126);
        tvAudienceRating13.setAudienceRating(Integer.MAX_VALUE);

        TVShowRequestDTO tvSeasons1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvSeasons2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvSeasons3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvSeasons4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvSeasons5 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvSeasons6 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvSeasons7 = getValidTVShow(validTVShowId);
        tvSeasons1.setNumberOfSeasons(null);
        tvSeasons2.setNumberOfSeasons(-1);
        tvSeasons3.setNumberOfSeasons(-2);
        tvSeasons4.setNumberOfSeasons(-5);
        tvSeasons5.setNumberOfSeasons(-10);
        tvSeasons6.setNumberOfSeasons(-25);
        tvSeasons7.setNumberOfSeasons(Integer.MIN_VALUE);

        TVShowRequestDTO tvGenres1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres5 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres6 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres7 = getValidTVShow(validTVShowId);
        tvGenres1.setGenres(null);
        tvGenres2.setGenres(new ArrayList<>());
        tvGenres3.setGenres(new ArrayList<>() {
            {
                add(null);
            }
        });
        tvGenres4.setGenres(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        tvGenres5.setGenres(new ArrayList<>() {
            {
                add(0l);
            }
        });
        tvGenres6.setGenres(new ArrayList<>() {
            {
                add(0l);
                add(-1l);
                add(-2l);
            }
        });
        tvGenres7.setGenres(new ArrayList<>() {
            {
                add(Long.MIN_VALUE);
                add(-1l);
                add(-2l);
            }
        });

        TVShowRequestDTO tvDir1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir5 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir6 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir7 = getValidTVShow(validTVShowId);
        tvDir1.setDirectors(null);
        tvDir2.setDirectors(new ArrayList<>());
        tvDir3.setDirectors(new ArrayList<>() {
            {
                add(null);
            }
        });
        tvDir4.setDirectors(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        tvDir5.setDirectors(new ArrayList<>() {
            {
                add(0l);
            }
        });
        tvDir6.setDirectors(new ArrayList<>() {
            {
                add(0l);
                add(-1l);
                add(-2l);
            }
        });
        tvDir7.setDirectors(new ArrayList<>() {
            {
                add(Long.MIN_VALUE);
                add(-1l);
                add(-2l);
            }
        });

        TVShowRequestDTO tvWri1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri5 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri6 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri7 = getValidTVShow(validTVShowId);
        tvWri1.setWriters(null);
        tvWri2.setWriters(new ArrayList<>());
        tvWri3.setWriters(new ArrayList<>() {
            {
                add(null);
            }
        });
        tvWri4.setWriters(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        tvWri5.setWriters(new ArrayList<>() {
            {
                add(0l);
            }
        });
        tvWri6.setWriters(new ArrayList<>() {
            {
                add(0l);
                add(-1l);
                add(-2l);
            }
        });
        tvWri7.setWriters(new ArrayList<>() {
            {
                add(Long.MIN_VALUE);
                add(-1l);
                add(-2l);
            }
        });

        TVShowRequestDTO tvAct1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct3 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct4 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct5 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct6 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct7 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct8 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct9 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct10 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct11 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct12 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct13 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct14 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct15 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct16 = getValidTVShow(validTVShowId);
        tvAct1.setActors(null);
        tvAct2.setActors(new ArrayList<>());
        tvAct3.setActors(new ArrayList<>() {
            {
                add(null);
            }
        });
        tvAct4.setActors(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        tvAct5.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(null, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct6.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(0l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct7.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(-1l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(-2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(-3l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct8.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(-1l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(-2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(Long.MIN_VALUE, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct9.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, null, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct10.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, null));
            }
        });
        tvAct11.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>()));
            }
        });
        tvAct12.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add(null);
                    }
                }));
            }
        });
        tvAct13.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("");
                    }
                }));
            }
        });
        tvAct14.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add(" ");
                    }
                }));
            }
        });
        tvAct15.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("       ");
                    }
                }));
            }
        });
        tvAct16.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add(getRandomString(301));
                    }
                }));
            }
        });
        List<Object[]> inputs = new ArrayList<>() {
            {
                //invalid title
                add(new Object[]{tvTitle1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvTitle2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvTitle3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvTitle4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvTitle5, getValidCoverImage(validImageContent)});
                //invalid description
                add(new Object[]{tvDescription1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDescription2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDescription3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDescription4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDescription5, getValidCoverImage(validImageContent)});
                //invalid release date
                add(new Object[]{tvReleaseDate1, getValidCoverImage(validImageContent)});
                //invalid audience rating
                add(new Object[]{tvAudienceRating1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating5, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating6, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating7, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating8, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating9, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating10, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating11, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating12, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAudienceRating13, getValidCoverImage(validImageContent)});
                //invalid length
                add(new Object[]{tvSeasons1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvSeasons2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvSeasons3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvSeasons4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvSeasons5, getValidCoverImage(validImageContent)});
                add(new Object[]{tvSeasons6, getValidCoverImage(validImageContent)});
                add(new Object[]{tvSeasons7, getValidCoverImage(validImageContent)});
                //invalid genres
                add(new Object[]{tvGenres1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres5, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres6, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres7, getValidCoverImage(validImageContent)});
                //invalid directors
                add(new Object[]{tvDir1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir5, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir6, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir7, getValidCoverImage(validImageContent)});
                //invalid writers
                add(new Object[]{tvWri1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri5, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri6, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri7, getValidCoverImage(validImageContent)});
                //invalid actors
                add(new Object[]{tvAct1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct3, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct4, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct5, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct6, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct7, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct8, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct9, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct10, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct11, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct12, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct13, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct14, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct15, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct16, getValidCoverImage(validImageContent)});
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
                inputs.add(new Object[]{getValidTVShow(validTVShowId), invalidCoverImage});
            } catch (IOException ex) {
                fail("getBytes() should not have failed!");
            }
        }
        //invalid file size
        MockMultipartFile pom = getValidCoverImage(validImageContent);
        MockMultipartFile invalidCoverImage = new MockMultipartFile(pom.getName(), pom.getOriginalFilename(), pom.getContentType(), new byte[8388998]);
        inputs.add(new Object[]{getValidTVShow(validTVShowId), invalidCoverImage});

        invalidCoverImage = new MockMultipartFile(pom.getName(), pom.getOriginalFilename(), pom.getContentType(), new byte[0]);
        inputs.add(new Object[]{getValidTVShow(validTVShowId), invalidCoverImage});

        return inputs;
    }

    private List<Object[]> getNonExistentDependencyTVShowsAndImages(Long validTVShowId, String validImageContent) {
        TVShowRequestDTO tvGenres1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvGenres3 = getValidTVShow(validTVShowId);
        tvGenres1.setGenres(new ArrayList<>() {
            {
                add(1l);
                add(2l);
                add(30l);
            }
        });
        tvGenres2.setGenres(new ArrayList<>() {
            {
                add(59l);
            }
        });
        tvGenres3.setGenres(new ArrayList<>() {
            {
                add(50l);
                add(1l);
            }
        });

        TVShowRequestDTO tvDir1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvDir3 = getValidTVShow(validTVShowId);
        tvDir1.setDirectors(new ArrayList<>() {
            {
                add(1l);
                add(14l);
                add(51l);
            }
        });
        tvDir2.setDirectors(new ArrayList<>() {
            {
                add(1l);
                add(2l);
            }
        });
        tvDir3.setDirectors(new ArrayList<>() {
            {
                add(140l);
            }
        });

        TVShowRequestDTO tvWri1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvWri3 = getValidTVShow(validTVShowId);
        tvWri1.setWriters(new ArrayList<>() {
            {
                add(1l);
                add(16l);
                add(51l);
            }
        });
        tvWri2.setWriters(new ArrayList<>() {
            {
                add(1l);
                add(2l);
            }
        });
        tvWri3.setWriters(new ArrayList<>() {
            {
                add(140l);
            }
        });

        TVShowRequestDTO tvAct1 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct2 = getValidTVShow(validTVShowId);
        TVShowRequestDTO tvAct3 = getValidTVShow(validTVShowId);
        tvAct1.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(1l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct2.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(15l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });
        tvAct3.setActors(new ArrayList<>() {
            {
                add(new TVShowRequestDTO.Actor(2l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(3l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
                add(new TVShowRequestDTO.Actor(89l, true, new ArrayList<>() {
                    {
                        add("Dummy value");
                    }
                }));
            }
        });

        return new ArrayList<>() {
            {
                //non-existent genres
                add(new Object[]{tvGenres1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvGenres3, getValidCoverImage(validImageContent)});
                //non-existent directors
                add(new Object[]{tvDir1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvDir3, getValidCoverImage(validImageContent)});
                //non-existent writers
                add(new Object[]{tvWri1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvWri3, getValidCoverImage(validImageContent)});
                //non-existent actors
                add(new Object[]{tvAct1, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct2, getValidCoverImage(validImageContent)});
                add(new Object[]{tvAct3, getValidCoverImage(validImageContent)});
            }
        };
    }

    private HttpEntity<MultiValueMap<String, Object>> constructRequest(TVShowRequestDTO tvShow, MockMultipartFile coverImage, String cookie) throws AssertionError {
        HttpHeaders requestHeader = new HttpHeaders();
        HttpHeaders tvShowHeaders = new HttpHeaders();
        HttpHeaders imageHeader = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpEntity<TVShowRequestDTO> tvShowBody;
        HttpEntity<Resource> imageBody;

        if (cookie != null) {
            requestHeader.set(HttpHeaders.COOKIE, cookie);
        }
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        tvShowHeaders.setContentType(MediaType.APPLICATION_JSON);
        tvShowBody = new HttpEntity<>(tvShow, tvShowHeaders);
        body.add("tv_show", tvShowBody);
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
    private void assertTVShowsEqual(List<TVShowJDBC> actual, List<TVShowJDBC> expected) throws AssertionError {
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
            assertThat(actual.get(i).getNumberOfSeasons()).isEqualTo(expected.get(i).getNumberOfSeasons());
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
                assertThat(actual.get(i).getDirectors().get(j).getId()).isEqualTo(expected.get(i).getDirectors().get(j).getId());
                assertThat(actual.get(i).getDirectors().get(j).getFirstName()).isEqualTo(expected.get(i).getDirectors().get(j).getFirstName());
                assertThat(actual.get(i).getDirectors().get(j).getLastName()).isEqualTo(expected.get(i).getDirectors().get(j).getLastName());
                assertThat(actual.get(i).getDirectors().get(j).getGender()).isEqualTo(expected.get(i).getDirectors().get(j).getGender());
                assertThat(actual.get(i).getDirectors().get(j).getProfilePhoto()).isEqualTo(expected.get(i).getDirectors().get(j).getProfilePhoto());
            }

            assertThat(actual.get(i).getWriters()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getWriters().size() == expected.get(i).getWriters().size()).isTrue();
            for (int j = 0; j < actual.get(i).getWriters().size(); j++) {
                assertThat(actual.get(i).getWriters().get(j)).isNotNull();
                assertThat(actual.get(i).getWriters().get(j).getId()).isEqualTo(expected.get(i).getWriters().get(j).getId());
                assertThat(actual.get(i).getWriters().get(j).getFirstName()).isEqualTo(expected.get(i).getWriters().get(j).getFirstName());
                assertThat(actual.get(i).getWriters().get(j).getLastName()).isEqualTo(expected.get(i).getWriters().get(j).getLastName());
                assertThat(actual.get(i).getWriters().get(j).getGender()).isEqualTo(expected.get(i).getWriters().get(j).getGender());
                assertThat(actual.get(i).getWriters().get(j).getProfilePhoto()).isEqualTo(expected.get(i).getWriters().get(j).getProfilePhoto());
            }

            assertThat(actual.get(i).getActings()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getActings().size() == expected.get(i).getActings().size()).isTrue();
            for (int j = 0; j < actual.get(i).getActings().size(); j++) {
                assertThat(actual.get(i).getActings().get(j)).isNotNull();
                assertThat(actual.get(i).getActings().get(j).isStarring()).isEqualTo(expected.get(i).getActings().get(j).isStarring());

                assertThat(actual.get(i).getActings().get(j).getActor()).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getActor().getId()).isEqualTo(expected.get(i).getActings().get(j).getActor().getId());
                assertThat(actual.get(i).getActings().get(j).getActor().getFirstName()).isEqualTo(expected.get(i).getActings().get(j).getActor().getFirstName());
                assertThat(actual.get(i).getActings().get(j).getActor().getLastName()).isEqualTo(expected.get(i).getActings().get(j).getActor().getLastName());
                assertThat(actual.get(i).getActings().get(j).getActor().getGender()).isEqualTo(expected.get(i).getActings().get(j).getActor().getGender());
                assertThat(actual.get(i).getActings().get(j).getActor().getProfilePhoto()).isEqualTo(expected.get(i).getActings().get(j).getActor().getProfilePhoto());
                assertThat(actual.get(i).getActings().get(j).getActor().isStar()).isEqualTo(expected.get(i).getActings().get(j).getActor().isStar());

                assertThat(actual.get(i).getActings().get(j).getMedia()).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getMedia() == actual.get(i)).isTrue();

                assertThat(actual.get(i).getActings().get(j).getRoles()).isNotNull().isNotEmpty();
                assertThat(actual.get(i).getActings().get(j).getRoles().size() == expected.get(i).getActings().get(j).getRoles().size()).isTrue();
                for (int k = 0; k < actual.get(i).getActings().get(j).getRoles().size(); k++) {
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k)).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getActing()).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getActing() == actual.get(i).getActings().get(j)).isTrue();

                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getId()).isEqualTo(expected.get(i).getActings().get(j).getRoles().get(k).getId());
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getName()).isEqualTo(actual.get(i).getActings().get(j).getRoles().get(k).getName());
                }
            }

            assertThat(actual.get(i).getCritiques()).isNotNull();
            assertThat(actual.get(i).getCritiques().size() == expected.get(i).getCritiques().size()).isTrue();
            for (int j = 0; j < actual.get(i).getCritiques().size(); j++) {
                assertThat(actual.get(i).getCritiques().get(j)).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic()).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getProfileName()).isNotEmpty().isEqualTo(expected.get(i).getCritiques().get(j).getCritic().getProfileName());
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getProfileImage()).isEqualTo(expected.get(i).getCritiques().get(j).getCritic().getProfileImage());
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getRole()).isEqualTo(UserRole.CRITIC);
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getFirstName()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getLastName()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getGender()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getCountry()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getUsername()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getPassword()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getEmail()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getCreatedAt()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getUpdatedAt()).isNull();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getMedias()).isNotNull().isEmpty();
                assertThat(actual.get(i).getCritiques().get(j).getCritic().getCritiques()).isNotNull().isEmpty();

                assertThat(actual.get(i).getCritiques().get(j).getMedia()).isNotNull();
                assertThat(actual.get(i).getCritiques().get(j).getMedia() == actual.get(i)).isTrue();

                assertThat(actual.get(i).getCritiques().get(j).getDescription()).isNotEmpty().isEqualTo(expected.get(i).getCritiques().get(j).getDescription());
                assertThat(actual.get(i).getCritiques().get(j).getRating()).isNotNull().isEqualTo(expected.get(i).getCritiques().get(j).getRating());
            }

        }
    }

    private void assertTVShowsEqual(TVShowResponseDTO actual, TVShowRequestDTO expected, String expectedCoverImageUrl) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getCoverImageUrl()).isEqualTo(expectedCoverImageUrl);
        assertThat(actual.getReleaseDate()).isEqualTo(expected.getReleaseDate());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getAudienceRating()).isEqualTo(expected.getAudienceRating());
        assertThat(actual.getNumberOfSeasons()).isEqualTo(expected.getNumberOfSeasons());
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

    private void assertTVShowsEqual(TVShowJDBC actual, TVShowResponseDTO expected) throws AssertionError {
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
        assertThat(actual.getNumberOfSeasons()).isEqualTo(expected.getNumberOfSeasons());
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
            assertThat(actual.getDirectors().get(i).getId()).isNotNull().isEqualTo(expected.getDirectors().get(i).getId());
            assertThat(actual.getDirectors().get(i).getFirstName()).isEqualTo(expected.getDirectors().get(i).getFirstName());
            assertThat(actual.getDirectors().get(i).getLastName()).isEqualTo(expected.getDirectors().get(i).getLastName());
            assertThat(actual.getDirectors().get(i).getGender()).isEqualTo(expected.getDirectors().get(i).getGender());
            if (actual.getDirectors().get(i).getProfilePhoto() == null) {
                assertThat(actual.getDirectors().get(i).getProfilePhoto()).isEqualTo(expected.getDirectors().get(i).getProfilePhotoUrl());
            } else {
                assertThat(config.getPersonImagesBaseUrl() + actual.getDirectors().get(i).getProfilePhoto()).isEqualTo(expected.getDirectors().get(i).getProfilePhotoUrl());
            }
        }

        assertThat(actual.getWriters()).isNotNull().isNotEmpty();
        assertThat(actual.getWriters().size()).isEqualTo(expected.getWriters().size());
        for (int i = 0; i < actual.getWriters().size(); i++) {
            assertThat(actual.getWriters().get(i)).isNotNull();
            assertThat(actual.getWriters().get(i).getId()).isNotNull().isEqualTo(expected.getWriters().get(i).getId());
            assertThat(actual.getWriters().get(i).getFirstName()).isEqualTo(expected.getWriters().get(i).getFirstName());
            assertThat(actual.getWriters().get(i).getLastName()).isEqualTo(expected.getWriters().get(i).getLastName());
            assertThat(actual.getWriters().get(i).getGender()).isEqualTo(expected.getWriters().get(i).getGender());
            if (actual.getWriters().get(i).getProfilePhoto() == null) {
                assertThat(actual.getWriters().get(i).getProfilePhoto()).isEqualTo(expected.getWriters().get(i).getProfilePhotoUrl());
            } else {
                assertThat(config.getPersonImagesBaseUrl() + actual.getWriters().get(i).getProfilePhoto()).isEqualTo(expected.getWriters().get(i).getProfilePhotoUrl());
            }

        }

        assertThat(actual.getActings()).isNotNull().isNotEmpty();
        assertThat(actual.getActings().size()).isEqualTo(expected.getActors().size());
        for (int i = 0; i < actual.getActings().size(); i++) {
            assertThat(actual.getActings().get(i)).isNotNull();
            assertThat(actual.getActings().get(i).getActor()).isNotNull();
            assertThat(actual.getActings().get(i).getActor().getId()).isNotNull().isEqualTo(expected.getActors().get(i).getId());
            assertThat(actual.getActings().get(i).getActor().getFirstName()).isEqualTo(expected.getActors().get(i).getFirstName());
            assertThat(actual.getActings().get(i).getActor().getLastName()).isEqualTo(expected.getActors().get(i).getLastName());
            assertThat(actual.getActings().get(i).getActor().getGender()).isEqualTo(expected.getActors().get(i).getGender());
            if (actual.getActings().get(i).getActor().getProfilePhoto() == null) {
                assertThat(actual.getActings().get(i).getActor().getProfilePhoto()).isEqualTo(expected.getActors().get(i).getProfilePhotoUrl());
            } else {
                assertThat(config.getPersonImagesBaseUrl() + actual.getActings().get(i).getActor().getProfilePhoto()).isEqualTo(expected.getActors().get(i).getProfilePhotoUrl());
            }

            assertThat(actual.getActings().get(i).isStarring()).isNotNull().isEqualTo(expected.getActors().get(i).getStarring());
            assertThat(actual.getActings().get(i).getRoles()).isNotNull().isNotEmpty();
            assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActors().get(i).getRoles().size());
            for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isNotNull().isEqualTo(expected.getActors().get(i).getRoles().get(j).getId());
                assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isNotEmpty().isEqualTo(expected.getActors().get(i).getRoles().get(j).getName());
            }
        }
        assertThat(actual.getCritiques()).isNotNull().isEmpty();

    }

    private void assertTVShowsEqual(TVShowJDBC actual, TVShowRequestDTO expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isNotBlank().isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isNotBlank().isEqualTo(expected.getDescription());
        assertThat(actual.getReleaseDate()).isNotNull().isEqualTo(expected.getReleaseDate());
        assertThat(actual.getAudienceRating()).isNotNull().isEqualTo(expected.getAudienceRating());
        assertThat(actual.getNumberOfSeasons()).isNotNull().isEqualTo(expected.getNumberOfSeasons());

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

        assertThat(actual.getActings()).isNotNull().isNotEmpty();
        assertThat(actual.getActings().size()).isEqualTo(expected.getActors().size());
        for (int i = 0; i < actual.getActings().size(); i++) {
            assertThat(actual.getActings().get(i)).isNotNull();
            assertThat(actual.getActings().get(i).getActor()).isNotNull();
            assertThat(actual.getActings().get(i).getActor().getId()).isNotNull().isEqualTo(expected.getActors().get(i).getId());

            assertThat(actual.getActings().get(i).isStarring()).isNotNull().isEqualTo(expected.getActors().get(i).getStarring());
            assertThat(actual.getActings().get(i).getRoles()).isNotNull().isNotEmpty();
            assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActors().get(i).getRoles().size());
            for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isNotNull().isEqualTo(j + 1);
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

    private boolean areTVShowsEqual(TVShowJDBC actual, TVShowRequestDTO expected) {
        try {
            assertTVShowsEqual(actual, expected);
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
        try {
            String folderPath = config.getMediaImagesFolderPath();
            Path path = Path.of(folderPath);
            try (Stream<Path> paths = Files.walk(path)) {
                return paths.filter(Files::isRegularFile)
                        .map(p -> {
                            try {
                                return new UrlResource(p.toUri());
                            } catch (MalformedURLException e) {
                                throw new RuntimeException("Issue in reading the file", e);
                            }
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue in reading files from the directory", e);
        }
    }

    private List<TVShowJDBC> getAllTVShows() {
        return tvShowRepo.findAllWithRelations();
    }

    private void changeAttributes(TVShowRequestDTO tvShow) {
        tvShow.setTitle("Dummy PUT title");
        tvShow.setDescription("Dummy PUT description");
        tvShow.setReleaseDate(LocalDate.now());
        tvShow.setNumberOfSeasons(3);
        tvShow.setAudienceRating(59);
        tvShow.setGenres(new ArrayList<>() {
            {
                add(1l);
                add(6l);
            }
        });
        tvShow.setDirectors(new ArrayList<>() {
            {
                add(32l);
                add(33l);
            }
        });
        tvShow.setWriters(new ArrayList<>() {
            {
                add(26l);
                add(27l);
            }
        });
        TVShowRequestDTO.Actor a1 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a2 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a3 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a4 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a5 = new TVShowRequestDTO.Actor();
        TVShowRequestDTO.Actor a6 = new TVShowRequestDTO.Actor();
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
        tvShow.setActors(new ArrayList<>() {
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
