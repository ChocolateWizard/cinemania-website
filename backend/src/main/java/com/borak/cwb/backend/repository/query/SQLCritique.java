/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.repository.query;

import com.borak.cwb.backend.domain.jdbc.CritiqueJDBC;
import com.borak.cwb.backend.domain.jdbc.MediaJDBC;
import com.borak.cwb.backend.domain.jdbc.UserJDBC;
import org.springframework.jdbc.core.RowMapper;

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
    public static final String FIND_BY_ID_PS = """
                                          SELECT critique.user_critic_id,critique.media_id,critique.description,critique.rating                                          
                                          FROM critique 
                                          WHERE critique.user_critic_id=? AND critique.media_id=?;
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
//================================================================================================
//========================================RowMappers==============================================
//================================================================================================
    public static final RowMapper<CritiqueJDBC> critiqueRM = (rs, num) -> {
        CritiqueJDBC critique = new CritiqueJDBC();
        critique.setCritic(new UserJDBC(rs.getLong("user_critic_id")));
        critique.setMedia(new MediaJDBC(rs.getLong("media_id")));
        critique.setDescription(rs.getString("description"));
        critique.setRating(rs.getInt("rating"));
        return critique;
    };

}
