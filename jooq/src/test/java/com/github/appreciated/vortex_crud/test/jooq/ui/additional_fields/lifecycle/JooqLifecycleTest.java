package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields.lifecycle;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractLifecycleTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("lifecycle_test.sql")
@SpringBootTest(classes = JooqLifecycleTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JooqLifecycleTest extends AbstractLifecycleTest {
}
