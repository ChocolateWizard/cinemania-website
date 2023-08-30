/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend;

import com.borak.kinweb.backend.repository.jdbc.movie.MovieRepositoryJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author Mr. Poyo
 */
@SpringBootTest
@ActiveProfiles("test")
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MovieRepositoryJDBC movieRepo;

}
