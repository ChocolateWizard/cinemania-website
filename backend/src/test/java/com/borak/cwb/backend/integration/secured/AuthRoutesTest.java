/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.secured;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.classes.MyImage;
import com.borak.cwb.backend.domain.dto.user.UserLoginDTO;
import com.borak.cwb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.cwb.backend.domain.security.SecurityUser;
import com.borak.cwb.backend.helpers.TestJsonResponseReader;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.logic.security.JwtUtils;
import com.borak.cwb.backend.repository.jdbc.UserRepositoryJDBC;
import com.borak.cwb.backend.repository.util.FileRepository;
import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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
import org.springframework.core.io.ByteArrayResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(6)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepositoryJDBC userRepo;
    @Autowired
    private FileRepository fileRepo;
    @Autowired
    private PasswordEncoder pswEncoder;
    @Autowired
    private ConfigProperties config;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/auth";

    static {
        testsPassed.put("register_InvalidInput_NotCreatedUserReturns400", false);
        testsPassed.put("register_DuplicateInput_NotCreatedUserReturns400", false);
        testsPassed.put("register_NonExistentDependencyObjects_NotCreatedUserReturns404", false);
        testsPassed.put("register_ValidInput_CreatedUserReturns200", false);

        testsPassed.put("login_InvalidInput_NotLoggedInReturns400", false);
        testsPassed.put("login_NonExistentUser_NotLoggedInReturns401", false);
        testsPassed.put("login_ValidInput_ReturnsUserJWTAnd200", false);
        testsPassed.put("login_SuccessiveValidInput_ReturnsCorrectJWTAndUser", false);
        testsPassed.put("login_NonExistentUserInputValidJWT_Returns401AndNoJWT", false);

        testsPassed.put("logout_NoJWT_Returns401", false);
        testsPassed.put("logout_InvalidJWT_Returns401", false);
        testsPassed.put("logout_NonExistentUserJWT_Returns401", false);
        testsPassed.put("logout_ValidJWT_ReturnsNoJWTAnd200", false);
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
        Assumptions.assumeTrue(TestResultsHelper.didAllPreControllerTestsPass());
    }

    @Test
    @Order(1)
    @DisplayName("Tests whether POST request to /api/auth/register with structurally invalid data did not create new user and it returned 400")
    void register_InvalidInput_NotCreatedUserReturns400() {
        ResponseEntity<String> response;
        boolean doesUserExist;
        Optional<UserJDBC> userDB;
        Resource userProfileImage;
        Random random = new Random();
        int i = 0;
        //bad requests
        List<SimpleEntry<UserRegisterDTO, MockMultipartFile>> badUserInputs = new ArrayList<>() {
            {
                //--------------------------------------------------------------------------------------
                //invalid first name
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                null, "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                " ", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "        ", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                getRandomString(101, random), "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-------------------------------------------------------------------------------------------
                //invalid last name
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", null, Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", " ", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "         ", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", getRandomString(101, random), Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-----------------------------------------------------------------------------------------------
                //invalid gender
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", null,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-----------------------------------------------------------------------------------------------
                //invalid profile name
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                null, null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                " ", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "           ", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                getRandomString(101, random), null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko  makro", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                " marko ", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "......marko....", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko&^$makro", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko.exe", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko!", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markovic?", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "/marko/", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "mar\tko", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "mar\nko", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-----------------------------------------------------------------------------------------------
                //invalid username
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, null, "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, " ", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "        ", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, getRandomString(301, random), "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-----------------------------------------------------------------------------------------------
                //invalid email
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", null, "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", " ", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "          ", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", getRandomString(301, random), "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", getRandomString(291, random) + "@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badIn..put@@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail@.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInputgmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-----------------------------------------------------------------------------------------------
                //invalid password
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", null, 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", " ", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "        ", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", getRandomString(301, random), 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //-----------------------------------------------------------------------------------------------
                //invalid country id
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", null),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 0l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", -1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", -10l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", -100l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
            }
        };
        UserRegisterDTO invalidUserRoleUser1 = new UserRegisterDTO(
                "Marko", "Markovic", Gender.MALE, "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l);
        UserRegisterDTO invalidUserRoleUser2 = new UserRegisterDTO(
                "Marko", "Markovic", Gender.MALE, "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l);
        invalidUserRoleUser1.setRole(null);
        invalidUserRoleUser2.setRole(UserRole.ADMINISTRATOR);
        badUserInputs.add(new SimpleEntry<>(invalidUserRoleUser1,
                new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
        badUserInputs.add(new SimpleEntry<>(invalidUserRoleUser2,
                new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
        try {
            for (SimpleEntry<UserRegisterDTO, MockMultipartFile> input : badUserInputs) {
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(input.getKey(), input.getValue()), String.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                if (input.getKey().getUsername() != null) {
                    doesUserExist = userRepo.existsUsername(input.getKey().getUsername());
                    userDB = userRepo.findByUsername(input.getKey().getUsername());
                    assertThat(doesUserExist).isFalse();
                    assertThat(userDB).isNotNull();
                    assertThat(userDB.isPresent()).isFalse();
                } else {
                    doesUserExist = userRepo.existsEmail(input.getKey().getEmail());
                    assertThat(doesUserExist).isFalse();
                }

                if (input.getValue() != null && input.getKey().getProfileName() != null) {
                    String[] nameAndExtension = MyImage.extractNameAndExtension(input.getValue().getOriginalFilename());
                    String filename = input.getKey().getProfileName() + "." + nameAndExtension[1];
                    if (isValidFilename(filename)) {
                        userProfileImage = fileRepo.getUserProfileImage(filename);

                        assertThat(userProfileImage.exists()).isFalse();
                        assertThat(userProfileImage.isReadable()).isFalse();
                    }
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }
        testsPassed.put("register_InvalidInput_NotCreatedUserReturns400", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/auth/register with data of already existing user did not create new user and it returned 400")
    void register_DuplicateInput_NotCreatedUserReturns400() {
        ResponseEntity<String> response;
        boolean doesUserExist;
        Optional<UserJDBC> userDB;
        Resource userProfileImage;
        int i = 0;

        List<SimpleEntry<UserRegisterDTO, MockMultipartFile>> duplicateUserInputs = new ArrayList<>() {
            {
                //username exists
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markov10", null, "admin", "marko10@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.png",
                                "image/png", "test image content".getBytes(StandardCharsets.UTF_8))));
                //email exists
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markov10", null, "marko10", "admin@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //profile name exists
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "Admin", null, "marko10", "marko10@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
            }
        };
        try {
            for (SimpleEntry<UserRegisterDTO, MockMultipartFile> input : duplicateUserInputs) {
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(input.getKey(), input.getValue()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                doesUserExist = userRepo.existsUsername(input.getKey().getUsername());
                userDB = userRepo.findByUsername(input.getKey().getUsername());

                if (input.getKey().getUsername().equals("admin")) {
                    assertThat(doesUserExist).isTrue();
                    assertThat(userDB).isNotNull();
                    assertThat(userDB.isPresent()).isTrue();

                    assertThat(userDB.get().getFirstName()).isNotEqualTo(input.getKey().getFirstName());
                    assertThat(userDB.get().getLastName()).isNotEqualTo(input.getKey().getLastName());
                    assertThat(userDB.get().getGender()).isNotEqualTo(input.getKey().getGender());
                    assertThat(userDB.get().getProfileName()).isNotEqualTo(input.getKey().getProfileName());
                    assertThat(userDB.get().getEmail()).isNotEqualTo(input.getKey().getEmail());
                    assertThat(userDB.get().getPassword()).isNotEqualTo(input.getKey().getPassword());
                    assertThat(userDB.get().getRole()).isNotEqualTo(input.getKey().getRole());
                    assertThat(userDB.get().getCountry().getId()).isNotEqualTo(input.getKey().getCountryId());

                } else {
                    assertThat(doesUserExist).isFalse();
                    assertThat(userDB).isNotNull();
                    assertThat(userDB.isPresent()).isFalse();
                }

                String[] nameAndExtension = MyImage.extractNameAndExtension(input.getValue().getOriginalFilename());
                userProfileImage = fileRepo.getUserProfileImage(input.getKey().getProfileName() + "." + nameAndExtension[1]);

                assertThat(userProfileImage.exists()).isFalse();
                assertThat(userProfileImage.isReadable()).isFalse();

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("register_DuplicateInput_NotCreatedUserReturns400", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/auth/register with user data that has non-existent dependency objects did not create new user and it returned 404")
    void register_NonExistentDependencyObjects_NotCreatedUserReturns404() {
        ResponseEntity<String> response;
        boolean doesUserExist;
        Optional<UserJDBC> userDB;
        Resource userProfileImage;
        UserRegisterDTO nonExistentCountryIdInput = new UserRegisterDTO(
                "Marko", "Markovic", Gender.MALE,
                "marko_markov11", null, "marko11", "marko11@gmail.com", "123456", 400l);
        MockMultipartFile mockImage = new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8));

        response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(nonExistentCountryIdInput, mockImage), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        doesUserExist = userRepo.existsUsername(nonExistentCountryIdInput.getUsername());
        userDB = userRepo.findByUsername(nonExistentCountryIdInput.getUsername());
        assertThat(doesUserExist).isFalse();
        assertThat(userDB).isNotNull();
        assertThat(userDB.isPresent()).isFalse();
        String[] nameAndExtension = MyImage.extractNameAndExtension(mockImage.getOriginalFilename());
        userProfileImage = fileRepo.getUserProfileImage(nonExistentCountryIdInput.getProfileName() + "." + nameAndExtension[1]);
        assertThat(userProfileImage.exists()).isFalse();
        assertThat(userProfileImage.isReadable()).isFalse();

        testsPassed.put("register_NonExistentDependencyObjects_NotCreatedUserReturns404", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/auth/register with valid user data did create new user and it returned 200")
    void register_ValidInput_CreatedUserReturns200() {
        ResponseEntity<String> response;
        boolean doesUserExist;
        Optional<UserJDBC> userDB;
        Resource userProfileImage;
        UserRole[] roles = {UserRole.REGULAR, UserRole.CRITIC};
        Random random = new Random();
        int i = 0;

        List<SimpleEntry<UserRegisterDTO, MockMultipartFile>> validInputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markov", null, "marko", "marko@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markov2", null, "marko2", "marko2@gmail.com", "123456", 190l),
                        null));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Teodora", "Teodorovic", Gender.FEMALE,
                                "tea123", null, "tea123", "tea123@gmail.com", "ttodora", 190l),
                        new MockMultipartFile("profile_image", "test.png",
                                "image/png", "test image random content bytes".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "aaaaa", "aaaaa", Gender.OTHER,
                                "aaaaa", null, "aaaaa", "aaaaa@gmail.com", "aaa11aaaaa", 1l),
                        new MockMultipartFile("profile_image", "test.png",
                                "image/png", "afnpaisjngfvapiugn1431234ijansdfv".getBytes(StandardCharsets.UTF_8))));

                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko3", "Markovic3", Gender.OTHER,
                                "marko2_markiz3", null, "marko3", "marko3@gmail.com", "mmarko123", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "testimageBYteasfaosdfnarandomcontent".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Teodora", "Teodorovic", Gender.FEMALE,
                                "teadora123", null, "tea321", "tea321@gmail.com", "ttodora", 190l),
                        null));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Teodora", "Teodorovic", Gender.FEMALE,
                                "teadora1234", null, "tea3214", "tea321@yahoo.com", "ttodora", 185l),
                        null));
            }
        };

        try {
            for (SimpleEntry<UserRegisterDTO, MockMultipartFile> validInput : validInputs) {
                //set randomly either REGULAR or CRITIC
                validInput.getKey().setRole(roles[random.nextInt(roles.length)]);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(validInput.getKey(), validInput.getValue()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                doesUserExist = userRepo.existsUsername(validInput.getKey().getUsername());
                userDB = userRepo.findByUsername(validInput.getKey().getUsername());

                assertThat(doesUserExist).isTrue();
                assertThat(userDB).isNotNull();
                assertThat(userDB.isPresent()).isTrue();

                assertThat(userDB.get()).isNotNull();
                assertThat(userDB.get().getFirstName()).isNotEmpty().isEqualTo(validInput.getKey().getFirstName());
                assertThat(userDB.get().getLastName()).isNotEmpty().isEqualTo(validInput.getKey().getLastName());
                assertThat(userDB.get().getGender()).isNotNull().isEqualTo(validInput.getKey().getGender());
                assertThat(userDB.get().getRole()).isNotNull().isEqualTo(validInput.getKey().getRole());
                assertThat(userDB.get().getCountry()).isNotNull();
                assertThat(userDB.get().getCountry().getId()).isNotNull().isEqualTo(validInput.getKey().getCountryId());
                assertThat(userDB.get().getEmail()).isNotEmpty().isEqualTo(validInput.getKey().getEmail());
                assertThat(userDB.get().getProfileName()).isNotEmpty().isEqualTo(validInput.getKey().getProfileName());
                assertThat(userDB.get().getUsername()).isNotEmpty().isEqualTo(validInput.getKey().getUsername());
                assertThat(userDB.get().getPassword()).isNotEmpty();
                assertThat(pswEncoder.matches(validInput.getKey().getPassword(), userDB.get().getPassword())).isTrue();

                if (validInput.getValue() != null) {
                    String[] nameAndExtension = MyImage.extractNameAndExtension(validInput.getValue().getOriginalFilename());
                    userProfileImage = fileRepo.getUserProfileImage(validInput.getKey().getProfileName() + "." + nameAndExtension[1]);

                    assertThat(userProfileImage.exists()).isTrue();
                    assertThat(userProfileImage.isReadable()).isTrue();
                    try {
                        assertThat(userProfileImage.getContentAsByteArray()).isEqualTo(validInput.getValue().getBytes());
                    } catch (IOException ex) {
                        fail("getContentAsByteArray() and getBytes() were not supposed to fail");
                    }
                } else {
                    for (String extension : MyImage.VALID_EXTENSIONS) {
                        userProfileImage = fileRepo.getUserProfileImage(validInput.getKey().getProfileName() + "." + extension);
                        assertThat(userProfileImage.exists()).isFalse();
                        assertThat(userProfileImage.isReadable()).isFalse();
                    }
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("register_ValidInput_CreatedUserReturns200", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to POST /api/auth/login with structurally invalid data did not login user and it returned 400")
    void login_InvalidInput_NotLoggedInReturns400() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<UserLoginDTO> invalidInput = new ArrayList<>() {
            {
                add(new UserLoginDTO(null, "admin"));
                add(new UserLoginDTO("", "admin"));
                add(new UserLoginDTO(" ", "admin"));
                add(new UserLoginDTO("       ", "admin"));
                add(new UserLoginDTO(getRandomString(301, random), "admin"));
                add(new UserLoginDTO("admin", null));
                add(new UserLoginDTO("admin", ""));
                add(new UserLoginDTO("admin", " "));
                add(new UserLoginDTO("admin", "      "));
                add(new UserLoginDTO("admin", getRandomString(301, random)));
            }
        };
        try {
            for (UserLoginDTO input : invalidInput) {
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(input, null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                // Check cookies
                MultiValueMap<String, String> responseHeaders = response.getHeaders();
                List<String> setCookieHeader = responseHeaders.get(HttpHeaders.SET_COOKIE);
                assertThat(setCookieHeader).isNull();

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("login_InvalidInput_NotLoggedInReturns400", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests whether POST request to POST /api/auth/login with non-existent user did not login user and it returned 401")
    void login_NonExistentUser_NotLoggedInReturns401() {
        ResponseEntity<String> response;
        int i = 0;
        List<UserLoginDTO> nonExistentUserInput = new ArrayList<>() {
            {
                add(new UserLoginDTO("admina", "admina"));
                add(new UserLoginDTO("admin", "admina"));
                add(new UserLoginDTO("admina", "admin"));
                add(new UserLoginDTO("admin", "admina"));
                add(new UserLoginDTO("aaaaa", "aaaaa"));
            }
        };
        try {
            for (UserLoginDTO input : nonExistentUserInput) {
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(input, null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                // Check cookies
                MultiValueMap<String, String> responseHeaders = response.getHeaders();
                List<String> setCookieHeader = responseHeaders.get(HttpHeaders.SET_COOKIE);
                assertThat(setCookieHeader).isNull();

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("login_NonExistentUser_NotLoggedInReturns401", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests whether POST request to /api/auth/login with valid user data did login user by returning user data, JWT and 200")
    void login_ValidInput_ReturnsUserJWTAnd200() {
        ResponseEntity<String> response;
        int i = 0;
        //existing user input and expected json response
        List<SimpleEntry<UserLoginDTO, String>> existingUserInput = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new UserLoginDTO("admin", "admin"), jsonReader.getUserJson(1)));
                add(new SimpleEntry<>(new UserLoginDTO("regular", "regular"), jsonReader.getUserJson(2)));
                add(new SimpleEntry<>(new UserLoginDTO("critic", "critic"), jsonReader.getUserJson(3)));
            }
        };
        try {
            for (SimpleEntry<UserLoginDTO, String> input : existingUserInput) {
                response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(input.getKey(), null), String.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                // Check cookies
                List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                assertThat(cookies).isNotNull();
                assertThat(cookies.size()).isEqualTo(1);
                for (String cookieString : cookies) {
                    HttpCookie cookie = HttpCookie.parse(cookieString).get(0);
                    assertThat(cookie).isNotNull();
                    assertThat(cookie.getName()).isEqualTo(config.getJwtCookieName());
                    assertThat(cookie.getPath()).isEqualTo("/api");
                    assertThat(cookie.isHttpOnly()).isTrue();
                    assertThat(cookie.getMaxAge()).isEqualTo(24 * 60 * 60);
                    assertThat(jwtUtils.getUserNameFromJwtToken(cookie.getValue())).isEqualTo(input.getKey().getUsername());
                }

                //check user data
                assertThat(response.getBody()).isEqualTo(input.getValue());

                i++;
            }
        } catch (AssertionError | IllegalArgumentException e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("login_ValidInput_ReturnsUserJWTAnd200", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether POST request to /api/auth/login with valid user data returns correct cookie and user data after POST of another valid user with different JWT")
    void login_SuccessiveValidInput_ReturnsCorrectJWTAndUser() {
        Assumptions.assumeTrue(testsPassed.get("login_ValidInput_ReturnsUserJWTAnd200"));

        ResponseEntity<String> response;
        UserLoginDTO adminUser = new UserLoginDTO("admin", "admin");
        UserLoginDTO regularUser = new UserLoginDTO("regular", "regular");
        UserLoginDTO criticUser = new UserLoginDTO("critic", "critic");

        //JWT of admin user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(adminUser, null), String.class);
        String adminCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(adminCookie).isNotNull();

        //JWT of regular user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(regularUser, null), String.class);
        String regularCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(regularCookie).isNotNull();

        //JWT of critic user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(criticUser, null), String.class);
        String criticCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(criticCookie).isNotNull();

        //make a login request for regular user with admin JWT
        HttpEntity request = constructRequest(regularUser, adminCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        String actualResponseCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(actualResponseCookie).isNotNull();
        assertThat(actualResponseCookie).isNotEqualTo(adminCookie);
        assertThat(actualResponseCookie).isNotEqualTo(criticCookie);
        HttpCookie responseCookie = HttpCookie.parse(actualResponseCookie).get(0);
        assertThat(responseCookie).isNotNull();
        assertThat(jwtUtils.getUserNameFromJwtToken(responseCookie.getValue())).isEqualTo("regular");
        assertThat(response.getBody()).isEqualTo(jsonReader.getUserJson(2));

        //make a login request for admin user with regular JWT
        request = constructRequest(adminUser, regularCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        actualResponseCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(actualResponseCookie).isNotNull();
        assertThat(actualResponseCookie).isNotEqualTo(regularCookie);
        assertThat(actualResponseCookie).isNotEqualTo(criticCookie);
        responseCookie = HttpCookie.parse(actualResponseCookie).get(0);
        assertThat(responseCookie).isNotNull();
        assertThat(jwtUtils.getUserNameFromJwtToken(responseCookie.getValue())).isEqualTo("admin");
        assertThat(response.getBody()).isEqualTo(jsonReader.getUserJson(1));

        //make a login request for admin user with critic JWT
        request = constructRequest(adminUser, criticCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        actualResponseCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(actualResponseCookie).isNotNull();
        assertThat(actualResponseCookie).isNotEqualTo(criticCookie);
        assertThat(actualResponseCookie).isNotEqualTo(regularCookie);
        responseCookie = HttpCookie.parse(actualResponseCookie).get(0);
        assertThat(responseCookie).isNotNull();
        assertThat(jwtUtils.getUserNameFromJwtToken(responseCookie.getValue())).isEqualTo("admin");
        assertThat(response.getBody()).isEqualTo(jsonReader.getUserJson(1));

        //make a login request for critic user with regular JWT
        request = constructRequest(criticUser, regularCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        actualResponseCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(actualResponseCookie).isNotNull();
        assertThat(actualResponseCookie).isNotEqualTo(regularCookie);
        assertThat(actualResponseCookie).isNotEqualTo(adminCookie);
        responseCookie = HttpCookie.parse(actualResponseCookie).get(0);
        assertThat(responseCookie).isNotNull();
        assertThat(jwtUtils.getUserNameFromJwtToken(responseCookie.getValue())).isEqualTo("critic");
        assertThat(response.getBody()).isEqualTo(jsonReader.getUserJson(3));

        testsPassed.put("login_SuccessiveValidInput_ReturnsCorrectJWTAndUser", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether POST request to /api/auth/login with non-existent user data and valid JWT returns 401 and no JWT")
    void login_NonExistentUserInputValidJWT_Returns401AndNoJWT() {
        Assumptions.assumeTrue(testsPassed.get("login_ValidInput_ReturnsUserJWTAnd200"));

        ResponseEntity<String> response;
        UserLoginDTO adminUser = new UserLoginDTO("admin", "admin");
        UserLoginDTO regularUser = new UserLoginDTO("regular", "regular");
        UserLoginDTO criticUser = new UserLoginDTO("critic", "critic");

        //JWT of admin user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(adminUser, null), String.class);
        String adminCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(adminCookie).isNotNull();

        //JWT of regular user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(regularUser, null), String.class);
        String regularCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(regularCookie).isNotNull();

        //JWT of critic user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(criticUser, null), String.class);
        String criticCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(criticCookie).isNotNull();

        //non-existent user with valid JWT
        HttpEntity request = constructRequest(new UserLoginDTO("dummyUser101", "dummyPassword101"), adminCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        List<String> actualResponseCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(actualResponseCookies).isNull();

        request = constructRequest(new UserLoginDTO("dummyUser102", "dummyPassword102"), regularCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        actualResponseCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(actualResponseCookies).isNull();

        request = constructRequest(new UserLoginDTO("dummyUser102", "dummyPassword102"), criticCookie);
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        actualResponseCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(actualResponseCookies).isNull();

        testsPassed.put("login_NonExistentUserInputValidJWT_Returns401AndNoJWT", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests whether POST request to /api/auth/logout without a JWT returned 401")
    void logout_NoJWT_Returns401() {
        for (int i = 0; i < 100; i++) {
            ResponseEntity<String> response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, constructRequest(null), String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        testsPassed.put("logout_NoJWT_Returns401", true);
    }

    @Test
    @Order(11)
    @DisplayName("Tests whether POST request to /api/auth/logout with an invalid JWT returned 401")
    void logout_InvalidJWT_Returns401() {
        Assumptions.assumeTrue(testsPassed.get("login_ValidInput_ReturnsUserJWTAnd200"));

        Random random = new Random();
        ResponseEntity<String> response;
        UserLoginDTO adminUser = new UserLoginDTO("admin", "admin");
        UserLoginDTO regularUser = new UserLoginDTO("regular", "regular");
        UserLoginDTO criticUser = new UserLoginDTO("critic", "critic");
        int i = 0;

        //JWT of admin user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(adminUser, null), String.class);
        HttpCookie adminCookie = HttpCookie.parse(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).get(0);
        assertThat(adminCookie).isNotNull();

        //JWT of regular user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(regularUser, null), String.class);
        HttpCookie regularCookie = HttpCookie.parse(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).get(0);
        assertThat(regularCookie).isNotNull();

        //JWT of critic user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(criticUser, null), String.class);
        HttpCookie criticCookie = HttpCookie.parse(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).get(0);
        assertThat(criticCookie).isNotNull();

        adminCookie.setValue("a" + adminCookie.getValue().substring(1));
        regularCookie.setValue("dummyfirstvalue.dummytsecond.thirdoptionwhatever");
        criticCookie.setValue(getRandomString(100, random));

        HttpCookie emptyValueCookie = (HttpCookie) adminCookie.clone();
        emptyValueCookie.setValue("");

        //invalid JWT value Strings
        List<String> invalidJWTs = new ArrayList<>() {
            {
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ");
                add(".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQSflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQSflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
                add("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ==.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
            }
        };

        //invalid cookies
        List<HttpCookie> invalidCookies = new ArrayList<>() {
            {
                add(adminCookie);
                add(regularCookie);
                add(criticCookie);
                add(emptyValueCookie);
            }
        };
        for (String invalidJWT : invalidJWTs) {
            HttpCookie invalidCookie = (HttpCookie) criticCookie.clone();
            invalidCookie.setValue(invalidJWT);
            invalidCookies.add(invalidCookie);
        }
        invalidCookies.add(new HttpCookie("token", getRandomString(100, random)));
        invalidCookies.add(new HttpCookie(getRandomString(10, random), getRandomString(100, random)));

        //check
        try {
            for (HttpCookie invalidCookie : invalidCookies) {
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, constructRequest(invalidCookie.toString()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        //invalid cookie strings
        List<String> invalidCookieNames = new ArrayList<>() {
            {
                add("");
                add("username=");
                add("user@name=value");
                add("user name=value");
                add("usernamevalue");
                add("username==value");
                add("username=\\u0000value");
                add("username=valué");
                add("cwt=; Path=/api; Max-Age=86400; HttpOnly");
                add("cwt=invalidToken; Path=/api; Max-Age=86400; HttpOnly");
                add("cwt=yourJwtToken; Path=/api; Max-Age=0; HttpOnly");
                add("cwt=yourJwtToken; Path=/api; Max-Age=3153600000; HttpOnly");
                add("cwt=yourJwtToken; Path=/wrongPath; Max-Age=86400; HttpOnly");
                add("cwt=yourJwtToken; Path=/api; Max-Age=86400");
                add("cwt=yourJwtToken; Path=/api; Max-Age=86400; HttpOnly; Secure; SameSite=Lax");
                add("wrongName=yourJwtToken; Path=/api; Max-Age=86400; HttpOnly");
                add("cwt=yourJwtToken$%^&*(); Path=/api; Max-Age=86400; HttpOnly");
                add("cwt=your Jwt Token; Path=/api; Max-Age=86400; HttpOnly");
                add("LWSSO_COOKIE_KEY=123-LWSSO_COOKIE_KEY-VALUE-456; Path=/; Domain=localhost; Max-Age=PT168H; Expires=Tue, 14 Jun 2022 22:35:02 GMT; Secure; HttpOnly");
            }
        };
        try {
            for (String cookie : invalidCookieNames) {
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, constructRequest(cookie), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("logout_InvalidJWT_Returns401", true);
    }

    @Test
    @Order(12)
    @DisplayName("Tests whether POST request to /api/auth/logout with a JWT of an non-existent user returned 401")
    void logout_NonExistentUserJWT_Returns401() {
        Assumptions.assumeTrue(testsPassed.get("login_ValidInput_ReturnsUserJWTAnd200"));

        Random random = new Random();
        ResponseEntity<String> response;
        int i = 0;

        SecurityUser user1 = new SecurityUser();
        user1.setFirstName("DummyName");
        user1.setLastName("DummyName");
        user1.setGender(Gender.OTHER);
        user1.setProfileName("DummyProfileName");
        user1.setEmail("DummyEmail@gmail.com");
        user1.setUsername("DummyUsername");
        user1.setPassword("DummyPassword");
        user1.setRole(UserRole.ADMINISTRATOR);
        user1.setCountry(new SecurityUser.Country(190l, "Some random country", "Some random country", "RM"));
        user1.setId(100l);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());

        SecurityUser user2 = new SecurityUser();
        user2.setFirstName("DummyName2");
        user2.setLastName("DummyName2");
        user2.setGender(Gender.MALE);
        user2.setProfileName("DummyProfileName2");
        user2.setEmail("DummyEmail2@gmail.com");
        user2.setUsername("DummyUsername2");
        user2.setPassword("DummyPassword2");
        user2.setRole(UserRole.REGULAR);
        user2.setCountry(new SecurityUser.Country(190l, "Some random country2", "Some random country2", "RF"));
        user2.setId(101l);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());

        SecurityUser user3 = new SecurityUser();
        user3.setFirstName(getRandomString(10, random));
        user3.setLastName(getRandomString(10, random));
        user3.setGender(Gender.FEMALE);
        user3.setProfileName(getRandomString(15, random));
        user3.setEmail(getRandomString(10, random) + "@gmail.com");
        user3.setUsername(getRandomString(20, random));
        user3.setPassword(getRandomString(12, random));
        user3.setRole(UserRole.CRITIC);
        user3.setCountry(new SecurityUser.Country(190l, getRandomString(30, random), getRandomString(33, random), getRandomString(2, random)));
        user3.setId(102l);
        user3.setCreatedAt(LocalDateTime.now());
        user3.setUpdatedAt(LocalDateTime.now());

        List<SecurityUser> users = new ArrayList<>() {
            {
                add(user1);
                add(user2);
                add(user3);
            }
        };
        try {
            for (SecurityUser user : users) {
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, constructRequest(jwtUtils.generateJwtCookie(user).toString()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("logout_NonExistentUserJWT_Returns401", true);
    }

    @Test
    @Order(13)
    @DisplayName("Tests whether POST request to /api/auth/logout with a JWT of an existing user returned no JWT and 200")
    void logout_ValidJWT_ReturnsNoJWTAnd200() {
        Assumptions.assumeTrue(testsPassed.get("login_ValidInput_ReturnsUserJWTAnd200"));

        ResponseEntity<String> response;
        UserLoginDTO adminUser = new UserLoginDTO("admin", "admin");
        UserLoginDTO regularUser = new UserLoginDTO("regular", "regular");
        UserLoginDTO criticUser = new UserLoginDTO("critic", "critic");
        int i = 0;

        //JWT of admin user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(adminUser, null), String.class);
        String adminCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(adminCookie).isNotNull();

        //JWT of regular user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(regularUser, null), String.class);
        String regularCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(regularCookie).isNotNull();

        //JWT of critic user
        response = restTemplate.exchange(ROUTE + "/login", HttpMethod.POST, constructRequest(criticUser, null), String.class);
        String criticCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        assertThat(criticCookie).isNotNull();

        List<String> cookies = new ArrayList<>() {
            {
                add(adminCookie);
                add(regularCookie);
                add(criticCookie);
            }
        };

        try {
            for (String cookie : cookies) {
                response = restTemplate.exchange(ROUTE + "/logout", HttpMethod.POST, constructRequest(cookie), String.class);
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
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("logout_ValidJWT_ReturnsNoJWTAnd200", true);
    }

//=========================================================================================================
//PRIVATE METHODS
    private HttpEntity<MultiValueMap<String, Object>> constructRequest(UserRegisterDTO registerForm, MockMultipartFile profileImage) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRegisterDTO> userEntity = new HttpEntity<>(registerForm, userHeaders);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("user", userEntity);

        if (profileImage != null) {
            try {
                Resource image = new ByteArrayResource(profileImage.getBytes()) {
                    @Override
                    public String getFilename() {
                        return profileImage.getOriginalFilename();
                    }
                };
                HttpHeaders imageHeaders = new HttpHeaders();
                imageHeaders.setContentType(MediaType.valueOf(profileImage.getContentType()));
                HttpEntity<Resource> imageEntity = new HttpEntity<>(image, imageHeaders);
                body.add("profile_image", imageEntity);
            } catch (IOException ex) {
                fail("getBytes() should not have thrown an exception");
            }
        }

        return new HttpEntity<>(body, requestHeaders);
    }

    private HttpEntity<UserLoginDTO> constructRequest(UserLoginDTO loginForm, String cookie) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (cookie != null) {
            requestHeaders.set(HttpHeaders.COOKIE, cookie);
        }

        HttpEntity<UserLoginDTO> request = new HttpEntity<>(loginForm, requestHeaders);

        return request;
    }

    private HttpEntity constructRequest(String cookie) {
        if (cookie != null) {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set(HttpHeaders.COOKIE, cookie);
            return new HttpEntity<>(null, requestHeaders);
        }
        return new HttpEntity<>(null);
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

    private boolean isValidFilename(String filename) {
        try {
            Paths.get(filename);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

//=========================================================================================================
}
