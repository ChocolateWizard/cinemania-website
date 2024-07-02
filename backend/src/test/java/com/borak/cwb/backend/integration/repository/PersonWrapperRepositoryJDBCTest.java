/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.repository;

import com.borak.cwb.backend.domain.enums.Gender;
import com.borak.cwb.backend.domain.jdbc.ActingJDBC;
import com.borak.cwb.backend.domain.jdbc.ActingRoleJDBC;
import com.borak.cwb.backend.domain.jdbc.ActorJDBC;
import com.borak.cwb.backend.domain.jdbc.DirectorJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.PersonJDBC;
import com.borak.cwb.backend.domain.jdbc.PersonWrapperJDBC;
import com.borak.cwb.backend.domain.jdbc.WriterJDBC;
import com.borak.cwb.backend.exceptions.DatabaseException;
import com.borak.cwb.backend.helpers.DataInitializer;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.repository.jdbc.PersonWrapperRepositoryJDBC;
import java.util.ArrayList;
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
public class PersonWrapperRepositoryJDBCTest {

    @Autowired
    private PersonWrapperRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("findAllPaginated_Test", false);
        testsPassed.put("findAllWithRelationsPaginated_Test", false);
        testsPassed.put("findById_Test", false);
        testsPassed.put("findByIdWithRelations_Test", false);
        testsPassed.put("insert_Test", false);
        testsPassed.put("update_Test", false);

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
    @DisplayName("Tests normal functionality of findAllPaginated method of PersonWrapperRepositoryJDBC class")
    void findAllPaginated_Test() {
        //inputs that should throw an exception
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
        //inputs that should return an empty list
        //(page,size)
        final int[][] emptyListInput = {
            {1, 0},
            {2, 0},
            {Integer.MAX_VALUE, 0},
            {Integer.MAX_VALUE, 1},
            {Integer.MAX_VALUE, 2},
            {Integer.MAX_VALUE, 3},
            {Integer.MAX_VALUE, 4},
            {51, 1},
            {27, 2},
            {2, Integer.MAX_VALUE},
            {3, Integer.MAX_VALUE},
            {Integer.MAX_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<PersonWrapperJDBC> actualList = repo.findAllPaginated(emptyListInput[i][0], emptyListInput[i][1]);
            assertThat(actualList).as("Code input values (%s,%s)", emptyListInput[i][0], emptyListInput[i][1]).isNotNull().isEmpty();
        }

        int page;
        int size;
        PersonWrapperJDBC expectedObject;
        List<PersonWrapperJDBC> actualList;
        List<PersonWrapperJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getPersonWrappers().get(0);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), false);

