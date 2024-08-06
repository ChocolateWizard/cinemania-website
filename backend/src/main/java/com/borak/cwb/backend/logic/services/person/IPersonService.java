/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.cwb.backend.logic.services.person;

import com.borak.cwb.backend.domain.dto.DTO;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 * @param <D> person request object
 */
public interface IPersonService<D extends DTO> {

    public ResponseEntity getAllPersonsPaginated(int page, int size);

    public ResponseEntity getAllPersonsWithDetailsPaginated(int page, int size);

    public ResponseEntity getPersonWithProfessions(long id);

    public ResponseEntity getPersonWithDetails(long id);

    public ResponseEntity postPerson(D person);

    public ResponseEntity putPerson(D person);

    public ResponseEntity deletePersonById(long id);

}
