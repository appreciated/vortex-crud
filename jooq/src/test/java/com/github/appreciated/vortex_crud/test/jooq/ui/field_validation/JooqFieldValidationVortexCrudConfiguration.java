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
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;
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

        addRoute(routes, "field-validation-test", VALIDATION_TEST, VALIDATION_TEST.ID, VALIDATION_TEST.REQUIRED_FIELD, VALIDATION_TEST.EMAIL_FIELD, VALIDATION_TEST.NUMERIC_FIELD, VALIDATION_TEST.DATE_FIELD, VALIDATION_TEST.DATETIME_FIELD, VALIDATION_TEST.ENUM_FIELD, VALIDATION_TEST.CHECKBOX_FIELD, VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "checkbox-validation-test", CHECKBOX_VALIDATION_TEST, CHECKBOX_VALIDATION_TEST.ID, CHECKBOX_VALIDATION_TEST.REQUIRED_FIELD, CHECKBOX_VALIDATION_TEST.EMAIL_FIELD, CHECKBOX_VALIDATION_TEST.NUMERIC_FIELD, CHECKBOX_VALIDATION_TEST.DATE_FIELD, CHECKBOX_VALIDATION_TEST.DATETIME_FIELD, CHECKBOX_VALIDATION_TEST.ENUM_FIELD, CHECKBOX_VALIDATION_TEST.CHECKBOX_FIELD, CHECKBOX_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "date-validation-test", DATE_VALIDATION_TEST, DATE_VALIDATION_TEST.ID, DATE_VALIDATION_TEST.REQUIRED_FIELD, DATE_VALIDATION_TEST.EMAIL_FIELD, DATE_VALIDATION_TEST.NUMERIC_FIELD, DATE_VALIDATION_TEST.DATE_FIELD, DATE_VALIDATION_TEST.DATETIME_FIELD, DATE_VALIDATION_TEST.ENUM_FIELD, DATE_VALIDATION_TEST.CHECKBOX_FIELD, DATE_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "datetime-validation-test", DATETIME_VALIDATION_TEST, DATETIME_VALIDATION_TEST.ID, DATETIME_VALIDATION_TEST.REQUIRED_FIELD, DATETIME_VALIDATION_TEST.EMAIL_FIELD, DATETIME_VALIDATION_TEST.NUMERIC_FIELD, DATETIME_VALIDATION_TEST.DATE_FIELD, DATETIME_VALIDATION_TEST.DATETIME_FIELD, DATETIME_VALIDATION_TEST.ENUM_FIELD, DATETIME_VALIDATION_TEST.CHECKBOX_FIELD, DATETIME_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "email-validation-test", EMAIL_VALIDATION_TEST, EMAIL_VALIDATION_TEST.ID, EMAIL_VALIDATION_TEST.REQUIRED_FIELD, EMAIL_VALIDATION_TEST.EMAIL_FIELD, EMAIL_VALIDATION_TEST.NUMERIC_FIELD, EMAIL_VALIDATION_TEST.DATE_FIELD, EMAIL_VALIDATION_TEST.DATETIME_FIELD, EMAIL_VALIDATION_TEST.ENUM_FIELD, EMAIL_VALIDATION_TEST.CHECKBOX_FIELD, EMAIL_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "image-validation-test", IMAGE_VALIDATION_TEST, IMAGE_VALIDATION_TEST.ID, IMAGE_VALIDATION_TEST.REQUIRED_FIELD, IMAGE_VALIDATION_TEST.EMAIL_FIELD, IMAGE_VALIDATION_TEST.NUMERIC_FIELD, IMAGE_VALIDATION_TEST.DATE_FIELD, IMAGE_VALIDATION_TEST.DATETIME_FIELD, IMAGE_VALIDATION_TEST.ENUM_FIELD, IMAGE_VALIDATION_TEST.CHECKBOX_FIELD, IMAGE_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "number-validation-test", NUMBER_VALIDATION_TEST, NUMBER_VALIDATION_TEST.ID, NUMBER_VALIDATION_TEST.REQUIRED_FIELD, NUMBER_VALIDATION_TEST.EMAIL_FIELD, NUMBER_VALIDATION_TEST.NUMERIC_FIELD, NUMBER_VALIDATION_TEST.DATE_FIELD, NUMBER_VALIDATION_TEST.DATETIME_FIELD, NUMBER_VALIDATION_TEST.ENUM_FIELD, NUMBER_VALIDATION_TEST.CHECKBOX_FIELD, NUMBER_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "select-validation-test", SELECT_VALIDATION_TEST, SELECT_VALIDATION_TEST.ID, SELECT_VALIDATION_TEST.REQUIRED_FIELD, SELECT_VALIDATION_TEST.EMAIL_FIELD, SELECT_VALIDATION_TEST.NUMERIC_FIELD, SELECT_VALIDATION_TEST.DATE_FIELD, SELECT_VALIDATION_TEST.DATETIME_FIELD, SELECT_VALIDATION_TEST.ENUM_FIELD, SELECT_VALIDATION_TEST.CHECKBOX_FIELD, SELECT_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "text-validation-test", TEXT_VALIDATION_TEST, TEXT_VALIDATION_TEST.ID, TEXT_VALIDATION_TEST.REQUIRED_FIELD, TEXT_VALIDATION_TEST.EMAIL_FIELD, TEXT_VALIDATION_TEST.NUMERIC_FIELD, TEXT_VALIDATION_TEST.DATE_FIELD, TEXT_VALIDATION_TEST.DATETIME_FIELD, TEXT_VALIDATION_TEST.ENUM_FIELD, TEXT_VALIDATION_TEST.CHECKBOX_FIELD, TEXT_VALIDATION_TEST.IMAGE_FIELD);
        addRoute(routes, "lifecycle-validation-test", LIFECYCLE_VALIDATION_TEST, LIFECYCLE_VALIDATION_TEST.ID, LIFECYCLE_VALIDATION_TEST.REQUIRED_FIELD, LIFECYCLE_VALIDATION_TEST.EMAIL_FIELD, LIFECYCLE_VALIDATION_TEST.NUMERIC_FIELD, LIFECYCLE_VALIDATION_TEST.DATE_FIELD, LIFECYCLE_VALIDATION_TEST.DATETIME_FIELD, LIFECYCLE_VALIDATION_TEST.ENUM_FIELD, LIFECYCLE_VALIDATION_TEST.CHECKBOX_FIELD, LIFECYCLE_VALIDATION_TEST.IMAGE_FIELD);


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

    private <R extends UpdatableRecord<R>> void addRoute(LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes, String routeName,
                          TableImpl<R> table,
                          TableField idField,
                          TableField requiredField,
                          TableField emailField,
                          TableField numericField,
                          TableField dateField,
                          TableField datetimeField,
                          TableField enumField,
                          TableField checkboxField,
                          TableField imageField) {
        JooqDataStore store = new JooqDataStore(table.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(table)
                .dataStoreInstance((VortexCrudDataStore) store)
                .fields(Map.of(
                        idField, JooqNumericIdField.builder().build(),
                        requiredField, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        emailField, JooqEmailField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        numericField, JooqDoubleField.builder().validators(List.of(new DoubleRangeValidator("Value must be at least 1.0", 1.0, Double.MAX_VALUE))).build(),
                        dateField, JooqDateField.builder().build(),
                        datetimeField, JooqDateTimePickerField.builder().build(),
                        enumField, JooqSelectField.builder().values("enum-options").build(),
                        checkboxField, JooqCheckboxField.builder().build(),
                        imageField, JooqImageField.builder().resourceProvider(new LocalImageResourceProvider()).build()
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
                        JooqFieldElement.of(datetimeField, "validation.fields.datetime").build(),
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
