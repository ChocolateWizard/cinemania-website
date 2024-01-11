/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.borak.kinweb.backend.logic.services.critique;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Mr. Poyo
 */
public interface ICritiqueService<C, ID> {

    public ResponseEntity postCritique(C critiqueRequest);

    public ResponseEntity putCritique(C critiqueRequest);

    public ResponseEntity deleteCritique(ID id);

}
