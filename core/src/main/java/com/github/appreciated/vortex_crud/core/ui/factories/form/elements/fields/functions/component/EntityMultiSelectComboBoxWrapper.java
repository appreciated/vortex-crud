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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityMultiSelectComboBoxWrapper<ModelClass, FieldType, RepositoryType> extends HorizontalLayout implements HasValue<ValueChangeEvent<Set<Object>>, Set<Object>>, HasLabel {

    private final MultiSelectComboBox<Object> multiSelectComboBox;
    private final VortexCrudDataStore<FieldType, ?> dataStore;

    public EntityMultiSelectComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                                            Field<ModelClass, FieldType, RepositoryType> dataStoreField,
                                            ReflectionService<FieldType> reflectionService
    ) {
        MultiSelectField<ModelClass, FieldType, RepositoryType> multiSelectField = (MultiSelectField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        this.dataStore = dataStoreFactoryRegistry.getDataStore(multiSelectField.dataStore());
        this.multiSelectComboBox = new MultiSelectComboBox<>();

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

        multiSelectComboBox.setWidthFull();
        add(multiSelectComboBox);
    }

    @Override
    public Set<Object> getValue() {
        return multiSelectComboBox.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Set<Object>>> listener) {
        return multiSelectComboBox.addValueChangeListener(listener);
    }

    @Override
    public void setValue(Set<Object> value) {
        if (value != null && !value.isEmpty()) {
            // If values are IDs (Numbers), fetch entities; otherwise use as-is
            Set<Object> entities = value.stream()
                    .map(v -> v instanceof Number ? dataStore.getRecordById(v) : v)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            multiSelectComboBox.setValue(entities);
        } else {
            multiSelectComboBox.clear();
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
