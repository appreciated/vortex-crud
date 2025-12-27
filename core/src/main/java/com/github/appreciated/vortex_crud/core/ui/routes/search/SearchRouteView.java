package com.github.appreciated.vortex_crud.core.ui.routes.search;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
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
import java.util.Optional;

@Slf4j
public class SearchRouteView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VerticalLayout resultsContainer;
    private final TextField searchField;

    public SearchRouteView(VortexCrudContext<ModelClass, FieldType, RepositoryType> context, RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer) {
        this.context = context;
        this.routeRenderer = routeRenderer;

        setPadding(true);
        setSpacing(true);
        setSizeFull();

        add(new H2(routeRenderer.title()));

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

        Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes = context.configService().configuration().routes();

        routes.forEach((path, route) -> {
            if (route.dataStoreConfig() != null && route.filterField() != null) {
                // Check if user has permission to read this route
                if (context.rbacPermissionChecker() != null && !context.rbacPermissionChecker().hasUserReadAccessToRoute(route)) {
                    return;
                }

                // The cast here is safe in the context of the configured application,
                // assuming the data store matches the application's generics.
                // However, mixed generics in a single application are not fully type-safe.
                @SuppressWarnings("unchecked")
                VortexCrudDataStore<FieldType, ModelClass> dataStore = (VortexCrudDataStore<FieldType, ModelClass>) route.dataStoreConfig().dataStoreInstance();

                if (dataStore != null) {
                    try {
                        List<ModelClass> results = dataStore.getRecordsFromTableWhereColumnLikeAndFiltersEqual(
                                route.filterField(),
                                searchValue,
                                route.filters(),
                                0,
                                5 // Limit results per domain
                        );

                        if (!results.isEmpty()) {
                            resultsContainer.add(createRouteResults(path, route, results));
                        }
                    } catch (Exception e) {
                        log.error("Error performing search for route: " + route.title(), e);
                    }
                }
            }
        });

        if (resultsContainer.getComponentCount() == 0) {
            resultsContainer.add(new Span("No results found."));
        }
    }

    private VerticalLayout createRouteResults(String routePath, RouteRenderer<ModelClass, FieldType, RepositoryType> route, List<ModelClass> results) {
        VerticalLayout routeLayout = new VerticalLayout();
        routeLayout.setPadding(false);
        routeLayout.setSpacing(false);

        H4 routeTitle = new H4(route.title());
        routeLayout.add(routeTitle);

        for (ModelClass result : results) {
            String recordTitle = getRecordTitle(route, result);
            Object recordId = getRecordId(result);

            if (recordId != null) {
                String fullPath = routePath + "/" + recordId.toString();
                // Ensure no leading slash if routePath already has one or if we are building a relative path
                if (fullPath.startsWith("/")) {
                    fullPath = fullPath.substring(1);
                }

                RouteParameters routeParameters = new RouteParameters("path", fullPath);

                RouterLink link = new RouterLink();
                link.setRoute(com.github.appreciated.vortex_crud.core.ui.routes.InternalDynamicRoute.class, routeParameters);
                link.setText(recordTitle);

                // Visual Path: Route Title > Record Title
                Span visualPath = new Span(route.title() + " > " + recordTitle);
                visualPath.getStyle().set("font-size", "0.8em").set("color", "gray");

                VerticalLayout itemLayout = new VerticalLayout(link, visualPath);
                itemLayout.setPadding(false);
                itemLayout.setSpacing(false);
                routeLayout.add(itemLayout);
            }
        }
        return routeLayout;
    }

    private String getRecordTitle(RouteRenderer<ModelClass, FieldType, RepositoryType> route, ModelClass result) {
        FieldType titleField = route.titleField();
        if (titleField != null) {
            Object value = context.reflectionService().getValue(result, titleField);
            if (value != null) {
                return value.toString();
            }
        }
        return "Item " + getRecordId(result);
    }

    private Object getRecordId(ModelClass result) {
        try {
            return context.reflectionService().getId(result);
        } catch (Exception e) {
            return null;
        }
    }
}
