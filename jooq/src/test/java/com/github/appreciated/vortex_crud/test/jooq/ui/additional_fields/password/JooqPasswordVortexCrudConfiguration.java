package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields.password;

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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.PASSWORD_TEST;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JooqPasswordVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> cachedApplication;

    public JooqPasswordVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        if (cachedApplication != null) {
            return cachedApplication;
        }

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("password-test", createPasswordTestRoute());

        cachedApplication = JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();

        return cachedApplication;
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
}
