/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.integration.nonsecured;

import com.borak.cwb.backend.helpers.Pair;
import static org.assertj.core.api.Assertions.assertThat;
import com.borak.cwb.backend.helpers.TestJsonResponseReader;
import com.borak.cwb.backend.helpers.TestResultsHelper;
import com.borak.cwb.backend.helpers.TestUtil;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Order(5)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MediaRoutesTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestJsonResponseReader jsonReader;

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();
    private static final String ROUTE = "/api/medias";

    static {
        TESTS_PASSED.put("getMedia_InvalidInputData_Returns400", false);
        TESTS_PASSED.put("getMedia_ValidInputData_Returns200", false);
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
        Assumptions.assumeTrue(TestResultsHelper.didAllPreControllerTestsPass());
    }

    @Test
    @Order(1)
    @DisplayName("Tests GET /api/medias with invalid input")
    void getMedia_InvalidInputData_Returns400() {
        ResponseEntity<String> response;
        String[] invalidUrls = getInvalidUrls();
        for (int i = 0; i < invalidUrls.length; i++) {
            response = restTemplate.getForEntity(ROUTE + invalidUrls[i], String.class);
            assertThat(response.getStatusCode()).as("Assertion at i=%d for url=%s", i, invalidUrls[i]).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        TESTS_PASSED.put("getMedia_InvalidInputData_Returns400", true);
    }

    @Test
    @Order(2)
    @DisplayName("Tests GET /api/medias with valid input")
    void getMedia_ValidInputData_Returns200() {
        ResponseEntity<String> response;
        Pair<Integer, String>[] inputs1 = getValidUrlsAndNonEmptyResponses();
        String[] inputs2 = getValidUrlsForEmptyResponses();

        for (Pair<Integer, String> pom : inputs1) {
            response = restTemplate.getForEntity(ROUTE + pom.getR(), String.class);
            assertThat(response.getStatusCode()).as("Assertion at L=%d for url=%s", pom.getL(), pom.getR()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Assertion at L=%d for url=%s", pom.getL(), pom.getR()).isEqualTo(jsonReader.getMediaJson(pom.getL()));
        }
        for (int i = 0; i < inputs2.length; i++) {
            response = restTemplate.getForEntity(ROUTE + inputs2[i], String.class);
            assertThat(response.getStatusCode()).as("Assertion at i=%d for url=%s", i, inputs2[i]).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).as("Assertion at i=%d for url=%s", i, inputs2[i]).isEqualTo("[]");
        }

        TESTS_PASSED.put("getMedia_ValidInputData_Returns200", true);
    }

//=================================================================================================================================
//PRIVATE METHODS
    private String[] getInvalidUrls() {
        return new String[]{
            "/search?page=0&size=1",
            "/search?page=-1&size=1",
            "/search?page=-5&size=1",
            "/search?page=-1101&size=1",
            "/search?page=1&size=0",
            "/search?page=1&size=-1",
            "/search?page=1&size=-5",
            "/search?page=1&size=-1101",
            "/search?page=1&size=101",
            "/search?page=1&size=10&title=",
            "/search?page=1&size=10&title= ",
            "/search?page=1&size=10&title=       ",
            "/search?page=1&size=10&title=" + TestUtil.getRandomString(301),
            "/search?page=1&size=10&title=text&genreIds=0",
            "/search?page=1&size=10&title=text&genreIds=1,2,0",
            "/search?page=1&size=10&title=text&genreIds=-1",
            "/search?page=1&size=10&title=text&genreIds=1,2,-1",
            "/search?page=1&size=10&title=text&genreIds=-5",
            "/search?page=1&size=10&title=text&genreIds=1,2,-5",
            "/search?page=1&size=10&title=text&genreIds=-1101",
            "/search?page=1&size=10&title=text&genreIds=1,2,-1101",
            "/search?page=1&size=10&title=text&genreIds=1,,2",
            "/search?page=1&size=10&title=text&genreIds=1,,",
            "/search?page=1&size=10&title=text&genreIds=,1,2",
            "/search?page=1&size=10&title=text&genreIds=1,,2",
            "/search?page=1&size=10&title=text&genreIds=,,,",
            "/search?page=1&size=10&title=text&genreIds=1,    ,2",
            "/search?page=1&size=10&title=text&genreIds=1,2,3,4,5,6",
            "/search?page=1&size=10&title=text&genreIds=1,2,3,4,5,6,7,8,9,10",
            "/search?page=1&size=10&title=text&genreIds=1,1,2",
            "/search?page=1&size=10&title=text&genreIds=1,2,1",
            "/search?page=1&size=10&title=text&genreIds=1,2,3,2,1",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=as",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=asca",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=ascendinga",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating= asc",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=asc ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating= asc ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desca",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating= descending",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=descending ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating= descending ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=as",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=asca",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=ascendinga",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate= asc",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=asc ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate= asc ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desca",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate= descending",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=descending ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate= descending ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=1964",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=1900",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=0",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=-1",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=-1965",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=-2024",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=a",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=aaaaaa",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=mov",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=moviee",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType= movie",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=movie  ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType= movie ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=tv show",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType= tv_show",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=tv_show ",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=tvv_show",
            "/search?page=1&size=10&title=text&genreIds=1,2&sortByAudienceRating=desc&sortByReleaseDate=desc&releaseYear=2020&mediaType=tv _show"
        };
    }

    private Pair<Integer, String>[] getValidUrlsAndNonEmptyResponses() {
        return new Pair[]{
            new Pair(1, "/search"),
            new Pair(1, "/search?page=1&size=10"),
            new Pair(2, "/search?page=1&size=10&title=a"),
            new Pair(3, "/search?page=1&size=10&title=c"),
            new Pair(4, "/search?page=1&size=10&title=Sou"),
            new Pair(5, "/search?page=1&size=10&title=st"),
            new Pair(6, "/search?page=1&size=10&title=South Park"),
            new Pair(7, "/search?page=1&size=10&title=ar"),
            new Pair(8, "/search?sortByAudienceRating=desc"),
            new Pair(9, "/search?sortByAudienceRating=ascending"),
            new Pair(10, "/search?sortByReleaseDate=desc"),
            new Pair(11, "/search?sortByReleaseDate=asc"),
            new Pair(12, "/search?sortByAudienceRating=asc&sortByReleaseDate=desc"),
            new Pair(13, "/search?sortByAudienceRating=desc&sortByReleaseDate=asc"),
            new Pair(14, "/search?title=an&sortByAudienceRating=desc&sortByReleaseDate=asc"),
            new Pair(15, "/search?genreIds=6,12&sortByAudienceRating=desc&sortByReleaseDate=asc"),
            new Pair(16, "/search?genreIds=6,13&sortByAudienceRating=desc&sortByReleaseDate=asc"),
            new Pair(17, "/search?genreIds=6,13,12&sortByAudienceRating=desc&sortByReleaseDate=asc"),
            new Pair(18, "/search?genreIds=13,6&sortByAudienceRating=asc&sortByReleaseDate=asc"),
            new Pair(19, "/search?genreIds=13,6&sortByAudienceRating=asc&sortByReleaseDate=asc&mediaType=movie"),
            new Pair(20, "/search?genreIds=13,6&sortByAudienceRating=asc&sortByReleaseDate=asc&mediaType=tv_show"),
            new Pair(20, "/search?genreIds=13,6&sortByAudienceRating=asc&sortByReleaseDate=asc&releaseYear=2004&mediaType=tv_show")
        };
    }

    private String[] getValidUrlsForEmptyResponses() {
        return new String[]{
            "/search?page=1&size=10&title=1",
            "/search?page=1&size=10&title=   a  ",
            "/search?page=1&size=10&title=Van Helsing",
            "/search?page=1&size=10&title=The Raging Bull",
            "/search?page=1&size=10&title=The Ra",
            "/search?page=1&size=10&title=South&genreIds=1",
            "/search?page=1&size=10&title=South&genreIds=1,2",
            "/search?page=1&size=10&title=South&genreIds=3,5",
            "/search?page=1&size=10&title=South&genreIds=3,4,5",
            "/search?page=1&size=10&title=South&genreIds=3,40",
            "/search?page=1&size=10&title=South&genreIds=3&releaseYear=2020",
            "/search?page=1&size=10&title=South&genreIds=3&releaseYear=1998",
            "/search?page=1&size=10&title=South&genreIds=3&releaseYear=1997&mediaType=movie",
            "/search?page=1&size=10&genreIds=1,2,3,4",
            "/search?page=1&size=10&genreIds=40",
            "/search?page=1&size=10&genreIds=1&mediaType=movie",
            "/search?page=1&size=10&genreIds=11&mediaType=tv_show",
            "/search?page=1&size=10&releaseYear=2001&mediaType=tv_show",
            "/search?page=1&size=10&releaseYear=2021&mediaType=movie",
            "/search?page=1&size=10&releaseYear=1965",
            "/search?page=1&size=10&releaseYear=2030",
            "/search?page=1&size=10&releaseYear=2001&mediaType=movie&title=Mull",
            "/search?page=1&size=10&genreIds=6&releaseYear=1965",
            "/search?page=1&size=10&genreIds=6,12&releaseYear=2000",
            "/search?page=1&size=10&genreIds=5,6,12",
            "/search?page=1&size=10&genreIds=6,12,5",
            "/search?page=1&size=10&genreIds=6,12&mediaType=tv_show",
            "/search?page=1&size=10&genreIds=8&mediaType=tv_show",
            "/search?page=1&size=10&genreIds=12,13&mediaType=movie&title=South"
        };
    }

}
