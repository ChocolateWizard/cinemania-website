/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author Mr. Poyo
 */
@Validated
@ConfigurationProperties(prefix = "kinweb.property")
public class ConfigProperties {

    @NotBlank
    private final String serverAddress;
    
    @NotNull
    @Min(value = 8080)
    @Max(value = 8100)
    private final Integer serverPort;

    @NotBlank
    private final String mediaImagesFolderPath;

    @NotBlank
    private final String personImagesFolderPath;

    @NotBlank
    private final String userImagesFolderPath;

    @NotBlank
    private final String mediaImagesBaseUrl;

    @NotBlank
    private final String personImagesBaseUrl;

    @NotBlank
    private final String userImagesBaseUrl;

    public ConfigProperties(String serverAddress, Integer serverPort, String mediaImagesFolderPath, String personImagesFolderPath, String userImagesFolderPath, String mediaImagesBaseUrl, String personImagesBaseUrl, String userImagesBaseUrl) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.mediaImagesFolderPath = mediaImagesFolderPath;
        this.personImagesFolderPath = personImagesFolderPath;
        this.userImagesFolderPath = userImagesFolderPath;
        this.mediaImagesBaseUrl = mediaImagesBaseUrl;
        this.personImagesBaseUrl = personImagesBaseUrl;
        this.userImagesBaseUrl = userImagesBaseUrl;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public String getMediaImagesFolderPath() {
        return mediaImagesFolderPath;
    }

    public String getPersonImagesFolderPath() {
        return personImagesFolderPath;
    }

    public String getUserImagesFolderPath() {
        return userImagesFolderPath;
    }

    public String getMediaImagesBaseUrl() {
        return mediaImagesBaseUrl;
    }

    public String getPersonImagesBaseUrl() {
        return personImagesBaseUrl;
    }

    public String getUserImagesBaseUrl() {
        return userImagesBaseUrl;
    }

  
    
    
    

}
