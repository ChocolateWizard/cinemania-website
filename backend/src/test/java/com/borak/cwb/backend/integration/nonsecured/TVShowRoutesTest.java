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

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/tv";

    static {
        TESTS_PASSED.put("getAllTVShowsWithGenresPaginated_Test", false);
        TESTS_PASSED.put("getAllTVShowsWithGenresPopularPaginated_Test", false);
        TESTS_PASSED.put("getAllTVShowsWithGenresCurrentPaginated_Test", false);
        TESTS_PASSED.put("getAllTVShowsWithDetailsPaginated_Test", false);
        TESTS_PASSED.put("getTVShowWithGenres_Test", false);
        TESTS_PASSED.put("getTVShowWithDetails_Test", false);
        TESTS_PASSED.put("getTVShowDirectors_Test", false);
        TESTS_PASSED.put("getTVShowWriters_Test", false);
        TESTS_PASSED.put("getTVShowActors_Test", false);
        TESTS_PASSED.put("getTVShowActorsWithRoles_Test", false);
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
    @DisplayName("Tests GET /api/tv")
    void getAllTVShowsWithGenresPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowsRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllTVShowsWithGenresPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/tv/popular")
    void getAllTVShowsWithGenresPopularPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowsPopularRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowsPopularRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowsPopularRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllTVShowsWithGenresPopularPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests GET /api/tv/current")
    void getAllTVShowsWithGenresCurrentPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowsCurrentRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowsCurrentRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }

        TESTS_PASSED.put("getAllTVShowsWithGenresCurrentPaginated_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests GET /api/tv/details")
    void getAllTVShowsWithDetailsPaginated_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowsDetailsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowsDetailsRequestValidResponseEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, req).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, req).isEqualTo("[]");
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowsDetailsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getAllTVShowsWithDetailsPaginated_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests GET /api/tv/{id}")
    void getTVShowWithGenres_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getTVShowWithGenres_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests GET /api/tv/{id}/details")
    void getTVShowWithDetails_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowDetailsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowDetailsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowDetailsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getTVShowWithDetails_Test", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests GET /api/tv/{id}/directors")
    void getTVShowDirectors_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowDirectorsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowDirectorsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowDirectorsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getTVShowDirectors_Test", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests GET /api/tv/{id}/writers")
    void getTVShowWriters_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowWritersRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowWritersRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowWritersRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getTVShowWriters_Test", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests GET /api/tv/{id}/actors")
    void getTVShowActors_Test() {

        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowActorsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowActorsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowActorsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getTVShowActors_Test", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests GET /api/tv/{id}/actors/roles")
    void getTVShowActorsWithRoles_Test() {

        ResponseEntity<String> response;
        int i = 0;
        for (String req : getTVShowActorsRolesRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getTVShowActorsRolesRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getTVShowActorsRolesRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getTVShowJson(reqres.getL()));
        }

        TESTS_PASSED.put("getTVShowActorsWithRoles_Test", true);
    }

//=================================================================================================================================
//PRIVATE METHODS
//=================================================================================================================================
//tv
    private String[] getTVShowsRequestInvalidResponse400() {
        return new String[]{
            "?page=0&size=1",
            "?page=-1&size=1",
            "?page=1&size=0",
            "?page=1&size=-1"
        };
    }

    private String[] getTVShowsRequestValidResponseEmpty200() {
        return new String[]{
            "?page=2&size=10",
            "?page=3&size=10"
        };
    }

    private Pair<Integer, String>[] getTVShowsRequestValidResponseNonEmpty200() {
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
//tv/popular
    private String[] getTVShowsPopularRequestInvalidResponse400() {
        return new String[]{
            "/popular?page=0&size=1",
            "/popular?page=-1&size=1",
            "/popular?page=1&size=0",
            "/popular?page=1&size=-1"
        };
    }

    private String[] getTVShowsPopularRequestValidResponseEmpty200() {
        return new String[]{
            "/popular?page=2&size=10",
            "/popular?page=3&size=10"
        };
    }

    private Pair<Integer, String>[] getTVShowsPopularRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(7, "/popular"),
            new Pair(7, "/popular?page=1&size=10"),
            new Pair(8, "/popular?page=1&size=2"),
            new Pair(9, "/popular?page=2&size=2"),
            new Pair(10, "/popular?page=1&size=1"),
            new Pair(11, "/popular?page=2&size=1"),
            new Pair(12, "/popular?page=3&size=1")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//tv/current
    private String[] getTVShowsCurrentRequestInvalidResponse400() {
        return new String[]{
            "/current?page=0&size=1",
            "/current?page=-1&size=1",
            "/current?page=1&size=0",
            "/current?page=1&size=-1"
        };
    }

    private String[] getTVShowsCurrentRequestValidResponseEmpty200() {
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
//tv/details  
    private String[] getTVShowsDetailsRequestInvalidResponse400() {
        return new String[]{
            "/details?page=0&size=1",
            "/details?page=-1&size=1",
            "/details?page=1&size=0",
            "/details?page=1&size=-1"
        };
    }

    private String[] getTVShowsDetailsRequestValidResponseEmpty200() {
        return new String[]{
            "/details?page=2&size=10",
            "/details?page=3&size=10"
        };
    }

    private Pair<Integer, String>[] getTVShowsDetailsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(13, "/details"),
            new Pair(13, "/details?page=1&size=10"),
            new Pair(14, "/details?page=1&size=2"),
            new Pair(15, "/details?page=2&size=2"),
            new Pair(16, "/details?page=1&size=1"),
            new Pair(17, "/details?page=2&size=1"),
            new Pair(18, "/details?page=3&size=1")
        };
    }
