package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.FormRouteProvider;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormRouteFactoryTest {

    @Test
    void testRenderRouteWithNullDataStoreConfigFallsBackToParent() {
        // Mock Context
        VortexCrudContext<Object, Object, Object> context = mock(VortexCrudContext.class);
        when(context.reflectionService()).thenReturn(mock(ReflectionService.class));
        when(context.formCreator()).thenReturn(mock(FormCreator.class));
        // when(context.rbacPermissionChecker()).thenReturn(mock(VortexCrudRbacPermissionChecker.class)); // Optional if logic handles null

        // Mock Route Resolver
        VortexCrudPathToRouteResolver routeResolver = mock(VortexCrudPathToRouteResolver.class);

        // Setup FormRoute (child) with null config
        FormRouteProvider<Object, Object, Object> childRoute = mock(FormRouteProvider.class);
        when(childRoute.dataStoreConfig()).thenReturn(null);
        when(childRoute.factory()).thenReturn(new FormRouteFactory<>());
        // Setup Parent Route with valid config
        RouteRenderer<Object, Object, Object> parentRoute = mock(RouteRenderer.class);
        DataStoreConfig<Object, Object, Object> dataStoreConfig = mock(DataStoreConfig.class);
        when(parentRoute.dataStoreConfig()).thenReturn(dataStoreConfig);
        when(dataStoreConfig.dataStoreInstance()).thenReturn(mock(VortexCrudDataStore.class));
        when(parentRoute.factory()).thenReturn(mock(com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory.class));

        int childIndex = 1;
        // Use doReturn to avoid wildcard capture issues
        doReturn(childRoute).when(routeResolver).getRouteForIndex(childIndex);
        when(routeResolver.determineActiveRouteIndex()).thenReturn(childIndex);
        doReturn(parentRoute).when(routeResolver).getRouteForIndex(0);

        // We need to mock getLastSegment for data loading unless creationMode is true
        when(routeResolver.getLastSegment()).thenReturn("some-id");

        // Mock DataStore behavior for creation mode
        VortexCrudDataStore<Object, Object> dataStore = mock(VortexCrudDataStore.class);
        when(dataStoreConfig.dataStoreInstance()).thenReturn(dataStore);
        when(dataStore.newInstance()).thenReturn(new Object());
        when(dataStore.getRecordById("some-id")).thenReturn(new Object());

        FormRouteFactory<Object, Object, Object> factory = new FormRouteFactory<>();

        // Execute renderRoute with creationMode=true to simplify data loading
        DetailRouteSetting setting = new DetailRouteSetting(false, false, true);

        try {
            Component component = factory.renderRoute(context, childIndex, routeResolver, setting);
            Assertions.assertNotNull(component);
        } catch (Exception e) {
             e.printStackTrace();
             Assertions.fail("Should not throw exception: " + e.getMessage());
        }

        // Verify fallback happened
        Mockito.verify(routeResolver).getRouteForIndex(0); // Parent index
        Mockito.verify(parentRoute).dataStoreConfig();
    }
}
