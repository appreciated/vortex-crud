package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route_factory;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomRouteFactoryTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
        JooqCustomRouteFactoryTestApplication.class,
        JooqCustomRouteFactoryTestVortexCrudConfiguration.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.datasource.url=jdbc:sqlite::memory:")
public class JooqCustomRouteFactoryTest extends AbstractCustomRouteFactoryTest {

    @Override
    protected String getNestedCustomRoutePath() {
        return "submenu/custom-nested";
    }
}
