/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.nonsecured;

import com.borak.cwb.backend.helpers.Pair;
import com.borak.cwb.backend.helpers.TestJsonResponseReader;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import java.util.HashMap;
import java.util.Map;
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

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/persons";

    static {
        TESTS_PASSED.put("getAllPersonsPaginated_Test", false);
        TESTS_PASSED.put("getAllPersonsWithDetailsPaginated_Test", false);
        TESTS_PASSED.put("getPersonWithProfessions_Test", false);
        TESTS_PASSED.put("getPersonWithDetails_Test", false);
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

    @Test
    @Order(1)
    @DisplayName("Tests GET /api/persons")
    void getAllPersonsPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getPersonsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getPersonsRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getPersonsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getPersonJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllPersonsPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/persons/details")
    void getAllPersonsWithDetailsPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getPersonsDetailsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getPersonsDetailsRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getPersonsDetailsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getPersonJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllPersonsWithDetailsPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /api/persons/{id}")
    void getPersonWithProfessions_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getPersonRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getPersonRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getPersonRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getPersonJson(reqres.getL()));
        }

        TESTS_PASSED.put("getPersonWithProfessions_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests GET /api/persons/{id}/details")
    void getPersonWithDetails_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getPersonDetailsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getPersonDetailsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getPersonDetailsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getPersonJson(reqres.getL()));
        }

        TESTS_PASSED.put("getPersonWithDetails_Test", true);
    }

//=================================================================================================================================
//PRIVATE METHODS
//=================================================================================================================================
//persons
    private String[] getPersonsRequestInvalidResponse400() {
        return new String[]{
            "?page=0&size=1",
            "?page=-1&size=1",
            "?page=1&size=0",
            "?page=1&size=-1",
            "?page=1&size=101",
            "?page=1&size=102",
            "?page=1&size=201"
        };
    }

    private String[] getPersonsRequestValidResponseEmpty200() {
        return new String[]{
            "?page=6&size=10",
            "?page=2&size=100"
        };
    }

    private Pair<Integer, String>[] getPersonsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(1, ""),
            new Pair(1, "?page=1&size=10"),
            new Pair(2, "?page=2&size=10"),
            new Pair(3, "?page=3&size=10"),
            new Pair(4, "?page=5&size=10"),
            new Pair(5, "?page=1&size=5"),
            new Pair(6, "?page=2&size=5"),
            new Pair(7, "?page=1&size=1"),
            new Pair(8, "?page=10&size=1"),
            new Pair(9, "?page=20&size=1")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//persons/details
    private String[] getPersonsDetailsRequestInvalidResponse400() {
        return new String[]{
            "/details?page=0&size=1",
            "/details?page=-1&size=1",
            "/details?page=1&size=0",
            "/details?page=1&size=-1",
            "/details?page=1&size=101",
            "/details?page=1&size=102",
            "/details?page=1&size=201"
        };
    }

    private String[] getPersonsDetailsRequestValidResponseEmpty200() {
        return new String[]{
            "/details?page=6&size=10",
            "/details?page=2&size=100"
        };
    }

    private Pair<Integer, String>[] getPersonsDetailsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(10, "/details"),
            new Pair(10, "/details?page=1&size=10"),
            new Pair(11, "/details?page=2&size=10"),
            new Pair(12, "/details?page=3&size=10"),
            new Pair(13, "/details?page=5&size=10"),
            new Pair(14, "/details?page=1&size=5"),
            new Pair(15, "/details?page=2&size=5"),
            new Pair(16, "/details?page=1&size=1"),
            new Pair(17, "/details?page=10&size=1"),
            new Pair(18, "/details?page=20&size=1")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//persons/{id}
    private String[] getPersonRequestInvalidResponse400() {
        return new String[]{
            "/0",
            "/-1"
        };
    }

    private String[] getPersonRequestValidResponse404() {
        return new String[]{
            "/51",
            "/52",
            "/70",
            "/100"
        };
    }

    private Pair<Integer, String>[] getPersonRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(19, "/1"),
            new Pair(20, "/2"),
            new Pair(21, "/10"),
            new Pair(22, "/20")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//persons/{id}/details
    private String[] getPersonDetailsRequestInvalidResponse400() {
        return new String[]{
            "/0/details",
            "/-1/details"
        };
    }

    private String[] getPersonDetailsRequestValidResponse404() {
        return new String[]{
            "/51/details",
            "/52/details",
            "/70/details",
            "/100/details"
        };
    }

    private Pair<Integer, String>[] getPersonDetailsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(23, "/1/details"),
            new Pair(24, "/2/details"),
            new Pair(25, "/10/details"),
            new Pair(26, "/20/details")
        };
    }
}
