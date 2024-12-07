package com.github.appreciated.turbo_crud.data_provider;

import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStore;
import com.vaadin.flow.data.provider.CallbackDataProvider;

public class GenericFilterableDataProvider extends CallbackDataProvider<GenericEntity, String> {
    public GenericFilterableDataProvider(TurboCrudDataStore dataStore, String filterField) {
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