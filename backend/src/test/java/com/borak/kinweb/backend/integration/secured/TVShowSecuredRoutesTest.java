/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.TVShowRepositoryJDBC;
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
public class TVShowSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TVShowRepositoryJDBC tvShowRepo;
    @Autowired
    private FileRepository fileRepo;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/tv";

    static {
        testsPassed.put("postTVShow_UnauthenticatedUser_DoesNotCreateTVShowReturns401", false);
        testsPassed.put("postTVShow_UnauthorizedUser_DoesNotCreateTVShowReturns403", false);
        testsPassed.put("postTVShow_InvalidInputData_DoesNotCreateTVShowReturns400", false);
        testsPassed.put("postTVShow_NonexistentDependencyData_DoesNotCreateTVShowReturns404", false);
        testsPassed.put("postTVShow_ValidInput_CreatesTVShowReturns200", false);

        testsPassed.put("putTVShow_UnauthenticatedUser_DoesNotUpdateTVShowReturns401", false);
        testsPassed.put("putTVShow_UnauthorizedUser_DoesNotUpdateTVShowReturns403", false);
        testsPassed.put("putTVShow_InvalidInputData_DoesNotUpdateTVShowReturns400", false);
        testsPassed.put("putTVShow_NonexistentDependencyData_DoesNotUpdateTVShowReturns404", false);
        testsPassed.put("putTVShow_ValidInput_UpdatesTVShowReturns200", false);

        testsPassed.put("deleteTVShow_UnauthenticatedUser_DoesNotDeleteTVShowReturns401", false);
        testsPassed.put("deleteTVShow_UnauthorizedUser_DoesNotDeleteTVShowReturns403", false);
        testsPassed.put("deleteTVShow_InvalidInputData_DoesNotDeleteTVShowReturns400", false);
        testsPassed.put("deleteTVShow_NonexistentDependencyData_DoesNotDeleteTVShowReturns404", false);
        testsPassed.put("deleteTVShow_ValidInput_DeletesTVShowReturns200", false);
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
    @DisplayName("Tests whether POST request to /api/tv with unauthenticated user did not create new tv show and it returned 401")
    void postTVShow_UnauthenticatedUser_DoesNotCreateTVShowReturns401() {

        testsPassed.put("postTVShow_UnauthenticatedUser_DoesNotCreateTVShowReturns401", true);
    }

    @Test
    @Order(2)
    @Disabled
    @DisplayName("Tests whether POST request to /api/tv with authenticated but not authorized user did not create new tv show and it returned 403")
    void postTVShow_UnauthorizedUser_DoesNotCreateTVShowReturns403() {

        testsPassed.put("postTVShow_UnauthorizedUser_DoesNotCreateTVShowReturns403", true);
    }

    @Test
    @Order(3)
    @Disabled
    @DisplayName("Tests whether POST request to /api/tv with invalid input data did not create new tv show and it returned 400")
    void postTVShow_InvalidInputData_DoesNotCreateTVShowReturns400() {

        testsPassed.put("postTVShow_InvalidInputData_DoesNotCreateTVShowReturns400", true);
    }

    @Test
    @Order(4)
    @Disabled
    @DisplayName("Tests whether POST request to /api/tv with non-existent dependency objects did not create new tv show and it returned 404")
    void postTVShow_NonexistentDependencyData_DoesNotCreateTVShowReturns404() {

        testsPassed.put("postTVShow_NonexistentDependencyData_DoesNotCreateTVShowReturns404", true);
    }

    @Test
    @Order(5)
    @Disabled
    @DisplayName("Tests whether POST request to /api/tv with valid input data did create new tv show and it returned 200")
    void postTVShow_ValidInput_CreatesTVShowReturns200() {

        testsPassed.put("postTVShow_ValidInput_CreatesTVShowReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(6)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/tv with unauthenticated user did not update tv show and it returned 401")
    void putTVShow_UnauthenticatedUser_DoesNotUpdateTVShowReturns401() {

        testsPassed.put("putTVShow_UnauthenticatedUser_DoesNotUpdateTVShowReturns401", true);
    }

    @Test
    @Order(7)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/tv with authenticated but unauthorized user did not update tv show and it returned 403")
    void putTVShow_UnauthorizedUser_DoesNotUpdateTVShowReturns403() {

        testsPassed.put("putTVShow_UnauthorizedUser_DoesNotUpdateTVShowReturns403", true);
    }

    @Test
    @Order(8)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/tv with invalid input data did not update tv show and it returned 400")
    void putTVShow_InvalidInputData_DoesNotUpdateTVShowReturns400() {

        testsPassed.put("putTVShow_InvalidInputData_DoesNotUpdateTVShowReturns400", true);
    }

    @Test
    @Order(9)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/tv with non-existent dependency objects did not update tv show and it returned 404")
    void putTVShow_NonexistentDependencyData_DoesNotUpdateTVShowReturns404() {

        testsPassed.put("putTVShow_NonexistentDependencyData_DoesNotUpdateTVShowReturns404", true);
    }

    @Test
    @Order(10)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/tv with valid input data did update tv show and it returned 200")
    void putTVShow_ValidInput_UpdatesTVShowReturns200() {

        testsPassed.put("putTVShow_ValidInput_UpdatesTVShowReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(11)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/tv with unauthenticated user did not delete tv show and it returned 401")
    void deleteTVShow_UnauthenticatedUser_DoesNotDeleteTVShowReturns401() {

        testsPassed.put("deleteTVShow_UnauthenticatedUser_DoesNotDeleteTVShowReturns401", true);
    }

    @Test
    @Order(12)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/tv with authenticated but unauthorized user did not delete tv show and it returned 403")
    void deleteTVShow_UnauthorizedUser_DoesNotDeleteTVShowReturns403() {

        testsPassed.put("deleteTVShow_UnauthorizedUser_DoesNotDeleteTVShowReturns403", true);
    }

    @Test
    @Order(13)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/tv with invalid input data did not delete tv show and it returned 400")
    void deleteTVShow_InvalidInputData_DoesNotDeleteTVShowReturns400() {

        testsPassed.put("deleteTVShow_InvalidInputData_DoesNotDeleteTVShowReturns400", true);
    }

    @Test
    @Order(14)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/tv with non-existent dependency objects did not delete tv show and it returned 404")
    void deleteTVShow_NonexistentDependencyData_DoesNotDeleteTVShowReturns404() {

        testsPassed.put("deleteTVShow_NonexistentDependencyData_DoesNotDeleteTVShowReturns404", true);
    }

    @Test
    @Order(15)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/tv with valid input data did delete tv show and it returned 200")
    void deleteTVShow_ValidInput_DeletesTVShowReturns200() {

        testsPassed.put("deleteTVShow_ValidInput_DeletesTVShowReturns200", true);
    }

}
