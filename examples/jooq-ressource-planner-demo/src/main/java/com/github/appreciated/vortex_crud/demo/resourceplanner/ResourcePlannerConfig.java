package com.github.appreciated.vortex_crud.demo.resourceplanner;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CollectionConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.AppointmentRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.Tables.*;

@Service
public class ResourcePlannerConfig implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public ResourcePlannerConfig(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Hooks
        DataStoreHooks<AppointmentRecord> appointmentHooks = new DataStoreHooks<>();
        appointmentHooks.beforeCreates().add(this::availabilityCheck);
        appointmentHooks.beforeUpdates().add(this::availabilityCheck);

        // Data Stores
        JooqDataStore roomStore = new JooqDataStore(ROOM.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore personStore = new JooqDataStore(PERSON.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore typeStore = new JooqDataStore(APPOINTMENT_TYPE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore personTypeStore = new JooqDataStore(PERSON_APPOINTMENT_TYPE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore appointmentStore = new JooqDataStore(APPOINTMENT.getRecordType(), dsl, appointmentHooks);
        JooqDataStore usersStore = new JooqDataStore(USERS.getRecordType(), dsl, new DataStoreHooks<>());

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance((VortexCrudDataStore) usersStore)
                .fields(Map.of(
                        USERS.ID, JooqIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var roomConfig = JooqDataStoreConfig.of(ROOM)
                .dataStoreInstance((VortexCrudDataStore) roomStore)
                .fields(Map.of(
                        ROOM.ID, JooqIdField.builder().build(),
                        ROOM.NAME, JooqTextField.builder().required(true).build(),
                        ROOM.CAPACITY, JooqIntegerField.builder().build(),
                        ROOM.DESCRIPTION, JooqTextAreaField.builder().build(),
                        ROOM.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var personConfig = JooqDataStoreConfig.of(PERSON)
                .dataStoreInstance((VortexCrudDataStore) personStore)
                .fields(Map.of(
                        PERSON.ID, JooqIdField.builder().build(),
                        PERSON.NAME, JooqTextField.builder().required(true).build(),
                        PERSON.EMAIL, JooqEmailField.builder().build(),
                        PERSON.TITLE, JooqTextField.builder().build(),
                        PERSON.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var typeConfig = JooqDataStoreConfig.of(APPOINTMENT_TYPE)
                .dataStoreInstance((VortexCrudDataStore) typeStore)
                .fields(Map.of(
                        APPOINTMENT_TYPE.ID, JooqIdField.builder().build(),
                        APPOINTMENT_TYPE.NAME, JooqTextField.builder().required(true).build(),
                        APPOINTMENT_TYPE.DESCRIPTION, JooqTextAreaField.builder().build(),
                        APPOINTMENT_TYPE.DURATION_MINUTES, JooqIntegerField.builder().build(),
                        APPOINTMENT_TYPE.PRICE, JooqDoubleField.builder().build(),
                        APPOINTMENT_TYPE.REQUIRES_ROOM, JooqCheckboxField.builder().build(),
                        APPOINTMENT_TYPE.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var appointmentConfig = JooqDataStoreConfig.of(APPOINTMENT)
                .dataStoreInstance((VortexCrudDataStore) appointmentStore)
                .fields(Map.ofEntries(
                        Map.entry(APPOINTMENT.ID, JooqIdField.builder().build()),
                        Map.entry(APPOINTMENT.START_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.END_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.APPOINTMENT_TYPE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) typeStore).field(APPOINTMENT.APPOINTMENT_TYPE_ID).filterField(APPOINTMENT_TYPE.NAME).children(List.of(APPOINTMENT_TYPE.NAME)).required(true).build()),
                        Map.entry(APPOINTMENT.ROOM_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) roomStore).field(APPOINTMENT.ROOM_ID).filterField(ROOM.NAME).children(List.of(ROOM.NAME)).build()),
                        Map.entry(APPOINTMENT.PERSON_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) personStore).field(APPOINTMENT.PERSON_ID).filterField(PERSON.NAME).children(List.of(PERSON.NAME)).build()),
                        Map.entry(APPOINTMENT.CUSTOMER_NAME, JooqTextField.builder().build()),
                        Map.entry(APPOINTMENT.CUSTOMER_EMAIL, JooqTextField.builder().build()),
                        Map.entry(APPOINTMENT.STATUS, JooqTextField.builder().build()),
                        Map.entry(APPOINTMENT.CREATED_AT, JooqDateTimePickerField.builder().build())
                ))
                .build();

        // Forms
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> roomForm = JooqFormRoute.builder()
                .dataStoreConfig(roomConfig)
                .title("route.rooms.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(ROOM.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ROOM.NAME, "route.rooms.labels.name").build(),
                                JooqFieldElement.of(ROOM.CAPACITY, "route.rooms.labels.capacity").build(),
                                JooqFieldElement.of(ROOM.DESCRIPTION, "route.rooms.labels.description").build(),
                                JooqFieldElement.of(ROOM.IS_ACTIVE, "route.rooms.labels.active").build()))
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> personForm = JooqFormRoute.builder()
                .dataStoreConfig(personConfig)
                .title("route.persons.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(PERSON.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PERSON.NAME, "route.persons.labels.name").readOnly(true).build(),
                                JooqFieldElement.of(PERSON.EMAIL, "route.persons.labels.email").build(),
                                JooqFieldElement.of(PERSON.TITLE, "route.persons.labels.title").build(),
                                JooqFieldElement.of(PERSON.IS_ACTIVE, "route.persons.labels.active").build(),
                                JooqCollectionElement.of("route.persons.labels.services")
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(typeConfig)
                                                        .manyToMany(new JooqManyToMany(
                                                                PERSON_APPOINTMENT_TYPE.PERSON_ID,
                                                                PERSON_APPOINTMENT_TYPE.APPOINTMENT_TYPE_ID,
                                                                APPOINTMENT_TYPE.ID,
                                                                PERSON_APPOINTMENT_TYPE))
                                                        .children(List.of(APPOINTMENT_TYPE.NAME, APPOINTMENT_TYPE.DURATION_MINUTES))
                                                        .build())
                                                .emptyMessage("route.persons.labels.services-empty")
                                                .configuration(new CollectionConfig<TableField<?, ?>>(APPOINTMENT_TYPE.NAME))
                                                .build())
                                        .build()))
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> typeForm = JooqFormRoute.builder()
                .dataStoreConfig(typeConfig)
                .title("route.types.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(APPOINTMENT_TYPE.NAME)
                        .children(List.of(
                                JooqFieldElement.of(APPOINTMENT_TYPE.NAME, "route.types.labels.name").build(),
                                JooqFieldElement.of(APPOINTMENT_TYPE.DURATION_MINUTES, "route.types.labels.duration").build(),
                                JooqFieldElement.of(APPOINTMENT_TYPE.PRICE, "route.types.labels.price").build(),
                                JooqFieldElement.of(APPOINTMENT_TYPE.REQUIRES_ROOM, "route.types.labels.requires_room").build(),
                                JooqFieldElement.of(APPOINTMENT_TYPE.IS_ACTIVE, "route.types.labels.active").build()))
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> appointmentForm = JooqFormRoute.builder()
                .dataStoreConfig(appointmentConfig)
                .title("route.appointments.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(APPOINTMENT.CUSTOMER_NAME)
                        .children(List.of(
                                JooqFieldElement.of(APPOINTMENT.START_TIME, "route.appointments.labels.start").build(),
                                JooqFieldElement.of(APPOINTMENT.END_TIME, "route.appointments.labels.end").build(),
                                JooqFieldElement.of(APPOINTMENT.APPOINTMENT_TYPE_ID, "route.appointments.labels.type").build(),
                                JooqFieldElement.of(APPOINTMENT.ROOM_ID, "route.appointments.labels.room").build(),
                                JooqFieldElement.of(APPOINTMENT.PERSON_ID, "route.appointments.labels.person").build(),
                                JooqFieldElement.of(APPOINTMENT.CUSTOMER_NAME, "route.appointments.labels.customer").build(),
                                JooqFieldElement.of(APPOINTMENT.STATUS, "route.appointments.labels.status").build()))
                        .build())
                .build();

        // Routes
        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        routes.put("appointments", JooqCalendarRoute.builder()
                .isDefaultRoute(true)
                .dataStoreConfig(appointmentConfig)
                .iconFactory(VaadinIcon.CALENDAR::create)
                .title("route.appointments.title")
                .configuration(JooqCalendarConfiguration.builder()
                        .titleField(APPOINTMENT.CUSTOMER_NAME)
                        .startDateField(APPOINTMENT.START_TIME)
                        .endDateField(APPOINTMENT.END_TIME)
                        .build())
                .child(appointmentForm)
                .build());

        routes.put("rooms", JooqGridRoute.builder()
                .dataStoreConfig(roomConfig)
                .iconFactory(VaadinIcon.HOME::create)
                .title("route.rooms.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(ROOM.NAME)
                        .build())
                .child(roomForm)
                .build());

        routes.put("persons", JooqGridRoute.builder()
                .dataStoreConfig(personConfig)
                .iconFactory(VaadinIcon.USER::create)
                .title("route.persons.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(PERSON.NAME)
                        .build())
                .child(personForm)
                .build());

        routes.put("types", JooqGridRoute.builder()
                .dataStoreConfig(typeConfig)
                .iconFactory(VaadinIcon.LIST::create)
                .title("route.types.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(APPOINTMENT_TYPE.NAME)
                        .build())
                .child(typeForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("rp_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(usersConfig)
                        .availableRoles(com.github.appreciated.vortex_crud.core.config.model.Roles.builder().roles(List.of("admin", "user")).build())
                        .defaultReadRoles(List.of("user"))
                        .defaultWriteRoles(List.of("admin"))
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFieldElement.of(USERS.USERNAME, "route.users.labels.username").build())
                        .password(JooqFieldElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build())
                        .signUpFields(List.of())
                        .rolesField(null)
                        .build())
                .routes(routes)
                .build();
    }

    private void availabilityCheck(AppointmentRecord record) {
        if (record.getStatus() != null && record.getStatus().equals("CANCELLED")) {
            return;
        }

        LocalDateTime start = record.getStartTime();
        LocalDateTime end = record.getEndTime();
        Integer id = record.getId(); // Might be null for create

        if (start == null || end == null) {
            return; // Let validation handle this
        }

        // Room Check
        if (record.getRoomId() != null) {
            boolean roomBusy = dsl.fetchExists(dsl.selectFrom(APPOINTMENT)
                    .where(APPOINTMENT.ROOM_ID.eq(record.getRoomId()))
                    .and(APPOINTMENT.START_TIME.lessThan(end))
                    .and(APPOINTMENT.END_TIME.greaterThan(start))
                    .and(APPOINTMENT.STATUS.ne("CANCELLED"))
                    .and(id != null ? APPOINTMENT.ID.ne(id) : org.jooq.impl.DSL.noCondition())
            );
            if (roomBusy) {
                throw new RuntimeException("Room is not available for the selected time.");
            }
        }

        // Person Check
        if (record.getPersonId() != null) {
            boolean personBusy = dsl.fetchExists(dsl.selectFrom(APPOINTMENT)
                    .where(APPOINTMENT.PERSON_ID.eq(record.getPersonId()))
                    .and(APPOINTMENT.START_TIME.lessThan(end))
                    .and(APPOINTMENT.END_TIME.greaterThan(start))
                    .and(APPOINTMENT.STATUS.ne("CANCELLED"))
                    .and(id != null ? APPOINTMENT.ID.ne(id) : org.jooq.impl.DSL.noCondition())
            );
            if (personBusy) {
                throw new RuntimeException("Person is not available for the selected time.");
            }
        }
    }
}
