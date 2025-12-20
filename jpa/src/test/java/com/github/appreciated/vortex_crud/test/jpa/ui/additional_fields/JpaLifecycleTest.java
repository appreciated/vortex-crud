package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractLifecycleTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "lifecycle_test.sql")
public class JpaLifecycleTest extends AbstractLifecycleTest {
}
