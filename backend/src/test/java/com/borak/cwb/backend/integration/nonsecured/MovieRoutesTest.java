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
public class MovieRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/movies";

    static {
        TESTS_PASSED.put("getAllMoviesWithGenresPaginated_Test", false);
        TESTS_PASSED.put("getAllMoviesWithGenresPopularPaginated_Test", false);
        TESTS_PASSED.put("getAllMoviesWithGenresCurrentPaginated_Test", false);
        TESTS_PASSED.put("getAllMoviesWithDetailsPaginated_Test", false);
        TESTS_PASSED.put("getMovieWithGenres_Test", false);
        TESTS_PASSED.put("getMovieWithDetails_Test", false);
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
    @DisplayName("Tests GET /api/movies")
    void getAllMoviesWithGenresPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMoviesRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMoviesRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMoviesRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllMoviesWithGenresPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/movies/popular")
    void getAllMoviesWithGenresPopularPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMoviesPopularRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMoviesPopularRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }

        TESTS_PASSED.put("getAllMoviesWithGenresPopularPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /api/movies/current")
    void getAllMoviesWithGenresCurrentPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMoviesCurrentRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMoviesCurrentRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }

        TESTS_PASSED.put("getAllMoviesWithGenresCurrentPaginated_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests GET /api/movies/details")
    void getAllMoviesWithDetailsPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMoviesDetailsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMoviesDetailsRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMoviesDetailsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllMoviesWithDetailsPaginated_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests GET /api/movies/{id}")
    void getMovieWithGenres_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMovieRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMovieRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMovieRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getMovieWithGenres_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests GET /api/movies/{id}/details")
    void getMovieWithDetails_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMovieDetailsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMovieDetailsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMovieDetailsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getMovieWithDetails_Test", true);
    }

//=================================================================================================================================
//PRIVATE METHODS
//=================================================================================================================================
///movies  
    private String[] getMoviesRequestInvalidResponse400() {
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

    private String[] getMoviesRequestValidResponseEmpty200() {
        return new String[]{
            "?page=2&size=10",
            "?page=3&size=10"
        };
    }

    private Pair<Integer, String>[] getMoviesRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(1, ""),
            new Pair(1, "?page=1&size=10"),
            new Pair(2, "?page=1&size=2"),
            new Pair(3, "?page=2&size=2"),
            new Pair(4, "?page=1&size=1"),
            new Pair(5, "?page=2&size=1"),
            new Pair(6, "?page=3&size=1")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/popular
    private String[] getMoviesPopularRequestInvalidResponse400() {
        return new String[]{
            "/popular?page=0&size=1",
            "/popular?page=-1&size=1",
            "/popular?page=1&size=0",
            "/popular?page=1&size=-1",
            "/popular?page=1&size=101",
            "/popular?page=1&size=102",
            "/popular?page=1&size=201"
        };
    }

    private String[] getMoviesPopularRequestValidResponseEmpty200() {
        return new String[]{
            "/popular",
            "/popular?page=1&size=10",
            "/popular?page=2&size=10",
            "/popular?page=3&size=10",
            "/popular?page=1&size=2",
            "/popular?page=2&size=2",
            "/popular?page=1&size=1",
            "/popular?page=2&size=1",
            "/popular?page=3&size=1"
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/current
    private String[] getMoviesCurrentRequestInvalidResponse400() {
        return new String[]{
            "/current?page=0&size=1",
            "/current?page=-1&size=1",
            "/current?page=1&size=0",
            "/current?page=1&size=-1",
            "/current?page=1&size=101",
            "/current?page=1&size=102",
            "/current?page=1&size=201"
        };
    }

    private String[] getMoviesCurrentRequestValidResponseEmpty200() {
        return new String[]{
            "/current",
            "/current?page=1&size=10",
            "/current?page=2&size=10",
            "/current?page=3&size=10",
            "/current?page=1&size=2",
            "/current?page=2&size=2",
            "/current?page=1&size=1",
            "/current?page=2&size=1",
            "/current?page=3&size=1"
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/details
    private String[] getMoviesDetailsRequestInvalidResponse400() {
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

    private String[] getMoviesDetailsRequestValidResponseEmpty200() {
        return new String[]{
            "/details?page=2&size=10",
            "/details?page=3&size=10"
        };
    }

    private Pair<Integer, String>[] getMoviesDetailsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(7, "/details"),
            new Pair(7, "/details?page=1&size=10"),
            new Pair(8, "/details?page=1&size=2"),
            new Pair(9, "/details?page=2&size=2"),
            new Pair(10, "/details?page=1&size=1"),
            new Pair(11, "/details?page=2&size=1"),
            new Pair(12, "/details?page=3&size=1")
        };
    }
//---------------------------------------------------------------------------------------------------------------------------------
//movies/{id}

    private String[] getMovieRequestInvalidResponse400() {
        return new String[]{
            "/0",
            "/-1"
        };
    }

    private String[] getMovieRequestValidResponse404() {
        return new String[]{
            "/3",
            "/5",
            "/6",
            "/20"
        };
    }

    private Pair<Integer, String>[] getMovieRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(13, "/1"),
            new Pair(14, "/2"),
            new Pair(15, "/4")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/{id}/details
    private String[] getMovieDetailsRequestInvalidResponse400() {
        return new String[]{
            "/0/details",
            "/-1/details"
        };
    }

    private String[] getMovieDetailsRequestValidResponse404() {
        return new String[]{
            "/3/details",
            "/5/details",
            "/6/details",
            "/20/details"
        };
    }

    private Pair<Integer, String>[] getMovieDetailsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(16, "/1/details"),
            new Pair(17, "/2/details"),
            new Pair(18, "/4/details")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
}
