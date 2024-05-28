/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.nonsecured;

import static org.assertj.core.api.Assertions.assertThat;
import com.borak.kinweb.backend.helpers.TestJsonResponseReader;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(5)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MediaRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/medias";

    static {
        testsPassed.put("getMediasByTitle_Test", false);
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
    @DisplayName("Tests GET /api/medias")
    void getMediasByTitle_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/search", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=a", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(2));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=c", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(3));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=Sou", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(4));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=st", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(5));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=South Park", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(6));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=ar", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMediaJson(7));

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=10&title=   a  ", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        //----------------------------------------------------------------------------
        //bad requests
        response
                = restTemplate.getForEntity(ROUTE + "/search?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=-1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/search?page=1&size=-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getMediasByTitle_Test", true);
    }

}
