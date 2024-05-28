/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.jdbc.classes.PersonJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.MovieRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.PersonRepositoryJDBC;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
public class PersonRepositoryJDBCTest {

    @Autowired
    private PersonRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("findAllPaginated_Test", false);
        testsPassed.put("findById_Test", false);
        testsPassed.put("existsById_Test", false);
        testsPassed.put("findByIdProfilePhoto_Test", false);
        testsPassed.put("updateProfilePhoto_Test", false);
        testsPassed.put("deleteById_Test", false);
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
    @DisplayName("Tests normal functionality of findAllPaginated method of PersonRepositoryJDBC class")
    void findAllPaginated_Test() {
        //(page,size)
        final int[][] invalidInput = {
            {0, 0},
            {-1, -1},
            {0, -1},
            {-1, 0},
            {-2, -2},
            {-2, -1},
            {-1, -2},
            {Integer.MIN_VALUE, -2},
            {-2, Integer.MIN_VALUE},
            {Integer.MIN_VALUE, Integer.MIN_VALUE},
            {0, 1},
            {0, 2},
            {0, Integer.MAX_VALUE},
            {1, -1},
            {-1, 1},
            {2, -2},
            {-2, 2},
            {Integer.MAX_VALUE, Integer.MIN_VALUE},
            {Integer.MIN_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInput[i][0], invalidInput[i][1]).isThrownBy(() -> {
                repo.findAllPaginated(invalidInput[i][0], invalidInput[i][1]);
            }).withMessage("Invalid parameters: page must be greater than 0 and size must be non-negative");
        }

        //(page,size)
        final int[][] emptyListInput = {
            {1, 0},
            {2, 0},
            {Integer.MAX_VALUE, 0},
            {51, 1},
            {Integer.MAX_VALUE, 1},
            {26, 2},
            {Integer.MAX_VALUE, 2},
            {18, 3},
            {19, 3},
            {Integer.MAX_VALUE, 3},
            {14, 4},
            {15, 4},
            {16, 4},
            {Integer.MAX_VALUE, 4},
            {2, Integer.MAX_VALUE},
            {3, Integer.MAX_VALUE},
            {Integer.MAX_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<PersonJDBC> actualList = repo.findAllPaginated(emptyListInput[i][0], emptyListInput[i][1]);
            assertThat(actualList).as("Code input values (%s,%s)", emptyListInput[i][0], emptyListInput[i][1]).isNotNull().isEmpty();
        }

        int page;
        int size;
        PersonJDBC expectedObject;
        List<PersonJDBC> actualList;
        List<PersonJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getPersons().get(0);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        expectedObject = init.getPersons().get(1);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        expectedObject = init.getPersons().get(2);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        expectedList = init.getPersons().subList(0, 2);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 2;
        size = 2;
        expectedList = init.getPersons().subList(2, 4);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 1;
        size = 50;
        expectedList = init.getPersons();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 1;
        size = 54;
        expectedList = init.getPersons();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getPersons();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);
        testsPassed.put("findAllPaginated_Test", true);

    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of findById method of PersonRepositoryJDBC class")
    void findById_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findById(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = null;
        Optional<PersonJDBC> actual;
        for (PersonJDBC expected : init.getPersons()) {
            actual = repo.findById(expected.getId());
            assertThat(actual).isNotNull();
            assertThat(actual.isEmpty()).isFalse();
            assertThat(actual.isPresent()).isTrue();
            checkValues(actual.get(), expected);
            id = expected.getId();
        }

        Long[] emptyInput = {id + 1l, id + 2l, id + 3l, id + 30l, Long.MAX_VALUE};
        for (int i = 0; i < emptyInput.length; i++) {
            actual = repo.findById(emptyInput[i]);
            assertThat(actual).as("Code input value (%s)", emptyInput[i]).isNotNull();
            assertThat(actual.isEmpty()).as("Code input value (%s)", emptyInput[i]).isTrue();
            assertThat(actual.isPresent()).as("Code input value (%s)", emptyInput[i]).isFalse();
        }

        testsPassed.put("findById_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests normal functionality of existsById method of PersonRepositoryJDBC class")
    void existsById_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.existsById(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = null;
        boolean actual;
        for (PersonJDBC expected : init.getPersons()) {
            actual = repo.existsById(expected.getId());
            assertThat(actual).isTrue();
            id = expected.getId();
        }

        actual = repo.existsById(id + 1l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 2l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 3l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 20l);
        assertThat(actual).isFalse();

        id = Long.MAX_VALUE;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();
        testsPassed.put("existsById_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests normal functionality of findByIdProfilePhoto method of PersonRepositoryJDBC class")
    void findByIdProfilePhoto_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findByIdProfilePhoto(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = null;
        Optional<String> actual;
        for (PersonJDBC expected : init.getPersons()) {
            actual = repo.findByIdProfilePhoto(expected.getId());
            assertThat(actual).isNotNull();
            if (expected.getProfilePhoto() == null) {
                assertThat(actual.isPresent()).isFalse();
                assertThat(actual.isEmpty()).isTrue();
            } else {
                assertThat(actual.isPresent()).isTrue();
                assertThat(actual.isEmpty()).isFalse();
                assertThat(actual.get()).isNotEmpty().isEqualTo(expected.getProfilePhoto());
            }
            id = expected.getId();
        }
        final Long[] invalidInput2 = {id + 1l, id + 2l, id + 3l, id + 10l, Long.MAX_VALUE};
        for (int iter = 0; iter < invalidInput2.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code input value (%s)", invalidInput2[i]).isThrownBy(() -> {
                repo.findByIdProfilePhoto(invalidInput2[i]);
            }).withMessage("No person found with given id: " + invalidInput2[i]);
        }

        testsPassed.put("findByIdProfilePhoto_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests normal functionality of updateProfilePhoto method of PersonRepositoryJDBC class")
    void updateProfilePhoto_Test() {
        //Assumes findByIdProfilePhoto_Test passed with no problems, as i need that method to check if profile photo updated corectly
        Assumptions.assumeTrue(testsPassed.get("findByIdProfilePhoto_Test"));

        Object[][] invalidInput1 = {
            {null, "Dummy profile photo 0"},
            {0l, "Dummy profile photo 1"},
            {-1l, "Dummy profile photo 2"},
            {-2l, "Dummy profile photo 3"},
            {Long.MIN_VALUE, "Dummy profile photo 3"}
        };

        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.updateProfilePhoto((Long) invalidInput1[i][0], (String) invalidInput1[i][1]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }

        Long id = null;
        String[] validInput = new String[]{null, "2023.jpg", "2023.png", "aaaa.png", null, null, "Dummy profile photo name"};
        Optional<String> actual;
        for (PersonJDBC expected : init.getPersons()) {
            repo.updateProfilePhoto(expected.getId(), expected.getProfilePhoto());
            actual = repo.findByIdProfilePhoto(expected.getId());
            if (expected.getProfilePhoto() == null) {
                assertThat(actual).isNotNull();
                assertThat(actual.isEmpty()).isTrue();
            } else {
                assertThat(actual).isNotNull();
                assertThat(actual.isPresent()).isTrue();
                assertThat(actual.get()).isEqualTo(expected.getProfilePhoto());
            }
            for (String string : validInput) {
                repo.updateProfilePhoto(expected.getId(), string);
                actual = repo.findByIdProfilePhoto(expected.getId());
                if (string == null) {
                    assertThat(actual).isNotNull();
                    assertThat(actual.isEmpty()).isTrue();
                } else {
                    assertThat(actual).isNotNull();
                    assertThat(actual.isPresent()).isTrue();
                    assertThat(actual.get()).isEqualTo(string);
                }
            }
            id = expected.getId();
        }

        //Non existant person IDs in database and invalid profile photo values
        StringBuilder sb = new StringBuilder(510);
        for (int i = 0; i < 510; i++) {
            sb.append('a'); // or any other character
        }
        Object[][] invalidInput2 = {
            {id + 1l, "Dummy cover image 0"},
            {id + 2l, "Dummy cover image 1"},
            {id + 10l, "Dummy cover image 2"},
            {Long.MAX_VALUE, "Dummy cover image 3"},
            {1l, sb.toString()}
        };

        for (int iter = 0; iter < invalidInput2.length; iter++) {
            final int i = iter;
            String message;
            if (i <= 3) {
                message = "Error while updating profile photo for person with id: " + invalidInput2[i][0] + ". No person found with given id";
            } else {
                message = "Error while updating profile photo for person with id: " + invalidInput2[i][0];
            }
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.updateProfilePhoto((Long) invalidInput2[i][0], (String) invalidInput2[i][1]);
            }).withMessage(message);
        }

        testsPassed.put("updateProfilePhoto_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests normal functionality of deleteById method of PersonRepositoryJDBC class")
    void deleteById_Test() {
        //Assumes existById_Test passed with no errors, as ill need existsById to rest whether or not deleteById works properly
        Assumptions.assumeTrue(testsPassed.get("existsById_Test"));
        Long[] invalidInput = new Long[]{null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final Long id = invalidInput[i];
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.deleteById(id);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        invalidInput = new Long[]{51l, 52l, 150l, Long.MAX_VALUE};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final Long id = invalidInput[i];
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.deleteById(id);
            }).withMessage("Error while deleting person with id: " + id + ". No person found with given id");
        }