//---------------------------------------------------------------------------------------------------------------------------------
//tv/{id}    

    private String[] getTVShowRequestInvalidResponse400() {
        return new String[]{
            "/0",
            "/-1"
        };
    }

    private String[] getTVShowRequestValidResponse404() {
        return new String[]{
            "/1",
            "/2",
            "/4",
            "/20"
        };
    }

    private Pair<Integer, String>[] getTVShowRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(19, "/3"),
            new Pair(20, "/5"),
            new Pair(21, "/6")
        };
    }
//---------------------------------------------------------------------------------------------------------------------------------
//tv/{id}/details   

    private String[] getTVShowDetailsRequestInvalidResponse400() {
        return new String[]{
            "/0/details",
            "/-1/details"
        };
    }

    private String[] getTVShowDetailsRequestValidResponse404() {
        return new String[]{
            "/1/details",
            "/2/details",
            "/4/details",
            "/20/details"
        };
    }

    private Pair<Integer, String>[] getTVShowDetailsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(22, "/3/details"),
            new Pair(23, "/5/details"),
            new Pair(24, "/6/details")
        };
    }
//---------------------------------------------------------------------------------------------------------------------------------
//tv/{id}/directors   

    private String[] getTVShowDirectorsRequestInvalidResponse400() {
        return new String[]{
            "/0/directors",
            "/-1/directors"
        };
    }

    private String[] getTVShowDirectorsRequestValidResponse404() {
        return new String[]{
            "/1/directors",
            "/2/directors",
            "/4/directors",
            "/20/directors"
        };
    }

    private Pair<Integer, String>[] getTVShowDirectorsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(25, "/3/directors"),
            new Pair(26, "/5/directors"),
            new Pair(27, "/6/directors")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//tv/{id}/writers   
    private String[] getTVShowWritersRequestInvalidResponse400() {
        return new String[]{
            "/0/writers",
            "/-1/writers"
        };
    }

    private String[] getTVShowWritersRequestValidResponse404() {
        return new String[]{
            "/1/writers",
            "/2/writers",
            "/4/writers",
            "/20/writers"
        };
    }

    private Pair<Integer, String>[] getTVShowWritersRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(28, "/3/writers"),
            new Pair(29, "/5/writers"),
            new Pair(30, "/6/writers")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//tv/{id}/actors
    private String[] getTVShowActorsRequestInvalidResponse400() {
        return new String[]{
            "/0/actors",
            "/-1/actors"
        };
    }

    private String[] getTVShowActorsRequestValidResponse404() {
        return new String[]{
            "/1/actors",
            "/2/actors",
            "/4/actors",
            "/20/actors"
        };
    }

    private Pair<Integer, String>[] getTVShowActorsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(31, "/3/actors"),
            new Pair(32, "/5/actors"),
            new Pair(33, "/6/actors")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//tv/{id}/actors/roles   
    private String[] getTVShowActorsRolesRequestInvalidResponse400() {
        return new String[]{
            "/0/actors/roles",
            "/-1/actors/roles"
        };
    }

    private String[] getTVShowActorsRolesRequestValidResponse404() {
        return new String[]{
            "/1/actors/roles",
            "/2/actors/roles",
            "/4/actors/roles",
            "/20/actors/roles"
        };
    }

    private Pair<Integer, String>[] getTVShowActorsRolesRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(34, "/3/actors/roles"),
            new Pair(35, "/5/actors/roles"),
            new Pair(36, "/6/actors/roles")
        };
    }

}
