package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class DefaultCollectionFactoryImpl implements FlowCmsCollectionFactory {

    private final FlowCmsEntityManagerService entityManagerService;

    public DefaultCollectionFactoryImpl(FlowCmsEntityManagerService entityManagerService) {
        this.entityManagerService = entityManagerService;
    }

    @Override
    public Component createCollection(String id, CollectionFactoryConfig factoryConfig) {
        VerticalLayout list = new VerticalLayout();
        List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(factoryConfig.getTable(), factoryConfig.getForeignKeyColumn(), id);
        for (GenericEntity recordsFromTableWhereColumnEqual : recordsFromTableWhereColumnEquals) {
            HorizontalLayout item = new HorizontalLayout();
            for (FormField child : factoryConfig.getChildren()) {
                Object o = recordsFromTableWhereColumnEqual.get(child.getColumn());
                item.add(new Text(o.toString()));
            }
        }
        return list;
    }
}