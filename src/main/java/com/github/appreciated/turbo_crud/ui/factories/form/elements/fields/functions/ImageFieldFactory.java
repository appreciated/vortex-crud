package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component.ImageHasValue;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class ImageFieldFactory implements TurboCrudFieldFactory {

    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public ImageFieldFactory(TurboCrudFileProviderRegistry fileProviderRegistry) {
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component createComponent(String table, String field, Field dataStoreField) {
        return new ImageHasValue(fileProviderRegistry.getFactory(dataStoreField.getConfiguration().getImageFactory()));
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
       return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB");
    }

}
