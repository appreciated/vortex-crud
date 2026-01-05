package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields.lifecycle;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.LifecycleTestRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.LIFECYCLE_TEST;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JooqLifecycleVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> cachedApplication;

    public JooqLifecycleVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        if (cachedApplication != null) {
            return cachedApplication;
        }

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("lifecycle-test", createLifecycleTestRoute());

        cachedApplication = JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();

        return cachedApplication;
    }

    private RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> createLifecycleTestRoute() {
        JooqDataStore<LifecycleTestRecord> store = new JooqDataStore<>(LIFECYCLE_TEST.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(LIFECYCLE_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        LIFECYCLE_TEST.ID, JooqNumericIdField.builder().build(),
                        LIFECYCLE_TEST.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Invalid length", 0, 255))).build(),
                        LIFECYCLE_TEST.DESCRIPTION, JooqTextAreaField.builder().build()
                )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> lifecycleForm = JooqFormRoute.builder()
                .titleField(LIFECYCLE_TEST.NAME)
                .fields(List.of(
                        JooqFormElement.of(LIFECYCLE_TEST.NAME, "lifecycle-test.labels.name").build(),
                        JooqFormElement.of(LIFECYCLE_TEST.DESCRIPTION, "lifecycle-test.labels.description").build()
                ))
                .build();

        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.lifecycle-test.title-list")
                .filterField(LIFECYCLE_TEST.NAME)
                .columns(List.of(
                        JooqFormElement.of(LIFECYCLE_TEST.NAME, "lifecycle-test.labels.name").build(),
                        JooqFormElement.of(LIFECYCLE_TEST.DESCRIPTION, "lifecycle-test.labels.description").build()
                ))
                .form(lifecycleForm)
                .build();
    }
}
