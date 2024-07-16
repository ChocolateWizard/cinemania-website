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
        TESTS_PASSED.put("getMovieDirectors_Test", false);
        TESTS_PASSED.put("getMovieWriters_Test", false);
        TESTS_PASSED.put("getMovieActors_Test", false);
        TESTS_PASSED.put("getMovieActorsWithRoles_Test", false);
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

    @Test
    @Order(7)
    @DisplayName("Tests GET /api/movies/{id}/directors")
    void getMovieDirectors_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMovieDirectorsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMovieDirectorsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMovieDirectorsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getMovieDirectors_Test", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests GET /api/movies/{id}/writers")
    void getMovieWriters_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMovieWritersRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMovieWritersRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMovieWritersRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getMovieWriters_Test", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests GET /api/movies/{id}/actors")
    void getMovieActors_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMovieActorsRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMovieActorsRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMovieActorsRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getMovieActors_Test", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests GET /api/movies/{id}/actors/roles")
    void getMovieActorsWithRoles_Test() {
        ResponseEntity<String> response;
        int i = 0;
        for (String req : getMovieActorsRolesRequestInvalidResponse400()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.BAD_REQUEST);
        }
        i = 0;
        for (String req : getMovieActorsRolesRequestValidResponse404()) {
            response = restTemplate.getForEntity(ROUTE + req, String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i++, req).isEqualTo(HttpStatus.NOT_FOUND);
        }
        i = 0;
        for (Pair<Integer, String> reqres : getMovieActorsRolesRequestValidResponseNonEmpty200()) {
            response = restTemplate.getForEntity(ROUTE + reqres.getR(), String.class);
            assertThat(response.getStatusCode()).as("Value i=%d, and url=%s", i, reqres.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Value i=%d, and url=%s", i++, reqres.getR()).isEqualTo(jsonReader.getMovieJson(reqres.getL()));
        }

        TESTS_PASSED.put("getMovieActorsWithRoles_Test", true);
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
            "?page=1&size=-1"
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
            "/popular?page=1&size=-1"
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
            "/current?page=1&size=-1"
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
            "/details?page=1&size=-1"
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
//movies/{id}/directors
    private String[] getMovieDirectorsRequestInvalidResponse400() {
        return new String[]{
            "/0/directors",
            "/-1/directors"
        };
    }

    private String[] getMovieDirectorsRequestValidResponse404() {
        return new String[]{
            "/3/directors",
            "/5/directors",
            "/6/directors",
            "/20/directors"
        };
    }

    private Pair<Integer, String>[] getMovieDirectorsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(19, "/1/directors"),
            new Pair(20, "/2/directors"),
            new Pair(21, "/4/directors")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/{id}/writers
    private String[] getMovieWritersRequestInvalidResponse400() {
        return new String[]{
            "/0/writers",
            "/-1/writers"
        };
    }

    private String[] getMovieWritersRequestValidResponse404() {
        return new String[]{
            "/3/writers",
            "/5/writers",
            "/6/writers",
            "/20/writers"
        };
    }

    private Pair<Integer, String>[] getMovieWritersRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(22, "/1/writers"),
            new Pair(23, "/2/writers"),
            new Pair(24, "/4/writers")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/{id}/actors
    private String[] getMovieActorsRequestInvalidResponse400() {
        return new String[]{
            "/0/actors",
            "/-1/actors"
        };
    }

    private String[] getMovieActorsRequestValidResponse404() {
        return new String[]{
            "/3/actors",
            "/5/actors",
            "/6/actors",
            "/20/actors"
        };
    }

    private Pair<Integer, String>[] getMovieActorsRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(25, "/1/actors"),
            new Pair(26, "/2/actors"),
            new Pair(27, "/4/actors")
        };
    }

//---------------------------------------------------------------------------------------------------------------------------------
//movies/{id}/actors/roles
    private String[] getMovieActorsRolesRequestInvalidResponse400() {
        return new String[]{
            "/0/actors/roles",
            "/-1/actors/roles"
        };
    }

    private String[] getMovieActorsRolesRequestValidResponse404() {
        return new String[]{
            "/3/actors/roles",
            "/5/actors/roles",
            "/6/actors/roles",
            "/20/actors/roles"
        };
    }

    private Pair<Integer, String>[] getMovieActorsRolesRequestValidResponseNonEmpty200() {
        return new Pair[]{
            new Pair(28, "/1/actors/roles"),
            new Pair(29, "/2/actors/roles"),
            new Pair(30, "/4/actors/roles")
        };
    }

}
