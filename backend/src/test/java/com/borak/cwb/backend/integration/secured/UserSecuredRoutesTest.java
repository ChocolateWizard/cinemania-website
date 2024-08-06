/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.secured;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.helpers.Pair;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.helpers.TestUtil;
import com.borak.cwb.backend.helpers.repositories.UserTestRepository;
import com.borak.cwb.backend.logic.security.JwtUtils;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(8)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSecuredRoutesTest {

    private final TestRestTemplate restTemplate;
    private final UserTestRepository userRepo;
    private final ConfigProperties config;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserSecuredRoutesTest(TestRestTemplate restTemplate, UserTestRepository userRepo, ConfigProperties config, JwtUtils jwtUtils) {
        this.restTemplate = restTemplate;
        this.userRepo = userRepo;
        this.config = config;
        this.jwtUtils = jwtUtils;
    }

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/users/library";

    static {
        TESTS_PASSED.put("postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401", false);
        TESTS_PASSED.put("postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400", false);
        TESTS_PASSED.put("postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404", false);
        TESTS_PASSED.put("postMediaToLibrary_DuplicateMediaData_DoesNotCreateMediaInLibraryReturns409", false);
        TESTS_PASSED.put("postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns204", false);

        TESTS_PASSED.put("deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401", false);
        TESTS_PASSED.put("deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400", false);
        TESTS_PASSED.put("deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404", false);
        TESTS_PASSED.put("deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns204", false);
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
        Assumptions.assumeTrue(TestResultsHelper.didAuthRoutesTestPass());
    }

    //=========================================================================================================
    //POST
    @Test
    @Order(1)
    @DisplayName("Tests whether POST request to /api/users/library with unauthenticated user did not create new media in library and it returned 401")
    void postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;
        try {
            for (Pair<Long, String> input : getExistingMediaIdNonExistentUsername()) {
                // no cookie
                request = constructRequest(null);
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);

                //random string as cookie
                request = constructRequest(TestUtil.getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);

                //jwt of non-existent user in cookies
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/users/library with invalid input data did not create new media in library and it returned 400")
    void postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;
        try {
            for (Pair<Long, String> input : getInvalidMediaIdExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/users/library with non-existent dependency objects did not create new media in library and it returned 404")
    void postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;
        try {
            for (Pair<Long, String> input : getNonExistentMediaIdExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/users/library with duplicate media data did not create new media in library and it returned 409")
    void postMediaToLibrary_DuplicateMediaData_DoesNotCreateMediaInLibraryReturns409() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;

        try {
            for (Pair<Long, String> input : getDuplicateMediaIdExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("postMediaToLibrary_DuplicateMediaData_DoesNotCreateMediaInLibraryReturns409", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to /api/users/library with valid input data did create new media in library and it returned 204")
    void postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns204() {
        HttpEntity request;
        ResponseEntity<String> response;
        int i = 0;
        try {
            for (Pair<Long, String> input : getValidExistingMediaIdValidExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                boolean isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getL());
                assertThat(isInLibrary).isFalse();

                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getL());
                assertThat(isInLibrary).isTrue();
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns204", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(6)
    @DisplayName("Tests whether DELETE request to /api/users/library with unauthenticated user did not delete media from library and it returned 401")
    void deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;

        try {
            for (Pair<Long, String> input : getExistingMediaIdNonExistentUsername()) {
                // no cookie
                request = constructRequest(null);
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);

                //random string as cookie
                request = constructRequest(TestUtil.getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);

                //jwt of non-existent user in cookies
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests whether DELETE request to /api/users/library with invalid input data did not delete media from library and it returned 400")
    void deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;
        try {
            for (Pair<Long, String> input : getInvalidMediaIdExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether DELETE request to /api/users/library with non-existent dependency objects did not delete media from library and it returned 404")
    void deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore = getAllUsersWithLibrary();
        List<UserJPA> usersAfter;
        int i = 0;
        try {
            for (Pair<Long, String> input : getNonExistentMediaIdInLibraryExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                usersAfter = getAllUsersWithLibrary();
                checkLibraryEqual(usersAfter, usersBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether DELETE request to /api/users/library with valid input data did delete media from library and it returned 204")
    void deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns204() {
        HttpEntity request;
        ResponseEntity<String> response;
        int i = 0;

        try {
            for (Pair<Long, String> input : getValidExistingMediaIdValidExistingUsername()) {
                Optional<UserJPA> user = userRepo.findByUsername(input.getR());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                boolean isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getL());
                assertThat(isInLibrary).isTrue();

                request = constructRequest(jwtUtils.generateTokenFromUsername(input.getR()));
                response = restTemplate.exchange(ROUTE + "/" + input.getL(), HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getL());
                assertThat(isInLibrary).isFalse();
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        TESTS_PASSED.put("deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns204", true);
    }
//=================================================================================================================================
//PRIVATE METHODS

    private HttpEntity constructRequest(String jwt) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (jwt != null) {
            HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
            cookie.setPath("/api");
            cookie.setHttpOnly(true);
            requestHeaders.set(HttpHeaders.COOKIE, cookie.toString());
        }
        return new HttpEntity<>(requestHeaders);
    }

    private List<UserJPA> getAllUsersWithLibrary() {
        return userRepo.findAll();
    }

    private void checkLibraryEqual(List<UserJPA> actual, List<UserJPA> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i).getId()).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getMedias().size()).isEqualTo(expected.get(i).getMedias().size());
            for (int j = 0; j < actual.get(i).getMedias().size(); j++) {
                assertThat(actual.get(i).getMedias().get(j).getId()).isEqualTo(expected.get(i).getMedias().get(j).getId());
            }
        }
    }

//=================================================================================================================================
    private Pair<Long, String>[] getExistingMediaIdNonExistentUsername() {
        return new Pair[]{
            new Pair(1l, "dummy user 1"),
            new Pair(2l, "dummy user 2"),
            new Pair(4l, "dummy user 4"),
            new Pair(3l, "admina"),
            new Pair(5l, "critic1"),
            new Pair(6l, "dummy user 6")
        };
    }

    private Pair<Long, String>[] getInvalidMediaIdExistingUsername() {
        return new Pair[]{
            new Pair(0l, "admin"),
            new Pair(-1l, "admin"),
            new Pair(-2l, "regular"),
            new Pair(-3l, "critic"),
            new Pair(-20l, "critic"),
            new Pair(Long.MIN_VALUE, "regular")
        };
    }

    private Pair<Long, String>[] getNonExistentMediaIdExistingUsername() {
        return new Pair[]{
            new Pair(7l, "admin"),
            new Pair(8l, "admin"),
            new Pair(10l, "regular"),
            new Pair(11l, "critic"),
            new Pair(25l, "admin"),
            new Pair(16l, "critic"),
            new Pair(Long.MAX_VALUE, "regular")
        };

    }
    
    private Pair<Long, String>[] getNonExistentMediaIdInLibraryExistingUsername() {
        return new Pair[]{
            new Pair(7l, "admin"),
            new Pair(8l, "admin"),
            new Pair(10l, "regular"),
            new Pair(11l, "critic"),
            new Pair(25l, "admin"),
            new Pair(16l, "critic"),
            new Pair(Long.MAX_VALUE, "regular"),
            new Pair(5l, "admin"),
            new Pair(4l, "regular"),
            new Pair(3l, "critic"),
            new Pair(5l, "critic"),
            new Pair(1l, "critic")
        };

    }

    private Pair<Long, String>[] getDuplicateMediaIdExistingUsername() {
        return new Pair[]{
            new Pair(1l, "admin"),
            new Pair(3l, "admin"),
            new Pair(4l, "admin"),
            new Pair(1l, "regular"),
            new Pair(2l, "regular"),
            new Pair(5l, "regular")
        };
    }

    private Pair<Long, String>[] getValidExistingMediaIdValidExistingUsername() {
        return new Pair[]{
            new Pair(2l, "admin"),
            new Pair(6l, "admin"),
            new Pair(6l, "regular"),
            new Pair(3l, "regular")
        };
    }

}
