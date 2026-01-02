package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import com.github.appreciated.vortex_crud.jooq.models.tables.ValidationTest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.VALIDATION_TEST;
import static com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum.*;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

public abstract class BaseJooqFieldValidationVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private final ValidationTest table;

    public BaseJooqFieldValidationVortexCrudConfiguration(DSLContext dsl, ValidationTest table) {
        this.dsl = dsl;
        this.table = table;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(table.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(table)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                table.ID, JooqNumericIdField.builder().build(),
                                table.REQUIRED_FIELD, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                                table.EMAIL_FIELD, JooqEmailField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                                table.NUMERIC_FIELD, JooqDoubleField.builder().validators(List.of(new DoubleRangeValidator("Value must be at least 1.0", 1.0, Double.MAX_VALUE))).build(),
                                table.DATE_FIELD, JooqDateField.builder().build(),
                                table.DATETIME_FIELD, JooqDateTimePickerField.builder().build(),
                                table.ENUM_FIELD, JooqSelectField.builder().values("enum-options").build(),
                                table.CHECKBOX_FIELD, JooqCheckboxField.builder().build(),
                                table.IMAGE_FIELD, JooqImageField.builder().resourceProvider(new LocalImageResourceProvider()).build()
                        )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> validationForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField(table.REQUIRED_FIELD)
                .fields(List.of(
                        JooqFieldElement.of(table.REQUIRED_FIELD, "validation.fields.required").build(),
                        JooqFieldElement.of(table.EMAIL_FIELD, "validation.fields.email").build(),
                        JooqFieldElement.of(table.NUMERIC_FIELD, "validation.fields.numeric").build(),
                        JooqFieldElement.of(table.DATE_FIELD, "validation.fields.date").build(),
                        JooqFieldElement.of(table.DATETIME_FIELD, "validation.fields.datetime").build(),
                        JooqFieldElement.of(table.CHECKBOX_FIELD, "validation.fields.checkbox").build(),
                        JooqFieldElement.of(table.ENUM_FIELD, "validation.fields.enum").build(),
                        JooqFieldElement.of(table.IMAGE_FIELD, "validation.fields.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("field-validation-test", JooqListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .filterField(table.REQUIRED_FIELD)
                .columns(List.of(
                        JooqFieldElement.of(table.REQUIRED_FIELD, "route.projects.labels.name").build(),
                        JooqFieldElement.of(table.EMAIL_FIELD, "route.projects.labels.description").build()
                ))
                .form(validationForm)
                .build());

        LinkedHashMap<JooqFieldValidationTestEnum, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put(OPTION1, "enums.option1");
        enumOptions.put(OPTION2, "enums.option2");
        enumOptions.put(OPTION3, "enums.option3");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}
