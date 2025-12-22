package com.github.appreciated.vortex_crud.test.jooq.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.OneToManyChild.ONE_TO_MANY_CHILD;
import static com.github.appreciated.vortex_crud.jooq.models.tables.OneToManyParent.ONE_TO_MANY_PARENT;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqOneToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqOneToManyVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore parentStore = new JooqDataStore(ONE_TO_MANY_PARENT.getRecordType(), dsl, new DataStoreHooks<>());
        var parentConfig = JooqDataStoreConfig.of(ONE_TO_MANY_PARENT)
                        .dataStoreInstance((VortexCrudDataStore) parentStore)
                        .fields(Map.of(
                                ONE_TO_MANY_PARENT.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                ONE_TO_MANY_PARENT.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        )).build();

        JooqDataStore childStore = new JooqDataStore(ONE_TO_MANY_CHILD.getRecordType(), dsl, new DataStoreHooks<>());
        var childConfig = JooqDataStoreConfig.of(ONE_TO_MANY_CHILD)
                        .dataStoreInstance((VortexCrudDataStore) childStore)
                        .fields(Map.of(
                                ONE_TO_MANY_CHILD.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                ONE_TO_MANY_CHILD.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> childForm = JooqFormRoute.builder()
                .dataStoreConfig(childConfig)
                .titleField(ONE_TO_MANY_CHILD.NAME)
                .children(List.of(
                        JooqFieldElement.of(ONE_TO_MANY_CHILD.NAME, "relations.labels.name").build()
                ))
                .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> parentForm = JooqFormRoute.builder()
                .dataStoreConfig(parentConfig)
                .titleField(ONE_TO_MANY_PARENT.NAME)
                .children(List.of(
                        JooqFieldElement.of(ONE_TO_MANY_PARENT.NAME, "relations.labels.name").build(),
                        JooqCollectionElement.of("relations.labels.children")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(childConfig)
                                .oneToMany(new JooqOneToMany(ONE_TO_MANY_CHILD.PARENT_ID))
                                .children(List.of(ONE_TO_MANY_CHILD.NAME))
                                .emptyMessage("relations.children.empty")
                                .form(childForm)
                                .build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("one-to-many-test", JooqListRoute.builder()
                .dataStoreConfig(parentConfig)
                .iconFactory(FACTORY::create)
                .title("relations.tests.one-to-many.title")
                .filterField(ONE_TO_MANY_PARENT.NAME)
                .children(List.of(
                        JooqFieldElement.of(ONE_TO_MANY_PARENT.NAME, "relations.labels.name").build()
                ))
                .form(parentForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
