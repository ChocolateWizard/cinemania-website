/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.repository;

import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.domain.jdbc.classes.CountryJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MovieJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.TVShowJDBC;
import static org.assertj.core.api.Assertions.assertThat;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.exceptions.DatabaseException;
import com.borak.kinweb.backend.helpers.DataInitializer;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.repository.jdbc.UserRepositoryJDBC;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserRepositoryJDBCTest {

    @Autowired
    private UserRepositoryJDBC repo;
    @Autowired
    private PasswordEncoder pswEncoder;
    private final DataInitializer init = new DataInitializer();
    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("existsUsername_Test", false);
        testsPassed.put("existsEmail_Test", false);
        testsPassed.put("existsProfileName_Test", false);
        testsPassed.put("findByUsername_Test", false);
        testsPassed.put("findByIdWithRelations_Test", false);
        testsPassed.put("insert_Test", false);
        testsPassed.put("existsMediaInLibrary_Test", false);
        testsPassed.put("addMediaToLibrary_Test", false);
        testsPassed.put("removeMediaFromLibrary_Test", false);
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
    @DisplayName("Tests normal functionality of existsUsername method of UserRepositoryJDBC class")
    void existsUsername_Test() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.existsUsername(null);
        }).withMessage("Invalid parameter: username must be non-null");

        for (UserJDBC user : init.getUsers()) {
            boolean actual = repo.existsUsername(user.getUsername());
            assertThat(actual).isTrue();
        }
        String[] falseInputs = new String[]{"", "a", "  a   ", "    ", "  aasdasd   asdas ", "aaaaaaaa", "Unknown username", "SomeUsername", "admi", "nadmin"};
        for (String input : falseInputs) {
            boolean actual = repo.existsUsername(input);
            assertThat(actual).isFalse();
        }

        testsPassed.put("existsUsername_Test", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests normal functionality of existsEmail method of UserRepositoryJDBC class")
    void existsEmail_Test() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.existsEmail(null);
        }).withMessage("Invalid parameter: email must be non-null");

        for (UserJDBC user : init.getUsers()) {
            boolean actual = repo.existsEmail(user.getEmail());
            assertThat(actual).isTrue();
        }
        String[] falseInputs = new String[]{"", "a", "  a   ", "    ", "  aasdasd   asdas ", "aaaaaaaa", "Unknown email", "SomeEmail", "admi", "nadmin",
            "@gmail.com", "a@gmail.com", "admi@gmail.com", "admin@gmail.co", "dmina@gmail.com", "admin@yahoo.com"};
        for (String input : falseInputs) {
            boolean actual = repo.existsEmail(input);
            assertThat(actual).isFalse();
        }

        testsPassed.put("existsEmail_Test", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests normal functionality of existsProfileName method of UserRepositoryJDBC class")
    void existsProfileName_Test() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.existsProfileName(null);
        }).withMessage("Invalid parameter: profile name must be non-null");

        for (UserJDBC user : init.getUsers()) {
            boolean actual = repo.existsProfileName(user.getProfileName());
            assertThat(actual).isTrue();
        }
        String[] falseInputs = new String[]{"", "a", "  a   ", "    ", "  aasdasd   asdas ", "aaaaaaaa", "Unknown profileName", "SomeUsername", "admi", "nadmin",
            "  a  __ A", "admi n", "a d m i n", "a_d_m_i_n", "dmina", " admin"};
        for (String input : falseInputs) {
            boolean actual = repo.existsProfileName(input);
            assertThat(actual).isFalse();
        }

        testsPassed.put("existsProfileName_Test", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests normal functionality of findByUsername method of UserRepositoryJDBC class")
    void findByUsername_Test() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.findByUsername(null);
        }).withMessage("Invalid parameter: username must be non-null");
        for (UserJDBC user : init.getUsers()) {
            Optional<UserJDBC> actual = repo.findByUsername(user.getUsername());
            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.isEmpty()).isFalse();
            checkValues(user, actual.get(), false);
        }
        String[] noResultInputs = new String[]{"", "a", "  a   ", "    ", "  aasdasd   asdas ", "aaaaaaaa", "Unknown username", "SomeUsername", "admi", "nadmin",
            "  a  __ A", "admi n", "a d m i n", "a_d_m_i_n", "dmina", " admin"};
        for (String input : noResultInputs) {
            Optional<UserJDBC> actual = repo.findByUsername(input);
            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isFalse();
            assertThat(actual.isEmpty()).isTrue();
        }

        testsPassed.put("findByUsername_Test", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests normal functionality of findByIdWithRelations method of UserRepositoryJDBC class")
    void findByIdWithRelations_Test() {
        final Long[] invalidInput = {null, 0l, -1l, -2l, Long.MIN_VALUE};
        for (int iter = 0; iter < invalidInput.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input value (%s)", invalidInput[i]).isThrownBy(() -> {
                repo.findByIdWithRelations(invalidInput[i]);
            }).withMessage("Invalid parameter: id must be non-null and greater than 0");
        }
        for (UserJDBC user : init.getUsers()) {
            Optional<UserJDBC> actual = repo.findByIdWithRelations(user.getId());
            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.isEmpty()).isFalse();
            checkValues(user, actual.get(), true);
        }
        Long[] noResultInputs = new Long[]{4l, 5l, 6l, 7l, 50l, 150l, Long.MAX_VALUE};
        for (Long input : noResultInputs) {
            Optional<UserJDBC> actual = repo.findByIdWithRelations(input);
            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isFalse();
            assertThat(actual.isEmpty()).isTrue();
        }

        testsPassed.put("findByIdWithRelations_Test", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests normal functionality of insert method of UserRepositoryJDBC class")
    void insert_Test() {
        Assumptions.assumeTrue(testsPassed.get("findByUsername_Test"));
        Assumptions.assumeTrue(testsPassed.get("findByIdWithRelations_Test"));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            repo.insert(null);
        }).withMessage("Invalid parameter: entity must be non-null");
        List<UserJDBC> invalidInputs = new ArrayList<>() {
            {
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        null));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(null, "Serbia", "The Republic of Serbia", "RS")));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(0l, "Serbia", "The Republic of Serbia", "RS")));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(1000l, "Serbia", "The Republic of Serbia", "RS")));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        null,
                        new CountryJDBC(190l, "Serbia", "The Republic of Serbia", "RS")));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        null,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        null,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        null,
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", null,
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        null, "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        Gender.OTHER,
                        null, null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", "Admin50",
                        null,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin50", null,
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, null, "Admin50",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin", "Admin",
                        Gender.OTHER,
                        "Admin", null,
                        "admin50", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin", "Admin",
                        Gender.OTHER,
                        "Admin50", null,
                        "admin", "admin50@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
                add(new UserJDBC(1l, "Admin", "Admin",
                        Gender.OTHER,
                        "Admin", null,
                        "admin50", "admin@gmail.com",
                        "admin50",
                        UserRole.ADMINISTRATOR,
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                        new CountryJDBC(190l)));
            }
        };
        for (int iter = 0; iter < invalidInputs.size(); iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code number(i) value (%s)", i).isThrownBy(() -> {
                repo.insert(invalidInputs.get(i));
            }).withMessage("Error while inserting user");
        }

        //Valid input
        UserJDBC validInput = new UserJDBC(null, "Admin50", "Admin50",
                Gender.OTHER,
                "Admin50", null,
                "admin50", "admin50@gmail.com",
                "admin50",
                UserRole.ADMINISTRATOR,
                LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                new CountryJDBC(190l));
        UserJDBC returnedValue = repo.insert(validInput);
        assertThat(validInput.getId()).isNotNull().isEqualTo(returnedValue.getId());
        checkInsertUpdateValues(validInput, returnedValue);
        Optional<UserJDBC> actual = repo.findByIdWithRelations(returnedValue.getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.isEmpty()).isFalse();
        checkInsertUpdateValues(returnedValue, actual.get());

        validInput = new UserJDBC(1l, "Admin51", "Admin51",
                Gender.OTHER,
                "Admin51", null,
                "admin51", "admin51@gmail.com",
                "admin50",
                UserRole.ADMINISTRATOR,
                LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                LocalDateTime.of(2024, Month.JANUARY, 25, 14, 49, 36),
                new CountryJDBC(190l));
        returnedValue = repo.insert(validInput);
        assertThat(validInput.getId()).isNotNull().isEqualTo(returnedValue.getId());
        checkInsertUpdateValues(validInput, returnedValue);
        actual = repo.findByIdWithRelations(returnedValue.getId());
        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.isEmpty()).isFalse();
        checkInsertUpdateValues(returnedValue, actual.get());

        testsPassed.put("insert_Test", true);
    }

    @Test
    @Order(7)
    @DisplayName("Tests normal functionality of existsMediaInLibrary method of UserRepositoryJDBC class")
    void existsMediaInLibrary_Test() {
        Long[][] invalidInputs1 = {{null, 1l}, {0l, 1l}, {-1l, 1l}, {-2l, 1l}, {-5l, 1l}, {Long.MIN_VALUE, 1l}};
        for (int iter = 0; iter < invalidInputs1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInputs1[i][0], invalidInputs1[i][1]).isThrownBy(() -> {
                repo.existsMediaInLibrary(invalidInputs1[i][0], invalidInputs1[i][1]);
            }).withMessage("Invalid parameter: userId must be non-null and greater than 0");
        }
        Long[][] invalidInputs2 = {{1l, null}, {1l, 0l}, {1l, -1l}, {1l, -2l}, {1l, -5l}, {1l, Long.MIN_VALUE}};
        for (int iter = 0; iter < invalidInputs2.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInputs2[i][0], invalidInputs2[i][1]).isThrownBy(() -> {
                repo.existsMediaInLibrary(invalidInputs2[i][0], invalidInputs2[i][1]);
            }).withMessage("Invalid parameter: mediaId must be non-null and greater than 0");
        }
        for (UserJDBC user : init.getUsers()) {
            for (MediaJDBC media : user.getMedias()) {
                boolean actual = repo.existsMediaInLibrary(user.getId(), media.getId());
                assertThat(actual).isTrue();
            }
        }
        Long[][] falseReturns = {{1l, 2l}, {2l, 4l}, {1l, 6l}, {3l, 1l}, {3l, 60l}, {2l, 3l}, {1l, 30l}, {30l, 1l}, {4l, 2l}};
        for (int iter = 0; iter < falseReturns.length; iter++) {
            boolean actual = repo.existsMediaInLibrary(falseReturns[iter][0], falseReturns[iter][1]);
            assertThat(actual).isFalse();
        }

        testsPassed.put("existsMediaInLibrary_Test", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests normal functionality of addMediaToLibrary method of UserRepositoryJDBC class")
    void addMediaToLibrary_Test() {
        Assumptions.assumeTrue(testsPassed.get("existsMediaInLibrary_Test"));
        Long[][] invalidInputs1 = {{null, 1l}, {0l, 1l}, {-1l, 1l}, {-2l, 1l}, {-5l, 1l}, {Long.MIN_VALUE, 1l}};
        for (int iter = 0; iter < invalidInputs1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInputs1[i][0], invalidInputs1[i][1]).isThrownBy(() -> {
                repo.addMediaToLibrary(invalidInputs1[i][0], invalidInputs1[i][1]);
            }).withMessage("Invalid parameter: userId must be non-null and greater than 0");
        }
        Long[][] invalidInputs2 = {{1l, null}, {1l, 0l}, {1l, -1l}, {1l, -2l}, {1l, -5l}, {1l, Long.MIN_VALUE}};
        for (int iter = 0; iter < invalidInputs2.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInputs2[i][0], invalidInputs2[i][1]).isThrownBy(() -> {
                repo.addMediaToLibrary(invalidInputs2[i][0], invalidInputs2[i][1]);
            }).withMessage("Invalid parameter: mediaId must be non-null and greater than 0");
        }
        Long[][] invalidInputs3 = {{1l, 1l}, {1l, 3l}, {1l, 4l}, {2l, 1l}, {2l, 2l}, {2l, 5l}, {1l, 10l}, {2l, 10l}, {10l, 1l}, {10l, 2l}};
        for (int iter = 0; iter < invalidInputs3.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code input values (%s,%s)", invalidInputs3[i][0], invalidInputs3[i][1]).isThrownBy(() -> {
                repo.addMediaToLibrary(invalidInputs3[i][0], invalidInputs3[i][1]);
            }).withMessage("Error while adding media with id: " + invalidInputs3[i][1] + " to users library");
        }
        Long[][] validInputs = {{1l, 2l}, {1l, 5l}, {1l, 6l}, {2l, 3l}, {3l, 1l}, {3l, 5l}};
        for (int iter = 0; iter < validInputs.length; iter++) {
            final int i = iter;
            boolean actual = repo.existsMediaInLibrary(validInputs[i][0], validInputs[i][1]);
            assertThat(actual).as("Code input values (%s,%s)", validInputs[i][0], validInputs[i][1]).isFalse();
            repo.addMediaToLibrary(validInputs[i][0], validInputs[i][1]);
            actual = repo.existsMediaInLibrary(validInputs[i][0], validInputs[i][1]);
            assertThat(actual).as("Code input values (%s,%s)", validInputs[i][0], validInputs[i][1]).isTrue();
        }

        testsPassed.put("addMediaToLibrary_Test", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests normal functionality of removeMediaFromLibrary method of UserRepositoryJDBC class")
    void removeMediaFromLibrary_Test() {
        Assumptions.assumeTrue(testsPassed.get("existsMediaInLibrary_Test"));
        Long[][] invalidInputs1 = {{null, 1l}, {0l, 1l}, {-1l, 1l}, {-2l, 1l}, {-5l, 1l}, {Long.MIN_VALUE, 1l}};
        for (int iter = 0; iter < invalidInputs1.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInputs1[i][0], invalidInputs1[i][1]).isThrownBy(() -> {
                repo.removeMediaFromLibrary(invalidInputs1[i][0], invalidInputs1[i][1]);
            }).withMessage("Invalid parameter: userId must be non-null and greater than 0");
        }
        Long[][] invalidInputs2 = {{1l, null}, {1l, 0l}, {1l, -1l}, {1l, -2l}, {1l, -5l}, {1l, Long.MIN_VALUE}};
        for (int iter = 0; iter < invalidInputs2.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(IllegalArgumentException.class).as("Code input values (%s,%s)", invalidInputs2[i][0], invalidInputs2[i][1]).isThrownBy(() -> {
                repo.removeMediaFromLibrary(invalidInputs2[i][0], invalidInputs2[i][1]);
            }).withMessage("Invalid parameter: mediaId must be non-null and greater than 0");
        }
        Long[][] invalidInputs3 = {{1l, 2l}, {1l, 5l}, {1l, 6l}, {2l, 3l}, {3l, 1l}, {3l, 5l}, {1l, 10l}, {2l, 10l}, {10l, 1l}, {10l, 2l}};
        for (int iter = 0; iter < invalidInputs3.length; iter++) {
            final int i = iter;
            assertThatExceptionOfType(DatabaseException.class).as("Code input values (%s,%s)", invalidInputs3[i][0], invalidInputs3[i][1]).isThrownBy(() -> {
                repo.removeMediaFromLibrary(invalidInputs3[i][0], invalidInputs3[i][1]);
            }).withMessage("Error while removing media with id: " + invalidInputs3[i][1] + " from users library");
        }
        Long[][] validInputs = {{1l, 1l}, {1l, 3l}, {1l, 4l}, {2l, 1l}, {2l, 2l}, {2l, 5l}};
        for (int iter = 0; iter < validInputs.length; iter++) {
            final int i = iter;
            boolean actual = repo.existsMediaInLibrary(validInputs[i][0], validInputs[i][1]);
            assertThat(actual).as("Code input values (%s,%s)", validInputs[i][0], validInputs[i][1]).isTrue();
            repo.removeMediaFromLibrary(validInputs[i][0], validInputs[i][1]);
            actual = repo.existsMediaInLibrary(validInputs[i][0], validInputs[i][1]);
            assertThat(actual).as("Code input values (%s,%s)", validInputs[i][0], validInputs[i][1]).isFalse();
        }

        testsPassed.put("removeMediaFromLibrary_Test", true);
    }

