/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend;

import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.initializer.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.MovieRepositoryJDBC;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
@Order(2)
public class MovieRepositoryJDBCTest {

    @Autowired
    private MovieRepositoryJDBC repo;

    private final DataInitializer init = new DataInitializer();

    @Test
    @DisplayName("Tests normal functionality of findAll method of MovieRepositoryJDBC class")
    void findAll_Test() {
        List<MovieJDBC> actual = repo.findAll();
        List<MovieJDBC> expected = init.getMovies();

        checkValues(actual, expected);
    }

    @Test
    @DisplayName("Tests normal functionality of findAllWithGenres method of MovieRepositoryJDBC class")
    void findAllWithGenres_Test() {
        List<MovieJDBC> actual = repo.findAllWithGenres();
        List<MovieJDBC> expected = init.getMovies();

        checkValuesWithGenres(actual, expected);
    }

    @Test
    @DisplayName("Tests normal functionality of findAllWithRelations method of MovieRepositoryJDBC class")
    void findAllWithRelations_Test() {
        List<MovieJDBC> actual = repo.findAllWithRelations();
        List<MovieJDBC> expected = init.getMovies();

        checkValuesWithRelations(actual, expected);
    }

    @Test
    @DisplayName("Tests normal functionality of count method of MovieRepositoryJDBC class")
    void count_Test() {
        long actual = repo.count();
        long expected = init.getMovies().size();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
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

    }

    @Test
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

    }

    @Test
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

    }

    @Test
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

    }

    @Test
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

    }

    @Test
    @DisplayName("Tests normal functionality of findByIdCoverImage method of MovieRepositoryJDBC class")
    void findByIdCoverImage_Test() {
        //(id)
        final Long[] invalidInput = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput[i]).isThrownBy(() -> {
                repo.findByIdCoverImage(invalidInput[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }

        Long id;
        String expected;
        Optional<String> actual;

        id = 1l;
        expected = init.getMullholadDrive().getCoverImage();
        actual = repo.findByIdCoverImage(id);
        assertThat(actual.get()).isNotNull().isNotEmpty().isEqualTo(expected);

        id = 2l;
        expected = init.getInlandEmpire().getCoverImage();
        actual = repo.findByIdCoverImage(id);
        assertThat(actual.get()).isNotNull().isNotEmpty().isEqualTo(expected);

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

}
