package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class EntityComboBoxWrapper<ModelClass, FieldType, RepositoryType> extends HorizontalLayout implements HasValue<ValueChangeEvent<Object>, Object>, HasLabel {

    private final ComboBox<Object> comboBox;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private Object currentValue;

    public EntityComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                 Field<ModelClass, FieldType, RepositoryType> dataStoreField,
                                 ReflectionService<FieldType> reflectionService,
                                 VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        ReferenceField<ModelClass, FieldType, RepositoryType> refField = (ReferenceField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        this.dataStore = (VortexCrudDataStore<FieldType, ?>) refField.dataStore();
        this.dataStoreUtil = dataStoreUtil;
        this.comboBox = new ComboBox<>();

        // Set up the ComboBox with a data provider and label generator
        comboBox.setDataProvider(
                (filterValue, i, i1) -> (java.util.stream.Stream<Object>) dataStore.getRecordsFromTableWhereColumnLike(refField.filterField(), filterValue, i, i1).stream(),
                filterValue -> dataStore.countWhereColumnLike(refField.filterField(), filterValue)
        );

        List<FieldType> labelFields = refField.children();
        if (labelFields == null || labelFields.isEmpty()) {
            labelFields = List.of(refField.filterField());
        }

        List<FieldType> finalLabelFields = labelFields;
        comboBox.setItemLabelGenerator(item -> finalLabelFields.stream()
                .map(fieldId -> {
                    String string = reflectionService.getString(item, fieldId);
                    return string == null ? "" : string;
                })
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        // Add a value change listener to handle when a new value is selected
        comboBox.addValueChangeListener(event -> {
            Object selectedEntity = event.getValue();
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
            }
        });
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