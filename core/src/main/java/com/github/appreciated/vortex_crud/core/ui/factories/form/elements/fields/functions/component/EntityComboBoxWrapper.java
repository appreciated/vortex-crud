package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class EntityComboBoxWrapper<ModelClass, FieldType, RepositoryType> extends HorizontalLayout implements HasValue<ValueChangeEvent<Object>, Object>, HasLabel {

    private final ComboBox<Object> comboBox;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private Object currentValue;

    public EntityComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                 Field<ModelClass, FieldType, RepositoryType> dataStoreField,
                                 ReflectionService<FieldType> reflectionService
    ) {
        ReferenceField<ModelClass, FieldType, RepositoryType> refField = (ReferenceField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        this.dataStore = (VortexCrudDataStore<FieldType, ?>) refField.dataStore();
        this.comboBox = new ComboBox<>();

        // Set up the ComboBox with a data provider and label generator
        if (dataStore instanceof VortexCrudQueryDataStore) {
            VortexCrudQueryDataStore<FieldType, ?> queryDataStore = (VortexCrudQueryDataStore<FieldType, ?>) dataStore;
            comboBox.setDataProvider(
                    (filterValue, i, i1) -> (java.util.stream.Stream<Object>) queryDataStore.getRecordsFromTableWhereColumnLike(refField.filterField(), filterValue, i, i1).stream(),
                    filterValue -> queryDataStore.countWhereColumnLike(refField.filterField(), filterValue)
            );
        } else {
             // Fallback for basic stores
             comboBox.setDataProvider(
                (filterValue, i, i1) -> (java.util.stream.Stream<Object>) dataStore.getRecordsFromTable(i, i1).stream(),
                filterValue -> dataStore.count()
            );
        }

        comboBox.setItemLabelGenerator(item -> refField.children().stream()
                .map(fieldId -> reflectionService.getString(item, fieldId))
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        // Add a value change listener to handle when a new value is selected
        comboBox.addValueChangeListener(event -> currentValue = event.getValue() != null ? event.getValue() : null);
        comboBox.setWidthFull();
        add(comboBox);
    }

    // Return the ComboBox component
    public Component getComponent() {
        return comboBox;
    }

    // Implementing getValue() to return the current ID
    @Override
    public Object getValue() {
        return currentValue;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Object>> listener) {
        return comboBox.addValueChangeListener(listener);
    }

    // Implementing setValue() to load an entity based on the ID
    @Override
    public void setValue(Object id) {
        if (id != null) {
            if (!(id instanceof Number)) {
                comboBox.setValue(id);
            } else {
                Object entity = dataStore.getRecordById(id);
                comboBox.setValue(entity);
                currentValue = id;
            }
        } else {
            comboBox.clear();
            currentValue = null;
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        comboBox.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return comboBox.isReadOnly();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        comboBox.setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return comboBox.isRequiredIndicatorVisible();
    }

    @Override
    public void setLabel(String label) {
        comboBox.setLabel(label);
    }

    @Override
    public String getLabel() {
        return comboBox.getLabel();
    }
}