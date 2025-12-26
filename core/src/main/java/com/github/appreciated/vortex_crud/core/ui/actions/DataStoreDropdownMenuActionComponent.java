package com.github.appreciated.vortex_crud.core.ui.actions;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreDropdownMenuAction;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.select.Select;

import java.util.List;

/**
 * Component for rendering a DataStoreDropdownMenuAction as a Select dropdown.
 * This component fetches data from a VortexCrudDataStore and displays it in a dropdown.
 *
 * @param <ModelClass> The type of entity being displayed
 * @param <FieldType> The type used to identify fields
 * @param <RepositoryType> The type of repository key
 */
public class DataStoreDropdownMenuActionComponent<ModelClass, FieldType, RepositoryType> extends Select<Object> {

    private final DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType> action;
    private final Object currentEntity;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;

    public DataStoreDropdownMenuActionComponent(
            DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType> action,
            Object currentEntity,
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            RouteRenderer<ModelClass, FieldType, RepositoryType> route) {
        this.action = action;
        this.currentEntity = currentEntity;
        this.context = context;

        initializeComponent();
        loadData();
    }

    private void initializeComponent() {
        // Set label if provided
        if (action.label() != null && !action.label().isEmpty()) {
            setLabel(getTranslation(action.label()));
        }

        // Set placeholder if provided
        if (action.placeholder() != null && !action.placeholder().isEmpty()) {
            setPlaceholder(getTranslation(action.placeholder()));
        }

        // Set read-only state
        setReadOnly(action.readOnly());

        // Set required state
        setRequiredIndicatorVisible(action.required());

        // Set item label generator using the labelField
        if (action.labelField() != null) {
            setItemLabelGenerator(item -> {
                if (item == null) {
                    return "";
                }
                try {
                    Object value = context.reflectionService().getValue(item, action.labelField());
                    return value != null ? value.toString() : "";
                } catch (Exception e) {
                    return item.toString();
                }
            });
        } else {
            // Default to toString
            setItemLabelGenerator(item -> item != null ? item.toString() : "");
        }
    }

    private void loadData() {
        DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = action.dataStoreConfig();
        if (dataStoreConfig == null) {
            return;
        }

        VortexCrudDataStore<FieldType, ModelClass> dataStore = dataStoreConfig.dataStoreInstance();
        if (dataStore == null) {
            return;
        }

        try {
            List<ModelClass> items;

            // Check if we need to filter
            if (action.filterField() != null && action.filterValue() != null) {
                items = dataStore.getRecordsFromTableWhereColumnEquals(
                    action.filterField(),
                    action.filterValue(),
                    0,
                    action.limit()
                );
            } else {
                items = dataStore.getRecordsFromTable(0, action.limit());
            }

            // Set items in the select
            setItems(items);

        } catch (Exception e) {
            // Log error but don't fail completely
            e.printStackTrace();
        }
    }
}
