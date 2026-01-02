package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.jooq.models.tables.ValidationTest;
import org.jooq.DSLContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Unified field validation configuration supporting all test profiles.
 * Replaces individual configuration classes for each field type.
 */
@TestConfiguration
public class JooqFieldValidationConfiguration {

    @Bean
    @Profile("checkbox-test")
    public BaseJooqFieldValidationVortexCrudConfiguration checkboxFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_checkbox")) {};
    }

    @Bean
    @Profile("date-test")
    public BaseJooqFieldValidationVortexCrudConfiguration dateFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_date")) {};
    }

    @Bean
    @Profile("datetime-test")
    public BaseJooqFieldValidationVortexCrudConfiguration datetimeFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_datetime")) {};
    }

    @Bean
    @Profile("email-test")
    public BaseJooqFieldValidationVortexCrudConfiguration emailFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_email")) {};
    }

    @Bean
    @Profile("image-test")
    public BaseJooqFieldValidationVortexCrudConfiguration imageFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_image")) {};
    }

    @Bean
    @Profile("lifecycle-test")
    public BaseJooqFieldValidationVortexCrudConfiguration lifecycleFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_lifecycle")) {};
    }

    @Bean
    @Profile("number-test")
    public BaseJooqFieldValidationVortexCrudConfiguration numberFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_number")) {};
    }

    @Bean
    @Profile("select-test")
    public BaseJooqFieldValidationVortexCrudConfiguration selectFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_select")) {};
    }

    @Bean
    @Profile("text-test")
    public BaseJooqFieldValidationVortexCrudConfiguration textFieldValidationConfiguration(DSLContext dsl) {
        return new BaseJooqFieldValidationVortexCrudConfiguration(dsl, new ValidationTest("validation_test_text")) {};
    }
}
