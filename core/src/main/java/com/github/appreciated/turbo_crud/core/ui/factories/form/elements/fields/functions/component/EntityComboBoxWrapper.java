package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class EntityComboBoxWrapper extends HorizontalLayout implements HasValue<ValueChangeEvent<Integer>, Integer>, HasLabel {

    private final ComboBox<GenericEntity> comboBox;
    private final TurboCrudDataStore dataStore;
    private Integer currentValue;

    public EntityComboBoxWrapper(TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry, Field dataStoreField) {
        this.dataStore = dataStoreFactoryRegistry.getFactory(dataStoreField.getDataStore());
        this.comboBox = new ComboBox<>();

        // Set up the ComboBox with a data provider and label generator
        comboBox.setDataProvider(
                (filterValue, i, i1) -> dataStore.getRecordsFromTableWhereColumnLike(dataStoreField.getFilterField(), filterValue, i, i1).stream(),
                filterValue -> dataStore.countWhereColumnLike(dataStoreField.getFilterField(), filterValue)
        );

        comboBox.setItemLabelGenerator(item -> dataStoreField.getChildren().stream()
                .map(item::getString)
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        // Add a value change listener to handle when a new value is selected
        comboBox.addValueChangeListener(event -> currentValue = event.getValue() != null ? (Integer) event.getValue().get("id") : null);
        comboBox.setWidthFull();
        add(comboBox);
    }

    // Return the ComboBox component
    public Component getComponent() {
        return comboBox;
    }

    // Implementing getValue() to return the current Id
    @Override
    public Integer getValue() {
        return currentValue;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Integer>> listener) {
        return comboBox.addValueChangeListener((ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<GenericEntity>, GenericEntity>>) listener);
    }

    // Implementing setValue() to load an entity based on the Id
    @Override
    public void setValue(Integer id) {
        if (id != null) {
            GenericEntity entity = dataStore.getRecordById(id);
            comboBox.setValue(entity);
            currentValue = id;
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