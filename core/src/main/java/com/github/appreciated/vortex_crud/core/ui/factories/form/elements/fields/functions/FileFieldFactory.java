package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.FileField;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.FileHasValue;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class FileFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudFileProviderRegistry fileProviderRegistry;

    public FileFieldFactory(VortexCrudFileProviderRegistry fileProviderRegistry) {
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField) {
        FileField<ModelClass, FieldType, RepositoryType> fileField = (FileField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        return new FileHasValue(fileProviderRegistry.getFactory(fileField.configuration().resourceProvider()));
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB");
    }

}
