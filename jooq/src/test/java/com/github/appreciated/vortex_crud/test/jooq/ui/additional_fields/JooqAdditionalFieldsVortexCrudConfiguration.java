package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.*;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JooqAdditionalFieldsVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqAdditionalFieldsVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Determine which table to use by checking which one exists and has data
        TableImpl<?> activeTable = findActiveTable();

        JooqDataStore store = new JooqDataStore(activeTable.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(activeTable)
                        .dataStoreInstance(store)
                        .fields(Map.of(
                                (TableField<?, ?>) activeTable.field("id"), JooqNumericIdField.builder().build(),
                                (TableField<?, ?>) activeTable.field("name"), JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Invalid length", 0, 255))).build(),
                                (TableField<?, ?>) activeTable.field("description"), JooqTextAreaField.builder().build(),
                                (TableField<?, ?>) activeTable.field("password"), JooqPasswordField.builder().build(),
                                (TableField<?, ?>) activeTable.field("price"), JooqBigDecimalField.builder().build(),
                                (TableField<?, ?>) activeTable.field("video_url"), JooqVideoField.builder().configuration(VideoFieldRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(new LocalVideoResourceProvider())
                                        .build()).build()
                        )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> additionalFieldsForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.additional-fields.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField((TableField<?, ?>) activeTable.field("name"))
                        .children(List.of(
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("name"), "additional-fields.labels.name").build(),
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("description"), "additional-fields.labels.description").build(),
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("password"), "additional-fields.labels.password").build(),
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("price"), "additional-fields.labels.price").build(),
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("video_url"), "additional-fields.labels.video").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("additional-fields-test", JooqListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.additional-fields.title-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField((TableField<?, ?>) activeTable.field("name"))
                        .children(List.of(
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("name"), "additional-fields.labels.name").build(),
                                JooqFieldElement.of((TableField<?, ?>) activeTable.field("description"), "additional-fields.labels.description").build()
                        ))
                        .build())
                .child(additionalFieldsForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }

    private TableImpl<?> findActiveTable() {
        // Check each possible test table and return the first one that has data
        TableImpl<?>[] tables = {LIFECYLE_TEST, PASSWORD_TEST, TEXTAREA_TEST, ADDITIONAL_FIELDS_TEST};

        for (TableImpl<?> table : tables) {
            try {
                int count = dsl.selectCount().from(table).fetchOne(0, int.class);
                if (count > 0) {
                    return table;
                }
            } catch (Exception e) {
                // Table doesn't exist, continue to next
            }
        }

        // Default to ADDITIONAL_FIELDS_TEST if no table has data yet
        return ADDITIONAL_FIELDS_TEST;
    }
}
