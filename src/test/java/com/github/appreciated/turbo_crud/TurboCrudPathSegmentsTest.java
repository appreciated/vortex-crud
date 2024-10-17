package com.github.appreciated.turbo_crud;

import com.github.appreciated.turbo_crud.config.TurboCrudPathSegments;
import com.github.appreciated.turbo_crud.config.model.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TurboCrudPathSegmentsTest {

    private Map<String, Route> routesConfig;

    @BeforeEach
    void setUp() {
        // Initialisiere die Route-Konfiguration vor jedem Test
        routesConfig = new HashMap<>();

        // Beispiel: Route-Objekte hinzufügen
        Route rootRoute = new Route();
        rootRoute.setTitle("rootRoute");
        Route childRoute1 = new Route();
        childRoute1.setTitle("childRoute1");
        Route childRoute2 = new Route();
        childRoute2.setTitle("childRoute2");

        // Kinder zur Root-Route hinzufügen
        Map<String, Route> children1 = new HashMap<>();
        children1.put("child1", childRoute1);
        rootRoute.setChildren(children1);

        Map<String, Route> children2 = new HashMap<>();
        children2.put("child2", childRoute2);
        childRoute1.setChildren(children2);

        // Root-Route in die Config setzen
        routesConfig.put("route", rootRoute);
    }

    @Test
    void testValidPathWithMarkers() {
        // Testpfad mit gültigen Abschnitten
        String path = "/route/identifier/child1/identifier/child2";
        TurboCrudPathSegments turboCrudPath = new TurboCrudPathSegments(path, routesConfig);

        // Abrufen der gesetzten Marker
        Map<Integer, Route> routes = turboCrudPath.getPathRoutes();

        // Prüfung der Marker
        assertEquals(3, routes.size(), "Es sollten 3 Marker gesetzt sein.");
        assertEquals("rootRoute", routes.get(1).getTitle(), "Erster Marker sollte 'route' sein.");
        assertEquals("childRoute1", routes.get(3).getTitle(), "Dritter Marker sollte 'child1' sein.");
        assertEquals("childRoute2", routes.get(5).getTitle(), "Vierter Marker sollte 'child2' sein.");
    }


    @Test
    void testValidPathWithMarkersOne() {
        // Testpfad mit gültigen Abschnitten
        String path = "route";
        TurboCrudPathSegments turboCrudPath = new TurboCrudPathSegments(path, routesConfig);

        // Abrufen der gesetzten Marker
        Map<Integer, Route> routes = turboCrudPath.getPathRoutes();

        // Prüfung der Marker
        assertEquals(1, routes.size(), "Es sollte 1 Marker gesetzt sein.");
        assertEquals("rootRoute", routes.get(1).getTitle(), "Erster Marker sollte 'route' sein.");
    }


    @Test
    void testPathWithInvalidRoute() {
        // Testpfad mit einem ungültigen Abschnitt
        String path = "/route/identifier/invalid/child2";

        Assertions.assertThrows( IllegalArgumentException.class, () -> new TurboCrudPathSegments(path, routesConfig));
    }

    @Test
    void testPathWithIdentifier() {
        // Testpfad mit einem Identifier an zweiter Stelle, der keinen Marker haben sollte
        String path = "/route/identifier/child1";
        TurboCrudPathSegments turboCrudPath = new TurboCrudPathSegments(path, routesConfig);

        // Abrufen der gesetzten Marker
        Map<Integer, Route> markers = turboCrudPath.getPathRoutes();

        // Der Identifier (zweite Position) sollte keinen Marker haben
        assertEquals(2, markers.size(), "Es sollten 2 Marker gesetzt sein.");
        assertEquals("rootRoute", markers.get(1).getTitle(), "Erster Marker sollte 'rootRoute' sein.");
        assertEquals("childRoute1", markers.get(3).getTitle(), "Dritter Marker sollte 'childRoute1' sein.");
        assertNull(markers.get(2), "Zweiter Abschnitt sollte kein Marker haben, da es ein Identifier ist.");
    }
}
