package com.github.appreciated.vortex_crud.test.jpa.ui.single_component.single_component_route;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractSingleComponentRouteTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = JpaSingleComponentRouteTestApplication.class, webEnvironment = RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
@Sql(scripts = "classpath:single_form_route_test.sql")
public class JpaSingleComponentRouteTest extends AbstractSingleComponentRouteTest {
}
