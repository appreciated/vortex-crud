package com.github.appreciated.vortex_crud.test.jooq.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.OneToManyChild.ONE_TO_MANY_CHILD;
import static com.github.appreciated.vortex_crud.jooq.models.tables.OneToManyParent.ONE_TO_MANY_PARENT;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqOneToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                ONE_TO_MANY_PARENT, JooqDataStoreConfig.of(ONE_TO_MANY_PARENT)
                        .withFields(Map.of(
                                ONE_TO_MANY_PARENT.ID, new IdField<>(),
                                ONE_TO_MANY_PARENT.NAME, new TextField<>()
                        )).build(),
                ONE_TO_MANY_CHILD, JooqDataStoreConfig.of(ONE_TO_MANY_CHILD)
                        .withFields(Map.of(
                                ONE_TO_MANY_CHILD.ID, new IdField<>(),
                                ONE_TO_MANY_CHILD.NAME, new TextField<>()
                        )).build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> childForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(ONE_TO_MANY_CHILD)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(ONE_TO_MANY_CHILD.NAME)
                        .withChildren(
                                new JooqFieldElement(ONE_TO_MANY_CHILD.NAME, "relations.labels.name")
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> parentForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(ONE_TO_MANY_PARENT)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(ONE_TO_MANY_PARENT.NAME)
                        .withChildren(
                                new JooqFieldElement(ONE_TO_MANY_PARENT.NAME, "relations.labels.name"),
                                JooqCollectionElement.of("relations.labels.children")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JooqCollection.of(FormDialogFactory.class)
                                                .withData(JooqCollectionConfiguration.of(ONE_TO_MANY_CHILD)
                                                        .withOneToMany(new JooqOneToMany(ONE_TO_MANY_CHILD.PARENT_ID))
                                                        .withChildren(ONE_TO_MANY_CHILD.NAME)
                                                        .build())
                                                .withEmptyMessage("relations.children.empty")
                                                .withChild(childForm)
                                                .build()
                                        )
                                        .build())
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("one-to-many-test", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(ONE_TO_MANY_PARENT)
                .withIconFactory(FACTORY::create)
                .withTitle("relations.tests.one-to-many.title")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField(ONE_TO_MANY_PARENT.NAME)
                        .withChildren(
                                new JooqFieldElement(ONE_TO_MANY_PARENT.NAME, "relations.labels.name")
                        )
                        .build())
                .withChild(parentForm)
                .build());

        return JooqApplication.builder()
                .withDataStores(dataStores)
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .build();
    }
}