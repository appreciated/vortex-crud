package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the VortexCrudComponentFactory interface.
 * This factory provides components based on the configuration specified in FieldConfig,
 * supporting various component types like text fields, text areas, date pickers, and dropdowns.
 */

@Service
public class DefaultFieldFactoryRegistry<DataStoreId, FieldId, ModelClass> implements VortexCrudFieldFactoryRegistry<DataStoreId, FieldId, ModelClass> {

    private final Map<Class<? extends VortexCrudFieldFactory>, VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> factories = new HashMap<>();

    public DefaultFieldFactoryRegistry(VortexCrudConfigService<DataStoreId, FieldId, ModelClass> configService, VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry, VortexCrudFileProviderRegistry fileProviderRegistry, VortexCrudDataStoreFieldNameResolver<FieldId> resolver) {
        Application<DataStoreId, FieldId, ModelClass> configuration = configService.getConfiguration();
        factories.put(TextFieldFactory.class, new TextFieldFactory<>());
        factories.put(TextAreaFieldFactory.class, new TextAreaFieldFactory<>());
        factories.put(DateFieldFactory.class, new DateFieldFactory<>());
        factories.put(DateTimePickerFactory.class, new DateTimePickerFactory<>());
        factories.put(SelectFieldFactory.class, new SelectFieldFactory<>(configuration.getSelects(), configuration.getDataStores()));
        factories.put(NumberFieldFactory.class, new NumberFieldFactory<>());
        factories.put(ReferenceFieldFactory.class, new ReferenceFieldFactory<>(resolver, dataStoreFactoryRegistry));
        factories.put(ImageFieldFactory.class, new ImageFieldFactory<>(fileProviderRegistry));
        factories.put(CheckboxFieldFactory.class, new CheckboxFieldFactory<>());
        factories.put(IdFieldFactory.class, new IdFieldFactory<>());
    }

    public Map<Class<? extends VortexCrudFieldFactory>, VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass> getFactory(Class<? extends VortexCrudFieldFactory> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    @Override
    public void addFactory(Class<? extends VortexCrudFieldFactory> key, VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass> factory) {
        factories.put(key, factory);
    }
}