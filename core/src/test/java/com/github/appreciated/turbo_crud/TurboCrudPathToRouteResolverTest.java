package com.github.appreciated.turbo_crud;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.service.TestContainerRouteFactory;
import com.github.appreciated.turbo_crud.service.TestNonContainerRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
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

        TestContainerRouteFactory containerFactory = mock(TestContainerRouteFactory.class);
        when(containerFactory.isContainerRoute()).thenReturn(true);  // Wrappable Routen
        when(registry.getFactory(TestContainerRouteFactory.class)).thenReturn(containerFactory);

        TestNonContainerRouteFactory nonContainerFactory = mock(TestNonContainerRouteFactory.class);
        when(nonContainerFactory.isContainerRoute()).thenReturn(false);  // Non-wrappable Routen
        when(registry.getFactory(TestNonContainerRouteFactory.class)).thenReturn(nonContainerFactory);
    }

    @Test
    void testWrappableRouteReturnsFirstContainer() {
        // Testpfad mit wrappable Routen
        Map<String, Route<String, String>> routesConfig = new HashMap<>();

        Route<String, String> routeWithContainer = new Route<>(TestContainerRouteFactory.class);
        routeWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeWithContainer);

        Route<String, String> childRoute = new Route<>(TestContainerRouteFactory.class);
        childRoute.setTitle("childRouteWithContainer");
        routeWithContainer.setChild(childRoute);

        String path = "routeWithContainer/routeWithContainer";
        TurboCrudPathToRouteResolver<String, String> turboCrudPath = new TurboCrudPathToRouteResolver<>(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route<String, String> currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrapbare Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRoute.getTitle(), "Die erste wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrapableRouteReturnsLast() {
        // Testpfad mit zwei aufeinanderfolgenden non-wrappable Routen
        Map<String, Route<String, String>> routesConfig = new HashMap<>();

        Route<String, String> routeWithoutContainer1 = new Route<>(TestNonContainerRouteFactory.class);
        routeWithoutContainer1.setTitle("routeWithoutContainer1");
        routesConfig.put("routeWithoutContainer1", routeWithoutContainer1);

        Route<String, String> routeWithoutContainer2 = new Route<>(TestNonContainerRouteFactory.class);
        routeWithoutContainer2.setTitle("routeWithoutContainer2");
        routeWithoutContainer1.setChild(routeWithoutContainer2);

        String path = "routeWithoutContainer1/routeWithoutContainer2";
        TurboCrudPathToRouteResolver<String, String> turboCrudPath = new TurboCrudPathToRouteResolver<>(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route<String, String> currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die letzte non-wrappable Route zurückgegeben wird
        assertEquals("routeWithoutContainer2", currentRoute.getTitle(), "Die letzte nicht-wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testMixedRoutes() {
        // Testpfad mit gemischten wrappable und non-wrappable Routen
        Map<String, Route<String, String>> routesConfig = new HashMap<>();

        Route<String, String> routeWithContainer = new Route<>(TestContainerRouteFactory.class);
        routeWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeWithContainer);

        Route<String, String> firstChild = new Route<>(TestContainerRouteFactory.class);
        firstChild.setTitle("routeWithoutContainer1");
        routeWithContainer.setChild(firstChild);

        Route<String, String> secondChild = new Route<>(TestNonContainerRouteFactory.class);
        secondChild.setTitle("routeWithoutContainer2");
        firstChild.setChild(secondChild);
        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        TurboCrudPathToRouteResolver<String, String> turboCrudPath = new TurboCrudPathToRouteResolver<>(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route<String, String> currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrappable Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRoute.getTitle(), "Die erste wrappable Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrappableRouteReturnsItself() {
        // Testpfad mit einer non-wrappable Route
        Map<String, Route<String, String>> routesConfig = new HashMap<>();

        Route<String, String> routeWithoutContainer1 = new Route<>(TestNonContainerRouteFactory.class);
        routeWithoutContainer1.setTitle("routeWithoutContainer1");
        routesConfig.put("routeWithoutContainer1", routeWithoutContainer1);

        String path = "routeWithoutContainer1";
        TurboCrudPathToRouteResolver<String, String> turboCrudPath = new TurboCrudPathToRouteResolver<>(registry, path, routesConfig);

        // Abrufen der aktuellen Route
        Route<String, String> currentRoute = turboCrudPath.getCurrentRoute();

        // Prüfung, ob die Route selbst zurückgegeben wird
        assertEquals("routeWithoutContainer1", currentRoute.getTitle(), "Die nicht-wrappable Route sollte zurückgegeben werden.");
    }

    @Test
    void testValidPathWithMarkers() {
        // Testpfad mit gültigen Abschnitten
        Map<String, Route<String, String>> routesConfig = new HashMap<>();

        Route<String, String> routeWithContainer = new Route<>(TestContainerRouteFactory.class);
        routeWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeWithContainer);

        Route<String, String> routeWithoutContainer1 = new Route<>(TestNonContainerRouteFactory.class);
        routeWithoutContainer1.setTitle("routeWithoutContainer1");
        routeWithContainer.setChild(routeWithoutContainer1);

        Route<String, String> routeWithoutContainer2 = new Route<>(TestNonContainerRouteFactory.class);
        routeWithoutContainer2.setTitle("routeWithoutContainer2");
        routeWithoutContainer1.setChild(routeWithoutContainer2);

        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        TurboCrudPathToRouteResolver<String, String> turboCrudPath = new TurboCrudPathToRouteResolver<>(registry, path, routesConfig);

        // Abrufen der gesetzten Marker
        Map<Integer, Route<String, String>> routes = turboCrudPath.getPathRoutes();

        // Prüfung der Marker
        assertEquals(3, routes.size(), "Es sollten 3 Marker gesetzt sein.");
        assertEquals("routeWithContainer", routes.get(0).getTitle(), "Erster Marker sollte 'routeWithContainer' sein.");
        assertEquals("routeWithoutContainer1", routes.get(1).getTitle(), "Zweiter Marker sollte 'routeWithoutContainer1' sein.");
        assertEquals("routeWithoutContainer2", routes.get(2).getTitle(), "Dritter Marker sollte 'routeWithoutContainer2' sein.");
    }
}