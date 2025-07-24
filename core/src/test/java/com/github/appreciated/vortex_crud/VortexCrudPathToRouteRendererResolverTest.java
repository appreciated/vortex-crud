package com.github.appreciated.vortex_crud;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
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
    private VortexCrudRouteFactoryRegistry registry;

    @Mock
    private VortexCrudDataStoreUtilStrategy dataStoreUtil;

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
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        RouteRenderer<String, String, String> routeRendererWithContainer = new RouteRenderer<>(TestContainerRouteFactory.class);
        routeRendererWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeRendererWithContainer);

        RouteRenderer<String, String, String> childRouteRenderer = new RouteRenderer<>(TestContainerRouteFactory.class);
        childRouteRenderer.setTitle("childRouteWithContainer");
        routeRendererWithContainer.setChild(childRouteRenderer);

        String path = "routeWithContainer/routeWithContainer";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                registry,
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrapbare Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRouteRenderer.getTitle(), "Die erste wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrapableRouteReturnsLast() {
        // Testpfad mit zwei aufeinanderfolgenden non-wrappable Routen
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        RouteRenderer<String, String, String> routeRendererWithoutContainer1 = new RouteRenderer<>(TestNonContainerRouteFactory.class);
        routeRendererWithoutContainer1.setTitle("routeWithoutContainer1");
        routesConfig.put("routeWithoutContainer1", routeRendererWithoutContainer1);

        RouteRenderer<String, String, String> routeRendererWithoutContainer2 = new RouteRenderer<>(TestNonContainerRouteFactory.class);
        routeRendererWithoutContainer2.setTitle("routeWithoutContainer2");
        routeRendererWithoutContainer1.setChild(routeRendererWithoutContainer2);

        String path = "routeWithoutContainer1/routeWithoutContainer2";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                registry,
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die letzte non-wrappable Route zurückgegeben wird
        assertEquals("routeWithoutContainer2", currentRouteRenderer.getTitle(), "Die letzte nicht-wrapbare Route sollte zurückgegeben werden.");
    }

    @Test
    void testMixedRoutes() {
        // Testpfad mit gemischten wrappable und non-wrappable Routen
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        RouteRenderer<String, String, String> routeRendererWithContainer = new RouteRenderer<>(TestContainerRouteFactory.class);
        routeRendererWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeRendererWithContainer);

        RouteRenderer<String, String, String> firstChild = new RouteRenderer<>(TestContainerRouteFactory.class);
        firstChild.setTitle("routeWithoutContainer1");
        routeRendererWithContainer.setChild(firstChild);

        RouteRenderer<String, String, String> secondChild = new RouteRenderer<>(TestNonContainerRouteFactory.class);
        secondChild.setTitle("routeWithoutContainer2");
        firstChild.setChild(secondChild);
        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                registry,
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die erste wrappable Route zurückgegeben wird
        assertEquals("routeWithContainer", currentRouteRenderer.getTitle(), "Die erste wrappable Route sollte zurückgegeben werden.");
    }

    @Test
    void testNonWrappableRouteReturnsItself() {
        // Testpfad mit einer non-wrappable Route
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        RouteRenderer<String, String, String> routeRendererWithoutContainer1 = new RouteRenderer<>(TestNonContainerRouteFactory.class);
        routeRendererWithoutContainer1.setTitle("routeWithoutContainer1");
        routesConfig.put("routeWithoutContainer1", routeRendererWithoutContainer1);

        String path = "routeWithoutContainer1";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                registry,
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der aktuellen Route
        RouteRenderer<String, String, String> currentRouteRenderer = vortexCrudPath.getCurrentRoute();

        // Prüfung, ob die Route selbst zurückgegeben wird
        assertEquals("routeWithoutContainer1", currentRouteRenderer.getTitle(), "Die nicht-wrappable Route sollte zurückgegeben werden.");
    }

    @Test
    void testValidPathWithMarkers() {
        // Testpfad mit gültigen Abschnitten
        Map<String, RouteRenderer<String, String, String>> routesConfig = new HashMap<>();

        RouteRenderer<String, String, String> routeRendererWithContainer = new RouteRenderer<>(TestContainerRouteFactory.class);
        routeRendererWithContainer.setTitle("routeWithContainer");
        routesConfig.put("routeWithContainer", routeRendererWithContainer);

        RouteRenderer<String, String, String> routeRendererWithoutContainer1 = new RouteRenderer<>(TestNonContainerRouteFactory.class);
        routeRendererWithoutContainer1.setTitle("routeWithoutContainer1");
        routeRendererWithContainer.setChild(routeRendererWithoutContainer1);

        RouteRenderer<String, String, String> routeRendererWithoutContainer2 = new RouteRenderer<>(TestNonContainerRouteFactory.class);
        routeRendererWithoutContainer2.setTitle("routeWithoutContainer2");
        routeRendererWithoutContainer1.setChild(routeRendererWithoutContainer2);

        String path = "routeWithContainer/routeWithoutContainer1/routeWithoutContainer2";
        VortexCrudPathToRouteResolver<String, String, String> vortexCrudPath = new VortexCrudPathToRouteResolver<>(
                registry,
                path,
                routesConfig,
                dataStoreUtil
        );

        // Abrufen der gesetzten Marker
        Map<Integer, RouteRenderer<String, String, String>> routes = vortexCrudPath.getPathRoutes();

        // Prüfung der Marker
        assertEquals(3, routes.size(), "Es sollten 3 Marker gesetzt sein.");
        assertEquals("routeWithContainer", routes.get(0).getTitle(), "Erster Marker sollte 'routeWithContainer' sein.");
        assertEquals("routeWithoutContainer1", routes.get(1).getTitle(), "Zweiter Marker sollte 'routeWithoutContainer1' sein.");
        assertEquals("routeWithoutContainer2", routes.get(2).getTitle(), "Dritter Marker sollte 'routeWithoutContainer2' sein.");
    }
}