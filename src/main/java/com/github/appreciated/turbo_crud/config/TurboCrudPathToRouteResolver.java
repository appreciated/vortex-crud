package com.github.appreciated.turbo_crud.config;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;

import java.util.*;


public class TurboCrudPathToRouteResolver {

    private final TurboCrudRouteFactoryRegistry routeFactoryRegistry;
    private String path;
    private String[] sections;
    private Map<Integer, Route> pathRoutes;
    private Map<String, Route> routesConfig;

    // Konstruktor
    public TurboCrudPathToRouteResolver(TurboCrudRouteFactoryRegistry routeFactoryRegistry, String path, Map<String, Route> routesConfig) {
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

    private void traverseRoutes(int sectionIndex, Map<String, Route> currentRoutes) {
        if (sectionIndex >= sections.length) {
            return; // End of path segments
        }

        String section = sections[sectionIndex];

        // Check if the current route exists
        Route currentRoute = currentRoutes.get(section);

        if (currentRoute == null && currentRoutes.containsKey("")) {
            pathRoutes.put(sectionIndex, currentRoute);
            currentRoute = currentRoutes.get("");
        } else if (routeFactoryRegistry.isContainerRoute(currentRoute)) {
            pathRoutes.put(sectionIndex, currentRoute);
        }

        if (currentRoute == null){
            throw new IllegalStateException("Current route is null");
        }

        pathRoutes.put(sectionIndex, currentRoute);

        // If this route has children, recurse into them
        if (currentRoute.getChildrenMap() != null && !currentRoute.getChildrenMap().isEmpty()) {
            traverseRoutes(sectionIndex + 1, currentRoute.getChildrenMap());
        } else {
            // If no children, continue to the next segment
            traverseRoutes(sectionIndex + 1, currentRoutes);
        }
    }

    public Map<Integer, Route> getPathRoutes() {
        return pathRoutes;
    }

    /**
     * This returns the to be rendered route.
     */
    public Route getCurrentRoute() {
        return pathRoutes.get(getCurrentIndex());
    }

    public Integer maxMapKey() {
        return pathRoutes.keySet().stream().max(Integer::compareTo).orElseThrow();
    }

    public String[] getSections() {
        return sections;
    }

    public String getPath() {
        return path;
    }

    public String getLastSegment() {
        return sections[sections.length - 1];
    }

    public boolean isLastPathIdentifier() {
        return maxMapKey() < sections.length - 1;
    }

    public String getPathForEntity(GenericEntity entity) {
        if (!isLastPathIdentifier()) {
            return getPath() + "/" + EntityUtil.getId(entity);
        } else {
            return getPath().substring(0, getPath().lastIndexOf("/")) + "/" + EntityUtil.getId(entity);
        }
    }

    public Route getRouteForIndex(Integer currentPathIndex) {
        return pathRoutes.get(currentPathIndex);
    }

    public Integer getCurrentIndex() {
        List<Integer> numbers = pathRoutes.keySet().stream().toList().reversed();

        for (int i = 0; i < numbers.size() - 1; i++) {
            Integer currentKey = numbers.get(i);
            Integer nextKey = numbers.get(i + 1);

            Route currentRoute = pathRoutes.get(currentKey);
            Route nextRoute = pathRoutes.get(nextKey);

            TurboCrudRouteFactory currentFactory = routeFactoryRegistry.getFactory(currentRoute.getFactory());
            TurboCrudRouteFactory nextFactory = routeFactoryRegistry.getFactory(nextRoute.getFactory());

            boolean currentIsContainer = currentFactory.isContainerRoute();
            boolean nextIsContainer = nextFactory.isContainerRoute();

            // Wenn beide Container-Routen sind, gib die erste Route zurück
            if (currentIsContainer && nextIsContainer) {
                return nextKey;
            }

            // Wenn auf einen Container eine nicht-Container-Route folgt, gib den Container zurück
            if (currentIsContainer && !nextIsContainer) {
                return currentKey;
            }
        }

        // Falls nur eine Route vorhanden ist oder keine der Bedingungen zutrifft, gib die erste Route zurück
        return numbers.get(0);
    }

    public boolean isLastIndex(Integer currentPathIndex) {
        return sections.length-1 <= currentPathIndex;
    }

    public String generateSubRoute(Integer currentPathIndex, String route) {
        String[] array = Arrays.copyOfRange(sections, 0, currentPathIndex + 1);
        return Arrays.stream(array)
                       .reduce((s, s2) -> s + "/" + s2).orElseThrow() + "/" + route;
    }
}