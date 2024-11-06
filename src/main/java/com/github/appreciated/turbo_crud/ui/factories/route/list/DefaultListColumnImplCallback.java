package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.FormItem;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;

public class DefaultListColumnImplCallback implements TurboCrudListColumnCallback {

    @Override
    public void addColumn(Grid<GenericEntity> grid, FormItem field, String table, String fieldName, FieldConfig fieldConfig) {
        if (fieldConfig.getFactory().equals("image")) {
            grid.addComponentColumn(genericEntity -> {
                        String string = genericEntity.getString(fieldName);
                        ImageDisplayComponent image = new ImageDisplayComponent();
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
