package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.annotation.NoCoverage;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;

import java.util.*;

public class SelectFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<? extends ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<? super ModelClass, FieldType, RepositoryType> context) {
        Selects selects = context.configService().configuration().selects();
        Select<?> select = new Select<>();

        SelectField<? extends ModelClass, FieldType, RepositoryType> sf = (SelectField<? extends ModelClass, FieldType, RepositoryType>) dataStoreField;
        String selectName = sf.values();
        Map<?, String> selectConfig = selects.configs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<?> keySet = selectConfig.keySet();
        select.setItems(new ArrayList(keySet));
        select.setItemLabelGenerator(item -> select.getTranslation(selectConfig.get(item)));

        return select;
    }

    @NoCoverage
    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING");
    }
}
