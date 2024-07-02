/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.secured;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.logic.security.JwtUtils;
import com.borak.cwb.backend.repository.jdbc.UserRepositoryJDBC;
import java.net.HttpCookie;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepositoryJDBC userRepo;
    @Autowired
    private ConfigProperties config;
    @Autowired
    private JwtUtils jwtUtils;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/users/library";

    static {
        testsPassed.put("postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401", false);
        testsPassed.put("postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400", false);
        testsPassed.put("postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404", false);
        testsPassed.put("postMediaToLibrary_DuplicateMediaData_DoesNotCreateMediaInLibraryReturns409", false);
        testsPassed.put("postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns204", false);

        testsPassed.put("deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401", false);
        testsPassed.put("deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400", false);
        testsPassed.put("deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404", false);
        testsPassed.put("deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns204", false);
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
        Assumptions.assumeTrue(TestResultsHelper.didAuthRoutesTestPass());
    }

    //=========================================================================================================
    //POST
    @Test
    @Order(1)
    @DisplayName("Tests whether POST request to /api/users/library with unauthenticated user did not create new media in library and it returned 401")
    void postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(1l, "dummy user 1"));
                add(new SimpleEntry<>(2l, "dummy user 2"));
                add(new SimpleEntry<>(4l, "dummy user 4"));
                add(new SimpleEntry<>(3l, "admina"));
                add(new SimpleEntry<>(5l, "critic1"));
                add(new SimpleEntry<>(6l, "dummy user 6"));
            }
        };
        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                // no cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);

                //random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(getRandomString(50, random)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);

                //jwt of non-existent user in cookies
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/users/library with invalid input data did not create new media in library and it returned 400")
    void postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(0l, "admin"));
                add(new SimpleEntry<>(-1l, "admin"));
                add(new SimpleEntry<>(-2l, "regular"));
                add(new SimpleEntry<>(Long.MIN_VALUE, "regular"));
            }
        };
        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/users/library with non-existent dependency objects did not create new media in library and it returned 404")
    void postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(7l, "admin"));
                add(new SimpleEntry<>(8l, "admin"));
                add(new SimpleEntry<>(10l, "regular"));
                add(new SimpleEntry<>(25l, "admin"));
                add(new SimpleEntry<>(Long.MAX_VALUE, "regular"));
            }
        };
        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/users/library with duplicate media data did not create new media in library and it returned 409")
    void postMediaToLibrary_DuplicateMediaData_DoesNotCreateMediaInLibraryReturns409() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(1l, "admin"));
                add(new SimpleEntry<>(3l, "admin"));
                add(new SimpleEntry<>(4l, "admin"));
                add(new SimpleEntry<>(1l, "regular"));
                add(new SimpleEntry<>(2l, "regular"));
                add(new SimpleEntry<>(5l, "regular"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("postMediaToLibrary_DuplicateMediaData_DoesNotCreateMediaInLibraryReturns409", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to /api/users/library with valid input data did create new media in library and it returned 204")
    void postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns204() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(2l, "admin"));
                add(new SimpleEntry<>(6l, "admin"));
                add(new SimpleEntry<>(6l, "regular"));
                add(new SimpleEntry<>(3l, "regular"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                boolean isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getKey());
                assertThat(isInLibrary).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.POST, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getKey());
                assertThat(isInLibrary).isTrue();
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns204", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(6)
    @DisplayName("Tests whether DELETE request to /api/users/library with unauthenticated user did not delete media from library and it returned 401")
    void deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(1l, "dummy user 1"));
                add(new SimpleEntry<>(2l, "dummy user 2"));
                add(new SimpleEntry<>(4l, "dummy user 4"));
                add(new SimpleEntry<>(3l, "admina"));
                add(new SimpleEntry<>(5l, "critic1"));
                add(new SimpleEntry<>(6l, "dummy user 6"));
            }
        };
        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                // no cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);

                //random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(getRandomString(50, random)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);

                //jwt of non-existent user in cookies
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests whether DELETE request to /api/users/library with invalid input data did not delete media from library and it returned 400")
    void deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(0l, "admin"));
                add(new SimpleEntry<>(-1l, "admin"));
                add(new SimpleEntry<>(-2l, "regular"));
                add(new SimpleEntry<>(Long.MIN_VALUE, "regular"));
            }
        };
        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether DELETE request to /api/users/library with non-existent dependency objects did not delete media from library and it returned 404")
    void deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(7l, "admin"));
                add(new SimpleEntry<>(8l, "admin"));
                add(new SimpleEntry<>(10l, "regular"));
                add(new SimpleEntry<>(25l, "admin"));
                add(new SimpleEntry<>(Long.MAX_VALUE, "regular"));
                add(new SimpleEntry<>(5l, "admin"));
                add(new SimpleEntry<>(4l, "regular"));
                add(new SimpleEntry<>(3l, "critic"));
                add(new SimpleEntry<>(5l, "critic"));
                add(new SimpleEntry<>(1l, "critic"));
            }
        };
        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<UserJDBC> usersBefore = getAllUsersWithLibrary();
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                List<UserJDBC> usersAfter = getAllUsersWithLibrary();
                checkUsersMediaId(usersBefore, usersAfter);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether DELETE request to /api/users/library with valid input data did delete media from library and it returned 204")
    void deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns204() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(2l, "admin"));
                add(new SimpleEntry<>(6l, "admin"));
                add(new SimpleEntry<>(6l, "regular"));
                add(new SimpleEntry<>(3l, "regular"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                boolean isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getKey());
                assertThat(isInLibrary).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                isInLibrary = userRepo.existsMediaInLibrary(user.get().getId(), input.getKey());
                assertThat(isInLibrary).isFalse();
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns204", true);
    }
    //=========================================================================================================
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

    private List<UserJDBC> getAllUsersWithLibrary() {
        List<UserJDBC> users = userRepo.findAll();
        for (UserJDBC user : users) {
            List<MediaJDBC> medias = userRepo.findAllLibraryMediaByUserId(user.getId());
            user.setMedias(medias);
        }
        return users;
    }

    //checks if all user ids and media ids are the same in value and number
    private void checkUsersMediaId(List<UserJDBC> beforeUsers, List<UserJDBC> afterUsers) {
        assertThat(afterUsers.size()).isEqualTo(beforeUsers.size());
        for (int i = 0; i < afterUsers.size(); i++) {
            assertThat(afterUsers.get(i).getId()).isEqualTo(beforeUsers.get(i).getId());
            assertThat(afterUsers.get(i).getMedias().size()).isEqualTo(beforeUsers.get(i).getMedias().size());
            for (int j = 0; j < afterUsers.get(i).getMedias().size(); j++) {
                assertThat(afterUsers.get(i).getMedias().get(j).getId()).isEqualTo(beforeUsers.get(i).getMedias().get(j).getId());
            }
        }
    }

    private String getRandomString(int length, Random random) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
