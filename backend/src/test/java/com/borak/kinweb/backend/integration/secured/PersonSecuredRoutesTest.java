/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.PersonWrapperRepositoryJDBC;
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
public class PersonSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersonWrapperRepositoryJDBC personRepo;
    @Autowired
    private FileRepository fileRepo;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/persons";

    static {
        testsPassed.put("postPerson_UnauthenticatedUser_DoesNotCreatePersonReturns401", false);
        testsPassed.put("postPerson_UnauthorizedUser_DoesNotCreatePersonReturns403", false);
        testsPassed.put("postPerson_InvalidInputData_DoesNotCreatePersonReturns400", false);
        testsPassed.put("postPerson_NonexistentDependencyData_DoesNotCreatePersonReturns404", false);
        testsPassed.put("postPerson_ValidInput_CreatesPersonReturns200", false);

        testsPassed.put("putPerson_UnauthenticatedUser_DoesNotUpdatePersonReturns401", false);
        testsPassed.put("putPerson_UnauthorizedUser_DoesNotUpdatePersonReturns403", false);
        testsPassed.put("putPerson_InvalidInputData_DoesNotUpdatePersonReturns400", false);
        testsPassed.put("putPerson_NonexistentDependencyData_DoesNotUpdatePersonReturns404", false);
        testsPassed.put("putPerson_ValidInput_UpdatesPersonReturns200", false);

        testsPassed.put("deletePerson_UnauthenticatedUser_DoesNotDeletePersonReturns401", false);
        testsPassed.put("deletePerson_UnauthorizedUser_DoesNotDeletePersonReturns403", false);
        testsPassed.put("deletePerson_InvalidInputData_DoesNotDeletePersonReturns400", false);
        testsPassed.put("deletePerson_NonexistentDependencyData_DoesNotDeletePersonReturns404", false);
        testsPassed.put("deletePerson_ValidInput_DeletesPersonReturns200", false);
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
    @DisplayName("Tests whether POST request to /api/persons with unauthenticated user did not create new person and it returned 401")
    void postPerson_UnauthenticatedUser_DoesNotCreatePersonReturns401() {

        testsPassed.put("postPerson_UnauthenticatedUser_DoesNotCreatePersonReturns401", true);
    }

    @Test
    @Order(2)
    @Disabled
    @DisplayName("Tests whether POST request to /api/persons with authenticated but unauthorized user did not create new person and it returned 403")
    void postPerson_UnauthorizedUser_DoesNotCreatePersonReturns403() {

        testsPassed.put("postPerson_UnauthorizedUser_DoesNotCreatePersonReturns403", true);
    }

    @Test
    @Order(3)
    @Disabled
    @DisplayName("Tests whether POST request to /api/persons with invalid input data did not create new person and it returned 400")
    void postPerson_InvalidInputData_DoesNotCreatePersonReturns400() {

        testsPassed.put("postPerson_InvalidInputData_DoesNotCreatePersonReturns400", true);
    }

    @Test
    @Order(4)
    @Disabled
    @DisplayName("Tests whether POST request to /api/persons with non-existent dependency objects did not create new person and it returned 404")
    void postPerson_NonexistentDependencyData_DoesNotCreatePersonReturns404() {

        testsPassed.put("postPerson_NonexistentDependencyData_DoesNotCreatePersonReturns404", true);
    }

    @Test
    @Order(5)
    @Disabled
    @DisplayName("Tests whether POST request to /api/persons with valid input data did create new person and it returned 200")
    void postPerson_ValidInput_CreatesPersonReturns200() {

        testsPassed.put("postPerson_ValidInput_CreatesPersonReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(6)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/persons with unauthenticated user did not update person and it returned 401")
    void putPerson_UnauthenticatedUser_DoesNotUpdatePersonReturns401() {

        testsPassed.put("putPerson_UnauthenticatedUser_DoesNotUpdatePersonReturns401", true);
    }

    @Test
    @Order(7)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/persons with authenticated but unauthorized user did not update person and it returned 403")
    void putPerson_UnauthorizedUser_DoesNotUpdatePersonReturns403() {

        testsPassed.put("putPerson_UnauthorizedUser_DoesNotUpdatePersonReturns403", true);
    }

    @Test
    @Order(8)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/persons with invalid input data did not update person and it returned 400")
    void putPerson_InvalidInputData_DoesNotUpdatePersonReturns400() {

        testsPassed.put("putPerson_InvalidInputData_DoesNotUpdatePersonReturns400", true);
    }

    @Test
    @Order(9)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/persons with non-existent dependency objects did not update person and it returned 404")
    void putPerson_NonexistentDependencyData_DoesNotUpdatePersonReturns404() {

        testsPassed.put("putPerson_NonexistentDependencyData_DoesNotUpdatePersonReturns404", true);
    }

    @Test
    @Order(10)
    @Disabled
    @DisplayName("Tests whether PUT request to /api/persons with valid input data did update person and it returned 200")
    void putPerson_ValidInput_UpdatesPersonReturns200() {

        testsPassed.put("putPerson_ValidInput_UpdatesPersonReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(11)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/persons with unauthenticated user did not delete person and it returned 401")
    void deletePerson_UnauthenticatedUser_DoesNotDeletePersonReturns401() {

        testsPassed.put("deletePerson_UnauthenticatedUser_DoesNotDeletePersonReturns401", true);
    }

    @Test
    @Order(12)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/persons with authenticated but unauthorized user did not delete person and it returned 403")
    void deletePerson_UnauthorizedUser_DoesNotDeletePersonReturns403() {

        testsPassed.put("deletePerson_UnauthorizedUser_DoesNotDeletePersonReturns403", true);
    }

    @Test
    @Order(13)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/persons with invalid input data did not delete person and it returned 400")
    void deletePerson_InvalidInputData_DoesNotDeletePersonReturns400() {

        testsPassed.put("deletePerson_InvalidInputData_DoesNotDeletePersonReturns400", true);
    }

    @Test
    @Order(14)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/persons with non-existent dependency objects did not delete person and it returned 404")
    void deletePerson_NonexistentDependencyData_DoesNotDeletePersonReturns404() {

        testsPassed.put("deletePerson_NonexistentDependencyData_DoesNotDeletePersonReturns404", true);
    }

    @Test
    @Order(15)
    @Disabled
    @DisplayName("Tests whether DELETE request to /api/persons with valid input data did delete person and it returned 200")
    void deletePerson_ValidInput_DeletesPersonReturns200() {

        testsPassed.put("deletePerson_ValidInput_DeletesPersonReturns200", true);
    }

}
