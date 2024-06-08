package com.borak.kinweb.backend;

import com.borak.kinweb.backend.helpers.TestResultsHelper;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Order(1)
public class InitialTest {

    private static final Map<String, Boolean> testsPassed = new HashMap<>();

    static {
        testsPassed.put("contextLoads", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : testsPassed.values()) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    @Test
    void contextLoads() {
        boolean didAllTestsFail = TestResultsHelper.didAllTestsFail();
        assertThat(didAllTestsFail).isTrue();
        testsPassed.put("contextLoads", true);
    }

}
