package com.github.appreciated.vortex_crud.test.jooq.ui.multi_form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
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

    private final DSLContext dsl;

    public JooqMultiFormVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(MULTI_FORM_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(MULTI_FORM_TEST)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                MULTI_FORM_TEST.ID, JooqNumericIdField.builder().build(),
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
                        )).build();

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
                        .dataStoreConfig(config)
                        .title("route.multi_form.title")
                        .configuration(MultiFormRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .forms(List.of(basicInfoForm, additionalDetailsForm))
                                .build())
                        .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        // Use the complete form config for the list (for dialog create/edit)
        routes.put("multi-form-test", JooqListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(USER::create)
                .title("route.multi_form.title")
                .configuration(ListItemRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .filterField(MULTI_FORM_TEST.PROFILE_NAME)
                        .children(List.of(
                                JooqFieldElement.of(MULTI_FORM_TEST.PROFILE_NAME, "relations.labels.name").build()
                        ))
                        .build())
                .child(multiFormRoute)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
