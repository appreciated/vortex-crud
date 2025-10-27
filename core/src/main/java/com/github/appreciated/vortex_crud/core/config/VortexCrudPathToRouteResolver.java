package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final String path;
    private String[] sections;
    private final Map<Integer, RouteRenderer<ModelClass, FieldType, RepositoryType>> pathRoutes;
    private final Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routesConfig;

    // Constructor
    public VortexCrudPathToRouteResolver(VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry,
                                         String path,
                                         Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routesConfig,
                                         VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.path = path;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.dataStoreUtil = dataStoreUtil;
        this.pathRoutes = new HashMap<>();
        this.routesConfig = routesConfig;
        splitPathAndInitializeRoutes();
    }

    // Method to split the path and initialize routes
    private void splitPathAndInitializeRoutes() {
        // Split path into sections
        this.sections = path.split("/");

        // Start at root
        buildRouteMapForPathSection(0, routesConfig);
    }

    private void buildRouteMapForPathSection(int sectionIndex, Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> currentRoutes) {
        if (sectionIndex >= sections.length) {
            return; // End of path segments
        }

        String section = sections[sectionIndex];

        // Check if the current route exists
        RouteRenderer<ModelClass, FieldType, RepositoryType> currentRouteRenderer = currentRoutes.get(section);

        if (currentRouteRenderer == null && currentRoutes.containsKey(null)) {
            pathRoutes.put(sectionIndex, currentRouteRenderer);
            currentRouteRenderer = currentRoutes.get(null);
        } else if (routeFactoryRegistry.isContainerRoute(currentRouteRenderer)) {
            pathRoutes.put(sectionIndex, currentRouteRenderer);
        }

        if (currentRouteRenderer == null) {
            throw new IllegalStateException("Current route is null");
        }

        pathRoutes.put(sectionIndex, currentRouteRenderer);

        // If this route has children, recurse into them
        if (currentRouteRenderer.getChildrenMap() != null && !currentRouteRenderer.getChildrenMap().isEmpty()) {
            buildRouteMapForPathSection(sectionIndex + 1, currentRouteRenderer.getChildrenMap());
        } else {
            // If no children, continue to the next segment
            buildRouteMapForPathSection(sectionIndex + 1, currentRoutes);
        }
    }

    public Map<Integer, RouteRenderer<ModelClass, FieldType, RepositoryType>> getPathRoutes() {
        return pathRoutes;
    }

    /**
     * This returns the to be rendered route.
     */
    public RouteRenderer<ModelClass, FieldType, RepositoryType> getCurrentRoute() {
        return pathRoutes.get(determineActiveRouteIndex());
    }

    public String getPath() {
        return path;
    }

    public String getLastSegment() {
        return sections[sections.length - 1];
    }

    public String getPathForEntity(Integer currentPathIndex, Object entity) {
        return buildPathUpToIndex(currentPathIndex, dataStoreUtil.getId(entity));
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> getRouteForIndex(Integer currentPathIndex) {
        return pathRoutes.get(currentPathIndex);
    }

    public Integer determineActiveRouteIndex() {
        List<Integer> numbers = pathRoutes.keySet().stream().toList().reversed();
        Integer currentPointer = numbers.getFirst();
        for (int i = 0; i < numbers.size() - 1; i++) {
            Integer currentKey = numbers.get(i);
            Integer nextKey = numbers.get(i + 1);

            RouteRenderer<ModelClass, FieldType, RepositoryType> currentRouteRenderer = pathRoutes.get(currentKey);
            RouteRenderer<ModelClass, FieldType, RepositoryType> nextRouteRenderer = pathRoutes.get(nextKey);

            VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> currentFactory = routeFactoryRegistry.getFactory(currentRouteRenderer.getFactory());
            VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> nextFactory = routeFactoryRegistry.getFactory(nextRouteRenderer.getFactory());

            if (currentFactory == null) {
                throw new IllegalStateException("The route does not have a factory set");
            }
            if (nextFactory == null) {
                throw new IllegalStateException("The route does not have a factory set");
            }

            boolean currentIsContainer = currentFactory.isContainerRoute();
            boolean nextIsContainer = nextFactory.isContainerRoute();

            // If both are container routes, return the first route
            if (currentIsContainer && nextIsContainer) {
                return nextKey;
            }

            // If a container is followed by a non-container route, return the container
            if (currentIsContainer && !nextIsContainer) {
                return currentKey;
            }

            // If next is a container route, update the pointer
            if (nextIsContainer) {
                currentPointer = nextKey;
            }
        }

        // If only one route exists or none of the conditions apply, return the first route
        return currentPointer;
    }

    public boolean isLastIndex(Integer currentPathIndex) {
        return sections.length - 1 <= currentPathIndex;
    }

    public String buildPathUpToIndex(Integer currentPathIndex, String route) {
        String[] array = Arrays.copyOfRange(sections, 0, currentPathIndex + 1);
        String lastIndex = route != null ? "/" + route : "";
        return Arrays.stream(array)
                       .reduce((s, s2) -> s + "/" + s2).orElseThrow() + lastIndex;
    }

    public boolean hasPathForIndex(int i) {
        return sections.length >= i + 1;
    }

    public String getPathForIndex(int i) {
        return sections[i];
    }
}