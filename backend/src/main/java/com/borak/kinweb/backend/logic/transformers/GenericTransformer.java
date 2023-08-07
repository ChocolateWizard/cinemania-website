/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.DTO;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;
import java.util.List;
import com.borak.kinweb.backend.domain.jdbc.classes.JDBC;
import java.util.ArrayList;

/**
 *
 * @author Mr. Poyo
 */
public interface GenericTransformer<D extends DTO, JD extends JDBC, JP extends JPA> {

    //throws NullPointerException if null passed
    public D jdbcToDto(JD jdbc) throws IllegalArgumentException;

    public D jpaToDto(JP jpa) throws IllegalArgumentException;

    public default List<D> jdbcToDto(List<JD> jdbcList) throws IllegalArgumentException {
        if(jdbcList==null){
            throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<D> list = new ArrayList<>();
        for (JD jd : jdbcList) {
            list.add(jdbcToDto(jd));
        }
        return list;
    }

    public default List<D> jpaToDto(List<JP> jpaList) throws IllegalArgumentException {
        if(jpaList==null){
             throw new IllegalArgumentException("Null passed as method parameter");
        }
        List<D> list = new ArrayList<>();
        for (JP jp : jpaList) {
            list.add(jpaToDto(jp));
        }
        return list;
    }

}
