package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreAdapter;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.Set;
import java.util.stream.Collectors;

public class EntityMultiSelectComboBoxWrapper<ModelClass, FieldType, RepositoryType> extends HorizontalLayout implements HasValue<ValueChangeEvent<Set<Object>>, Set<Object>>, HasLabel {

    private final MultiSelectComboBox<Object> multiSelectComboBox;
    private final VortexCrudDataStore<FieldType, ?> dataStore;

    public EntityMultiSelectComboBoxWrapper(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                            Field<ModelClass, FieldType, RepositoryType> dataStoreField,
                                            ReflectionService<FieldType> reflectionService
    ) {
        MultiSelectField<ModelClass, FieldType, RepositoryType> multiSelectField = (MultiSelectField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        this.dataStore = (VortexCrudDataStore<FieldType, ?>) multiSelectField.dataStore();
        this.multiSelectComboBox = new MultiSelectComboBox<>();

        // Set up the MultiSelectComboBox with a data provider and label generator
        VortexCrudQueryDataStore<FieldType, ?> queryDataStore =
                (dataStore instanceof VortexCrudQueryDataStore)
                        ? (VortexCrudQueryDataStore<FieldType, ?>) dataStore
                        : new VortexCrudQueryDataStoreAdapter<>(dataStore);

        multiSelectComboBox.setDataProvider(
                (filterValue, offset, limit) ->
                        queryDataStore.getRecordsFromTableWhereColumnLike(multiSelectField.filterField(), filterValue, offset, limit)
                                .stream()
                                .map(obj -> (Object) obj),
                filterValue -> queryDataStore.countWhereColumnLike(multiSelectField.filterField(), filterValue)
        );

        multiSelectComboBox.setItemLabelGenerator(item -> multiSelectField.children().stream()
                .map(fieldId -> reflectionService.getString(item, fieldId))
                .reduce((o, o2) -> o + ", " + o2)
                .orElse("")
        );

        multiSelectComboBox.setWidthFull();
        add(multiSelectComboBox);
    }

    // Implementing getValue() to return the current set of entity objects or IDs
    @Override
    public Set<Object> getValue() {
        return multiSelectComboBox.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Set<Object>>> listener) {
        return multiSelectComboBox.addValueChangeListener(listener);
    }

    // Implementing setValue() to load entities based on the IDs or set entities directly
    @Override
    public void setValue(Set<Object> values) {
        if (values == null || values.isEmpty()) {
            multiSelectComboBox.clear();
            return;
        }

        // Check if we have ID values or entity objects
        Object firstItem = values.iterator().next();
        if (firstItem instanceof Number) {
            // Load entities from IDs
            Set<Object> entities = values.stream()
                    .map(dataStore::getRecordById)
                    .collect(Collectors.toSet());
            multiSelectComboBox.setValue(entities);
        } else {
            // Already entity objects
            multiSelectComboBox.setValue(values);
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
