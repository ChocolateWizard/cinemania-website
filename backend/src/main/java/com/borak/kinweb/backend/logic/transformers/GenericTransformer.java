/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.transformers;

import com.borak.kinweb.backend.domain.dto.classes.DTO;
import com.borak.kinweb.backend.domain.jpa.classes.JPA;
import com.borak.kinweb.backend.domain.pojo.classes.POJO;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 * @param <D>
 * @param <P>
 * @param <J>
 */
public interface GenericTransformer<D extends DTO, P extends POJO, J extends JPA> {

    public D pojoToDto(P pojo);

    public D jpaToDto(J jpa);

    public List<D> pojoToDto(List<P> pojoList);

    public List<D> jpaToDto(List<J> jpaList);
    
   
}
