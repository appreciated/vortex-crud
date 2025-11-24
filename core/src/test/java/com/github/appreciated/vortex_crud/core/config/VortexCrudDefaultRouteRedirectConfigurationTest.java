package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class VortexCrudDefaultRouteRedirectConfigurationTest {

    @Mock
    private VortexCrudConfigService<Object, Object, Object> configService;
    @Mock
    private Application<Object, Object, Object> configuration;
    @Mock
    private ServiceInitEvent event;
    @Mock
    private VaadinService vaadinService;

    private VortexCrudDefaultRouteRedirectConfiguration<Object, Object, Object> redirectConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(configService.configuration()).thenReturn(configuration);
        when(event.getSource()).thenReturn(vaadinService);
    }

    @Test
    void testServiceInitWithSingleDefaultRoute() {
        Map<String, RouteRenderer<Object, Object, Object>> routes = new HashMap<>();
        RouteRenderer<Object, Object, Object> defaultRoute = mock(RouteRenderer.class);
        when(defaultRoute.isDefaultRoute()).thenReturn(true);
        routes.put("default", defaultRoute);

        when(configuration.routes()).thenReturn(routes);

        try (MockedStatic<RouteConfiguration> routeConfigMock = mockStatic(RouteConfiguration.class)) {
            RouteConfiguration routeConfiguration = mock(RouteConfiguration.class);
            routeConfigMock.when(RouteConfiguration::forApplicationScope).thenReturn(routeConfiguration);

            redirectConfiguration = new VortexCrudDefaultRouteRedirectConfiguration<>(configService);
            redirectConfiguration.serviceInit(event);

            // Verify that addServiceDestroyListener is called, implying default route logic ran
            verify(vaadinService).addServiceDestroyListener(any());

            // Verify route set
            verify(routeConfiguration).setRoute(eq(""), eq(VortexCrudDefaultRouteRedirectConfiguration.VortexCrudDefaultRedirect.class));
        }
    }

    @Test
    void testServiceInitWithNoDefaultRoute() {
        Map<String, RouteRenderer<Object, Object, Object>> routes = new HashMap<>();
        RouteRenderer<Object, Object, Object> nonDefaultRoute = mock(RouteRenderer.class);
        when(nonDefaultRoute.isDefaultRoute()).thenReturn(false);
        routes.put("other", nonDefaultRoute);

        when(configuration.routes()).thenReturn(routes);

        redirectConfiguration = new VortexCrudDefaultRouteRedirectConfiguration<>(configService);
        redirectConfiguration.serviceInit(event);

        verify(vaadinService, never()).addServiceDestroyListener(any());
    }
}
