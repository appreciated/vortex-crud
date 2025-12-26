package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route_factory;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomRouteFactoryTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
        JpaCustomRouteFactoryTestApplication.class,
        JpaCustomRouteFactoryTestVortexCrudConfiguration.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaCustomRouteFactoryTest extends AbstractCustomRouteFactoryTest {

    @Override
    protected String getNestedCustomRoutePath() {
        return "submenu/custom-nested";
    }
}
