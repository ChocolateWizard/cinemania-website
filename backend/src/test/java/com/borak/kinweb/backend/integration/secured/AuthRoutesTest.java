/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.domain.classes.MyImage;

import com.borak.kinweb.backend.domain.dto.user.UserRegisterDTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.UserRepositoryJDBC;
import com.borak.kinweb.backend.repository.util.FileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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
import org.springframework.core.io.ByteArrayResource;

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
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder pswEncoder;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/auth";

    static {
        testsPassed.put("register_Test", false);
        testsPassed.put("login_Test", false);
        testsPassed.put("logout_Test", false);
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
    @Disabled
    @DisplayName("Tests POST /api/auth/register")
    void register_Test() {
        //response from server
        ResponseEntity<String> response;
        //if object was created
        boolean actualBool;
        //created object
        Optional<UserJDBC> actualObject;
        //image file request
        Resource actualFile;
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
            }
        };

        //assert valid inputs
        try {
            for (SimpleEntry<UserRegisterDTO, MockMultipartFile> validInput : validInputs) {
                //set randomly either REGULAR or CRITIC
                validInput.getKey().setRole(roles[random.nextInt(roles.length)]);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(validInput.getKey(), validInput.getValue()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                actualBool = userRepo.existsUsername(validInput.getKey().getUsername());
                actualObject = userRepo.findByUsername(validInput.getKey().getUsername());

                assertThat(actualBool).isTrue();
                assertThat(actualObject).isNotNull();
                assertThat(actualObject.isPresent()).isTrue();
                checkObjectValues(actualObject.get(), validInput.getKey());

                if (validInput.getValue() != null) {
                    String[] nameAndExtension = MyImage.extractNameAndExtension(validInput.getValue().getOriginalFilename());
                    actualFile = fileRepo.getUserProfileImage(validInput.getKey().getProfileName() + "." + nameAndExtension[1]);

                    assertThat(actualFile.exists()).isTrue();
                    assertThat(actualFile.isReadable()).isTrue();
                    try {
                        assertThat(actualFile.getContentAsByteArray()).isEqualTo(validInput.getValue().getBytes());
                    } catch (IOException ex) {
                        fail("getContentAsByteArray() and getBytes() were not supposed to fail");
                    }
                } else {
                    for (String extension : MyImage.VALID_EXTENSIONS) {
                        actualFile = fileRepo.getUserProfileImage(validInput.getKey().getProfileName() + "." + extension);
                        assertThat(actualFile.exists()).isFalse();
                        assertThat(actualFile.isReadable()).isFalse();
                    }
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        //duplicate user requests
        List<SimpleEntry<UserRegisterDTO, MockMultipartFile>> duplicateUserInputs = new ArrayList<>() {
            {
                //username exists
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markov10", null, "admin", "marko10@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                //email exists
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "marko_markov10", null, "marko10", "marko@gmail.com", "123456", 1l),
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
        i = 0;
        try {
            for (SimpleEntry<UserRegisterDTO, MockMultipartFile> input : duplicateUserInputs) {
                //set randomly either REGULAR or CRITIC
                input.getKey().setRole(roles[random.nextInt(roles.length)]);
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(input.getKey(), input.getValue()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                actualBool = userRepo.existsUsername(input.getKey().getUsername());
                actualObject = userRepo.findByUsername(input.getKey().getUsername());

                if (input.getKey().getUsername().equals("admin")) {
                    assertThat(actualBool).isTrue();
                    assertThat(actualObject).isNotNull();
                    assertThat(actualObject.isPresent()).isTrue();

                    assertThat(actualObject.get().getFirstName()).isNotEqualTo(input.getKey().getFirstName());
                    assertThat(actualObject.get().getLastName()).isNotEqualTo(input.getKey().getLastName());
                    assertThat(actualObject.get().getGender()).isNotEqualTo(input.getKey().getGender());
                    assertThat(actualObject.get().getProfileName()).isNotEqualTo(input.getKey().getProfileName());
                    assertThat(actualObject.get().getEmail()).isNotEqualTo(input.getKey().getEmail());
                    assertThat(actualObject.get().getPassword()).isNotEqualTo(input.getKey().getPassword());

                } else {
                    assertThat(actualBool).isFalse();
                    assertThat(actualObject).isNotNull();
                    assertThat(actualObject.isPresent()).isFalse();
                }

                if (!input.getKey().getProfileName().equals("Admin")) {
                    String[] nameAndExtension = MyImage.extractNameAndExtension(input.getValue().getOriginalFilename());
                    actualFile = fileRepo.getUserProfileImage(input.getKey().getProfileName() + "." + nameAndExtension[1]);

                    assertThat(actualFile.exists()).isFalse();
                    assertThat(actualFile.isReadable()).isFalse();
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        //country id not found
        UserRegisterDTO invalidCountryIdUser = new UserRegisterDTO(
                "Marko", "Markovic", Gender.MALE,
                "marko_markov11", null, "marko11", "marko11@gmail.com", "123456", 400l);
        MockMultipartFile mockImage = new MockMultipartFile("profile_image", "test.jpg", "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8));
        response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(invalidCountryIdUser, mockImage), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        actualBool = userRepo.existsUsername(invalidCountryIdUser.getUsername());
        actualObject = userRepo.findByUsername(invalidCountryIdUser.getUsername());
        assertThat(actualBool).isFalse();
        assertThat(actualObject).isNotNull();
        assertThat(actualObject.isPresent()).isFalse();
        String[] nameAndExtension = MyImage.extractNameAndExtension(mockImage.getOriginalFilename());
        actualFile = fileRepo.getUserProfileImage(invalidCountryIdUser.getProfileName() + "." + nameAndExtension[1]);
        assertThat(actualFile.exists()).isFalse();
        assertThat(actualFile.isReadable()).isFalse();

        //bad requests
        List<SimpleEntry<UserRegisterDTO, MockMultipartFile>> badUserInputs = new ArrayList<>() {
            {
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
                                "Marko", "Markovic", null,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
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
                                "marko  makro", null, "badInput", "badInput@gmail.com", "123456", 1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
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
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", null),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
                add(new SimpleEntry<>(
                        new UserRegisterDTO(
                                "Marko", "Markovic", Gender.MALE,
                                "badInput", null, "badInput", "badInput@gmail.com", "123456", -1l),
                        new MockMultipartFile("profile_image", "test.jpg",
                                "image/jpeg", "test image content".getBytes(StandardCharsets.UTF_8))));
            }
        };
        i = 0;
        try {
            for (SimpleEntry<UserRegisterDTO, MockMultipartFile> input : badUserInputs) {
                response = restTemplate.exchange(ROUTE + "/register", HttpMethod.POST, constructRequest(input.getKey(), input.getValue()), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                if (input.getKey().getUsername() != null) {
                    actualBool = userRepo.existsUsername(input.getKey().getUsername());
                    actualObject = userRepo.findByUsername(input.getKey().getUsername());
                    assertThat(actualBool).isFalse();
                    assertThat(actualObject).isNotNull();
                    assertThat(actualObject.isPresent()).isFalse();
                } else {
                    actualBool = userRepo.existsEmail(input.getKey().getEmail());
                    assertThat(actualBool).isFalse();
                }

                if (input.getValue() != null && input.getKey().getProfileName() != null) {
                    nameAndExtension = MyImage.extractNameAndExtension(input.getValue().getOriginalFilename());
                    actualFile = fileRepo.getUserProfileImage(input.getKey().getProfileName() + "." + nameAndExtension[1]);

                    assertThat(actualFile.exists()).isFalse();
                    assertThat(actualFile.isReadable()).isFalse();
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("register_Test", true);
    }

    @Test
    @Order(2)
    @Disabled
    @DisplayName("Tests POST /api/auth/login")
    void login_Test() {

        testsPassed.put("login_Test", true);
    }

    @Test
    @Order(3)
    @Disabled
    @DisplayName("Tests POST /api/auth/logout")
    void logout_Test() {

        testsPassed.put("logout_Test", true);
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

    private void checkObjectValues(UserJDBC actual, UserRegisterDTO expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getFirstName()).isNotEmpty().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotEmpty().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isNotNull().isEqualTo(expected.getRole());
        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getCountry().getId()).isNotNull().isEqualTo(expected.getCountryId());
        assertThat(actual.getEmail()).isNotEmpty().isEqualTo(expected.getEmail());
        assertThat(actual.getProfileName()).isNotEmpty().isEqualTo(expected.getProfileName());
        assertThat(actual.getUsername()).isNotEmpty().isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isNotEmpty();
        assertThat(pswEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();
    }

//=========================================================================================================
}