        page = 2;
        size = 1;
        expectedObject = init.getPersonWrappers().get(1);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), false);

        page = 3;
        size = 1;
        expectedObject = init.getPersonWrappers().get(2);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), false);

        page = 39;
        size = 1;
        expectedObject = init.getPersonWrappers().get(38);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), false);

        page = 50;
        size = 1;
        expectedObject = init.getPersonWrappers().get(49);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), false);

        page = 1;
        size = 2;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 2;
        size = 2;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 18;
        size = 2;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 2;
        size = 21;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 12;
        size = 3;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 1;
        size = 50;
        expectedList = init.getPersonWrappers();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 1;
        size = 54;
        expectedList = init.getPersonWrappers();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getPersonWrappers();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList, false);

        testsPassed.put("findAllPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of findAllWithRelationsPaginated method of PersonWrapperRepositoryJDBC class")
    void findAllWithRelationsPaginated_Test() {
        //inputs that should throw an exception
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
                repo.findAllWithRelationsPaginated(invalidInput[i][0], invalidInput[i][1]);
            }).withMessage("Invalid parameters: page must be greater than 0 and size must be non-negative");
        }
        //inputs that should return an empty list
        //(page,size)
        final int[][] emptyListInput = {
            {1, 0},
            {2, 0},
            {Integer.MAX_VALUE, 0},
            {Integer.MAX_VALUE, 1},
            {Integer.MAX_VALUE, 2},
            {Integer.MAX_VALUE, 3},
            {Integer.MAX_VALUE, 4},
            {51, 1},
            {27, 2},
            {2, Integer.MAX_VALUE},
            {3, Integer.MAX_VALUE},
            {Integer.MAX_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<PersonWrapperJDBC> actualList = repo.findAllWithRelationsPaginated(emptyListInput[i][0], emptyListInput[i][1]);
            assertThat(actualList).as("Code input values (%s,%s)", emptyListInput[i][0], emptyListInput[i][1]).isNotNull().isEmpty();
        }

        int page;
        int size;
        PersonWrapperJDBC expectedObject;
        List<PersonWrapperJDBC> actualList;
        List<PersonWrapperJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getPersonWrappers().get(0);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), true);

        page = 2;
        size = 1;
        expectedObject = init.getPersonWrappers().get(1);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), true);

        page = 3;
        size = 1;
        expectedObject = init.getPersonWrappers().get(2);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), true);

        page = 39;
        size = 1;
        expectedObject = init.getPersonWrappers().get(38);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), true);

        page = 50;
        size = 1;
        expectedObject = init.getPersonWrappers().get(49);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject), true);

        page = 1;
        size = 2;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 2;
        size = 2;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 18;
        size = 2;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 2;
        size = 21;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 12;
        size = 3;
        expectedList = init.getPersonWrappers(page, size);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 1;
        size = 50;
        expectedList = init.getPersonWrappers();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 1;
        size = 54;
        expectedList = init.getPersonWrappers();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getPersonWrappers();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValues(actualList, expectedList, true);

        testsPassed.put("findAllWithRelationsPaginated_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests normal functionality of findById method of PersonWrapperRepositoryJDBC class")
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
        Optional<PersonWrapperJDBC> actual;
        for (PersonWrapperJDBC expected : init.getPersonWrappers()) {
            actual = repo.findById(expected.getPerson().getId());
            assertThat(actual).isNotNull();
            assertThat(actual.isEmpty()).isFalse();
            assertThat(actual.isPresent()).isTrue();
            checkValues(actual.get(), expected, false);
            id = expected.getPerson().getId();
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
    @Order(4)
    @DisplayName("Tests normal functionality of findByIdWithRelations method of PersonWrapperRepositoryJDBC class")
    void findByIdWithRelations_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findByIdWithRelations(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = null;
        Optional<PersonWrapperJDBC> actual;
        for (PersonWrapperJDBC expected : init.getPersonWrappers()) {
            actual = repo.findByIdWithRelations(expected.getPerson().getId());
            assertThat(actual).isNotNull();
            assertThat(actual.isEmpty()).isFalse();
            assertThat(actual.isPresent()).isTrue();
            checkValues(actual.get(), expected, true);
            id = expected.getPerson().getId();
        }

        Long[] emptyInput = {id + 1l, id + 2l, id + 3l, id + 30l, Long.MAX_VALUE};
        for (int i = 0; i < emptyInput.length; i++) {
            actual = repo.findByIdWithRelations(emptyInput[i]);
            assertThat(actual).as("Code input value (%s)", emptyInput[i]).isNotNull();
            assertThat(actual.isEmpty()).as("Code input value (%s)", emptyInput[i]).isTrue();
            assertThat(actual.isPresent()).as("Code input value (%s)", emptyInput[i]).isFalse();
        }
        testsPassed.put("findByIdWithRelations_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests normal functionality of insert method of PersonWrapperRepositoryJDBC class")
    void insert_Test() {
        //Assume dependant methods executed with no errors, since i need them to test wheter or not PersonWrapper inserted properly      
        Assumptions.assumeTrue(testsPassed.get("findById_Test"));
        Assumptions.assumeTrue(testsPassed.get("findByIdWithRelations_Test"));

        List<PersonWrapperJDBC> invalidInput1 = new ArrayList<PersonWrapperJDBC>() {
            {
                add(null);
                add(new PersonWrapperJDBC());
                add(new PersonWrapperJDBC(null, null, null, null));
            }
        };
        List<PersonWrapperJDBC> invalidInput2 = new ArrayList<PersonWrapperJDBC>() {
            {
                add(new PersonWrapperJDBC(new PersonJDBC(null, "dummyFirstName", "dummyLastName", null, null), null, null, null));
                add(new PersonWrapperJDBC(new PersonJDBC(1l, "dummyFirstName", null, Gender.OTHER, "dummy.jpg"), null, null, null));
                add(new PersonWrapperJDBC(new PersonJDBC(56l, null, "dummyLastName", Gender.OTHER, null), null, null, null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        new DirectorJDBC(1l, null, null, null, null),
                        null, null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        new DirectorJDBC(1l, null, null, null, null),
                        null, null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        new WriterJDBC(1l, null, null, null, null),
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        new WriterJDBC(1l, null, null, null, null),
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(56l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
            }
        };
        invalidInput2.get(3).getDirector().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(3).getDirector().getMedias().add(new MediaJDBC(null));
        invalidInput2.get(3).getDirector().getMedias().add(new MediaJDBC(3l));

        invalidInput2.get(4).getDirector().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(4).getDirector().getMedias().add(new MediaJDBC(2l));
        invalidInput2.get(4).getDirector().getMedias().add(new MediaJDBC(30l));

        invalidInput2.get(5).getWriter().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(5).getWriter().getMedias().add(new MediaJDBC(null));
        invalidInput2.get(5).getWriter().getMedias().add(new MediaJDBC(3l));

        invalidInput2.get(6).getWriter().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(6).getWriter().getMedias().add(new MediaJDBC(2l));
        invalidInput2.get(6).getWriter().getMedias().add(new MediaJDBC(30l));

        invalidInput2.get(7).getActor().getActings().add(new ActingJDBC(new MediaJDBC(1l), false));
        invalidInput2.get(7).getActor().getActings().add(new ActingJDBC(null, false));
        invalidInput2.get(7).getActor().getActings().add(new ActingJDBC(new MediaJDBC(3l), false));

        invalidInput2.get(8).getActor().getActings().add(new ActingJDBC(new MediaJDBC(1l), false));
        invalidInput2.get(8).getActor().getActings().add(new ActingJDBC(new MediaJDBC(null), false));
        invalidInput2.get(8).getActor().getActings().add(new ActingJDBC(new MediaJDBC(3l), false));

        invalidInput2.get(9).getActor().getActings().add(new ActingJDBC(new MediaJDBC(1l), false));
        invalidInput2.get(9).getActor().getActings().add(new ActingJDBC(new MediaJDBC(2l), false));
        invalidInput2.get(9).getActor().getActings().add(new ActingJDBC(new MediaJDBC(30l), false));

        ActingJDBC pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, null, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 3l, "dummyRoleName"));
        invalidInput2.get(10).getActor().getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        invalidInput2.get(11).getActor().getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, -3l, "dummyRoleName"));
        invalidInput2.get(12).getActor().getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 2l, null));
        pom.getRoles().add(new ActingRoleJDBC(null, 3l, "dummyRoleName"));
        invalidInput2.get(13).getActor().getActings().add(pom);

        for (int iter = 0; iter < invalidInput1.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.insert(invalidInput1.get(i));
            }).withMessage("Invalid parameter: entity must be non-null");
        }

        for (int iter = 0; iter < invalidInput2.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.insert(invalidInput2.get(i));
            }).withMessage("Error while inserting person");
        }

        //VALID INPUTS - SHOULD NOT THROW AN EXCEPTION
        PersonWrapperJDBC validInput = new PersonWrapperJDBC();
        PersonJDBC validPerson = new PersonJDBC(null, "dummyFirstName", "dummyLastName", Gender.OTHER, null);
        DirectorJDBC validDirector = new DirectorJDBC(null, null, null, null, null);
        WriterJDBC validWriter = new WriterJDBC(null, null, null, null, null);
        ActorJDBC validActor = new ActorJDBC(null, null, null, null, null, false);

        validInput.setPerson(validPerson);
        validInput.setDirector(validDirector);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);

        PersonWrapperJDBC returnedInput = repo.insert(validInput);
        assertThat(returnedInput.getPerson().getId()).isNotNull();
        Optional<PersonWrapperJDBC> actual = repo.findById(returnedInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), returnedInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(null);
        validInput.setActor(null);

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput.getPerson().getId()).isNotNull();
        actual = repo.findById(returnedInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), returnedInput);

        validInput.setDirector(null);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput.getPerson().getId()).isNotNull();
        actual = repo.findById(returnedInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), returnedInput);

        validInput.setDirector(null);
        validInput.setWriter(validWriter);
        validInput.setActor(null);

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput.getPerson().getId()).isNotNull();
        actual = repo.findById(returnedInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), returnedInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(null);
        validInput.setActor(validActor);

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput.getPerson().getId()).isNotNull();
        actual = repo.findById(returnedInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), returnedInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);

        validDirector.getMedias().add(new MediaJDBC(1l));
        validDirector.getMedias().add(new MediaJDBC(3l));

        validWriter.getMedias().add(new MediaJDBC(2l));
        validWriter.getMedias().add(new MediaJDBC(4l));

        pom = new ActingJDBC(new MediaJDBC(1l), true);
        pom.getRoles().add(new ActingRoleJDBC(pom, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 3l, "dummyRoleName"));
        pom.setActor(validActor);
        validActor.getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(2l), true);
        pom.getRoles().add(new ActingRoleJDBC(pom, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 3l, "dummyRoleName"));
        pom.setActor(validActor);
        validActor.getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(4l), false);
        pom.getRoles().add(new ActingRoleJDBC(pom, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 2l, "dummyRoleName"));
        pom.setActor(validActor);
        validActor.getActings().add(pom);

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput.getPerson().getId()).isNotNull();
        actual = repo.findByIdWithRelations(returnedInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), returnedInput);

        testsPassed.put("insert_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests normal functionality of update method of PersonWrapperRepositoryJDBC class")
    void update_Test() {
        //Assume dependant methods executed with no errors, since i need them to test wheter or not PersonWrapper updated properly      
        Assumptions.assumeTrue(testsPassed.get("findById_Test"));
        Assumptions.assumeTrue(testsPassed.get("findByIdWithRelations_Test"));

        List<PersonWrapperJDBC> invalidInput1 = new ArrayList<PersonWrapperJDBC>() {
            {
                add(null);
                add(new PersonWrapperJDBC());
                add(new PersonWrapperJDBC(null, null, null, null));
            }
        };

        List<PersonWrapperJDBC> invalidInput2 = new ArrayList<PersonWrapperJDBC>() {
            {
                add(new PersonWrapperJDBC(new PersonJDBC(null, "dummyFirstName", "dummyLastName", null, null), null, null, null));
                add(new PersonWrapperJDBC(new PersonJDBC(1l, "dummyFirstName", null, Gender.OTHER, "dummy.jpg"), null, null, null));
                add(new PersonWrapperJDBC(new PersonJDBC(1l, null, "dummyLastName", Gender.OTHER, null), null, null, null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        new DirectorJDBC(1l, null, null, null, null),
                        null, null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        new DirectorJDBC(1l, null, null, null, null),
                        null, null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        new WriterJDBC(1l, null, null, null, null),
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        new WriterJDBC(1l, null, null, null, null),
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(-1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(-1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        new DirectorJDBC(1l, null, null, null, null),
                        null,
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(-1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        new WriterJDBC(1l, null, null, null, null),
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(-1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(150l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(150l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        new DirectorJDBC(1l, null, null, null, null),
                        null,
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(150l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        new WriterJDBC(1l, null, null, null, null),
                        null));
                add(new PersonWrapperJDBC(
                        new PersonJDBC(150l, "dummyFirstName", "dummyLastName", Gender.OTHER, null),
                        null,
                        null,
                        new ActorJDBC(1l, null, null, null, null, false)));
            }
        };
        invalidInput2.get(3).getDirector().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(3).getDirector().getMedias().add(new MediaJDBC(null));
        invalidInput2.get(3).getDirector().getMedias().add(new MediaJDBC(3l));

        invalidInput2.get(4).getDirector().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(4).getDirector().getMedias().add(new MediaJDBC(2l));
        invalidInput2.get(4).getDirector().getMedias().add(new MediaJDBC(30l));

        invalidInput2.get(5).getWriter().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(5).getWriter().getMedias().add(new MediaJDBC(null));
        invalidInput2.get(5).getWriter().getMedias().add(new MediaJDBC(3l));

        invalidInput2.get(6).getWriter().getMedias().add(new MediaJDBC(1l));
        invalidInput2.get(6).getWriter().getMedias().add(new MediaJDBC(2l));
        invalidInput2.get(6).getWriter().getMedias().add(new MediaJDBC(30l));

        invalidInput2.get(7).getActor().getActings().add(new ActingJDBC(new MediaJDBC(1l), false));
        invalidInput2.get(7).getActor().getActings().add(new ActingJDBC(null, false));
        invalidInput2.get(7).getActor().getActings().add(new ActingJDBC(new MediaJDBC(3l), false));

        invalidInput2.get(8).getActor().getActings().add(new ActingJDBC(new MediaJDBC(1l), false));
        invalidInput2.get(8).getActor().getActings().add(new ActingJDBC(new MediaJDBC(null), false));
        invalidInput2.get(8).getActor().getActings().add(new ActingJDBC(new MediaJDBC(3l), false));

        invalidInput2.get(9).getActor().getActings().add(new ActingJDBC(new MediaJDBC(1l), false));
        invalidInput2.get(9).getActor().getActings().add(new ActingJDBC(new MediaJDBC(2l), false));
        invalidInput2.get(9).getActor().getActings().add(new ActingJDBC(new MediaJDBC(30l), false));

        ActingJDBC pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, null, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 3l, "dummyRoleName"));
        invalidInput2.get(10).getActor().getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        invalidInput2.get(11).getActor().getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, -3l, "dummyRoleName"));
        invalidInput2.get(12).getActor().getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(1l), false);
        pom.getRoles().add(new ActingRoleJDBC(null, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(null, 2l, null));
        pom.getRoles().add(new ActingRoleJDBC(null, 3l, "dummyRoleName"));
        invalidInput2.get(13).getActor().getActings().add(pom);

        for (int iter = 0; iter < invalidInput1.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.update(invalidInput1.get(i));
            }).withMessage("Invalid parameter: entity must be non-null");
        }

        for (int iter = 0; iter < invalidInput2.size(); iter++) {
            final int i = iter;
            String message;
            if (i >= 14) {
                message = "Error while updating person with id: " + invalidInput2.get(i).getPerson().getId() + ". No person found with given id";
            } else {
                message = "Error while updating person with id: " + invalidInput2.get(i).getPerson().getId();
            }
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.update(invalidInput2.get(i));
            }).withMessage(message);
        }

        //VALID INPUTS - SHOULD NOT THROW AN EXCEPTION
        PersonWrapperJDBC validInput = new PersonWrapperJDBC();
        PersonJDBC validPerson = new PersonJDBC(1l, "dummyFirstName", "dummyLastName", Gender.OTHER, null);
        DirectorJDBC validDirector = new DirectorJDBC(1l, null, null, null, null);
        WriterJDBC validWriter = new WriterJDBC(1l, null, null, null, null);
        ActorJDBC validActor = new ActorJDBC(1l, null, null, null, null, false);

        validInput.setPerson(validPerson);
        validInput.setDirector(validDirector);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);

        repo.update(validInput);
        Optional<PersonWrapperJDBC> actual = repo.findById(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(null);
        validInput.setActor(null);

        repo.update(validInput);
        actual = repo.findById(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        validInput.setDirector(null);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);

        repo.update(validInput);
        actual = repo.findById(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        validInput.setDirector(null);
        validInput.setWriter(validWriter);
        validInput.setActor(null);

        repo.update(validInput);
        actual = repo.findById(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(null);
        validInput.setActor(validActor);

        repo.update(validInput);
        actual = repo.findById(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);
        validInput.getPerson().setFirstName("EDIT: FIRST NAME");
        validInput.getPerson().setLastName("EDIT: LAST NAME");
        validInput.getPerson().setGender(Gender.MALE);
        validInput.setId(30l);

        repo.update(validInput);
        actual = repo.findById(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        validInput.setDirector(validDirector);
        validInput.setWriter(validWriter);
        validInput.setActor(validActor);

        validDirector.getMedias().add(new MediaJDBC(1l));
        validDirector.getMedias().add(new MediaJDBC(3l));

        validWriter.getMedias().add(new MediaJDBC(2l));
        validWriter.getMedias().add(new MediaJDBC(4l));

        pom = new ActingJDBC(new MediaJDBC(1l), true);
        pom.getRoles().add(new ActingRoleJDBC(pom, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 3l, "dummyRoleName"));
        pom.setActor(validActor);
        validActor.getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(2l), true);
        pom.getRoles().add(new ActingRoleJDBC(pom, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 2l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 3l, "dummyRoleName"));
        pom.setActor(validActor);
        validActor.getActings().add(pom);

        pom = new ActingJDBC(new MediaJDBC(4l), false);
        pom.getRoles().add(new ActingRoleJDBC(pom, 1l, "dummyRoleName"));
        pom.getRoles().add(new ActingRoleJDBC(pom, 2l, "dummyRoleName"));
        pom.setActor(validActor);
        validActor.getActings().add(pom);

        repo.update(validInput);
        actual = repo.findByIdWithRelations(validInput.getPerson().getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkInsertedOrUpdatedValues(actual.get(), validInput);

        testsPassed.put("update_Test", true);
    }
//===============================================================================================================================
//=========================================PRIVATE METHODS=======================================================================
//===============================================================================================================================

    private void checkValues(List<PersonWrapperJDBC> actual, List<PersonWrapperJDBC> expected, boolean checkRelations) {
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            checkValues(actual.get(i), expected.get(i), checkRelations);
        }
    }

    private void checkValues(PersonWrapperJDBC actual, PersonWrapperJDBC expected, boolean checkRelations) {
        assertThat(actual).isNotNull();
        checkPerson(actual.getPerson(), expected.getPerson());
        checkDirector(actual.getDirector(), expected.getDirector(), checkRelations);
        checkWriter(actual.getWriter(), expected.getWriter(), checkRelations);
        checkActor(actual.getActor(), expected.getActor(), checkRelations);
    }

    private void checkPerson(PersonJDBC actual, PersonJDBC expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isNotNull().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotNull().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());
    }

    private void checkDirector(DirectorJDBC actual, DirectorJDBC expected, boolean checkRelations) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isNull();
            assertThat(actual.getLastName()).isNull();
            assertThat(actual.getGender()).isNull();
            assertThat(actual.getProfilePhoto()).isNull();

            assertThat(actual.getMedias()).isNotNull();
            if (checkRelations) {
                assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
                for (int i = 0; i < actual.getMedias().size(); i++) {
                    assertThat(actual.getMedias().get(i)).isNotNull();
                    assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());

                    assertThat(actual.getMedias().get(i).getTitle()).isNull();
                    assertThat(actual.getMedias().get(i).getDescription()).isNull();
                    assertThat(actual.getMedias().get(i).getReleaseDate()).isNull();
                    assertThat(actual.getMedias().get(i).getCoverImage()).isNull();
                    assertThat(actual.getMedias().get(i).getAudienceRating()).isNull();
                    assertThat(actual.getMedias().get(i).getCriticRating()).isNull();

                    assertThat(actual.getMedias().get(i).getGenres()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getDirectors()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getWriters()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getActings()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getCritiques()).isNotNull().isEmpty();

                }
            } else {
                assertThat(actual.getMedias()).isEmpty();
            }
        }
    }

    private void checkWriter(WriterJDBC actual, WriterJDBC expected, boolean checkRelations) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isNull();
            assertThat(actual.getLastName()).isNull();
            assertThat(actual.getGender()).isNull();
            assertThat(actual.getProfilePhoto()).isNull();

            assertThat(actual.getMedias()).isNotNull();
            if (checkRelations) {
                assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
                for (int i = 0; i < actual.getMedias().size(); i++) {
                    assertThat(actual.getMedias().get(i)).isNotNull();
                    assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());

                    assertThat(actual.getMedias().get(i).getTitle()).isNull();
                    assertThat(actual.getMedias().get(i).getDescription()).isNull();
                    assertThat(actual.getMedias().get(i).getReleaseDate()).isNull();
                    assertThat(actual.getMedias().get(i).getCoverImage()).isNull();
                    assertThat(actual.getMedias().get(i).getAudienceRating()).isNull();
                    assertThat(actual.getMedias().get(i).getCriticRating()).isNull();

                    assertThat(actual.getMedias().get(i).getGenres()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getDirectors()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getWriters()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getActings()).isNotNull().isEmpty();
                    assertThat(actual.getMedias().get(i).getCritiques()).isNotNull().isEmpty();

                }
            } else {
                assertThat(actual.getMedias()).isEmpty();
            }
        }
    }

    private void checkActor(ActorJDBC actual, ActorJDBC expected, boolean checkRelations) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isNull();
            assertThat(actual.getLastName()).isNull();
            assertThat(actual.getGender()).isNull();
            assertThat(actual.getProfilePhoto()).isNull();
            assertThat(actual.isStar()).isNotNull().isEqualTo(expected.isStar());

            assertThat(actual.getActings()).isNotNull();
            if (checkRelations) {
                assertThat(actual.getActings().size()).isEqualTo(expected.getActings().size());
                for (int i = 0; i < actual.getActings().size(); i++) {
                    assertThat(actual.getActings().get(i)).isNotNull();

                    assertThat(actual.getActings().get(i).getMedia()).isNotNull();
                    assertThat(actual.getActings().get(i).getMedia().getId()).isNotNull().isEqualTo(expected.getActings().get(i).getMedia().getId());

                    assertThat(actual.getActings().get(i).getMedia().getTitle()).isNull();
                    assertThat(actual.getActings().get(i).getMedia().getDescription()).isNull();
                    assertThat(actual.getActings().get(i).getMedia().getReleaseDate()).isNull();
                    assertThat(actual.getActings().get(i).getMedia().getCoverImage()).isNull();
                    assertThat(actual.getActings().get(i).getMedia().getAudienceRating()).isNull();
                    assertThat(actual.getActings().get(i).getMedia().getCriticRating()).isNull();

                    assertThat(actual.getActings().get(i).getMedia().getGenres()).isNotNull().isEmpty();
                    assertThat(actual.getActings().get(i).getMedia().getDirectors()).isNotNull().isEmpty();
                    assertThat(actual.getActings().get(i).getMedia().getWriters()).isNotNull().isEmpty();
                    assertThat(actual.getActings().get(i).getMedia().getActings()).isNotNull().isEmpty();
                    assertThat(actual.getActings().get(i).getMedia().getCritiques()).isNotNull().isEmpty();

                    assertThat(actual.getActings().get(i).getActor()).isNotNull();
                    assertThat(actual.getActings().get(i).getActor() == actual).isTrue();
                    assertThat(actual.getActings().get(i).isStarring()).isNotNull().isEqualTo(expected.getActings().get(i).isStarring());

                    assertThat(actual.getActings().get(i).getRoles()).isNotNull();
                    assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActings().get(i).getRoles().size());
                    for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                        assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                        assertThat(actual.getActings().get(i).getRoles().get(j).getActing() == actual.getActings().get(i)).isTrue();
                        assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isNotNull().isEqualTo(expected.getActings().get(i).getRoles().get(j).getId());
                        assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isNotNull().isEqualTo(expected.getActings().get(i).getRoles().get(j).getName());
                    }
                }
            } else {
                assertThat(actual.getActings()).isEmpty();
            }
        }
    }
