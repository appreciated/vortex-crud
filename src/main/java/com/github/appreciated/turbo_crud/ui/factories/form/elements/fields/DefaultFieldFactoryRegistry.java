package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.config.model.Application;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
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

    public DefaultFieldFactoryRegistry(TurboCrudConfigService configService, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry, TurboCrudFileProviderRegistry fileProviderRegistry) {
        Application configuration = configService.getConfiguration();
        factories.put(TCTextFieldFactory.class, new TCTextFieldFactory());
        factories.put(TCTextAreaFieldFactory.class, new TCTextAreaFieldFactory());
        factories.put(TCDateFieldFactory.class, new TCDateFieldFactory());
        factories.put(TCDateTimePickerFactory.class, new TCDateTimePickerFactory());
        factories.put(TCSelectFieldFactory.class, new TCSelectFieldFactory(configuration.getSelects(), configuration.getRepositories()));
        factories.put(TCNumberFieldFactory.class, new TCNumberFieldFactory());
        factories.put(TCReferenceFieldFactory.class, new TCReferenceFieldFactory(entityManagerFactoryRegistry));
        factories.put(TCImageFieldFactory.class, new TCImageFieldFactory(fileProviderRegistry));
        factories.put(TCCheckboxFieldFactory.class, new TCCheckboxFieldFactory());
        factories.put(TCIdFieldFactory.class, new TCIdFieldFactory());
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