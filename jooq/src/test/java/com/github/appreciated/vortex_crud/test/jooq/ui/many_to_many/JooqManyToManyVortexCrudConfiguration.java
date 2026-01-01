package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.models.tables.ManyToManyItemRelation;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.ManyToManyItemRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.CALENDAR_EVENTS;
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
        JooqDataStore<ManyToManyItemRecord> store = new JooqDataStore<>(MANY_TO_MANY_ITEM.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(MANY_TO_MANY_ITEM)
                        .dataStoreInstance(store)
                        .fields(Map.of(
                                MANY_TO_MANY_ITEM.ID, JooqNumericIdField.builder().build(),
                                MANY_TO_MANY_ITEM.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build())
                        ).build();

        JooqFormRoute itemForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .titleField(MANY_TO_MANY_ITEM.NAME)
                .fields(List.of(
                        JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.labels.name").build(),
                        JooqCollection.builder()
                                .field(MANY_TO_MANY_ITEM.NAME)
                                .label("relations.labels.related")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
                                .dataStoreConfig(config)
                                .manyToMany(new JooqManyToMany<>(
                                        ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION.ITEM_ID,
                                        ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION.RELATED_ITEM_ID,
                                        MANY_TO_MANY_ITEM.ID,
                                        ManyToManyItemRelation.MANY_TO_MANY_ITEM_RELATION))
                                .children(List.of(MANY_TO_MANY_ITEM.NAME))
                                .emptyMessage("relations.related.empty")
                                .build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JooqListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("relations.tests.many-to-many.title")
                .filterField(MANY_TO_MANY_ITEM.NAME)
                .columns(List.of( JooqFieldElement.of(MANY_TO_MANY_ITEM.NAME, "relations.tests.many-to-many.title").build()))
                .form(itemForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