//--------------------CHECK INSERT OR UPDATE--------------------------------------------------------------------------------------

    private void checkInsertedOrUpdatedValues(PersonWrapperJDBC actual, PersonWrapperJDBC expected) {
        assertThat(actual).isNotNull();
        checkInsertedOrUpdatedPerson(actual.getPerson(), expected.getPerson());
        checkInsertedOrUpdatedDirector(actual.getDirector(), expected.getDirector());
        checkInsertedOrUpdatedWriter(actual.getWriter(), expected.getWriter());
        checkInsertedOrUpdatedActor(actual.getActor(), expected.getActor());
    }

    private void checkInsertedOrUpdatedPerson(PersonJDBC actual, PersonJDBC expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isNotNull().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotNull().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());

    }

    private void checkInsertedOrUpdatedDirector(DirectorJDBC actual, DirectorJDBC expected) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
            assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
            assertThat(actual.getGender()).isEqualTo(expected.getGender());
            assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());

            assertThat(actual.getMedias()).isNotNull();
            assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
            for (int i = 0; i < actual.getMedias().size(); i++) {
                assertThat(actual.getMedias().get(i)).isNotNull();
                assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());

                assertThat(actual.getMedias().get(i).getTitle()).isEqualTo(expected.getMedias().get(i).getTitle());
                assertThat(actual.getMedias().get(i).getDescription()).isEqualTo(expected.getMedias().get(i).getDescription());
                assertThat(actual.getMedias().get(i).getReleaseDate()).isEqualTo(expected.getMedias().get(i).getReleaseDate());
                assertThat(actual.getMedias().get(i).getCoverImage()).isEqualTo(expected.getMedias().get(i).getCoverImage());
                assertThat(actual.getMedias().get(i).getAudienceRating()).isEqualTo(expected.getMedias().get(i).getAudienceRating());
                assertThat(actual.getMedias().get(i).getCriticRating()).isEqualTo(expected.getMedias().get(i).getCriticRating());

                assertThat(actual.getMedias().get(i).getGenres()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getDirectors()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getWriters()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getActings()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getCritiques()).isNotNull().isEmpty();

            }
        }
    }

    private void checkInsertedOrUpdatedWriter(WriterJDBC actual, WriterJDBC expected) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
            assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
            assertThat(actual.getGender()).isEqualTo(expected.getGender());
            assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());

            assertThat(actual.getMedias()).isNotNull();
            assertThat(actual.getMedias().size()).isEqualTo(expected.getMedias().size());
            for (int i = 0; i < actual.getMedias().size(); i++) {
                assertThat(actual.getMedias().get(i)).isNotNull();
                assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());

                assertThat(actual.getMedias().get(i).getTitle()).isEqualTo(expected.getMedias().get(i).getTitle());
                assertThat(actual.getMedias().get(i).getDescription()).isEqualTo(expected.getMedias().get(i).getDescription());
                assertThat(actual.getMedias().get(i).getReleaseDate()).isEqualTo(expected.getMedias().get(i).getReleaseDate());
                assertThat(actual.getMedias().get(i).getCoverImage()).isEqualTo(expected.getMedias().get(i).getCoverImage());
                assertThat(actual.getMedias().get(i).getAudienceRating()).isEqualTo(expected.getMedias().get(i).getAudienceRating());
                assertThat(actual.getMedias().get(i).getCriticRating()).isEqualTo(expected.getMedias().get(i).getCriticRating());

                assertThat(actual.getMedias().get(i).getGenres()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getDirectors()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getWriters()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getActings()).isNotNull().isEmpty();
                assertThat(actual.getMedias().get(i).getCritiques()).isNotNull().isEmpty();

            }
        }
    }

    private void checkInsertedOrUpdatedActor(ActorJDBC actual, ActorJDBC expected) {
        if (actual == null) {
            assertThat(expected).isNull();
        } else {
            assertThat(expected).isNotNull();
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
            assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
            assertThat(actual.getGender()).isEqualTo(expected.getGender());
            assertThat(actual.getProfilePhoto()).isEqualTo(expected.getProfilePhoto());
            assertThat(actual.isStar()).isNotNull().isEqualTo(expected.isStar());

            assertThat(actual.getActings()).isNotNull();
            assertThat(actual.getActings().size()).isEqualTo(expected.getActings().size());
            for (int i = 0; i < actual.getActings().size(); i++) {
                assertThat(actual.getActings().get(i)).isNotNull();

                assertThat(actual.getActings().get(i).getMedia()).isNotNull();
                assertThat(actual.getActings().get(i).getMedia().getId()).isNotNull().isEqualTo(expected.getActings().get(i).getMedia().getId());

                assertThat(actual.getActings().get(i).getMedia().getTitle()).isEqualTo(expected.getActings().get(i).getMedia().getTitle());
                assertThat(actual.getActings().get(i).getMedia().getDescription()).isEqualTo(expected.getActings().get(i).getMedia().getDescription());
                assertThat(actual.getActings().get(i).getMedia().getReleaseDate()).isEqualTo(expected.getActings().get(i).getMedia().getReleaseDate());
                assertThat(actual.getActings().get(i).getMedia().getCoverImage()).isEqualTo(expected.getActings().get(i).getMedia().getCoverImage());
                assertThat(actual.getActings().get(i).getMedia().getAudienceRating()).isEqualTo(expected.getActings().get(i).getMedia().getAudienceRating());
                assertThat(actual.getActings().get(i).getMedia().getCriticRating()).isEqualTo(expected.getActings().get(i).getMedia().getCriticRating());

                assertThat(actual.getActings().get(i).getMedia().getGenres()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getDirectors()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getWriters()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getActings()).isNotNull().isEmpty();
                assertThat(actual.getActings().get(i).getMedia().getCritiques()).isNotNull().isEmpty();

                assertThat(actual.getActings().get(i).getActor()).isEqualTo(expected.getActings().get(i).getActor());
                assertThat(actual.getActings().get(i).isStarring()).isNotNull().isEqualTo(expected.getActings().get(i).isStarring());

                assertThat(actual.getActings().get(i).getRoles()).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().size()).isEqualTo(expected.getActings().get(i).getRoles().size());
                for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                    assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                    assertThat(actual.getActings().get(i).getRoles().get(j).getActing()).isEqualTo(expected.getActings().get(i).getRoles().get(j).getActing());
                    assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isNotNull().isEqualTo(expected.getActings().get(i).getRoles().get(j).getId());
                    assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isNotNull().isEqualTo(expected.getActings().get(i).getRoles().get(j).getName());
                }
            }

        }
    }

}
