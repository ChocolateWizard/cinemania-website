/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.MediaRepositoryJDBC;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MediaRepositoryJDBCTest {

    @Autowired
    private MediaRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("existsById_Test", false);
        testsPassed.put("findAllByTitleWithGenresPaginated_Test", false);
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
    @DisplayName("Tests normal functionality of findAllByTitleWithGenresPaginated method of MediaRepositoryJDBC class")
    void findAllByTitleWithGenresPaginated_Test() {
        //(page,size)
        final Object[][] invalidInput = {
            {0, 0, ""},
            {-1, -1, ""},
            {0, -1, ""},
            {-1, 0, ""},
            {-2, -2, ""},
            {-2, -1, ""},
            {-1, -2, ""},
            {Integer.MIN_VALUE, -2, ""},
            {-2, Integer.MIN_VALUE, ""},
            {Integer.MIN_VALUE, Integer.MIN_VALUE, ""},
            {0, 1, ""},
            {0, 2, ""},
            {0, Integer.MAX_VALUE, ""},
            {1, -1, ""},
            {-1, 1, ""},
            {2, -2, ""},
            {-2, 2, ""},
            {Integer.MAX_VALUE, Integer.MIN_VALUE, ""},
            {Integer.MIN_VALUE, Integer.MAX_VALUE, ""},
            {1, 1, null}
        };
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s,%s)", invalidInput[i][0], invalidInput[i][1], invalidInput[i][2]).isThrownBy(() -> {
                repo.findAllByTitleWithGenresPaginated((Integer) invalidInput[i][0], (Integer) invalidInput[i][1], (invalidInput[i][2] == null) ? null : String.valueOf(invalidInput[i][2]));
            }).withMessage("Invalid parameters: page must be greater than 0, size must be non-negative and title must be non-null");
        }
        //(page,size)
        final Object[][] emptyListInput = {
            {1, 0, ""},
            {2, 0, ""},
            {Integer.MAX_VALUE, 0, ""},
            {7, 1, ""},
            {Integer.MAX_VALUE, 1, ""},
            {4, 2, ""},
            {Integer.MAX_VALUE, 2, ""},
            {3, 3, ""},
            {4, 3, ""},
            {Integer.MAX_VALUE, 3, ""},
            {3, 4, ""},
            {4, 4, ""},
            {5, 4, ""},
            {Integer.MAX_VALUE, 4, ""},
            {2, Integer.MAX_VALUE, ""},
            {3, Integer.MAX_VALUE, ""},
            {Integer.MAX_VALUE, Integer.MAX_VALUE, ""}
        };
        for (int iter = 0; iter < emptyListInput.length; iter++) {
            final int i = iter;
            List<MediaJDBC> actualList = repo.findAllByTitleWithGenresPaginated((Integer) emptyListInput[i][0], (Integer) emptyListInput[i][1], String.valueOf(emptyListInput[i][2]));
            assertThat(actualList).as("Code input values (%s,%s,%s)", emptyListInput[i][0], emptyListInput[i][1], emptyListInput[i][2]).isNotNull().isEmpty();
        }

        int page;
        int size;
        String title = "";
        MediaJDBC expectedObject;
        List<MediaJDBC> actualList;
        List<MediaJDBC> expectedList;

        page = 1;
        size = 1;
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 2;
        size = 1;
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 3;
        size = 1;
        expectedObject = init.getArcane();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        expectedList = init.getMedias().subList(0, 2);
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, expectedList);

        page = 2;
        size = 2;
        expectedList = init.getMedias().subList(2, 4);
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 3;
        expectedList = init.getMedias().subList(0, 3);
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 4;
        expectedList = init.getMedias().subList(0, 4);
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = Integer.MAX_VALUE;
        expectedList = init.getMedias();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, expectedList);

        page = 1;
        size = 1;
        title = "the";
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        title = "the";
        expectedObject = init.getTheLighthouse();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        title = "Inland";
        expectedObject = init.getInlandEmpire();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        title = "South";
        expectedObject = init.getSouthPark();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 2;
        title = "Drive";
        expectedObject = init.getMullholadDrive();
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, Arrays.asList(expectedObject));

        page = 1;
        size = 7;
        title = "a";
        expectedList = Arrays.asList(init.getMullholadDrive(), init.getInlandEmpire(), init.getArcane(), init.getSouthPark());
        actualList = repo.findAllByTitleWithGenresPaginated(page, size, title);
        checkValuesWithGenres(actualList, expectedList);

        testsPassed.put("findAllByTitleWithGenresPaginated_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of existsById method of MediaRepositoryJDBC class")
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
        for (MediaJDBC media : init.getMedias()) {
            id = media.getId();
            actual = repo.existsById(id);
            assertThat(actual).isTrue();
        }

        actual = repo.existsById(id + 1l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 2l);
        assertThat(actual).isFalse();

        actual = repo.existsById(id + 20l);
        assertThat(actual).isFalse();

        id = Long.MAX_VALUE;
        actual = repo.existsById(id);
        assertThat(actual).isFalse();
        testsPassed.put("existsById_Test", true);
    }

//==============================================================================================================
//===========================================PRIVATE METHODS====================================================
//==============================================================================================================
    //expect Lists to be non null and non empty
    private void checkValuesWithGenres(List<MediaJDBC> actual, List<MediaJDBC> expected) {
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

}
