package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.jooq.models.tables.ValidationTest;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("email-test")
public class JooqEmailFieldValidationConfiguration extends BaseJooqFieldValidationVortexCrudConfiguration {

    public JooqEmailFieldValidationConfiguration(DSLContext dsl) {
        super(dsl, new ValidationTest("validation_test_email"));
    }
}
