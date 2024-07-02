/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.nonsecured;

import static org.assertj.core.api.Assertions.assertThat;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.PersonJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import com.borak.cwb.backend.helpers.DataInitializer;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.repository.file.FileRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(5)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FileRepository fileRepo;

    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/images";

    static {
        testsPassed.put("getMediaImage_Test", false);
        testsPassed.put("getPersonImage_Test", false);
        testsPassed.put("getUserImage_Test", false);
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
        init.initImages();
    }

    @Test
    @Order(1)
    @DisplayName("Tests GET /images/media/")
    void getMediaImage_Test() throws IOException {
        ResponseEntity<byte[]> response;
        String imageName;
        Resource image;
        for (MediaJDBC media : init.getMedias()) {
            imageName = media.getCoverImage();
            if (imageName != null) {
                response = restTemplate.getForEntity(ROUTE + "/media/" + imageName, byte[].class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                image = fileRepo.getMediaCoverImage(imageName);
                assertThat(image).isNotNull();
                assertThat(response.getBody()).isEqualTo(image.getContentAsByteArray());
            } else {
                response = restTemplate.getForEntity(ROUTE + "/media/" + media.getId() + ".jpg", byte[].class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            }
        }
        testsPassed.put("getMediaImage_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /images/person/")
    void getPersonImage_Test() throws IOException {
        ResponseEntity<byte[]> response;
        String imageName;
        Resource image;
        for (PersonJDBC person : init.getPersons()) {
            imageName = person.getProfilePhoto();
            if (imageName != null) {
                response = restTemplate.getForEntity(ROUTE + "/person/" + imageName, byte[].class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                image = fileRepo.getPersonProfilePhoto(imageName);
                assertThat(image).isNotNull();
                assertThat(response.getBody()).isEqualTo(image.getContentAsByteArray());
            } else {
                response = restTemplate.getForEntity(ROUTE + "/person/" + person.getId() + ".jpg", byte[].class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            }
        }
        testsPassed.put("getPersonImage_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /images/user/")
    void getUserImage_Test() throws IOException {
        ResponseEntity<byte[]> response;
        String imageName;
        Resource image;
        for (UserJDBC user : init.getUsers()) {
            imageName = user.getProfileImage();
            if (imageName != null) {
                response = restTemplate.getForEntity(ROUTE + "/user/" + imageName, byte[].class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                image = fileRepo.getUserProfileImage(imageName);
                assertThat(image).isNotNull();
                assertThat(response.getBody()).isEqualTo(image.getContentAsByteArray());
            } else {
                response = restTemplate.getForEntity(ROUTE + "/user/" + user.getProfileName() + ".jpg", byte[].class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            }
        }
        testsPassed.put("getUserImage_Test", true);
    }

}
