package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldValueStrategy;
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
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final ReferenceFieldValueStrategy valueStrategy;
    private Object currentValue;
    private Object currentEntityValue;

    public EntityComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                 Field<? extends ModelClass, FieldType, RepositoryType> dataStoreField,
                                 ReflectionService<FieldType> reflectionService,
                                 VortexCrudDataStoreUtilStrategy dataStoreUtil,
                                 ReferenceFieldValueStrategy valueStrategy
    ) {
        ReferenceField<? extends ModelClass, FieldType, RepositoryType> refField = (ReferenceField<? extends ModelClass, FieldType, RepositoryType>) dataStoreField;
        this.dataStore = (VortexCrudDataStore<FieldType, ?>) refField.dataStore();
        this.dataStoreUtil = dataStoreUtil;
        this.valueStrategy = valueStrategy;
        this.comboBox = new ComboBox<>();

        // Set up the ComboBox with a data provider and label generator
        comboBox.setDataProvider(
                (filterValue, i, i1) -> (java.util.stream.Stream<Object>) dataStore.getRecordsFromTableWhereColumnLike(refField.filterField(), filterValue, i, i1).stream(),
                filterValue -> dataStore.countWhereColumnLike(refField.filterField(), filterValue)
        );

        comboBox.setItemLabelGenerator(item -> refField.children().stream()
                .map(fieldId -> reflectionService.getString(item, fieldId))
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        // Add a value change listener to handle when a new value is selected
        comboBox.addValueChangeListener(event -> {
            Object selectedEntity = event.getValue();
            currentEntityValue = selectedEntity;
            if (selectedEntity != null) {
                // Extract the ID from the selected entity using the utility strategy
                String idString = dataStoreUtil.getId(selectedEntity);
                // Convert the ID string to the appropriate type (Integer, Long, etc.)
                if (idString != null) {
                    try {
                        currentValue = Integer.valueOf(idString);
                    } catch (NumberFormatException e) {
                        // If it's not an integer, try other types or keep as string
                        try {
                            currentValue = Long.valueOf(idString);
                        } catch (NumberFormatException ex) {
                            currentValue = idString;
                        }
                    }
                } else {
                    currentValue = null;
                }
            } else {
                currentValue = null;
                currentEntityValue = null;
            }
        });
        comboBox.setWidthFull();
        add(comboBox);
    }

    // Return the ComboBox component
    public Component getComponent() {
        return comboBox;
    }

    // Implementing getValue() to return either the entity object (JPA) or the ID (jOOQ)
    @Override
    public Object getValue() {
        // Use strategy to determine what to return
        return valueStrategy.prepareValueForEntity(currentEntityValue, currentValue, dataStoreUtil);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Object>> listener) {
        return comboBox.addValueChangeListener(listener);
    }

    // Implementing setValue() to load an entity based on the ID or entity object
    @Override
    public void setValue(Object value) {
        if (value != null) {
            // Use strategy to process the incoming value
            ReferenceFieldValueStrategy.ValueHolder holder = valueStrategy.processIncomingValue(value, dataStore, dataStoreUtil);
            currentEntityValue = holder.getEntityValue();
            currentValue = holder.getIdValue();
            comboBox.setValue(currentEntityValue);
        } else {
            comboBox.clear();
            currentEntityValue = null;
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