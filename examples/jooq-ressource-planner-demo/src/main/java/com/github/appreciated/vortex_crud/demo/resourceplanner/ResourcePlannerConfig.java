package com.github.appreciated.vortex_crud.demo.resourceplanner;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.AppointmentRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
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
        // Email Notification Hooks
        appointmentHooks.afterCreates().add(this::sendConfirmationEmail);
        appointmentHooks.afterUpdates().add(this::sendUpdateEmail);

        // Data Stores
        JooqDataStore roomStore = new JooqDataStore(ROOM.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore personStore = new JooqDataStore(PERSON.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore typeStore = new JooqDataStore(APPOINTMENT_TYPE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore personTypeStore = new JooqDataStore(PERSON_APPOINTMENT_TYPE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore appointmentStore = new JooqDataStore(APPOINTMENT.getRecordType(), dsl, appointmentHooks);
        JooqDataStore usersStore = new JooqDataStore(USERS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore customerStore = new JooqDataStore(CUSTOMER.getRecordType(), dsl, new DataStoreHooks<>());

        // Selects
        LinkedHashMap<String, String> recurrenceOptions = new LinkedHashMap<>();
        recurrenceOptions.put("DAILY", "Daily");
        recurrenceOptions.put("WEEKLY", "Weekly");
        recurrenceOptions.put("MONTHLY", "Monthly");
        Selects recurrenceSelects = Selects.builder()
                .configs(Map.of("recurrence_options", recurrenceOptions))
                .build();

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance((VortexCrudDataStore) usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var roomConfig = JooqDataStoreConfig.of(ROOM)
                .dataStoreInstance((VortexCrudDataStore) roomStore)
                .fields(Map.of(
                        ROOM.ID, JooqNumericIdField.builder().build(),
                        ROOM.NAME, JooqTextField.builder().required(true).build(),
                        ROOM.CAPACITY, JooqIntegerField.builder().build(),
                        ROOM.DESCRIPTION, JooqTextAreaField.builder().build(),
                        ROOM.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var personConfig = JooqDataStoreConfig.of(PERSON)
                .dataStoreInstance((VortexCrudDataStore) personStore)
                .fields(Map.of(
                        PERSON.ID, JooqNumericIdField.builder().build(),
                        PERSON.NAME, JooqTextField.builder().required(true).build(),
                        PERSON.EMAIL, JooqEmailField.builder().build(),
                        PERSON.TITLE, JooqTextField.builder().build(),
                        PERSON.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var customerConfig = JooqDataStoreConfig.of(CUSTOMER)
                .dataStoreInstance((VortexCrudDataStore) customerStore)
                .fields(Map.of(
                        CUSTOMER.ID, JooqNumericIdField.builder().build(),
                        CUSTOMER.NAME, JooqTextField.builder().required(true).build(),
                        CUSTOMER.EMAIL, JooqEmailField.builder().build(),
                        CUSTOMER.PHONE, JooqTextField.builder().build(),
                        CUSTOMER.ADDRESS, JooqTextAreaField.builder().build(),
                        CUSTOMER.IS_ACTIVE, JooqCheckboxField.builder().build(),
                        CUSTOMER.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var typeConfig = JooqDataStoreConfig.of(APPOINTMENT_TYPE)
                .dataStoreInstance((VortexCrudDataStore) typeStore)
                .fields(Map.of(
                        APPOINTMENT_TYPE.ID, JooqNumericIdField.builder().build(),
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
                        Map.entry(APPOINTMENT.ID, JooqNumericIdField.builder().build()),
                        Map.entry(APPOINTMENT.START_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.END_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.APPOINTMENT_TYPE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) typeStore).field(APPOINTMENT.APPOINTMENT_TYPE_ID).filterField(APPOINTMENT_TYPE.NAME).children(List.of(APPOINTMENT_TYPE.NAME)).required(true).build()),
                        Map.entry(APPOINTMENT.ROOM_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) roomStore).field(APPOINTMENT.ROOM_ID).filterField(ROOM.NAME).children(List.of(ROOM.NAME)).build()),
                        Map.entry(APPOINTMENT.PERSON_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) personStore).field(APPOINTMENT.PERSON_ID).filterField(PERSON.NAME).children(List.of(PERSON.NAME)).build()),
                        Map.entry(APPOINTMENT.CUSTOMER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) customerStore).field(APPOINTMENT.CUSTOMER_ID).filterField(CUSTOMER.NAME).children(List.of(CUSTOMER.NAME)).required(true).build()),
                        Map.entry(APPOINTMENT.RECURRENCE_RULE, JooqSelectField.builder().values("recurrence_options").build()),
                        Map.entry(APPOINTMENT.STATUS, JooqTextField.builder().build()),
                        Map.entry(APPOINTMENT.CREATED_AT, JooqDateTimePickerField.builder().build())
                ))
                .build();

        // Forms
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> roomForm = JooqFormRoute.builder()
                .dataStoreConfig(roomConfig)
                .title("route.rooms.title")
                .titleField(ROOM.NAME)
                .children(List.of(
                        JooqFieldElement.of(ROOM.NAME, "route.rooms.labels.name").build(),
                        JooqFieldElement.of(ROOM.CAPACITY, "route.rooms.labels.capacity").build(),
                        JooqFieldElement.of(ROOM.DESCRIPTION, "route.rooms.labels.description").build(),
                        JooqFieldElement.of(ROOM.IS_ACTIVE, "route.rooms.labels.active").build()))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> personForm = JooqFormRoute.builder()
                .dataStoreConfig(personConfig)
                .title("route.persons.title")
                .titleField(PERSON.NAME)
                .children(List.of(
                        JooqFieldElement.of(PERSON.NAME, "route.persons.labels.name").readOnly(true).build(),
                        JooqFieldElement.of(PERSON.EMAIL, "route.persons.labels.email").build(),
                        JooqFieldElement.of(PERSON.TITLE, "route.persons.labels.title").build(),
                        JooqFieldElement.of(PERSON.IS_ACTIVE, "route.persons.labels.active").build(),
                        JooqCollectionElement.of("route.persons.labels.services")
                                .factory(new ListCollectionFactory())
                                .dialogFactory(new ConnectDialogFactory())
                                .dataStoreConfig(typeConfig)
                                .manyToMany(new JooqManyToMany(
                                        PERSON_APPOINTMENT_TYPE.PERSON_ID,
                                        PERSON_APPOINTMENT_TYPE.APPOINTMENT_TYPE_ID,
                                        APPOINTMENT_TYPE.ID,
                                        PERSON_APPOINTMENT_TYPE))
                                .children(List.of(APPOINTMENT_TYPE.NAME, APPOINTMENT_TYPE.DURATION_MINUTES))
                                .emptyMessage("route.persons.labels.services-empty")
                                .titleField(APPOINTMENT_TYPE.NAME)
                                .build()))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> customerForm = JooqFormRoute.builder()
                .dataStoreConfig(customerConfig)
                .title("route.customers.title")
                .titleField(CUSTOMER.NAME)
                .children(List.of(
                        JooqFieldElement.of(CUSTOMER.NAME, "route.customers.labels.name").build(),
                        JooqFieldElement.of(CUSTOMER.EMAIL, "route.customers.labels.email").build(),
                        JooqFieldElement.of(CUSTOMER.PHONE, "route.customers.labels.phone").build(),
                        JooqFieldElement.of(CUSTOMER.ADDRESS, "route.customers.labels.address").build(),
                        JooqFieldElement.of(CUSTOMER.IS_ACTIVE, "route.customers.labels.active").build()))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> typeForm = JooqFormRoute.builder()
                .dataStoreConfig(typeConfig)
                .title("route.types.title")
                .titleField(APPOINTMENT_TYPE.NAME)
                .children(List.of(
                        JooqFieldElement.of(APPOINTMENT_TYPE.NAME, "route.types.labels.name").build(),
                        JooqFieldElement.of(APPOINTMENT_TYPE.DURATION_MINUTES, "route.types.labels.duration").build(),
                        JooqFieldElement.of(APPOINTMENT_TYPE.PRICE, "route.types.labels.price").build(),
                        JooqFieldElement.of(APPOINTMENT_TYPE.REQUIRES_ROOM, "route.types.labels.requires_room").build(),
                        JooqFieldElement.of(APPOINTMENT_TYPE.IS_ACTIVE, "route.types.labels.active").build()))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> appointmentForm = JooqFormRoute.builder()
                .dataStoreConfig(appointmentConfig)
                .title("route.appointments.title")
                .titleField(APPOINTMENT.CUSTOMER_ID) // Using Customer ID (Reference) as title, which resolves to Name via ReferenceField config
                .children(List.of(
                        JooqFieldElement.of(APPOINTMENT.START_TIME, "route.appointments.labels.start").build(),
                        JooqFieldElement.of(APPOINTMENT.END_TIME, "route.appointments.labels.end").build(),
                        JooqFieldElement.of(APPOINTMENT.APPOINTMENT_TYPE_ID, "route.appointments.labels.type").build(),
                        JooqFieldElement.of(APPOINTMENT.ROOM_ID, "route.appointments.labels.room").build(),
                        JooqFieldElement.of(APPOINTMENT.PERSON_ID, "route.appointments.labels.person").build(),
                        JooqFieldElement.of(APPOINTMENT.CUSTOMER_ID, "route.appointments.labels.customer").build(),
                        JooqFieldElement.of(APPOINTMENT.RECURRENCE_RULE, "route.appointments.labels.recurrence").build(),
                        JooqFieldElement.of(APPOINTMENT.STATUS, "route.appointments.labels.status").build()))
                .build();

        // Resource View (Master-Detail)
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> roomCalendarForm = JooqCalendarRoute.builder()
                .dataStoreConfig(appointmentConfig)
                .iconFactory(VaadinIcon.CALENDAR::create)
                .title("route.resource_view.calendar_title")
                .titleField(APPOINTMENT.CUSTOMER_ID)
                .startDateField(APPOINTMENT.START_TIME)
                .endDateField(APPOINTMENT.END_TIME)
                .form(appointmentForm) // Use shared appointment form
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> resourceView = JooqMasterDetailRoute.builder()
                .dataStoreConfig(roomConfig)
                .iconFactory(VaadinIcon.DOCTOR::create) // Or any suitable icon
                .title("route.resource_view.title")
                .titleField(ROOM.NAME)
                // Need a way to filter appointments by selected room. MasterDetailRoute usually links via ID.
                // However, standard MasterDetailRoute in Vortex displays a FORM for the selected item, not a Calendar of children.
                // The prompt asked for "Split view by Resource" or "Gantt".
                // Since I cannot implement a Gantt view without a new component, and CalendarRoute is for ALL appointments.
                // I will configure this Master Detail to show Room details, and maybe I can't easily embed a calendar filtered by it without a Custom Route.
                // But wait, MasterDetailRoute has a `form` property. If I set that to a CalendarRoute...
                // The `CalendarRoute` usually fetches ALL data from its dataStore.
                // If I want to filter it by the parent Room, I would need context-aware filtering which might not be out-of-the-box.
                // Let's stick to the prompt's request: "Resource View: The calendar view is basic. A split view by Resource (Room/Person) or a Gantt-style timeline is missing."
                // I will add a MasterDetail route where Master is Room, and Detail is a Grid of Appointments for that room (using standard OneToMany collection logic if possible, or just a grid).
                // Actually, `JooqCalendarRoute` doesn't support "filtering by parent" easily in configuration.
                // Let's implement a `JooqMasterDetailRoute` for Rooms, and the "form" will be the Room Form, but I will add a CollectionElement to the Room Form that shows Appointments?
                // No, that's inside the form.
                // Let's try to add the MasterDetailRoute as requested in plan.
                .form(roomForm) // This shows the Room details. To see appointments, I should add a collection to the Room form.
                .build();

        // Let's enhance Room Form to show appointments (as a poor man's resource view)
         RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> roomFormWithAppointments = JooqFormRoute.builder()
                .dataStoreConfig(roomConfig)
                .title("route.rooms.title")
                .titleField(ROOM.NAME)
                .children(List.of(
                        JooqFieldElement.of(ROOM.NAME, "route.rooms.labels.name").build(),
                        JooqFieldElement.of(ROOM.CAPACITY, "route.rooms.labels.capacity").build(),
                        JooqFieldElement.of(ROOM.DESCRIPTION, "route.rooms.labels.description").build(),
                        JooqFieldElement.of(ROOM.IS_ACTIVE, "route.rooms.labels.active").build(),
                         // Add list of appointments for this room
                        JooqCollectionElement.of("route.rooms.labels.appointments")
                                .factory(new ListCollectionFactory()) // Or GridCollectionFactory if available? ListCollectionFactory is standard.
                                .dialogFactory(new ConnectDialogFactory())
                                .dataStoreConfig(appointmentConfig)
                                .oneToMany(new JooqOneToMany(APPOINTMENT.ROOM_ID)) // Link appointment.room_id to room.id
                                .children(List.of(APPOINTMENT.START_TIME, APPOINTMENT.END_TIME, APPOINTMENT.CUSTOMER_ID))
                                .titleField(APPOINTMENT.START_TIME)
                                .build()
                        ))
                .build();
         // Update resources view to use this enhanced form
         // Actually, let's just use this enhanced form for the "Resources" route.

        // Routes
        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        routes.put("appointments", JooqCalendarRoute.builder()
                .isDefaultRoute(true)
                .dataStoreConfig(appointmentConfig)
                .iconFactory(VaadinIcon.CALENDAR::create)
                .title("route.appointments.title")
                .titleField(APPOINTMENT.CUSTOMER_ID)
                .startDateField(APPOINTMENT.START_TIME)
                .endDateField(APPOINTMENT.END_TIME)
                .form(appointmentForm)
                .build());

        routes.put("rooms", JooqGridRoute.builder()
                .dataStoreConfig(roomConfig)
                .iconFactory(VaadinIcon.HOME::create)
                .title("route.rooms.title")
                .titleField(ROOM.NAME)
                .form(roomFormWithAppointments) // Use enhanced form here
                .build());

        routes.put("resources", JooqMasterDetailRoute.builder() // Explicit "Resource View"
                 .dataStoreConfig(roomConfig)
                 .iconFactory(VaadinIcon.DOCTOR::create)
                 .title("route.resource_view.title")
                 .titleField(ROOM.NAME)
                 .form(roomFormWithAppointments)
                 .build());

        routes.put("persons", JooqGridRoute.builder()
                .dataStoreConfig(personConfig)
                .iconFactory(VaadinIcon.USER::create)
                .title("route.persons.title")
                .titleField(PERSON.NAME)
                .form(personForm)
                .build());

        routes.put("customers", JooqGridRoute.builder()
                .dataStoreConfig(customerConfig)
                .iconFactory(VaadinIcon.USERS::create)
                .title("route.customers.title")
                .titleField(CUSTOMER.NAME)
                .form(customerForm)
                .build());

        routes.put("types", JooqGridRoute.builder()
                .dataStoreConfig(typeConfig)
                .iconFactory(VaadinIcon.LIST::create)
                .title("route.types.title")
                .titleField(APPOINTMENT_TYPE.NAME)
                .form(typeForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("rp_i18n")
                .selects(recurrenceSelects)
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

    private void sendConfirmationEmail(AppointmentRecord record) {
        // Retrieve customer email
        // record.getCustomerId() -> fetch customer -> get email
        if (record.getCustomerId() == null) return;

        var customer = dsl.selectFrom(CUSTOMER).where(CUSTOMER.ID.eq(record.getCustomerId())).fetchOne();
        if (customer != null && customer.getEmail() != null) {
            System.out.println("Sending Confirmation Email to " + customer.getEmail() + " for Appointment on " + record.getStartTime());
            // Mock email sending
        }
    }

    private void sendUpdateEmail(AppointmentRecord record) {
         if (record.getCustomerId() == null) return;

        var customer = dsl.selectFrom(CUSTOMER).where(CUSTOMER.ID.eq(record.getCustomerId())).fetchOne();
        if (customer != null && customer.getEmail() != null) {
            System.out.println("Sending Update Email to " + customer.getEmail() + " for Appointment on " + record.getStartTime());
             // Mock email sending
        }
    }
}
