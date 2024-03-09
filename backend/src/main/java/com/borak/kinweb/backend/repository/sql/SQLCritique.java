/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.repository.sql;

/**
 *
 * @author Mr. Poyo
 */
public class SQLCritique {

    public static final String FIND_ID_PS = """
                                          SELECT media_id 
                                          FROM critique 
                                          WHERE user_critic_id=? AND media_id=?; 
                                          """;
    public static final String INSERT_CRITIQUE_PS = """
                                          INSERT INTO critique(user_critic_id,media_id,description,rating) 
                                          VALUES(?,?,?,?);
                                          """;
    public static final String DELETE_CRITIQUE_PS = """
                                          DELETE FROM critique WHERE user_critic_id=? AND media_id=?;
                                          """;
    public static final String UPDATE_CRITIQUE_PS = """
                                          UPDATE critique 
                                          SET description = ?, rating = ? 
                                          WHERE user_critic_id=? AND media_id=?;
                                          """;
}
