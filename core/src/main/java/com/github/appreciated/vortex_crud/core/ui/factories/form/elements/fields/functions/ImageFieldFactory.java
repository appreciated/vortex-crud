package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.annotation.NoCoverage;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.ImageHasValue;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class ImageFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<? extends ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<? super ModelClass, FieldType, RepositoryType> context) {
        ImageField<? extends ModelClass, FieldType, RepositoryType> imageField = (ImageField<? extends ModelClass, FieldType, RepositoryType>) dataStoreField;
        return new ImageHasValue(imageField.resourceProvider());
    }

    @NoCoverage
    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB");
    }
}
