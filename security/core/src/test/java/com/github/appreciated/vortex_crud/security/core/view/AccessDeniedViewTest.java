package com.github.appreciated.vortex_crud.security.core.view;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccessDeniedViewTest {

    @Test
    void testInstantiation() {
        AccessDeniedView view = new AccessDeniedView();
        assertNotNull(view);
    }

    @Test
    void testBeforeEnter() {
        AccessDeniedView view = new AccessDeniedView();

        BeforeEnterEvent event = mock(BeforeEnterEvent.class);
        Location location = mock(Location.class);
        QueryParameters queryParameters = mock(QueryParameters.class);

        when(event.getLocation()).thenReturn(location);
        when(location.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.getParameters()).thenReturn(Collections.emptyMap());

        view.beforeEnter(event);

        // No exception should be thrown
    }
}
