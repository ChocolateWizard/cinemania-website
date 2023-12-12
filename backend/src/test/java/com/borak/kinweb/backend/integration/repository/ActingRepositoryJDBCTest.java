/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.ConfigPropertiesTest;
import com.borak.kinweb.backend.domain.jdbc.classes.ActingJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.repository.jdbc.ActingRepositoryJDBC;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.AfterAll;
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
@Order(8)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class ActingRepositoryJDBCTest {

    @Autowired
    private ActingRepositoryJDBC repo;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("findAllByMediaId_Test", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
//=================================================================================================
    
    @BeforeEach
    void beforeEach() {
        Assumptions.assumeTrue(ConfigPropertiesTest.didAllTestsPass());
    }

    @Test
    @Order(1)
    @DisplayName("Tests normal functionality of findAllByMediaId method of ActingRepositoryJDBC class")
    void findAllByMediaId_Test() {
        final Long[] invalidInput1 = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput1[i]).isThrownBy(() -> {
                repo.findAllByMediaId(invalidInput1[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        List<ActingJDBC> expected;
        List<ActingJDBC> actual;
        for (MediaJDBC media : init.getMedias()) {
            expected = media.getActings();
            actual = repo.findAllByMediaId(media.getId());
            checkValues(actual, expected);
        }

        actual = repo.findAllByMediaId(10l);
        assertThat(actual).isNotNull().isEmpty();

        actual = repo.findAllByMediaId(150l);
        assertThat(actual).isNotNull().isEmpty();

        actual = repo.findAllByMediaId(Long.MAX_VALUE);
        assertThat(actual).isNotNull().isEmpty();

        testsPassed.put("findAllByMediaId_Test", true);
    }

    private void checkValues(List<ActingJDBC> actual, List<ActingJDBC> expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.size() == expected.size()).isTrue();
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isNotNull();
            assertThat(actual.get(i).isStarring()).isEqualTo(expected.get(i).isStarring());

            assertThat(actual.get(i).getActor()).isNotNull();
            assertThat(actual.get(i).getActor().getId()).isEqualTo(expected.get(i).getActor().getId());
            assertThat(actual.get(i).getActor().getFirstName()).isEqualTo(expected.get(i).getActor().getFirstName());
            assertThat(actual.get(i).getActor().getLastName()).isEqualTo(expected.get(i).getActor().getLastName());
            assertThat(actual.get(i).getActor().getGender()).isEqualTo(expected.get(i).getActor().getGender());
            assertThat(actual.get(i).getActor().getProfilePhoto()).isEqualTo(expected.get(i).getActor().getProfilePhoto());
            assertThat(actual.get(i).getActor().isStar()).isEqualTo(expected.get(i).getActor().isStar());

            assertThat(actual.get(i).getMedia()).isNull();

            assertThat(actual.get(i).getRoles()).isNotNull();
            assertThat(actual.get(i).getRoles().size() == expected.get(i).getRoles().size()).isTrue();
            for (int j = 0; j < actual.get(i).getRoles().size(); j++) {
                assertThat(actual.get(i).getRoles().get(j)).isNotNull();
                assertThat(actual.get(i).getRoles().get(j).getActing()).isNotNull();
                assertThat(actual.get(i).getRoles().get(j).getActing() == actual.get(i)).isTrue();

                assertThat(actual.get(i).getRoles().get(j).getId()).isEqualTo(expected.get(i).getRoles().get(j).getId());
                assertThat(actual.get(i).getRoles().get(j).getName()).isEqualTo(actual.get(i).getRoles().get(j).getName());
            }
        }
    }

}
