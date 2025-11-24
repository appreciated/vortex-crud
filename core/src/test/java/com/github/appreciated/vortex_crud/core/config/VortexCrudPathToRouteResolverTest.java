package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererMultipleChildren;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VortexCrudPathToRouteResolverTest {

    @Mock
    private VortexCrudRouteFactoryRegistry<Object, Object, Object> routeFactoryRegistry;
    @Mock
    private VortexCrudDataStoreUtilStrategy dataStoreUtil;

    private Map<String, RouteRenderer<Object, Object, Object>> routesConfig;

    abstract static class TestFactory1 implements VortexCrudRouteFactory<Object, Object, Object> {}
    abstract static class TestFactory2 implements VortexCrudRouteFactory<Object, Object, Object> {}
    abstract static class TestFactory3 implements VortexCrudRouteFactory<Object, Object, Object> {}

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        routesConfig = new HashMap<>();
    }

    @Test
    void testDetermineActiveRouteIndexNestedContainer() {
        // Setup: root -> child
        // Path: "root/child"
        String path = "root/child";

        RouteRendererMultipleChildren<Object, Object, Object> rootRoute = mock(RouteRendererMultipleChildren.class);
        doReturn(TestFactory1.class).when(rootRoute).factory();

        RouteRenderer<Object, Object, Object> childRoute = mock(RouteRenderer.class);
        doReturn(TestFactory2.class).when(childRoute).factory();

        Map<String, RouteRenderer<Object, Object, Object>> children = new HashMap<>();
        children.put("child", childRoute);
        when(rootRoute.childrenMap()).thenReturn(children);

        routesConfig.put("root", rootRoute);

        // Factories must be returned by registry
        VortexCrudRouteFactory<Object, Object, Object> rootFactory = mock(VortexCrudRouteFactory.class);
        when(rootFactory.isContainerRoute()).thenReturn(true);
        when(routeFactoryRegistry.getFactory(TestFactory1.class)).thenReturn(rootFactory);

        VortexCrudRouteFactory<Object, Object, Object> childFactory = mock(VortexCrudRouteFactory.class);
        when(childFactory.isContainerRoute()).thenReturn(false);
        when(routeFactoryRegistry.getFactory(TestFactory2.class)).thenReturn(childFactory);

        when(routeFactoryRegistry.isContainerRoute(rootRoute)).thenReturn(true);
        when(routeFactoryRegistry.isContainerRoute(childRoute)).thenReturn(false);

        VortexCrudPathToRouteResolver<Object, Object, Object> resolver = new VortexCrudPathToRouteResolver<>(
                routeFactoryRegistry, path, routesConfig, dataStoreUtil
        );

        // Logic check: Container followed by Non-Container -> returns Container index (0)
        assertEquals(0, resolver.determineActiveRouteIndex());
    }

    @Test
    void testDetermineActiveRouteIndexNestedContainerContainer() {
        // Setup: root (Container) -> middle (Container) -> leaf (Non-Container)
        // Path: "root/middle/leaf"
        String path = "root/middle/leaf";

        RouteRendererMultipleChildren<Object, Object, Object> rootRoute = mock(RouteRendererMultipleChildren.class);
        doReturn(TestFactory1.class).when(rootRoute).factory();

        RouteRendererMultipleChildren<Object, Object, Object> middleRoute = mock(RouteRendererMultipleChildren.class);
        doReturn(TestFactory2.class).when(middleRoute).factory();

        RouteRenderer<Object, Object, Object> leafRoute = mock(RouteRenderer.class);
        doReturn(TestFactory3.class).when(leafRoute).factory();

        Map<String, RouteRenderer<Object, Object, Object>> rootChildren = new HashMap<>();
        rootChildren.put("middle", middleRoute);
        when(rootRoute.childrenMap()).thenReturn(rootChildren);

        Map<String, RouteRenderer<Object, Object, Object>> middleChildren = new HashMap<>();
        middleChildren.put("leaf", leafRoute);
        when(middleRoute.childrenMap()).thenReturn(middleChildren);

        routesConfig.put("root", rootRoute);

        // Factories
        VortexCrudRouteFactory<Object, Object, Object> rootFactory = mock(VortexCrudRouteFactory.class);
        when(rootFactory.isContainerRoute()).thenReturn(true);
        when(routeFactoryRegistry.getFactory(TestFactory1.class)).thenReturn(rootFactory);

        VortexCrudRouteFactory<Object, Object, Object> middleFactory = mock(VortexCrudRouteFactory.class);
        when(middleFactory.isContainerRoute()).thenReturn(true);
        when(routeFactoryRegistry.getFactory(TestFactory2.class)).thenReturn(middleFactory);

        VortexCrudRouteFactory<Object, Object, Object> leafFactory = mock(VortexCrudRouteFactory.class);
        when(leafFactory.isContainerRoute()).thenReturn(false);
        when(routeFactoryRegistry.getFactory(TestFactory3.class)).thenReturn(leafFactory);

        when(routeFactoryRegistry.isContainerRoute(rootRoute)).thenReturn(true);
        when(routeFactoryRegistry.isContainerRoute(middleRoute)).thenReturn(true);
        when(routeFactoryRegistry.isContainerRoute(leafRoute)).thenReturn(false);

        VortexCrudPathToRouteResolver<Object, Object, Object> resolver = new VortexCrudPathToRouteResolver<>(
                routeFactoryRegistry, path, routesConfig, dataStoreUtil
        );

        // Expectation: Root (index 0) is returned, as it is the top-level container responsible for rendering the structure.
        assertEquals(0, resolver.determineActiveRouteIndex());
    }
}
