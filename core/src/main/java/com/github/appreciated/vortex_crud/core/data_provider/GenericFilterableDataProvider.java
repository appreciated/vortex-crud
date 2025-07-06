package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.data.provider.CallbackDataProvider;

public class GenericFilterableDataProvider<FieldId, ModelClass> extends CallbackDataProvider<ModelClass, String> {
    public GenericFilterableDataProvider(VortexCrudDataStore<FieldId, ModelClass> dataStore, FieldId filterField) {
        super(query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return dataStore.getRecordsFromTable(query.getOffset(), query.getLimit()).stream();
                    } else {
                        return dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream();
                    }
                },
                query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return dataStore.count();
                    } else {
                        return dataStore.countWhereColumnLike(filterField, filterText);
                    }
                }
        );
    }
}
