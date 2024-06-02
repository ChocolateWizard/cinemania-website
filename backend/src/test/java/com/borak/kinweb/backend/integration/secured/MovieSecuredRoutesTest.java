/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.MovieRepositoryJDBC;
import com.borak.kinweb.backend.repository.util.FileRepository;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(7)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MovieRepositoryJDBC movieRepo;
    @Autowired
    private FileRepository fileRepo;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/movies";

    static {
        testsPassed.put("postMovie_UnauthenticatedUser_DoesNotCreateMovieReturns401", false);
        testsPassed.put("postMovie_UnauthorizedUser_DoesNotCreateMovieReturns403", false);
        testsPassed.put("postMovie_InvalidInputData_DoesNotCreateMovieReturns400", false);
        testsPassed.put("postMovie_NonexistentDependencyData_DoesNotCreateMovieReturns404", false);
        testsPassed.put("postMovie_ValidInput_CreatesMovieReturns200", false);

        testsPassed.put("putMovie_UnauthenticatedUser_DoesNotUpdateMovieReturns401", false);
        testsPassed.put("putMovie_UnauthorizedUser_DoesNotUpdateMovieReturns403", false);
        testsPassed.put("putMovie_InvalidInputData_DoesNotUpdateMovieReturns400", false);
        testsPassed.put("putMovie_NonexistentDependencyData_DoesNotUpdateMovieReturns404", false);
        testsPassed.put("putMovie_ValidInput_UpdatesMovieReturns200", false);

        testsPassed.put("deleteMovie_UnauthenticatedUser_DoesNotDeleteMovieReturns401", false);
        testsPassed.put("deleteMovie_UnauthorizedUser_DoesNotDeleteMovieReturns403", false);
        testsPassed.put("deleteMovie_InvalidInputData_DoesNotDeleteMovieReturns400", false);
        testsPassed.put("deleteMovie_NonexistentDependencyData_DoesNotDeleteMovieReturns404", false);
        testsPassed.put("deleteMovie_ValidInput_DeletesMovieReturns200", false);
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
        Assumptions.assumeTrue(TestResultsHelper.didAuthRoutesTestPass());
    }

    //=========================================================================================================
    //POST
    @Test
    @Order(1)
    @Disabled
    @DisplayName("Tests whether POST request to /api/movies with unauthenticated user did not create new movie and it returned 401")
    void postMovie_UnauthenticatedUser_DoesNotCreateMovieReturns401() {

        testsPassed.put("postMovie_UnauthenticatedUser_DoesNotCreateMovieReturns401", true);
    }

    @Test
    @Order(2)
    @Disabled
    @DisplayName("Tests whether POST request to /api/movies with unauthorized but authenticated user did not create new movie and it returned 403")
    void postMovie_UnauthorizedUser_DoesNotCreateMovieReturns403() {

        testsPassed.put("postMovie_UnauthorizedUser_DoesNotCreateMovieReturns403", true);
    }

    @Test
    @Order(3)
    @Disabled
    @DisplayName("Tests whether POST request to /api/movies with invalid movie data did not create new movie and it returned 400")
    void postMovie_InvalidInputData_DoesNotCreateMovieReturns400() {

        testsPassed.put("postMovie_InvalidInputData_DoesNotCreateMovieReturns400", true);
    }

    @Test
    @Order(4)
    @Disabled
    @DisplayName("Tests whether POST request to /api/movies with non-existent dependency objects did not create new movie and it returned 404")
    void postMovie_NonexistentDependencyData_DoesNotCreateMovieReturns404() {

        testsPassed.put("postMovie_NonexistentDependencyData_DoesNotCreateMovieReturns404", true);
    }

    @Test
    @Order(5)
    @Disabled
    @DisplayName("Tests whether POST request to /api/movies with valid input did create new movie and it returned 200")
    void postMovie_ValidInput_CreatesMovieReturns200() {

        testsPassed.put("postMovie_ValidInput_CreatesMovieReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(6)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/movies with unauthenticated user did not update movie and it returned 401")
    void putMovie_UnauthenticatedUser_DoesNotUpdateMovieReturns401() {

        testsPassed.put("putMovie_UnauthenticatedUser_DoesNotUpdateMovieReturns401", true);
    }

    @Test
    @Order(7)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/movies with unauthorized but authenticated user did not update movie and it returned 403")
    void putMovie_UnauthorizedUser_DoesNotUpdateMovieReturns403() {

        testsPassed.put("putMovie_UnauthorizedUser_DoesNotUpdateMovieReturns403", true);
    }

    @Test
    @Order(8)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/movies with invalid movie data did not update movie and it returned 400")
    void putMovie_InvalidInputData_DoesNotUpdateMovieReturns400() {

        testsPassed.put("putMovie_InvalidInputData_DoesNotUpdateMovieReturns400", true);
    }

    @Test
    @Order(9)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/movies with non-existent dependency objects did not update movie and it returned 404")
    void putMovie_NonexistentDependencyData_DoesNotUpdateMovieReturns404() {

        testsPassed.put("putMovie_NonexistentDependencyData_DoesNotUpdateMovieReturns404", true);
    }

    @Test
    @Order(10)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/movies with valid movie data did update movie and it returned 200")
    void putMovie_ValidInput_UpdatesMovieReturns200() {

        testsPassed.put("putMovie_ValidInput_UpdatesMovieReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(11)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/movies with unauthenticated user did not delete movie and it returned 401")
    void deleteMovie_UnauthenticatedUser_DoesNotDeleteMovieReturns401() {

        testsPassed.put("deleteMovie_UnauthenticatedUser_DoesNotDeleteMovieReturns401", true);
    }

    @Test
    @Order(12)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/movies with unauthorized but authenticated user did not delete movie and it returned 403")
    void deleteMovie_UnauthorizedUser_DoesNotDeleteMovieReturns403() {

        testsPassed.put("deleteMovie_UnauthorizedUser_DoesNotDeleteMovieReturns403", true);
    }

    @Test
    @Order(13)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/movies with invalid input data did not delete movie and it returned 400")
    void deleteMovie_InvalidInputData_DoesNotDeleteMovieReturns400() {

        testsPassed.put("deleteMovie_InvalidInputData_DoesNotDeleteMovieReturns400", true);
    }

    @Test
    @Order(14)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/movies with non-existent movie did not delete movie and it returned 404")
    void deleteMovie_NonexistentDependencyData_DoesNotDeleteMovieReturns404() {

        testsPassed.put("deleteMovie_NonexistentDependencyData_DoesNotDeleteMovieReturns404", true);
    }

    @Test
    @Order(15)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/movies with valid movie data did delete movie and it returned 200")
    void deleteMovie_ValidInput_DeletesMovieReturns200() {

        testsPassed.put("deleteMovie_ValidInput_DeletesMovieReturns200", true);
    }

}
