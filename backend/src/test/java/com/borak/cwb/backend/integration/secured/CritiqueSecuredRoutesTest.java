/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.secured;

import com.borak.cwb.backend.config.ConfigProperties;
import com.borak.cwb.backend.domain.dto.critique.CritiqueRequestDTO;
import com.borak.cwb.backend.domain.enums.UserRole;
import com.borak.cwb.backend.domain.jpa.CritiqueJPA;
import com.borak.cwb.backend.domain.jpa.MediaJPA;
import com.borak.cwb.backend.domain.jpa.UserJPA;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.helpers.TestUtil;
import com.borak.cwb.backend.helpers.repositories.CritiqueTestRepository;
import com.borak.cwb.backend.helpers.repositories.MovieTestRepository;
import com.borak.cwb.backend.helpers.repositories.TVShowTestRepository;
import com.borak.cwb.backend.helpers.repositories.UserTestRepository;
import com.borak.cwb.backend.logic.security.JwtUtils;
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

    private final TestRestTemplate restTemplate;
    private final ConfigProperties config;
    private final CritiqueTestRepository critiqueRepo;
    private final MovieTestRepository movieRepo;
    private final TVShowTestRepository tvShowRepo;
    private final UserTestRepository userRepo;
    private final JwtUtils jwtUtils;

    @Autowired
    public CritiqueSecuredRoutesTest(TestRestTemplate restTemplate, ConfigProperties config, CritiqueTestRepository critiqueRepo, MovieTestRepository movieRepo, TVShowTestRepository tvShowRepo, UserTestRepository userRepo, JwtUtils jwtUtils) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.critiqueRepo = critiqueRepo;
        this.movieRepo = movieRepo;
        this.tvShowRepo = tvShowRepo;
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
    }

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/critiques";

    static {
        TESTS_PASSED.put("postCritique_UnauthenticatedUser_DoesNotCreateCritiqueReturns401", false);
        TESTS_PASSED.put("postCritique_UnauthorizedUser_DoesNotCreateCritiqueReturns403", false);
        TESTS_PASSED.put("postCritique_InvalidInputData_DoesNotCreateCritiqueReturns400", false);
        TESTS_PASSED.put("postCritique_NonexistentDependencyData_DoesNotCreateCritiqueReturns404", false);
        TESTS_PASSED.put("postCritique_DuplicateCritiqueData_DoesNotCreateCritiqueReturns409", false);
        TESTS_PASSED.put("postCritique_ValidInput_CreatesCritiqueReturns200", false);

        TESTS_PASSED.put("putCritique_UnauthenticatedUser_DoesNotUpdateCritiqueReturns401", false);
        TESTS_PASSED.put("putCritique_UnauthorizedUser_DoesNotUpdateCritiqueReturns403", false);
        TESTS_PASSED.put("putCritique_InvalidInputData_DoesNotUpdateCritiqueReturns400", false);
        TESTS_PASSED.put("putCritique_NonexistentDependencyData_DoesNotUpdateCritiqueReturns404", false);
        TESTS_PASSED.put("putCritique_ValidInput_UpdatesCritiqueReturns200", false);

        TESTS_PASSED.put("deleteCritique_UnauthenticatedUser_DoesNotDeleteCritiqueReturns401", false);
        TESTS_PASSED.put("deleteCritique_UnauthorizedUser_DoesNotDeleteCritiqueReturns403", false);
        TESTS_PASSED.put("deleteCritique_InvalidInputData_DoesNotDeleteCritiqueReturns400", false);
        TESTS_PASSED.put("deleteCritique_NonexistentDependencyData_DoesNotDeleteCritiqueReturns404", false);
        TESTS_PASSED.put("deleteCritique_ValidInput_DeletesCritiqueReturns200", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : TESTS_PASSED.values()) {
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
        HttpEntity request;
        ResponseEntity<String> response;
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

                List<CritiqueJPA> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), TestUtil.getRandomString(50)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with jwt of non-existent user in cookies
                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("postCritique_UnauthenticatedUser_DoesNotCreateCritiqueReturns401", true);
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

                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                assertThat(user.get().getRole() != UserRole.CRITIC && user.get().getRole() != UserRole.ADMINISTRATOR).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

                List<CritiqueJPA> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("postCritique_UnauthorizedUser_DoesNotCreateCritiqueReturns403", true);
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
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, TestUtil.getRandomString(501), 50), "admin"));
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

                for (CritiqueJPA critique : userRepo.findById(
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

        TESTS_PASSED.put("postCritique_InvalidInputData_DoesNotCreateCritiqueReturns400", true);
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
                Optional<UserJPA> user = userRepo.findById(userRepo.findByUsername(input.getValue()).get().getId());
                Optional<CritiqueJPA> actualCritique = critiqueRepo.find(
                        new CritiqueJPA(new CritiqueJPA.ID(user.get(), new MediaJPA(input.getKey().getMediaId())), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isFalse();

                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

                //does critique exist now
                user = userRepo.findById(userRepo.findByUsername(input.getValue()).get().getId());
                actualCritique = critiqueRepo.find(
                        new CritiqueJPA(new CritiqueJPA.ID(user.get(), new MediaJPA(input.getKey().getMediaId())), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isFalse();

                for (CritiqueJPA critique : user.get().getCritiques()) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("postCritique_NonexistentDependencyData_DoesNotCreateCritiqueReturns404", true);
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

                for (CritiqueJPA critique : userRepo.findById(
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

        TESTS_PASSED.put("postCritique_DuplicateCritiqueData_DoesNotCreateCritiqueReturns409", true);
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
                Optional<UserJPA> user = userRepo.findById(userRepo.findByUsername(input.getValue()).get().getId());

                Optional<CritiqueJPA> actualCritique = critiqueRepo.find(
                        new CritiqueJPA(new CritiqueJPA.ID(user.get(), new MediaJPA(input.getKey().getMediaId())), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isFalse();

                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.POST,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);

                user = userRepo.findById(userRepo.findByUsername(input.getValue()).get().getId());

                actualCritique = critiqueRepo.find(
                        new CritiqueJPA(new CritiqueJPA.ID(user.get(), new MediaJPA(input.getKey().getMediaId())), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isTrue();
                assertThat(actualCritique.get().getId().getMedia()).isNotNull();
                assertThat(actualCritique.get().getId().getMedia().getId()).isNotNull().isEqualTo(input.getKey().getMediaId());
                assertThat(actualCritique.get().getId().getCritic()).isNotNull();
                assertThat(actualCritique.get().getId().getCritic().getId()).isNotNull().isEqualTo(user.get().getId());
                assertThat(actualCritique.get().getDescription()).isNotBlank().isEqualTo(input.getKey().getDescription());
                assertThat(actualCritique.get().getRating()).isNotNull().isEqualTo(input.getKey().getRating());

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("postCritique_ValidInput_CreatesCritiqueReturns200", true);
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

                List<CritiqueJPA> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), TestUtil.getRandomString(50)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }

                //critique present, with jwt of non-existent user in cookies
                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("putCritique_UnauthenticatedUser_DoesNotUpdateCritiqueReturns401", true);
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

                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

                List<CritiqueJPA> critiques;
                if (movieRepo.existsById(input.getKey().getMediaId())) {
                    critiques = movieRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                } else {
                    critiques = tvShowRepo.findById(input.getKey().getMediaId()).get().getCritiques();
                }

                for (CritiqueJPA critique : critiques) {
                    assertThat(critique.getDescription()).isNotEqualTo(input.getKey().getDescription());
                    assertThat(critique.getRating()).isNotEqualTo(input.getKey().getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("putCritique_UnauthorizedUser_DoesNotUpdateCritiqueReturns403", true);
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
                add(new SimpleEntry<>(new CritiqueRequestDTO(1l, TestUtil.getRandomString(501), 61), "admin"));
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

                for (CritiqueJPA critique : userRepo.findById(
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

        TESTS_PASSED.put("putCritique_InvalidInputData_DoesNotUpdateCritiqueReturns400", true);
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
                for (CritiqueJPA critique : userRepo.findById(
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

        TESTS_PASSED.put("putCritique_NonexistentDependencyData_DoesNotUpdateCritiqueReturns404", true);
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

                Optional<UserJPA> user = userRepo.findById(userRepo.findByUsername(input.getValue()).get().getId());

                Optional<CritiqueJPA> actualCritique = critiqueRepo.find(
                        new CritiqueJPA(new CritiqueJPA.ID(user.get(), new MediaJPA(input.getKey().getMediaId())), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isTrue();
                assertThat(actualCritique.get().getId().getMedia()).isNotNull();
                assertThat(actualCritique.get().getId().getMedia().getId()).isNotNull().isEqualTo(input.getKey().getMediaId());
                assertThat(actualCritique.get().getId().getCritic()).isNotNull();
                assertThat(actualCritique.get().getId().getCritic().getId()).isNotNull().isEqualTo(user.get().getId());
                assertThat(actualCritique.get().getDescription()).isNotBlank().isNotEqualTo(input.getKey().getDescription());
                assertThat(actualCritique.get().getRating()).isNotNull().isNotEqualTo(input.getKey().getRating());

                response = restTemplate.exchange(ROUTE + "/" + input.getKey().getMediaId(), HttpMethod.PUT,
                        constructRequest(input.getKey(), jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);

                user = userRepo.findById(userRepo.findByUsername(input.getValue()).get().getId());

                actualCritique = critiqueRepo.find(
                        new CritiqueJPA(new CritiqueJPA.ID(user.get(), new MediaJPA(input.getKey().getMediaId())), null, null)
                );
                assertThat(actualCritique).isNotNull();
                assertThat(actualCritique.isPresent()).isTrue();
                assertThat(actualCritique.get().getId().getMedia()).isNotNull();
                assertThat(actualCritique.get().getId().getMedia().getId()).isNotNull().isEqualTo(input.getKey().getMediaId());
                assertThat(actualCritique.get().getId().getCritic()).isNotNull();
                assertThat(actualCritique.get().getId().getCritic().getId()).isNotNull().isEqualTo(user.get().getId());
                assertThat(actualCritique.get().getDescription()).isNotBlank().isEqualTo(input.getKey().getDescription());
                assertThat(actualCritique.get().getRating()).isNotNull().isEqualTo(input.getKey().getRating());

                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("putCritique_ValidInput_UpdatesCritiqueReturns200", true);
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
                List<CritiqueJPA> critsBeforeRequest = getCritsByMediaId(input.getKey());

                //with no cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, null), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                List<CritiqueJPA> critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getId().getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getId().getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }

                //critique present, with random string as cookie
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, TestUtil.getRandomString(50)), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getId().getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getId().getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }

                //critique present, with jwt of non-existent user in cookies
                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isFalse();
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getId().getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getId().getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("deleteCritique_UnauthenticatedUser_DoesNotDeleteCritiqueReturns401", true);
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
                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                assertThat(user.get().getRole() != UserRole.CRITIC && user.get().getRole() != UserRole.ADMINISTRATOR).isTrue();

                List<CritiqueJPA> critsBeforeRequest = getCritsByMediaId(input.getKey());
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                List<CritiqueJPA> critsAfterRequest = getCritsByMediaId(input.getKey());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getId().getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getId().getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("deleteCritique_UnauthorizedUser_DoesNotDeleteCritiqueReturns403", true);
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
                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();
                List<CritiqueJPA> critsBeforeRequest = getCritsByUserUsername(input.getValue());
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                List<CritiqueJPA> critsAfterRequest = getCritsByUserUsername(input.getValue());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getId().getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getId().getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("deleteCritique_InvalidInputData_DoesNotDeleteCritiqueReturns400", true);
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
                Optional<UserJPA> user = userRepo.findByUsername(input.getValue());
                assertThat(user).isNotNull();
                assertThat(user.isPresent()).isTrue();

                List<CritiqueJPA> critsBeforeRequest = getCritsByUserUsername(input.getValue());
                response = restTemplate.exchange(ROUTE + "/" + input.getKey(), HttpMethod.DELETE, constructRequest(null, jwtUtils.generateTokenFromUsername(input.getValue())), String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                List<CritiqueJPA> critsAfterRequest = getCritsByUserUsername(input.getValue());
                assertThat(critsBeforeRequest.size()).isEqualTo(critsAfterRequest.size());
                for (int j = 0; j < critsAfterRequest.size(); j++) {
                    assertThat(critsAfterRequest.get(j).getId().getMedia().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getMedia().getId());
                    assertThat(critsAfterRequest.get(j).getId().getCritic().getId()).isEqualTo(critsBeforeRequest.get(j).getId().getCritic().getId());
                    assertThat(critsAfterRequest.get(j).getDescription()).isEqualTo(critsBeforeRequest.get(j).getDescription());
                    assertThat(critsAfterRequest.get(j).getRating()).isEqualTo(critsBeforeRequest.get(j).getRating());
                }
                i++;
            }
        } catch (AssertionError e) {
            throw new AssertionError("Assertion failed at index " + i, e);
        }

        TESTS_PASSED.put("deleteCritique_NonexistentDependencyData_DoesNotDeleteCritiqueReturns404", true);
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

        TESTS_PASSED.put("deleteCritique_ValidInput_DeletesCritiqueReturns200", true);
    }
//=================================================================================================================================
//PRIVATE METHODS
//=================================================================================================================================

    private HttpEntity<CritiqueRequestDTO> constructRequest(CritiqueRequestDTO critique, String jwt) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (jwt != null) {
            HttpCookie cookie = new HttpCookie(config.getJwtCookieName(), jwt);
            cookie.setPath("/api");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(24 * 60 * 60);
            requestHeaders.set(HttpHeaders.COOKIE, cookie.toString());
        }
        if (critique == null) {
            return new HttpEntity<>(requestHeaders);
        }
        return new HttpEntity<>(critique, requestHeaders);

    }

    private List<CritiqueJPA> getCritsByMediaId(Long mediaId) {
        if (movieRepo.existsById(mediaId)) {
            return movieRepo.findById(mediaId).get().getCritiques();
        }
        return tvShowRepo.findById(mediaId).get().getCritiques();
    }

    private List<CritiqueJPA> getCritsByUserUsername(String username) {
        return userRepo.findById(
                userRepo.findByUsername(username).get().getId()
        ).get().getCritiques();
    }

    private boolean doesCritiqueExist(Long mediaId, String username) {
        return critiqueRepo.exists(new CritiqueJPA(new CritiqueJPA.ID(userRepo.findByUsername(username).get(), new MediaJPA(mediaId)), null, null));
    }

}
