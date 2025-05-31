package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.model.GenericEntityMapper;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.UUID;

public class EntityComboBoxWrapper<DataStoreId, FieldId> extends HorizontalLayout implements HasValue<ValueChangeEvent<Object>, Object>, HasLabel {

    private final ComboBox<GenericEntity> comboBox;
    private final VortexCrudDataStore<FieldId> dataStore;
    private final Field<DataStoreId, FieldId> dataStoreField;
    private final GenericEntityMapper entityMapper;
    private Object currentValue;

    public EntityComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                 VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                 Field<DataStoreId, FieldId> dataStoreField,
                                 GenericEntityMapper entityMapper
    ) {
        this.dataStoreField = dataStoreField;
        this.entityMapper = entityMapper;
        this.dataStore = dataStoreFactoryRegistry.getFactory(dataStoreField.getDataStore());
        this.comboBox = new ComboBox<>();

        // Set up the ComboBox with a data provider and label generator
        comboBox.setDataProvider(
                (filterValue, i, i1) -> dataStore.getRecordsFromTableWhereColumnLike(dataStoreField.getFilterField(), filterValue, i, i1).stream(),
                filterValue -> dataStore.countWhereColumnLike(dataStoreField.getFilterField(), filterValue)
        );

        comboBox.setItemLabelGenerator(item -> dataStoreField.getChildren().stream()
                .map(resolver::getKeyForFieldId)
                .map(item::getString)
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        // Add a value change listener to handle when a new value is selected
        comboBox.addValueChangeListener(event -> currentValue = event.getValue() != null ? event.getValue().get("id") : null);
        comboBox.setWidthFull();
        add(comboBox);
    }

    // Return the ComboBox component
    public Component getComponent() {
        return comboBox;
    }

    // Implementing getValue() to return the current Id
    @Override
    public Object getValue() {
        return currentValue;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Object>> listener) {
        return comboBox.addValueChangeListener((ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<GenericEntity>, GenericEntity>>) listener);
    }

    // Implementing setValue() to load an entity based on the Id
    @Override
    public void setValue(Object id) {
        if (id != null) {
            if (!(id instanceof Number || id instanceof UUID)) {
                comboBox.setValue(entityMapper.mapFromEntity(id, dataStore.getFields()));
            } else {
                GenericEntity entity = dataStore.getRecordById(id);
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