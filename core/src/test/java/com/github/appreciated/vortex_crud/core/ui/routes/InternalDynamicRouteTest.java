package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.RouteParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InternalDynamicRouteTest {

    @Mock
    private VortexCrudConfigService<Object, Object, Object> configService;
    @Mock
    private VortexCrudRouteFactoryRegistry<Object, Object, Object> routeFactoryRegistry;
    @Mock
    private VortexCrudDataStoreUtilStrategy dataStoreUtil;
    @Mock
    private BeforeEnterEvent event;
    @Mock
    private Location location;
    @Mock
    private Application<Object, Object, Object> configuration;
    @Mock
    private VortexCrudRouteFactory<Object, Object, Object> routeFactory;

    private InternalDynamicRoute<Object, Object, Object> route;

    abstract static class TestFactory implements VortexCrudRouteFactory<Object, Object, Object> {}

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        route = new InternalDynamicRoute<>(configService, routeFactoryRegistry, dataStoreUtil);

        when(configService.configuration()).thenReturn(configuration);
        when(configuration.routes()).thenReturn(new HashMap<>());
        when(event.getLocation()).thenReturn(location);
        when(location.getFirstSegment()).thenReturn("root");
    }

    @Test
    void testBeforeEnterWithoutPath() {
        RouteParameters routeParameters = mock(RouteParameters.class);
        when(event.getRouteParameters()).thenReturn(routeParameters);
        when(routeParameters.get("path")).thenReturn(Optional.empty());

        Map<String, RouteRenderer<Object, Object, Object>> routes = new HashMap<>();
        RouteRenderer<Object, Object, Object> rootRoute = mock(RouteRenderer.class);
        doReturn(TestFactory.class).when(rootRoute).factory();
        routes.put("root", rootRoute);
        when(configuration.routes()).thenReturn(routes);

        when(routeFactoryRegistry.getFactory(TestFactory.class)).thenReturn(routeFactory);
        Component mockComponent = new Div();
        when(routeFactory.renderRoute(any(), any(), any())).thenReturn(mockComponent);

        when(routeFactoryRegistry.isContainerRoute(rootRoute)).thenReturn(false);

        route.beforeEnter(event);

        verify(routeFactory).renderRoute(eq(0), any(VortexCrudPathToRouteResolver.class), any(DetailRouteSetting.class));
        assertEquals(1, route.getChildren().count());
    }
}
