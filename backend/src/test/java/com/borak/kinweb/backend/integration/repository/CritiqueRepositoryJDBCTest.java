/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.CritiqueRepositoryJDBC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CritiqueRepositoryJDBCTest {

    @Autowired
    private CritiqueRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("exists_Test", false);
        testsPassed.put("find_Test", false);
        testsPassed.put("insert_Test", false);
        testsPassed.put("update_Test", false);
        testsPassed.put("delete_Test", false);

    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(TestResultsHelper.didConfigPropertiesTestsPass());
    }

    //============================================================================================================ 
    //===========================================TESTS============================================================ 
    //============================================================================================================ 
    @Test
    @Order(1)
    @DisplayName("Tests normal functionality of exists method of CritiqueRepositoryJDBC class")
    void exists_Test() {
        List<CritiqueJDBC> invalidInputs = new ArrayList<>() {
            {
                add(null);
                add(new CritiqueJDBC(null, null, "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), null, "description", 15));
                add(new CritiqueJDBC(null, new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(null), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(null), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-1l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-2l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-5l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-51l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(Long.MIN_VALUE), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-1l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-2l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-5l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-51l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(Long.MIN_VALUE), "description", 15));
            }
        };

        for (int iter = 0; iter < invalidInputs.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.exists(invalidInputs.get(i));
            }).withMessage("Invalid parameter: critique, media and critic must not be null, and must have non-null ids greater than 0");
        }

        List<CritiqueJDBC> falseReturnsInputs = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(3l), null, null));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(2l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(8l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(6l), null, null));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(6l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(12l), null, null));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(3l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(20l), null, null));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(4l), null, null));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(5l), null, null));
                add(new CritiqueJDBC(new UserJDBC(7l), new MediaJDBC(15l), null, null));
            }
        };
        //false returns
        for (CritiqueJDBC falseReturnsInput : falseReturnsInputs) {
            boolean actual = repo.exists(falseReturnsInput);
            assertThat(actual).isFalse();
        }

        //true returns
        for (UserJDBC user : init.getUsers()) {
            for (CritiqueJDBC critique : user.getCritiques()) {
                boolean actual = repo.exists(critique);
                assertThat(actual).isTrue();
            }
        }

        testsPassed.put("exists_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of find method of CritiqueRepositoryJDBC class")
    void find_Test() {
        List<CritiqueJDBC> invalidInputs = new ArrayList<>() {
            {
                add(null);
                add(new CritiqueJDBC(null, null, "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), null, "description", 15));
                add(new CritiqueJDBC(null, new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(null), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(null), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-1l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-2l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-5l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(-51l), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(Long.MIN_VALUE), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-1l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-2l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-5l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(-51l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(Long.MIN_VALUE), "description", 15));
            }
        };

        for (int iter = 0; iter < invalidInputs.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.find(invalidInputs.get(i));
            }).withMessage("Invalid parameter: critique, media and critic must not be null, and must have non-null ids greater than 0");
        }

        List<CritiqueJDBC> falseReturnsInputs = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(3l), null, null));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(2l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(8l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(6l), null, null));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(6l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(12l), null, null));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(3l), null, null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(20l), null, null));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(4l), null, null));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(5l), null, null));
                add(new CritiqueJDBC(new UserJDBC(7l), new MediaJDBC(15l), null, null));
                add(new CritiqueJDBC(new UserJDBC(16l), new MediaJDBC(1l), null, null));

            }
        };
        //false returns
        for (CritiqueJDBC falseReturnsInput : falseReturnsInputs) {
            Optional<CritiqueJDBC> actual = repo.find(falseReturnsInput);
            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isFalse();
            assertThat(actual.isEmpty()).isTrue();
        }

        //true returns
        for (UserJDBC user : init.getUsers()) {
            for (CritiqueJDBC critique : user.getCritiques()) {
                Optional<CritiqueJDBC> actual = repo.find(critique);
                assertThat(actual).isNotNull();
                assertThat(actual.isPresent()).isTrue();
                assertThat(actual.isEmpty()).isFalse();
                checkValues(actual.get(), critique);
            }
        }

        testsPassed.put("find_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests normal functionality of insert method of CritiqueRepositoryJDBC class")
    void insert_Test() {
        Assumptions.assumeTrue(testsPassed.get("exists_Test"));
        Assumptions.assumeTrue(testsPassed.get("find_Test"));

        List<CritiqueJDBC> invalidInputs1 = new ArrayList<>() {
            {
                add(null);
                add(new CritiqueJDBC(null, null, "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), null, "description", 15));
                add(new CritiqueJDBC(null, new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(null), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(null), "description", 15));
            }
        };
        for (int iter = 0; iter < invalidInputs1.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.insert(invalidInputs1.get(i));
            }).withMessage("Invalid parameter: critique, media and critic must not be null, and must have non-null ids greater than 0");
        }
        List<CritiqueJDBC> invalidInputs2 = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), null, 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", -1));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", -2));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", -5));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", -50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", Integer.MIN_VALUE));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", 101));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", 102));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", 105));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", 150));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", Integer.MAX_VALUE));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(6l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(5l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(10l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(10l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(15l), "description", 50));
            }
        };
        for (int iter = 0; iter < invalidInputs2.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.insert(invalidInputs2.get(i));
            }).withMessage("Error while inserting critique");
        }

        List<CritiqueJDBC> validInputs = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(3l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(6l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(2l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(3l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(3l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(3l), new MediaJDBC(5l), "description", 50));
            }
        };
        for (CritiqueJDBC validInput : validInputs) {
            boolean actualBool = repo.exists(validInput);
            Optional<CritiqueJDBC> actualObject = repo.find(validInput);
            assertThat(actualBool).isFalse();
            assertThat(actualObject).isNotNull();
            assertThat(actualObject.isPresent()).isFalse();
            assertThat(actualObject.isEmpty()).isTrue();

            repo.insert(validInput);

            actualBool = repo.exists(validInput);
            actualObject = repo.find(validInput);
            assertThat(actualBool).isTrue();
            assertThat(actualObject).isNotNull();
            assertThat(actualObject.isPresent()).isTrue();
            assertThat(actualObject.isEmpty()).isFalse();
            checkValues(actualObject.get(), validInput);
        }

        testsPassed.put("insert_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests normal functionality of update method of CritiqueRepositoryJDBC class")
    void update_Test() {
        Assumptions.assumeTrue(testsPassed.get("exists_Test"));
        Assumptions.assumeTrue(testsPassed.get("find_Test"));

        List<CritiqueJDBC> invalidInputs1 = new ArrayList<>() {
            {
                add(null);
                add(new CritiqueJDBC(null, null, "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), null, "description", 15));
                add(new CritiqueJDBC(null, new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(null), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(null), "description", 15));
            }
        };
        for (int iter = 0; iter < invalidInputs1.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.update(invalidInputs1.get(i));
            }).withMessage("Invalid parameter: critique, media and critic must not be null, and must have non-null ids greater than 0");
        }
        List<CritiqueJDBC> invalidInputs2 = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), null, 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", null));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", -1));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", -2));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", -5));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", -50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", Integer.MIN_VALUE));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", 101));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", 102));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", 105));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", 150));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(1l), "description", Integer.MAX_VALUE));
            }
        };
        for (int iter = 0; iter < invalidInputs2.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.update(invalidInputs2.get(i));
            }).withMessage("Error while updating critique");
        }

        //not found inputs
        List<CritiqueJDBC> notFoundInputs = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(3l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(5l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(10l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(10l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(15l), "description", 50));
            }
        };
        for (int iter = 0; iter < notFoundInputs.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.update(notFoundInputs.get(i));
            }).withMessage("No critique found with userId: " + notFoundInputs.get(i).getCritic().getId() + ", and mediaId: " + notFoundInputs.get(i).getMedia().getId());
        }

        //valid found inputs
        for (UserJDBC user : init.getUsers()) {
            for (CritiqueJDBC critique : user.getCritiques()) {
                boolean actualBool = repo.exists(critique);
                Optional<CritiqueJDBC> actualObject = repo.find(critique);
                assertThat(actualBool).isTrue();
                assertThat(actualObject).isNotNull();
                assertThat(actualObject.isPresent()).isTrue();
                assertThat(actualObject.isEmpty()).isFalse();
                checkValues(actualObject.get(), critique);

                CritiqueJDBC expectedObject = new CritiqueJDBC(
                        new UserJDBC(critique.getCritic().getId()),
                        new MediaJDBC(critique.getMedia().getId()),
                        "some random description", 50);

                repo.update(expectedObject);

                actualBool = repo.exists(expectedObject);
                actualObject = repo.find(expectedObject);
                assertThat(actualBool).isTrue();
                assertThat(actualObject).isNotNull();
                assertThat(actualObject.isPresent()).isTrue();
                assertThat(actualObject.isEmpty()).isFalse();
                checkValues(actualObject.get(), expectedObject);
            }
        }

        testsPassed.put("update_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests normal functionality of delete method of CritiqueRepositoryJDBC class")
    void delete_Test() {
        Assumptions.assumeTrue(testsPassed.get("exists_Test"));

        List<CritiqueJDBC> invalidInputs1 = new ArrayList<>() {
            {
                add(null);
                add(new CritiqueJDBC(null, null, "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), null, "description", 15));
                add(new CritiqueJDBC(null, new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(null), new MediaJDBC(3l), "description", 15));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(null), "description", 15));
            }
        };
        for (int iter = 0; iter < invalidInputs1.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.delete(invalidInputs1.get(i));
            }).withMessage("Invalid parameter: critique, media and critic must not be null, and must have non-null ids greater than 0");
        }
        //not found inputs
        List<CritiqueJDBC> notFoundInputs = new ArrayList<>() {
            {
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(2l), new MediaJDBC(3l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(5l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(10l), new MediaJDBC(1l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(10l), "description", 50));
                add(new CritiqueJDBC(new UserJDBC(1l), new MediaJDBC(15l), "description", 50));
            }
        };
        for (int iter = 0; iter < notFoundInputs.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.delete(notFoundInputs.get(i));
            }).withMessage("No critique found with userId: " + notFoundInputs.get(i).getCritic().getId() + ", and mediaId: " + notFoundInputs.get(i).getMedia().getId());
        }

        //valid found inputs
        for (UserJDBC user : init.getUsers()) {
            for (CritiqueJDBC critique : user.getCritiques()) {
                boolean actualBool = repo.exists(critique);
                assertThat(actualBool).isTrue();

                repo.delete(critique);

                actualBool = repo.exists(critique);
                assertThat(actualBool).isFalse();
            }
        }

        testsPassed.put("delete_Test", true);
    }
//=========================================================================================================
//PRIVATE METHODS

    private void checkValues(CritiqueJDBC actual, CritiqueJDBC expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getCritic()).isNotNull();
        assertThat(actual.getMedia()).isNotNull();
        assertThat(actual.getCritic().getId()).isNotNull().isEqualTo(expected.getCritic().getId());
        assertThat(actual.getMedia().getId()).isNotNull().isEqualTo(expected.getMedia().getId());
        assertThat(actual.getDescription()).isNotEmpty().isEqualTo(expected.getDescription());
        assertThat(actual.getRating()).isNotNull().isEqualTo(expected.getRating());
    }

}
