/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.h2.triggers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import org.h2.api.Trigger;

/**
 *
 * @author Mr. Poyo
 */
public class AftCritDelTrigger {

//    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        String sql = """
                   UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique
                   WHERE media.id = ?)
                   WHERE id=?;
                   """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, (Long) oldRow[1]);
            ps.setLong(2, (Long) oldRow[1]);
            ps.executeUpdate();
        }
    }

}
