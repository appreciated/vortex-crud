package com.github.appreciated.vortex_crud.demo.projectmanagement.ui.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.demo.projectmanagement.service.CustomFieldService;
import com.vaadin.flow.component.Component;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.Collection;
import java.util.List;

public class JsonCustomFieldFactory implements VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final CustomFieldService service;
    private final String entityType;

    public JsonCustomFieldFactory(CustomFieldService service, String entityType) {
        this.service = service;
        this.entityType = entityType;
    }

    @Override
    public Component createComponent(TableImpl<?> table, TableField<?, ?> field, Field<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dataStoreField, VortexCrudContext<TableRecord<?>, TableField<?, ?>, TableImpl<?>> context) {
        return new DynamicCustomFields(service, entityType);
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "TEXT");
    }
}
