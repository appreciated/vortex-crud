package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.data.provider.CallbackDataProvider;

public class GenericFilterableDataProvider<FieldId> extends CallbackDataProvider<Object, String> {
    public GenericFilterableDataProvider(VortexCrudDataStore<FieldId, ?> dataStore, FieldId filterField) {
        super(query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return (java.util.stream.Stream<Object>) dataStore.getRecordsFromTable(query.getOffset(), query.getLimit()).stream();
                    } else {
                        return (java.util.stream.Stream<Object>) dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream();
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