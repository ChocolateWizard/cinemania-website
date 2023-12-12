package com.borak.kinweb.backend;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Order(1)
class KinomaniaWebsiteBackendApplicationTests {

    @AfterAll
    static void dummy() {
        System.out.println(""+KinomaniaWebsiteBackendApplicationTests.class.getName());
    }
    
    @Test
    void contextLoads_Test() {

    }

    
    
}
