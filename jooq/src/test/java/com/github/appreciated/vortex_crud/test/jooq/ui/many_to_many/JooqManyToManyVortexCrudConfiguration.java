package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
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

import static com.github.appreciated.vortex_crud.jooq.models.tables.ManyToManyItem.MANY_TO_MANY_ITEM;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqManyToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> itemForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(MANY_TO_MANY_ITEM)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(MANY_TO_MANY_ITEM.NAME)
                        .withChildren(
                                new JooqFieldElement(MANY_TO_MANY_ITEM.NAME, "relations.labels.name"),
                                JooqCollectionElement.of("relations.labels.related")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JooqCollection.of(ConnectDialogFactory.class)
                                                .withData(JooqCollectionConfiguration.of(MANY_TO_MANY_ITEM)
                                                        .withManyToMany(new JooqManyToMany(MANY_TO_MANY_ITEM, ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION))
                                                        .withChildren("name")
                                                        .build())
                                                .withEmptyMessage("relations.related.empty")
                                                .withConfiguration(new com.github.appreciated.vortex_crud.core.config.model.CollectionConfig("name"))
                                                .build())
                                        .build()
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(MANY_TO_MANY_ITEM)
                .withIconFactory(FACTORY::create)
                .withTitle("relations.tests.many-to-many.title")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField(MANY_TO_MANY_ITEM.NAME)
                        .withChildren(
                                new JooqFieldElement(MANY_TO_MANY_ITEM.NAME, "relations.labels.name")
                        )
                        .build())
                .withChild(itemForm)
                .build());

        return JooqApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .build();
    }
}
