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
import org.jooq.UpdatableRecord;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.*;
import static com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum.*;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqFieldValidationVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqFieldValidationVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // Register routes for each test table
        registerRoute(routes, "text-field-test", TEXT_FIELD_TEST, TEXT_FIELD_TEST.ID, TEXT_FIELD_TEST.REQUIRED_FIELD, TEXT_FIELD_TEST.EMAIL_FIELD, TEXT_FIELD_TEST.NUMERIC_FIELD, TEXT_FIELD_TEST.DATE_FIELD, TEXT_FIELD_TEST.DATETIME_FIELD, TEXT_FIELD_TEST.CHECKBOX_FIELD, TEXT_FIELD_TEST.ENUM_FIELD, TEXT_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "checkbox-field-test", CHECKBOX_FIELD_TEST, CHECKBOX_FIELD_TEST.ID, CHECKBOX_FIELD_TEST.REQUIRED_FIELD, CHECKBOX_FIELD_TEST.EMAIL_FIELD, CHECKBOX_FIELD_TEST.NUMERIC_FIELD, CHECKBOX_FIELD_TEST.DATE_FIELD, CHECKBOX_FIELD_TEST.DATETIME_FIELD, CHECKBOX_FIELD_TEST.CHECKBOX_FIELD, CHECKBOX_FIELD_TEST.ENUM_FIELD, CHECKBOX_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "date-field-test", DATE_FIELD_TEST, DATE_FIELD_TEST.ID, DATE_FIELD_TEST.REQUIRED_FIELD, DATE_FIELD_TEST.EMAIL_FIELD, DATE_FIELD_TEST.NUMERIC_FIELD, DATE_FIELD_TEST.DATE_FIELD, DATE_FIELD_TEST.DATETIME_FIELD, DATE_FIELD_TEST.CHECKBOX_FIELD, DATE_FIELD_TEST.ENUM_FIELD, DATE_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "datetime-field-test", DATETIME_FIELD_TEST, DATETIME_FIELD_TEST.ID, DATETIME_FIELD_TEST.REQUIRED_FIELD, DATETIME_FIELD_TEST.EMAIL_FIELD, DATETIME_FIELD_TEST.NUMERIC_FIELD, DATETIME_FIELD_TEST.DATE_FIELD, DATETIME_FIELD_TEST.DATETIME_FIELD, DATETIME_FIELD_TEST.CHECKBOX_FIELD, DATETIME_FIELD_TEST.ENUM_FIELD, DATETIME_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "email-field-test", EMAIL_FIELD_TEST, EMAIL_FIELD_TEST.ID, EMAIL_FIELD_TEST.REQUIRED_FIELD, EMAIL_FIELD_TEST.EMAIL_FIELD, EMAIL_FIELD_TEST.NUMERIC_FIELD, EMAIL_FIELD_TEST.DATE_FIELD, EMAIL_FIELD_TEST.DATETIME_FIELD, EMAIL_FIELD_TEST.CHECKBOX_FIELD, EMAIL_FIELD_TEST.ENUM_FIELD, EMAIL_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "image-field-test", IMAGE_FIELD_TEST, IMAGE_FIELD_TEST.ID, IMAGE_FIELD_TEST.REQUIRED_FIELD, IMAGE_FIELD_TEST.EMAIL_FIELD, IMAGE_FIELD_TEST.NUMERIC_FIELD, IMAGE_FIELD_TEST.DATE_FIELD, IMAGE_FIELD_TEST.DATETIME_FIELD, IMAGE_FIELD_TEST.CHECKBOX_FIELD, IMAGE_FIELD_TEST.ENUM_FIELD, IMAGE_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "number-field-test", NUMBER_FIELD_TEST, NUMBER_FIELD_TEST.ID, NUMBER_FIELD_TEST.REQUIRED_FIELD, NUMBER_FIELD_TEST.EMAIL_FIELD, NUMBER_FIELD_TEST.NUMERIC_FIELD, NUMBER_FIELD_TEST.DATE_FIELD, NUMBER_FIELD_TEST.DATETIME_FIELD, NUMBER_FIELD_TEST.CHECKBOX_FIELD, NUMBER_FIELD_TEST.ENUM_FIELD, NUMBER_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "select-field-test", SELECT_FIELD_TEST, SELECT_FIELD_TEST.ID, SELECT_FIELD_TEST.REQUIRED_FIELD, SELECT_FIELD_TEST.EMAIL_FIELD, SELECT_FIELD_TEST.NUMERIC_FIELD, SELECT_FIELD_TEST.DATE_FIELD, SELECT_FIELD_TEST.DATETIME_FIELD, SELECT_FIELD_TEST.CHECKBOX_FIELD, SELECT_FIELD_TEST.ENUM_FIELD, SELECT_FIELD_TEST.IMAGE_FIELD);
        registerRoute(routes, "lifecycle-field-test", LIFECYCLE_FIELD_TEST, LIFECYCLE_FIELD_TEST.ID, LIFECYCLE_FIELD_TEST.REQUIRED_FIELD, LIFECYCLE_FIELD_TEST.EMAIL_FIELD, LIFECYCLE_FIELD_TEST.NUMERIC_FIELD, LIFECYCLE_FIELD_TEST.DATE_FIELD, LIFECYCLE_FIELD_TEST.DATETIME_FIELD, LIFECYCLE_FIELD_TEST.CHECKBOX_FIELD, LIFECYCLE_FIELD_TEST.ENUM_FIELD, LIFECYCLE_FIELD_TEST.IMAGE_FIELD);

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

    private <R extends UpdatableRecord<R>> void registerRoute(LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes, String routeName, TableImpl<R> table, TableField<?, Integer> idField, TableField<?, String> requiredField, TableField<?, String> emailField, TableField<?, Double> numericField, TableField<?, ?> dateField, TableField<?, ?> dateTimeField, TableField<?, Boolean> checkboxField, TableField<?, ?> enumField, TableField<?, String> imageField) {
        // Cast to Class<R> explicitly to solve type inference issue
        Class<R> recordType = (Class<R>) table.getRecordType();
        JooqDataStore<R> store = new JooqDataStore<>(recordType, dsl);
        // Cast fields to raw types to bypass generic wildcard issues in the map
        TableField rawIdField = idField;
        TableField rawRequiredField = requiredField;
        TableField rawEmailField = emailField;
        TableField rawNumericField = numericField;
        TableField rawDateField = dateField;
        TableField rawDateTimeField = dateTimeField;
        TableField rawCheckboxField = checkboxField;
        TableField rawEnumField = enumField;
        TableField rawImageField = imageField;

        var config = JooqDataStoreConfig.of(table)
                .dataStoreInstance((VortexCrudDataStore) store)
                .fields(Map.of(
                        rawIdField, JooqNumericIdField.builder().build(),
                        rawRequiredField, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        rawEmailField, JooqEmailField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        rawNumericField, JooqDoubleField.builder().validators(List.of(new DoubleRangeValidator("Value must be at least 1.0", 1.0, Double.MAX_VALUE))).build(),
                        rawDateField, JooqDateField.builder().build(),
                        rawDateTimeField, JooqDateTimePickerField.builder().build(),
                        rawEnumField, JooqSelectField.builder().values("enum-options").build(),
                        rawCheckboxField, JooqCheckboxField.builder().build(),
                        rawImageField, JooqImageField.builder().resourceProvider(new LocalImageResourceProvider()).build()
                )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> validationForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField(requiredField)
                .fields(List.of(
                        JooqFieldElement.of(requiredField, "validation.fields.required").build(),
                        JooqFieldElement.of(emailField, "validation.fields.email").build(),
                        JooqFieldElement.of(numericField, "validation.fields.numeric").build(),
                        JooqFieldElement.of(dateField, "validation.fields.date").build(),
                        JooqFieldElement.of(dateTimeField, "validation.fields.datetime").build(),
                        JooqFieldElement.of(checkboxField, "validation.fields.checkbox").build(),
                        JooqFieldElement.of(enumField, "validation.fields.enum").build(),
                        JooqFieldElement.of(imageField, "validation.fields.image").build()
                ))
                .build();

        routes.put(routeName, JooqListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .filterField(requiredField)
                .columns(List.of(
                        JooqFieldElement.of(requiredField, "route.projects.labels.name").build(),
                        JooqFieldElement.of(emailField, "route.projects.labels.description").build()
                ))
                .form(validationForm)
                .build());
    }
}
