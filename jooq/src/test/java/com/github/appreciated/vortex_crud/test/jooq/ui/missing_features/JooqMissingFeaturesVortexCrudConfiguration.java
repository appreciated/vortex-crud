package com.github.appreciated.vortex_crud.test.jooq.ui.missing_features;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalFileResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.core.ui.actions.MultiEntityRouteAction;
import com.github.appreciated.vortex_crud.core.ui.actions.SingleEntityRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.*;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class JooqMissingFeaturesVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqMissingFeaturesVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Selects config for tags
        LinkedHashMap<String, String> tagOptions = new LinkedHashMap<>();
        tagOptions.put("tag1", "Tag 1");
        tagOptions.put("tag2", "Tag 2");

        Map<String, LinkedHashMap<?, String>> selectsConfig = new HashMap<>();
        selectsConfig.put("tags", tagOptions);

        Selects selects = Selects.builder()
            .configs(selectsConfig)
            .build();

        // Referenced Store
        var referencedStore = new JooqDataStore(MISSING_FEATURES_REFERENCED.getRecordType(), dsl, new DataStoreHooks<>());
        var referencedConfig = JooqDataStoreConfig.of(MISSING_FEATURES_REFERENCED)
                .dataStoreInstance(referencedStore)
                .fields(Map.of(
                        MISSING_FEATURES_REFERENCED.ID, JooqNumericIdField.builder().build(),
                        MISSING_FEATURES_REFERENCED.NAME, JooqTextField.builder().build()
                )).build();

        // Main Store
        var taskStore = new JooqDataStore(MISSING_FEATURES_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var taskConfig = JooqDataStoreConfig.of(MISSING_FEATURES_TEST)
                .dataStoreInstance(taskStore)
                .fields(Map.ofEntries(
                        Map.entry(MISSING_FEATURES_TEST.ID, JooqNumericIdField.builder().build()),
                        Map.entry(MISSING_FEATURES_TEST.NAME, JooqTextField.builder().build()),
                        Map.entry(MISSING_FEATURES_TEST.PDF_DOC, JooqFileField.builder()
                                .resourceProvider(new LocalPdfResourceProvider())
                                .build()),
                        Map.entry(MISSING_FEATURES_TEST.NOTES, JooqTextAreaField.builder().build()),
                        Map.entry(MISSING_FEATURES_TEST.MARKDOWN_CONTENT, JooqMarkDownField.builder().build()),
                        Map.entry(MISSING_FEATURES_TEST.FILE_ATTACHMENT, JooqFileField.builder()
                                .resourceProvider(new LocalFileResourceProvider())
                                .build()),
                        Map.entry(MISSING_FEATURES_TEST.PRICE, JooqBigDecimalField.builder().build()),
                        Map.entry(MISSING_FEATURES_TEST.VIDEO_URL, JooqVideoField.builder()
                                .resourceProvider(new LocalVideoResourceProvider())
                                .build()),
                        // ReferenceField
                        Map.entry(MISSING_FEATURES_TEST.REFERENCED_ID, JooqReferenceField.builder()
                                .dataStore(referencedStore)
                                .field(MISSING_FEATURES_REFERENCED.NAME)
                                .build())
                ))
                .build();

        // Form Route
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> form = JooqFormRoute.builder()
            .dataStoreConfig(taskConfig)
            .title("route.missing.title")
            .titleField(MISSING_FEATURES_TEST.NAME)
            .children(List.of(
                JooqFieldElement.of(MISSING_FEATURES_TEST.NAME, "Name").build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.PDF_DOC, "PDF").build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.NOTES, "Notes").build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.REFERENCED_ID, "Referenced").build(),
                // Multi Select (ManyToMany)
                JooqCollectionElement.of("Multi Select")
                    .factory(new ListCollectionFactory())
                    .dialogFactory(new ConnectDialogFactory())
                    .dataStoreConfig(referencedConfig)
                    .manyToMany(new JooqManyToMany<>(
                        MISSING_FEATURES_TEST_RELATIONS.TEST_ID,
                        MISSING_FEATURES_TEST_RELATIONS.REFERENCED_ID,
                        MISSING_FEATURES_REFERENCED.ID,
                        MISSING_FEATURES_TEST_RELATIONS
                    ))
                    .children(List.of(MISSING_FEATURES_REFERENCED.NAME))
                    .label(MISSING_FEATURES_REFERENCED.NAME.getName())
                    .build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.MARKDOWN_CONTENT, "Markdown").build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.FILE_ATTACHMENT, "File").build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.PRICE, "Price").build(),
                JooqFieldElement.of(MISSING_FEATURES_TEST.VIDEO_URL, "Video").build()
            ))
            .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // List Route with Global, Single, Multi and Menu Actions
        routes.put("missing-features-test-new", JooqListRoute.builder()
            .dataStoreConfig(taskConfig)
            .iconFactory(COG::create)
            .title("route.missing.list")
            .filterField(MISSING_FEATURES_TEST.NAME)
            .children(List.of(
                  JooqFieldElement.of(MISSING_FEATURES_TEST.NAME, "Name").build()
            ))
            .routeActions(List.of(
                 GlobalRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                    .componentFactory(() -> new Button("Print", PRINT.create()))
                    .handler(ctx -> Notification.show("Global Action Executed"))
                    .build(),
                 SingleEntityRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                    .componentFactory(() -> new Button("Single", PENCIL.create()))
                    .handler(ctx -> Notification.show("Single Action Executed"))
                    .build(),
                 MultiEntityRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                    .componentFactory(() -> new Button("Multi", TRASH.create()))
                    .handler(ctx -> Notification.show("Multi Action Executed"))
                    .build()
            ))
            .menuActions(List.of(
                DataStoreDropdownMenuAction.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                    .dataStoreConfig(referencedConfig)
                    .label("Referenced Filter")
                    .labelField(MISSING_FEATURES_REFERENCED.NAME)
                    .build()
            ))
            .form(form)
            .build());

        // Single Form Route
        routes.put("single-form-test", JooqSingleFormRoute.builder()
             .dataStoreConfig(taskConfig)
             .title("Single Form")
             .entityFilterField(MISSING_FEATURES_TEST.ID)
             .entityFilterValueProvider(() -> 1)
             .titleField(MISSING_FEATURES_TEST.NAME)
             .children(List.of(
                 JooqFieldElement.of(MISSING_FEATURES_TEST.NAME, "Name").build()
             ))
             .build());

        return JooqApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
