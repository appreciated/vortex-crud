package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqPasswordField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextAreaField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.*;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JooqAdditionalFieldsVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> cachedApplication;

    public JooqAdditionalFieldsVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        if (cachedApplication != null) {
            return cachedApplication;
        }

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("lifecycle-test", createLifecycleTestRoute());
        routes.put("password-test", createPasswordTestRoute());
        routes.put("textarea-test", createTextAreaTestRoute());

        cachedApplication = JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();

        return cachedApplication;
    }

    private RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> createLifecycleTestRoute() {
        JooqDataStore store = new JooqDataStore(LIFECYLE_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(LIFECYLE_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        LIFECYLE_TEST.ID, JooqNumericIdField.builder().build(),
                        LIFECYLE_TEST.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Invalid length", 0, 255))).build(),
                        LIFECYLE_TEST.DESCRIPTION, JooqTextAreaField.builder().build()
                )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> lifecycleForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.lifecycle-test.title")
                .titleField(LIFECYLE_TEST.NAME)
                .children(List.of(
                        JooqFieldElement.of(LIFECYLE_TEST.NAME, "lifecycle-test.labels.name").build(),
                        JooqFieldElement.of(LIFECYLE_TEST.DESCRIPTION, "lifecycle-test.labels.description").build()
                ))
                .build();

        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.lifecycle-test.title-list")
                .filterField(LIFECYLE_TEST.NAME)
                .children(List.of(
                        JooqFieldElement.of(LIFECYLE_TEST.NAME, "lifecycle-test.labels.name").build(),
                        JooqFieldElement.of(LIFECYLE_TEST.DESCRIPTION, "lifecycle-test.labels.description").build()
                ))
                .form(lifecycleForm)
                .build();
    }

    private RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> createPasswordTestRoute() {
        JooqDataStore store = new JooqDataStore(PASSWORD_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(PASSWORD_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        PASSWORD_TEST.ID, JooqNumericIdField.builder().build(),
                        PASSWORD_TEST.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Invalid length", 0, 255))).build(),
                        PASSWORD_TEST.PASSWORD, JooqPasswordField.builder().build()
                )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> passwordForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.password-test.title")
                .titleField(PASSWORD_TEST.NAME)
                .children(List.of(
                        JooqFieldElement.of(PASSWORD_TEST.NAME, "password-test.labels.name").build(),
                        JooqFieldElement.of(PASSWORD_TEST.PASSWORD, "password-test.labels.password").build()
                ))
                .build();

        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.password-test.title-list")
                .filterField(PASSWORD_TEST.NAME)
                .children(List.of(
                        JooqFieldElement.of(PASSWORD_TEST.NAME, "password-test.labels.name").build()
                ))
                .form(passwordForm)
                .build();
    }

    private RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> createTextAreaTestRoute() {
        JooqDataStore store = new JooqDataStore(TEXTAREA_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(TEXTAREA_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        TEXTAREA_TEST.ID, JooqNumericIdField.builder().build(),
                        TEXTAREA_TEST.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Invalid length", 0, 255))).build(),
                        TEXTAREA_TEST.DESCRIPTION, JooqTextAreaField.builder().build()
                )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> textAreaForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.textarea-test.title")
                .titleField(TEXTAREA_TEST.NAME)
                .children(List.of(
                        JooqFieldElement.of(TEXTAREA_TEST.NAME, "textarea-test.labels.name").build(),
                        JooqFieldElement.of(TEXTAREA_TEST.DESCRIPTION, "textarea-test.labels.content").build()
                ))
                .build();

        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.textarea-test.title-list")
                .filterField(TEXTAREA_TEST.NAME)
                .children(List.of(
                        JooqFieldElement.of(TEXTAREA_TEST.NAME, "textarea-test.labels.name").build(),
                        JooqFieldElement.of(TEXTAREA_TEST.DESCRIPTION, "textarea-test.labels.content").build()
                ))
                .form(textAreaForm)
                .build();
    }
}
