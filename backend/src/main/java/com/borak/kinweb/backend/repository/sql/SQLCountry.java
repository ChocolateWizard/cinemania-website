/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

/**
 *
 * @author Mr. Poyo
 */
public class SQLCountry {

    public static final String FIND_ID_PS = """
                                       SELECT id 
                                       FROM country 
                                       WHERE id=?;
                                       """;

}
