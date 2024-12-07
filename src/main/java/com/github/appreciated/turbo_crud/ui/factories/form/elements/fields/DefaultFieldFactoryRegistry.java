package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.config.model.Application;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the TurboCrudComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultFieldFactoryRegistry implements TurboCrudFieldFactoryRegistry {

    private final Map<Class<? extends TurboCrudFieldFactory>, TurboCrudFieldFactory> factories = new HashMap<>();

    public DefaultFieldFactoryRegistry(TurboCrudConfigService configService, TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry, TurboCrudFileProviderRegistry fileProviderRegistry) {
        Application configuration = configService.getConfiguration();
        factories.put(TextFieldFactory.class, new TextFieldFactory());
        factories.put(TextAreaFieldFactory.class, new TextAreaFieldFactory());
        factories.put(DateFieldFactory.class, new DateFieldFactory());
        factories.put(DateTimePickerFactory.class, new DateTimePickerFactory());
        factories.put(SelectFieldFactory.class, new SelectFieldFactory(configuration.getSelects(), configuration.getRepositories()));
        factories.put(NumberFieldFactory.class, new NumberFieldFactory());
        factories.put(ReferenceFieldFactory.class, new ReferenceFieldFactory(dataStoreFactoryRegistry));
        factories.put(ImageFieldFactory.class, new ImageFieldFactory(fileProviderRegistry));
        factories.put(TCCheckboxFieldFactory.class, new TCCheckboxFieldFactory());
        factories.put(IdFieldFactory.class, new IdFieldFactory());
    }

    public Map<Class<? extends TurboCrudFieldFactory>, TurboCrudFieldFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudFieldFactory getFactory(Class<? extends TurboCrudFieldFactory> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudFieldFactory> key, TurboCrudFieldFactory factory) {
        factories.put(key, factory);
    }
}