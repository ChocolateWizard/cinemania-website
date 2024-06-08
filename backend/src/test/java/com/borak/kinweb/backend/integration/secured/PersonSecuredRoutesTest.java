/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.dto.person.PersonRequestDTO;
import com.borak.kinweb.backend.domain.dto.person.PersonResponseDTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonWrapperJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.logic.security.JwtUtils;
import com.borak.kinweb.backend.repository.jdbc.PersonWrapperRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.UserRepositoryJDBC;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@Order(11)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersonWrapperRepositoryJDBC personRepo;
    @Autowired
    private UserRepositoryJDBC userRepo;
    @Autowired
    private FileRepository fileRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ConfigProperties config;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/persons";

    static {
        testsPassed.put("postPerson_UnauthenticatedUser_DoesNotCreatePersonReturns401", false);
        testsPassed.put("postPerson_UnauthorizedUser_DoesNotCreatePersonReturns403", false);
        testsPassed.put("postPerson_InvalidInputData_DoesNotCreatePersonReturns400", false);
        testsPassed.put("postPerson_NonexistentDependencyData_DoesNotCreatePersonReturns404", false);
        testsPassed.put("postPerson_ValidInput_CreatesPersonReturns200", false);

        testsPassed.put("putPerson_UnauthenticatedUser_DoesNotUpdatePersonReturns401", false);
        testsPassed.put("putPerson_UnauthorizedUser_DoesNotUpdatePersonReturns403", false);
        testsPassed.put("putPerson_InvalidInputData_DoesNotUpdatePersonReturns400", false);
        testsPassed.put("putPerson_NonexistentDependencyData_DoesNotUpdatePersonReturns404", false);
        testsPassed.put("putPerson_ValidInput_UpdatesPersonReturns200", false);

        testsPassed.put("deletePerson_UnauthenticatedUser_DoesNotDeletePersonReturns401", false);
        testsPassed.put("deletePerson_UnauthorizedUser_DoesNotDeletePersonReturns403", false);
        testsPassed.put("deletePerson_InvalidInputData_DoesNotDeletePersonReturns400", false);
        testsPassed.put("deletePerson_NonexistentDependencyData_DoesNotDeletePersonReturns404", false);
        testsPassed.put("deletePerson_ValidInput_DeletesPersonReturns200", false);
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
    @DisplayName("Tests whether POST request to /api/persons with unauthenticated user did not create new person and it returned 401")
    void postPerson_UnauthenticatedUser_DoesNotCreatePersonReturns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        PersonRequestDTO personValid = getValidPerson(51l);
        MockMultipartFile imageValid = getValidProfilePhoto(getRandomString(20));
        int i = 0;
        try {
            for (String username : getNonExistentUsernames()) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();

                //no cookie
                request = constructRequest(personValid, imageValid, null);
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(personValid, imageValid, getRandomString(50));
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(personValid, imageValid, jwt);
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(personValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postPerson_UnauthenticatedUser_DoesNotCreatePersonReturns401", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/persons with authenticated but unauthorized user did not create new person and it returned 403")
    void postPerson_UnauthorizedUser_DoesNotCreatePersonReturns403() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        PersonRequestDTO personValid = getValidPerson(51l);
        MockMultipartFile imageValid = getValidProfilePhoto(getRandomString(20));
        int i = 0;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(personValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postPerson_UnauthorizedUser_DoesNotCreatePersonReturns403", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/persons with invalid input data did not create new person and it returned 400")
    void postPerson_InvalidInputData_DoesNotCreatePersonReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
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
            for (Object[] input : getBadRequestPersonsAndImages(51l, getRandomString(20))) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest((PersonRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postPerson_InvalidInputData_DoesNotCreatePersonReturns400", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/persons with non-existent dependency objects did not create new person and it returned 404")
    void postPerson_NonexistentDependencyData_DoesNotCreatePersonReturns404() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
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
            for (Object[] input : getNonExistentDependencyPersonsAndImages(51l, getRandomString(20))) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest((PersonRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postPerson_NonexistentDependencyData_DoesNotCreatePersonReturns404", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to /api/persons with valid input data did create new person and it returned 200")
    void postPerson_ValidInput_CreatesPersonReturns200() {
        HttpEntity request;
        ResponseEntity<PersonResponseDTO> response;

        PersonRequestDTO personValid1 = getValidPerson(51l);
        PersonRequestDTO personValid2 = getValidPerson(null);

        MockMultipartFile imageValid1 = getValidProfilePhoto(getRandomString(20));
        MockMultipartFile imageValid2 = getValidProfilePhoto(getRandomString(20));

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where person id is set
        assertThat(personRepo.findById(personValid1.getId()).isPresent()).isFalse();
        assertThat(existsProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isFalse();

        request = constructRequest(personValid1, imageValid1, cookie.toString());
        response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(personValid1.getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();

        Optional<PersonWrapperJDBC> actualDBPerson = personRepo.findByIdWithRelations(personValid1.getId());
        Resource actualDBImage = fileRepo.getPersonProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(actualDBPerson).isNotNull();
        assertThat(actualDBPerson.isPresent()).isTrue();
        String profilePhotoUrl = config.getPersonImagesBaseUrl() + personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename());
        assertPersonsEqual(response.getBody(), personValid1, profilePhotoUrl);
        assertPersonsEqual(actualDBPerson.get(), response.getBody());
        assertImagesEqual(actualDBImage, imageValid1);

        //----------------------------------------------------------------------------
        //second valid request where person id is null
        request = constructRequest(personValid2, imageValid2, cookie.toString());
        response = restTemplate.exchange(ROUTE, HttpMethod.POST, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(response.getBody().getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()))).isTrue();

        actualDBPerson = personRepo.findByIdWithRelations(response.getBody().getId());
        actualDBImage = fileRepo.getPersonProfilePhoto(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()));
        assertThat(actualDBPerson).isNotNull();
        assertThat(actualDBPerson.isPresent()).isTrue();
        personValid2.setId(response.getBody().getId());
        profilePhotoUrl = config.getPersonImagesBaseUrl() + personValid2.getId() + getExtensionWithDot(imageValid2.getOriginalFilename());
        assertPersonsEqual(response.getBody(), personValid2, profilePhotoUrl);
        assertPersonsEqual(actualDBPerson.get(), response.getBody());
        assertImagesEqual(actualDBImage, imageValid2);

        testsPassed.put("postPerson_ValidInput_CreatesPersonReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(6)
    @DisplayName("Tests whether PUT request to /api/persons with unauthenticated user did not update person and it returned 401")
    void putPerson_UnauthenticatedUser_DoesNotUpdatePersonReturns401() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        PersonRequestDTO personValid = getValidPerson(51l);
        MockMultipartFile imageValid = getValidProfilePhoto(getRandomString(40));
        int i = 0;
        try {
            for (String username : getNonExistentUsernames()) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();

                //no cookie
                request = constructRequest(personValid, imageValid, null);
                response = restTemplate.exchange(ROUTE + "/" + personValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(personValid, imageValid, getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + personValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(personValid, imageValid, jwt);
                response = restTemplate.exchange(ROUTE + "/" + personValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(personValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + personValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putPerson_UnauthenticatedUser_DoesNotUpdatePersonReturns401", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests whether PUT request to /api/persons with authenticated but unauthorized user did not update person and it returned 403")
    void putPerson_UnauthorizedUser_DoesNotUpdatePersonReturns403() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        PersonRequestDTO personValid = getValidPerson(51l);
        MockMultipartFile imageValid = getValidProfilePhoto(getRandomString(40));
        int i = 0;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(personValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + personValid.getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }

        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putPerson_UnauthorizedUser_DoesNotUpdatePersonReturns403", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether PUT request to /api/persons with invalid input data did not update person and it returned 400")
    void putPerson_InvalidInputData_DoesNotUpdatePersonReturns400() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
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
            for (Object[] input : getBadRequestPersonsAndImages(51l, getRandomString(40))) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();

                request = constructRequest((PersonRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + ((PersonRequestDTO) input[0]).getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            i = 0;
            PersonRequestDTO personValid = getValidPerson(51l);
            MockMultipartFile imageValid = getValidProfilePhoto(getRandomString(40));
            for (long invalidId : new long[]{0l, -1l, -2l, -5l, -10l, -23l, Long.MIN_VALUE}) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest(personValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putPerson_InvalidInputData_DoesNotUpdatePersonReturns400", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether PUT request to /api/persons with non-existent dependency objects did not update person and it returned 404")
    void putPerson_NonexistentDependencyData_DoesNotUpdatePersonReturns404() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
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
            for (Object[] input : getNonExistentDependencyPersonsAndImages(51l, getRandomString(40))) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest((PersonRequestDTO) input[0], (MockMultipartFile) input[1], cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + ((PersonRequestDTO) input[0]).getId(), HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
            i = 0;
            PersonRequestDTO personValid = getValidPerson(51l);
            MockMultipartFile imageValid = getValidProfilePhoto(getRandomString(40));
            for (long invalidId : new long[]{101l, 102l, 150l, 200l, Long.MAX_VALUE}) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest(personValid, imageValid, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.PUT, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putPerson_NonexistentDependencyData_DoesNotUpdatePersonReturns404", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests whether PUT request to /api/persons with valid input data did update person and it returned 200")
    void putPerson_ValidInput_UpdatesPersonReturns200() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<PersonResponseDTO> response;

        PersonRequestDTO personValid1 = getValidPerson(51l);
        PersonRequestDTO personValid2 = getValidPerson(null);
        PersonRequestDTO personValid3 = getValidPerson(52l);

        changeAttributes(personValid1);
        changeAttributes(personValid2);
        changeAttributes(personValid3);

        MockMultipartFile imageValid1 = getValidProfilePhoto(getRandomString(40));
        MockMultipartFile imageValid2 = getValidProfilePhoto(getRandomString(50));

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where person has different valid attributes and ID and image are set
        assertThat(personRepo.findById(personValid1.getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        Optional<PersonWrapperJDBC> actualDBPerson = personRepo.findByIdWithRelations(personValid1.getId());
        Resource actualDBImage = fileRepo.getPersonProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(arePersonsEqual(actualDBPerson.get(), personValid1)).isFalse();
        assertThat(areImagesEqual(actualDBImage, imageValid1)).isFalse();

        request = constructRequest(personValid1, imageValid1, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + personValid1.getId(), HttpMethod.PUT, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(personValid1.getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        actualDBPerson = personRepo.findByIdWithRelations(personValid1.getId());
        actualDBImage = fileRepo.getPersonProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        assertThat(arePersonsEqual(actualDBPerson.get(), personValid1)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid1)).isTrue();

        String profilePhotoUrl = config.getPersonImagesBaseUrl() + personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename());
        assertPersonsEqual(response.getBody(), personValid1, profilePhotoUrl);
        assertPersonsEqual(actualDBPerson.get(), response.getBody());

        //----------------------------------------------------------------------------
        //second valid request where person has different valid attributes and ID is null and image has more bytes than the previous updated one
        //check if personValid1 object is present in database and is the same as personValid2
        //and that imageValid2 is different than the database imageValid1
        assertThat(personRepo.findById(personValid1.getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()))).isTrue();
        actualDBPerson = personRepo.findByIdWithRelations(personValid1.getId());
        actualDBImage = fileRepo.getPersonProfilePhoto(personValid1.getId() + getExtensionWithDot(imageValid1.getOriginalFilename()));
        personValid2.setId(personValid1.getId());
        assertThat(arePersonsEqual(actualDBPerson.get(), personValid2)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid2)).isFalse();

        //make request
        personValid2.setId(null);
        request = constructRequest(personValid2, imageValid2, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + personValid1.getId(), HttpMethod.PUT, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(response.getBody().getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()))).isTrue();

        actualDBPerson = personRepo.findByIdWithRelations(response.getBody().getId());
        actualDBImage = fileRepo.getPersonProfilePhoto(response.getBody().getId() + getExtensionWithDot(imageValid2.getOriginalFilename()));
        assertThat(actualDBPerson).isNotNull();
        assertThat(actualDBPerson.isPresent()).isTrue();
        personValid2.setId(response.getBody().getId());
        assertThat(arePersonsEqual(actualDBPerson.get(), personValid2)).isTrue();
        assertThat(areImagesEqual(actualDBImage, imageValid2)).isTrue();

        profilePhotoUrl = config.getPersonImagesBaseUrl() + personValid2.getId() + getExtensionWithDot(imageValid2.getOriginalFilename());
        assertPersonsEqual(response.getBody(), personValid2, profilePhotoUrl);
        assertPersonsEqual(actualDBPerson.get(), response.getBody());
        //----------------------------------------------------------------------------
        //third valid request where person has different valid attributes, ID is 52 and image is null
        //check if image that was in database was deleted after successful PUT request
        assertThat(personRepo.findById(personValid3.getId()).isPresent()).isTrue();
        actualDBPerson = personRepo.findByIdWithRelations(personValid3.getId());
        assertThat(arePersonsEqual(actualDBPerson.get(), personValid3)).isFalse();
        assertThat(existsProfilePhoto(actualDBPerson.get().getPerson().getProfilePhoto())).isTrue();

        request = constructRequest(personValid3, null, cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + personValid3.getId(), HttpMethod.PUT, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(personValid3.getId()).isPresent()).isTrue();
        assertThat(existsProfilePhoto(actualDBPerson.get().getPerson().getProfilePhoto())).isFalse();
        actualDBPerson = personRepo.findByIdWithRelations(personValid3.getId());
        assertThat(arePersonsEqual(actualDBPerson.get(), personValid3)).isTrue();

        assertPersonsEqual(response.getBody(), personValid3, null);
        assertPersonsEqual(actualDBPerson.get(), response.getBody());

        testsPassed.put("putPerson_ValidInput_UpdatesPersonReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(11)
    @DisplayName("Tests whether DELETE request to /api/persons with unauthenticated user did not delete person and it returned 401")
    void deletePerson_UnauthenticatedUser_DoesNotDeletePersonReturns401() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putPerson_ValidInput_UpdatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        int i = 0;
        long personValidId = 51l;
        try {
            for (String username : getNonExistentUsernames()) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();

                //no cookie
                request = constructRequest(null);
                response = restTemplate.exchange(ROUTE + "/" + personValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //random string as cookie
                request = constructRequest(getRandomString(50));
                response = restTemplate.exchange(ROUTE + "/" + personValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //jwt of non-existent user as cookie
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                String jwt = jwtUtils.generateTokenFromUsername(username);
                request = constructRequest(jwt);
                response = restTemplate.exchange(ROUTE + "/" + personValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //valid cookie with jwt of non-existent user
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + personValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deletePerson_UnauthenticatedUser_DoesNotDeletePersonReturns401", true);
    }

    @Test
    @Order(12)
    @DisplayName("Tests whether DELETE request to /api/persons with authenticated but unauthorized user did not delete person and it returned 403")
    void deletePerson_UnauthorizedUser_DoesNotDeletePersonReturns403() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putPerson_ValidInput_UpdatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        int i = 0;
        long personValidId = 51l;
        try {
            for (String username : new String[]{"regular", "critic"}) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                Optional<UserJDBC> user = userRepo.findByUsername(username);
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(username));
                cookie.setPath("/api");
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setHttpOnly(true);
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + personValidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deletePerson_UnauthorizedUser_DoesNotDeletePersonReturns403", true);
    }

    @Test
    @Order(13)
    @DisplayName("Tests whether DELETE request to /api/persons with invalid input data did not delete person and it returned 400")
    void deletePerson_InvalidInputData_DoesNotDeletePersonReturns400() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putPerson_ValidInput_UpdatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
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
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + input, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deletePerson_InvalidInputData_DoesNotDeletePersonReturns400", true);
    }

    @Test
    @Order(14)
    @DisplayName("Tests whether DELETE request to /api/persons with non-existent dependency objects did not delete person and it returned 404")
    void deletePerson_NonexistentDependencyData_DoesNotDeletePersonReturns404() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putPerson_ValidInput_UpdatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<String> response;
        List<PersonWrapperJDBC> personsBefore;
        List<PersonWrapperJDBC> personsAfter;
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
            for (long invalidId : new long[]{101l, 102l, 150l, 200l, Long.MAX_VALUE}) {
                personsBefore = getAllPersons();
                imagesBefore = getAllProfilePhotos();
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/" + invalidId, HttpMethod.DELETE, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                personsAfter = getAllPersons();
                imagesAfter = getAllProfilePhotos();
                assertPersonsEqual(personsAfter, personsBefore);
                assertImagesEqual(imagesAfter, imagesBefore);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deletePerson_NonexistentDependencyData_DoesNotDeletePersonReturns404", true);
    }

    @Test
    @Order(15)
    @DisplayName("Tests whether DELETE request to /api/persons with valid input data did delete person and it returned 200")
    void deletePerson_ValidInput_DeletesPersonReturns200() {
        Assumptions.assumeTrue(testsPassed.get("postPerson_ValidInput_CreatesPersonReturns200"));
        Assumptions.assumeTrue(testsPassed.get("putPerson_ValidInput_UpdatesPersonReturns200"));
        HttpEntity request;
        ResponseEntity<PersonResponseDTO> response;

        long personValidId1 = 51l;
        long personValidId2 = 52l;

        Optional<UserJDBC> user = userRepo.findByUsername("admin");
        assertThat(user).isNotNull();
        assertThat(user.isPresent()).isTrue();
        HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwtUtils.generateTokenFromUsername(user.get().getUsername()));
        cookie.setPath("/api");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        //----------------------------------------------------------------------------
        //first valid request where personId is 51
        assertThat(personRepo.findById(personValidId1).isPresent()).isTrue();
        Optional<PersonWrapperJDBC> personDB = personRepo.findByIdWithRelations(personValidId1);
        assertThat(existsProfilePhoto(personDB.get().getPerson().getProfilePhoto())).isTrue();

        request = constructRequest(cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + personValidId1, HttpMethod.DELETE, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(personValidId1).isPresent()).isFalse();
        assertThat(existsProfilePhoto(personDB.get().getPerson().getProfilePhoto())).isFalse();
        assertPersonsEqual(personDB.get(), response.getBody());

        //----------------------------------------------------------------------------
        //second valid request where personId is 52
        assertThat(personRepo.findById(personValidId2).isPresent()).isTrue();
        personDB = personRepo.findByIdWithRelations(personValidId2);
        assertThat(existsProfilePhoto(personDB.get().getPerson().getProfilePhoto())).isFalse();

        request = constructRequest(cookie.toString());
        response = restTemplate.exchange(ROUTE + "/" + personValidId2, HttpMethod.DELETE, request, PersonResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(personRepo.findById(personValidId2).isPresent()).isFalse();
        assertThat(existsProfilePhoto(personDB.get().getPerson().getProfilePhoto())).isFalse();
        assertPersonsEqual(personDB.get(), response.getBody());

        testsPassed.put("deletePerson_ValidInput_DeletesPersonReturns200", true);
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

    private PersonRequestDTO getValidPerson(Long personId) {
        PersonRequestDTO person = new PersonRequestDTO();
        person.setId(personId);
        person.setFirstName("Dummy first name");
        person.setLastName("Dummy last name");
        person.setGender(Gender.MALE);
        List<PersonRequestDTO.Profession> professions = new ArrayList<>();
        PersonRequestDTO.Director dir = new PersonRequestDTO.Director();
        dir.setWorkedOn(new ArrayList<>() {
            {
                add(1l);
                add(3l);
            }
        });
        PersonRequestDTO.Writer wri = new PersonRequestDTO.Writer();
        wri.setWorkedOn(new ArrayList<>() {
            {
                add(1l);
                add(2l);
                add(4l);
            }
        });
        PersonRequestDTO.Actor act = new PersonRequestDTO.Actor();
        act.setStar(true);
        PersonRequestDTO.Actor.Acting actin1 = new PersonRequestDTO.Actor.Acting();
        PersonRequestDTO.Actor.Acting actin2 = new PersonRequestDTO.Actor.Acting();
        PersonRequestDTO.Actor.Acting actin3 = new PersonRequestDTO.Actor.Acting();

        actin1.setMediaId(1l);
        actin1.setStarring(true);
        actin1.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
                add("Dummy role 2");
                add("Dummy role 3");
            }
        });
        actin2.setMediaId(2l);
        actin2.setStarring(false);
        actin2.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
            }
        });
        actin3.setMediaId(4l);
        actin3.setStarring(true);
        actin3.setRoles(new ArrayList<>() {
            {
                add("Dummy role 1");
                add("Dummy role 2");
            }
        });
        act.setWorkedOn(new ArrayList<>() {
            {
                add(actin1);
                add(actin2);
                add(actin3);
            }
        });
        professions.add(dir);
        professions.add(wri);
        professions.add(act);
        person.setProfessions(professions);
        return person;
    }

    private MockMultipartFile getValidProfilePhoto(String content) {
        return new MockMultipartFile("profile_photo", "person_routes.png", "image/png", content.getBytes(StandardCharsets.UTF_8));
    }

    private List<Object[]> getBadRequestPersonsAndImages(Long validPersonId, String validImageContent) {
        PersonRequestDTO pFirstName1 = getValidPerson(validPersonId);
        PersonRequestDTO pFirstName2 = getValidPerson(validPersonId);
        PersonRequestDTO pFirstName3 = getValidPerson(validPersonId);
        PersonRequestDTO pFirstName4 = getValidPerson(validPersonId);
        PersonRequestDTO pFirstName5 = getValidPerson(validPersonId);
        pFirstName1.setFirstName(null);
        pFirstName2.setFirstName("");
        pFirstName3.setFirstName(" ");
        pFirstName4.setFirstName("         ");
        pFirstName5.setFirstName(getRandomString(101));

        PersonRequestDTO pLastName1 = getValidPerson(validPersonId);
        PersonRequestDTO pLastName2 = getValidPerson(validPersonId);
        PersonRequestDTO pLastName3 = getValidPerson(validPersonId);
        PersonRequestDTO pLastName4 = getValidPerson(validPersonId);
        PersonRequestDTO pLastName5 = getValidPerson(validPersonId);
        pLastName1.setLastName(null);
        pLastName2.setLastName("");
        pLastName3.setLastName(" ");
        pLastName4.setLastName("         ");
        pLastName5.setLastName(getRandomString(101));

        PersonRequestDTO pGender1 = getValidPerson(validPersonId);
        pGender1.setGender(null);

        //prof list has null elements
        PersonRequestDTO pp1 = getValidPerson(validPersonId);
        PersonRequestDTO pp2 = getValidPerson(validPersonId);
        pp1.setProfessions(new ArrayList<>() {
            {
                add(null);
            }
        });
        pp2.setProfessions(new ArrayList<>() {
            {
                add(null);
                add(null);
                add(null);
            }
        });
        //prof list has director, writer or actor with null name set
        PersonRequestDTO ppN1 = getValidPerson(validPersonId);
        PersonRequestDTO ppN2 = getValidPerson(validPersonId);
        PersonRequestDTO ppN3 = getValidPerson(validPersonId);
        PersonRequestDTO ppN4 = getValidPerson(validPersonId);
        PersonRequestDTO ppN5 = getValidPerson(validPersonId);
        PersonRequestDTO ppN6 = getValidPerson(validPersonId);
        PersonRequestDTO ppN7 = getValidPerson(validPersonId);

        PersonRequestDTO.Director ppND = new PersonRequestDTO.Director();
        PersonRequestDTO.Writer ppNW = new PersonRequestDTO.Writer();
        PersonRequestDTO.Actor ppNA = new PersonRequestDTO.Actor();
        ppNA.setStar(true);

        ppND.setName(null);
        ppNW.setName(null);
        ppNA.setName(null);
        ppN1.setProfessions(new ArrayList<>() {
            {
                add(ppND);
            }
        });
        ppN2.setProfessions(new ArrayList<>() {
            {
                add(ppNW);
            }
        });

        ppN3.setProfessions(new ArrayList<>() {
            {
                add(ppNA);
            }
        });
        ppN4.setProfessions(new ArrayList<>() {
            {
                add(ppND);
                add(ppNW);
            }
        });
        ppN5.setProfessions(new ArrayList<>() {
            {
                add(ppNW);
                add(ppNA);
            }
        });
        ppN6.setProfessions(new ArrayList<>() {
            {
                add(ppND);
                add(ppNA);
            }
        });
        ppN7.setProfessions(new ArrayList<>() {
            {
                add(ppND);
                add(ppNW);
                add(ppNA);
            }
        });
        //prof list has director, writer or actor with invalid name set
        PersonRequestDTO ppNI1 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI2 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI3 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI4 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI5 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI6 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI7 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI8 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI9 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI10 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI11 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI12 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI13 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI14 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI15 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI16 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI17 = getValidPerson(validPersonId);
        PersonRequestDTO ppNI18 = getValidPerson(validPersonId);

        PersonRequestDTO.Director ppNID1 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppNID2 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppNID3 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppNID4 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppNID5 = new PersonRequestDTO.Director();
        PersonRequestDTO.Writer ppNIW1 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppNIW2 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppNIW3 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppNIW4 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppNIW5 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Actor ppNIA1 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppNIA2 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppNIA3 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppNIA4 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppNIA5 = new PersonRequestDTO.Actor();
        ppNIA1.setStar(true);
        ppNIA2.setStar(true);
        ppNIA3.setStar(true);
        ppNIA4.setStar(true);
        ppNIA5.setStar(true);

        ppNID1.setName("");
        ppNID2.setName(" ");
        ppNID3.setName("        ");
        ppNID4.setName(getRandomString(20));
        ppNID5.setName("actor");
        ppNID5.setWorkedOn(new ArrayList<>() {
            {
                add(1l);
                add(2l);
                add(3l);
            }
        });

        ppNIW1.setName("");
        ppNIW2.setName(" ");
        ppNIW3.setName("        ");
        ppNIW4.setName(getRandomString(20));
        ppNIW5.setName("actor");
        ppNIW5.setWorkedOn(new ArrayList<>() {
            {
                add(1l);
                add(2l);
                add(3l);
            }
        });

        ppNIA1.setName("");
        ppNIA2.setName(" ");
        ppNIA3.setName("        ");
        ppNIA4.setName(getRandomString(20));
        ppNIA5.setName("director");

        ppNI1.setProfessions(new ArrayList<>() {
            {
                add(ppNID1);
            }
        });
        ppNI2.setProfessions(new ArrayList<>() {
            {
                add(ppNID2);
            }
        });
        ppNI3.setProfessions(new ArrayList<>() {
            {
                add(ppNID3);
            }
        });
        ppNI4.setProfessions(new ArrayList<>() {
            {
                add(ppNID4);
            }
        });
        ppNI5.setProfessions(new ArrayList<>() {
            {
                add(ppNIW1);
            }
        });
        ppNI6.setProfessions(new ArrayList<>() {
            {
                add(ppNIW2);
            }
        });
        ppNI7.setProfessions(new ArrayList<>() {
            {
                add(ppNIW3);
            }
        });
        ppNI8.setProfessions(new ArrayList<>() {
            {
                add(ppNIW4);
            }
        });
        ppNI9.setProfessions(new ArrayList<>() {
            {
                add(ppNIA1);
            }
        });
        ppNI10.setProfessions(new ArrayList<>() {
            {
                add(ppNIA2);
            }
        });
        ppNI11.setProfessions(new ArrayList<>() {
            {
                add(ppNIA3);
            }
        });
        ppNI12.setProfessions(new ArrayList<>() {
            {
                add(ppNIA4);
            }
        });
        ppNI13.setProfessions(new ArrayList<>() {
            {
                add(ppNIA1);
                add(ppNIA5);
            }
        });
        ppNI14.setProfessions(new ArrayList<>() {
            {
                add(ppNIA3);
                add(ppNID1);
            }
        });
        ppNI15.setProfessions(new ArrayList<>() {
            {
                add(ppNIW4);
                add(ppNID2);
            }
        });
        ppNI16.setProfessions(new ArrayList<>() {
            {
                add(ppNIW4);
                add(ppNIA2);
                add(ppNIA5);
            }
        });
        ppNI17.setProfessions(new ArrayList<>() {
            {
                add(ppNID5);
            }
        });
        ppNI18.setProfessions(new ArrayList<>() {
            {
                add(ppNIW5);
            }
        });

        //prof list has more than 1 valid director, writer or actor
        PersonRequestDTO ppNV1 = getValidPerson(validPersonId);
        PersonRequestDTO ppNV2 = getValidPerson(validPersonId);
        PersonRequestDTO ppNV3 = getValidPerson(validPersonId);
        PersonRequestDTO ppNV4 = getValidPerson(validPersonId);
        PersonRequestDTO ppNV5 = getValidPerson(validPersonId);
        PersonRequestDTO ppNV6 = getValidPerson(validPersonId);

        PersonRequestDTO.Director ppNVD1 = new PersonRequestDTO.Director();
        PersonRequestDTO.Writer ppNVW1 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Actor ppNVA1 = new PersonRequestDTO.Actor();
        ppNVA1.setStar(true);

        ppNV1.setProfessions(new ArrayList<>() {
            {
                add(ppNVD1);
                add(ppNVD1);
            }
        });
        ppNV2.setProfessions(new ArrayList<>() {
            {
                add(ppNVW1);
                add(ppNVW1);
            }
        });
        ppNV3.setProfessions(new ArrayList<>() {
            {
                add(ppNVA1);
                add(ppNVA1);
            }
        });
        ppNV4.setProfessions(new ArrayList<>() {
            {
                add(ppNVD1);
                add(ppNVW1);
                add(ppNVD1);
            }
        });
        ppNV5.setProfessions(new ArrayList<>() {
            {
                add(ppNVD1);
                add(ppNVW1);
                add(ppNVA1);
                add(ppNVW1);
            }
        });
        ppNV6.setProfessions(new ArrayList<>() {
            {
                add(ppNVA1);
                add(ppNVD1);
                add(ppNVW1);
                add(ppNVA1);
            }
        });

        //prof list has a prof element with wokred_on list that contains null as a element
        PersonRequestDTO ppWON1 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON2 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON3 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON4 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON5 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON6 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON7 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON8 = getValidPerson(validPersonId);
        PersonRequestDTO ppWON9 = getValidPerson(validPersonId);
        PersonRequestDTO.Director ppWOND1 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppWOND2 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppWOND3 = new PersonRequestDTO.Director();
        PersonRequestDTO.Writer ppWONVW1 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppWONVW2 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppWONVW3 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Actor ppWONA1 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppWONA2 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppWONA3 = new PersonRequestDTO.Actor();
        ppWONA1.setStar(true);
        ppWONA2.setStar(true);
        ppWONA3.setStar(true);

        ppWOND1.getWorkedOn().add(null);
        ppWOND2.getWorkedOn().add(null);
        ppWOND2.getWorkedOn().add(null);
        ppWOND3.getWorkedOn().add(1l);
        ppWOND3.getWorkedOn().add(2l);
        ppWOND3.getWorkedOn().add(null);

        ppWONVW1.getWorkedOn().add(null);
        ppWONVW2.getWorkedOn().add(null);
        ppWONVW2.getWorkedOn().add(null);
        ppWONVW3.getWorkedOn().add(1l);
        ppWONVW3.getWorkedOn().add(2l);
        ppWONVW3.getWorkedOn().add(null);
        ppWONA1.getWorkedOn().add(null);
        ppWONA2.getWorkedOn().add(null);
        ppWONA2.getWorkedOn().add(null);

        ppWONA3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("role1");
            }
        }));
        ppWONA3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
            {
                add("role1");
                add("role2");
            }
        }));
        ppWONA3.getWorkedOn().add(null);

        ppWON1.setProfessions(new ArrayList<>() {
            {
                add(ppWOND1);

            }
        });
        ppWON2.setProfessions(new ArrayList<>() {
            {
                add(ppWOND2);

            }
        });
        ppWON3.setProfessions(new ArrayList<>() {
            {
                add(ppWOND3);

            }
        });
        ppWON4.setProfessions(new ArrayList<>() {
            {
                add(ppWONVW1);

            }
        });
        ppWON5.setProfessions(new ArrayList<>() {
            {
                add(ppWONVW2);

            }
        });
        ppWON6.setProfessions(new ArrayList<>() {
            {
                add(ppWONVW3);

            }
        });
        ppWON7.setProfessions(new ArrayList<>() {
            {
                add(ppWONA1);

            }
        });
        ppWON8.setProfessions(new ArrayList<>() {
            {
                add(ppWONA2);

            }
        });
        ppWON9.setProfessions(new ArrayList<>() {
            {
                add(ppWONA3);

            }
        });

        //prof list has a prof element with wokred_on list that contains invalid id as element
        PersonRequestDTO ppWOI1 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI2 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI3 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI4 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI5 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI6 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI7 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI8 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI9 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOI10 = getValidPerson(validPersonId);
        PersonRequestDTO.Director ppWOID1 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppWOID2 = new PersonRequestDTO.Director();
        PersonRequestDTO.Director ppWOID3 = new PersonRequestDTO.Director();
        PersonRequestDTO.Writer ppWOIW1 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppWOIW2 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Writer ppWOIW3 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Actor ppWOIA1 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppWOIA2 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppWOIA3 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppWOIA4 = new PersonRequestDTO.Actor();
        ppWOIA1.setStar(true);
        ppWOIA2.setStar(true);
        ppWOIA3.setStar(true);
        ppWOIA4.setStar(true);

        ppWOID1.getWorkedOn().add(0l);
        ppWOID2.getWorkedOn().add(-1l);
        ppWOID2.getWorkedOn().add(-2l);
        ppWOID3.getWorkedOn().add(1l);
        ppWOID3.getWorkedOn().add(2l);
        ppWOID3.getWorkedOn().add(Long.MIN_VALUE);

        ppWOIW1.getWorkedOn().add(0l);
        ppWOIW2.getWorkedOn().add(-1l);
        ppWOIW2.getWorkedOn().add(-2l);
        ppWOIW3.getWorkedOn().add(1l);
        ppWOIW3.getWorkedOn().add(2l);
        ppWOIW3.getWorkedOn().add(Long.MIN_VALUE);

        ppWOIA1.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(null, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOIA2.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(0l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOIA3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(-1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOIA3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(-2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOIA4.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOIA4.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOIA4.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(Long.MIN_VALUE, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));

        ppWOI1.setProfessions(new ArrayList<>() {
            {
                add(ppWOID1);

            }
        });
        ppWOI2.setProfessions(new ArrayList<>() {
            {
                add(ppWOID2);

            }
        });
        ppWOI3.setProfessions(new ArrayList<>() {
            {
                add(ppWOID3);

            }
        });
        ppWOI4.setProfessions(new ArrayList<>() {
            {
                add(ppWOIW1);

            }
        });
        ppWOI5.setProfessions(new ArrayList<>() {
            {
                add(ppWOIW2);

            }
        });
        ppWOI6.setProfessions(new ArrayList<>() {
            {
                add(ppWOIW3);

            }
        });
        ppWOI7.setProfessions(new ArrayList<>() {
            {
                add(ppWOIA1);

            }
        });
        ppWOI8.setProfessions(new ArrayList<>() {
            {
                add(ppWOIA2);

            }
        });
        ppWOI9.setProfessions(new ArrayList<>() {
            {
                add(ppWOIA3);

            }
        });
        ppWOI10.setProfessions(new ArrayList<>() {
            {
                add(ppWOIA4);

            }
        });

        //prof list has a prof element with wokred_on list that contains duplicate id values
        PersonRequestDTO ppWOD1 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOD2 = getValidPerson(validPersonId);
        PersonRequestDTO ppWOD3 = getValidPerson(validPersonId);
        PersonRequestDTO.Director ppWODD1 = new PersonRequestDTO.Director();
        PersonRequestDTO.Writer ppWODW1 = new PersonRequestDTO.Writer();
        PersonRequestDTO.Actor ppWODA1 = new PersonRequestDTO.Actor();
        ppWODA1.setStar(true);

        ppWODD1.getWorkedOn().add(1l);
        ppWODD1.getWorkedOn().add(2l);
        ppWODD1.getWorkedOn().add(1l);

        ppWODW1.getWorkedOn().add(1l);
        ppWODW1.getWorkedOn().add(2l);
        ppWODW1.getWorkedOn().add(1l);

        ppWODA1.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWODA1.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWODA1.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppWOD1.setProfessions(new ArrayList<>() {
            {
                add(ppWODD1);
            }
        });
        ppWOD2.setProfessions(new ArrayList<>() {
            {
                add(ppWODW1);
            }
        });
        ppWOD3.setProfessions(new ArrayList<>() {
            {
                add(ppWODA1);
            }
        });

        //prof list has a Actor element with star attribute as null
        PersonRequestDTO ppAS1 = getValidPerson(validPersonId);
        ppAS1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(null));
            }
        });

        //prof list has a Actor element with acting list with null elements
        PersonRequestDTO ppAAN1 = getValidPerson(validPersonId);
        PersonRequestDTO ppAAN2 = getValidPerson(validPersonId);
        PersonRequestDTO ppAAN3 = getValidPerson(validPersonId);
        PersonRequestDTO.Actor ppAN1 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppAN2 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppAN3 = new PersonRequestDTO.Actor();
        ppAN1.setStar(true);
        ppAN2.setStar(true);
        ppAN3.setStar(true);

        ppAN1.getWorkedOn().add(null);
        ppAN2.getWorkedOn().add(null);
        ppAN2.getWorkedOn().add(null);
        ppAN3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAN3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAN3.getWorkedOn().add(null);

        ppAAN1.setProfessions(new ArrayList<>() {
            {
                add(ppAN1);
            }
        });
        ppAAN2.setProfessions(new ArrayList<>() {
            {
                add(ppAN2);
            }
        });
        ppAAN3.setProfessions(new ArrayList<>() {
            {
                add(ppAN3);
            }
        });

        //prof list has a Actor element with acting list with null mediaId
        PersonRequestDTO ppAAMN1 = getValidPerson(validPersonId);
        PersonRequestDTO ppAAMN2 = getValidPerson(validPersonId);
        PersonRequestDTO ppAAMN3 = getValidPerson(validPersonId);
        PersonRequestDTO.Actor ppAMN1 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppAMN2 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppAMN3 = new PersonRequestDTO.Actor();
        ppAMN1.setStar(true);
        ppAMN2.setStar(true);
        ppAMN3.setStar(true);
        ppAMN1.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(null, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMN2.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(null, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMN2.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(null, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMN3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMN3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMN3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(null, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAAMN1.setProfessions(new ArrayList<>() {
            {
                add(ppAMN1);
            }
        });
        ppAAMN2.setProfessions(new ArrayList<>() {
            {
                add(ppAMN2);
            }
        });
        ppAAMN3.setProfessions(new ArrayList<>() {
            {
                add(ppAMN3);
            }
        });

        //prof list has a Actor element with acting list with invalid mediaId
        PersonRequestDTO ppAAMI1 = getValidPerson(validPersonId);
        PersonRequestDTO ppAAMI2 = getValidPerson(validPersonId);
        PersonRequestDTO ppAAMI3 = getValidPerson(validPersonId);
        PersonRequestDTO.Actor ppAMI1 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppAMI2 = new PersonRequestDTO.Actor();
        PersonRequestDTO.Actor ppAMI3 = new PersonRequestDTO.Actor();
        ppAMI1.setStar(true);
        ppAMI2.setStar(true);
        ppAMI3.setStar(true);
        ppAMI1.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(0l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMI2.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(-1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMI2.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(-2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMI3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMI3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAMI3.getWorkedOn().add(new PersonRequestDTO.Actor.Acting(Long.MIN_VALUE, true, new ArrayList<>() {
            {
                add("Role 1");
            }
        }));
        ppAAMI1.setProfessions(new ArrayList<>() {
            {
                add(ppAMI1);
            }
        });
        ppAAMI2.setProfessions(new ArrayList<>() {
            {
                add(ppAMI2);
            }
        });
        ppAAMI3.setProfessions(new ArrayList<>() {
            {
                add(ppAMI3);
            }
        });

        //prof list has a Actor element with acting list with null starring
        PersonRequestDTO ppAASN1 = getValidPerson(validPersonId);
        ppAASN1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, null, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                    }
                }));
            }
        });

        //prof list has a Actor element with acting list with roles list with null elements
        PersonRequestDTO ppAARN1 = getValidPerson(validPersonId);
        PersonRequestDTO ppAARN2 = getValidPerson(validPersonId);
        PersonRequestDTO ppAARN3 = getValidPerson(validPersonId);
        ppAARN1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add(null);
                            }
                        }));
                    }
                }));
            }
        });
        ppAARN2.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add(null);
                                add(null);
                            }
                        }));
                    }
                }));
            }
        });
        ppAARN3.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                                add("Role 2");
                                add(null);
                            }
                        }));
                    }
                }));
            }
        });

        //prof list has a Actor element with acting list with roles list with invalid elements
        PersonRequestDTO ppAARI1 = getValidPerson(validPersonId);
        PersonRequestDTO ppAARI2 = getValidPerson(validPersonId);
        PersonRequestDTO ppAARI3 = getValidPerson(validPersonId);
        PersonRequestDTO ppAARI4 = getValidPerson(validPersonId);
        PersonRequestDTO ppAARI5 = getValidPerson(validPersonId);
        ppAARI1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add("");
                            }
                        }));
                    }
                }));
            }
        });
        ppAARI2.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add(" ");
                            }
                        }));
                    }
                }));
            }
        });
        ppAARI3.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add("         ");
                            }
                        }));
                    }
                }));
            }
        });
        ppAARI4.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add(getRandomString(301));
                            }
                        }));
                    }
                }));
            }
        });
        ppAARI5.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                                add("Role 2");
                                add("  ");
                            }
                        }));
                    }
                }));
            }
        });

        //inputs
        List<Object[]> inputs = new ArrayList<>() {
            {
                //invalid first name 0
                add(new Object[]{pFirstName1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pFirstName2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pFirstName3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pFirstName4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pFirstName5, getValidProfilePhoto(validImageContent)});
                //invalid last name 5
                add(new Object[]{pLastName1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pLastName2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pLastName3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pLastName4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pLastName5, getValidProfilePhoto(validImageContent)});
                //invalid gender 10
                add(new Object[]{pGender1, getValidProfilePhoto(validImageContent)});
                //invalid professions
                //prof list has null elements 11
                add(new Object[]{pp1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pp2, getValidProfilePhoto(validImageContent)});
                //prof list has director, writer or actor with null name set 13
                add(new Object[]{ppN1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppN2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppN3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppN4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppN5, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppN6, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppN7, getValidProfilePhoto(validImageContent)});
                //prof list has director, writer or actor with invalid name set 20
                add(new Object[]{ppNI1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI5, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI6, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI7, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI8, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI9, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI10, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI11, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI12, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI13, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI14, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI15, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI16, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI17, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNI18, getValidProfilePhoto(validImageContent)});
                //prof list has more than 1 valid director, writer or actor 38
                add(new Object[]{ppNV1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNV2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNV3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNV4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNV5, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppNV6, getValidProfilePhoto(validImageContent)});
                //prof list has a prof element with wokred_on list that contains null as a element 44
                add(new Object[]{ppWON1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON5, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON6, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON7, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON8, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWON9, getValidProfilePhoto(validImageContent)});
                //prof list has a prof element with wokred_on list that contains invalid id as element 53
                add(new Object[]{ppWOI1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI5, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI6, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI7, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI8, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI9, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOI10, getValidProfilePhoto(validImageContent)});
                //prof list has a prof element with wokred_on list that contains duplicate id values 63
                add(new Object[]{ppWOD1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOD2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppWOD3, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with star attribute as null 66
                add(new Object[]{ppAS1, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with acting list with null elements 67
                add(new Object[]{ppAAN1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAAN2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAAN3, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with acting list with null mediaId 70
                add(new Object[]{ppAAMN1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAAMN2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAAMN3, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with acting list with invalid mediaId 73
                add(new Object[]{ppAAMI1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAAMI2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAAMI3, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with acting list with null starring 76
                add(new Object[]{ppAASN1, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with acting list with roles list with null elements 77
                add(new Object[]{ppAARN1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAARN2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAARN3, getValidProfilePhoto(validImageContent)});
                //prof list has a Actor element with acting list with roles list with invalid elements 80
                add(new Object[]{ppAARI1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAARI2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAARI3, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAARI4, getValidProfilePhoto(validImageContent)});
                add(new Object[]{ppAARI5, getValidProfilePhoto(validImageContent)});
            }
        };

        //invalid file name i=85
        String[] invImageNames = {null, "", " ", "     ", "jpg", "aaaaaaaaa", "..", "...", " .   . ", ".", " . ", ".jpg.", ".png.", "jpg.",
            "..jpg", "png.", "http://www.website.com/images/.jpg.", ".jpg/", ".jpg/website", ".jpg/jpg", ".gpj", ".g", "aaaa.g", "  a  aa  aa.g",
            "http://www.google.com/images/aaaa.jgp", "/", "\\", "aaaaa aaaaa", "   aaaaaa", "aaaaaa  ", "aaaa.aaaa..aaa", "..", "/////",
            "/aaa/aaa/aa", "aaa\\aaaa\\aaaa", "/aaa..aaa..aa..aa", "http://www.google.com/aaaa. jpg",
            "http://www.website.com/images/.jpg.", "..jpg", "aaa aaa aaa..jpg",
            ".gpj", ".g", "aaaaa.g", "  a  aa  aa.g", "http://www.google.com/images/aaaa.jgp", "image.mp3", "image.exe", "image.mp4", "image.gif"};
        for (String invalidName : invImageNames) {
            try {
                MockMultipartFile pom = getValidProfilePhoto(validImageContent);
                MockMultipartFile invalidProfilePhoto = new MockMultipartFile(pom.getName(), invalidName, pom.getContentType(), pom.getBytes());
                inputs.add(new Object[]{getValidPerson(validPersonId), invalidProfilePhoto});
            } catch (IOException ex) {
                fail("getBytes() should not have failed!");
            }
        }
        //invalid file size
        MockMultipartFile pom = getValidProfilePhoto(validImageContent);
        MockMultipartFile invalidProfilePhoto = new MockMultipartFile(pom.getName(), pom.getOriginalFilename(), pom.getContentType(), new byte[8388998]);
        inputs.add(new Object[]{getValidPerson(validPersonId), invalidProfilePhoto});

        invalidProfilePhoto = new MockMultipartFile(pom.getName(), pom.getOriginalFilename(), pom.getContentType(), new byte[0]);
        inputs.add(new Object[]{getValidPerson(validPersonId), invalidProfilePhoto});

        return inputs;
    }

    private List<Object[]> getNonExistentDependencyPersonsAndImages(Long validPersonId, String validImageContent) {
        PersonRequestDTO pD1 = getValidPerson(validPersonId);
        PersonRequestDTO pD2 = getValidPerson(validPersonId);
        PersonRequestDTO pD3 = getValidPerson(validPersonId);

        PersonRequestDTO pW1 = getValidPerson(validPersonId);
        PersonRequestDTO pW2 = getValidPerson(validPersonId);
        PersonRequestDTO pW3 = getValidPerson(validPersonId);

        PersonRequestDTO pA1 = getValidPerson(validPersonId);
        PersonRequestDTO pA2 = getValidPerson(validPersonId);
        PersonRequestDTO pA3 = getValidPerson(validPersonId);

        pD1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Director(new ArrayList<>() {
                    {
                        add(100l);
                    }
                }));
            }
        });
        pD2.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Director(new ArrayList<>() {
                    {
                        add(1l);
                        add(2l);
                        add(100l);
                    }
                }));
            }
        });
        pD3.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Director(new ArrayList<>() {
                    {
                        add(1l);
                        add(Long.MAX_VALUE);
                        add(2l);
                    }
                }));
            }
        });

        pW1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Writer(new ArrayList<>() {
                    {
                        add(100l);
                    }
                }));
            }
        });
        pW2.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Writer(new ArrayList<>() {
                    {
                        add(1l);
                        add(2l);
                        add(100l);
                    }
                }));
            }
        });
        pW3.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Writer(new ArrayList<>() {
                    {
                        add(1l);
                        add(Long.MAX_VALUE);
                        add(2l);
                    }
                }));
            }
        });
        pA1.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(100l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                    }
                }));
            }
        });
        pA2.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                        add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                        add(new PersonRequestDTO.Actor.Acting(100l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                    }
                }));
            }
        });
        pA3.setProfessions(new ArrayList<>() {
            {
                add(new PersonRequestDTO.Actor(true, new ArrayList<>() {
                    {
                        add(new PersonRequestDTO.Actor.Acting(1l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                        add(new PersonRequestDTO.Actor.Acting(Long.MAX_VALUE, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                        add(new PersonRequestDTO.Actor.Acting(2l, true, new ArrayList<>() {
                            {
                                add("Role 1");
                            }
                        }));
                    }
                }));
            }
        });

        return new ArrayList<>() {
            {
                //non-existent director worked on medias
                add(new Object[]{pD1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pD2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pD3, getValidProfilePhoto(validImageContent)});
                //non-existent writer worked on medias
                add(new Object[]{pW1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pW2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pW3, getValidProfilePhoto(validImageContent)});
                //non-existent actor worked on medias
                add(new Object[]{pA1, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pA2, getValidProfilePhoto(validImageContent)});
                add(new Object[]{pA3, getValidProfilePhoto(validImageContent)});
            }
        };
    }

    private HttpEntity<MultiValueMap<String, Object>> constructRequest(PersonRequestDTO person, MockMultipartFile profilePhoto, String cookie) throws AssertionError {
        HttpHeaders requestHeader = new HttpHeaders();
        HttpHeaders personHeader = new HttpHeaders();
        HttpHeaders imageHeader = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpEntity<PersonRequestDTO> personBody;
        HttpEntity<Resource> imageBody;

        if (cookie != null) {
            requestHeader.set(HttpHeaders.COOKIE, cookie);
        }
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        personHeader.setContentType(MediaType.APPLICATION_JSON);
        personBody = new HttpEntity<>(person, personHeader);
        body.add("person", personBody);
        if (profilePhoto != null) {
            try {
                imageHeader.setContentType(MediaType.valueOf(profilePhoto.getContentType()));
                imageBody = new HttpEntity<>(profilePhoto.getResource(), imageHeader);
                body.add("profile_photo", imageBody);

            } catch (Exception ex) {
                throw new AssertionError("Failed to set profile_photo part of multipart form data of HttpEntity request", ex);
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
    private void assertPersonsEqual(List<PersonWrapperJDBC> actual, List<PersonWrapperJDBC> expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            checkValues(actual.get(i), expected.get(i));
        }
    }

    private void checkValues(PersonWrapperJDBC actual, PersonWrapperJDBC expected) {
        assertThat(actual).isNotNull();
        checkPerson(actual.getPerson(), expected.getPerson());
        checkDirector(actual.getDirector(), expected.getDirector());
        checkWriter(actual.getWriter(), expected.getWriter());
        checkActor(actual.getActor(), expected.getActor());
    }

    private void checkPerson(PersonJDBC actual, PersonJDBC expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isNotNull().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotNull().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());
    }

    private void checkDirector(DirectorJDBC actual, DirectorJDBC expected) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isNull();
            assertThat(actual.getLastName()).isNull();
            assertThat(actual.getGender()).isNull();
            assertThat(actual.getProfilePhoto()).isNull();

            assertThat(actual.getMedias()).isNotNull();
            assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
            for (int i = 0; i < actual.getMedias().size(); i++) {
                assertThat(actual.getMedias().get(i)).isNotNull();
                assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());

                assertThat(actual.getMedias().get(i).getTitle()).isNull();
                assertThat(actual.getMedias().get(i).getDescription()).isNull();
                assertThat(actual.getMedias().get(i).getReleaseDate()).isNull();
                assertThat(actual.getMedias().get(i).getCoverImage()).isNull();
                assertThat(actual.getMedias().get(i).getAudienceRating()).isNull();
                assertThat(actual.getMedias().get(i).getCriticRating()).isNull();

                assertThat(actual.getMedias().get(i).getGenres()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getDirectors()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getWriters()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getActings()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getCritiques()).isNotNull().isEmpty();

            }
        }
    }

    private void checkWriter(WriterJDBC actual, WriterJDBC expected) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isNull();
            assertThat(actual.getLastName()).isNull();
            assertThat(actual.getGender()).isNull();
            assertThat(actual.getProfilePhoto()).isNull();

            assertThat(actual.getMedias()).isNotNull();
            assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
            for (int i = 0; i < actual.getMedias().size(); i++) {
                assertThat(actual.getMedias().get(i)).isNotNull();
                assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());

                assertThat(actual.getMedias().get(i).getTitle()).isNull();
                assertThat(actual.getMedias().get(i).getDescription()).isNull();
                assertThat(actual.getMedias().get(i).getReleaseDate()).isNull();
                assertThat(actual.getMedias().get(i).getCoverImage()).isNull();
                assertThat(actual.getMedias().get(i).getAudienceRating()).isNull();
                assertThat(actual.getMedias().get(i).getCriticRating()).isNull();

                assertThat(actual.getMedias().get(i).getGenres()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getDirectors()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getWriters()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getActings()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getCritiques()).isNotNull().isEmpty();

            }

        }
    }

    private void checkActor(ActorJDBC actual, ActorJDBC expected) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isNull();
            assertThat(actual.getLastName()).isNull();
            assertThat(actual.getGender()).isNull();
            assertThat(actual.getProfilePhoto()).isNull();
            assertThat(actual.isStar()).isNotNull().isEqualTo(expected.isStar());

            assertThat(actual.getActings()).isNotNull();
            assertThat(actual.getActings().size()).isEqualTo(expected.getActings().size());
            for (int i = 0; i < actual.getActings().size(); i++) {
                assertThat(actual.getActings().get(i)).isNotNull();

                assertThat(actual.getActings().get(i).getMedia()).isNotNull();
                assertThat(actual.getActings().get(i).getMedia().getId()).isNotNull().isEqualTo(expected.getActings().get(i).getMedia().getId());

                assertThat(actual.getActings().get(i).getMedia().getTitle()).isNull();
                assertThat(actual.getActings().get(i).getMedia().getDescription()).isNull();
                assertThat(actual.getActings().get(i).getMedia().getReleaseDate()).isNull();
                assertThat(actual.getActings().get(i).getMedia().getCoverImage()).isNull();
                assertThat(actual.getActings().get(i).getMedia().getAudienceRating()).isNull();
                assertThat(actual.getActings().get(i).getMedia().getCriticRating()).isNull();

                assertThat(actual.getActings().get(i).getMedia().getGenres()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getDirectors()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getWriters()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getActings()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getCritiques()).isNotNull().isEmpty();

                assertThat(actual.getActings().get(i).getActor()).isNotNull();
                assertThat(actual.getActings().get(i).getActor() == actual).isTrue();
                assertThat(actual.getActings().get(i).isStarring()).isNotNull().isEqualTo(expected.getActings().get(i).isStarring());

                assertThat(actual.getActings().get(i).getRoles()).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActings().get(i).getRoles().size());
                for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                    assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                    assertThat(actual.getActings().get(i).getRoles().get(j).getActing() == actual.getActings().get(i)).isTrue();
                    assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isNotNull().isEqualTo(expected.getActings().get(i).getRoles().get(j).getId());
                    assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isNotNull().isEqualTo(expected.getActings().get(i).getRoles().get(j).getName());
                }
            }
        }
    }

    private void assertPersonsEqual(PersonResponseDTO actual, PersonRequestDTO expected, String expectedProfilePhotoUrl) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isNotBlank().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotBlank().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getProfilePhotoUrl()).isEqualTo(expectedProfilePhotoUrl);

        assertThat(actual.getProfessions()).isNotNull();
        assertThat(actual.getProfessions().size()).isEqualTo(expected.getProfessions().size());
        for (int i = 0; i < actual.getProfessions().size(); i++) {
            assertThat(actual.getProfessions().get(i)).isNotNull();
            assertThat(expected.getProfessions().get(i)).isNotNull();
            if (actual.getProfessions().get(i) instanceof PersonResponseDTO.Director) {
                assertThat(expected.getProfessions().get(i) instanceof PersonRequestDTO.Director).isTrue();
                assertThat(expected.getProfessions().get(i).getName()).isEqualTo("director");
                assertThat(((PersonResponseDTO.Director) actual.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(((PersonResponseDTO.Director) actual.getProfessions().get(i)).getWorkedOn().size()).isEqualTo(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < ((PersonResponseDTO.Director) actual.getProfessions().get(i)).getWorkedOn().size(); j++) {
                    assertThat(((PersonResponseDTO.Director) actual.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull().isEqualTo(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn().get(j));
                }

            } else if (actual.getProfessions().get(i) instanceof PersonResponseDTO.Writer) {
                assertThat(expected.getProfessions().get(i) instanceof PersonRequestDTO.Writer).isTrue();
                assertThat(expected.getProfessions().get(i).getName()).isEqualTo("writer");
                assertThat(((PersonResponseDTO.Writer) actual.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(((PersonResponseDTO.Writer) actual.getProfessions().get(i)).getWorkedOn().size()).isEqualTo(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < ((PersonResponseDTO.Writer) actual.getProfessions().get(i)).getWorkedOn().size(); j++) {
                    assertThat(((PersonResponseDTO.Writer) actual.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull().isEqualTo(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().get(j));
                }

            } else if (actual.getProfessions().get(i) instanceof PersonResponseDTO.Actor) {
                assertThat(expected.getProfessions().get(i) instanceof PersonRequestDTO.Actor).isTrue();
                assertThat(expected.getProfessions().get(i).getName()).isEqualTo("actor");
                assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).isStar()).isNotNull().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).isStar());
                assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().size()).isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < ((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().size(); j++) {
                    assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();

                    assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getMediaId()).isNotNull().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getMediaId());
                    assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).isStarring()).isNotNull().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).isStarring());
                    assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getRoles()).isNotNull();
                    assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles()).isNotNull();

                    assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getRoles().size()).isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().size());

                    for (int k = 0; k < ((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getRoles().size(); k++) {
                        assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k)).isNotNull();
                        assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k)).isNotNull();
                        assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k).getId()).isNotNull().isEqualTo(k + 1);
                        assertThat(((PersonResponseDTO.Actor) actual.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k).getName()).isNotBlank().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k));

                    }

                }

            } else {
                throw new AssertionError("Unknown profession type!");
            }

        }

    }

    private void assertPersonsEqual(PersonWrapperJDBC actual, PersonResponseDTO expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getPerson()).isNotNull();
        assertThat(actual.getPerson().getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getPerson().getFirstName()).isNotBlank().isEqualTo(expected.getFirstName());
        assertThat(actual.getPerson().getLastName()).isNotBlank().isEqualTo(expected.getLastName());
        assertThat(actual.getPerson().getGender()).isNotNull().isEqualTo(expected.getGender());
        if (expected.getProfilePhotoUrl() == null) {
            assertThat(actual.getPerson().getProfilePhoto()).isNull();
        } else {
            assertThat(config.getPersonImagesBaseUrl() + actual.getPerson().getProfilePhoto()).isEqualTo(expected.getProfilePhotoUrl());
        }
        int numberOfProfessions = 0;
        if (actual.getDirector() != null) {
            numberOfProfessions++;
        }
        if (actual.getWriter() != null) {
            numberOfProfessions++;
        }
        if (actual.getActor() != null) {
            numberOfProfessions++;
        }
        assertThat(expected.getProfessions()).isNotNull();
        assertThat(numberOfProfessions).isEqualTo(expected.getProfessions().size());
        for (int i = 0; i < expected.getProfessions().size(); i++) {
            if (expected.getProfessions().get(i) instanceof PersonResponseDTO.Director) {
                assertThat(actual.getDirector()).isNotNull();
                assertThat(actual.getDirector().getId()).isNotNull().isEqualTo(actual.getPerson().getId());
                assertThat(actual.getDirector().getMedias()).isNotNull();
                assertThat(((PersonResponseDTO.Director) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(actual.getDirector().getMedias().size()).isEqualTo(((PersonResponseDTO.Director) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < actual.getDirector().getMedias().size(); j++) {
                    assertThat(actual.getDirector().getMedias().get(j)).isNotNull();
                    assertThat(((PersonResponseDTO.Director) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(actual.getDirector().getMedias().get(j).getId()).isNotNull().isEqualTo(((PersonResponseDTO.Director) expected.getProfessions().get(i)).getWorkedOn().get(j));
                }

            } else if (expected.getProfessions().get(i) instanceof PersonResponseDTO.Writer) {
                assertThat(actual.getWriter()).isNotNull();
                assertThat(actual.getWriter().getId()).isNotNull().isEqualTo(actual.getPerson().getId());
                assertThat(actual.getWriter().getMedias()).isNotNull();
                assertThat(((PersonResponseDTO.Writer) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(actual.getWriter().getMedias().size()).isEqualTo(((PersonResponseDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < actual.getWriter().getMedias().size(); j++) {
                    assertThat(actual.getWriter().getMedias().get(j)).isNotNull();
                    assertThat(((PersonResponseDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(actual.getWriter().getMedias().get(j).getId()).isNotNull().isEqualTo(((PersonResponseDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().get(j));
                }

            } else if (expected.getProfessions().get(i) instanceof PersonResponseDTO.Actor) {
                assertThat(actual.getActor()).isNotNull();
                assertThat(actual.getActor().getId()).isNotNull().isEqualTo(actual.getPerson().getId());
                assertThat(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).isStar()).isNotNull();
                assertThat(actual.getActor().isStar()).isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).isStar());
                assertThat(actual.getActor().getActings()).isNotNull();
                assertThat(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(actual.getActor().getActings().size()).isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < actual.getActor().getActings().size(); j++) {
                    assertThat(actual.getActor().getActings().get(j)).isNotNull();
                    assertThat(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(actual.getActor().getActings().get(j).getMedia()).isNotNull();
                    assertThat(actual.getActor().getActings().get(j).getMedia().getId()).isNotNull().isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getMediaId());
                    assertThat(actual.getActor().getActings().get(j).isStarring()).isNotNull().isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).isStarring());
                    assertThat(actual.getActor().getActings().get(j).getRoles()).isNotNull();
                    assertThat(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles()).isNotNull();
                    assertThat(actual.getActor().getActings().get(j).getRoles().size()).isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().size());
                    for (int k = 0; k < actual.getActor().getActings().get(j).getRoles().size(); k++) {
                        assertThat(actual.getActor().getActings().get(j).getRoles().get(k)).isNotNull();
                        assertThat(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k)).isNotNull();
                        assertThat(actual.getActor().getActings().get(j).getRoles().get(k).getId()).isNotNull().isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k).getId());
                        assertThat(actual.getActor().getActings().get(j).getRoles().get(k).getName()).isNotBlank().isEqualTo(((PersonResponseDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k).getName());
                    }
                }
            } else {
                throw new AssertionError("Unknown PersonResponseDTO profession!");
            }

        }

    }

    private void assertPersonsEqual(PersonWrapperJDBC actual, PersonRequestDTO expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getPerson()).isNotNull();
        assertThat(actual.getPerson().getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getPerson().getFirstName()).isNotBlank().isEqualTo(expected.getFirstName());
        assertThat(actual.getPerson().getLastName()).isNotBlank().isEqualTo(expected.getLastName());
        assertThat(actual.getPerson().getGender()).isNotNull().isEqualTo(expected.getGender());
        int numberOfProfessions = 0;
        if (actual.getDirector() != null) {
            numberOfProfessions++;
        }
        if (actual.getWriter() != null) {
            numberOfProfessions++;
        }
        if (actual.getActor() != null) {
            numberOfProfessions++;
        }
        assertThat(expected.getProfessions()).isNotNull();
        assertThat(numberOfProfessions).isEqualTo(expected.getProfessions().size());
        for (int i = 0; i < expected.getProfessions().size(); i++) {
            if (expected.getProfessions().get(i) instanceof PersonRequestDTO.Director) {
                assertThat(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getName()).isNotBlank().isEqualTo("director");
                assertThat(actual.getDirector()).isNotNull();
                assertThat(actual.getDirector().getId()).isNotNull().isEqualTo(actual.getPerson().getId());
                assertThat(actual.getDirector().getMedias()).isNotNull();
                assertThat(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(actual.getDirector().getMedias().size()).isEqualTo(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < actual.getDirector().getMedias().size(); j++) {
                    assertThat(actual.getDirector().getMedias().get(j)).isNotNull();
                    assertThat(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(actual.getDirector().getMedias().get(j).getId()).isNotNull().isEqualTo(((PersonRequestDTO.Director) expected.getProfessions().get(i)).getWorkedOn().get(j));
                }

            } else if (expected.getProfessions().get(i) instanceof PersonRequestDTO.Writer) {
                assertThat(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getName()).isNotBlank().isEqualTo("writer");
                assertThat(actual.getWriter()).isNotNull();
                assertThat(actual.getWriter().getId()).isNotNull().isEqualTo(actual.getPerson().getId());
                assertThat(actual.getWriter().getMedias()).isNotNull();
                assertThat(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(actual.getWriter().getMedias().size()).isEqualTo(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < actual.getWriter().getMedias().size(); j++) {
                    assertThat(actual.getWriter().getMedias().get(j)).isNotNull();
                    assertThat(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(actual.getWriter().getMedias().get(j).getId()).isNotNull().isEqualTo(((PersonRequestDTO.Writer) expected.getProfessions().get(i)).getWorkedOn().get(j));
                }

            } else if (expected.getProfessions().get(i) instanceof PersonRequestDTO.Actor) {
                assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getName()).isNotBlank().isEqualTo("actor");
                assertThat(actual.getActor()).isNotNull();
                assertThat(actual.getActor().getId()).isNotNull().isEqualTo(actual.getPerson().getId());
                assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).isStar()).isNotNull();
                assertThat(actual.getActor().isStar()).isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).isStar());
                assertThat(actual.getActor().getActings()).isNotNull();
                assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn()).isNotNull();
                assertThat(actual.getActor().getActings().size()).isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().size());
                for (int j = 0; j < actual.getActor().getActings().size(); j++) {
                    assertThat(actual.getActor().getActings().get(j)).isNotNull();
                    assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j)).isNotNull();
                    assertThat(actual.getActor().getActings().get(j).getMedia()).isNotNull();
                    assertThat(actual.getActor().getActings().get(j).getMedia().getId()).isNotNull().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getMediaId());
                    assertThat(actual.getActor().getActings().get(j).isStarring()).isNotNull().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).isStarring());
                    assertThat(actual.getActor().getActings().get(j).getRoles()).isNotNull();
                    assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles()).isNotNull();
                    assertThat(actual.getActor().getActings().get(j).getRoles().size()).isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().size());
                    for (int k = 0; k < actual.getActor().getActings().get(j).getRoles().size(); k++) {
                        assertThat(actual.getActor().getActings().get(j).getRoles().get(k)).isNotNull();
                        assertThat(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k)).isNotNull();
                        assertThat(actual.getActor().getActings().get(j).getRoles().get(k).getId()).isNotNull().isEqualTo(k + 1);
                        assertThat(actual.getActor().getActings().get(j).getRoles().get(k).getName()).isNotBlank().isEqualTo(((PersonRequestDTO.Actor) expected.getProfessions().get(i)).getWorkedOn().get(j).getRoles().get(k));
                    }
                }
            } else {
                throw new AssertionError("Unknown PersonResponseDTO profession!");
            }

        }

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

    private boolean arePersonsEqual(PersonWrapperJDBC actual, PersonRequestDTO expected) {
        try {
            assertPersonsEqual(actual, expected);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    private boolean existsProfilePhoto(String filename) {
        if (filename != null) {
            try {
                Resource r = fileRepo.getPersonProfilePhoto(filename);
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

    private List<Resource> getAllProfilePhotos() {
        try {
            String folderPath = config.getPersonImagesFolderPath();
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

    private List<PersonWrapperJDBC> getAllPersons() {
        return personRepo.findAllWithRelationsPaginated(1, 100);
    }

    private void changeAttributes(PersonRequestDTO person) {
        person.setFirstName("Dummy PUT first name");
        person.setLastName("Dummy PUT last name");
        person.setGender(Gender.FEMALE);
        List<PersonRequestDTO.Profession> professions = new ArrayList<>();
        PersonRequestDTO.Director dir = new PersonRequestDTO.Director();
        dir.setWorkedOn(new ArrayList<>() {
            {
                add(1l);
                add(2l);
                add(3l);
            }
        });
        PersonRequestDTO.Writer wri = new PersonRequestDTO.Writer();
        wri.setWorkedOn(new ArrayList<>() {
            {
                add(1l);
                add(4l);
            }
        });
        PersonRequestDTO.Actor act = new PersonRequestDTO.Actor();
        act.setStar(true);
        PersonRequestDTO.Actor.Acting actin1 = new PersonRequestDTO.Actor.Acting();
        PersonRequestDTO.Actor.Acting actin2 = new PersonRequestDTO.Actor.Acting();

        actin1.setMediaId(3l);
        actin1.setStarring(true);
        actin1.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
            }
        });
        actin2.setMediaId(6l);
        actin2.setStarring(false);
        actin2.setRoles(new ArrayList<>() {
            {
                add("Dummy PUT role 1");
                add("Dummy PUT role 2");
                add("Dummy PUT role 3");
            }
        });
        act.setWorkedOn(new ArrayList<>() {
            {
                add(actin1);
                add(actin2);
            }
        });
        professions.add(dir);
        professions.add(wri);
        professions.add(act);
        person.setProfessions(professions);
    }

}
