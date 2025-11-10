package com.github.appreciated.vortex_crud.test.jooq.ui.multi_form;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractMultiFormRouteTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = JooqMultiFormTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "multi_form_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JooqMultiFormTest extends AbstractMultiFormRouteTest {
}
