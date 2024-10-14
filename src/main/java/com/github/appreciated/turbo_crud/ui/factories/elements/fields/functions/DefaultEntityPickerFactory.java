package com.github.appreciated.turbo_crud.ui.factories.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.TableConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;

import java.util.Map;

public class DefaultEntityPickerFactory implements TurboCrudFieldFactory {

    private final Map<String, TableConfig> tableConfig;
    private final TurboCrudEntityManagerService entityManagerService;

    public DefaultEntityPickerFactory(Map<String, TableConfig> tableConfig, TurboCrudEntityManagerService entityManagerService) {
        this.tableConfig = tableConfig;
        this.entityManagerService = entityManagerService;
    }

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        return new EntityComboBoxWrapper(entityManagerService,fieldConfig);
    }
}