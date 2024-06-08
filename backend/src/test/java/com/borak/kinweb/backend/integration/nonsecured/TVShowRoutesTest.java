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
public class TVShowRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/tv";

    static {
        testsPassed.put("getAllTVShowsWithGenresPaginated_Test", false);
        testsPassed.put("getAllTVShowsWithGenresPopularPaginated_Test", false);
        testsPassed.put("getAllTVShowsWithGenresCurrentPaginated_Test", false);
        testsPassed.put("getAllTVShowsWithDetailsPaginated_Test", false);
        testsPassed.put("getTVShowWithGenres_Test", false);
        testsPassed.put("getTVShowWithDetails_Test", false);
        testsPassed.put("getTVShowDirectors_Test", false);
        testsPassed.put("getTVShowWriters_Test", false);
        testsPassed.put("getTVShowActors_Test", false);
        testsPassed.put("getTVShowActorsWithRoles_Test", false);
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
    @DisplayName("Tests GET /api/tv")
    void getAllTVShowsWithGenresPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "?page=3&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(2));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(3));

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(4));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(5));

        response
                = restTemplate.getForEntity(ROUTE + "?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(6));

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

        testsPassed.put("getAllTVShowsWithGenresPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/tv/popular")
    void getAllTVShowsWithGenresPopularPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/popular", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(7));

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(7));

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=2&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=3&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(8));

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(9));

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(10));

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(11));

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(12));

        //----------------------------------------------------------------------------
        //bad requests
        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=-1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getAllTVShowsWithGenresPopularPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /api/tv/current")
    void getAllTVShowsWithGenresCurrentPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/current", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=2&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=3&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=1&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        //----------------------------------------------------------------------------
        //bad requests
        response
                = restTemplate.getForEntity(ROUTE + "/current?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=-1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=1&size=0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/current?page=1&size=-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getAllTVShowsWithGenresCurrentPaginated_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests GET /api/tv/details")
    void getAllTVShowsWithDetailsPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(13));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(13));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=3&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(14));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(15));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(16));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(17));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(18));

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

        testsPassed.put("getAllTVShowsWithDetailsPaginated_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests GET /api/tv/{id}")
    void getTVShowWithGenres_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/3", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(19));

        response
                = restTemplate.getForEntity(ROUTE + "/5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(20));

        response
                = restTemplate.getForEntity(ROUTE + "/6", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(21));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/4", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/20", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getTVShowWithGenres_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests GET /api/tv/{id}/details")
    void getTVShowWithDetails_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/3/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(22));

        response
                = restTemplate.getForEntity(ROUTE + "/5/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(23));

        response
                = restTemplate.getForEntity(ROUTE + "/6/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(24));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/1/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/2/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/4/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/20/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getTVShowWithDetails_Test", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests GET /api/tv/{id}/directors")
    void getTVShowDirectors_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/3/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(25));

        response
                = restTemplate.getForEntity(ROUTE + "/5/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(26));

        response
                = restTemplate.getForEntity(ROUTE + "/6/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(27));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/1/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/2/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/4/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/20/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getTVShowDirectors_Test", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests GET /api/tv/{id}/writers")
    void getTVShowWriters_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/3/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(28));

        response
                = restTemplate.getForEntity(ROUTE + "/5/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(29));

        response
                = restTemplate.getForEntity(ROUTE + "/6/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(30));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/1/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/2/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/4/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/20/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getTVShowWriters_Test", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests GET /api/tv/{id}/actors")
    void getTVShowActors_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/3/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(31));

        response
                = restTemplate.getForEntity(ROUTE + "/5/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(32));

        response
                = restTemplate.getForEntity(ROUTE + "/6/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(33));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/1/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/2/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/4/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/20/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getTVShowActors_Test", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests GET /api/tv/{id}/actors/roles")
    void getTVShowActorsWithRoles_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/3/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(34));

        response
                = restTemplate.getForEntity(ROUTE + "/5/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(35));

        response
                = restTemplate.getForEntity(ROUTE + "/6/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getTVShowJson(36));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/1/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/2/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/4/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/20/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //bad request
        response
                = restTemplate.getForEntity(ROUTE + "/0/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        response
                = restTemplate.getForEntity(ROUTE + "/-1/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        testsPassed.put("getTVShowActorsWithRoles_Test", true);
    }

}
