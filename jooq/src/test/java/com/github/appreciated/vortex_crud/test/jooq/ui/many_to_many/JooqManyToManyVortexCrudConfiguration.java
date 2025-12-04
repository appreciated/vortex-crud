package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.models.tables.ManyToManyItemRelation;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqIdField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.ManyToManyItem.MANY_TO_MANY_ITEM;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqManyToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqManyToManyVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(MANY_TO_MANY_ITEM.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(MANY_TO_MANY_ITEM)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                MANY_TO_MANY_ITEM.ID, JooqIdField.builder().build(),
                                MANY_TO_MANY_ITEM.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build())
                        ).build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> itemForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(MANY_TO_MANY_ITEM.NAME)
                        .children(List.of(
                                JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.labels.name").build(),
                                JooqCollectionElement.of("relations.labels.related").factory(new ListCollectionFactory())
                                        .configuration(JooqCollection.builder(new ConnectDialogFactory())
                                                .data(JooqCollectionConfiguration.of(config)
                                                        .manyToMany(new JooqManyToMany(
                                                                ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION.ITEM_ID,
                                                                ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION.RELATED_ITEM_ID,
                                                                MANY_TO_MANY_ITEM.ID,
                                                                ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION))
                                                        .children(List.of(MANY_TO_MANY_ITEM.NAME))
                                                        .build())
                                                .emptyMessage("relations.related.empty")
                                                .configuration(new com.github.appreciated.vortex_crud.core.config.model.CollectionConfig(MANY_TO_MANY_ITEM.NAME))
                                                .build())
                                        .build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JooqListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("relations.tests.many-to-many.title")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(MANY_TO_MANY_ITEM.NAME)
                        .children(List.of(
                                JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.labels.name").build()
                        ))
                        .build())
                .child(itemForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
