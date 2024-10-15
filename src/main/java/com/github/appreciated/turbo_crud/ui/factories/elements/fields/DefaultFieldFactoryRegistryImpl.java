package com.github.appreciated.turbo_crud.ui.factories.elements.fields;

import com.github.appreciated.turbo_crud.config.model.ApplicationConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.elements.fields.functions.*;
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
public class DefaultFieldFactoryRegistryImpl implements TurboCrudFieldFactoryRegistry {

    private final Map<String, TurboCrudFieldFactory> factories = new HashMap<>();

    public DefaultFieldFactoryRegistryImpl(TurboCrudConfigService configService, TurboCrudEntityManagerService entityManagerService) {
        ApplicationConfig configuration = configService.getConfiguration();
        factories.put("text", new DefaultTextFieldFactory());
        factories.put("textarea", new DefaultTextAreaFactory());
        factories.put("date", new DefaultDatePickerFactory());
        factories.put("select", new DefaultComboBoxFactory(configuration.getSelects(), configuration.getTablesConfig()));
        factories.put("dropdown", new DefaultComboBoxFactory(configuration.getSelects(), configuration.getTablesConfig()));
        factories.put("reference", new DefaultReferenceFieldFactory(entityManagerService));
    }

    public Map<String, TurboCrudFieldFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudFieldFactory getFactory(String type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory with key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), type)));
    }

    @Override
    public void addFactory(String key, TurboCrudFieldFactory factory) {
        factories.put(key, factory);
    }
}