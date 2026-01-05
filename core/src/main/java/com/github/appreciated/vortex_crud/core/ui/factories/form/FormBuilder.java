package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.springframework.stereotype.Service;

@Service
public class FormBuilder<ModelClass, FieldType, RepositoryType> {

    public Component createComponent(RepositoryType dataStoreKey,
                                     FieldType fieldName,
                                     Field<ModelClass, FieldType, RepositoryType> field,
                                     VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = field.factory();
        return factory.createComponent(dataStoreKey, fieldName, field, context);
    }

    public void addComponentToForm(Component component,
                                   InternalFormElement<FieldType> element,
                                   FormLayout form) {
        if (component instanceof HasSize) {
            ((HasSize) component).setWidthFull();
        }
        if (component instanceof HasLabel) {
            ((HasLabel) component).setLabel(component.getTranslation(element.label()));
            form.add(component);
            form.setColspan(component, element.span());
        } else {
            FormLayout.FormItem formItem = form.addFormItem(component, component.getTranslation(element.label()));
            form.setColspan(formItem, element.span());
        }
    }

    public void createAndAddCollectionToForm(RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                             Collection<ModelClass, FieldType, RepositoryType> element,
                                             Object entity,
                                             VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                             FormLayout form) {
        Component collection = element.listFactory().createCollection(
                context.reflectionService().getId(entity),
                routeRenderer,
                element,
                context
        );
        form.add(collection);
        form.setColspan(collection, element.span());
    }
}
