package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jooq.models.tables.ManyToManyItemRelation;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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
                                MANY_TO_MANY_ITEM.ID, new IdField<>(),
                                MANY_TO_MANY_ITEM.NAME, new TextField<>())
                        ).build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> itemForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(MANY_TO_MANY_ITEM)
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(MANY_TO_MANY_ITEM.NAME)
                        .children(List.of(
                                JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.labels.name").build(),
                                JooqCollectionElement.of("relations.labels.related")
                                        .configuration(JooqCollection.of(ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(MANY_TO_MANY_ITEM)
                                                        .manyToMany(new JooqManyToMany(
                                                                ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION.ITEM_ID,
                                                                ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION.RELATED_ITEM_ID,
                                                                MANY_TO_MANY_ITEM.ID,
                                                                ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION))
                                                        .children(List.of(MANY_TO_MANY_ITEM.NAME))
                                                        .build())
                                                .emptyMessage("relations.related.empty")
                                                .config(new com.github.appreciated.vortex_crud.core.config.model.CollectionConfig<org.jooq.TableField<?, ?>>(MANY_TO_MANY_ITEM.NAME))
                                                .build())
                                        .build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JooqRouteRenderer.of(ListRouteFactory.class)
                .dataStoreKey(MANY_TO_MANY_ITEM)
                .iconFactory(FACTORY::create)
                .title("relations.tests.many-to-many.title")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .filterField(MANY_TO_MANY_ITEM.NAME)
                        .children(List.of(
                                JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.labels.name").build()
                        ))
                        .build())
                .childrenMap(Map.of("form", itemForm))
                .build());

        return JooqApplication.builder()
                .dataStores(dataStores)
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
