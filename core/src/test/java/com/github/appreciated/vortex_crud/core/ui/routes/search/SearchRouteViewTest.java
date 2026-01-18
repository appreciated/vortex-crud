package com.github.appreciated.vortex_crud.core.ui.routes.search;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchResult;
import com.github.appreciated.vortex_crud.core.config.model.SearchRoute;
import com.github.appreciated.vortex_crud.core.service.GlobalSearchService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.internal.UIInternals;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.RouteRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SearchRouteViewTest {

    @Mock
    private VortexCrudContext<Object, Object, Object> context;
    @Mock
    private SearchRoute<Object, Object, Object> searchRoute;
    @Mock
    private GlobalSearchService globalSearchService;
    @Mock
    private UI ui;
    @Mock
    private Page page;
    @Mock
    private History history;
    @Mock
    private VaadinSession session;
    @Mock
    private VaadinService vaadinService;
    @Mock
    private Router router;
    @Mock
    private RouteRegistry routeRegistry;

    private SearchRouteView<Object, Object, Object> view;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(context.globalSearchService()).thenReturn(globalSearchService);
        when(searchRoute.title()).thenReturn("Search Title");
        when(searchRoute.searchableRoutes()).thenReturn(Collections.emptyList());

        when(ui.getPage()).thenReturn(page);
        when(page.getHistory()).thenReturn(history);
        when(ui.getSession()).thenReturn(session);
        when(session.hasLock()).thenReturn(true);
        when(session.getService()).thenReturn(vaadinService);

        when(vaadinService.getRouter()).thenReturn(router);
        when(router.getRegistry()).thenReturn(routeRegistry);

        // Mock getTargetUrl for RouteRegistry
        when(routeRegistry.getTargetUrl(any(), any(RouteParameters.class))).thenReturn(Optional.of("mock/url"));
        // Also mock simple getTargetUrl just in case
        when(routeRegistry.getTargetUrl(any())).thenReturn(Optional.of("mock/url"));

        UI.setCurrent(ui);
        VaadinService.setCurrent(vaadinService);

        // Mock RouteRenderer to be castable to SearchRoute
        RouteRenderer<Object, Object, Object> routeRenderer = searchRoute;

        view = new SearchRouteView<>(context, routeRenderer);
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
        VaadinService.setCurrent(null);
    }

    @Test
    void testInitialization() {
        // Verify title
        Component titleComponent = view.getComponentAt(0);
        assertInstanceOf(H1.class, titleComponent);
        assertEquals("Search Title", ((H1) titleComponent).getText());

        // Verify Search Field
        Component searchFieldComponent = view.getComponentAt(1);
        assertInstanceOf(TextField.class, searchFieldComponent);
        TextField searchField = (TextField) searchFieldComponent;
        assertEquals("Search...", searchField.getPlaceholder());

        // Verify Results Container
        Component resultsContainer = view.getComponentAt(2);
        assertInstanceOf(VerticalLayout.class, resultsContainer);
    }

    @Test
    void testPerformSearch_NoResults() {
        TextField searchField = (TextField) view.getComponentAt(1);
        VerticalLayout resultsContainer = (VerticalLayout) view.getComponentAt(2);

        when(globalSearchService.search(any(), any())).thenReturn(Collections.emptyList());

        searchField.setValue("query");

        try {
            java.lang.reflect.Method method = SearchRouteView.class.getDeclaredMethod("performSearch", String.class);
            method.setAccessible(true);
            method.invoke(view, "query");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke performSearch: " + e.getMessage());
        }

        verify(globalSearchService).search(eq("query"), any());

        // Verify results container has "No results found."
        assertEquals(1, resultsContainer.getComponentCount());
        Component message = resultsContainer.getComponentAt(0);
        assertInstanceOf(Span.class, message);
        assertEquals("No results found.", ((Span) message).getText());

        // Verify URL update
        verify(history).replaceState(null, "?q=query");
    }

    @Test
    void testPerformSearch_WithResults() {
        VerticalLayout resultsContainer = (VerticalLayout) view.getComponentAt(2);

        // SearchResult(String title, String routePath, String routeTitle, Object id)
        SearchResult result1 = new SearchResult(
            "Item 1", "route/1", "Route 1", 1L
        );
        SearchResult result2 = new SearchResult(
            "Item 2", "route/1", "Route 1", 2L
        );
         SearchResult result3 = new SearchResult(
            "Item 3", "route/2", "Route 2", 3L
        );

        when(globalSearchService.search(any(), any())).thenReturn(List.of(result1, result2, result3));

        try {
            java.lang.reflect.Method method = SearchRouteView.class.getDeclaredMethod("performSearch", String.class);
            method.setAccessible(true);
            method.invoke(view, "test");
        } catch (Exception e) {
             e.printStackTrace();
             // Unwrap InvocationTargetException
             if (e.getCause() != null) {
                 fail("Failed to invoke performSearch: " + e.getCause());
             } else {
                 fail("Failed to invoke performSearch: " + e.getMessage());
             }
        }

        verify(globalSearchService).search(eq("test"), any());

        // We expect grouping by route title.
        assertEquals(2, resultsContainer.getComponentCount());

        VerticalLayout group1 = (VerticalLayout) resultsContainer.getComponentAt(0);
        // Header + Item 1 + Item 2
        assertEquals(3, group1.getComponentCount());
        assertInstanceOf(H4.class, group1.getComponentAt(0));

        VerticalLayout group2 = (VerticalLayout) resultsContainer.getComponentAt(1);
        // Header + Item 3
        assertEquals(2, group2.getComponentCount());
    }

    @Test
    void testCheckForQueryParameter() {
         // Mock UI internals
        UIInternals internals = mock(UIInternals.class);
        when(ui.getInternals()).thenReturn(internals);

        Location location = mock(Location.class);
        when(internals.getLastHandledLocation()).thenReturn(location);

        QueryParameters queryParameters = mock(QueryParameters.class);
        when(location.getQueryParameters()).thenReturn(queryParameters);

        Map<String, List<String>> params = Map.of("q", List.of("queryFromUrl"));
        when(queryParameters.getParameters()).thenReturn(params);

        try {
            java.lang.reflect.Method method = Component.class.getDeclaredMethod("onAttach", AttachEvent.class);
            method.setAccessible(true);
            method.invoke(view, new AttachEvent(view, true));
        } catch (Exception e) {
            // Try getting it from SearchRouteView if overridden there
            try {
                 java.lang.reflect.Method onAttach = SearchRouteView.class.getDeclaredMethod("onAttach", AttachEvent.class);
                 onAttach.setAccessible(true);
                 onAttach.invoke(view, new AttachEvent(view, true));
            } catch (Exception ex) {
                 // Try directly invoking checkForQueryParameter if onAttach fails/is weird with reflection
                 try {
                     java.lang.reflect.Method checkForQueryParameter = SearchRouteView.class.getDeclaredMethod("checkForQueryParameter");
                     checkForQueryParameter.setAccessible(true);
                     checkForQueryParameter.invoke(view);
                 } catch (Exception ex2) {
                     fail("Failed to invoke checkForQueryParameter: " + ex2.getMessage());
                 }
            }
        }

        TextField searchField = (TextField) view.getComponentAt(1);
        assertEquals("queryFromUrl", searchField.getValue());
        verify(globalSearchService).search(eq("queryFromUrl"), any());
    }
}
