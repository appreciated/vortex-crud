package com.github.appreciated.vortex_crud.test.jooq.ui.calendar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqDateTimePickerField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.CALENDAR_EVENTS;

@Service
public class JooqCalendarTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqCalendarTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(CALENDAR_EVENTS.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(CALENDAR_EVENTS)
                        .dataStoreInstance(store)
                        .fields(Map.of(
                                CALENDAR_EVENTS.ID, JooqNumericIdField.builder().build(),
                                CALENDAR_EVENTS.TITLE, JooqTextField.builder().build(),
                                CALENDAR_EVENTS.START_DATE, JooqDateTimePickerField.builder().build(),
                                CALENDAR_EVENTS.END_DATE, JooqDateTimePickerField.builder().build()
                        ))
                        .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> eventForm = JooqFormRoute.builder()
                .titleField(CALENDAR_EVENTS.TITLE)
                .fields(List.of(
                        JooqFormElement.of(CALENDAR_EVENTS.TITLE, "calendar-test.labels.title").build(),
                        JooqFormElement.of(CALENDAR_EVENTS.START_DATE, "calendar-test.labels.start").build(),
                        JooqFormElement.of(CALENDAR_EVENTS.END_DATE, "calendar-test.labels.end").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("calendar", JooqCalendarRoute.builder()
                .dataStoreConfig(config)
                .title("Calendar")
                .titleField(CALENDAR_EVENTS.TITLE)
                .startDateField(CALENDAR_EVENTS.START_DATE)
                .endDateField(CALENDAR_EVENTS.END_DATE)
                .searchField(CALENDAR_EVENTS.TITLE)
                .form(eventForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name.calendar-test")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
