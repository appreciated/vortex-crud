package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.config.model.FormElement;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;

public class DefaultListColumnImplCallback implements TurboCrudListColumnCallback {

    private final TurboCrudFileProviderRegistry registry;

    public DefaultListColumnImplCallback(TurboCrudFileProviderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void addColumn(Grid<GenericEntity> grid, FormElement field, String table, String fieldName, Field dataStoreField) {
        if (dataStoreField.getFactory().equals("image")) {
            if(dataStoreField.getConfiguration() == null){
                throw new IllegalArgumentException("The image field '" + fieldName + "' does not provide a imageFieldConfiguration");
            }
            grid.addComponentColumn(genericEntity -> {
                        String string = genericEntity.getString(fieldName);
                        ImageDisplayComponent image = new ImageDisplayComponent(registry.getFactory((Class<? extends TurboCrudFileProvider>) dataStoreField.getConfiguration().getFactory()));
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
