package com.github.appreciated.turbo_crud.core.ui.factories.route.list;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.config.model.FormElement;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.ui.components.ImageDisplayComponent;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions.ImageFieldFactory;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;

public class DefaultListColumnImplCallback implements TurboCrudListColumnCallback {

    private final TurboCrudFileProviderRegistry registry;

    public DefaultListColumnImplCallback(TurboCrudFileProviderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void addColumn(Grid<GenericEntity> grid, FormElement field, String table, String fieldName, Field dataStoreField) {
        if (dataStoreField.getFactory() == ImageFieldFactory.class) {
            if(dataStoreField.getConfiguration() == null){
                throw new IllegalArgumentException("The image field '" + fieldName + "' does not provide a imageFieldConfiguration");
            }
            grid.addComponentColumn(genericEntity -> {
                        String string = genericEntity.getString(fieldName);
                        ImageDisplayComponent image = new ImageDisplayComponent(registry.getFactory(dataStoreField.getConfiguration().getImageFactory()));
                        image.setImageSource(string);
                        image.setWidth(30, Unit.PIXELS);
                        image.setHeight(30, Unit.PIXELS);
                        return image;
                    }).setHeader(grid.getTranslation(field.getLabel()))
                    .setResizable(true)
                    .setAutoWidth(true);
        } else {
            grid.addColumn(entity -> entity.get(fieldName))
                    .setHeader(grid.getTranslation(field.getLabel()))
                    .setResizable(true)
                    .setAutoWidth(true);
        }
    }
}
