package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.HashSet;
import java.util.Set;

public class EntityMultiSelectComboBoxWrapper<ModelClass, FieldType, RepositoryType> extends HorizontalLayout implements HasValue<ValueChangeEvent<Set<Object>>, Set<Object>>, HasLabel {

    private final MultiSelectComboBox<Object> multiSelectComboBox;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private Set<Object> currentValue;

    public EntityMultiSelectComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                                            Field<ModelClass, FieldType, RepositoryType> dataStoreField,
                                            ReflectionService<FieldType> reflectionService
    ) {
        MultiSelectField<ModelClass, FieldType, RepositoryType> multiSelectField = (MultiSelectField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        this.dataStore = dataStoreFactoryRegistry.getDataStore(multiSelectField.dataStore());
        this.multiSelectComboBox = new MultiSelectComboBox<>();
        this.currentValue = new HashSet<>();

        // Set up the MultiSelectComboBox with a data provider and label generator
        multiSelectComboBox.setDataProvider(
                (filterValue, i, i1) -> (java.util.stream.Stream<Object>) dataStore.getRecordsFromTableWhereColumnLike(multiSelectField.filterField(), filterValue, i, i1).stream(),
                filterValue -> dataStore.countWhereColumnLike(multiSelectField.filterField(), filterValue)
        );

        multiSelectComboBox.setItemLabelGenerator(item -> multiSelectField.children().stream()
                .map(fieldId -> reflectionService.getString(item, fieldId))
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        // Add a value change listener to handle when new values are selected
        multiSelectComboBox.addValueChangeListener(event -> currentValue = event.getValue() != null ? new HashSet<>(event.getValue()) : new HashSet<>());
        multiSelectComboBox.setWidthFull();
        add(multiSelectComboBox);
    }

    // Return the MultiSelectComboBox component
    public Component getComponent() {
        return multiSelectComboBox;
    }

    // Implementing getValue() to return the current selected entities
    @Override
    public Set<Object> getValue() {
        return currentValue;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Set<Object>>> listener) {
        return multiSelectComboBox.addValueChangeListener(listener);
    }

    // Implementing setValue() to load entities based on their IDs
    @Override
    public void setValue(Set<Object> ids) {
        if (ids != null && !ids.isEmpty()) {
            Set<Object> entities = new HashSet<>();
            for (Object id : ids) {
                if (!(id instanceof Number)) {
                    entities.add(id);
                } else {
                    Object entity = dataStore.getRecordById(id);
                    if (entity != null) {
                        entities.add(entity);
                    }
                }
            }
            multiSelectComboBox.setValue(entities);
            currentValue = new HashSet<>(ids);
        } else {
            multiSelectComboBox.clear();
            currentValue = new HashSet<>();
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        multiSelectComboBox.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return multiSelectComboBox.isReadOnly();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        multiSelectComboBox.setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return multiSelectComboBox.isRequiredIndicatorVisible();
    }

    @Override
    public void setLabel(String label) {
        multiSelectComboBox.setLabel(label);
    }

    @Override
    public String getLabel() {
        return multiSelectComboBox.getLabel();
    }
}
