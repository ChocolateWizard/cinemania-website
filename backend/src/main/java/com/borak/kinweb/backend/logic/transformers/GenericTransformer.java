/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.DTO;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;
import java.util.List;
import com.borak.kinweb.backend.domain.jdbc.classes.JDBC;

/**
 *
 * @author Mr. Poyo
 */
public interface GenericTransformer<D extends DTO, JD extends JDBC, JP extends JPA> {

    public D jdbcToDto(JD jdbc);

    public D jpaToDto(JP jpa);

    public List<D> jdbcToDto(List<JD> jdbcList);

    public List<D> jpaToDto(List<JP> jpaList);

}
