package com.github.appreciated.vortex_crud.demo.devplatform.factory;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.demo.devplatform.component.StarButton;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.vaadin.flow.component.Component;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.Collection;
import java.util.List;

public class StarFieldFactory implements VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final JooqDataStore starStore;
    private final JooqDataStore userStore;

    public StarFieldFactory(JooqDataStore starStore, JooqDataStore userStore) {
        this.starStore = starStore;
        this.userStore = userStore;
    }

    @Override
    public Component createComponent(TableImpl<?> table, TableField<?, ?> field, Field<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dataStoreField, VortexCrudContext<TableRecord<?>, TableField<?, ?>, TableImpl<?>> context) {
        return new StarButton(starStore, userStore);
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of();
    }
}
