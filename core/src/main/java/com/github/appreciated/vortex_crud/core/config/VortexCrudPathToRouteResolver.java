package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererMultipleChildren;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VortexCrudPathToRouteResolver {

    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final String path;
    private String[] sections;
    private final Map<Integer, RouteRenderer<?, ?, ?>> pathRoutes;
    private final Map<String, RouteRenderer<?, ?, ?>> routesConfig;
    private final Map<Integer, String> indexToRouteName;  // Maps segment index to route name
    private final Map<Integer, RouteIdContext> indexToContext;  // Maps segment index to accumulated context

    // Constructor
    public VortexCrudPathToRouteResolver(String path,
                                         Map<String, RouteRenderer<?, ?, ?>> routesConfig,
                                         VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.path = path;
        this.dataStoreUtil = dataStoreUtil;
        this.pathRoutes = new HashMap<>();
        this.routesConfig = routesConfig;
        this.indexToRouteName = new HashMap<>();
        this.indexToContext = new HashMap<>();
        splitPathAndInitializeRoutes();
    }

    // Method to split the path and initialize routes
    private void splitPathAndInitializeRoutes() {
        // Split path into sections
        this.sections = path.split("/");

        // Start at root with empty context
        buildRouteMapForPathSection(0, routesConfig, new RouteIdContext(), null);
    }

    private void buildRouteMapForPathSection(int sectionIndex, Map<String, RouteRenderer<?, ?, ?>> currentRoutes, RouteIdContext currentContext, String lastRouteName) {
        if (sectionIndex >= sections.length) {
            return; // End of path segments
        }

        String section = sections[sectionIndex];

        // Check if the current route exists
        RouteRenderer<?, ?, ?> currentRouteRenderer = currentRoutes.get(section);
        String nextRouteName = lastRouteName;
        RouteIdContext nextContext = currentContext;

        if (currentRouteRenderer == null && currentRoutes.containsKey(null)) {
            // This is an ID segment (wildcard match)
            currentRouteRenderer = currentRoutes.get(null);
            pathRoutes.put(sectionIndex, currentRouteRenderer);

            // Add the ID to context using the last route name
            if (lastRouteName != null) {
                nextContext = currentContext.withRouteId(lastRouteName, section);
            }
        } else if (currentRouteRenderer != null) {
            // This is a named route segment
            nextRouteName = section;
            indexToRouteName.put(sectionIndex, section);

            if (currentRouteRenderer.factory().isContainerRoute()) {
                pathRoutes.put(sectionIndex, currentRouteRenderer);
            }
        }

        // Store the context for this index AFTER we've determined if it's an ID segment
        // This ensures the context includes any ID that was just added
        indexToContext.put(sectionIndex, nextContext);

        if (currentRouteRenderer == null) {
            throw new IllegalStateException("Current route is null at section: " + section);
        }

        pathRoutes.put(sectionIndex, currentRouteRenderer);

        // If this route has children, recurse into them
        // Check RouteRendererMultipleChildren first
        boolean hasChildren = false;
        if (currentRouteRenderer instanceof RouteRendererMultipleChildren<?, ?, ?>) {
            @SuppressWarnings("unchecked")
            RouteRendererMultipleChildren<?, ?, ?> multi = (RouteRendererMultipleChildren<?, ?, ?>) currentRouteRenderer;
            try {
                @SuppressWarnings("unchecked")
                Map<String, RouteRenderer<?,?,?>> childRoutes = (Map<String, RouteRenderer<?,?,?>>) multi.routes();

                if (childRoutes != null && !childRoutes.isEmpty()) {
                    buildRouteMapForPathSection(sectionIndex + 1, childRoutes, nextContext, nextRouteName);
                    hasChildren = true;
                }
            } catch (ClassCastException e) {
                // Type mismatch in routes - ignore and try single child
                System.err.println("Warning: Type mismatch in routes for section: " + section);
            }
        }

        // Check RouteRendererSingleChild if no multiple children were found
        if (!hasChildren && currentRouteRenderer instanceof RouteRendererSingleChild<?, ?, ?>) {
            @SuppressWarnings("unchecked")
            RouteRendererSingleChild<?, ?, ?> single = (RouteRendererSingleChild<?, ?, ?>) currentRouteRenderer;

            if (single.form() != null) {
                // For single child routes, create a map with the child as a wildcard (null key)
                Map<String, RouteRenderer<?, ?, ?>> singleChildMap = new HashMap<>();
                singleChildMap.put(null, single.form());
                buildRouteMapForPathSection(sectionIndex + 1, singleChildMap, nextContext, nextRouteName);
                hasChildren = true;
            }
        }

        // If no children at all, continue to the next segment
        if (!hasChildren) {
            buildRouteMapForPathSection(sectionIndex + 1, currentRoutes, nextContext, nextRouteName);
        }
    }

    public Map<Integer, RouteRenderer<?, ?, ?>> getPathRoutes() {
        return pathRoutes;
    }

    /**
     * This returns the to be rendered route.
     */
    public RouteRenderer<?, ?, ?> getCurrentRoute() {
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

    public RouteRenderer<?, ?, ?> getRouteForIndex(Integer currentPathIndex) {
        return pathRoutes.get(currentPathIndex);
    }

    public Integer determineActiveRouteIndex() {
        List<Integer> numbers = pathRoutes.keySet().stream().toList().reversed();
        Integer currentPointer = numbers.getFirst();
        for (int i = 0; i < numbers.size() - 1; i++) {
            Integer currentKey = numbers.get(i);
            Integer nextKey = numbers.get(i + 1);

            RouteRenderer<?, ?, ?> currentRouteRenderer = pathRoutes.get(currentKey);
            RouteRenderer<?, ?, ?> nextRouteRenderer = pathRoutes.get(nextKey);

            VortexCrudRouteFactory<?, ?, ?> currentFactory = currentRouteRenderer.factory();
            VortexCrudRouteFactory<?, ?, ?> nextFactory = nextRouteRenderer.factory();

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

    /**
     * Get the route ID context at a specific path index.
     * This contains all parent entity IDs accumulated up to this point in the path.
     *
     * @param index the path segment index
     * @return the RouteIdContext at that index, or empty context if not found
     */
    public RouteIdContext getRouteIdContext(int index) {
        return indexToContext.getOrDefault(index, new RouteIdContext());
    }

    /**
     * Get the route name at a specific path index.
     *
     * @param index the path segment index
     * @return the route name, or null if this index is not a named route
     */
    public String getRouteNameForIndex(int index) {
        return indexToRouteName.get(index);
    }
}
