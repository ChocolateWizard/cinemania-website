/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.CritiqueRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.UserRepositoryJDBC;
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
public class UserSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepositoryJDBC userRepo;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/users/library";

    static {
        testsPassed.put("postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401", false);
        testsPassed.put("postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400", false);
        testsPassed.put("postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404", false);
        testsPassed.put("postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns200", false);

        testsPassed.put("deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401", false);
        testsPassed.put("deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400", false);
        testsPassed.put("deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404", false);
        testsPassed.put("deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns200", false);
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
    @DisplayName("Tests whether POST request to /api/users/library with unauthenticated user did not create new media in library and it returned 401")
    void postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401() {

        testsPassed.put("postMediaToLibrary_UnauthenticatedUser_DoesNotCreateMediaInLibraryReturns401", true);
    }

    @Test
    @Order(2)
    @Disabled
    @DisplayName("Tests whether POST request to /api/users/library with invalid input data did not create new media in library and it returned 400")
    void postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400() {

        testsPassed.put("postMediaToLibrary_InvalidInputData_DoesNotCreateMediaInLibraryReturns400", true);
    }

    @Test
    @Order(3)
    @Disabled
    @DisplayName("Tests whether POST request to /api/users/library with non-existent dependency objects did not create new media in library and it returned 404")
    void postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404() {

        testsPassed.put("postMediaToLibrary_NonexistentDependencyData_DoesNotCreateMediaInLibraryReturns404", true);
    }

    @Test
    @Order(4)
    @Disabled
    @DisplayName("Tests whether POST request to /api/users/library with valid input data did create new media in library and it returned 200")
    void postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns200() {

        testsPassed.put("postMediaToLibrary_ValidInput_CreatesMediaInLibraryReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(5)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/users/library with unauthenticated user did not delete media from library and it returned 401")
    void deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401() {

        testsPassed.put("deleteMediaFromLibrary_UnauthenticatedUser_DoesNotDeleteMediaFromLibraryReturns401", true);
    }

    @Test
    @Order(6)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/users/library with invalid input data did not delete media from library and it returned 400")
    void deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400() {

        testsPassed.put("deleteMediaFromLibrary_InvalidInputData_DoesNotDeleteMediaFromLibraryReturns400", true);
    }

    @Test
    @Order(7)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/users/library with non-existent dependency objects did not delete media from library and it returned 404")
    void deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404() {

        testsPassed.put("deleteMediaFromLibrary_NonexistentDependencyData_DoesNotDeleteMediaFromLibraryReturns404", true);
    }

    @Test
    @Order(8)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/users/library with valid input data did delete media from library and it returned 200")
    void deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns200() {

        testsPassed.put("deleteMediaFromLibrary_ValidInput_DeletesMediaFromLibraryReturns200", true);
    }

}