//=========================================================================================================
//PRIVATE METHODS
    void checkValues(UserJDBC expected, UserJDBC actual, boolean hasRelations) {
        assertThat(expected).isNotNull();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isNotEmpty().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotEmpty().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isNotNull().isEqualTo(expected.getRole());
        assertThat(actual.getProfileName()).isNotEmpty().isEqualTo(expected.getProfileName());
        assertThat(actual.getProfileImage()).isEqualTo(expected.getProfileImage());
        assertThat(actual.getEmail()).isNotEmpty().isEqualTo(expected.getEmail());
        assertThat(actual.getUsername()).isNotEmpty().isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isNotEmpty();
        assertThat(pswEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();
        assertThat(actual.getCreatedAt()).isNotNull().isEqualTo(expected.getCreatedAt());
        assertThat(actual.getUpdatedAt()).isNotNull().isEqualTo(expected.getUpdatedAt());
        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getCountry().getId()).isNotNull().isEqualTo(expected.getCountry().getId());
        assertThat(actual.getCountry().getName()).isNotEmpty().isEqualTo(expected.getCountry().getName());
        assertThat(actual.getCountry().getOfficialStateName()).isNotEmpty().isEqualTo(expected.getCountry().getOfficialStateName());
        assertThat(actual.getCountry().getCode()).isNotEmpty().isEqualTo(expected.getCountry().getCode());
        assertThat(actual.getMedias()).isNotNull();
        assertThat(actual.getCritiques()).isNotNull();
        if (hasRelations) {
            assertThat(actual.getMedias().size() == expected.getMedias().size()).isTrue();
            for (int i = 0; i < actual.getMedias().size(); i++) {
                assertThat(actual.getMedias().get(i)).isNotNull();
                assertThat(actual.getMedias().get(i).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getId());
                assertThat(actual.getMedias().get(i).getTitle()).isNotEmpty().isEqualTo(expected.getMedias().get(i).getTitle());
                assertThat(actual.getMedias().get(i).getCoverImage()).isEqualTo(expected.getMedias().get(i).getCoverImage());
                assertThat(actual.getMedias().get(i).getDescription()).isNotEmpty().isEqualTo(expected.getMedias().get(i).getDescription());
                assertThat(actual.getMedias().get(i).getReleaseDate()).isNotNull().isEqualTo(expected.getMedias().get(i).getReleaseDate());
                assertThat(actual.getMedias().get(i).getAudienceRating()).isNotNull().isEqualTo(expected.getMedias().get(i).getAudienceRating());
                assertThat(actual.getMedias().get(i).getCriticRating()).isEqualTo(expected.getMedias().get(i).getCriticRating());
                if (expected.getMedias().get(i) instanceof MovieJDBC) {
                    assertThat(actual.getMedias().get(i) instanceof MovieJDBC).isTrue();
                    assertThat(((MovieJDBC) actual.getMedias().get(i)).getLength()).isNotNull().isEqualTo(((MovieJDBC) expected.getMedias().get(i)).getLength());
                } else if (expected.getMedias().get(i) instanceof TVShowJDBC) {
                    assertThat(actual.getMedias().get(i) instanceof TVShowJDBC).isTrue();
                    assertThat(((TVShowJDBC) actual.getMedias().get(i)).getNumberOfSeasons()).isNotNull().isEqualTo(((TVShowJDBC) expected.getMedias().get(i)).getNumberOfSeasons());
                } else {
                    assertThat(actual.getMedias().get(i) instanceof MediaJDBC).isTrue();
                }
                assertThat(actual.getMedias().get(i).getGenres()).isNotNull();
                assertThat(actual.getMedias().get(i).getGenres().size() == expected.getMedias().get(i).getGenres().size()).isTrue();
                for (int j = 0; j < actual.getMedias().get(i).getGenres().size(); j++) {
                    assertThat(actual.getMedias().get(i).getGenres().get(j)).isNotNull();
                    assertThat(actual.getMedias().get(i).getGenres().get(j).getId()).isNotNull().isEqualTo(expected.getMedias().get(i).getGenres().get(j).getId());
                    assertThat(actual.getMedias().get(i).getGenres().get(j).getName()).isNotEmpty().isEqualTo(expected.getMedias().get(i).getGenres().get(j).getName());
                }

            }

            assertThat(actual.getCritiques().size() == expected.getCritiques().size()).isTrue();
            for (int i = 0; i < actual.getCritiques().size(); i++) {
                assertThat(actual.getCritiques().get(i)).isNotNull();
                assertThat(actual.getCritiques().get(i).getDescription()).isNotEmpty().isEqualTo(expected.getCritiques().get(i).getDescription());
                assertThat(actual.getCritiques().get(i).getRating()).isNotNull().isEqualTo(expected.getCritiques().get(i).getRating());

                assertThat(actual.getCritiques().get(i).getCritic()).isNotNull();
                assertThat(actual.getCritiques().get(i).getCritic() == actual).isTrue();

                assertThat(actual.getCritiques().get(i).getMedia()).isNotNull();
                assertThat(actual.getCritiques().get(i).getMedia().getId()).isNotNull().isEqualTo(expected.getCritiques().get(i).getMedia().getId());
                assertThat(actual.getCritiques().get(i).getMedia().getTitle()).isNotEmpty().isEqualTo(expected.getCritiques().get(i).getMedia().getTitle());
                assertThat(actual.getCritiques().get(i).getMedia().getCoverImage()).isEqualTo(expected.getCritiques().get(i).getMedia().getCoverImage());
                assertThat(actual.getCritiques().get(i).getMedia().getDescription()).isNotEmpty().isEqualTo(expected.getCritiques().get(i).getMedia().getDescription());
                assertThat(actual.getCritiques().get(i).getMedia().getReleaseDate()).isNotNull().isEqualTo(expected.getCritiques().get(i).getMedia().getReleaseDate());
                assertThat(actual.getCritiques().get(i).getMedia().getAudienceRating()).isNotNull().isEqualTo(expected.getCritiques().get(i).getMedia().getAudienceRating());
                assertThat(actual.getCritiques().get(i).getMedia().getCriticRating()).isEqualTo(expected.getCritiques().get(i).getMedia().getCriticRating());
                if (expected.getCritiques().get(i).getMedia() instanceof MovieJDBC) {
                    assertThat(actual.getCritiques().get(i).getMedia() instanceof MovieJDBC).isTrue();
                    assertThat(((MovieJDBC) actual.getCritiques().get(i).getMedia()).getLength()).isNotNull().isEqualTo(((MovieJDBC) expected.getCritiques().get(i).getMedia()).getLength());
                } else if (expected.getCritiques().get(i).getMedia() instanceof TVShowJDBC) {
                    assertThat(actual.getCritiques().get(i).getMedia() instanceof TVShowJDBC).isTrue();
                    assertThat(((TVShowJDBC) actual.getCritiques().get(i).getMedia()).getNumberOfSeasons()).isNotNull().isEqualTo(((TVShowJDBC) expected.getCritiques().get(i).getMedia()).getNumberOfSeasons());
                } else {
                    assertThat(actual.getCritiques().get(i).getMedia() instanceof MediaJDBC).isTrue();
                }
                assertThat(actual.getCritiques().get(i).getMedia().getGenres()).isNotNull();
                assertThat(actual.getCritiques().get(i).getMedia().getGenres().size() == expected.getCritiques().get(i).getMedia().getGenres().size()).isTrue();
                for (int j = 0; j < actual.getCritiques().get(i).getMedia().getGenres().size(); j++) {
                    assertThat(actual.getCritiques().get(i).getMedia().getGenres().get(j)).isNotNull();
                    assertThat(actual.getCritiques().get(i).getMedia().getGenres().get(j).getId()).isNotNull().isEqualTo(expected.getCritiques().get(i).getMedia().getGenres().get(j).getId());
                    assertThat(actual.getCritiques().get(i).getMedia().getGenres().get(j).getName()).isNotEmpty().isEqualTo(expected.getCritiques().get(i).getMedia().getGenres().get(j).getName());
                }

            }

        } else {
            assertThat(actual.getMedias()).isEmpty();
            assertThat(actual.getCritiques()).isEmpty();
        }
    }

    void checkInsertUpdateValues(UserJDBC expected, UserJDBC actual) {
        assertThat(expected).isNotNull();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull().isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isNotEmpty().isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isNotEmpty().isEqualTo(expected.getLastName());
        assertThat(actual.getGender()).isNotNull().isEqualTo(expected.getGender());
        assertThat(actual.getRole()).isNotNull().isEqualTo(expected.getRole());
        assertThat(actual.getProfileName()).isNotEmpty().isEqualTo(expected.getProfileName());
        assertThat(actual.getProfileImage()).isEqualTo(expected.getProfileImage());
        assertThat(actual.getEmail()).isNotEmpty().isEqualTo(expected.getEmail());
        assertThat(actual.getUsername()).isNotEmpty().isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isNotEmpty().isEqualTo(expected.getPassword());
        assertThat(actual.getCreatedAt()).isNotNull().isEqualTo(expected.getCreatedAt());
        assertThat(actual.getUpdatedAt()).isNotNull().isEqualTo(expected.getUpdatedAt());
        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getCountry().getId()).isNotNull().isEqualTo(expected.getCountry().getId());

        assertThat(actual.getMedias()).isNotNull().isEmpty();
        assertThat(actual.getCritiques()).isNotNull().isEmpty();
    }

//=========================================================================================================
}
