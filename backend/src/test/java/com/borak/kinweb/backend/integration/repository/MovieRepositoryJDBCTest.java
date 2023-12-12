/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingRoleJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.ActorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.DirectorJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.GenreJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.WriterJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.MovieRepositoryJDBC;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(3)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class MovieRepositoryJDBCTest {

    @Autowired
    private MovieRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("findAll_Test", false);
        testsPassed.put("findAllWithGenres_Test", false);
        testsPassed.put("findAllWithRelations_Test", false);
        testsPassed.put("count_Test", false);
        testsPassed.put("findAllPaginated_Test", false);
        testsPassed.put("findAllWithGenresPaginated_Test", false);
        testsPassed.put("findAllWithRelationsPaginated_Test", false);
        testsPassed.put("findAllByAudienceRatingWithGenresPaginated_Test", false);
        testsPassed.put("findAllByReleaseYearWithGenresPaginated_Test", false);
        testsPassed.put("findByIdCoverImage_Test", false);
        testsPassed.put("existsById_Test", false);
        testsPassed.put("findById_Test", false);
        testsPassed.put("findByIdWithGenres_Test", false);
        testsPassed.put("findByIdWithRelations_Test", false);
        testsPassed.put("insert_Test", false);
        testsPassed.put("update_Test", false);
        testsPassed.put("updateCoverImage_Test", false);
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
        Assumptions.assumeTrue(ConfigPropertiesTest.didAllTestsPass());
    }

    //============================================================================================================ 
    //===========================================TESTS============================================================ 
    //============================================================================================================ 
    @Test
    @Order(1)
    @DisplayName("Tests normal functionality of findAll method of MovieRepositoryJDBC class")
    void findAll_Test() {
        List<MovieJDBC> actual = repo.findAll();
        List<MovieJDBC> expected = init.getMovies();

        checkValues(actual, expected);
        testsPassed.put("findAll_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of findAllWithGenres method of MovieRepositoryJDBC class")
    void findAllWithGenres_Test() {
        List<MovieJDBC> actual = repo.findAllWithGenres();
        List<MovieJDBC> expected = init.getMovies();

        checkValuesWithGenres(actual, expected);
        testsPassed.put("findAllWithGenres_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests normal functionality of findAllWithRelations method of MovieRepositoryJDBC class")
    void findAllWithRelations_Test() {
        List<MovieJDBC> actual = repo.findAllWithRelations();
        List<MovieJDBC> expected = init.getMovies();

        checkValuesWithRelations(actual, expected);
        testsPassed.put("findAllWithRelations_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests normal functionality of count method of MovieRepositoryJDBC class")
    void count_Test() {
        long actual = repo.count();
        long expected = init.getMovies().size();
        assertThat(actual).isEqualTo(expected);
        testsPassed.put("count_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests normal functionality of findAllPaginated method of MovieRepositoryJDBC class")
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
            {4, 1},
            {Integer.MAX_VALUE, 1},
            {3, 2},
            {Integer.MAX_VALUE, 2},
            {2, 3},
            {3, 3},
            {Integer.MAX_VALUE, 3},
            {2, 4},
            {3, 4},
            {4, 4},
            {Integer.MAX_VALUE, 4},
            {2, Integer.MAX_VALUE},
            {3, Integer.MAX_VALUE},
            {Integer.MAX_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<MovieJDBC> actualList = repo.findAllPaginated(emptyListInput[i][0], emptyListInput[i][1]);
            assertThat(actualList).as("Code input values (%s,%s)", emptyListInput[i][0], emptyListInput[i][1]).isNotNull().isEmpty();
        }

        int page;
        int size;
        MovieJDBC expectedObject;
        List<MovieJDBC> actualList;
        List<MovieJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 2;
        size = 2;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        expectedList = init.getMovies();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 1;
        size = 4;
        expectedList = init.getMovies();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getMovies();
        actualList = repo.findAllPaginated(page, size);
        checkValues(actualList, expectedList);
        testsPassed.put("findAllPaginated_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests normal functionality of findAllWithGenresPaginated method of MovieRepositoryJDBC class")
    void findAllWithGenresPaginated_Test() {
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
                repo.findAllWithGenresPaginated(invalidInput[i][0], invalidInput[i][1]);
            }).withMessage("Invalid parameters: page must be greater than 0 and size must be non-negative");
        }

        //(page,size)
        final int[][] emptyListInput = {
            {1, 0},
            {2, 0},
            {Integer.MAX_VALUE, 0},
            {4, 1},
            {Integer.MAX_VALUE, 1},
            {3, 2},
            {Integer.MAX_VALUE, 2},
            {2, 3},
            {3, 3},
            {Integer.MAX_VALUE, 3},
            {2, 4},
            {3, 4},
            {4, 4},
            {Integer.MAX_VALUE, 4},
            {2, Integer.MAX_VALUE},
            {3, Integer.MAX_VALUE},
            {Integer.MAX_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<MovieJDBC> actualList = repo.findAllWithGenresPaginated(emptyListInput[i][0], emptyListInput[i][1]);
            assertThat(actualList).as("Code input values (%s,%s)", emptyListInput[i][0], emptyListInput[i][1]).isNotNull().isEmpty();
        }

        int page;
        int size;
        MovieJDBC expectedObject;
        List<MovieJDBC> actualList;
        List<MovieJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, expectedList);

        page = 2;
        size = 2;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        expectedList = init.getMovies();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 4;
        expectedList = init.getMovies();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getMovies();
        actualList = repo.findAllWithGenresPaginated(page, size);
        checkValuesWithGenres(actualList, expectedList);
        testsPassed.put("findAllWithGenresPaginated_Test", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests normal functionality of findAllWithRelationsPaginated method of MovieRepositoryJDBC class")
    void findAllWithRelationsPaginated_Test() {
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

        //(page,size)
        final int[][] emptyListInput = {
            {1, 0},
            {2, 0},
            {Integer.MAX_VALUE, 0},
            {4, 1},
            {Integer.MAX_VALUE, 1},
            {3, 2},
            {Integer.MAX_VALUE, 2},
            {2, 3},
            {3, 3},
            {Integer.MAX_VALUE, 3},
            {2, 4},
            {3, 4},
            {4, 4},
            {Integer.MAX_VALUE, 4},
            {2, Integer.MAX_VALUE},
            {3, Integer.MAX_VALUE},
            {Integer.MAX_VALUE, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<MovieJDBC> actualList = repo.findAllWithRelationsPaginated(emptyListInput[i][0], emptyListInput[i][1]);
            assertThat(actualList).as("Code input values (%s,%s)", emptyListInput[i][0], emptyListInput[i][1]).isNotNull().isEmpty();
        }

        int page;
        int size;
        MovieJDBC expectedObject;
        List<MovieJDBC> actualList;
        List<MovieJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, expectedList);

        page = 2;
        size = 2;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        expectedList = init.getMovies();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, expectedList);

        page = 1;
        size = 4;
        expectedList = init.getMovies();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getMovies();
        actualList = repo.findAllWithRelationsPaginated(page, size);
        checkValuesWithRelations(actualList, expectedList);
        testsPassed.put("findAllWithRelationsPaginated_Test", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests normal functionality of findAllByAudienceRatingWithGenresPaginated method of MovieRepositoryJDBC class")
    void findAllByAudienceRatingWithGenresPaginated_Test() {
        //(page,size,treshold)
        final int[][] invalidInput = {
            {0, 0, 0},
            {-1, -1, 0},
            {0, -1, 0},
            {-1, 0, 0},
            {-2, -2, 0},
            {-2, -1, 0},
            {-1, -2, 0},
            {Integer.MIN_VALUE, -2, 0},
            {-2, Integer.MIN_VALUE, 0},
            {Integer.MIN_VALUE, Integer.MIN_VALUE, 0},
            {0, 1, 0},
            {0, 2, 0},
            {0, Integer.MAX_VALUE, 0},
            {1, -1, 0},
            {-1, 1, 0},
            {2, -2, 0},
            {-2, 2, 0},
            {Integer.MAX_VALUE, Integer.MIN_VALUE, 0},
            {Integer.MIN_VALUE, Integer.MAX_VALUE, 0},
            {0, 0, 100},
            {0, 1, 100},
            {0, 2, 100},
            {1, 3, -1},
            {1, 3, -2},
            {1, 3, Integer.MIN_VALUE},
            {1, 3, 101},
            {1, 3, 102},
            {1, 3, Integer.MAX_VALUE}
        };
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s,%s)", invalidInput[i][0], invalidInput[i][1], invalidInput[i][2]).isThrownBy(() -> {
                repo.findAllByAudienceRatingWithGenresPaginated(invalidInput[i][0], invalidInput[i][1], invalidInput[i][2]);
            }).withMessage("Invalid parameters: page must be greater than 0, size must be non-negative, and ratingTresh must be between 0 and 100 (inclusive)");
        }

        //(page,size,treshold)
        final int[][] emptyListInput = {
            {1, 0, 0},
            {2, 0, 0},
            {Integer.MAX_VALUE, 0, 0},
            {4, 1, 0},
            {Integer.MAX_VALUE, 1, 0},
            {3, 2, 0},
            {Integer.MAX_VALUE, 2, 0},
            {2, 3, 0},
            {3, 3, 0},
            {Integer.MAX_VALUE, 3, 0},
            {2, 4, 0},
            {3, 4, 0},
            {4, 4, 0},
            {Integer.MAX_VALUE, 4, 0},
            {2, Integer.MAX_VALUE, 0},
            {3, Integer.MAX_VALUE, 0},
            {Integer.MAX_VALUE, Integer.MAX_VALUE, 0},
            {1, 1, 100},
            {1, 3, 100},
            {1, 1, 90},
            {1, 3, 90},
            {1, 10, 90},
            {1, 1, 80},
            {1, 3, 80},
            {1, 10, 80}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<MovieJDBC> actualList = repo.findAllByAudienceRatingWithGenresPaginated(emptyListInput[i][0], emptyListInput[i][1], emptyListInput[i][2]);
            assertThat(actualList).as("Code input values (%s,%s,%s)", emptyListInput[i][0], emptyListInput[i][1], emptyListInput[i][2]).isNotNull().isEmpty();
        }

        int page;
        int size;
        int treshold;
        MovieJDBC expectedObject;
        List<MovieJDBC> actualList;
        List<MovieJDBC> expectedList;

        page = 1;
        size = 1;
        treshold = 0;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        treshold = 0;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        treshold = 0;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        treshold = 0;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, expectedList);

        page = 2;
        size = 2;
        treshold = 0;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        treshold = 0;
        expectedList = init.getMovies();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 4;
        treshold = 0;
        expectedList = init.getMovies();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        treshold = 0;
        expectedList = init.getMovies();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 1;
        treshold = 68;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 1;
        treshold = 79;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        treshold = 68;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, expectedList);

        page = 2;
        size = 1;
        treshold = 68;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        treshold = 68;
        expectedList = init.getMovies();
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 2;
        treshold = 74;
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(init.getMullholadDrive(), init.getTheLighthouse()));

        page = 1;
        size = 3;
        treshold = 74;
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(init.getMullholadDrive(), init.getTheLighthouse()));

        page = 2;
        size = 1;
        treshold = 74;
        actualList = repo.findAllByAudienceRatingWithGenresPaginated(page, size, treshold);
        checkValuesWithGenres(actualList, Arrays.asList(init.getTheLighthouse()));
        testsPassed.put("findAllByAudienceRatingWithGenresPaginated_Test", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests normal functionality of findAllByReleaseYearWithGenresPaginated method of MovieRepositoryJDBC class")
    void findAllByReleaseYearWithGenresPaginated_Test() {
        //(page,size,year)
        final int currentYear = Year.now().getValue();
        final int[][] invalidInput = {
            {0, 0, 0},
            {-1, -1, 0},
            {0, -1, 0},
            {-1, 0, 0},
            {-2, -2, 0},
            {-2, -1, 0},
            {-1, -2, 0},
            {Integer.MIN_VALUE, -2, 0},
            {-2, Integer.MIN_VALUE, 0},
            {Integer.MIN_VALUE, Integer.MIN_VALUE, 0},
            {0, 1, 0},
            {0, 2, 0},
            {0, Integer.MAX_VALUE, 0},
            {1, -1, 0},
            {-1, 1, 0},
            {2, -2, 0},
            {-2, 2, 0},
            {Integer.MAX_VALUE, Integer.MIN_VALUE, 0},
            {Integer.MIN_VALUE, Integer.MAX_VALUE, 0},
            {0, 0, currentYear},
            {0, 1, currentYear},
            {0, 2, currentYear},
            {1, 3, -1},
            {1, 3, -2},
            {1, 3, Integer.MIN_VALUE}
        };
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s,%s)", invalidInput[i][0], invalidInput[i][1], invalidInput[i][2]).isThrownBy(() -> {
                repo.findAllByReleaseYearWithGenresPaginated(invalidInput[i][0], invalidInput[i][1], invalidInput[i][2]);
            }).withMessage("Invalid parameters: page must be greater than 0 and the size and year must be non-negative");
        }

        //(page,size,year)
        final int[][] emptyListInput = {
            {1, 0, 0},
            {2, 0, 0},
            {Integer.MAX_VALUE, 0, 0},
            {4, 1, 0},
            {Integer.MAX_VALUE, 1, 0},
            {3, 2, 0},
            {Integer.MAX_VALUE, 2, 0},
            {2, 3, 0},
            {3, 3, 0},
            {Integer.MAX_VALUE, 3, 0},
            {2, 4, 0},
            {3, 4, 0},
            {4, 4, 0},
            {Integer.MAX_VALUE, 4, 0},
            {2, Integer.MAX_VALUE, 0},
            {3, Integer.MAX_VALUE, 0},
            {Integer.MAX_VALUE, Integer.MAX_VALUE, 0},
            {1, 1, currentYear},
            {1, 3, currentYear},
            {1, 1, 2021},
            {1, 3, 2021},
            {1, 10, 2021},
            {1, 1, 2020},
            {1, 3, 2020},
            {1, 10, 2020},
            {1, 1, Integer.MAX_VALUE},
            {1, 3, Integer.MAX_VALUE},
            {1, 10, Integer.MAX_VALUE},};
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<MovieJDBC> actualList = repo.findAllByReleaseYearWithGenresPaginated(emptyListInput[i][0], emptyListInput[i][1], emptyListInput[i][2]);
            assertThat(actualList).as("Code input values (%s,%s,%s)", emptyListInput[i][0], emptyListInput[i][1], emptyListInput[i][2]).isNotNull().isEmpty();
        }

        int page;
        int size;
        int year;
        MovieJDBC expectedObject;
        List<MovieJDBC> actualList;
        List<MovieJDBC> expectedList;

        page = 1;
        size = 1;
        year = 2000;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        year = 2000;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        year = 2000;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        year = 2000;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, expectedList);

        page = 2;
        size = 2;
        year = 2000;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        year = 2000;
        expectedList = init.getMovies();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 4;
        year = 2000;
        expectedList = init.getMovies();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        year = 2000;
        expectedList = init.getMovies();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 1;
        year = 2001;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 1;
        year = 2002;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 1;
        year = 2006;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        year = 2001;
        expectedList = init.getMovies().subList(0, 2);
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, expectedList);

        page = 2;
        size = 1;
        year = 2001;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 3;
        year = 2001;
        expectedList = init.getMovies();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 1;
        year = 2019;
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        year = 2006;
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(init.getInlandEmpire(), init.getTheLighthouse()));

        page = 1;
        size = 3;
        year = 2006;
        actualList = repo.findAllByReleaseYearWithGenresPaginated(page, size, year);
        checkValuesWithGenres(actualList, Arrays.asList(init.getInlandEmpire(), init.getTheLighthouse()));
        testsPassed.put("findAllByReleaseYearWithGenresPaginated_Test", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests normal functionality of findByIdCoverImage method of MovieRepositoryJDBC class")
    void findByIdCoverImage_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findByIdCoverImage(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        final Long[] invalidInput2 = {3l, 5l, 6l, 7l, Long.MAX_VALUE};
        for (int iter = 0; iter < invalidInput2.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code input value (%s)", invalidInput2[i]).isThrownBy(() -> {
                repo.findByIdCoverImage(invalidInput2[i]);
            }).withMessage("No movie found with given id: " + invalidInput2[i]);
        }

        Long id;
        String expected;
        Optional<String> actual;

        id = 1l;
        expected = init.getMullholadDrive().getCoverImage();
        actual = repo.findByIdCoverImage(id);
        assertThat(actual).isNotNull();
        assertThat(actual.get()).isNotNull().isNotEmpty().isEqualTo(expected);

        id = 2l;
        expected = init.getInlandEmpire().getCoverImage();
        actual = repo.findByIdCoverImage(id);
        assertThat(actual).isNotNull();
        assertThat(actual.get()).isNotNull().isNotEmpty().isEqualTo(expected);

        id = 4l;
        actual = repo.findByIdCoverImage(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isTrue();
        assertThat(actual.isPresent()).isFalse();

        testsPassed.put("findByIdCoverImage_Test", true);
    }

    @Test
    @Order(11)
    @DisplayName("Tests normal functionality of existsById method of MovieRepositoryJDBC class")
    void existsById_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.existsById(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = 1l;
        boolean actual = repo.existsById(id);
        assertThat(actual).isTrue();

        id = 2l;
        actual = repo.existsById(id);
        assertThat(actual).isTrue();

        id = 3l;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();

        id = 4l;
        actual = repo.existsById(id);
        assertThat(actual).isTrue();

        id = 5l;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();

        id = 6l;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();

        id = Long.MAX_VALUE;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();
        testsPassed.put("existsById_Test", true);
    }

    @Test
    @Order(12)
    @DisplayName("Tests normal functionality of findById method of MovieRepositoryJDBC class")
    void findById_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findById(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = 1l;
        MovieJDBC expected = init.getMullholadDrive();
        Optional<MovieJDBC> actual = repo.findById(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValues(Arrays.asList(actual.get()), Arrays.asList(expected));

        id = 2l;
        expected = init.getInlandEmpire();
        actual = repo.findById(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValues(Arrays.asList(actual.get()), Arrays.asList(expected));

        id = 4l;
        expected = init.getTheLighthouse();
        actual = repo.findById(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValues(Arrays.asList(actual.get()), Arrays.asList(expected));

        Long[] emptyInput = {3l, 5l, 6l, 7l, Long.MAX_VALUE};
        for (int i = 0; i < emptyInput.length; i++) {
            actual = repo.findById(emptyInput[i]);
            assertThat(actual).as("Code input value (%s)", emptyInput[i]).isNotNull();
            assertThat(actual.isEmpty()).as("Code input value (%s)", emptyInput[i]).isTrue();
            assertThat(actual.isPresent()).as("Code input value (%s)", emptyInput[i]).isFalse();
        }

        testsPassed.put("findById_Test", true);
    }

    @Test
    @Order(13)
    @DisplayName("Tests normal functionality of findByIdWithGenres method of MovieRepositoryJDBC class")
    void findByIdWithGenres_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findByIdWithGenres(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = 1l;
        MovieJDBC expected = init.getMullholadDrive();
        Optional<MovieJDBC> actual = repo.findByIdWithGenres(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValuesWithGenres(Arrays.asList(actual.get()), Arrays.asList(expected));

        id = 2l;
        expected = init.getInlandEmpire();
        actual = repo.findByIdWithGenres(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValuesWithGenres(Arrays.asList(actual.get()), Arrays.asList(expected));

        id = 4l;
        expected = init.getTheLighthouse();
        actual = repo.findByIdWithGenres(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValuesWithGenres(Arrays.asList(actual.get()), Arrays.asList(expected));

        Long[] emptyInput = {3l, 5l, 6l, 7l, Long.MAX_VALUE};
        for (int i = 0; i < emptyInput.length; i++) {
            actual = repo.findByIdWithGenres(emptyInput[i]);
            assertThat(actual).as("Code input value (%s)", emptyInput[i]).isNotNull();
            assertThat(actual.isEmpty()).as("Code input value (%s)", emptyInput[i]).isTrue();
            assertThat(actual.isPresent()).as("Code input value (%s)", emptyInput[i]).isFalse();
        }
        testsPassed.put("findByIdWithGenres_Test", true);
    }

    @Test
    @Order(14)
    @DisplayName("Tests normal functionality of findByIdWithRelations method of MovieRepositoryJDBC class")
    void findByIdWithRelations_Test() {
        //(id)
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findByIdWithRelations(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        Long id = 1l;
        MovieJDBC expected = init.getMullholadDrive();
        Optional<MovieJDBC> actual = repo.findByIdWithRelations(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValuesWithRelations(Arrays.asList(actual.get()), Arrays.asList(expected));

        id = 2l;
        expected = init.getInlandEmpire();
        actual = repo.findByIdWithRelations(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValuesWithRelations(Arrays.asList(actual.get()), Arrays.asList(expected));

        id = 4l;
        expected = init.getTheLighthouse();
        actual = repo.findByIdWithRelations(id);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.isPresent()).isTrue();
        checkValuesWithRelations(Arrays.asList(actual.get()), Arrays.asList(expected));

        Long[] emptyInput = {3l, 5l, 6l, 7l, Long.MAX_VALUE};
        for (int i = 0; i < emptyInput.length; i++) {
            actual = repo.findByIdWithRelations(emptyInput[i]);
            assertThat(actual).as("Code input value (%s)", emptyInput[i]).isNotNull();
            assertThat(actual.isEmpty()).as("Code input value (%s)", emptyInput[i]).isTrue();
            assertThat(actual.isPresent()).as("Code input value (%s)", emptyInput[i]).isFalse();
        }
        testsPassed.put("findByIdWithRelations_Test", true);
    }

    @Test
    @Order(15)
    @DisplayName("Tests normal functionality of insert method of MovieRepositoryJDBC class")
    void insert_Test() {
        //Assume dependant methods executed with no errors, since i need them to test wheter or not Movie inserted properly      
        Assumptions.assumeTrue(testsPassed.get("findById_Test"));
        Assumptions.assumeTrue(testsPassed.get("findByIdWithRelations_Test"));

        //Invalid inputs to insert method
        List<MovieJDBC> invalidInput = new ArrayList<MovieJDBC>() {
            {
                add(new MovieJDBC(null, "Dummy title 0", null, "Dummy description 0", LocalDate.now(), 0, null, null));
                add(new MovieJDBC(null, "Dummy title 1", null, "Dummy description 1", LocalDate.now(), 0, null, -1));
                add(new MovieJDBC(null, "Dummy title 2", null, "Dummy description 2", LocalDate.now(), 0, null, -2));
                add(new MovieJDBC(null, "Dummy title 3", null, "Dummy description 3", LocalDate.now(), 0, null, Integer.MIN_VALUE));
                add(new MovieJDBC(null, "Dummy title 4", null, "Dummy description 4", LocalDate.now(), null, null, 0));
                add(new MovieJDBC(null, "Dummy title 5", null, "Dummy description 5", LocalDate.now(), -1, null, 0));
                add(new MovieJDBC(null, "Dummy title 6", null, "Dummy description 6", LocalDate.now(), -2, null, 0));
                add(new MovieJDBC(null, "Dummy title 7", null, "Dummy description 7", LocalDate.now(), Integer.MIN_VALUE, null, 0));
                add(new MovieJDBC(null, "Dummy title 8", null, "Dummy description 8", LocalDate.now(), 101, null, 0));
                add(new MovieJDBC(null, "Dummy title 9", null, "Dummy description 9", LocalDate.now(), 102, null, 0));
                add(new MovieJDBC(null, "Dummy title 10", null, "Dummy description 10", LocalDate.now(), Integer.MAX_VALUE, null, 0));
                add(new MovieJDBC(null, "Dummy title 11", null, "Dummy description 11", null, 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 12", null, null, LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, null, null, "Dummy description 13", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 14", "Dummy cover image 14", "Dummy description 14", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 15", "Dummy cover image 15", "Dummy description 15", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 16", "Dummy cover image 16", "Dummy description 16", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 17", "Dummy cover image 17", "Dummy description 17", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 18", "Dummy cover image 18", "Dummy description 18", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 19", "Dummy cover image 19", "Dummy description 19", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 20", "Dummy cover image 20", "Dummy description 20", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 21", "Dummy cover image 21", "Dummy description 21", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 22", "Dummy cover image 22", "Dummy description 22", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 23", "Dummy cover image 23", "Dummy description 23", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 24", "Dummy cover image 24", "Dummy description 24", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 25", "Dummy cover image 25", "Dummy description 25", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 26", "Dummy cover image 26", "Dummy description 26", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 27", "Dummy cover image 27", "Dummy description 27", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 28", "Dummy cover image 28", "Dummy description 28", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 29", "Dummy cover image 29", "Dummy description 29", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 30", "Dummy cover image 30", "Dummy description 30", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 31", "Dummy cover image 31", "Dummy description 31", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 32", "Dummy cover image 32", "Dummy description 32", LocalDate.now(), 0, null, 0));
            }
        };
        invalidInput.get(14).getGenres().add(new GenreJDBC(1l, "Dummy genre 0"));
        invalidInput.get(14).getGenres().add(new GenreJDBC(2l, "Dummy genre 1"));
        invalidInput.get(14).getGenres().add(new GenreJDBC(143l, "Dummy genre 2"));

        invalidInput.get(15).getGenres().add(new GenreJDBC(1l, "Dummy genre 0"));
        invalidInput.get(15).getGenres().add(new GenreJDBC(2l, "Dummy genre 1"));
        invalidInput.get(15).getGenres().add(new GenreJDBC(null, "Dummy genre 2"));

        invalidInput.get(16).getGenres().add(new GenreJDBC(1l, "Dummy genre 0"));
        invalidInput.get(16).getGenres().add(new GenreJDBC(2l, "Dummy genre 1"));
        invalidInput.get(16).getGenres().add(new GenreJDBC(1l, "Dummy genre 2"));

        //------------
        invalidInput.get(17).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(17).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(17).getDirectors().add(new DirectorJDBC(16l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(18).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(18).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(18).getDirectors().add(new DirectorJDBC(160l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(19).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(19).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(19).getDirectors().add(new DirectorJDBC(null, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(20).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(20).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(20).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        //-------
        invalidInput.get(21).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(21).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(21).getWriters().add(new WriterJDBC(20l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(22).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(22).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(22).getWriters().add(new WriterJDBC(200l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(23).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(23).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(23).getWriters().add(new WriterJDBC(null, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(24).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(24).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(24).getWriters().add(new WriterJDBC(1l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        //-----------
        invalidInput.get(25).getActings().add(new ActingJDBC(invalidInput.get(25), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(25).getActings().add(new ActingJDBC(invalidInput.get(25), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(25).getActings().add(new ActingJDBC(invalidInput.get(25), new ActorJDBC(14l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(26).getActings().add(new ActingJDBC(invalidInput.get(26), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(26).getActings().add(new ActingJDBC(invalidInput.get(26), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(26).getActings().add(new ActingJDBC(invalidInput.get(26), new ActorJDBC(140l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(27).getActings().add(new ActingJDBC(invalidInput.get(27), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(27).getActings().add(new ActingJDBC(invalidInput.get(27), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(27).getActings().add(new ActingJDBC(invalidInput.get(27), new ActorJDBC(null, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(28).getActings().add(new ActingJDBC(invalidInput.get(28), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(28).getActings().add(new ActingJDBC(invalidInput.get(28), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(28).getActings().add(new ActingJDBC(invalidInput.get(28), new ActorJDBC(2l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(29).getActings().add(new ActingJDBC(invalidInput.get(29), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(29).getActings().add(new ActingJDBC(invalidInput.get(29), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(29).getActings().add(new ActingJDBC(invalidInput.get(29), new ActorJDBC(4l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), null));

        //-------
        ActingJDBC invalidActing = new ActingJDBC(invalidInput.get(30), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true);
        invalidInput.get(30).getActings().add(invalidActing);
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 0"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 2l, "Dummy name 1"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 3l, null));

        invalidActing = new ActingJDBC(invalidInput.get(31), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true);
        invalidInput.get(31).getActings().add(invalidActing);
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 0"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 2l, "Dummy name 1"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, null, "Dummy name 2"));

        invalidActing = new ActingJDBC(invalidInput.get(32), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true);
        invalidInput.get(32).getActings().add(invalidActing);
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 0"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 2l, "Dummy name 1"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 2"));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.insert(null);
        }).withMessage("Invalid parameter: entity must be non-null");

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.insert(invalidInput.get(i));
            }).withMessage("Error while inserting movie");
        }

        //VALID inputs to insert method
        //1. Movie ID is null, but rest values are valid and given. No relations
        MovieJDBC validInput = new MovieJDBC(null, "Dummy title 33", null, "Dummy description 33", LocalDate.now(), 15, null, 140);
        MovieJDBC returnedInput = repo.insert(validInput);
        assertThat(returnedInput).isNotNull();
        assertThat(returnedInput.getId()).isNotNull().isEqualTo(30l);
        assertThat(validInput.getId()).isNotNull().isEqualTo(30l);
        checkValues(returnedInput, validInput);
        Optional<MovieJDBC> actual = repo.findById(30l);
        assertThat(actual.isPresent()).isTrue();
        checkValues(actual.get(), validInput);

        //2. ID is given as already present one in DB; rest values are valid; No relations
        validInput = new MovieJDBC(2l, "Dummy title 33", null, "Dummy description 33", LocalDate.now(), 15, null, 140);
        returnedInput = repo.insert(validInput);
        assertThat(returnedInput).isNotNull();
        assertThat(returnedInput.getId()).isNotNull().isEqualTo(31l);
        assertThat(validInput.getId()).isNotNull().isEqualTo(31l);
        checkValues(returnedInput, validInput);
        actual = repo.findById(31l);
        assertThat(actual.isPresent()).isTrue();
        checkValues(actual.get(), validInput);

        //3. ID is given as already present one in DB; rest values are valid; With relations
        validInput = new MovieJDBC(2l, "Dummy title 33", "2.png", "Dummy description 33", LocalDate.now(), 15, null, 140);
        validInput.getGenres().add(new GenreJDBC(1l, "Action"));
        validInput.getGenres().add(new GenreJDBC(3l, "Animation"));
        validInput.getGenres().add(new GenreJDBC(4l, "Comedy"));

        validInput.getDirectors().add(new DirectorJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(14l, "Pascal", "Charrue", Gender.MALE, "14.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(15l, "Arnaud", "Delord", Gender.MALE, "15.jpg"));

        validInput.getWriters().add(new WriterJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getWriters().add(new WriterJDBC(16l, "Mollie Bickley", "St. John", Gender.FEMALE, null));
        validInput.getWriters().add(new WriterJDBC(27l, "Max", "Eggers", Gender.MALE, "27.jpg"));

        ActingJDBC validActing = new ActingJDBC(validInput, new ActorJDBC(2l, "Naomi", "Watts", Gender.FEMALE, "2.jpg", true), true);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 0"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 2l, "Dummy name 2"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 3l, "Dummy name 1"));

        validActing = new ActingJDBC(validInput, new ActorJDBC(25l, "Mick", "Wingert", Gender.MALE, "25.jpg", false), false);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 2"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 4l, "Dummy name 1"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 6l, "Dummy name 0"));

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput).isNotNull();
        assertThat(returnedInput.getId()).isNotNull().isEqualTo(32l);
        assertThat(validInput.getId()).isNotNull().isEqualTo(32l);
        checkValues(returnedInput, validInput);
        actual = repo.findByIdWithRelations(32l);
        assertThat(actual.isPresent()).isTrue();
        checkValues(actual.get(), validInput);

        //4. Check if returned movie after insert ordered ids correctly
        validInput = new MovieJDBC(2l, "Dummy title 33", "2.png", "Dummy description 33", LocalDate.now(), 15, null, 140);
        validInput.getGenres().add(new GenreJDBC(1l, "Action"));
        validInput.getGenres().add(new GenreJDBC(4l, "Comedy"));
        validInput.getGenres().add(new GenreJDBC(3l, "Animation"));

        validInput.getDirectors().add(new DirectorJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(15l, "Arnaud", "Delord", Gender.MALE, "15.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(14l, "Pascal", "Charrue", Gender.MALE, "14.jpg"));

        validInput.getWriters().add(new WriterJDBC(16l, "Mollie Bickley", "St. John", Gender.FEMALE, null));
        validInput.getWriters().add(new WriterJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getWriters().add(new WriterJDBC(27l, "Max", "Eggers", Gender.MALE, "27.jpg"));

        validActing = new ActingJDBC(validInput, new ActorJDBC(2l, "Naomi", "Watts", Gender.FEMALE, "2.jpg", true), true);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 0"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 3l, "Dummy name 1"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 2l, "Dummy name 2"));

        validActing = new ActingJDBC(validInput, new ActorJDBC(25l, "Mick", "Wingert", Gender.MALE, "25.jpg", false), false);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 6l, "Dummy name 0"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 4l, "Dummy name 1"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 2"));

        returnedInput = repo.insert(validInput);
        assertThat(returnedInput).isNotNull();
        assertThat(returnedInput.getId()).isNotNull().isEqualTo(33l);
        assertThat(validInput.getId()).isNotNull().isEqualTo(33l);
        checkValues(returnedInput, validInput);
        actual = repo.findByIdWithRelations(33l);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getGenres().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getGenres().get(1).getId()).isEqualTo(3l);
        assertThat(actual.get().getGenres().get(2).getId()).isEqualTo(4l);

        assertThat(actual.get().getDirectors().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getDirectors().get(1).getId()).isEqualTo(14l);
        assertThat(actual.get().getDirectors().get(2).getId()).isEqualTo(15l);

        assertThat(actual.get().getWriters().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getWriters().get(1).getId()).isEqualTo(16l);
        assertThat(actual.get().getWriters().get(2).getId()).isEqualTo(27l);

        assertThat(actual.get().getActings().get(0).getActor().getId()).isEqualTo(2l);
        assertThat(actual.get().getActings().get(0).getRoles().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getActings().get(0).getRoles().get(1).getId()).isEqualTo(2l);
        assertThat(actual.get().getActings().get(0).getRoles().get(2).getId()).isEqualTo(3l);

        assertThat(actual.get().getActings().get(1).getActor().getId()).isEqualTo(25l);
        assertThat(actual.get().getActings().get(1).getRoles().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getActings().get(1).getRoles().get(1).getId()).isEqualTo(4l);
        assertThat(actual.get().getActings().get(1).getRoles().get(2).getId()).isEqualTo(6l);

        testsPassed.put("insert_Test", true);
    }

    @Test
    @Order(16)
    @DisplayName("Tests normal functionality of update method of MovieRepositoryJDBC class")
    void update_Test() {
        //Assume dependant methods executed with no errors, since i need them to test wheter or not Movie updated properly    
        Assumptions.assumeTrue(testsPassed.get("findById_Test"));
        Assumptions.assumeTrue(testsPassed.get("findByIdWithRelations_Test"));

        //Invalid inputs to insert method
        List<MovieJDBC> invalidInput = new ArrayList<MovieJDBC>() {
            {
                add(new MovieJDBC(1l, "Dummy title 0", null, "Dummy description 0", LocalDate.now(), 0, null, null));
                add(new MovieJDBC(1l, "Dummy title 1", null, "Dummy description 1", LocalDate.now(), 0, null, -1));
                add(new MovieJDBC(1l, "Dummy title 2", null, "Dummy description 2", LocalDate.now(), 0, null, -2));
                add(new MovieJDBC(1l, "Dummy title 3", null, "Dummy description 3", LocalDate.now(), 0, null, Integer.MIN_VALUE));
                add(new MovieJDBC(1l, "Dummy title 4", null, "Dummy description 4", LocalDate.now(), null, null, 0));
                add(new MovieJDBC(1l, "Dummy title 5", null, "Dummy description 5", LocalDate.now(), -1, null, 0));
                add(new MovieJDBC(1l, "Dummy title 6", null, "Dummy description 6", LocalDate.now(), -2, null, 0));
                add(new MovieJDBC(1l, "Dummy title 7", null, "Dummy description 7", LocalDate.now(), Integer.MIN_VALUE, null, 0));
                add(new MovieJDBC(1l, "Dummy title 8", null, "Dummy description 8", LocalDate.now(), 101, null, 0));
                add(new MovieJDBC(1l, "Dummy title 9", null, "Dummy description 9", LocalDate.now(), 102, null, 0));
                add(new MovieJDBC(1l, "Dummy title 10", null, "Dummy description 10", LocalDate.now(), Integer.MAX_VALUE, null, 0));
                add(new MovieJDBC(1l, "Dummy title 11", null, "Dummy description 11", null, 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 12", null, null, LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, null, null, "Dummy description 13", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(null, "Dummy title 14", "Dummy cover image 14", "Dummy description 14", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 15", "Dummy cover image 15", "Dummy description 15", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 16", "Dummy cover image 16", "Dummy description 16", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 17", "Dummy cover image 17", "Dummy description 17", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 18", "Dummy cover image 18", "Dummy description 18", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 19", "Dummy cover image 19", "Dummy description 19", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 20", "Dummy cover image 20", "Dummy description 20", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 21", "Dummy cover image 21", "Dummy description 21", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 22", "Dummy cover image 22", "Dummy description 22", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 23", "Dummy cover image 23", "Dummy description 23", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 24", "Dummy cover image 24", "Dummy description 24", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 25", "Dummy cover image 25", "Dummy description 25", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 26", "Dummy cover image 26", "Dummy description 26", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 27", "Dummy cover image 27", "Dummy description 27", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 28", "Dummy cover image 28", "Dummy description 28", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 29", "Dummy cover image 29", "Dummy description 29", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 30", "Dummy cover image 30", "Dummy description 30", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 31", "Dummy cover image 31", "Dummy description 31", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 32", "Dummy cover image 32", "Dummy description 32", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(1l, "Dummy title 33", "Dummy cover image 33", "Dummy description 33", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(3l, "Dummy title 34", "Dummy cover image 34", "Dummy description 34", LocalDate.now(), 0, null, 0));
                add(new MovieJDBC(30l, "Dummy title 35", "Dummy cover image 35", "Dummy description 35", LocalDate.now(), 0, null, 0));
            }
        };

        invalidInput.get(15).getGenres().add(new GenreJDBC(1l, "Dummy genre 0"));
        invalidInput.get(15).getGenres().add(new GenreJDBC(2l, "Dummy genre 1"));
        invalidInput.get(15).getGenres().add(new GenreJDBC(143l, "Dummy genre 2"));

        invalidInput.get(16).getGenres().add(new GenreJDBC(1l, "Dummy genre 0"));
        invalidInput.get(16).getGenres().add(new GenreJDBC(2l, "Dummy genre 1"));
        invalidInput.get(16).getGenres().add(new GenreJDBC(null, "Dummy genre 2"));

        invalidInput.get(17).getGenres().add(new GenreJDBC(1l, "Dummy genre 0"));
        invalidInput.get(17).getGenres().add(new GenreJDBC(2l, "Dummy genre 1"));
        invalidInput.get(17).getGenres().add(new GenreJDBC(1l, "Dummy genre 2"));

        //------------
        invalidInput.get(18).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(18).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(18).getDirectors().add(new DirectorJDBC(16l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(19).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(19).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(19).getDirectors().add(new DirectorJDBC(160l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(20).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(20).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(20).getDirectors().add(new DirectorJDBC(null, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(21).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(21).getDirectors().add(new DirectorJDBC(14l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(21).getDirectors().add(new DirectorJDBC(1l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        //-------
        invalidInput.get(22).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(22).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(22).getWriters().add(new WriterJDBC(20l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(23).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(23).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(23).getWriters().add(new WriterJDBC(200l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(24).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(24).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(24).getWriters().add(new WriterJDBC(null, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        invalidInput.get(25).getWriters().add(new WriterJDBC(1l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0"));
        invalidInput.get(25).getWriters().add(new WriterJDBC(16l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1"));
        invalidInput.get(25).getWriters().add(new WriterJDBC(1l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2"));

        //-----------
        invalidInput.get(26).getActings().add(new ActingJDBC(invalidInput.get(25), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(26).getActings().add(new ActingJDBC(invalidInput.get(25), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(26).getActings().add(new ActingJDBC(invalidInput.get(25), new ActorJDBC(14l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(27).getActings().add(new ActingJDBC(invalidInput.get(26), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(27).getActings().add(new ActingJDBC(invalidInput.get(26), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(27).getActings().add(new ActingJDBC(invalidInput.get(26), new ActorJDBC(140l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(28).getActings().add(new ActingJDBC(invalidInput.get(27), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(28).getActings().add(new ActingJDBC(invalidInput.get(27), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(28).getActings().add(new ActingJDBC(invalidInput.get(27), new ActorJDBC(null, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(29).getActings().add(new ActingJDBC(invalidInput.get(28), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(29).getActings().add(new ActingJDBC(invalidInput.get(28), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(29).getActings().add(new ActingJDBC(invalidInput.get(28), new ActorJDBC(2l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), true));

        invalidInput.get(30).getActings().add(new ActingJDBC(invalidInput.get(29), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true));
        invalidInput.get(30).getActings().add(new ActingJDBC(invalidInput.get(29), new ActorJDBC(3l, "Dummy first name 1", "Dummy last name 1", Gender.OTHER, "Dummy profile photo 1", true), true));
        invalidInput.get(30).getActings().add(new ActingJDBC(invalidInput.get(29), new ActorJDBC(4l, "Dummy first name 2", "Dummy last name 2", Gender.OTHER, "Dummy profile photo 2", true), null));

        //-------
        ActingJDBC invalidActing = new ActingJDBC(invalidInput.get(31), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true);
        invalidInput.get(31).getActings().add(invalidActing);
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 0"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 2l, "Dummy name 1"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 3l, null));

        invalidActing = new ActingJDBC(invalidInput.get(32), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true);
        invalidInput.get(32).getActings().add(invalidActing);
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 0"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 2l, "Dummy name 1"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, null, "Dummy name 2"));

        invalidActing = new ActingJDBC(invalidInput.get(33), new ActorJDBC(2l, "Dummy first name 0", "Dummy last name 0", Gender.OTHER, "Dummy profile photo 0", true), true);
        invalidInput.get(33).getActings().add(invalidActing);
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 0"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 2l, "Dummy name 1"));
        invalidActing.getRoles().add(new ActingRoleJDBC(invalidActing, 1l, "Dummy name 2"));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.update(null);
        }).withMessage("Invalid parameter: entity must be non-null");

        for (int iter = 0; iter < invalidInput.size(); iter++) {
            final int i = iter;
            String message;
            if (i == 14 || i >= 34) {
                message = "Error while updating movie with id: " + invalidInput.get(i).getId() + ". No movie found with given id";
            } else {
                message = "Error while updating movie with id: " + invalidInput.get(i).getId();
            }
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.update(invalidInput.get(i));
            }).withMessage(message);
        }

        //VALID inputs to update method
        //1. ID is given as already present one in DB; rest values are valid; No relations
        MovieJDBC validInput = new MovieJDBC(1l, "Dummy title 33", null, "Dummy description 33", LocalDate.now(), 15, null, 140);
        repo.update(validInput);
        Optional<MovieJDBC> actual = repo.findByIdWithRelations(1l);
        assertThat(actual.isPresent()).isTrue();
        checkValues(actual.get(), validInput);

        //2. ID is given as already present one in DB; rest values are valid; With relations
        validInput = new MovieJDBC(1l, "Dummy title 33", "2.png", "Dummy description 33", LocalDate.now(), 15, null, 140);
        validInput.getGenres().add(new GenreJDBC(1l, "Action"));
        validInput.getGenres().add(new GenreJDBC(3l, "Animation"));
        validInput.getGenres().add(new GenreJDBC(4l, "Comedy"));

        validInput.getDirectors().add(new DirectorJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(14l, "Pascal", "Charrue", Gender.MALE, "14.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(15l, "Arnaud", "Delord", Gender.MALE, "15.jpg"));

        validInput.getWriters().add(new WriterJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getWriters().add(new WriterJDBC(16l, "Mollie Bickley", "St. John", Gender.FEMALE, null));
        validInput.getWriters().add(new WriterJDBC(27l, "Max", "Eggers", Gender.MALE, "27.jpg"));

        ActingJDBC validActing = new ActingJDBC(validInput, new ActorJDBC(2l, "Naomi", "Watts", Gender.FEMALE, "2.jpg", true), true);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 0"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 2l, "Dummy name 2"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 3l, "Dummy name 1"));

        validActing = new ActingJDBC(validInput, new ActorJDBC(25l, "Mick", "Wingert", Gender.MALE, "25.jpg", false), false);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 2"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 4l, "Dummy name 1"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 6l, "Dummy name 0"));

        repo.update(validInput);
        actual = repo.findByIdWithRelations(1l);
        assertThat(actual.isPresent()).isTrue();
        checkValues(actual.get(), validInput);

        //4. Check if returned movie after update ordered ids correctly
        validInput = new MovieJDBC(2l, "Dummy title 33", "2.png", "Dummy description 33", LocalDate.now(), 15, null, 140);
        validInput.getGenres().add(new GenreJDBC(1l, "Action"));
        validInput.getGenres().add(new GenreJDBC(4l, "Comedy"));
        validInput.getGenres().add(new GenreJDBC(3l, "Animation"));

        validInput.getDirectors().add(new DirectorJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(15l, "Arnaud", "Delord", Gender.MALE, "15.jpg"));
        validInput.getDirectors().add(new DirectorJDBC(14l, "Pascal", "Charrue", Gender.MALE, "14.jpg"));

        validInput.getWriters().add(new WriterJDBC(16l, "Mollie Bickley", "St. John", Gender.FEMALE, null));
        validInput.getWriters().add(new WriterJDBC(1l, "David", "Lynch", Gender.MALE, "1.jpg"));
        validInput.getWriters().add(new WriterJDBC(27l, "Max", "Eggers", Gender.MALE, "27.jpg"));

        validActing = new ActingJDBC(validInput, new ActorJDBC(2l, "Naomi", "Watts", Gender.FEMALE, "2.jpg", true), true);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 0"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 3l, "Dummy name 1"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 2l, "Dummy name 2"));

        validActing = new ActingJDBC(validInput, new ActorJDBC(25l, "Mick", "Wingert", Gender.MALE, "25.jpg", false), false);
        validInput.getActings().add(validActing);
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 6l, "Dummy name 0"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 4l, "Dummy name 1"));
        validActing.getRoles().add(new ActingRoleJDBC(validActing, 1l, "Dummy name 2"));

        repo.update(validInput);
        actual = repo.findByIdWithRelations(2l);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getGenres().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getGenres().get(1).getId()).isEqualTo(3l);
        assertThat(actual.get().getGenres().get(2).getId()).isEqualTo(4l);

        assertThat(actual.get().getDirectors().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getDirectors().get(1).getId()).isEqualTo(14l);
        assertThat(actual.get().getDirectors().get(2).getId()).isEqualTo(15l);

        assertThat(actual.get().getWriters().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getWriters().get(1).getId()).isEqualTo(16l);
        assertThat(actual.get().getWriters().get(2).getId()).isEqualTo(27l);

        assertThat(actual.get().getActings().get(0).getActor().getId()).isEqualTo(2l);
        assertThat(actual.get().getActings().get(0).getRoles().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getActings().get(0).getRoles().get(1).getId()).isEqualTo(2l);
        assertThat(actual.get().getActings().get(0).getRoles().get(2).getId()).isEqualTo(3l);

        assertThat(actual.get().getActings().get(1).getActor().getId()).isEqualTo(25l);
        assertThat(actual.get().getActings().get(1).getRoles().get(0).getId()).isEqualTo(1l);
        assertThat(actual.get().getActings().get(1).getRoles().get(1).getId()).isEqualTo(4l);
        assertThat(actual.get().getActings().get(1).getRoles().get(2).getId()).isEqualTo(6l);

        testsPassed.put("update_Test", true);
    }

    @Test
    @Order(17)
    @DisplayName("Tests normal functionality of updateCoverImage method of MovieRepositoryJDBC class")
    void updateCoverImage_Test() {
        //Assumes findbyIdCoverImage_Test passed with no problems, as i need that method to check if cover image updated corectly
        Assumptions.assumeTrue(testsPassed.get("findByIdCoverImage_Test"));

        Object[][] invalidInput1 = {
            {null, "Dummy cover image 0"},
            {0l, "Dummy cover image 1"},
            {-1l, "Dummy cover image 2"},
            {-2l, "Dummy cover image 3"},
            {Long.MIN_VALUE, "Dummy cover image 3"}
        };

        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.updateCoverImage((Long) invalidInput1[i][0], (String) invalidInput1[i][1]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }

        //Non existant movie IDs in database and invalid cover image values
        StringBuilder sb = new StringBuilder(510);
        for (int i = 0; i < 510; i++) {
            sb.append('a'); // or any other character
        }
        Object[][] invalidInput2 = {
            {3l, "Dummy cover image 0"},
            {6l, "Dummy cover image 1"},
            {150l, "Dummy cover image 2"},
            {Long.MAX_VALUE, "Dummy cover image 3"},
            {1l, sb.toString()}
        };

        for (int iter = 0; iter < invalidInput2.length; iter++) {
            final int i = iter;
            String message;
            if (i <= 3) {
                message = "Error while updating cover image for movie with id: " + invalidInput2[i][0] + ". No movie found with given id";
            } else {
                message = "Error while updating cover image for movie with id: " + invalidInput2[i][0];
            }
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.updateCoverImage((Long) invalidInput2[i][0], (String) invalidInput2[i][1]);
            }).withMessage(message);
        }

        //VALID values
        Optional<String> actual = repo.findByIdCoverImage(1l);
        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(init.getMullholadDrive().getCoverImage());

        repo.updateCoverImage(1l, "Dummy cover image");
        actual = repo.findByIdCoverImage(1l);
        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo("Dummy cover image");

        repo.updateCoverImage(1l, null);
        actual = repo.findByIdCoverImage(1l);
        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isTrue();

        testsPassed.put("updateCoverImage_Test", true);
    }

    @Test
    @Order(18)
    @DisplayName("Tests normal functionality of deleteById method of MovieRepositoryJDBC class")
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
        invalidInput = new Long[]{3l, 6l, 150l, Long.MAX_VALUE};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            final Long id = invalidInput[i];
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.deleteById(id);
            }).withMessage("Error while deleting movie with id: " + id + ". No movie found with given id");
        }

        Long id = 1l;
        assertThat(repo.existsById(id)).isTrue();
        repo.deleteById(id);
        assertThat(repo.existsById(id)).isFalse();

        id = 2l;
        assertThat(repo.existsById(id)).isTrue();
        repo.deleteById(id);
        assertThat(repo.existsById(id)).isFalse();

        testsPassed.put("deleteById_Test", true);
    }

//==============================================================================================================
//===========================================PRIVATE METHODS====================================================
//==============================================================================================================
    //expect Lists to be non null and non empty
    private void checkValues(List<MovieJDBC> actual, List<MovieJDBC> expected) {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(actual.size() == expected.size()).isTrue();
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getTitle()).isEqualTo(expected.get(i).getTitle());
            assertThat(actual.get(i).getCoverImage()).isEqualTo(expected.get(i).getCoverImage());
            assertThat(actual.get(i).getReleaseDate()).isEqualTo(expected.get(i).getReleaseDate());
            assertThat(actual.get(i).getDescription()).isEqualTo(expected.get(i).getDescription());
            assertThat(actual.get(i).getAudienceRating()).isEqualTo(expected.get(i).getAudienceRating());
            assertThat(actual.get(i).getLength()).isEqualTo(expected.get(i).getLength());
            assertThat(actual.get(i).getCriticRating()).isNull();

            assertThat(actual.get(i).getGenres()).isNotNull().isEmpty();
            assertThat(actual.get(i).getDirectors()).isNotNull().isEmpty();
            assertThat(actual.get(i).getWriters()).isNotNull().isEmpty();
            assertThat(actual.get(i).getCritiques()).isNotNull().isEmpty();
            assertThat(actual.get(i).getActings()).isNotNull().isEmpty();
        }
    }

    //expect Lists to be non null and non empty
    private void checkValuesWithGenres(List<MovieJDBC> actual, List<MovieJDBC> expected) {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(actual.size() == expected.size()).isTrue();
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getTitle()).isEqualTo(expected.get(i).getTitle());
            assertThat(actual.get(i).getCoverImage()).isEqualTo(expected.get(i).getCoverImage());
            assertThat(actual.get(i).getReleaseDate()).isEqualTo(expected.get(i).getReleaseDate());
            assertThat(actual.get(i).getDescription()).isEqualTo(expected.get(i).getDescription());
            assertThat(actual.get(i).getAudienceRating()).isEqualTo(expected.get(i).getAudienceRating());
            assertThat(actual.get(i).getLength()).isEqualTo(expected.get(i).getLength());
            assertThat(actual.get(i).getCriticRating()).isNull();

            assertThat(actual.get(i).getGenres()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getGenres().size() == expected.get(i).getGenres().size()).isTrue();

            for (int j = 0; j < actual.get(i).getGenres().size(); j++) {
                assertThat(actual.get(i).getGenres().get(j)).isNotNull();
                assertThat(actual.get(i).getGenres().get(j).getId()).isEqualTo(expected.get(i).getGenres().get(j).getId());
                assertThat(actual.get(i).getGenres().get(j).getName()).isEqualTo(expected.get(i).getGenres().get(j).getName());
            }
            assertThat(actual.get(i).getDirectors()).isNotNull().isEmpty();
            assertThat(actual.get(i).getWriters()).isNotNull().isEmpty();
            assertThat(actual.get(i).getCritiques()).isNotNull().isEmpty();
            assertThat(actual.get(i).getActings()).isNotNull().isEmpty();
        }
    }

    //expect Lists to be non null and non empty
    private void checkValuesWithRelations(List<MovieJDBC> actual, List<MovieJDBC> expected) {
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(actual.size() == expected.size()).isTrue();

        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.get(i).getId());
            assertThat(actual.get(i).getTitle()).isEqualTo(expected.get(i).getTitle());
            assertThat(actual.get(i).getCoverImage()).isEqualTo(expected.get(i).getCoverImage());
            assertThat(actual.get(i).getReleaseDate()).isEqualTo(expected.get(i).getReleaseDate());
            assertThat(actual.get(i).getDescription()).isEqualTo(expected.get(i).getDescription());
            assertThat(actual.get(i).getAudienceRating()).isEqualTo(expected.get(i).getAudienceRating());
            assertThat(actual.get(i).getLength()).isEqualTo(expected.get(i).getLength());
            assertThat(actual.get(i).getCriticRating()).isNull();

            assertThat(actual.get(i).getGenres()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getGenres().size() == expected.get(i).getGenres().size()).isTrue();

            for (int j = 0; j < actual.get(i).getGenres().size(); j++) {
                assertThat(actual.get(i).getGenres().get(j)).isNotNull();
                assertThat(actual.get(i).getGenres().get(j).getId()).isEqualTo(expected.get(i).getGenres().get(j).getId());
                assertThat(actual.get(i).getGenres().get(j).getName()).isEqualTo(expected.get(i).getGenres().get(j).getName());
            }

            assertThat(actual.get(i).getDirectors()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getDirectors().size() == expected.get(i).getDirectors().size()).isTrue();
            for (int j = 0; j < actual.get(i).getDirectors().size(); j++) {
                assertThat(actual.get(i).getDirectors().get(j)).isNotNull();
                assertThat(actual.get(i).getDirectors().get(j).getId()).isEqualTo(expected.get(i).getDirectors().get(j).getId());
                assertThat(actual.get(i).getDirectors().get(j).getFirstName()).isEqualTo(expected.get(i).getDirectors().get(j).getFirstName());
                assertThat(actual.get(i).getDirectors().get(j).getLastName()).isEqualTo(expected.get(i).getDirectors().get(j).getLastName());
                assertThat(actual.get(i).getDirectors().get(j).getGender()).isEqualTo(expected.get(i).getDirectors().get(j).getGender());
                assertThat(actual.get(i).getDirectors().get(j).getProfilePhoto()).isEqualTo(expected.get(i).getDirectors().get(j).getProfilePhoto());
            }

            assertThat(actual.get(i).getWriters()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getWriters().size() == expected.get(i).getWriters().size()).isTrue();
            for (int j = 0; j < actual.get(i).getWriters().size(); j++) {
                assertThat(actual.get(i).getWriters().get(j)).isNotNull();
                assertThat(actual.get(i).getWriters().get(j).getId()).isEqualTo(expected.get(i).getWriters().get(j).getId());
                assertThat(actual.get(i).getWriters().get(j).getFirstName()).isEqualTo(expected.get(i).getWriters().get(j).getFirstName());
                assertThat(actual.get(i).getWriters().get(j).getLastName()).isEqualTo(expected.get(i).getWriters().get(j).getLastName());
                assertThat(actual.get(i).getWriters().get(j).getGender()).isEqualTo(expected.get(i).getWriters().get(j).getGender());
                assertThat(actual.get(i).getWriters().get(j).getProfilePhoto()).isEqualTo(expected.get(i).getWriters().get(j).getProfilePhoto());
            }

            assertThat(actual.get(i).getActings()).isNotNull().isNotEmpty();
            assertThat(actual.get(i).getActings().size() == expected.get(i).getActings().size()).isTrue();
            for (int j = 0; j < actual.get(i).getActings().size(); j++) {
                assertThat(actual.get(i).getActings().get(j)).isNotNull();
                assertThat(actual.get(i).getActings().get(j).isStarring()).isEqualTo(expected.get(i).getActings().get(j).isStarring());

                assertThat(actual.get(i).getActings().get(j).getActor()).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getActor().getId()).isEqualTo(expected.get(i).getActings().get(j).getActor().getId());
                assertThat(actual.get(i).getActings().get(j).getActor().getFirstName()).isEqualTo(expected.get(i).getActings().get(j).getActor().getFirstName());
                assertThat(actual.get(i).getActings().get(j).getActor().getLastName()).isEqualTo(expected.get(i).getActings().get(j).getActor().getLastName());
                assertThat(actual.get(i).getActings().get(j).getActor().getGender()).isEqualTo(expected.get(i).getActings().get(j).getActor().getGender());
                assertThat(actual.get(i).getActings().get(j).getActor().getProfilePhoto()).isEqualTo(expected.get(i).getActings().get(j).getActor().getProfilePhoto());
                assertThat(actual.get(i).getActings().get(j).getActor().isStar()).isEqualTo(expected.get(i).getActings().get(j).getActor().isStar());

                assertThat(actual.get(i).getActings().get(j).getMedia()).isNotNull();
                assertThat(actual.get(i).getActings().get(j).getMedia() == actual.get(i)).isTrue();

                assertThat(actual.get(i).getActings().get(j).getRoles()).isNotNull().isNotEmpty();
                assertThat(actual.get(i).getActings().get(j).getRoles().size() == expected.get(i).getActings().get(j).getRoles().size()).isTrue();
                for (int k = 0; k < actual.get(i).getActings().get(j).getRoles().size(); k++) {
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k)).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getActing()).isNotNull();
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getActing() == actual.get(i).getActings().get(j)).isTrue();

                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getId()).isEqualTo(expected.get(i).getActings().get(j).getRoles().get(k).getId());
                    assertThat(actual.get(i).getActings().get(j).getRoles().get(k).getName()).isEqualTo(actual.get(i).getActings().get(j).getRoles().get(k).getName());
                }
            }

            assertThat(actual.get(i).getCritiques()).isNotNull().isEmpty();

        }
    }

    private void checkValues(MovieJDBC actual, MovieJDBC expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isGreaterThan(0).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getCoverImage()).isEqualTo(expected.getCoverImage());
        assertThat(actual.getReleaseDate()).isEqualTo(expected.getReleaseDate());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getAudienceRating()).isEqualTo(expected.getAudienceRating());
        assertThat(actual.getLength()).isEqualTo(expected.getLength());
        assertThat(actual.getCriticRating()).isNull();

        assertThat(actual.getGenres()).isNotNull();
        assertThat(actual.getGenres().size() == expected.getGenres().size()).isTrue();

        for (int i = 0; i < actual.getGenres().size(); i++) {
            assertThat(actual.getGenres().get(i)).isNotNull();
            assertThat(actual.getGenres().get(i).getId()).isEqualTo(expected.getGenres().get(i).getId());
            assertThat(actual.getGenres().get(i).getName()).isEqualTo(expected.getGenres().get(i).getName());
        }

        assertThat(actual.getDirectors()).isNotNull();
        assertThat(actual.getDirectors().size() == expected.getDirectors().size()).isTrue();
        for (int i = 0; i < actual.getDirectors().size(); i++) {
            assertThat(actual.getDirectors().get(i)).isNotNull();
            assertThat(actual.getDirectors().get(i).getId()).isEqualTo(expected.getDirectors().get(i).getId());
            assertThat(actual.getDirectors().get(i).getFirstName()).isEqualTo(expected.getDirectors().get(i).getFirstName());
            assertThat(actual.getDirectors().get(i).getLastName()).isEqualTo(expected.getDirectors().get(i).getLastName());
            assertThat(actual.getDirectors().get(i).getGender()).isEqualTo(expected.getDirectors().get(i).getGender());
            assertThat(actual.getDirectors().get(i).getProfilePhoto()).isEqualTo(expected.getDirectors().get(i).getProfilePhoto());
        }

        assertThat(actual.getWriters()).isNotNull();
        assertThat(actual.getWriters().size() == expected.getWriters().size()).isTrue();
        for (int i = 0; i < actual.getWriters().size(); i++) {
            assertThat(actual.getWriters().get(i)).isNotNull();
            assertThat(actual.getWriters().get(i).getId()).isEqualTo(expected.getWriters().get(i).getId());
            assertThat(actual.getWriters().get(i).getFirstName()).isEqualTo(expected.getWriters().get(i).getFirstName());
            assertThat(actual.getWriters().get(i).getLastName()).isEqualTo(expected.getWriters().get(i).getLastName());
            assertThat(actual.getWriters().get(i).getGender()).isEqualTo(expected.getWriters().get(i).getGender());
            assertThat(actual.getWriters().get(i).getProfilePhoto()).isEqualTo(expected.getWriters().get(i).getProfilePhoto());
        }

        assertThat(actual.getActings()).isNotNull();
        assertThat(actual.getActings().size() == expected.getActings().size()).isTrue();
        for (int i = 0; i < actual.getActings().size(); i++) {
            assertThat(actual.getActings().get(i)).isNotNull();
            assertThat(actual.getActings().get(i).isStarring()).isEqualTo(expected.getActings().get(i).isStarring());

            assertThat(actual.getActings().get(i).getActor()).isNotNull();
            assertThat(actual.getActings().get(i).getActor().getId()).isEqualTo(expected.getActings().get(i).getActor().getId());
            assertThat(actual.getActings().get(i).getActor().getFirstName()).isEqualTo(expected.getActings().get(i).getActor().getFirstName());
            assertThat(actual.getActings().get(i).getActor().getLastName()).isEqualTo(expected.getActings().get(i).getActor().getLastName());
            assertThat(actual.getActings().get(i).getActor().getGender()).isEqualTo(expected.getActings().get(i).getActor().getGender());
            assertThat(actual.getActings().get(i).getActor().getProfilePhoto()).isEqualTo(expected.getActings().get(i).getActor().getProfilePhoto());
            assertThat(actual.getActings().get(i).getActor().isStar()).isEqualTo(expected.getActings().get(i).getActor().isStar());

            assertThat(actual.getActings().get(i).getMedia()).isNotNull();
            assertThat(actual.getActings().get(i).getMedia() == actual).isTrue();

            assertThat(actual.getActings().get(i).getRoles()).isNotNull();
            assertThat(actual.getActings().get(i).getRoles().size() == expected.getActings().get(i).getRoles().size()).isTrue();
            for (int j = 0; j < actual.getActings().get(i).getRoles().size(); j++) {
                assertThat(actual.getActings().get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getActing()).isNotNull();
                assertThat(actual.getActings().get(i).getRoles().get(j).getActing() == actual.getActings().get(i)).isTrue();

                assertThat(actual.getActings().get(i).getRoles().get(j).getId()).isEqualTo(expected.getActings().get(i).getRoles().get(j).getId());
                assertThat(actual.getActings().get(i).getRoles().get(j).getName()).isEqualTo(actual.getActings().get(i).getRoles().get(j).getName());
            }
        }

        assertThat(actual.getCritiques()).isNotNull().isEmpty();

    }

}
