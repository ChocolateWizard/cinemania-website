package com.borak.kinweb.backend;

import com.borak.kinweb.backend.domain.dto.classes.ActingDTO;
import com.borak.kinweb.backend.domain.dto.classes.MediaDTO;
import com.borak.kinweb.backend.domain.dto.classes.MovieDTO;
import com.borak.kinweb.backend.domain.dto.classes.TVShowDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class KinomaniaWebsiteBackendApplication {

    public static void main(String[] args) {
//        SpringApplication.run(KinomaniaWebsiteBackendApplication.class, args);
        MovieDTO movie = new MovieDTO();
        TVShowDTO show = new TVShowDTO();
        MediaDTO media;
        media=movie;
        System.out.println(""+media.getClass());
        media=show;
        System.out.println(""+media.getClass());
        
        

    }

}
