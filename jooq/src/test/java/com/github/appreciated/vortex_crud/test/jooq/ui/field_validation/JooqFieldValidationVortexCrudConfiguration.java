package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.VALIDATION_TEST;
import static com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum.*;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqFieldValidationVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                VALIDATION_TEST, JooqDataStoreConfig.of(VALIDATION_TEST)
                        .fields(Map.of(
                                VALIDATION_TEST.ID, JooqIdField.builder().build(),
                                VALIDATION_TEST.REQUIRED_FIELD, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                                VALIDATION_TEST.EMAIL_FIELD, JooqEmailField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                                VALIDATION_TEST.NUMERIC_FIELD, JooqDoubleField.builder().validators(List.of(new DoubleRangeValidator("Value must be at least 1.0", 1.0, Double.MAX_VALUE))).build(),
                                VALIDATION_TEST.DATE_FIELD, JooqDateField.builder().build(),
                                VALIDATION_TEST.DATETIME_FIELD, JooqDateTimePickerField.builder().build(),
                                VALIDATION_TEST.ENUM_FIELD, JooqSelectField.builder().values("enum-options").build(),
                                VALIDATION_TEST.CHECKBOX_FIELD, JooqCheckboxField.builder().build(),
                                VALIDATION_TEST.IMAGE_FIELD, JooqImageField.builder().configuration(ImageFieldRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(LocalImageResourceProvider.class)
                                        .build()).build()
                        )).build()
        );

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> validationForm = JooqFormRoute.builder()
                .dataStoreKey(VALIDATION_TEST)
                .title("route.projects.title-cards")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(VALIDATION_TEST.REQUIRED_FIELD)
                        .children(List.of(
                                JooqFieldElement.of(VALIDATION_TEST.REQUIRED_FIELD, "validation.fields.required").build(),
                                JooqFieldElement.of(VALIDATION_TEST.EMAIL_FIELD, "validation.fields.email").build(),
                                JooqFieldElement.of(VALIDATION_TEST.NUMERIC_FIELD, "validation.fields.numeric").build(),
                                JooqFieldElement.of(VALIDATION_TEST.DATE_FIELD, "validation.fields.date").build(),
                                JooqFieldElement.of(VALIDATION_TEST.DATETIME_FIELD, "validation.fields.datetime").build(),
                                JooqFieldElement.of(VALIDATION_TEST.CHECKBOX_FIELD, "validation.fields.checkbox").build(),
                                JooqFieldElement.of(VALIDATION_TEST.ENUM_FIELD, "validation.fields.enum").build(),
                                JooqFieldElement.of(VALIDATION_TEST.IMAGE_FIELD, "validation.fields.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("field-validation-test", JooqListRoute.builder()
                .dataStoreKey(VALIDATION_TEST)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(VALIDATION_TEST.REQUIRED_FIELD)
                        .children(List.of(
                                JooqFieldElement.of(VALIDATION_TEST.REQUIRED_FIELD, "route.projects.labels.name").build(),
                                JooqFieldElement.of(VALIDATION_TEST.EMAIL_FIELD, "route.projects.labels.description").build()
                        ))
                        .build())
                .child(validationForm)
                .build());

        LinkedHashMap<JooqFieldValidationTestEnum, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put(OPTION1, "enums.option1");
        enumOptions.put(OPTION2, "enums.option2");
        enumOptions.put(OPTION3, "enums.option3");

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}
