package com.github.appreciated.turbo_crud.ui.factories.fields;

import com.github.appreciated.turbo_crud.config.model.ApplicationConfig;
import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.fields.functions.DefaultComboBoxFactory;
import com.github.appreciated.turbo_crud.ui.factories.fields.functions.DefaultDatePickerFactory;
import com.github.appreciated.turbo_crud.ui.factories.fields.functions.DefaultTextAreaFactory;
import com.github.appreciated.turbo_crud.ui.factories.fields.functions.DefaultTextFieldFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the TurboCrudComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultFieldFactoryRegistryImpl implements TurboCrudFieldFactoryRegistry {

    private final Map<String, TurboCrudFieldFactory> factories = new HashMap<>();

    public DefaultFieldFactoryRegistryImpl(TurboCrudConfigService configService) {
        ApplicationConfig configuration = configService.getConfiguration();
        factories.put("text", new DefaultTextFieldFactory());
        factories.put("textarea", new DefaultTextAreaFactory());
        factories.put("date", new DefaultDatePickerFactory());
        factories.put("select", new DefaultComboBoxFactory(configuration.getSelects(), configuration.getTablesConfig()));
        factories.put("dropdown", new DefaultComboBoxFactory(configuration.getSelects(), configuration.getTablesConfig()));
    }

    public Map<String, TurboCrudFieldFactory> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudFieldFactory getFactory(FieldConfig type) {
        return factories.get(type.getType());
    }

    @Override
    public void addFactory(String key, TurboCrudFieldFactory factory) {
        factories.put(key, factory);
    }
}