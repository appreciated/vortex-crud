package com.github.appreciated.vortex_crud.test.jpa.ui.field_types;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractFieldTypesTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = JpaFieldTypesTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "vaadin.productionMode=true")
@Sql(scripts = "field_types_test.sql")
public class JpaFieldTypesTest extends AbstractFieldTypesTest {
    @Override
    protected boolean supportsDateRangeFields() {
        return true;
    }
}
