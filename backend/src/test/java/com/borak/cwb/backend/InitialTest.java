package com.borak.cwb.backend;

import com.borak.cwb.backend.helpers.TestResultsHelper;
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

    private static final Map<String, Boolean> TESTS_PASSED = new HashMap<>();

    static {
        TESTS_PASSED.put("contextLoads", false);
    }

    public static boolean didAllTestsPass() {
        for (boolean b : TESTS_PASSED.values()) {
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

        TESTS_PASSED.put("contextLoads", true);
    }

}
