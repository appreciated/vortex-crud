package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields.textarea;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.TEXTAREA_TEST;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JooqTextAreaVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> cachedApplication;

    public JooqTextAreaVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        if (cachedApplication != null) {
            return cachedApplication;
        }

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("textarea-test", createTextAreaTestRoute());

        cachedApplication = JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();

        return cachedApplication;
    }

    private RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> createTextAreaTestRoute() {
        JooqDataStore store = new JooqDataStore(TEXTAREA_TEST.getRecordType(), dsl);
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
                .fields(List.of(
                        JooqFieldElement.of(TEXTAREA_TEST.NAME, "textarea-test.labels.name").build(),
                        JooqFieldElement.of(TEXTAREA_TEST.DESCRIPTION, "textarea-test.labels.content").build()
                ))
                .build();

        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.textarea-test.title-list")
                .filterField(TEXTAREA_TEST.NAME)
                .columns(List.of(
                        JooqFieldElement.of(TEXTAREA_TEST.NAME, "textarea-test.labels.name").build(),
                        JooqFieldElement.of(TEXTAREA_TEST.DESCRIPTION, "textarea-test.labels.content").build()
                ))
                .form(textAreaForm)
                .build();
    }
}
