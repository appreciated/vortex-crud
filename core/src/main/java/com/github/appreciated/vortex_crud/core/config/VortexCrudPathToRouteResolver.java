package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VortexCrudPathToRouteResolver<DataStoreId, FieldId> {

    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;
    private final String path;
    private String[] sections;
    private final Map<Integer, RouteRenderer<DataStoreId,FieldId>> pathRoutes;
    private final Map<String, RouteRenderer<DataStoreId,FieldId>> routesConfig;

    // Konstruktor
    public VortexCrudPathToRouteResolver(VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry, String path, Map<String, RouteRenderer<DataStoreId,FieldId>> routesConfig) {
        this.path = path;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.pathRoutes = new HashMap<>();
        this.routesConfig = routesConfig;
        splitPathAndAddMarkers();
    }

    // Methode zum Zerlegen des Pfades und Hinzufügen der Marker
    private void splitPathAndAddMarkers() {
        // Pfad in Abschnitte zerlegen
        this.sections = path.split("/");

        // Starte bei Root
        traverseRoutes(0, routesConfig);
    }

    private void traverseRoutes(int sectionIndex, Map<String, RouteRenderer<DataStoreId,FieldId>> currentRoutes) {
        if (sectionIndex >= sections.length) {
            return; // End of path segments
        }

        String section = sections[sectionIndex];

        // Check if the current route exists
        RouteRenderer<DataStoreId,FieldId> currentRouteRenderer = currentRoutes.get(section);

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
            traverseRoutes(sectionIndex + 1, currentRouteRenderer.getChildrenMap());
        } else {
            // If no children, continue to the next segment
            traverseRoutes(sectionIndex + 1, currentRoutes);
        }
    }

    public Map<Integer, RouteRenderer<DataStoreId,FieldId>> getPathRoutes() {
        return pathRoutes;
    }

    /**
     * This returns the to be rendered route.
     */
    public RouteRenderer<DataStoreId,FieldId> getCurrentRoute() {
        return pathRoutes.get(getCurrentIndex());
    }

    public String getPath() {
        return path;
    }

    public String getLastSegment() {
        return sections[sections.length - 1];
    }

    public String getPathForEntity(Integer currentPathIndex, Object entity) {
        return generateSubRoute(currentPathIndex, DataStoreUtil.getId(entity));
    }

    public RouteRenderer<DataStoreId,FieldId> getRouteForIndex(Integer currentPathIndex) {
        return pathRoutes.get(currentPathIndex);
    }

    public Integer getCurrentIndex() {
        List<Integer> numbers = pathRoutes.keySet().stream().toList().reversed();
        Integer currentPointer = numbers.getFirst();
        for (int i = 0; i < numbers.size() - 1; i++) {
            Integer currentKey = numbers.get(i);
            Integer nextKey = numbers.get(i + 1);

            RouteRenderer<DataStoreId,FieldId> currentRouteRenderer = pathRoutes.get(currentKey);
            RouteRenderer<DataStoreId,FieldId> nextRouteRenderer = pathRoutes.get(nextKey);

            VortexCrudRouteFactory currentFactory = routeFactoryRegistry.getFactory(currentRouteRenderer.getFactory());
            VortexCrudRouteFactory nextFactory = routeFactoryRegistry.getFactory(nextRouteRenderer.getFactory());

            if (currentFactory == null) {
                throw new IllegalStateException("The route does not have a factory set");
            }
            if (nextFactory == null) {
                throw new IllegalStateException("The route does not have a factory set");
            }

            boolean currentIsContainer = currentFactory.isContainerRoute();
            boolean nextIsContainer = nextFactory.isContainerRoute();

            // Wenn beide Container-Routen sind, gib die erste Route<DataStoreId> zurück
            if (currentIsContainer && nextIsContainer) {
                return nextKey;
            }

            // Wenn auf einen Container eine nicht-Container-Route folgt, gib den Container zurück
            if (currentIsContainer && !nextIsContainer) {
                return currentKey;
            }

            // Wenn auf einen Container eine nicht-Container-Route folgt, gib den Container zurück
            if (nextIsContainer) {
                currentPointer = nextKey;
            }
        }

        // Falls nur eine Route<DataStoreId> vorhanden ist oder keine der Bedingungen zutrifft, gib die erste Route<DataStoreId> zurück
        return currentPointer;
    }

    public boolean isLastIndex(Integer currentPathIndex) {
        return sections.length - 1 <= currentPathIndex;
    }

    public String generateSubRoute(Integer currentPathIndex, String route) {
        String[] array = Arrays.copyOfRange(sections, 0, currentPathIndex + 1);
        return Arrays.stream(array)
                .reduce((s, s2) -> s + "/" + s2).orElseThrow() + "/" + route;
    }

    public boolean hasPathForIndex(int i) {
        return sections.length >= i + 1;
    }

    public String getPathForIndex(int i) {
        return sections[i];
    }
}