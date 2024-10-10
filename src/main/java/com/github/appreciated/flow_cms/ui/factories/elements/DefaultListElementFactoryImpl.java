package com.github.appreciated.flow_cms.ui.factories.elements;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.vaadin.flow.component.Component;

public class DefaultListElementFactoryImpl implements FlowCmsElementFactory {

    private final FlowCmsEntityManagerService entityManagerService;
    private final FlowCmsConfigService configService;

    public DefaultListElementFactoryImpl(FlowCmsEntityManagerService entityManagerService,
                                         FlowCmsConfigService configService) {
        this.entityManagerService = entityManagerService;
        this.configService = configService;
    }

    @Override
    public Component createElement(String id, String field, FieldConfig fieldConfig) {
        // Create a component that will display a list based on the table and field configurations
       /* List<GenericEntity> records = entityManagerService.getRecordsFromTable(table, 0, 100);
        
        ListComponent listComponent = componentFactory.createListComponent();
        for (GenericEntity record : records) {
            ListItemComponent itemComponent = componentFactory.createListItemComponent();
            
            // Iterate through each child configuration to build the list items
            for (String column : fieldConfig.getValues().split(",")) {
                String value = record.getFieldValue(column.trim());
                itemComponent.addField(column, value);
            }

            listComponent.addItem(itemComponent);
        }

        return listComponent;*/
        return null;
    }
}