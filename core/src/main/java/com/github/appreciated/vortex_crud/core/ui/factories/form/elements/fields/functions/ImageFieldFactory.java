package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.ImageHasValue;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class ImageFieldFactory<DataStoreId, FieldId, ModelClass>  implements VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>  {

    private final VortexCrudFileProviderRegistry fileProviderRegistry;

    public ImageFieldFactory(VortexCrudFileProviderRegistry fileProviderRegistry) {
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId, ModelClass>  dataStoreField) {
        return new ImageHasValue(fileProviderRegistry.getFactory(dataStoreField.getConfiguration().getImageFactory()));
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB");
    }

}
