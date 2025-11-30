package com.github.appreciated.vortex_crud.demo.resourceplanner;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.Tables.APPOINTMENT_TYPE;

@SpringBootTest(properties = "spring.datasource.url=jdbc:sqlite::memory:")
class ResourcePlannerDemoTest {

    @Autowired
    private ResourcePlannerConfig config;

    @Autowired
    private DSLContext dsl;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(config, "ResourcePlannerConfig should be loaded");
        Assertions.assertNotNull(dsl, "DSLContext should be loaded");

        // Verify DB connectivity and Schema existence
        int count = dsl.fetchCount(APPOINTMENT_TYPE);
        Assertions.assertTrue(count >= 0, "Should be able to count records in APPOINTMENT_TYPE");
    }

}
