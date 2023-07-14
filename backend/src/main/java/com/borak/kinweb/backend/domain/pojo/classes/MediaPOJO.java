/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.pojo.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mr Poyo
 */
public class MediaPOJO implements POJO {

    private Long id;

    private String title;

    private String coverImageUrl;

    private String description;

    private LocalDate releaseDate;

    private Integer rating;

    private List<GenrePOJO> genres = new ArrayList<>();

    private List<CritiquePOJO> critiques = new ArrayList<>();

    private MediaCastPOJO cast;
    private MediaCrewPOJO crew;

   

}
