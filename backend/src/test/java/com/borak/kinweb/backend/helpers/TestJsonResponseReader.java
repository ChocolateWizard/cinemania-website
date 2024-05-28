/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.helpers;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public final class TestJsonResponseReader {

    private static final Logger log = LoggerFactory.getLogger(TestJsonResponseReader.class);

    @Value("${custom.property.jsonResponsesPropertiesFilePath}")
    private String jsonResponsesPropertiesFilePath;
    private Properties properties;

    @PostConstruct
    public void init() {
        properties = new Properties();
        try (InputStream input = (new ClassPathResource(jsonResponsesPropertiesFilePath)).getInputStream()) {
            properties.load(input);
        } catch (IOException ex) {
            log.error("Unable to load properties file: ", ex);
        }
    }

    public String getCountryJson(int number) {
        return properties.getProperty("country." + number);
    }

    public String getMovieJson(int number) {
        return properties.getProperty("movie." + number);
    }

    public String getTVShowJson(int number) {
        return properties.getProperty("tvShow." + number);
    }

    public String getMediaJson(int number) {
        return properties.getProperty("media." + number);
    }

    public String getPersonJson(int number) {
        return properties.getProperty("person." + number);
    }

}
