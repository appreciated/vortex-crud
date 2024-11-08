package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.ImageFieldConfiguration;
import com.github.appreciated.turbo_crud.config.model.RepositoryField;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component.ImageHasValue;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;

public class DefaultImageFieldFactory implements TurboCrudFieldFactory {

    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public DefaultImageFieldFactory(TurboCrudFileProviderRegistry fileProviderRegistry) {
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component createComponent(String table, String field, RepositoryField repositoryField) {
        ImageFieldConfiguration imageFieldConfiguration = ConfigBeanFactory.create(repositoryField.getConfiguration(), ImageFieldConfiguration.class);
        return new ImageHasValue(fileProviderRegistry.getFactory(imageFieldConfiguration.getFactory()));
    }

}
