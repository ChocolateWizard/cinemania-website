/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.secured;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.user.UserLoginDTO;
import com.borak.cwb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.domain.SecurityUser;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.helpers.Pair;
import com.borak.cwb.backend.helpers.TestJsonResponseReader;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.helpers.TestUtil;
import com.borak.cwb.backend.helpers.repositories.UserTestRepository;
import com.borak.cwb.backend.logic.security.JwtUtils;
import com.borak.cwb.backend.repository.file.FileRepository;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(6)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthRoutesTest {

    private final TestRestTemplate restTemplate;
    private final UserTestRepository userRepo;
    private final FileRepository fileRepo;
    private final PasswordEncoder pswEncoder;
    private final ConfigProperties config;
    private final JwtUtils jwtUtils;
    private final TestJsonResponseReader jsonReader;
    private final TestUtil testUtil;

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/auth";

    @Autowired
    public AuthRoutesTest(TestRestTemplate restTemplate, UserTestRepository userRepo, FileRepository fileRepo, PasswordEncoder pswEncoder, ConfigProperties config, JwtUtils jwtUtils, TestJsonResponseReader jsonReader, TestUtil testUtil) {
        this.restTemplate = restTemplate;
        this.userRepo = userRepo;
        this.fileRepo = fileRepo;
        this.pswEncoder = pswEncoder;
        this.config = config;
        this.jwtUtils = jwtUtils;
        this.jsonReader = jsonReader;
        this.testUtil = testUtil;
    }

    static {
        TESTS_PASSED.put("register_InvalidInput_NotCreatedUserReturns400", false);
        TESTS_PASSED.put("register_DuplicateInput_NotCreatedUserReturns400", false);
        TESTS_PASSED.put("register_NonExistentDependencyObjects_NotCreatedUserReturns404", false);
        TESTS_PASSED.put("register_ValidInput_CreatedUserReturns200", false);

        TESTS_PASSED.put("login_InvalidInput_NotLoggedInReturns400", false);
        TESTS_PASSED.put("login_NonExistentUser_NotLoggedInReturns401", false);
        TESTS_PASSED.put("login_ValidInput_ReturnsUserJWTAnd200", false);
        TESTS_PASSED.put("login_SuccessiveValidInput_ReturnsCorrectJWTAndUser", false);
        TESTS_PASSED.put("login_NonExistentUserInputValidJWT_Returns401AndNoJWT", false);

        TESTS_PASSED.put("logout_NoJWT_Returns401", false);
        TESTS_PASSED.put("logout_InvalidJWT_Returns401", false);
        TESTS_PASSED.put("logout_NonExistentUserJWT_Returns401", false);
        TESTS_PASSED.put("logout_ValidJWT_ReturnsNoJWTAnd200", false);
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
        Assumptions.assumeTrue(TestResultsHelper.didAllPreControllerTestsPass());
    }
//---------------------------------------------------------------------------------------------------------------------------------
//register

    @Test
    @Order(1)
    @DisplayName("Tests whether POST request to /api/auth/register with structurally invalid data did not create new user and it returned 400")
    void register_InvalidInput_NotCreatedUserReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        final List<UserJPA> usersBefore = getAllUsers();
        List<UserJPA> usersAfter;
        final List<Resource> imagesBefore = getAllProfileImages();
        List<Resource> imagesAfter;

        //get a valid JWT cookie 
        final Optional<UserJPA> user = userRepo.findByUsername("admin");
        final HttpCookie cookie = testUtil.constructJWTCookie(user.get().getUsername());

        int i = 0;
        try {
            for (Pair<UserRegisterDTO, MockMultipartFile> pair : getInvalidRegisterFormsForResponse400()) {
                //1. request without a cookie
                request = constructRequest(pair.getL(), pair.getR(), null);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();

                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //2. request with a valid cookie
                request = constructRequest(pair.getL(), pair.getR(), cookie.toString());
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();

                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("register_InvalidInput_NotCreatedUserReturns400", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/auth/register with data of already existing user did not create new user and it returned 400")
    void register_DuplicateInput_NotCreatedUserReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        final List<UserJPA> usersBefore = getAllUsers();
        List<UserJPA> usersAfter;
        final List<Resource> imagesBefore = getAllProfileImages();
        List<Resource> imagesAfter;

        final Optional<UserJPA> user = userRepo.findByUsername("admin");
        final HttpCookie cookie = testUtil.constructJWTCookie(user.get().getUsername());

        int i = 0;
        try {
            for (Pair<UserRegisterDTO, MockMultipartFile> pair : getDuplicateDataRegisterFormsForResponse400()) {
                //1. request without a cookie
                request = constructRequest(pair.getL(), pair.getR(), null);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();

                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //2. request with a cookie
                request = constructRequest(pair.getL(), pair.getR(), cookie.toString());
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();

                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("register_DuplicateInput_NotCreatedUserReturns400", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/auth/register with user data that has non-existent dependency objects did not create new user and it returned 404")
    void register_NonExistentDependencyObjects_NotCreatedUserReturns404() {
        HttpEntity request;
        ResponseEntity<String> response;
        final List<UserJPA> usersBefore = getAllUsers();
        List<UserJPA> usersAfter;
        final List<Resource> imagesBefore = getAllProfileImages();
        List<Resource> imagesAfter;

        final Optional<UserJPA> user = userRepo.findByUsername("admin");
        final HttpCookie cookie = testUtil.constructJWTCookie(user.get().getUsername());

        int i = 0;
        try {
            for (Pair<UserRegisterDTO, MockMultipartFile> pair : getNonExistentRelationshipDataRegisterFormsForResponse404()) {
                //1. request without a cookie
                request = constructRequest(pair.getL(), pair.getR(), null);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();

                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //2. request with a cookie
                request = constructRequest(pair.getL(), pair.getR(), cookie.toString());
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();

                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("register_NonExistentDependencyObjects_NotCreatedUserReturns404", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/auth/register with valid user data did create new user and it returned 200")
    void register_ValidInput_CreatedUserReturns200() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<UserJPA> usersBefore;
        List<UserJPA> usersAfter;
        List<Resource> imagesBefore;
        List<Resource> imagesAfter;
        Optional<UserJPA> userDB;
        Resource imageDB;

        int i = 0;
        try {
            for (Pair<UserRegisterDTO, MockMultipartFile> pair : getValidRegisterFormsForResponse200()) {
                usersBefore = getAllUsers();
                imagesBefore = getAllProfileImages();

                request = constructRequest(pair.getL(), pair.getR(), null);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                usersAfter = getAllUsers();
                userDB = userRepo.findByUsername(pair.getL().getUsername());
                assertThat(userDB).isNotNull();
                assertThat(userDB.isPresent()).isTrue();
                assertThat(usersAfter.size()).isEqualTo(usersBefore.size() + 1);
                assertThat(usersAfter.contains(userDB.get())).isTrue();

                assertUsersEqual(userDB.get(), pair.getL());
                usersAfter.remove(userDB.get());
                assertUsersEqual(usersAfter, usersBefore);

                if (pair.getR() != null) {
                    imageDB = fileRepo.getUserProfileImage(userDB.get().getProfileImage());
                    assertThat(imageDB).isNotNull();
                    assertThat(imageDB.exists()).isTrue();
                    assertThat(imageDB.isReadable()).isTrue();
                    assertThat(imageDB.getContentAsByteArray()).isEqualTo(pair.getR().getBytes());
                } else {
                    imagesAfter = getAllProfileImages();
                    assertImagesEqual(imagesAfter, imagesBefore);
                }

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("register_ValidInput_CreatedUserReturns200", true);
    }
//---------------------------------------------------------------------------------------------------------------------------------
//login

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to POST /api/auth/login with structurally invalid data did not login user and it returned 400 with no Cookie")
    void login_InvalidInput_NotLoggedInReturns400() {
        HttpEntity request;
        ResponseEntity<String> response;
        final List<UserJPA> usersBefore = getAllUsers();
        List<UserJPA> usersAfter;
        final List<Resource> imagesBefore = getAllProfileImages();
        List<Resource> imagesAfter;

        final Optional<UserJPA> user = userRepo.findByUsername("admin");
        final HttpCookie cookie = testUtil.constructJWTCookie(user.get().getUsername());

        int i = 0;
        try {
            for (UserLoginDTO lForm : getStructurallyInvalidLoginFormsForResponse400()) {
                //1. request without a cookie
                request = constructRequest(lForm, null);
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                // Check received cookies
                assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).isNull();

                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();
                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //2. request with a cookie
                request = constructRequest(lForm, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                // Check received cookies
                assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).isNull();

                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();
                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("login_InvalidInput_NotLoggedInReturns400", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests whether POST request to POST /api/auth/login with non-existent user did not login user and it returned 401 with no Cookie")
    void login_NonExistentUser_NotLoggedInReturns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        final List<UserJPA> usersBefore = getAllUsers();
        List<UserJPA> usersAfter;
        final List<Resource> imagesBefore = getAllProfileImages();
        List<Resource> imagesAfter;

        final Optional<UserJPA> user = userRepo.findByUsername("admin");
        final HttpCookie cookie = testUtil.constructJWTCookie(user.get().getUsername());

        int i = 0;
        try {
            for (UserLoginDTO input : getNonExistentUserLoginForms()) {
                //1. request without a cookie
                request = constructRequest(input, null);
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                // Check received cookies
                assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).isNull();

                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();
                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                //2. request with a cookie
                request = constructRequest(input, cookie.toString());
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                // Check received cookies
                assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).isNull();

                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();
                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("login_NonExistentUser_NotLoggedInReturns401", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests whether POST request to /api/auth/login with valid user data did login user by returning user data, JWT and 200")
    void login_ValidInput_ReturnsUserJWTAnd200() {
        HttpEntity request;
        ResponseEntity<String> response;
        final List<UserJPA> usersBefore = getAllUsers();
        List<UserJPA> usersAfter;
        final List<Resource> imagesBefore = getAllProfileImages();
        List<Resource> imagesAfter;

        int i = 0;
        try {
            for (Pair<UserLoginDTO, String> pair : getValidUserLoginFormsAndResponses()) {
                request = constructRequest(pair.getL(), null);
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                //check user data
                assertThat(response.getBody()).isEqualTo(pair.getR());
                // Check cookies
                List<String> cookieStrings = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                assertThat(cookieStrings).isNotNull();
                assertThat(cookieStrings.size()).isEqualTo(1);
                List<HttpCookie> cookies = HttpCookie.parse(cookieStrings.get(0));
                assertThat(cookies).isNotNull();
                assertThat(cookies.size()).isEqualTo(1);
                HttpCookie cookie = cookies.get(0);

                assertThat(cookie).isNotNull();
                assertThat(cookie.getName()).isEqualTo(config.getJwtCookieName());
                assertThat(cookie.getPath()).isEqualTo("/api");
                assertThat(cookie.isHttpOnly()).isTrue();
                assertThat(cookie.getMaxAge()).isEqualTo(24 * 60 * 60);
                assertThat(jwtUtils.getUserNameFromJwtToken(cookie.getValue())).isEqualTo(pair.getL().getUsername());

                usersAfter = getAllUsers();
                imagesAfter = getAllProfileImages();
                assertUsersEqual(usersAfter, usersBefore);
                assertImagesEqual(imagesAfter, imagesBefore);

                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("login_ValidInput_ReturnsUserJWTAnd200", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether POST request to /api/auth/login with valid user and a valid JWT of a different valid user, returns 200 and response of the initial user with his JWT")
    void login_SuccessiveValidInput_ReturnsCorrectJWTAndUser() {
        Assumptions.assumeTrue(TESTS_PASSED.get("login_ValidInput_ReturnsUserJWTAnd200"));

        HttpEntity request;
        ResponseEntity<String> response;
        final Pair<UserLoginDTO, String>[] pairs1 = getValidUserLoginFormsAndResponses();
        final Pair<UserLoginDTO, String>[] pairs2 = getValidUserLoginFormsAndResponses();
        int i = 0;
        int j = 0;
        try {
            for (Pair<UserLoginDTO, String> pair1 : pairs1) {
                request = constructRequest(pair1.getL(), null);
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                String cookieString1 = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
                assertThat(cookieString1).isNotNull();

                for (Pair<UserLoginDTO, String> pair2 : pairs2) {
                    if (!pair1.getL().getUsername().equals(pair2.getL().getUsername())) {
                        request = constructRequest(pair2.getL(), cookieString1);
                        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                        String cookieString2 = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
                        assertThat(cookieString2).isNotNull();
                        assertThat(cookieString2).isNotEqualTo(cookieString1);
                        HttpCookie cookie2 = HttpCookie.parse(cookieString2).get(0);
                        assertThat(cookie2).isNotNull();
                        assertThat(jwtUtils.getUserNameFromJwtToken(cookie2.getValue())).isEqualTo(pair2.getL().getUsername());
                        assertThat(response.getBody()).isEqualTo(pair2.getR());
                    }
                    j++;
                }
                i++;
            }
        } catch (Throwable t) {
            fail("Assertion failed at index (i,j)=(" + i + "," + j + ")", t);
        }

        TESTS_PASSED.put("login_SuccessiveValidInput_ReturnsCorrectJWTAndUser", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether POST request to /api/auth/login with non-existent user and valid JWT returns 401 and no JWT")
    void login_NonExistentUserInputValidJWT_Returns401AndNoJWT() {
        Assumptions.assumeTrue(TESTS_PASSED.get("login_ValidInput_ReturnsUserJWTAnd200"));

        HttpEntity request;
        ResponseEntity<String> response;
        final Pair<UserLoginDTO, String>[] pairs1 = getValidUserLoginFormsAndResponses();
        final UserLoginDTO[] users2 = getNonExistentUserLoginForms();
        int i = 0;
        int j = 0;
        try {
            for (Pair<UserLoginDTO, String> pair1 : pairs1) {
                request = constructRequest(pair1.getL(), null);
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                String cookieString1 = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
                assertThat(cookieString1).isNotNull();

                for (UserLoginDTO user : users2) {
                    request = constructRequest(user, cookieString1);
                    response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    List<String> actualResponseCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                    assertThat(actualResponseCookies).isNull();
                    j++;
                }
                i++;
            }
        } catch (Throwable t) {
            fail("Assertion failed at index (i,j)=(" + i + "," + j + ")", t);
        }

        TESTS_PASSED.put("login_NonExistentUserInputValidJWT_Returns401AndNoJWT", true);
    }
//---------------------------------------------------------------------------------------------------------------------------------
//logout

    @Test
    @Order(10)
    @DisplayName("Tests whether POST request to /api/auth/logout without a JWT returned 401 and no cookie")
    void logout_NoJWT_Returns401() {
        HttpEntity request;
        ResponseEntity<String> response;
        List<String> cookies;
        int i = 0;
        try {
            for (i = 0; i < 100; i++) {
                request = constructRequest(null);
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                assertThat(cookies).isNull();
            }
        } catch (Throwable t) {
            fail("Assertion failed at index " + i, t);
        }

        TESTS_PASSED.put("logout_NoJWT_Returns401", true);
    }

    @Test
    @Order(11)
    @DisplayName("Tests whether POST request to /api/auth/logout with an invalid JWT returned 401 and no cookie")
    void logout_InvalidJWT_Returns401() {
        Assumptions.assumeTrue(TESTS_PASSED.get("login_ValidInput_ReturnsUserJWTAnd200"));

        HttpEntity request;
        ResponseEntity<String> response;
        List<String> cookies;
        int i = 0;
        try {
            for (HttpCookie cookie : getInvalidCookies()) {
                request = constructRequest(cookie.toString());
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                assertThat(cookies).isNull();
                i++;
            }
            i = 0;
            for (String cookie : getInvalidCookieStrings()) {
                request = constructRequest(cookie);
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                assertThat(cookies).isNull();
                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("logout_InvalidJWT_Returns401", true);
    }

    @Test
    @Order(12)
    @DisplayName("Tests whether POST request to /api/auth/logout with a JWT of an non-existent user returned 401 and no cookie")
    void logout_NonExistentUserJWT_Returns401() {
        Assumptions.assumeTrue(TESTS_PASSED.get("login_ValidInput_ReturnsUserJWTAnd200"));

        HttpEntity request;
        ResponseEntity<String> response;
        List<String> cookies;
        int i = 0;
        try {
            for (String cookie : getNonExistentUserValidJWTs()) {
                request = constructRequest(cookie);
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                assertThat(cookies).isNull();
                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("logout_NonExistentUserJWT_Returns401", true);
    }

    @Test
    @Order(13)
    @DisplayName("Tests whether POST request to /api/auth/logout with a JWT of an existing user returned 200 and an empty JWT cookie")
    void logout_ValidJWT_ReturnsNoJWTAnd200() {
        Assumptions.assumeTrue(TESTS_PASSED.get("login_ValidInput_ReturnsUserJWTAnd200"));

        HttpEntity request;
        ResponseEntity<String> response;
        int i = 0;
        try {
            for (String cookie : getValidCookies()) {
                request = constructRequest(cookie);
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, request, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                // Check cookies
                MultiValueMap<String, String> responseHeaders = response.getHeaders();
                List<String> setCookieHeader = responseHeaders.get(HttpHeaders.SET_COOKIE);
                assertThat(setCookieHeader).isNotNull();
                assertThat(setCookieHeader.size()).isEqualTo(1);
                HttpCookie responseCookie = HttpCookie.parse(setCookieHeader.get(0)).get(0);
                assertThat(responseCookie).isNotNull();
                assertThat(responseCookie.getName()).isEqualTo(config.getJwtCookieName());
                assertThat(responseCookie.getValue()).isEmpty();
                assertThat(responseCookie.getPath()).isNotNull().isEqualTo("/api");

                assertThat(response.getBody()).isEqualTo("{\"message\":\"You've been logged out!\"}");
                i++;
            }
        } catch (Throwable e) {
            fail("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("logout_ValidJWT_ReturnsNoJWTAnd200", true);
    }

//=================================================================================================================================
//PRIVATE METHODS
//=================================================================================================================================
    private HttpEntity<MultiValueMap<String, Object>> constructRequest(UserRegisterDTO registerForm, MockMultipartFile profileImage, String cookie) throws AssertionError {
        HttpHeaders requestHeader = new HttpHeaders();
        HttpHeaders userHeader = new HttpHeaders();
        HttpHeaders imageHeader = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpEntity<UserRegisterDTO> userBody;
        HttpEntity<Resource> imageBody;

        if (cookie != null) {
            requestHeader.set(HttpHeaders.COOKIE, cookie);
        }
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        userHeader.setContentType(MediaType.APPLICATION_JSON);
        userBody = new HttpEntity<>(registerForm, userHeader);
        body.add("user", userBody);

        if (profileImage != null) {
            try {
                imageHeader.setContentType(MediaType.valueOf(profileImage.getContentType()));
                imageBody = new HttpEntity<>(profileImage.getResource(), imageHeader);
                body.add("profile_image", imageBody);
            } catch (Exception ex) {
                fail("Failed to set profile_image part of multipart form data of HttpEntity request", ex);
            }
        }

        return new HttpEntity<>(body, requestHeader);

    }

    private HttpEntity<UserLoginDTO> constructRequest(UserLoginDTO loginForm, String cookie) {
        HttpHeaders requestHeaders = new HttpHeaders();
        if (cookie != null) {
            requestHeaders.set(HttpHeaders.COOKIE, cookie);
        }
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(loginForm, requestHeaders);
    }

    private HttpEntity constructRequest(String cookie) {
        if (cookie != null) {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set(HttpHeaders.COOKIE, cookie);
            return new HttpEntity<>(null, requestHeaders);
        }
        return new HttpEntity<>(null);
    }

    private List<UserJPA> getAllUsers() {
        return userRepo.findAll();
    }

    private List<Resource> getAllProfileImages() throws RuntimeException {
        return testUtil.getAllUserProfileImages();
    }

//---------------------------------------------------------------------------------------------------------------------------------
//assert methods
    private void assertUsersEqual(List<UserJPA> actual, List<UserJPA> expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.size()).isEqualTo(expected.size());
        int i = 0;
        try {
            for (i = 0; i < actual.size(); i++) {
                assertUsersEqual(actual.get(i), expected.get(i));
                i++;
            }
        } catch (AssertionError e) {
            fail("Error at i=" + i + " for:\nActual=" + actual.get(i) + "\nExpected=" + expected.get(i), e);
        }
    }

    private void assertUsersEqual(UserJPA actual, UserJPA expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
        assertThat(actual.getProfileName()).isEqualTo(expected.getProfileName());
        assertThat(actual.getProfileImage()).isEqualTo(expected.getProfileImage());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getCreatedAt()).isEqualTo(expected.getCreatedAt());
        assertThat(actual.getUpdatedAt()).isEqualTo(expected.getUpdatedAt());

        assertThat(actual.getCountry()).isNotNull();
        assertThat(expected.getCountry()).isNotNull();
        assertThat(actual.getCountry().getId()).isEqualTo(expected.getCountry().getId());
        assertThat(actual.getCountry().getName()).isEqualTo(expected.getCountry().getName());
        assertThat(actual.getCountry().getOfficialStateName()).isEqualTo(expected.getCountry().getOfficialStateName());
        assertThat(actual.getCountry().getCode()).isEqualTo(expected.getCountry().getCode());

        assertThat(actual.getMedias()).isNotNull();
        assertThat(expected.getMedias()).isNotNull();
        assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
        for (int i = 0; i < actual.getMedias().size(); i++) {
            assertThat(actual.getMedias().get(i)).isNotNull();
            assertThat(expected.getMedias().get(i)).isNotNull();
            assertThat(actual.getMedias().get(i).getId()).isEqualTo(expected.getMedias().get(i).getId());
        }

        assertThat(actual.getCritiques()).isNotNull();
        assertThat(expected.getCritiques()).isNotNull();
        assertThat(actual.getCritiques().size()).isEqualTo(expected.getCritiques().size());
        for (int i = 0; i < actual.getCritiques().size(); i++) {
            assertThat(actual.getCritiques().get(i)).isNotNull();
            assertThat(expected.getCritiques().get(i)).isNotNull();

            assertThat(actual.getCritiques().get(i).getUser()).isNotNull();
            assertThat(expected.getCritiques().get(i).getUser()).isNotNull();
            assertThat(actual.getCritiques().get(i).getMedia()).isNotNull();
            assertThat(expected.getCritiques().get(i).getMedia()).isNotNull();

            assertThat(actual.getCritiques().get(i).getId()).isNotNull().isEqualTo(expected.getCritiques().get(i).getId());
            assertThat(actual.getCritiques().get(i).getUser().getId()).isEqualTo(expected.getCritiques().get(i).getUser().getId());
            assertThat(actual.getCritiques().get(i).getUser().getId()).isEqualTo(actual.getId());
            assertThat(actual.getCritiques().get(i).getMedia().getId()).isEqualTo(expected.getCritiques().get(i).getMedia().getId());
            assertThat(actual.getCritiques().get(i).getDescription()).isEqualTo(expected.getCritiques().get(i).getDescription());
            assertThat(actual.getCritiques().get(i).getRating()).isEqualTo(expected.getCritiques().get(i).getRating());
            assertThat(actual.getCritiques().get(i).getCreatedAt()).isNotNull().isEqualTo(expected.getCritiques().get(i).getCreatedAt());
        }
        assertThat(actual.getComments()).isNotNull();
        assertThat(expected.getComments()).isNotNull();
        assertThat(actual.getComments().size()).isEqualTo(expected.getComments().size());
        for (int i = 0; i < actual.getComments().size(); i++) {
            assertThat(actual.getComments().get(i)).isNotNull();
            assertThat(expected.getComments().get(i)).isNotNull();

            assertThat(actual.getComments().get(i).getUser()).isNotNull();
            assertThat(expected.getComments().get(i).getUser()).isNotNull();
            assertThat(actual.getComments().get(i).getUser().getId()).isNotNull().isEqualTo(expected.getComments().get(i).getUser().getId());
            assertThat(actual.getComments().get(i).getUser().getId()).isNotNull().isEqualTo(actual.getId());

            assertThat(actual.getComments().get(i).getId()).isNotNull().isEqualTo(expected.getComments().get(i).getId());
            assertThat(actual.getComments().get(i).getContent()).isNotBlank().isEqualTo(expected.getComments().get(i).getContent());
            assertThat(actual.getComments().get(i).getCreatedAt()).isNotNull().isEqualTo(expected.getComments().get(i).getCreatedAt());
        }

        assertThat(actual.getCritiqueLikeDislikes()).isNotNull();
        assertThat(expected.getCritiqueLikeDislikes()).isNotNull();
        assertThat(actual.getCritiqueLikeDislikes().size()).isEqualTo(expected.getCritiqueLikeDislikes().size());
        for (int i = 0; i < actual.getCritiqueLikeDislikes().size(); i++) {
            assertThat(actual.getCritiqueLikeDislikes().get(i)).isNotNull();
            assertThat(expected.getCritiqueLikeDislikes().get(i)).isNotNull();

            assertThat(actual.getCritiqueLikeDislikes().get(i).getId()).isNotNull();
            assertThat(expected.getCritiqueLikeDislikes().get(i).getId()).isNotNull();
            assertThat(actual.getCritiqueLikeDislikes().get(i).getId().getUser()).isNotNull();
            assertThat(expected.getCritiqueLikeDislikes().get(i).getId().getUser()).isNotNull();
            assertThat(actual.getCritiqueLikeDislikes().get(i).getId().getUser().getId()).isNotNull().isEqualTo(expected.getCritiqueLikeDislikes().get(i).getId().getUser().getId());
            assertThat(actual.getCritiqueLikeDislikes().get(i).getId().getUser().getId()).isNotNull().isEqualTo(actual.getId());

            assertThat(actual.getCritiqueLikeDislikes().get(i).getId().getCritique()).isNotNull();
            assertThat(expected.getCritiqueLikeDislikes().get(i).getId().getCritique()).isNotNull();
            assertThat(actual.getCritiqueLikeDislikes().get(i).getId().getCritique().getId()).isNotNull().isEqualTo(expected.getCritiqueLikeDislikes().get(i).getId().getCritique().getId());

            assertThat(actual.getCritiqueLikeDislikes().get(i).getIsLike()).isNotNull().isEqualTo(expected.getCritiqueLikeDislikes().get(i).getIsLike());
        }
        
        assertThat(actual.getCommentsLikeDislikes()).isNotNull();
        assertThat(expected.getCommentsLikeDislikes()).isNotNull();
        assertThat(actual.getCommentsLikeDislikes().size()).isEqualTo(expected.getCommentsLikeDislikes().size());
        for (int i = 0; i < actual.getCommentsLikeDislikes().size(); i++) {
            assertThat(actual.getCommentsLikeDislikes().get(i)).isNotNull();
            assertThat(expected.getCommentsLikeDislikes().get(i)).isNotNull();

            assertThat(actual.getCommentsLikeDislikes().get(i).getId()).isNotNull();
            assertThat(expected.getCommentsLikeDislikes().get(i).getId()).isNotNull();
            assertThat(actual.getCommentsLikeDislikes().get(i).getId().getUser()).isNotNull();
            assertThat(expected.getCommentsLikeDislikes().get(i).getId().getUser()).isNotNull();
            assertThat(actual.getCommentsLikeDislikes().get(i).getId().getUser().getId()).isNotNull().isEqualTo(expected.getCommentsLikeDislikes().get(i).getId().getUser().getId());
            assertThat(actual.getCommentsLikeDislikes().get(i).getId().getUser().getId()).isNotNull().isEqualTo(actual.getId());

            assertThat(actual.getCommentsLikeDislikes().get(i).getId().getComment()).isNotNull();
            assertThat(expected.getCommentsLikeDislikes().get(i).getId().getComment()).isNotNull();
            assertThat(actual.getCommentsLikeDislikes().get(i).getId().getComment().getId()).isNotNull().isEqualTo(expected.getCommentsLikeDislikes().get(i).getId().getComment().getId());

            assertThat(actual.getCommentsLikeDislikes().get(i).getIsLike()).isNotNull().isEqualTo(expected.getCommentsLikeDislikes().get(i).getIsLike());
        }

    }

    private void assertUsersEqual(UserJPA actual, UserRegisterDTO expected) throws AssertionError {
        assertThat(actual).isNotNull();
        assertThat(expected).isNotNull();
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
        assertThat(actual.getProfileName()).isEqualTo(expected.getProfileName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(pswEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();

        assertThat(actual.getCountry()).isNotNull();
        assertThat(expected.getCountryId()).isNotNull();
        assertThat(actual.getCountry().getId()).isEqualTo(expected.getCountryId());

        assertThat(actual.getMedias()).isNotNull().isEmpty();
        assertThat(actual.getCritiques()).isNotNull().isEmpty();
    }

    private void assertImagesEqual(List<Resource> actual, List<Resource> expected) throws AssertionError {
        TestUtil.assertResourcesEqual(actual, expected);
    }

//---------------------------------------------------------------------------------------------------------------------------------
//register
    private UserRegisterDTO getRandomValidUserRegisterDTO() {
        Gender[] genders = Gender.values();
        UserRole[] roles = {UserRole.REGULAR, UserRole.CRITIC};
        String emailDomain = TestUtil.getRandomValidEmailDomain();

        UserRegisterDTO register = new UserRegisterDTO();
        register.setFirstName(TestUtil.getRandomString(TestUtil.getRandomInt(1, 100)));
        register.setLastName(TestUtil.getRandomString(TestUtil.getRandomInt(1, 100)));
        register.setProfileName(TestUtil.getRandomString(TestUtil.getRandomInt(1, 100)));
        register.setEmail(TestUtil.getRandomString(TestUtil.getRandomInt(1, 64)) + "@" + emailDomain);
        register.setUsername(TestUtil.getRandomString(TestUtil.getRandomInt(1, 300)));
        register.setPassword(TestUtil.getRandomString(TestUtil.getRandomInt(1, 300)));
        register.setGender(TestUtil.getRandomEnum(genders));
        register.setRole(TestUtil.getRandomEnum(roles));
        register.setCountryId((long) TestUtil.getRandomInt(1, 249));
        return register;
    }

    private List<Pair<UserRegisterDTO, MockMultipartFile>> getInvalidRegisterFormsForResponse400() {
        final String validEmailDomain = TestUtil.getRandomValidEmailDomain();
        final String[] invalidFirstNames = {null, "", " ", "        ", TestUtil.getRandomString(101)};
        final String[] invalidLastNames = {null, "", " ", "        ", TestUtil.getRandomString(101)};
        final Gender[] invalidGenders = {null};
        final UserRole[] invalidUserRoles = {null, UserRole.ADMINISTRATOR};
        final String[] invalidProfileNames = {null, "", " ", "           ", TestUtil.getRandomString(101), "marko  makro", " marko ",
            "......marko....", "marko&^$makro", "marko.exe", "marko!", "marko_markovic?", "/marko/", "mar\tko", "mar\nko"};
        final MockMultipartFile[] invalidProfileImages = TestUtil.getInvalidImages();
        final String[] invalidEmails = {null, "", " ", "           ",
            TestUtil.getRandomString(301), TestUtil.getRandomString((300 - (validEmailDomain.length() + 1)) + 1) + "@" + validEmailDomain,
            "badIn..put@@" + TestUtil.getRandomValidEmailDomain(), "badInput@gmail@.com", "badInput" + TestUtil.getRandomValidEmailDomain()
        };
        final String[] invalidUsernames = {null, "", " ", "           ", TestUtil.getRandomString(301)};
        final String[] invalidPasswords = {null, "", " ", "        ", TestUtil.getRandomString(301)};
        final Long[] invalidCountryIds = {null, 0l, -1l, -10l, -100l};

        final List<Pair<UserRegisterDTO, MockMultipartFile>> pairs = new LinkedList<>();
        UserRegisterDTO user;
        MockMultipartFile image;
        for (String pom : invalidFirstNames) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setFirstName(pom);
            pairs.add(new Pair(user, image));
        }
        for (String pom : invalidLastNames) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setLastName(pom);
            pairs.add(new Pair(user, image));
        }
        for (Gender pom : invalidGenders) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setGender(pom);
            pairs.add(new Pair(user, image));
        }
        for (UserRole pom : invalidUserRoles) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setRole(pom);
            pairs.add(new Pair(user, image));
        }
        for (String pom : invalidProfileNames) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setProfileName(pom);
            pairs.add(new Pair(user, image));
        }
        for (MockMultipartFile pom : invalidProfileImages) {
            user = getRandomValidUserRegisterDTO();
            pairs.add(new Pair(user, pom));
        }
        for (String pom : invalidEmails) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setEmail(pom);
            pairs.add(new Pair(user, image));
        }
        for (String pom : invalidUsernames) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setUsername(pom);
            pairs.add(new Pair(user, image));
        }
        for (String pom : invalidPasswords) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setPassword(pom);
            pairs.add(new Pair(user, image));
        }
        for (Long pom : invalidCountryIds) {
            user = getRandomValidUserRegisterDTO();
            image = TestUtil.getRandomValidImage();
            user.setCountryId(pom);
            pairs.add(new Pair(user, image));
        }
        return pairs;
    }

    private List<Pair<UserRegisterDTO, MockMultipartFile>> getDuplicateDataRegisterFormsForResponse400() {
        final List<UserJPA> users = getAllUsers();
        final List<Pair<UserRegisterDTO, MockMultipartFile>> pairs = new ArrayList<>(users.size() * 3);
        UserRegisterDTO rf;
        for (UserJPA user : users) {
            rf = getRandomValidUserRegisterDTO();
            rf.setProfileName(user.getProfileName());
            pairs.add(new Pair(rf, TestUtil.getRandomValidImage()));

            rf = getRandomValidUserRegisterDTO();
            rf.setEmail(user.getEmail());
            pairs.add(new Pair(rf, TestUtil.getRandomValidImage()));

            rf = getRandomValidUserRegisterDTO();
            rf.setUsername(user.getUsername());
            pairs.add(new Pair(rf, TestUtil.getRandomValidImage()));
        }
        return pairs;
    }

    private List<Pair<UserRegisterDTO, MockMultipartFile>> getNonExistentRelationshipDataRegisterFormsForResponse404() {
        Long[] nonExistentCountryIds = new Long[]{400l, 401l, 402l, 499l, 1000l, 301l, 300l};
        List<Pair<UserRegisterDTO, MockMultipartFile>> pairs = new LinkedList<>();
        UserRegisterDTO registerForm;
        for (Long countryId : nonExistentCountryIds) {
            registerForm = getRandomValidUserRegisterDTO();
            registerForm.setCountryId(countryId);
            pairs.add(new Pair(registerForm, TestUtil.getRandomValidImage()));
        }
        return pairs;
    }

    private Pair<UserRegisterDTO, MockMultipartFile>[] getValidRegisterFormsForResponse200() {
        UserRole[] roles = {UserRole.REGULAR, UserRole.CRITIC};
        return new Pair[]{
            new Pair<>(
            new UserRegisterDTO("Marko", "Markovic", Gender.MALE, "marko_markov", null, "marko", "marko@gmail.com", "123456", TestUtil.getRandomEnum(roles), 1l),
            new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", TestUtil.getBytes(TestUtil.getRandomString(TestUtil.getRandomInt(10, 100))))),
            new Pair<>(
            new UserRegisterDTO("Marko", "Markovic", Gender.MALE, "marko_markov2", null, "marko2", "marko2@gmail.com", "123456", TestUtil.getRandomEnum(roles), 190l),
            null),
            new Pair<>(
            new UserRegisterDTO("Teodora", "Teodorovic", Gender.FEMALE, "tea123", null, "tea123", "tea123@gmail.com", "ttodora", TestUtil.getRandomEnum(roles), 190l),
            new MockMultipartFile("profile_image", "test.png", "image/png", TestUtil.getBytes(TestUtil.getRandomString(TestUtil.getRandomInt(10, 100))))),
            new Pair<>(
            new UserRegisterDTO("aaaaa", "aaaaa", Gender.OTHER, "aaaaa", null, "aaaaa", "aaaaa@gmail.com", "aaa11aaaaa", TestUtil.getRandomEnum(roles), 1l),
            new MockMultipartFile("profile_image", "test.png", "image/png", TestUtil.getBytes(TestUtil.getRandomString(TestUtil.getRandomInt(10, 100))))),
            new Pair<>(
            new UserRegisterDTO("Marko3", "Markovic3", Gender.OTHER, "marko2_markiz3", null, "marko3", "marko3@gmail.com", "mmarko123", TestUtil.getRandomEnum(roles), 1l),
            new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", TestUtil.getBytes(TestUtil.getRandomString(TestUtil.getRandomInt(10, 100))))),
            new Pair<>(
            new UserRegisterDTO("Teodora", "Teodorovic", Gender.FEMALE, "teadora123", null, "tea321", "tea321@gmail.com", "ttodora", TestUtil.getRandomEnum(roles), 190l),
            null),
            new Pair<>(
            new UserRegisterDTO("Teodora", "Teodorovic", Gender.FEMALE, "teadora1234", null, "tea3214", "tea321@yahoo.com", "ttodora", TestUtil.getRandomEnum(roles), 185l),
            null)

        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//login
    private UserLoginDTO getRandomStructurallyValidUserLoginDTO() {
        UserLoginDTO login = new UserLoginDTO();
        login.setUsername(TestUtil.getRandomString(TestUtil.getRandomInt(1, 300)));
        login.setPassword(TestUtil.getRandomString(TestUtil.getRandomInt(1, 300)));
        return login;
    }

    private List<UserLoginDTO> getStructurallyInvalidLoginFormsForResponse400() {
        final String[] invalidUsernames = {null, "", " ", "           ", TestUtil.getRandomString(301)};
        final String[] invalidPasswords = {null, "", " ", "        ", TestUtil.getRandomString(301)};

        List<UserLoginDTO> forms = new LinkedList<>();
        UserLoginDTO user;

        for (String pom : invalidUsernames) {
            user = getRandomStructurallyValidUserLoginDTO();
            user.setUsername(pom);
            forms.add(user);
        }
        for (String pom : invalidPasswords) {
            user = getRandomStructurallyValidUserLoginDTO();
            user.setPassword(pom);
            forms.add(user);
        }

        return forms;
    }

    private UserLoginDTO[] getNonExistentUserLoginForms() {
        return new UserLoginDTO[]{
            new UserLoginDTO("admina", "admina"),
            new UserLoginDTO("admin", "admina"),
            new UserLoginDTO("admina", "admin"),
            new UserLoginDTO("admin", "admina"),
            new UserLoginDTO("aaaaa", "aaaaa"),
            new UserLoginDTO("dummyUser101", "dummyPassword101"),
            new UserLoginDTO("dummyUser102", "dummyUser102"),
            new UserLoginDTO("DummyUsername", "DummyPassword"),
            new UserLoginDTO("DummyUsername2", "DummyPassword2"),
            new UserLoginDTO(TestUtil.getRandomString(20), TestUtil.getRandomString(12))
        };
    }

    private Pair<UserLoginDTO, String>[] getValidUserLoginFormsAndResponses() {
        return new Pair[]{
            new Pair<>(new UserLoginDTO("admin", "admin"), jsonReader.getUserJson(1)),
            new Pair<>(new UserLoginDTO("regular", "regular"), jsonReader.getUserJson(2)),
            new Pair<>(new UserLoginDTO("critic", "critic"), jsonReader.getUserJson(3))
        };

    }
//---------------------------------------------------------------------------------------------------------------------------------
//logout

    private String[] getNonExistentUserValidJWTs() {
        final String[] usernames = {
            "admina",
            "aaaaaa",
            "dummyUser101",
            "admina",
            "dummyUser102",
            "DummyUsername",
            "DummyUsername2",
            TestUtil.getRandomString(20)
        };
        final SecurityUser user = new SecurityUser();
        final String[] result = new String[usernames.length];
        for (int i = 0; i < usernames.length; i++) {
            user.setUsername(usernames[i]);
            result[i] = jwtUtils.generateJwtCookie(user).toString();
        }
        return result;
    }

    private List<HttpCookie> getInvalidCookies() {
        HttpEntity request;
        ResponseEntity<String> response;
        final Pair<UserLoginDTO, String>[] pairs = getValidUserLoginFormsAndResponses();
        final List<HttpCookie> validCookies = new ArrayList<>(pairs.length);
        final List<HttpCookie> invalidCookies = new LinkedList<>();

        for (Pair<UserLoginDTO, String> pair : pairs) {
            request = constructRequest(pair.getL(), null);
            response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
            validCookies.add(HttpCookie.parse(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).get(0));
        }
        final String[] invalidJWTs = {
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ",
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQSflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQSflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ==.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

        };
        HttpCookie invalidCookie;
        for (HttpCookie cookie : validCookies) {
            invalidCookie = (HttpCookie) cookie.clone();
            invalidCookie.setValue("a" + invalidCookie.getValue().substring(1));
            invalidCookies.add(invalidCookie);

            invalidCookie = (HttpCookie) cookie.clone();
            invalidCookie.setValue("dummyfirstvalue.dummytsecond.thirdoptionwhatever");
            invalidCookies.add(invalidCookie);

            invalidCookie = (HttpCookie) cookie.clone();
            invalidCookie.setValue(TestUtil.getRandomString(100));
            invalidCookies.add(invalidCookie);

            invalidCookie = (HttpCookie) cookie.clone();
            invalidCookie.setValue("");
            invalidCookies.add(invalidCookie);

            for (String invalidJWT : invalidJWTs) {
                invalidCookie = (HttpCookie) cookie.clone();
                invalidCookie.setValue(invalidJWT);
                invalidCookies.add(invalidCookie);
            }
        }
        invalidCookies.add(new HttpCookie("token", TestUtil.getRandomString(100)));
        invalidCookies.add(new HttpCookie(TestUtil.getRandomString(10), TestUtil.getRandomString(100)));
        return invalidCookies;
    }

    private String[] getInvalidCookieStrings() {
        return new String[]{
            "",
            "username=",
            "user@name=value",
            "user name=value",
            "usernamevalue",
            "username==value",
            "username=\\u0000value",
            "username=valué",
            "cwt=; Path=/api; Max-Age=86400; HttpOnly",
            "cwt=invalidToken; Path=/api; Max-Age=86400; HttpOnly",
            "cwt=yourJwtToken; Path=/api; Max-Age=0; HttpOnly",
            "cwt=yourJwtToken; Path=/api; Max-Age=3153600000; HttpOnly",
            "cwt=yourJwtToken; Path=/wrongPath; Max-Age=86400; HttpOnly",
            "cwt=yourJwtToken; Path=/api; Max-Age=86400",
            "cwt=yourJwtToken; Path=/api; Max-Age=86400; HttpOnly; Secure; SameSite=Lax",
            "wrongName=yourJwtToken; Path=/api; Max-Age=86400; HttpOnly",
            "cwt=yourJwtToken$%^&*(); Path=/api; Max-Age=86400; HttpOnly",
            "cwt=your Jwt Token; Path=/api; Max-Age=86400; HttpOnly",
            "LWSSO_COOKIE_KEY=123-LWSSO_COOKIE_KEY-VALUE-456; Path=/; Domain=localhost; Max-Age=PT168H; Expires=Tue, 14 Jun 2022 22:35:02 GMT; Secure; HttpOnly"

        };
    }

    private String[] getValidCookies() {
        HttpEntity request;
        ResponseEntity<String> response;
        final Pair<UserLoginDTO, String>[] pairs = getValidUserLoginFormsAndResponses();
        final String[] result = new String[pairs.length];
        int i = 0;
        for (Pair<UserLoginDTO, String> pair : pairs) {
            request = constructRequest(pair.getL(), null);
            response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
            result[i] = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
            i++;
        }
        return result;
    }

}
