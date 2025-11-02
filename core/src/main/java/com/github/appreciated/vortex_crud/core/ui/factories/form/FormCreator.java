package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FormCreator<ModelClass, FieldType, RepositoryType> {

    private final DefaultFieldFactoryRegistry<ModelClass, FieldType, RepositoryType> componentFactory;
    private final VortexCrudCollectionFactoryRegistry<ModelClass, FieldType, RepositoryType> collectionFactoryRegistry;
    private final ReflectionService<FieldType> reflectionService;

    @Autowired(required = false)
    private com.github.appreciated.vortex_crud.core.security.RbacPermissionChecker permissionChecker;

    public FormCreator(DefaultFieldFactoryRegistry<ModelClass, FieldType, RepositoryType> componentFactory,
                       VortexCrudCollectionFactoryRegistry<ModelClass, FieldType, RepositoryType> collectionFactoryRegistry,
                       ReflectionService<FieldType> reflectionService) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
        this.reflectionService = reflectionService;
    }

    public void bindAndAddToLayout(RepositoryType dataStoreKey,
                                   RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                   List<InternalFormElement<ModelClass, FieldType, RepositoryType>> fieldsViewConfig,
                                   Object entity,
                                   VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
                                   DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
                                   Binder<Object> binder,
                                   FormLayout form) {
        Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig = dataStoreConfig.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<ModelClass, FieldType, RepositoryType> element : fieldsViewConfig) {
            if (element.getType() != ViewFieldType.COLLECTION) {
                FieldType fieldName = element.getField();
                Field<ModelClass, FieldType, RepositoryType> field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + dataStoreKey + "'");
                }

                // Check field-level permissions
                FieldAccessLevel accessLevel = getFieldAccessLevel(field);
                if (accessLevel == FieldAccessLevel.NONE) {
                    continue; // Skip this field entirely
                }

                VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = componentFactory.getFactory(field.factory());
                Component component = factory.createComponent(dataStoreKey, fieldName, field);

                // Apply read-only if user only has read access
                if (accessLevel == FieldAccessLevel.READ_ONLY && component instanceof HasEnabled) {
                    ((HasEnabled) component).setEnabled(false);
                }

                // Apply validation if present
                if (field.validation() != null) {
                    field.validation().applyToComponent(component);
                }

                Binder.BindingBuilder<Object, Object> builder = (Binder.BindingBuilder<Object, Object>) binder.forField((HasValue<?, ?>) component);
                if (fieldName instanceof String propertyName) {
                    builder = builder.withValidator(new BeanValidator(entity.getClass(), propertyName));
                }
                if (field.required() && component instanceof HasValue) {
                    builder = builder.asRequired();
                }

                builder.bind(
                        entity1 -> reflectionService.getValue(entity1, fieldName),
                        (entity1, o) -> reflectionService.setValue(entity1, fieldName, o)
                );

                if (component instanceof HasSize) {
                    ((HasSize) component).setWidthFull();
                }
                if (component instanceof HasLabel) {
                    ((HasLabel) component).setLabel(component.getTranslation(element.getLabel()));
                    form.add(component);
                    form.setColspan(component, element.getSpan());
                } else {
                    FormLayout.FormItem formItem = form.addFormItem(component, component.getTranslation(element.getLabel()));
                    form.setColspan(formItem, element.getSpan());
                }
            } else {
                Component collection = collectionFactoryRegistry.getFactory(element.getFactory()).createCollection(
                        reflectionService.getId(entity),
                        routeRenderer,
                        element,
                        routeFactory,
                        this
                );
                form.add(collection);
                form.setColspan(collection, element.getSpan());
            }
        }
    }

    /**
     * Access level for field permissions.
     */
    private enum FieldAccessLevel {
        NONE,       // Field is hidden
        READ_ONLY,  // Field is visible but disabled
        WRITE       // Field is fully editable
    }

    /**
     * Determines the access level for a field based on user roles.
     */
    private FieldAccessLevel getFieldAccessLevel(AccessControlled field) {
        // If permission checker is not available (security module not included), grant full access
        if (permissionChecker == null) {
            return FieldAccessLevel.WRITE;
        }

        com.github.appreciated.vortex_crud.core.security.RbacPermissionChecker.AccessLevel level =
                permissionChecker.getAccessLevel(field);

        return switch (level) {
            case NONE -> FieldAccessLevel.NONE;
            case READ_ONLY -> FieldAccessLevel.READ_ONLY;
            case WRITE -> FieldAccessLevel.WRITE;
        };
    }
}