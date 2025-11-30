package com.github.appreciated.vortex_crud.core.ui.factories.route.calendar.component;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.CalendarConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class CalendarView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final CalendarConfiguration<ModelClass, FieldType, RepositoryType> calendarConfiguration;
    private final RepositoryType dataStoreIdentifier;
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VortexCrudDataStore<FieldType, Object> dataStore;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver;

    private ConfigurableFilterDataProvider<Object, Void, String> dataProvider;
    private final FullCalendar calendar;
    private final Map<String, Object> entryToEntityMap = new HashMap<>();
    private final Map<String, Entry> entryMap = new HashMap<>();

    public CalendarView(RepositoryType dataStoreIdentifier,
                        RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                        VortexCrudDataStore<FieldType, ?> dataStore,
                        CalendarConfiguration<ModelClass, FieldType, RepositoryType> calendarConfiguration,
                        VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                        DetailRouteSetting detailRouteSetting,
                        VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver
    ) {
        this.dataStoreIdentifier = dataStoreIdentifier;
        this.routeRenderer = routeRenderer;
        this.dataStore = (VortexCrudDataStore<FieldType, Object>) dataStore;
        this.context = context;
        this.routeResolver = routeResolver;
        this.calendarConfiguration = calendarConfiguration;

        dataProvider = new GenericFilterableDataProvider<>(this.dataStore, calendarConfiguration.filterField()).withConfigurableFilter();

        // Create the FullCalendar instance
        calendar = FullCalendarBuilder.create().build();
        calendar.setSizeFull();

        // Add click listeners
        calendar.addEntryClickedListener(event -> {
            Entry entry = event.getEntry();
            Object entity = entryToEntityMap.get(entry.getId());
            if (entity != null) {
                openDialog(entity);
            }
        });

        calendar.addTimeslotClickedListener(event -> {
            // When clicking on an empty timeslot, create a new entry
            onAdd(event.getDate().atStartOfDay());
        });

        // Load entries
        refreshCalendar();

        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(null),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));

        if (!detailRouteSetting.isHeaderHidden()) {
            add(headerBar, search);
        } else {
            add(search);
        }

        add(calendar);
        setSizeFull();
        setPadding(true);
    }

    private void refreshCalendar() {
        // Clear entry tracking maps
        entryToEntityMap.clear();
        entryMap.clear();

        // Fetch all entities and create entries
        Query<Object, Void> query = new Query<>(0, 10000, Collections.emptyList(), null, null);
        dataProvider.fetch(query).forEach(entity -> {
            Entry entry = createEntryFromEntity(entity);
            if (entry != null) {
                entryToEntityMap.put(entry.getId(), entity);
                entryMap.put(entry.getId(), entry);
            }
        });
    }

    private Entry createEntryFromEntity(Object entity) {
        if (calendarConfiguration.titleField() == null || calendarConfiguration.startDateField() == null) {
            return null;
        }

        Object titleValue = context.getReflectionService().getValue(entity, calendarConfiguration.titleField());
        Object startValue = context.getReflectionService().getValue(entity, calendarConfiguration.startDateField());

        if (titleValue == null || startValue == null) {
            return null;
        }

        String title = titleValue.toString();
        Instant start = convertToInstant(startValue);

        if (start == null) {
            return null;
        }

        Entry entry = new Entry();
        entry.setTitle(title);
        entry.setStart(start);

        // Set end date if available
        if (calendarConfiguration.endDateField() != null) {
            Object endValue = context.getReflectionService().getValue(entity, calendarConfiguration.endDateField());
            if (endValue != null) {
                Instant end = convertToInstant(endValue);
                if (end != null) {
                    entry.setEnd(end);
                }
            }
        }

        // Set all day if available
        if (calendarConfiguration.allDayField() != null) {
            Object allDayValue = context.getReflectionService().getValue(entity, calendarConfiguration.allDayField());
            if (allDayValue instanceof Boolean) {
                entry.setAllDay((Boolean) allDayValue);
            }
        }

        // Set color if available
        if (calendarConfiguration.colorField() != null) {
            Object colorValue = context.getReflectionService().getValue(entity, calendarConfiguration.colorField());
            if (colorValue != null) {
                entry.setColor(colorValue.toString());
            }
        }

        // Set description if available
        if (calendarConfiguration.descriptionField() != null) {
            Object descValue = context.getReflectionService().getValue(entity, calendarConfiguration.descriptionField());
            if (descValue != null) {
                entry.setDescription(descValue.toString());
            }
        }

        return entry;
    }

    private Instant convertToInstant(Object value) {
        if (value instanceof Instant) {
            return (Instant) value;
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant();
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant();
        } else if (value instanceof Date) {
            return ((Date) value).toInstant();
        }
        return null;
    }

    private void applyFilter(String filterText) {
        dataProvider.setFilter(filterText);
        refreshCalendar();
    }

    private void openDialog(Object entity) {
        Dialog dialog = context.getDialogFactoryRegistry().getFactory(((RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer).child().factory()).create(
                context,
                context.getDataStoreUtil().getId(entity),
                null,
                null,
                ((RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer).child(),
                null,
                (VortexCrudDataStore<FieldType, ModelClass>) dataStore,
                () -> {
                    Object recordById = dataStore.getRecordById(context.getDataStoreUtil().getId(entity));
                    this.dataStore.updateRecordById(recordById);
                    refreshCalendar();
                    String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), null);
                    Optional<UI> ui1 = getUI();
                    ui1.ifPresent(ui -> ui.navigate(nextRoute));
                },
                () -> {
                    String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), null);
                    Optional<UI> ui1 = getUI();
                    ui1.ifPresent(ui -> ui.navigate(nextRoute));
                });
        dialog.open();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        Integer currentIndex = routeResolver.determineActiveRouteIndex();
        int i = currentIndex + 1;
        if (routeResolver.hasPathForIndex(i)) {
            String pathForIndex = routeResolver.getPathForIndex(i);
            Object recordById = dataStore.getRecordById(pathForIndex);
            openDialog(recordById);
        }
    }

    private void onAdd(LocalDateTime defaultStart) {
        Object entity = new Object();
        Dialog dialog = context.getDialogFactoryRegistry().getFactory(((RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer).child().factory()).create(
                context,
                null,
                null,
                null,
                ((RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer).child(),
                null,
                (VortexCrudDataStore<FieldType, ModelClass>) dataStore,
                () -> {
                    Object recordById = this.dataStore.getRecordById(context.getDataStoreUtil().getId(entity));
                    this.dataStore.updateRecordById(recordById);
                    refreshCalendar();
                },
                () -> {

                });
        dialog.open();
    }
}
