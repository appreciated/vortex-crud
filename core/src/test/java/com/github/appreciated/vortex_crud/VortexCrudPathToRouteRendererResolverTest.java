package com.github.appreciated.vortex_crud;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.service.TestContainerRouteFactory;
import com.github.appreciated.vortex_crud.service.TestNonContainerRouteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VortexCrudPathToRouteRendererResolverTest {

    @Mock
    private VortexCrudDataStoreUtilStrategy dataStoreUtil;

    private TestContainerRouteFactory containerFactory;
    private TestNonContainerRouteFactory nonContainerFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        containerFactory = mock(TestContainerRouteFactory.class);
        when(containerFactory.isContainerRoute()).thenReturn(true);  // Wrappable Routen

        nonContainerFactory = mock(TestNonContainerRouteFactory.class);
        when(nonContainerFactory.isContainerRoute()).thenReturn(false);  // Non-wrappable Routen
    }

    @Test
    void testWrappableRouteReturnsFirstContainer() {
        // Testpfad mit wrappable Routen
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        // Build child first
        ListRoute<String, String, String> childRouteRenderer = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .factoryInstance(containerFactory)
                .title("childRouteWithContainer")
                .build();

        // Build parent with child
        ListRoute<String, String, String> routeRendererWithContainer = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .factoryInstance(containerFactory)
                .title("routeWithContainer")
                .child(childRouteRenderer)
                .build();

        routesConfig.put("routeWithContainer", routeRendererWithContainer);

        String path = "routeWithContainer/routeWithContainer";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrapbare Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRouteRenderer.title(), "Die erste wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrappableRouteReturnsLast() {
        // Testpfad mit zwei aufeinanderfolgenden non-wrappable Routen
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        // Build child first
        ListRoute<String, String, String> routeRendererWithoutContainer2 = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .factoryInstance(nonContainerFactory)
                .title("routeWithoutContainer2")
                .build();

        // Build parent with child
        ListRoute<String, String, String> routeRendererWithoutContainer1 = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .factoryInstance(nonContainerFactory)
                .title("routeWithoutContainer1")
                .child(routeRendererWithoutContainer2)
                .build();

        routesConfig.put("routeWithoutContainer1", routeRendererWithoutContainer1);

        String path = "routeWithoutContainer1/routeWithoutContainer2";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die letzte non-wrappable Route zurückgegeben wird
        assertEquals("routeWithoutContainer2", currentRouteRenderer.title(), "Die letzte nicht-wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testMixedRoutes() {
        // Testpfad mit gemischten wrappable und non-wrappable Routen
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        // Build from innermost to outermost
        ListRoute<String, String, String> secondChild = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .factoryInstance(nonContainerFactory)
                .title("routeWithoutContainer2")
                .build();

        ListRoute<String, String, String> firstChild = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .factoryInstance(containerFactory)
                .title("routeWithoutContainer1")
                .child(secondChild)
                .build();

        ListRoute<String, String, String> routeRendererWithContainer = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .factoryInstance(containerFactory)
                .title("routeWithContainer")
                .child(firstChild)
                .build();

        routesConfig.put("routeWithContainer", routeRendererWithContainer);

        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                path,
                 routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrappable Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRouteRenderer.title(), "Die erste wrappable Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrappableRouteReturnsItself() {
        // Testpfad mit einer non-wrappable Route
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        ListRoute<String, String, String> routeRendererWithoutContainer1 = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .factoryInstance(nonContainerFactory)
                .title("routeWithoutContainer1")
                .build();

        routesConfig.put("routeWithoutContainer1", routeRendererWithoutContainer1);

        String path = "routeWithoutContainer1";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die Route selbst zurückgegeben wird
        assertEquals("routeWithoutContainer1", currentRouteRenderer.title(), "Die nicht-wrappable Route sollte zurückgegeben werden.");
    }

    @Test
    void testValidPathWithMarkers() {
        // Testpfad mit gültigen Abschnitten
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        // Build from innermost to outermost
        ListRoute<String, String, String> routeRendererWithoutContainer2 = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .factoryInstance(nonContainerFactory)
                .title("routeWithoutContainer2")
                .build();

        ListRoute<String, String, String> routeRendererWithoutContainer1 = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .factoryInstance(nonContainerFactory)
                .title("routeWithoutContainer1")
                .child(routeRendererWithoutContainer2)
                .build();

        ListRoute<String, String, String> routeRendererWithContainer = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .factoryInstance(containerFactory)
                .title("routeWithContainer")
                .child(routeRendererWithoutContainer1)
                .build();

        routesConfig.put("routeWithContainer", routeRendererWithContainer);

        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der gesetzten Marker
        Map<Integer, RouteRenderer<String, String, String>> routes = vortexCrudPath.getPathRoutes();

        // Prüfung der Marker
        assertEquals(3, routes.size(), "Es sollten 3 Marker gesetzt sein.");
        assertEquals("routeWithContainer", routes.get(0).title(), "Erster Marker sollte 'routeWithContainer' sein.");
        assertEquals("routeWithoutContainer1", routes.get(1).title(), "Zweiter Marker sollte 'routeWithoutContainer1' sein.");
        assertEquals("routeWithoutContainer2", routes.get(2).title(), "Dritter Marker sollte 'routeWithoutContainer2' sein.");
    }
}