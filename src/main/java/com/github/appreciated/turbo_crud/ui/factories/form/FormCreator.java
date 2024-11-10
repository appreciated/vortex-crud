package com.github.appreciated.turbo_crud.ui.factories.form;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.one_to_many.TurboCrudOneToManyCollectionFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormCreator {

    private final DefaultFieldFactoryRegistryImpl componentFactory;
    private final TurboCrudOneToManyCollectionFactoryRegistry collectionFactoryRegistry;

    public FormCreator(DefaultFieldFactoryRegistryImpl componentFactory, TurboCrudOneToManyCollectionFactoryRegistry collectionFactoryRegistry) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
    }

    public void bindAndAddToLayout(String table,
                                   Route route,
                                   Form formConfiguration,
                                   GenericEntity entity,
                                   TurboCrudRouteFactoryRegistry routeFactory,
                                   Repository tables,
                                   Binder<GenericEntity> binder,
                                   FormLayout form,
                                   FormCreator formCreator) {
        Map<String, RepositoryField> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormElement field : formConfiguration.getChildren()) {
            String fieldName = field.getField();
            if (!field.getType().equals("collection")) {
                RepositoryField repositoryField = fieldsConfig.get(fieldName);
                if (repositoryField == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
                }
                TurboCrudFieldFactory factory = componentFactory.getFactory(repositoryField.getFactory());
                Component component = factory.createComponent(table, fieldName, repositoryField);
                binder.bind((HasValue) component, entity1 -> entity1.get(fieldName), (entity1, o) -> entity1.put(fieldName, o));
                if (component instanceof HasSize){
                    ((HasSize) component).setWidthFull();
                }
                FormLayout.FormItem formItem = form.addFormItem(component, component.getTranslation(field.getLabel()));
                form.setColspan(formItem, (field.getSpan() == null ? 1 : field.getSpan()));
            } else {
                if (field.getType().equals("collection")) {
                    Component collection = collectionFactoryRegistry.getFactory(field.getFactory()).createCollection(
                            EntityUtil.getId(entity),
                            route,
                            field,
                            routeFactory,
                            formCreator
                    );
                    FormLayout.FormItem formItem = form.addFormItem(collection, form.getTranslation(field.getLabel()));
                    form.setColspan(formItem, (field.getSpan() == null ? 2 : field.getSpan()));
                } else {
                    throw new IllegalStateException("Cannot initialize field with name '%s'".formatted(fieldName));
                }
            }
        }
    }
}