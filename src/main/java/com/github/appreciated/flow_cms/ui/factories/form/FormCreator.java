package com.github.appreciated.flow_cms.ui.factories.form;

import com.github.appreciated.flow_cms.config.model.*;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.factories.collection.FlowCmsCollectionFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.fields.DefaultFieldFactoryRegistryImpl;
import com.github.appreciated.flow_cms.ui.factories.fields.FlowCmsFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.shared.InputField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormCreator {

    private final DefaultFieldFactoryRegistryImpl componentFactory;
    private final FlowCmsCollectionFactoryRegistry collectionFactoryRegistry;

    public FormCreator(DefaultFieldFactoryRegistryImpl componentFactory, FlowCmsCollectionFactoryRegistry collectionFactoryRegistry) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
    }

    public void bindAndAddToLayout(String table, DetailFactory detailFactory, GenericEntity entity, FlowCmsDetailFactoryRegistry detailFactoryRegistry, TableConfig tables, Binder<GenericEntity> binder, FormLayout form, FormCreator formCreator) {
        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormField field : detailFactory.getChildren()) {
            String fieldName = field.getColumn();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);
            if (fieldConfig == null && field.getType() != null && !field.getType().equals("collection")) {
                throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
            }
            if (fieldConfig != null){
                FlowCmsFieldFactory factory = componentFactory.getFactory(fieldConfig);
                Component component = factory.createComponent(table, fieldName, fieldConfig);
                if (component instanceof InputField) {
                    ((InputField<?, ?>) component).setLabel(component.getTranslation(field.getLabel()));
                }
                binder.bind((HasValue) component, entity1 -> entity1.get(fieldName), (entity1, o) -> entity1.put(fieldName, o));
                form.add(component);
            } else {
                CollectionFactoryConfig collectionFactory = field.getCollectionFactory();
                Component collection = collectionFactoryRegistry.getFactory(field).createCollection("" + entity.get("id"), collectionFactory,detailFactoryRegistry, collectionFactory.getDetailFactory(),formCreator);
                form.add(collection);
                form.setColspan(collection,2);
            }
        }
    }
}