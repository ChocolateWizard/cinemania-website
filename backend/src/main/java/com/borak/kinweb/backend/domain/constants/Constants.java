/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.constants;

/**
 *
 * @author Mr. Poyo
 */
public final class Constants {

    public static final String MEDIA_IMAGES_FOLDER_PATH = "src/main/resources/static/images/media/";
    public static final String PERSON_IMAGES_FOLDER_PATH = "src/main/resources/static/images/person/";
    public static final String USER_IMAGES_FOLDER_PATH = "src/main/resources/static/images/user/";

    public static final int SERVER_PORT = 8080;
    public static final String SERVER_ADDRESS = "http://localhost:" + SERVER_PORT + "/";

    public static final String MEDIA_IMAGES_BASE_URL = SERVER_ADDRESS + "images/media/";
    public static final String PERSON_IMAGES_BASE_URL = SERVER_ADDRESS + "images/person/";
    public static final String USER_IMAGES_BASE_URL = SERVER_ADDRESS + "images/user/";

    private Constants() {
    }

}
