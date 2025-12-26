package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.textarea;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractTextAreaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "textarea_test.sql")
@SpringBootTest(classes = JpaTextAreaTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaTextAreaTest extends AbstractTextAreaTest {
}
