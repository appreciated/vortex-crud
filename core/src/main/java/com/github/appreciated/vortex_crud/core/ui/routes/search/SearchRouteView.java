package com.github.appreciated.vortex_crud.core.ui.routes.search;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchResult;
import com.github.appreciated.vortex_crud.core.config.model.SearchRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class SearchRouteView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final SearchRoute<ModelClass, FieldType, RepositoryType> searchRoute;
    private final VerticalLayout resultsContainer;
    private final TextField searchField;

    public SearchRouteView(VortexCrudContext<ModelClass, FieldType, RepositoryType> context, RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer) {
        this.context = context;
        this.searchRoute = (SearchRoute<ModelClass, FieldType, RepositoryType>) routeRenderer;

        setPadding(true);
        setSpacing(true);
        setSizeFull();

        add(new H1(searchRoute.title()));

        searchField = new TextField();
        searchField.setPlaceholder("Search...");
        searchField.setWidthFull();
        searchField.setClearButtonVisible(true);
        searchField.addKeyPressListener(Key.ENTER, event -> performSearch(searchField.getValue()));

        resultsContainer = new VerticalLayout();
        resultsContainer.setPadding(false);
        resultsContainer.setSpacing(true);

        add(searchField, resultsContainer);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        checkForQueryParameter();
    }

    private void checkForQueryParameter() {
        UI ui = UI.getCurrent();
        if (ui != null && ui.getInternals().getLastHandledLocation() != null) {
            Location location = ui.getInternals().getLastHandledLocation();
            QueryParameters queryParameters = location.getQueryParameters();
            Map<String, List<String>> parameters = queryParameters.getParameters();
            if (parameters.containsKey("q")) {
                String query = parameters.get("q").get(0);
                if (query != null && !query.isEmpty()) {
                    searchField.setValue(query);
                    performSearch(query);
                }
            }
        }
    }

    private void performSearch(String searchValue) {
        resultsContainer.removeAll();
        if (searchValue == null || searchValue.isBlank()) {
            return;
        }

        // Update URL to include query parameter so it's shareable/bookmarkable
        if (UI.getCurrent() != null) {
             UI.getCurrent().getPage().getHistory().replaceState(null, "?q=" + searchValue);
        }

        // Respect DataProvider contract by using query pagination parameters
        List<SearchResult> results = context.globalSearchService().search(searchValue, searchRoute.searchableRoutes()).stream()
                .skip(0)
                .limit(10).toList();

        if (results.isEmpty()) {
            resultsContainer.add(new Span("No results found."));
        } else {
            // The service returns a flat list; group results by route title for display

            String currentRouteTitle = null;
            VerticalLayout currentRouteLayout = null;

            for (SearchResult result : results) {
                if (!result.routeTitle().equals(currentRouteTitle)) {
                    currentRouteTitle = result.routeTitle();
                    currentRouteLayout = new VerticalLayout();
                    currentRouteLayout.setPadding(false);
                    currentRouteLayout.setSpacing(false);
                    currentRouteLayout.add(new H4(currentRouteTitle));
                    resultsContainer.add(currentRouteLayout);
                }

                if (currentRouteLayout != null) {
                    currentRouteLayout.add(createResultItem(result));
                }
            }
        }
    }

    private VerticalLayout createResultItem(SearchResult result) {
        String fullPath = result.routePath() + "/" + result.id().toString();
        // Ensure no leading slash if routePath already has one or if we are building a relative path
        if (fullPath.startsWith("/")) {
            fullPath = fullPath.substring(1);
        }

        RouteParameters routeParameters = new RouteParameters("path", fullPath);

        RouterLink link = new RouterLink();
        link.setRoute(com.github.appreciated.vortex_crud.core.ui.routes.InternalDynamicRoute.class, routeParameters);
        link.setText(result.title());

        // Visual Path: Route Title > Record Title
        Span visualPath = new Span(result.routeTitle() + " > " + result.title());
        visualPath.getStyle().set("font-size", "0.8em").set("color", "gray");

        VerticalLayout itemLayout = new VerticalLayout(link, visualPath);
        itemLayout.setPadding(false);
        itemLayout.setSpacing(false);
        return itemLayout;
    }
}
