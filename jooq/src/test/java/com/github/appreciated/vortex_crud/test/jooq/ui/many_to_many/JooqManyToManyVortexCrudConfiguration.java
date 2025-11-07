package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.models.tables.ManyToManyItemRelation;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqIdField;
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

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                MANY_TO_MANY_ITEM, JooqDataStoreConfig.of(MANY_TO_MANY_ITEM)
                        .fields(Map.of(
                                MANY_TO_MANY_ITEM.ID, JooqIdField.builder().build(),
                                MANY_TO_MANY_ITEM.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build())
                        ).build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> itemForm = JooqFormRoute.builder()
                .dataStoreKey(MANY_TO_MANY_ITEM)
                .configuration(JooqFormRendererConfiguration.builder()
                        .titleField(MANY_TO_MANY_ITEM.NAME)
                        .children(List.of(
                                JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.labels.name").build(),
                                JooqCollectionElement.of("relations.labels.related").factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(MANY_TO_MANY_ITEM)
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
                .dataStoreKey(MANY_TO_MANY_ITEM)
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
                .dataStores(dataStores)
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
