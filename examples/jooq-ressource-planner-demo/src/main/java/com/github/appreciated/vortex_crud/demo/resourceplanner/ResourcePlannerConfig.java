package com.github.appreciated.vortex_crud.demo.resourceplanner;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EntityComboBoxWrapper;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.*;
import com.github.appreciated.vortex_crud.demo.resourceplanner.service.AppointmentBusinessService;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.strategy.JoinTableRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.Tables.*;

@Service
public class ResourcePlannerConfig implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private final AppointmentBusinessService appointmentBusinessService;

    public ResourcePlannerConfig(DSLContext dsl, AppointmentBusinessService appointmentBusinessService) {
        this.dsl = dsl;
        this.appointmentBusinessService = appointmentBusinessService;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Hooks
        DataStoreHooks<AppointmentRecord> appointmentHooks = new DataStoreHooks<>();
        appointmentHooks.beforeCreates().add(appointmentBusinessService::availabilityCheck);
        appointmentHooks.beforeUpdates().add(appointmentBusinessService::availabilityCheck);
        appointmentHooks.afterCreates().add(appointmentBusinessService::onAppointmentCreated);
        appointmentHooks.afterUpdates().add(appointmentBusinessService::sendEmailNotification);

        // Data Stores
        JooqDataStore<RoomRecord> roomStore = new JooqDataStore<>(ROOM.getRecordType(), dsl);
        JooqDataStore<PersonRecord> personStore = new JooqDataStore<>(PERSON.getRecordType(), dsl);
        JooqDataStore<AppointmentTypeRecord> typeStore = new JooqDataStore<>(APPOINTMENT_TYPE.getRecordType(), dsl);
        JooqDataStore<PersonAppointmentTypeRecord> personTypeStore = new JooqDataStore<>(PERSON_APPOINTMENT_TYPE.getRecordType(), dsl);
        JooqDataStore<CustomerRecord> customerStore = new JooqDataStore<>(CUSTOMER.getRecordType(), dsl);
        JooqDataStore<AppointmentRecord> appointmentStore = new JooqDataStore<>(APPOINTMENT.getRecordType(), dsl, appointmentHooks);
        appointmentBusinessService.setAppointmentStore(appointmentStore);
        JooqDataStore<UsersRecord> usersStore = new JooqDataStore<>(USERS.getRecordType(), dsl);
        JooqDataStore<RolesRecord> rolesStore = new JooqDataStore<>(ROLES.getRecordType(), dsl);
        JooqDataStore<UserRolesRecord> userRolesStore = new JooqDataStore<>(USER_ROLES.getRecordType(), dsl);
        JooqDataStore<EmailTemplatesRecord> emailTemplatesStore = new JooqDataStore<>(EMAIL_TEMPLATES.getRecordType(), dsl);
        JooqDataStore<SettingsRecord> settingsStore = new JooqDataStore<>(SETTINGS.getRecordType(), dsl);
        JooqDataStore<NotificationRecord> notificationStore = new JooqDataStore<>(NOTIFICATION.getRecordType(), dsl);

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance(usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var roomConfig = JooqDataStoreConfig.of(ROOM)
                .dataStoreInstance(roomStore)
                .fields(Map.of(
                        ROOM.ID, JooqNumericIdField.builder().build(),
                        ROOM.NAME, JooqTextField.builder().required(true).build(),
                        ROOM.CAPACITY, JooqIntegerField.builder().build(),
                        ROOM.DESCRIPTION, JooqTextAreaField.builder().build(),
                        ROOM.WORKING_HOURS_START, JooqTextField.builder().build(),
                        ROOM.WORKING_HOURS_END, JooqTextField.builder().build(),
                        ROOM.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var personConfig = JooqDataStoreConfig.of(PERSON)
                .dataStoreInstance(personStore)
                .fields(Map.of(
                        PERSON.ID, JooqNumericIdField.builder().build(),
                        PERSON.NAME, JooqTextField.builder().required(true).build(),
                        PERSON.EMAIL, JooqEmailField.builder().build(),
                        PERSON.TITLE, JooqTextField.builder().build(),
                        PERSON.WORKING_HOURS_START, JooqTextField.builder().build(),
                        PERSON.WORKING_HOURS_END, JooqTextField.builder().build(),
                        PERSON.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var customerConfig = JooqDataStoreConfig.of(CUSTOMER)
                .dataStoreInstance(customerStore)
                .fields(Map.of(
                        CUSTOMER.ID, JooqNumericIdField.builder().build(),
                        CUSTOMER.NAME, JooqTextField.builder().required(true).build(),
                        CUSTOMER.EMAIL, JooqEmailField.builder().build(),
                        CUSTOMER.PHONE, JooqTextField.builder().build(),
                        CUSTOMER.NOTES, JooqTextAreaField.builder().build(),
                        CUSTOMER.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var typeConfig = JooqDataStoreConfig.of(APPOINTMENT_TYPE)
                .dataStoreInstance(typeStore)
                .fields(Map.of(
                        APPOINTMENT_TYPE.ID, JooqNumericIdField.builder().build(),
                        APPOINTMENT_TYPE.NAME, JooqTextField.builder().required(true).build(),
                        APPOINTMENT_TYPE.DESCRIPTION, JooqTextAreaField.builder().build(),
                        APPOINTMENT_TYPE.DURATION_MINUTES, JooqIntegerField.builder().build(),
                        APPOINTMENT_TYPE.PRICE, JooqBigDecimalField.builder().build(),
                        APPOINTMENT_TYPE.REQUIRES_ROOM, JooqCheckboxField.builder().build(),
                        APPOINTMENT_TYPE.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var appointmentConfig = JooqDataStoreConfig.of(APPOINTMENT)
                .dataStoreInstance(appointmentStore)
                .fields(Map.ofEntries(
                        Map.entry(APPOINTMENT.ID, JooqNumericIdField.builder().build()),
                        Map.entry(APPOINTMENT.START_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.END_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.APPOINTMENT_TYPE_ID, JooqReferenceField.builder().dataStore(typeStore).field(APPOINTMENT.APPOINTMENT_TYPE_ID).searchField(APPOINTMENT_TYPE.NAME).children(List.of(APPOINTMENT_TYPE.NAME)).required(true).build()),
                        Map.entry(APPOINTMENT.ROOM_ID, JooqReferenceField.builder().dataStore(roomStore).field(APPOINTMENT.ROOM_ID).searchField(ROOM.NAME).children(List.of(ROOM.NAME)).build()),
                        Map.entry(APPOINTMENT.PERSON_ID, JooqReferenceField.builder().dataStore(personStore).field(APPOINTMENT.PERSON_ID).searchField(PERSON.NAME).children(List.of(PERSON.NAME)).build()),
                        Map.entry(APPOINTMENT.CUSTOMER_ID, JooqReferenceField.builder().dataStore(customerStore).field(APPOINTMENT.CUSTOMER_ID).searchField(CUSTOMER.NAME).children(List.of(CUSTOMER.NAME)).build()),
                        Map.entry(APPOINTMENT.STATUS, JooqTextField.builder().build()),
                        Map.entry(APPOINTMENT.RECURRENCE_FREQUENCY, JooqSelectField.builder().values("recurrence-frequency").build()),
                        Map.entry(APPOINTMENT.RECURRENCE_INTERVAL, JooqIntegerField.builder().build()),
                        Map.entry(APPOINTMENT.RECURRENCE_END_DATE, JooqDateTimePickerField.builder().build()),
                        Map.entry(APPOINTMENT.RECURRENCE_GROUP_ID, JooqIntegerField.builder().build()),
                        Map.entry(APPOINTMENT.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(APPOINTMENT.USER_AGREEMENT_ACCEPTED, JooqCheckboxField.builder().validators(List.of(new com.vaadin.flow.data.validator.RangeValidator<>("Must be accepted", java.util.Comparator.naturalOrder(), true, true))).build())
                ))
                .build();

        var emailTemplatesConfig = JooqDataStoreConfig.of(EMAIL_TEMPLATES)
                .dataStoreInstance(emailTemplatesStore)
                .fields(Map.of(
                        EMAIL_TEMPLATES.ID, JooqNumericIdField.builder().build(),
                        EMAIL_TEMPLATES.NAME, JooqTextField.builder().required(true).build(),
                        EMAIL_TEMPLATES.SUBJECT, JooqTextField.builder().build(),
                        EMAIL_TEMPLATES.BODY, JooqMarkDownField.builder().build()))
                .build();

        var settingsConfig = JooqDataStoreConfig.of(SETTINGS)
                .dataStoreInstance(settingsStore)
                .fields(Map.of(
                        SETTINGS.ID, JooqNumericIdField.builder().build(),
                        SETTINGS.USER_AGREEMENT_TEXT, JooqTextAreaField.builder().build(),
                        SETTINGS.DEFAULT_EMAIL_TEMPLATE_ID, JooqReferenceField.builder()
                                .dataStore(emailTemplatesStore)
                                .field(SETTINGS.DEFAULT_EMAIL_TEMPLATE_ID)
                                .searchField(EMAIL_TEMPLATES.NAME)
                                .children(List.of(EMAIL_TEMPLATES.NAME, EMAIL_TEMPLATES.SUBJECT))
                                .build()))
                .build();

        var notificationConfig = JooqDataStoreConfig.of(NOTIFICATION)
                .dataStoreInstance(notificationStore)
                .fields(Map.of(
                        NOTIFICATION.ID, JooqNumericIdField.builder().build(),
                        NOTIFICATION.TITLE, JooqTextField.builder().build(),
                        NOTIFICATION.MESSAGE, JooqTextAreaField.builder().build(),
                        NOTIFICATION.LINK, JooqTextField.builder().build(),
                        NOTIFICATION.IS_READ, JooqCheckboxField.builder().build(),
                        NOTIFICATION.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        // Forms
        var roomForm = JooqFormRoute.builder()
                .titleField(ROOM.NAME)
                .fields(List.of(
                        JooqFormElement.of(ROOM.NAME, "route.rooms.labels.name").build(),
                        JooqFormElement.of(ROOM.CAPACITY, "route.rooms.labels.capacity").build(),
                        JooqFormElement.of(ROOM.DESCRIPTION, "route.rooms.labels.description").build(),
                        JooqFormElement.of(ROOM.WORKING_HOURS_START, "route.rooms.labels.working_hours_start").build(),
                        JooqFormElement.of(ROOM.WORKING_HOURS_END, "route.rooms.labels.working_hours_end").build(),
                        JooqFormElement.of(ROOM.IS_ACTIVE, "route.rooms.labels.active").build()))
                .build();

        var appointmentForm = JooqFormRoute.builder()
                .titleField(APPOINTMENT.START_TIME)
                .fields(List.of(
                        JooqFormElement.of(APPOINTMENT.START_TIME, "route.appointments.labels.start").build(),
                        JooqFormElement.of(APPOINTMENT.END_TIME, "route.appointments.labels.end").build(),
                        JooqFormElement.of(APPOINTMENT.APPOINTMENT_TYPE_ID, "route.appointments.labels.type").build(),
                        JooqFormElement.of(APPOINTMENT.ROOM_ID, "route.appointments.labels.room").build(),
                        JooqFormElement.of(APPOINTMENT.PERSON_ID, "route.appointments.labels.person").build(),
                        JooqFormElement.of(APPOINTMENT.CUSTOMER_ID, "route.appointments.labels.customer_id").build(),
                        JooqFormElement.of(APPOINTMENT.STATUS, "route.appointments.labels.status").build(),
                        JooqFormElement.of(APPOINTMENT.RECURRENCE_FREQUENCY, "route.appointments.labels.recurrence_frequency").build(),
                        JooqFormElement.of(APPOINTMENT.RECURRENCE_INTERVAL, "route.appointments.labels.recurrence_interval").build(),
                        JooqFormElement.of(APPOINTMENT.RECURRENCE_END_DATE, "route.appointments.labels.recurrence_end_date").build(),
                        JooqFormElement.of(APPOINTMENT.USER_AGREEMENT_ACCEPTED, "route.appointments.labels.user_agreement_accepted").build()
                ))
                .formLogic((binder, components, entity, context) -> {
                    Component typeComponent = components.get(APPOINTMENT.APPOINTMENT_TYPE_ID);
                    Component startComponent = components.get(APPOINTMENT.START_TIME);
                    Component endComponent = components.get(APPOINTMENT.END_TIME);
                    Component roomComponent = components.get(APPOINTMENT.ROOM_ID);
                    Component personComponent = components.get(APPOINTMENT.PERSON_ID);

                    // Auto-calculation Logic
                    if (typeComponent instanceof EntityComboBoxWrapper && startComponent instanceof DateTimePicker && endComponent instanceof DateTimePicker) {
                        EntityComboBoxWrapper typeWrapper = (EntityComboBoxWrapper) typeComponent;
                        DateTimePicker startDatePicker = (DateTimePicker) startComponent;
                        DateTimePicker endDatePicker = (DateTimePicker) endComponent;

                        com.vaadin.flow.component.HasValue.ValueChangeListener listener = event -> {
                            Integer typeId = (Integer) typeWrapper.getValue();
                            java.time.LocalDateTime start = startDatePicker.getValue();
                            if (typeId != null && start != null) {
                                var typeRecord = typeStore.getRecordById(typeId);
                                if (typeRecord != null && typeRecord.getDurationMinutes() != null) {
                                    endDatePicker.setValue(start.plusMinutes(typeRecord.getDurationMinutes()));
                                }
                            }
                        };

                        typeWrapper.addValueChangeListener(listener);
                        startDatePicker.addValueChangeListener(listener);
                    }

                    // Dynamic Filtering Logic
                    if (roomComponent instanceof EntityComboBoxWrapper && personComponent instanceof EntityComboBoxWrapper && startComponent instanceof DateTimePicker && endComponent instanceof DateTimePicker) {
                        EntityComboBoxWrapper roomWrapper = (EntityComboBoxWrapper) roomComponent;
                        EntityComboBoxWrapper personWrapper = (EntityComboBoxWrapper) personComponent;
                        DateTimePicker startDatePicker = (DateTimePicker) startComponent;
                        DateTimePicker endDatePicker = (DateTimePicker) endComponent;
                        ComboBox<Object> personComboBox = (ComboBox<Object>) personWrapper.getComponent();

                        Runnable updatePersons = () -> {
                            Integer roomId = (Integer) roomWrapper.getValue();
                            java.time.LocalDateTime start = startDatePicker.getValue();
                            java.time.LocalDateTime end = endDatePicker.getValue();

                            if (roomId != null && start != null && end != null) {
                                var busyPersons = dsl.select(APPOINTMENT.PERSON_ID)
                                        .from(APPOINTMENT)
                                        .where(APPOINTMENT.START_TIME.lessThan(end))
                                        .and(APPOINTMENT.END_TIME.greaterThan(start))
                                        .and(APPOINTMENT.STATUS.ne("CANCELLED"));

                                var availablePersons = dsl.selectFrom(PERSON)
                                        .where(PERSON.IS_ACTIVE.eq(true))
                                        .and(PERSON.ID.notIn(busyPersons))
                                        .fetch().stream();
                                personComboBox.setItems(availablePersons);
                            } else {
                                java.util.stream.Stream<PersonRecord> personStream = dsl.selectFrom(PERSON).where(PERSON.IS_ACTIVE.eq(true)).fetch().stream();
                                personComboBox.setItems(personStream);
                            }
                        };

                        roomWrapper.addValueChangeListener(e -> updatePersons.run());
                        startDatePicker.addValueChangeListener(e -> updatePersons.run());
                        endDatePicker.addValueChangeListener(e -> updatePersons.run());
                    }
                })
                .build();
        // Room Detail (for Resource View)
        var roomDetailForm = JooqFormRoute.builder()
                .titleField(ROOM.NAME)
                .fields(List.of(
                        JooqFormElement.of(ROOM.NAME, "route.rooms.labels.name").readOnly(true).build(),
                        JooqFormElement.of(ROOM.CAPACITY, "route.rooms.labels.capacity").readOnly(true).build(),
                        JooqCollection.builder()
                                .label("route.rooms.labels.appointments")
                                .field(APPOINTMENT.START_TIME)
                                .listFactory(new ListCollectionFactory<>())
                                .dataStoreConfig(appointmentConfig)
                                .form(appointmentForm)
                                .oneToMany(new JooqOneToMany(APPOINTMENT.ROOM_ID))
                                .children(List.of(APPOINTMENT.START_TIME, APPOINTMENT.END_TIME, APPOINTMENT.STATUS))
                                .titleField(APPOINTMENT.START_TIME)
                                .build()))
                .build();

        var personForm = JooqFormRoute.builder()
                .titleField(PERSON.NAME)
                .fields(List.of(
                        JooqFormElement.of(PERSON.NAME, "route.persons.labels.name").readOnly(true).build(),
                        JooqFormElement.of(PERSON.EMAIL, "route.persons.labels.email").build(),
                        JooqFormElement.of(PERSON.TITLE, "route.persons.labels.title").build(),
                        JooqFormElement.of(PERSON.WORKING_HOURS_START, "route.persons.labels.working_hours_start").build(),
                        JooqFormElement.of(PERSON.WORKING_HOURS_END, "route.persons.labels.working_hours_end").build(),
                        JooqFormElement.of(PERSON.IS_ACTIVE, "route.persons.labels.active").build(),
                        JooqCollection.builder()
                                .label("route.persons.labels.services")
                                .field(APPOINTMENT_TYPE.NAME)
                                .listFactory(new ListCollectionFactory<>())
                                .dataStoreConfig(typeConfig)
                                .manyToMany(new JooqManyToMany<>(
                                        PERSON_APPOINTMENT_TYPE.PERSON_ID,
                                        PERSON_APPOINTMENT_TYPE.APPOINTMENT_TYPE_ID,
                                        APPOINTMENT_TYPE.ID,
                                        PERSON_APPOINTMENT_TYPE))
                                .children(List.of(APPOINTMENT_TYPE.NAME, APPOINTMENT_TYPE.DURATION_MINUTES))
                                .emptyMessage("route.persons.labels.services-empty")
                                .titleField(APPOINTMENT_TYPE.NAME)
                                .build()))
                .build();

        var customerForm = JooqFormRoute.builder()
                .titleField(CUSTOMER.NAME)
                .fields(List.of(
                        JooqFormElement.of(CUSTOMER.NAME, "route.customers.labels.name").build(),
                        JooqFormElement.of(CUSTOMER.EMAIL, "route.customers.labels.email").build(),
                        JooqFormElement.of(CUSTOMER.PHONE, "route.customers.labels.phone").build(),
                        JooqFormElement.of(CUSTOMER.NOTES, "route.customers.labels.notes").build(),
                        JooqFormElement.of(CUSTOMER.CREATED_AT, "route.customers.labels.created_at").readOnly(true).build()))
                .build();

        var typeForm = JooqFormRoute.builder()
                .titleField(APPOINTMENT_TYPE.NAME)
                .fields(List.of(
                        JooqFormElement.of(APPOINTMENT_TYPE.NAME, "route.types.labels.name").build(),
                        JooqFormElement.of(APPOINTMENT_TYPE.DURATION_MINUTES, "route.types.labels.duration").build(),
                        JooqFormElement.of(APPOINTMENT_TYPE.PRICE, "route.types.labels.price").build(),
                        JooqFormElement.of(APPOINTMENT_TYPE.REQUIRES_ROOM, "route.types.labels.requires_room").build(),
                        JooqFormElement.of(APPOINTMENT_TYPE.IS_ACTIVE, "route.types.labels.active").build()))
                .build();


        var emailTemplatesForm = JooqFormRoute.builder()
                .titleField(EMAIL_TEMPLATES.NAME)
                .fields(List.of(
                        JooqFormElement.of(EMAIL_TEMPLATES.NAME, "route.email_templates.labels.name").build(),
                        JooqFormElement.of(EMAIL_TEMPLATES.SUBJECT, "route.email_templates.labels.subject").build(),
                        JooqFormElement.of(EMAIL_TEMPLATES.BODY, "route.email_templates.labels.body").build()))
                .build();

        // Routes
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        var appointmentsRoute = JooqCalendarRoute.builder()
                .defaultRoute(true)
                .dataStoreConfig(appointmentConfig)
                .iconFactory(VaadinIcon.CALENDAR::create)
                .title("route.appointments.title")
                .titleField(APPOINTMENT.START_TIME)
                .startDateField(APPOINTMENT.START_TIME)
                .endDateField(APPOINTMENT.END_TIME)
                .form(appointmentForm)
                .build();

        var personsRoute = JooqGridRoute.builder()
                .dataStoreConfig(personConfig)
                .iconFactory(VaadinIcon.USER::create)
                .title("route.persons.title")
                .titleField(PERSON.NAME)
                .form(personForm)
                .build();

        var customersRoute = JooqGridRoute.builder()
                .dataStoreConfig(customerConfig)
                .iconFactory(VaadinIcon.USERS::create)
                .title("route.customers.title")
                .titleField(CUSTOMER.NAME)
                .form(customerForm)
                .build();

        routes.put("search", SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("route.search.title")
                .iconFactory(VaadinIcon.SEARCH::create)
                .searchableRoutes(List.of(appointmentsRoute, personsRoute, customersRoute))
                .build());

        routes.put("appointments", appointmentsRoute);

        routes.put("resource-view", JooqMasterDetailRoute.builder()
                .dataStoreConfig(roomConfig)
                .iconFactory(VaadinIcon.BUILDING::create)
                .title("route.resource_view.title")
                .titleField(ROOM.NAME)
                .form(roomDetailForm)
                .build());

        routes.put("rooms", JooqGridRoute.builder()
                .dataStoreConfig(roomConfig)
                .iconFactory(VaadinIcon.HOME::create)
                .title("route.rooms.title")
                .titleField(ROOM.NAME)
                .form(roomForm)
                .build());

        routes.put("persons", personsRoute);

        routes.put("customers", customersRoute);

        routes.put("types", JooqGridRoute.builder()
                .dataStoreConfig(typeConfig)
                .iconFactory(VaadinIcon.LIST::create)
                .title("route.types.title")
                .titleField(APPOINTMENT_TYPE.NAME)
                .form(typeForm)
                .build());

        routes.put("email-templates", JooqGridRoute.builder()
                .dataStoreConfig(emailTemplatesConfig)
                .iconFactory(VaadinIcon.ENVELOPE::create)
                .title("route.email_templates.title")
                .titleField(EMAIL_TEMPLATES.NAME)
                .form(emailTemplatesForm)
                .build());

        routes.put("settings", JooqSingleFormRoute.builder()
                .dataStoreConfig(settingsConfig)
                .iconFactory(VaadinIcon.COG::create)
                .title("route.settings.title")
                .titleField(SETTINGS.ID)
                .entityFilterField(SETTINGS.ID)
                .entityFilterValueProvider(() -> 1)  // Always fetch the singleton settings record with ID 1
                .fields(List.of(
                        JooqFormElement.of(SETTINGS.USER_AGREEMENT_TEXT, "route.settings.labels.user_agreement_text").build(),
                        JooqFormElement.of(SETTINGS.DEFAULT_EMAIL_TEMPLATE_ID, "route.settings.labels.default_email_template").build()))
                .build());

        LinkedHashMap<String, String> recurrenceFrequencies = new LinkedHashMap<>();
        recurrenceFrequencies.put("NONE", "None");
        recurrenceFrequencies.put("DAILY", "Daily");
        recurrenceFrequencies.put("WEEKLY", "Weekly");
        recurrenceFrequencies.put("MONTHLY", "Monthly");
        recurrenceFrequencies.put("YEARLY", "Yearly");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("rp_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(usersConfig)
                        .roleResolutionStrategy(new JoinTableRoleResolutionStrategy<>(
                                userRolesStore,
                                rolesStore,
                                USER_ROLES.USER_ID,
                                USER_ROLES.ROLE_ID,
                                ROLES.NAME,
                                USERS.ID
                        ))
                        .availableRoles(com.github.appreciated.vortex_crud.core.config.model.Roles.builder().roles(List.of("admin", "user")).build())
                        .defaultReadRoles(List.of("user"))
                        .defaultWriteRoles(List.of("admin"))
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFormElement.of(USERS.USERNAME, "route.users.labels.username").build())
                        .password(JooqFormElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build())
                        .signUpFields(List.of())
                        .build())
                .routes(routes)
                .selects(Selects.builder()
                        .configs(Map.of("recurrence-frequency", recurrenceFrequencies))
                        .build())
                .notificationPanelConfiguration(NotificationPanelConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(notificationConfig)
                        .timestampField(NOTIFICATION.CREATED_AT)
                        .messageField(NOTIFICATION.MESSAGE)
                        .readStatusField(NOTIFICATION.IS_READ)
                        .readStatusValueForRead(1)
                        .readStatusValueForUnread(0)
                        .build())
                .build();
    }
}
