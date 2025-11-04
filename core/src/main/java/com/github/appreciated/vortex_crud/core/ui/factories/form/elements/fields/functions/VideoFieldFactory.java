package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.VideoField;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.VideoHasValue;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class VideoFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudFileProviderRegistry fileProviderRegistry;

    public VideoFieldFactory(VortexCrudFileProviderRegistry fileProviderRegistry) {
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField) {
        VideoField<ModelClass, FieldType, RepositoryType> videoField = (VideoField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        return new VideoHasValue(fileProviderRegistry.getFactory(videoField.configuration().resourceProvider()));
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB");
    }
}