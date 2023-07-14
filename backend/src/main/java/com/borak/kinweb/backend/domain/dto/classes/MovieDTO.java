/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.classes;

import com.borak.kinweb.backend.domain.pojo.classes.*;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Mr Poyo
 */
public class MovieDTO extends MediaDTO {

    private Integer length;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
