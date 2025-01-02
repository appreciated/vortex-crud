package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.turbo_crud.core.config.model.Application;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions.*;
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
public class DefaultFieldFactoryRegistry<DataStoreId, FieldId> implements TurboCrudFieldFactoryRegistry<DataStoreId, FieldId> {

    private final Map<Class<? extends TurboCrudFieldFactory>, TurboCrudFieldFactory<DataStoreId, FieldId>> factories = new HashMap<>();

    public DefaultFieldFactoryRegistry(TurboCrudConfigService<DataStoreId, FieldId> configService, TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, TurboCrudFileProviderRegistry fileProviderRegistry) {
        Application<DataStoreId, FieldId> configuration = configService.getConfiguration();
        factories.put(TextFieldFactory.class, new TextFieldFactory<>());
        factories.put(TextAreaFieldFactory.class, new TextAreaFieldFactory<>());
        factories.put(DateFieldFactory.class, new DateFieldFactory<>());
        factories.put(DateTimePickerFactory.class, new DateTimePickerFactory<>());
        factories.put(SelectFieldFactory.class, new SelectFieldFactory<>(configuration.getSelects(), configuration.getDataStores()));
        factories.put(NumberFieldFactory.class, new NumberFieldFactory<>());
        factories.put(ReferenceFieldFactory.class, new ReferenceFieldFactory<>(dataStoreFactoryRegistry));
        factories.put(ImageFieldFactory.class, new ImageFieldFactory<>(fileProviderRegistry));
        factories.put(TCCheckboxFieldFactory.class, new TCCheckboxFieldFactory<>());
        factories.put(IdFieldFactory.class, new IdFieldFactory<>());
    }

    public Map<Class<? extends TurboCrudFieldFactory>, TurboCrudFieldFactory<DataStoreId, FieldId>> getFactories() {
        return factories;
    }

    @Override
    public TurboCrudFieldFactory<DataStoreId, FieldId> getFactory(Class<? extends TurboCrudFieldFactory> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends TurboCrudFieldFactory> key, TurboCrudFieldFactory<DataStoreId, FieldId> factory) {
        factories.put(key, factory);
    }
}