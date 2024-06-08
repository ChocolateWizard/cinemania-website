/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.nonsecured;

import com.borak.kinweb.backend.helpers.TestJsonResponseReader;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import java.util.HashMap;
import java.util.Map;
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
public class PersonRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/persons";

    static {
        testsPassed.put("getAllPersonsPaginated_Test", false);
        testsPassed.put("getAllPersonsWithDetailsPaginated_Test", false);
        testsPassed.put("getPersonWithProfessions_Test", false);
        testsPassed.put("getPersonWithDetails_Test", false);
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
    @DisplayName("Tests GET /api/persons")
    void getAllPersonsPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(2));

        response
                = restTemplate.getForEntity(ROUTE + "?page=3&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(3));

        response
                = restTemplate.getForEntity(ROUTE + "?page=5&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(4));

        response
                = restTemplate.getForEntity(ROUTE + "?page=6&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(5));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(6));

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(7));

        response
                = restTemplate.getForEntity(ROUTE + "?page=10&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(8));

        response
                = restTemplate.getForEntity(ROUTE + "?page=20&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(9));

        //----------------------------------------------------------------------------
        //bad requests
        response
                = restTemplate.getForEntity(ROUTE + "?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "?page=-1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getAllPersonsPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/persons/details")
    void getAllPersonsWithDetailsPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(10));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(10));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(11));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=3&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(12));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=5&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(13));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=6&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(14));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(15));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(16));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=10&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(17));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=20&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(18));

        //----------------------------------------------------------------------------
        //bad requests
        response
                = restTemplate.getForEntity(ROUTE + "/details?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=-1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getAllPersonsWithDetailsPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /api/persons/{id}")
    void getPersonWithProfessions_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(19));

        response
                = restTemplate.getForEntity(ROUTE + "/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(20));

        response
                = restTemplate.getForEntity(ROUTE + "/10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(21));

        response
                = restTemplate.getForEntity(ROUTE + "/20", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(22));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/51", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/52", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/70", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getPersonWithProfessions_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests GET /api/persons/{id}/details")
    void getPersonWithDetails_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(23));

        response
                = restTemplate.getForEntity(ROUTE + "/2/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(24));

        response
                = restTemplate.getForEntity(ROUTE + "/10/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(25));

        response
                = restTemplate.getForEntity(ROUTE + "/20/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getPersonJson(26));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/51/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/52/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/70/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/100/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getPersonWithDetails_Test", true);
    }

}
