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
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(5)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/movies";

    static {
        testsPassed.put("getAllMoviesWithGenresPaginated_Test", false);
        testsPassed.put("getAllMoviesWithGenresPopularPaginated_Test", false);
        testsPassed.put("getAllMoviesWithGenresCurrentPaginated_Test", false);
        testsPassed.put("getAllMoviesWithDetailsPaginated_Test", false);
        testsPassed.put("getMovieWithGenres_Test", false);
        testsPassed.put("getMovieWithDetails_Test", false);
        testsPassed.put("getMovieDirectors_Test", false);
        testsPassed.put("getMovieWriters_Test", false);
        testsPassed.put("getMovieActors_Test", false);
        testsPassed.put("getMovieActorsWithRoles_Test", false);
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
    @DisplayName("Tests GET /api/movies")
    void getAllMoviesWithGenresPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(1));

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(1));

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
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(2));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(3));

        response
                = restTemplate.getForEntity(ROUTE + "?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(4));

        response
                = restTemplate.getForEntity(ROUTE + "?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(5));

        response
                = restTemplate.getForEntity(ROUTE + "?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(6));

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

        testsPassed.put("getAllMoviesWithGenresPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/movies/popular")
    void getAllMoviesWithGenresPopularPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/popular", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

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
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

        response
                = restTemplate.getForEntity(ROUTE + "/popular?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");

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

        testsPassed.put("getAllMoviesWithGenresPopularPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /api/movies/current")
    void getAllMoviesWithGenresCurrentPaginated_Test() {
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

        testsPassed.put("getAllMoviesWithGenresCurrentPaginated_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests GET /api/movies/details")
    void getAllMoviesWithDetailsPaginated_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(7));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=10", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(7));

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
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(8));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(9));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=1&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(10));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=2&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(11));

        response
                = restTemplate.getForEntity(ROUTE + "/details?page=3&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(12));

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

        testsPassed.put("getAllMoviesWithDetailsPaginated_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests GET /api/movies/{id}")
    void getMovieWithGenres_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(13));

        response
                = restTemplate.getForEntity(ROUTE + "/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(14));

        response
                = restTemplate.getForEntity(ROUTE + "/4", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(15));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/3", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/6", String.class);
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

        testsPassed.put("getMovieWithGenres_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests GET /api/movies/{id}/details")
    void getMovieWithDetails_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(16));

        response
                = restTemplate.getForEntity(ROUTE + "/2/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(17));

        response
                = restTemplate.getForEntity(ROUTE + "/4/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(18));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/3/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/5/details", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/6/details", String.class);
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

        testsPassed.put("getMovieWithDetails_Test", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests GET /api/movies/{id}/directors")
    void getMovieDirectors_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(19));

        response
                = restTemplate.getForEntity(ROUTE + "/2/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(20));

        response
                = restTemplate.getForEntity(ROUTE + "/4/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(21));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/3/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/5/directors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/6/directors", String.class);
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

        testsPassed.put("getMovieDirectors_Test", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests GET /api/movies/{id}/writers")
    void getMovieWriters_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(22));

        response
                = restTemplate.getForEntity(ROUTE + "/2/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(23));

        response
                = restTemplate.getForEntity(ROUTE + "/4/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(24));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/3/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/5/writers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/6/writers", String.class);
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

        testsPassed.put("getMovieWriters_Test", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests GET /api/movies/{id}/actors")
    void getMovieActors_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(25));

        response
                = restTemplate.getForEntity(ROUTE + "/2/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(26));

        response
                = restTemplate.getForEntity(ROUTE + "/4/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(27));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/3/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/5/actors", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/6/actors", String.class);
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

        testsPassed.put("getMovieActors_Test", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests GET /api/movies/{id}/actors/roles")
    void getMovieActorsWithRoles_Test() {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ROUTE + "/1/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(28));

        response
                = restTemplate.getForEntity(ROUTE + "/2/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(29));

        response
                = restTemplate.getForEntity(ROUTE + "/4/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jsonReader.getMovieJson(30));

        //not found
        response
                = restTemplate.getForEntity(ROUTE + "/3/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/5/actors/roles", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        response
                = restTemplate.getForEntity(ROUTE + "/6/actors/roles", String.class);
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

        testsPassed.put("getMovieActorsWithRoles_Test", true);
    }

}
