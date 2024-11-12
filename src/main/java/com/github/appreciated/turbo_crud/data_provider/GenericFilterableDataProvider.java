package com.github.appreciated.turbo_crud.data_provider;

import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManager;
import com.vaadin.flow.data.provider.CallbackDataProvider;

public class GenericFilterableDataProvider extends CallbackDataProvider<GenericEntity, String> {
    public GenericFilterableDataProvider(TurboCrudEntityManager entityManager, String filterField) {
        super(query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return entityManager.getRecordsFromTable(query.getOffset(), query.getLimit()).stream();
                    } else {
                        return entityManager.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream();
                    }
                },
                query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return entityManager.count();
                    } else {
                        return entityManager.countWhereColumnLike(filterField, filterText);
                    }
                }
        );
    }
}