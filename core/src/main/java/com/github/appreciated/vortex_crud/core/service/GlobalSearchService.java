package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchResult;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.vaadin.flow.data.provider.AbstractDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GlobalSearchService<ModelClass, FieldType, RepositoryType> extends AbstractDataProvider<SearchResult, String> {

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

    /**
     * Search across all routes with dataStoreConfig and filterField.
     */
    public List<SearchResult> search(String query) {
        return search(query, null);
    }

    /**
     * Search within specific routes.
     *
     * @param query             The search query
     * @param specificRoutes    Optional list of specific routes to search within.
     *                          If null or empty, all routes will be searched.
     * @return List of search results
     */
    public List<SearchResult> search(String query, List<RouteRenderer<ModelClass, FieldType, RepositoryType>> specificRoutes) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        List<SearchResult> allResults = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes = (Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>>) (Map<?, ?>) configService.configuration().routes();

        // If specific routes are provided, only search those
        if (specificRoutes != null && !specificRoutes.isEmpty()) {
            for (RouteRenderer<ModelClass, FieldType, RepositoryType> route : specificRoutes) {
                // Find the path for this route
                String routePath = findRoutePathInMap(routes, route);
                if (routePath != null) {
                    searchRoute(query, routePath, route, allResults);
                }
            }
        } else {
            // Search all routes
            routes.forEach((path, route) -> searchRoute(query, path, route, allResults));
        }

        return allResults;
    }

    /**
     * Find the path key for a given route in the routes map.
     */
    private String findRoutePathInMap(Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes, RouteRenderer<ModelClass, FieldType, RepositoryType> targetRoute) {
        for (Map.Entry<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> entry : routes.entrySet()) {
            if (entry.getValue() == targetRoute) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Search a single route and add results to the allResults list.
     */
    private void searchRoute(String query, String path, RouteRenderer<ModelClass, FieldType, RepositoryType> route, List<SearchResult> allResults) {
        if (route.dataStoreConfig() != null && route.filterField() != null) {
            // Check permissions
            if (permissionChecker != null && !permissionChecker.hasUserReadAccessToRoute(route)) {
                return;
            }

            @SuppressWarnings("unchecked")
            VortexCrudDataStore<FieldType, ModelClass> dataStore = route.dataStoreConfig().dataStoreInstance();

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
                    log.error("Error performing search for route: {}", route.title(), e);
                }
            }
        }
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

    // DataProvider interface implementation

    @Override
    public java.util.stream.Stream<SearchResult> fetch(com.vaadin.flow.data.provider.Query<SearchResult, String> query) {
        String filter = query.getFilter().orElse("");
        if (filter.isBlank()) {
            return java.util.stream.Stream.empty();
        }
        // Properly use query pagination parameters
        int offset = query.getOffset();
        int limit = query.getLimit();

        return search(filter).stream()
                .skip(offset)
                .limit(limit);
    }

    @Override
    public int size(com.vaadin.flow.data.provider.Query<SearchResult, String> query) {
        String filter = query.getFilter().orElse("");
        if (filter.isBlank()) {
            return 0;
        }
        return search(filter).size();
    }

    @Override
    public boolean isInMemory() {
        return false;
    }
}
