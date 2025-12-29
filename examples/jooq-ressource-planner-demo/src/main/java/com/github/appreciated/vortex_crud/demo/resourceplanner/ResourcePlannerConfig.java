package com.github.appreciated.vortex_crud.demo.resourceplanner;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.*;
import com.github.appreciated.vortex_crud.demo.resourceplanner.service.AppointmentBusinessService;
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
        JooqDataStore<RoomRecord> roomStore = new JooqDataStore<>(ROOM.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<PersonRecord> personStore = new JooqDataStore<>(PERSON.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<AppointmentTypeRecord> typeStore = new JooqDataStore<>(APPOINTMENT_TYPE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<PersonAppointmentTypeRecord> personTypeStore = new JooqDataStore<>(PERSON_APPOINTMENT_TYPE.getRecordType(), dsl, new DataStoreHooks<>());
        //TODO UNUSED
        JooqDataStore<CustomerRecord> customerStore = new JooqDataStore<>(CUSTOMER.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<AppointmentRecord> appointmentStore = new JooqDataStore<>(APPOINTMENT.getRecordType(), dsl, appointmentHooks);
        appointmentBusinessService.setAppointmentStore(appointmentStore);
        JooqDataStore<UsersRecord> usersStore = new JooqDataStore<>(USERS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<EmailTemplatesRecord> emailTemplatesStore = new JooqDataStore<>(EMAIL_TEMPLATES.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<SettingsRecord> settingsStore = new JooqDataStore<>(SETTINGS.getRecordType(), dsl, new DataStoreHooks<>());

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
                        ROOM.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var personConfig = JooqDataStoreConfig.of(PERSON)
                .dataStoreInstance(personStore)
                .fields(Map.of(
                        PERSON.ID, JooqNumericIdField.builder().build(),
                        PERSON.NAME, JooqTextField.builder().required(true).build(),
                        PERSON.EMAIL, JooqEmailField.builder().build(),
                        PERSON.TITLE, JooqTextField.builder().build(),
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
                        APPOINTMENT_TYPE.PRICE, JooqDoubleField.builder().build(),
                        APPOINTMENT_TYPE.REQUIRES_ROOM, JooqCheckboxField.builder().build(),
                        APPOINTMENT_TYPE.IS_ACTIVE, JooqCheckboxField.builder().build()))
                .build();

        var appointmentConfig = JooqDataStoreConfig.of(APPOINTMENT)
                .dataStoreInstance(appointmentStore)
                .fields(Map.ofEntries(
                        Map.entry(APPOINTMENT.ID, JooqNumericIdField.builder().build()),
                        Map.entry(APPOINTMENT.START_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.END_TIME, JooqDateTimePickerField.builder().required(true).build()),
                        Map.entry(APPOINTMENT.APPOINTMENT_TYPE_ID, JooqReferenceField.builder().dataStore(typeStore).field(APPOINTMENT.APPOINTMENT_TYPE_ID).filterField(APPOINTMENT_TYPE.NAME).children(List.of(APPOINTMENT_TYPE.NAME)).required(true).build()),
                        Map.entry(APPOINTMENT.ROOM_ID, JooqReferenceField.builder().dataStore(roomStore).field(APPOINTMENT.ROOM_ID).filterField(ROOM.NAME).children(List.of(ROOM.NAME)).build()),
                        Map.entry(APPOINTMENT.PERSON_ID, JooqReferenceField.builder().dataStore(personStore).field(APPOINTMENT.PERSON_ID).filterField(PERSON.NAME).children(List.of(PERSON.NAME)).build()),
                        Map.entry(APPOINTMENT.CUSTOMER_ID, JooqReferenceField.builder().dataStore(customerStore).field(APPOINTMENT.CUSTOMER_ID).filterField(CUSTOMER.NAME).children(List.of(CUSTOMER.NAME)).build()),
                        Map.entry(APPOINTMENT.STATUS, JooqTextField.builder().build()),
                        Map.entry(APPOINTMENT.RECURRENCE_FREQUENCY, JooqSelectField.builder().values("recurrence-frequency").build()),
                        Map.entry(APPOINTMENT.RECURRENCE_INTERVAL, JooqIntegerField.builder().build()),
                        Map.entry(APPOINTMENT.RECURRENCE_END_DATE, JooqDateTimePickerField.builder().build()),
                        Map.entry(APPOINTMENT.RECURRENCE_GROUP_ID, JooqIntegerField.builder().build()),
                        Map.entry(APPOINTMENT.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(APPOINTMENT.USER_AGREEMENT_ACCEPTED, JooqCheckboxField.builder().validators(List.of(new com.vaadin.flow.data.validator.RangeValidator<Boolean>("Must be accepted", java.util.Comparator.naturalOrder(), true, true))).build())
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
                                .filterField(EMAIL_TEMPLATES.NAME)
                                .children(List.of(EMAIL_TEMPLATES.NAME, EMAIL_TEMPLATES.SUBJECT))
                                .build()))
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

        // Room Detail (for Resource View)
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> roomDetailForm = JooqFormRoute.builder()
                .dataStoreConfig(roomConfig)
                .title("route.rooms.title")
                .titleField(ROOM.NAME)
                .children(List.of(
                        JooqFieldElement.of(ROOM.NAME, "route.rooms.labels.name").readOnly(true).build(),
                        JooqFieldElement.of(ROOM.CAPACITY, "route.rooms.labels.capacity").readOnly(true).build(),
                        JooqCollectionElement.of("route.rooms.labels.appointments")
                                .factory(new ListCollectionFactory())
                                .dataStoreConfig(appointmentConfig)
                                .oneToMany(new JooqOneToMany(APPOINTMENT.ROOM_ID))
                                .children(List.of(APPOINTMENT.START_TIME, APPOINTMENT.END_TIME, APPOINTMENT.STATUS))
                                .titleField(APPOINTMENT.START_TIME)
                                .build()))
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
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
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

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> customerForm = JooqFormRoute.builder()
                .dataStoreConfig(customerConfig)
                .title("route.customers.title")
                .titleField(CUSTOMER.NAME)
                .children(List.of(
                        JooqFieldElement.of(CUSTOMER.NAME, "route.customers.labels.name").build(),
                        JooqFieldElement.of(CUSTOMER.EMAIL, "route.customers.labels.email").build(),
                        JooqFieldElement.of(CUSTOMER.PHONE, "route.customers.labels.phone").build(),
                        JooqFieldElement.of(CUSTOMER.NOTES, "route.customers.labels.notes").build(),
                        JooqFieldElement.of(CUSTOMER.CREATED_AT, "route.customers.labels.created_at").readOnly(true).build()))
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
                .titleField(APPOINTMENT.START_TIME)
                .children(List.of(
                        JooqFieldElement.of(APPOINTMENT.START_TIME, "route.appointments.labels.start").build(),
                        JooqFieldElement.of(APPOINTMENT.END_TIME, "route.appointments.labels.end").build(),
                        JooqFieldElement.of(APPOINTMENT.APPOINTMENT_TYPE_ID, "route.appointments.labels.type").build(),
                        JooqFieldElement.of(APPOINTMENT.ROOM_ID, "route.appointments.labels.room").build(),
                        JooqFieldElement.of(APPOINTMENT.PERSON_ID, "route.appointments.labels.person").build(),
                        JooqFieldElement.of(APPOINTMENT.CUSTOMER_ID, "route.appointments.labels.customer_id").build(),
                        JooqFieldElement.of(APPOINTMENT.STATUS, "route.appointments.labels.status").build(),
                        JooqFieldElement.of(APPOINTMENT.RECURRENCE_FREQUENCY, "route.appointments.labels.recurrence_frequency").build(),
                        JooqFieldElement.of(APPOINTMENT.RECURRENCE_INTERVAL, "route.appointments.labels.recurrence_interval").build(),
                        JooqFieldElement.of(APPOINTMENT.RECURRENCE_END_DATE, "route.appointments.labels.recurrence_end_date").build(),
                        JooqFieldElement.of(APPOINTMENT.USER_AGREEMENT_ACCEPTED, "route.appointments.labels.user_agreement_accepted").build()
                ))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> emailTemplatesForm = JooqFormRoute.builder()
                .dataStoreConfig(emailTemplatesConfig)
                .title("route.email_templates.title")
                .titleField(EMAIL_TEMPLATES.NAME)
                .children(List.of(
                        JooqFieldElement.of(EMAIL_TEMPLATES.NAME, "route.email_templates.labels.name").build(),
                        JooqFieldElement.of(EMAIL_TEMPLATES.SUBJECT, "route.email_templates.labels.subject").build(),
                        JooqFieldElement.of(EMAIL_TEMPLATES.BODY, "route.email_templates.labels.body").build()))
                .build();

        // Routes
        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        routes.put("appointments", JooqCalendarRoute.builder()
                .isDefaultRoute(true)
                .dataStoreConfig(appointmentConfig)
                .iconFactory(VaadinIcon.CALENDAR::create)
                .title("route.appointments.title")
                .titleField(APPOINTMENT.START_TIME)
                .startDateField(APPOINTMENT.START_TIME)
                .endDateField(APPOINTMENT.END_TIME)
                .form(appointmentForm)
                .build());

        routes.put("resource-board", JooqKanbanRoute.builder()
                .dataStoreConfig(appointmentConfig)
                .iconFactory(VaadinIcon.DASHBOARD::create)
                .title("route.kanban.title")
                .titleField(APPOINTMENT.START_TIME)
                .columnField(APPOINTMENT.ROOM_ID)
                .form(appointmentForm)
                .build());

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
                .children(List.of(
                        JooqFieldElement.of(SETTINGS.USER_AGREEMENT_TEXT, "route.settings.labels.user_agreement_text").build(),
                        JooqFieldElement.of(SETTINGS.DEFAULT_EMAIL_TEMPLATE_ID, "route.settings.labels.default_email_template").build()))
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
                .selects(Selects.builder()
                        .configs(Map.of("recurrence-frequency", recurrenceFrequencies))
                        .build())
                .build();
    }
}
