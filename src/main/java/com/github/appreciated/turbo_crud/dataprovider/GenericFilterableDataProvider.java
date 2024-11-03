package com.github.appreciated.turbo_crud.dataprovider;

import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerService;
import com.vaadin.flow.data.provider.CallbackDataProvider;

public class GenericFilterableDataProvider extends CallbackDataProvider<GenericEntity, String> {
    public GenericFilterableDataProvider(TurboCrudEntityManagerService entityManagerService, String filterField) {
        super(query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return entityManagerService.getRecordsFromTable(query.getOffset(), query.getLimit()).stream();
                    } else {
                        return entityManagerService.getRecordsFromTableWhereColumnLike(filterField, filterText, query.getOffset(), query.getLimit()).stream();
                    }
                },
                query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return entityManagerService.count();
                    } else {
                        return entityManagerService.countWhereColumnLike(filterField, filterText);
                    }
                }
        );
    }
}