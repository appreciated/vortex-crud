package com.github.appreciated.turbo_crud.config;

import com.github.appreciated.turbo_crud.config.model.Route;

import java.util.HashMap;
import java.util.Map;


public class TurboCrudPathSegments {

    private String path;
    private String[] sections;
    private Map<Integer, Route> pathRoutes;
    private Map<String, Route> routesConfig;

    // Konstruktor
    public TurboCrudPathSegments(String path, Map<String, Route> routesConfig) {
        this.path = path;
        this.pathRoutes = new HashMap<>();
        this.routesConfig = routesConfig;
        splitPathAndAddMarkers();
    }

    // Methode zum Zerlegen des Pfades und Hinzufügen der Marker
    private void splitPathAndAddMarkers() {
        // Pfad in Abschnitte zerlegen
        this.sections = path.split("/");

        // Starte bei Root
        traverseRoutes(1, routesConfig);
    }

    private void traverseRoutes(int sectionIndex, Map<String, Route> currentRoutes) {
        if (sectionIndex >= sections.length) {
            return; // End of path segments
        }

        String section = sections[sectionIndex];

        // Skip identifiers (even index positions should be identifiers)
        if (sectionIndex % 2 == 0) {
            traverseRoutes(sectionIndex + 1, currentRoutes); // Move to the next segment
            return;
        }

        // Check if the current route exists
        Route currentRoute = currentRoutes.get(section);
        if (currentRoute == null) {
            throw new IllegalArgumentException("No route found");
        }

        pathRoutes.put(sectionIndex, currentRoute);

        // If this route has children, recurse into them
        if (currentRoute.getChildren() != null && !currentRoute.getChildren().isEmpty()) {
            traverseRoutes(sectionIndex + 1, currentRoute.getChildren());
        } else {
            // If no children, continue to the next segment
            traverseRoutes(sectionIndex + 1, currentRoutes);
        }
    }

    public Map<Integer, Route> getPathRoutes() {
        return pathRoutes;
    }

    public Route getCurrentRoute() {
        return pathRoutes.get(maxPathSegmentIndex());
    }

    public Integer maxPathSegmentIndex() {
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
}