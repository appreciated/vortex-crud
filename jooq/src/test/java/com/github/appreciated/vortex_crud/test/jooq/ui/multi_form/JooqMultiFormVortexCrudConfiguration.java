package com.github.appreciated.vortex_crud.test.jooq.ui.multi_form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.MULTI_FORM_TEST;
import static com.vaadin.flow.component.icon.VaadinIcon.USER;

@Service
public class JooqMultiFormVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                MULTI_FORM_TEST, JooqDataStoreConfig.of(MULTI_FORM_TEST)
                        .fields(Map.of(
                                MULTI_FORM_TEST.ID, JooqIdField.builder().build(),
                                MULTI_FORM_TEST.PROFILE_NAME, JooqTextField.builder()
                                        .required(true)
                                        .validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255)))
                                        .build(),
                                MULTI_FORM_TEST.EMAIL, JooqEmailField.builder()
                                        .required(true)
                                        .validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255)))
                                        .build(),
                                MULTI_FORM_TEST.DESCRIPTION, JooqTextAreaField.builder().build(),
                                MULTI_FORM_TEST.AGE, JooqIntegerField.builder().build()
                        )).build()
        );

        // Complete form configuration with all fields for dialog-based create/edit
        FormRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> completeFormConfig =
                JooqFormRendererConfiguration.builder()
                        .titleField(MULTI_FORM_TEST.PROFILE_NAME)
                        .children(List.of(
                                JooqFieldElement.of(MULTI_FORM_TEST.PROFILE_NAME, "multi_form.fields.profile_name").build(),
                                JooqFieldElement.of(MULTI_FORM_TEST.EMAIL, "multi_form.fields.email").build(),
                                JooqFieldElement.of(MULTI_FORM_TEST.DESCRIPTION, "multi_form.fields.description").build(),
                                JooqFieldElement.of(MULTI_FORM_TEST.AGE, "multi_form.fields.age").build()
                        ))
                        .build();

        // Individual form configurations for multi-form rendering
        FormRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> basicInfoForm =
                JooqFormRendererConfiguration.builder()
                        .titleField(MULTI_FORM_TEST.PROFILE_NAME)
                        .children(List.of(
                                JooqFieldElement.of(MULTI_FORM_TEST.PROFILE_NAME, "multi_form.fields.profile_name").build(),
                                JooqFieldElement.of(MULTI_FORM_TEST.EMAIL, "multi_form.fields.email").build()
                        ))
                        .build();

        FormRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> additionalDetailsForm =
                JooqFormRendererConfiguration.builder()
                        .titleField(MULTI_FORM_TEST.DESCRIPTION)
                        .children(List.of(
                                JooqFieldElement.of(MULTI_FORM_TEST.DESCRIPTION, "multi_form.fields.description").build(),
                                JooqFieldElement.of(MULTI_FORM_TEST.AGE, "multi_form.fields.age").build()
                        ))
                        .build();

        // Create MultiFormRoute with both forms
        MultiFormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> multiFormRoute =
                MultiFormRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreKey(MULTI_FORM_TEST)
                        .title("route.multi_form.title")
                        .configuration(MultiFormRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .forms(List.of(basicInfoForm, additionalDetailsForm))
                                .build())
                        .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        // Use the complete form config for the list (for dialog create/edit)
        routes.put("multi-form-test", JooqListRoute.builder()
                .dataStoreKey(MULTI_FORM_TEST)
                .iconFactory(USER::create)
                .title("route.multi_form.title")
                .configuration(completeFormConfig)
                .child(multiFormRoute)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
