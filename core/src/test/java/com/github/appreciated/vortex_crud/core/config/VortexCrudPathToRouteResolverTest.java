package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VortexCrudPathToRouteResolverTest {

    private VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private Map<String, RouteRenderer<Object, String, Object>> routesConfig;
    private RouteRenderer<Object, String, Object> masterRoute;
    private RouteRenderer<Object, String, Object> childRoute;

    @BeforeEach
    public void setUp() {
        dataStoreUtil = mock(VortexCrudDataStoreUtilStrategy.class);

        masterRoute = mock(RouteRenderer.class, Mockito.withSettings().extraInterfaces(RouteRendererSingleChild.class));
        childRoute = mock(RouteRenderer.class);

        VortexCrudRouteFactory masterFactory = mock(VortexCrudRouteFactory.class);
        when(masterFactory.isContainerRoute()).thenReturn(true);
        when(masterRoute.factory()).thenReturn(masterFactory);

        VortexCrudRouteFactory childFactory = mock(VortexCrudRouteFactory.class);
        when(childFactory.isContainerRoute()).thenReturn(false);
        when(childRoute.factory()).thenReturn(childFactory);

        when(((RouteRendererSingleChild) masterRoute).child()).thenReturn(childRoute);

        routesConfig = Collections.singletonMap("master", masterRoute);
    }

    @Test
    public void testResolution() {
        VortexCrudPathToRouteResolver<Object, String, Object> resolver = new VortexCrudPathToRouteResolver<>(
                "master/123",
                routesConfig,
                dataStoreUtil
        );

        Assertions.assertEquals(masterRoute, resolver.getRouteForIndex(0));
        Assertions.assertEquals(childRoute, resolver.getRouteForIndex(1));
    }

    @Test
    public void testBuildPathUpToIndexAndSyntheticResolver() {
        // Initial resolver with path 'master'
        VortexCrudPathToRouteResolver<Object, String, Object> resolver = new VortexCrudPathToRouteResolver<>(
                "master",
                routesConfig,
                dataStoreUtil
        );

        Assertions.assertEquals(masterRoute, resolver.getRouteForIndex(0));

        // Build path up to index 0 + "create"
        String syntheticPath = resolver.buildPathUpToIndex(0, "create");
        Assertions.assertEquals("master/create", syntheticPath);

        // Create new resolver with synthetic path
        VortexCrudPathToRouteResolver<Object, String, Object> syntheticResolver = new VortexCrudPathToRouteResolver<>(
                syntheticPath,
                routesConfig,
                dataStoreUtil
        );

        Assertions.assertEquals(masterRoute, syntheticResolver.getRouteForIndex(0));
        Assertions.assertEquals(childRoute, syntheticResolver.getRouteForIndex(1));
    }
}
