package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.textarea;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractTextAreaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql( "textarea_test.sql")
public class JpaTextAreaTest extends AbstractTextAreaTest {
}
