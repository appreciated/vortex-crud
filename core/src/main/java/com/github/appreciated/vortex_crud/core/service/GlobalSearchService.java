package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchResult;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GlobalSearchService<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker;

    public GlobalSearchService(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            ReflectionService<FieldType> reflectionService,
            @Autowired(required = false) VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker) {
        this.configService = configService;
        this.reflectionService = reflectionService;
        this.permissionChecker = permissionChecker;
    }

    public List<SearchResult> search(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        List<SearchResult> allResults = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes = (Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>>) (Map<?, ?>) configService.configuration().routes();

        routes.forEach((path, route) -> {
            if (route.dataStoreConfig() != null && route.filterField() != null) {
                // Check permissions
                if (permissionChecker != null && !permissionChecker.hasUserReadAccessToRoute(route)) {
                    return;
                }

                @SuppressWarnings("unchecked")
                VortexCrudQueryDataStore<FieldType, ModelClass> dataStore = route.dataStoreConfig().dataStoreInstance();

                if (dataStore != null) {
                    try {
                        List<ModelClass> results = dataStore.getRecordsFromTableWhereColumnLikeAndFiltersEqual(
                                route.filterField(),
                                query,
                                route.filters(),
                                0,
                                5 // Limit results per domain
                        );

                        for (ModelClass result : results) {
                            String title = getRecordTitle(route, result);
                            Object id = getRecordId(result);
                            if (id != null) {
                                allResults.add(new SearchResult(title, path, route.title(), id));
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error performing search for route: " + route.title(), e);
                    }
                }
            }
        });

        return allResults;
    }

    private String getRecordTitle(RouteRenderer<ModelClass, FieldType, RepositoryType> route, ModelClass result) {
        FieldType titleField = route.titleField();
        if (titleField != null) {
            Object value = reflectionService.getValue(result, titleField);
            if (value != null) {
                return value.toString();
            }
        }
        return "Item " + getRecordId(result);
    }

    private Object getRecordId(ModelClass result) {
        try {
            return reflectionService.getId(result);
        } catch (Exception e) {
            return null;
        }
    }
}
