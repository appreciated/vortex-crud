package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;

public class DefaultListColumnImplCallback<DataStoreId, FieldId, KeyType> implements VortexCrudListColumnCallback<DataStoreId, FieldId, KeyType> {

    private final VortexCrudFileProviderRegistry registry;
    private final ReflectionService<FieldId> reflectionService;

    public DefaultListColumnImplCallback(VortexCrudFileProviderRegistry registry,
                                         ReflectionService<FieldId> reflectionService) {
        this.registry = registry;
        this.reflectionService = reflectionService;
    }

    @Override
    public void addColumn(Grid<Object> grid, InternalFormElement<DataStoreId, FieldId, KeyType> field, Object table, Field<DataStoreId, FieldId, KeyType> dataStoreField) {
        if (dataStoreField instanceof ImageField<?, ?, ?>) {
            ImageField<DataStoreId, FieldId, KeyType> imageField = (ImageField<DataStoreId, FieldId, KeyType>) dataStoreField;
            if (imageField.getConfiguration() == null) {
                throw new IllegalArgumentException("The image field '" + field.getField() + "' does not provide a imageFieldConfiguration");
            }
            grid.addComponentColumn(entity -> {
                        String string = reflectionService.getString(entity, field.getField());
                        ImageDisplayComponent image = new ImageDisplayComponent(registry.getFactory(imageField.getConfiguration().getImageFactory()));
                        image.setImageSource(string);
                        image.setWidth(30, Unit.PIXELS);
                        image.setHeight(30, Unit.PIXELS);
                        return image;
                    }).setHeader(grid.getTranslation(field.getLabel()))
                    .setResizable(true)
                    .setAutoWidth(true);
        } else {
            grid.addColumn(entity -> reflectionService.getValue(entity, field.getField()))
                    .setHeader(grid.getTranslation(field.getLabel()))
                    .setResizable(true)
                    .setAutoWidth(true);
        }
    }
}
