package com.github.appreciated.turbo_crud;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class TurboCrudPathToRouteResolverTest {

    @Mock
    private TurboCrudRouteFactoryRegistry registry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        TurboCrudRouteFactory containerFactory = mock(TurboCrudRouteFactory.class);
        when(containerFactory.isContainerRoute()).thenReturn(true);  // Wrapbare Routen
        when(registry.getFactory("container")).thenReturn(containerFactory);

        TurboCrudRouteFactory nonContainerFactory = mock(TurboCrudRouteFactory.class);
        when(nonContainerFactory.isContainerRoute()).thenReturn(false);  // Nicht-wrapbare Routen
        when(registry.getFactory("non-container")).thenReturn(nonContainerFactory);
    }

    @Test
    void testWrapableRouteReturnsFirstContainer() {
        // Testpfad mit wrapbaren Routen
        Map<String, Route> routesConfig = new HashMap<>();

        Route routeWithContainer = new Route("container");
        routeWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeWithContainer);

        Route childRoute = new Route("container");
        childRoute.setTitle("childRouteWithContainer");
        routeWithContainer.setChild(childRoute);

        String path = "routeWithContainer/routeWithContainer";
        TurboCrudPathToRouteResolver turboCrudPath = new TurboCrudPathToRouteResolver(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrapbare Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRoute.getTitle(), "Die erste wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrapableRouteReturnsLast() {
        // Testpfad mit zwei aufeinanderfolgenden nicht-wrapbaren Routen
        Map<String, Route> routesConfig = new HashMap<>();

        Route routeWithoutContainer1 = new Route("non-container");
        routeWithoutContainer1.setTitle("routeWithoutContainer1");
        routesConfig.put("routeWithoutContainer1", routeWithoutContainer1);

        Route routeWithoutContainer2 = new Route("non-container");
        routeWithoutContainer2.setTitle("routeWithoutContainer2");
        routeWithoutContainer1.setChild(routeWithoutContainer2);

        String path = "routeWithoutContainer1/routeWithoutContainer2";
        TurboCrudPathToRouteResolver turboCrudPath = new TurboCrudPathToRouteResolver(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die letzte nicht-wrapbare Route zurückgegeben wird
        assertEquals("routeWithoutContainer2", currentRoute.getTitle(), "Die letzte nicht-wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testMixedRoutes() {
        // Testpfad mit gemischten wrapbaren und nicht-wrapbaren Routen
        Map<String, Route> routesConfig = new HashMap<>();

        Route routeWithContainer = new Route("container");
        routeWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeWithContainer);

        Route firstChild = new Route("container");
        firstChild.setTitle("routeWithoutContainer1");
        routeWithContainer.setChild(firstChild);

        Route secondChild = new Route("non-container");
        secondChild.setTitle("routeWithoutContainer2");
        firstChild.setChild(secondChild);
        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        TurboCrudPathToRouteResolver turboCrudPath = new TurboCrudPathToRouteResolver(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrapbare Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRoute.getTitle(), "Die erste wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrapableRouteReturnsItself() {
        // Testpfad mit einer nicht-wrapbaren Route
        Map<String, Route> routesConfig = new HashMap<>();

        Route routeWithoutContainer1 = new Route("non-container");
        routeWithoutContainer1.setTitle("routeWithoutContainer1");
        routesConfig.put("routeWithoutContainer1", routeWithoutContainer1);

        String path = "routeWithoutContainer1";
        TurboCrudPathToRouteResolver turboCrudPath = new TurboCrudPathToRouteResolver(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die Route selbst zurückgegeben wird
        assertEquals("routeWithoutContainer1", currentRoute.getTitle(), "Die nicht-wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testValidPathWithMarkers() {
        // Testpfad mit gültigen Abschnitten
        Map<String, Route> routesConfig = new HashMap<>();

        Route routeWithContainer = new Route("container");
        routeWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeWithContainer);

        Route routeWithoutContainer1 = new Route("non-container");
        routeWithoutContainer1.setTitle("routeWithoutContainer1");
        routeWithContainer.setChild(routeWithoutContainer1);

        Route routeWithoutContainer2 = new Route("non-container");
        routeWithoutContainer2.setTitle("routeWithoutContainer2");
        routeWithoutContainer1.setChild(routeWithoutContainer2);

        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        TurboCrudPathToRouteResolver turboCrudPath = new TurboCrudPathToRouteResolver(registry, path, routesConfig);

        // Abrufen der gesetzten Marker
        Map<Integer, Route> routes = turboCrudPath.getPathRoutes();

        // Prüfung der Marker
        assertEquals(3, routes.size(), "Es sollten 3 Marker gesetzt sein.");
        assertEquals("routeWithContainer", routes.get(0).getTitle(), "Erster Marker sollte 'routeWithContainer' sein.");
        assertEquals("routeWithoutContainer1", routes.get(1).getTitle(), "Zweiter Marker sollte 'routeWithoutContainer1' sein.");
        assertEquals("routeWithoutContainer2", routes.get(2).getTitle(), "Dritter Marker sollte 'routeWithoutContainer2' sein.");
    }
}