package com.github.appreciated.vortex_crud.core.ui.factories.route.calendar.component;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.CalendarConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
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
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VortexCrudDataStore<FieldType, Object> dataStore;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final FormCreator<ModelClass, FieldType, RepositoryType> formCreator;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver;

    private ConfigurableFilterDataProvider<Object, Void, String> dataProvider;
    private final FullCalendar calendar;
    private final Map<String, Object> entryToEntityMap = new HashMap<>();
    private final Map<String, Entry> entryMap = new HashMap<>();

    public CalendarView(RepositoryType dataStoreIdentifier,
                        RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                        VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                        VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                        DetailRouteSetting detailRouteSetting
    ) {
        this.routeRenderer = routeRenderer;
        this.context = context;
        this.dataStore = (VortexCrudDataStore<FieldType, Object>) routeRenderer.dataStoreInstance();
        this.fieldNameResolver = context.fieldNameResolver();
        this.formCreator = context.formCreator();
        this.reflectionService = context.reflectionService();
        this.dataStoreUtil = context.dataStoreUtil();
        this.routeResolver = routeResolver;
        this.calendarConfiguration = (CalendarConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration();

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

        Object titleValue = reflectionService.getValue(entity, calendarConfiguration.titleField());
        Object startValue = reflectionService.getValue(entity, calendarConfiguration.startDateField());

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
            Object endValue = reflectionService.getValue(entity, calendarConfiguration.endDateField());
            if (endValue != null) {
                Instant end = convertToInstant(endValue);
                if (end != null) {
                    entry.setEnd(end);
                }
            }
        }

        // Set all day if available
        if (calendarConfiguration.allDayField() != null) {
            Object allDayValue = reflectionService.getValue(entity, calendarConfiguration.allDayField());
            if (allDayValue instanceof Boolean) {
                entry.setAllDay((Boolean) allDayValue);
            }
        }

        // Set color if available
        if (calendarConfiguration.colorField() != null) {
            Object colorValue = reflectionService.getValue(entity, calendarConfiguration.colorField());
            if (colorValue != null) {
                entry.setColor(colorValue.toString());
            }
        }

        // Set description if available
        if (calendarConfiguration.descriptionField() != null) {
            Object descValue = reflectionService.getValue(entity, calendarConfiguration.descriptionField());
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
        RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> singleChildRenderer = (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer;
        if (singleChildRenderer.child() != null && singleChildRenderer.child().dialogFactory() != null) {
            Dialog dialog = singleChildRenderer.child().dialogFactory().create(
                    dataStoreUtil.getId(entity),
                    null,
                    null,
                    singleChildRenderer.child(),
                    null,
                    (VortexCrudDataStore<FieldType, ModelClass>) dataStore,
                    context,
                    () -> {
                        Object recordById = dataStore.getRecordById(dataStoreUtil.getId(entity));
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
        Object entity = new Object(); // Should be dataStore.newInstance() but keeping original logic if needed, but Dialog logic uses ID which requires entity.
        // Actually, FormDialogFactory checks if entityId is provided. If null, creates new instance.
        // So passing null as ID is correct for creation.

        RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> singleChildRenderer = (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer;

        if (singleChildRenderer.child() != null && singleChildRenderer.child().dialogFactory() != null) {
            Dialog dialog = singleChildRenderer.child().dialogFactory().create(
                    null,
                    null,
                    null,
                    singleChildRenderer.child(),
                    null,
                    (VortexCrudDataStore<FieldType, ModelClass>) dataStore,
                    context,
                    () -> {
                        // Refresh
                        refreshCalendar();
                    },
                    () -> {

                    });
            dialog.open();
        }
    }
}
