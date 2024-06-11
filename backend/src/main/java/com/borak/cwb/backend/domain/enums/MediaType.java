/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.borak.cwb.backend.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Mr. Poyo
 */
public enum MediaType {
    MOVIE("movie"), TV_SHOW("tv_show");

    private final String text;

    private MediaType(String text) {
        this.text = text;
    }

    public static MediaType parseMediaType(String mediaType) throws UnsupportedOperationException {
        if (mediaType == null) {
            return null;
        }
        switch (mediaType.toLowerCase()) {
            case "movie":
                return MOVIE;
            case "tv_show":
                return TV_SHOW;
            default:
                throw new UnsupportedOperationException("The media type " + mediaType + " is not supported!");
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return text;
    }

}