        Long id = init.getPersons().get(0).getId();
        assertThat(repo.existsById(id)).isTrue();
        repo.deleteById(id);
        assertThat(repo.existsById(id)).isFalse();

        id = init.getPersons().get(1).getId();
        assertThat(repo.existsById(id)).isTrue();
        repo.deleteById(id);
        assertThat(repo.existsById(id)).isFalse();

        id = init.getPersons().get(15).getId();
        assertThat(repo.existsById(id)).isTrue();
        repo.deleteById(id);
        assertThat(repo.existsById(id)).isFalse();

        testsPassed.put("deleteById_Test", true);
    }

//==============================================================================================================
//===========================================PRIVATE METHODS====================================================
//==============================================================================================================       
    private void checkValues(List<PersonJDBC> actual, List<PersonJDBC> expected) {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(actual.size() == expected.size()).isTrue();

        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).getId()).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getFirstName()).isEqualTo(expected.get(i).getFirstName());
            assertThat(actual.get(i).getLastName()).isEqualTo(expected.get(i).getLastName());
            assertThat(actual.get(i).getGender()).isEqualTo(expected.get(i).getGender());
            assertThat(actual.get(i).getProfilePhoto()).isEqualTo(expected.get(i).getProfilePhoto());
        }
    }

    private void checkValues(PersonJDBC actual, PersonJDBC expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());

    }

}
