package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component.ImageHasValue;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class TCImageFieldFactory implements TurboCrudFieldFactory {

    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public TCImageFieldFactory(TurboCrudFileProviderRegistry fileProviderRegistry) {
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component createComponent(String table, String field, Field repositoryField) {
        return new ImageHasValue(fileProviderRegistry.getFactory((Class<? extends TurboCrudFileProvider>) repositoryField.getConfiguration().getFactory()));
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
       return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB");
    }

}
