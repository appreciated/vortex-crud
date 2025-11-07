package com.github.appreciated.vortex_crud.test.jooq.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                ONE_TO_MANY_PARENT, JooqDataStoreConfig.of(ONE_TO_MANY_PARENT)
                        .fields(Map.of(
                                ONE_TO_MANY_PARENT.ID, IdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                ONE_TO_MANY_PARENT.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        )).build(),
                ONE_TO_MANY_CHILD, JooqDataStoreConfig.of(ONE_TO_MANY_CHILD)
                        .fields(Map.of(
                                ONE_TO_MANY_CHILD.ID, IdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                ONE_TO_MANY_CHILD.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        )).build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> childForm = JooqFormRoute.builder()
                .dataStoreKey(ONE_TO_MANY_CHILD)
                .configuration(JooqFormRendererConfiguration.builder()
                        .titleField(ONE_TO_MANY_CHILD.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ONE_TO_MANY_CHILD.NAME, "relations.labels.name").build()
                        ))
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> parentForm = JooqFormRoute.builder()
                .dataStoreKey(ONE_TO_MANY_PARENT)
                .configuration(JooqFormRendererConfiguration.builder()
                        .titleField(ONE_TO_MANY_PARENT.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ONE_TO_MANY_PARENT.NAME, "relations.labels.name").build(),
                                JooqCollectionElement.of("relations.labels.children")
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class)FormDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(ONE_TO_MANY_CHILD)
                                                        .oneToMany(new JooqOneToMany(ONE_TO_MANY_CHILD.PARENT_ID))
                                                        .children(List.of(ONE_TO_MANY_CHILD.NAME))
                                                        .build())
                                                .emptyMessage("relations.children.empty")
                                                .child(childForm)
                                                .build()
                                        )
                                        .build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("one-to-many-test", JooqListRoute.builder()
                .dataStoreKey(ONE_TO_MANY_PARENT)
                .iconFactory(FACTORY::create)
                .title("relations.tests.one-to-many.title")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(ONE_TO_MANY_PARENT.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ONE_TO_MANY_PARENT.NAME, "relations.labels.name").build()
                        ))
                        .build())
                .child(parentForm)
                .build());

        return JooqApplication.builder()
                .dataStores(dataStores)
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
