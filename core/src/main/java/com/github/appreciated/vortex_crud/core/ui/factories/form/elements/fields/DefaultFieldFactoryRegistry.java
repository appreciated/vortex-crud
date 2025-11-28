package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EmailFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.PasswordFieldFactory;
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
public class DefaultFieldFactoryRegistry<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    private final Map<Class<? extends VortexCrudFieldFactory>, VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factories = new HashMap<>();

    public DefaultFieldFactoryRegistry(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                       VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                                       VortexCrudFileProviderRegistry fileProviderRegistry,
                                       VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                       ReflectionService<FieldType> reflectionService) {
        Application<ModelClass, FieldType, RepositoryType> configuration = configService.configuration();
        factories.put(TextFieldFactory.class, new TextFieldFactory<>());
        factories.put(EmailFieldFactory.class, new EmailFieldFactory<>());
        factories.put(TextAreaFieldFactory.class, new TextAreaFieldFactory<>());
        factories.put(DateFieldFactory.class, new DateFieldFactory<>());
        factories.put(DateTimePickerFactory.class, new DateTimePickerFactory<>());
        factories.put(SelectFieldFactory.class, new SelectFieldFactory<>(configuration.selects(), configuration.dataStores()));
        factories.put(MultiSelectValueFieldFactory.class, new MultiSelectValueFieldFactory<>(configuration.selects(), configuration.dataStores()));
        factories.put(DoubleNumberFieldFactory.class, new DoubleNumberFieldFactory<>());
        factories.put(BigDecimalNumberFieldFactory.class, new BigDecimalNumberFieldFactory<>());
        factories.put(IntegerNumberFieldFactory.class, new IntegerNumberFieldFactory<>());
        factories.put(ReferenceFieldFactory.class, new ReferenceFieldFactory<>(resolver, dataStoreFactoryRegistry, reflectionService));
        factories.put(MultiSelectFieldFactory.class, new MultiSelectFieldFactory<>(resolver, dataStoreFactoryRegistry, reflectionService));
        factories.put(ImageFieldFactory.class, new ImageFieldFactory<>(fileProviderRegistry));
        factories.put(VideoFieldFactory.class, new VideoFieldFactory<>(fileProviderRegistry));
        factories.put(FileFieldFactory.class, new FileFieldFactory<>(fileProviderRegistry));
        factories.put(PdfFieldFactory.class, new PdfFieldFactory<>(fileProviderRegistry));
        factories.put(MarkDownFieldFactory.class, new MarkDownFieldFactory<>());
        factories.put(CheckboxFieldFactory.class, new CheckboxFieldFactory<>());
        factories.put(IdFieldFactory.class, new IdFieldFactory<>());
        factories.put(PasswordFieldFactory.class, new PasswordFieldFactory<>());
    }

    public Map<Class<? extends VortexCrudFieldFactory>, VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactories() {
        return factories;
    }

    @Override
    public VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<? extends VortexCrudFieldFactory> type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), type)));
    }

    public VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> getFactoryForField(Field<?, ?, ?> field) {
        // Resolve by concrete Field implementation class (raw checks to avoid generic incompatibilities)
        if (field instanceof SelectField) {
            return getFactory(SelectFieldFactory.class);
        }
        if (field instanceof MultiSelectValueField) {
            return getFactory(MultiSelectValueFieldFactory.class);
        }
        if (field instanceof ImageField) {
            return getFactory(ImageFieldFactory.class);
        }
        if (field instanceof VideoField) {
            return getFactory(VideoFieldFactory.class);
        }
        if (field instanceof FileField) {
            return getFactory(FileFieldFactory.class);
        }
        if (field instanceof PdfField) {
            return getFactory(PdfFieldFactory.class);
        }
        if (field instanceof MarkDownField) {
            return getFactory(MarkDownFieldFactory.class);
        }
        if (field instanceof ReferenceField) {
            return getFactory(ReferenceFieldFactory.class);
        }
        if (field instanceof MultiSelectField) {
            return getFactory(MultiSelectFieldFactory.class);
        }
        if (field instanceof TextField) {
            return getFactory(TextFieldFactory.class);
        }
        if (field instanceof TextAreaField) {
            return getFactory(TextAreaFieldFactory.class);
        }
        if (field instanceof DateField) {
            return getFactory(DateFieldFactory.class);
        }
        if (field instanceof DateTimePickerField) {
            return getFactory(DateTimePickerFactory.class);
        }
        if (field instanceof DoubleField) {
            return getFactory(DoubleNumberFieldFactory.class);
        }
        if (field instanceof BigDecimalField) {
            return getFactory(BigDecimalNumberFieldFactory.class);
        }
        if (field instanceof IntegerField) {
            return getFactory(IntegerNumberFieldFactory.class);
        }
        if (field instanceof CheckboxField) {
            return getFactory(CheckboxFieldFactory.class);
        }
        if (field instanceof IdField) {
            return getFactory(IdFieldFactory.class);
        }
        // Default to TextFieldFactory for simple cases
        return getFactory(TextFieldFactory.class);
    }

    @Override
    public void addFactory(Class<? extends VortexCrudFieldFactory> key, VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory) {
        factories.put(key, factory);
    }
}