/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.seeder.domain.api.ApiDetailsMovie;
import com.borak.kinweb.backend.seeder.domain.api.ApiDetailsTV;
import com.borak.kinweb.backend.seeder.domain.api.ApiDiscoverMovie;
import com.borak.kinweb.backend.seeder.domain.api.ApiDiscoverMovieResponse;
import com.borak.kinweb.backend.seeder.domain.api.ApiDiscoverTV;
import com.borak.kinweb.backend.seeder.domain.api.ApiDiscoverTVResponse;
import com.borak.kinweb.backend.seeder.domain.api.ApiGenre;
import com.borak.kinweb.backend.seeder.domain.api.ApiGenreResponse;
import com.borak.kinweb.backend.seeder.domain.db.GenreDB;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Mr. Poyo
 */
public class ApiCaller {

    private static ApiCaller instance;

    private final RestTemplate api;
    private static int timesApiCalled = 0;

    private ApiCaller() {
        this.api = new RestTemplate();
    }

    public static ApiCaller getInstance() throws InterruptedException {
        if (instance == null) {
            instance = new ApiCaller();
        }
        if (++timesApiCalled >= 50) {
            timesApiCalled = 0;
            Thread.sleep(1000);
        }
        return instance;
    }
//=============================================================================================================

    public List<ApiGenre> getGenres(String url) throws Exception {
        ResponseEntity<ApiGenreResponse> response = api.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ApiGenreResponse>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getGenres();
        } else {
            throw new Exception("Unable to retreive genre information!");
        }
    }

    public List<ApiDiscoverMovie> getDiscoveredMovies(String url) throws Exception {
        ResponseEntity<ApiDiscoverMovieResponse> response = api.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ApiDiscoverMovieResponse>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getResults();
        } else {
            throw new Exception("Unable to retreive discovered movies information!");
        }
    }

    public ApiDetailsMovie getMovieDetails(String url) throws Exception {
        ResponseEntity<ApiDetailsMovie> response = api.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ApiDetailsMovie>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Unable to retreive movie details information!");
        }
    }

    public MyImage getPosterImageAsMyImage(String url, String filename) throws Exception {
        ResponseEntity<byte[]> response = api.getForEntity(url, byte[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new MyImage(filename, response.getBody());
        } else {
            throw new Exception("Unable to retreive cover image bytes!");
        }
    }

    public List<ApiDiscoverTV> getDiscoveredTV(String url) throws Exception {
        ResponseEntity<ApiDiscoverTVResponse> response = api.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ApiDiscoverTVResponse>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getResults();
        } else {
            throw new Exception("Unable to retreive discovered tv shows information!");
        }

    }

    public ApiDetailsTV getTVDetails(String url) throws Exception {
        ResponseEntity<ApiDetailsTV> response = api.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ApiDetailsTV>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Unable to retreive tv show details information!");
        }
    }

}
