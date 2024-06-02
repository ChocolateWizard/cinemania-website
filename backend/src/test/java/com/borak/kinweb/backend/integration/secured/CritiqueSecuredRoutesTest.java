/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.integration.secured;

import com.borak.kinweb.backend.config.ConfigProperties;
import com.borak.kinweb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.kinweb.backend.domain.enums.UserRole;
import com.borak.kinweb.backend.domain.jdbc.classes.CritiqueJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.MediaJDBC;
import com.borak.kinweb.backend.domain.jdbc.classes.UserJDBC;
import com.borak.kinweb.backend.helpers.TestResultsHelper;
import com.borak.kinweb.backend.logic.security.JwtUtils;
import com.borak.kinweb.backend.repository.jdbc.CritiqueRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.MovieRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.TVShowRepositoryJDBC;
import com.borak.kinweb.backend.repository.jdbc.UserRepositoryJDBC;
import java.net.HttpCookie;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(7)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CritiqueSecuredRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ConfigProperties config;
    @Autowired
    private CritiqueRepositoryJDBC critiqueRepo;
    @Autowired
    private MovieRepositoryJDBC movieRepo;
    @Autowired
    private TVShowRepositoryJDBC tvShowRepo;
    @Autowired
    private UserRepositoryJDBC userRepo;
    @Autowired
    private JwtUtils jwtUtils;

    private static final Map<String, Boolean> testsPassed = new HashMap<>();
    private static final String ROUTE = "/api/critiques";

    static {
        testsPassed.put("postCritique_UnauthenticatedUser_DoesNotCreateCritiqueReturns401", false);
        testsPassed.put("postCritique_UnauthorizedUser_DoesNotCreateCritiqueReturns403", false);
        testsPassed.put("postCritique_InvalidInputData_DoesNotCreateCritiqueReturns400", false);
        testsPassed.put("postCritique_NonexistentDependencyData_DoesNotCreateCritiqueReturns404", false);
        testsPassed.put("postCritique_DuplicateCritiqueData_DoesNotCreateCritiqueReturns409", false);
        testsPassed.put("postCritique_ValidInput_CreatesCritiqueReturns200", false);

        testsPassed.put("putCritique_UnauthenticatedUser_DoesNotUpdateCritiqueReturns401", false);
        testsPassed.put("putCritique_UnauthorizedUser_DoesNotUpdateCritiqueReturns403", false);
        testsPassed.put("putCritique_InvalidInputData_DoesNotUpdateCritiqueReturns400", false);
        testsPassed.put("putCritique_NonexistentDependencyData_DoesNotUpdateCritiqueReturns404", false);
        testsPassed.put("putCritique_ValidInput_UpdatesCritiqueReturns200", false);

        testsPassed.put("deleteCritique_UnauthenticatedUser_DoesNotDeleteCritiqueReturns401", false);
        testsPassed.put("deleteCritique_UnauthorizedUser_DoesNotDeleteCritiqueReturns403", false);
        testsPassed.put("deleteCritique_InvalidInputData_DoesNotDeleteCritiqueReturns400", false);
        testsPassed.put("deleteCritique_NonexistentDependencyData_DoesNotDeleteCritiqueReturns404", false);
        testsPassed.put("deleteCritique_ValidInput_DeletesCritiqueReturns200", false);
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
    @DisplayName("Tests whether POST request to /api/critiques with unauthenticated user did not create new critique and it returned 401")
    void postCritique_UnauthenticatedUser_DoesNotCreateCritiqueReturns401() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", 50), "dummy user 1"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(2l, "Dummy description", 50), "dummy user 2"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy description", 50), "dummy user 4"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(3l, "Dummy description", 50), "admina"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy description", 50), "critic1"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy description", 50), "dummy user 6"));
            }
        };

        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                //critique present, with no cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                List<CritiqueJDBC> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), getRandomString(50, random)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with jwt of non-existent user in cookies
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postCritique_UnauthenticatedUser_DoesNotCreateCritiqueReturns401", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests whether POST request to /api/critiques with authenticated but unauthorized user did not create new critique and it returned 403")
    void postCritique_UnauthorizedUser_DoesNotCreateCritiqueReturns403() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", 50), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(2l, "Dummy description", 50), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy description", 50), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(3l, "Dummy description", 50), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy description", 50), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy description", 50), "regular"));
            }
        };

        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {

                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                assertThat(user.get().getRole() != UserRole.CRITIC && user.get().getRole() != UserRole.ADMINISTRATOR).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

                List<CritiqueJDBC> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postCritique_UnauthorizedUser_DoesNotCreateCritiqueReturns403", true);
    }

    @Test
    @Order(3)
    @DisplayName("Tests whether POST request to /api/critiques with invalid input data did not create new critique and it returned 400")
    void postCritique_InvalidInputData_DoesNotCreateCritiqueReturns400() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(0l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(-1l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(-2l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(Long.MIN_VALUE, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, null, 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, " ", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "      ", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, getRandomString(501, random), 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", -1), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", -2), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", Integer.MIN_VALUE), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", 101), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", 102), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", Integer.MAX_VALUE), "admin"));
            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                for (CritiqueJDBC critique : userRepo.findByIdWithRelations(
                        userRepo.findByUsername(input.getValue()).get().getId()
                ).get().getCritiques()) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postCritique_InvalidInputData_DoesNotCreateCritiqueReturns400", true);
    }

    @Test
    @Order(4)
    @DisplayName("Tests whether POST request to /api/critiques with non-existent dependency objects did not create new critique and it returned 404")
    void postCritique_NonexistentDependencyData_DoesNotCreateCritiqueReturns404() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(7l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(8l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(10l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(25l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(Long.MAX_VALUE, "Dummy description", 50), "admin"));
            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {

                //does such critique exist beforehand
                Optional<UserJDBC> user = userRepo.findByIdWithRelations(userRepo.findByUsername(input.getValue()).get().getId());
                Optional<CritiqueJDBC> actualCritique = critiqueRepo.find(
                        new CritiqueJDBC(user.get(), new MediaJDBC(input.getKey().getMediaId()), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isFalse();

                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

                //does critique exist now
                user = userRepo.findByIdWithRelations(userRepo.findByUsername(input.getValue()).get().getId());
                actualCritique = critiqueRepo.find(
                        new CritiqueJDBC(user.get(), new MediaJDBC(input.getKey().getMediaId()), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isFalse();

                for (CritiqueJDBC critique : user.get().getCritiques()) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postCritique_NonexistentDependencyData_DoesNotCreateCritiqueReturns404", true);
    }

    @Test
    @Order(5)
    @DisplayName("Tests whether POST request to /api/critiques with duplicate critique did not create new critique and it returned 409")
    void postCritique_DuplicateCritiqueData_DoesNotCreateCritiqueReturns409() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy description", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy description", 50), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(2l, "Dummy description", 50), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy description", 50), "critic"));

            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

                for (CritiqueJDBC critique : userRepo.findByIdWithRelations(
                        userRepo.findByUsername(input.getValue()).get().getId()
                ).get().getCritiques()) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postCritique_DuplicateCritiqueData_DoesNotCreateCritiqueReturns409", true);
    }

    @Test
    @Order(6)
    @DisplayName("Tests whether POST request to /api/critiques with valid input data did create new critique and it returned 200")
    void postCritique_ValidInput_CreatesCritiqueReturns200() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(2l, "Dummy description 2", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy description 21", 50), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(3l, "Dummy description 3", 20), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy description 34", 20), "critic"));
            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                Optional<UserJDBC> user = userRepo.findByIdWithRelations(userRepo.findByUsername(input.getValue()).get().getId());

                Optional<CritiqueJDBC> actualCritique = critiqueRepo.find(
                        new CritiqueJDBC(user.get(), new MediaJDBC(input.getKey().getMediaId()), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isFalse();

                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);

                user = userRepo.findByIdWithRelations(userRepo.findByUsername(input.getValue()).get().getId());

                actualCritique = critiqueRepo.find(
                        new CritiqueJDBC(user.get(), new MediaJDBC(input.getKey().getMediaId()), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isTrue();
                assertThat(actualCritique.get().getMedia()).isNotNull();
                assertThat(actualCritique.get().getMedia().getId()).isNotNull().isEqualTo(input.getKey().getMediaId());
                assertThat(actualCritique.get().getCritic()).isNotNull();
                assertThat(actualCritique.get().getCritic().getId()).isNotNull().isEqualTo(user.get().getId());
                assertThat(actualCritique.get().getDescription()).isNotBlank().isEqualTo(input.getKey().getDescription());
                assertThat(actualCritique.get().getRating()).isNotNull().isEqualTo(input.getKey().getRating());

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("postCritique_ValidInput_CreatesCritiqueReturns200", true);
    }

    //=========================================================================================================
    //PUT
    @Test
    @Order(7)
    @DisplayName("Tests whether PUT request to /api/critiques with unauthenticated user did not update critique and it returned 401")
    void putCritique_UnauthenticatedUser_DoesNotUpdateCritiqueReturns401() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", 61), "dummy user 1"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy PUT description", 61), "dummy user 2"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy PUT description", 61), "dummy user 4"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(3l, "Dummy PUT description", 61), "admina"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy PUT description", 61), "critic1"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy PUT description", 61), "dummy user 6"));
            }
        };

        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                //critique present, with no cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                List<CritiqueJDBC> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), getRandomString(50, random)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with jwt of non-existent user in cookies
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putCritique_UnauthenticatedUser_DoesNotUpdateCritiqueReturns401", true);
    }

    @Test
    @Order(8)
    @DisplayName("Tests whether PUT request to /api/critiques with authenticated but unauthorized user did not update critique and it returned 403")
    void putCritique_UnauthorizedUser_DoesNotUpdateCritiqueReturns403() {

        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", 61), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(2l, "Dummy PUT description", 61), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy PUT description", 61), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(3l, "Dummy PUT description", 61), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy PUT description", 61), "regular"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy PUT description", 61), "regular"));
            }
        };

        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {

                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

                List<CritiqueJDBC> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findByIdWithRelations(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJDBC critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putCritique_UnauthorizedUser_DoesNotUpdateCritiqueReturns403", true);
    }

    @Test
    @Order(9)
    @DisplayName("Tests whether PUT request to /api/critiques with invalid input data did not update critique and it returned 400")
    void putCritique_InvalidInputData_DoesNotUpdateCritiqueReturns400() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(0l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(-1l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(-2l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(Long.MIN_VALUE, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, null, 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, " ", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "      ", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, getRandomString(501, random), 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", -1), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", -2), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", Integer.MIN_VALUE), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", 101), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", 102), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", Integer.MAX_VALUE), "admin"));
            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                for (CritiqueJDBC critique : userRepo.findByIdWithRelations(
                        userRepo.findByUsername(input.getValue()).get().getId()
                ).get().getCritiques()) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putCritique_InvalidInputData_DoesNotUpdateCritiqueReturns400", true);
    }

    @Test
    @Order(10)
    @DisplayName("Tests whether PUT request to /api/critiques with non-existent dependency objects did not update critique and it returned 404")
    void putCritique_NonexistentDependencyData_DoesNotUpdateCritiqueReturns404() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(7l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(8l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(10l, "Dummy PUT description", 61), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(25l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(Long.MAX_VALUE, "Dummy PUT description", 61), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(3l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy PUT description", 61), "critic"));
            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

                //iterate over all critiques of current user
                for (CritiqueJDBC critique : userRepo.findByIdWithRelations(
                        userRepo.findByUsername(input.getValue()).get().getId()
                ).get().getCritiques()) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putCritique_NonexistentDependencyData_DoesNotUpdateCritiqueReturns404", true);
    }

    @Test
    @Order(11)
    @DisplayName("Tests whether PUT request to /api/critiques with valid input data did update critique and it returned 200")
    void putCritique_ValidInput_UpdatesCritiqueReturns200() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<CritiqueRequestDTO, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(4l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(5l, "Dummy PUT description", 61), "admin"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, "Dummy PUT description", 61), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(2l, "Dummy PUT description", 61), "critic"));
                add(new SimpleEntry<>(new CritiqueRequestDTO(6l, "Dummy PUT description", 61), "critic"));
            }
        };
        try {
            for (SimpleEntry<CritiqueRequestDTO, String> input : inputs) {

                Optional<UserJDBC> user = userRepo.findByIdWithRelations(userRepo.findByUsername(input.getValue()).get().getId());

                Optional<CritiqueJDBC> actualCritique = critiqueRepo.find(
                        new CritiqueJDBC(user.get(), new MediaJDBC(input.getKey().getMediaId()), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isTrue();
                assertThat(actualCritique.get().getMedia()).isNotNull();
                assertThat(actualCritique.get().getMedia().getId()).isNotNull().isEqualTo(input.getKey().getMediaId());
                assertThat(actualCritique.get().getCritic()).isNotNull();
                assertThat(actualCritique.get().getCritic().getId()).isNotNull().isEqualTo(user.get().getId());
                assertThat(actualCritique.get().getDescription()).isNotBlank().isNotEqualTo(input.getKey().getDescription());
                assertThat(actualCritique.get().getRating()).isNotNull().isNotEqualTo(input.getKey().getRating());

                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);

                user = userRepo.findByIdWithRelations(userRepo.findByUsername(input.getValue()).get().getId());

                actualCritique = critiqueRepo.find(
                        new CritiqueJDBC(user.get(), new MediaJDBC(input.getKey().getMediaId()), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isTrue();
                assertThat(actualCritique.get().getMedia()).isNotNull();
                assertThat(actualCritique.get().getMedia().getId()).isNotNull().isEqualTo(input.getKey().getMediaId());
                assertThat(actualCritique.get().getCritic()).isNotNull();
                assertThat(actualCritique.get().getCritic().getId()).isNotNull().isEqualTo(user.get().getId());
                assertThat(actualCritique.get().getDescription()).isNotBlank().isEqualTo(input.getKey().getDescription());
                assertThat(actualCritique.get().getRating()).isNotNull().isEqualTo(input.getKey().getRating());

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("putCritique_ValidInput_UpdatesCritiqueReturns200", true);
    }

    //=========================================================================================================
    //DELETE
    @Test
    @Order(12)
    @DisplayName("Tests whether DELETE request to /api/critiques with unauthenticated user did not delete critique and it returned 401")
    void deleteCritique_UnauthenticatedUser_DoesNotDeleteCritiqueReturns401() {
        ResponseEntity<String> response;
        Random random = new Random();
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(1l, "dummy user 1"));
                add(new SimpleEntry<>(2l, "dummy user 2"));
                add(new SimpleEntry<>(3l, "dummy user 4"));
                add(new SimpleEntry<>(4l, "admina"));
                add(new SimpleEntry<>(5l, "critic1"));
                add(new SimpleEntry<>(6l, "dummy user 6"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                List<CritiqueJDBC> critsBeforeRequest = getCritsByMediaId(input.getKey());

                //with no cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                List<CritiqueJDBC> critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }

                //critique present, with random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, getRandomString(50, random)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }

                //critique present, with jwt of non-existent user in cookies
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteCritique_UnauthenticatedUser_DoesNotDeleteCritiqueReturns401", true);
    }

    @Test
    @Order(13)
    @DisplayName("Tests whether DELETE request to /api/critiques with authenticated but unauthorized user did not delete critique and it returned 403")
    void deleteCritique_UnauthorizedUser_DoesNotDeleteCritiqueReturns403() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(1l, "regular"));
                add(new SimpleEntry<>(2l, "regular"));
                add(new SimpleEntry<>(3l, "regular"));
                add(new SimpleEntry<>(4l, "regular"));
                add(new SimpleEntry<>(5l, "regular"));
                add(new SimpleEntry<>(6l, "regular"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                assertThat(user.get().getRole() != UserRole.CRITIC && user.get().getRole() != UserRole.ADMINISTRATOR).isTrue();

                List<CritiqueJDBC> critsBeforeRequest = getCritsByMediaId(input.getKey());
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                List<CritiqueJDBC> critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteCritique_UnauthorizedUser_DoesNotDeleteCritiqueReturns403", true);
    }

    @Test
    @Order(14)
    @DisplayName("Tests whether DELETE request to /api/critiques with invalid input data did not delete critique and it returned 400")
    void deleteCritique_InvalidInputData_DoesNotDeleteCritiqueReturns400() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(0l, "admin"));
                add(new SimpleEntry<>(-1l, "admin"));
                add(new SimpleEntry<>(-2l, "admin"));
                add(new SimpleEntry<>(-10l, "admin"));
                add(new SimpleEntry<>(Long.MIN_VALUE, "admin"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                List<CritiqueJDBC> critsBeforeRequest = getCritsByUserUsername(input.getValue());
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                List<CritiqueJDBC> critsAfterRequest = getCritsByUserUsername(input.getValue());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteCritique_InvalidInputData_DoesNotDeleteCritiqueReturns400", true);
    }

    @Test
    @Order(15)
    @DisplayName("Tests whether DELETE request to /api/critiques with non-existent dependency objects did not delete critique and it returned 404")
    void deleteCritique_NonexistentDependencyData_DoesNotDeleteCritiqueReturns404() {
        ResponseEntity<String> response;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(7l, "admin"));
                add(new SimpleEntry<>(8l, "admin"));
                add(new SimpleEntry<>(10l, "admin"));
                add(new SimpleEntry<>(25l, "admin"));
                add(new SimpleEntry<>(Long.MAX_VALUE, "admin"));
                add(new SimpleEntry<>(3l, "admin"));
                add(new SimpleEntry<>(5l, "critic"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                Optional<UserJDBC> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();

                List<CritiqueJDBC> critsBeforeRequest = getCritsByUserUsername(input.getValue());
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                List<CritiqueJDBC> critsAfterRequest = getCritsByUserUsername(input.getValue());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteCritique_NonexistentDependencyData_DoesNotDeleteCritiqueReturns404", true);
    }

    @Test
    @Order(16)
    @DisplayName("Tests whether DELETE request to /api/critiques with valid input data did delete critique and it returned 200")
    void deleteCritique_ValidInput_DeletesCritiqueReturns200() {
        ResponseEntity<String> response;
        boolean critiqueExists;
        int i = 0;
        List<SimpleEntry<Long, String>> inputs = new ArrayList<>() {
            {
                add(new SimpleEntry<>(2l, "admin"));
                add(new SimpleEntry<>(6l, "admin"));
                add(new SimpleEntry<>(5l, "admin"));
                add(new SimpleEntry<>(1l, "critic"));
                add(new SimpleEntry<>(6l, "critic"));
                add(new SimpleEntry<>(4l, "critic"));
            }
        };

        try {
            for (SimpleEntry<Long, String> input : inputs) {
                critiqueExists = doesCritiqueExist(input.getKey(), input.getValue());
                assertThat(critiqueExists).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);
                critiqueExists = doesCritiqueExist(input.getKey(), input.getValue());
                assertThat(critiqueExists).isFalse();
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        testsPassed.put("deleteCritique_ValidInput_DeletesCritiqueReturns200", true);
    }
    //=========================================================================================================
    //PRIVATE METHODS

    private HttpEntity<CritiqueRequestDTO> constructRequest(CritiqueRequestDTO critique, String jwt) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (jwt != null) {
            HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
            cookie.setPath("/api");
            cookie.setHttpOnly(true);
            requestHeaders.set(HttpHeaders.COOKIE, cookie.toString());
        }
        if (critique == null) {
            return new HttpEntity<>(requestHeaders);
        }
        return new HttpEntity<>(critique, requestHeaders);

    }

    private String getRandomString(int length, Random random) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private List<CritiqueJDBC> getCritsByMediaId(Long mediaId) {
        if (movieRepo.existsById(mediaId)) {
            return movieRepo.findByIdWithRelations(mediaId).get().getCritiques();
        }
        return tvShowRepo.findByIdWithRelations(mediaId).get().getCritiques();
    }

    private List<CritiqueJDBC> getCritsByUserUsername(String username) {
        return userRepo.findByIdWithRelations(
                userRepo.findByUsername(username).get().getId()
        ).get().getCritiques();
    }

    private boolean doesCritiqueExist(Long mediaId, String username) {
        return critiqueRepo.exists(new CritiqueJDBC(userRepo.findByUsername(username).get(), new MediaJDBC(mediaId), null, null));
    }

}
