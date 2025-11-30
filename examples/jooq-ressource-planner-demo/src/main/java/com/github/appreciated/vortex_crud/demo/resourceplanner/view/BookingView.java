package com.github.appreciated.vortex_crud.demo.resourceplanner.view;

import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.AppointmentRecord;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.AppointmentTypeRecord;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.PersonRecord;
import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.RoomRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jooq.DSLContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.Tables.*;

@Route("booking")
@PermitAll
public class BookingView extends VerticalLayout {

    private final DSLContext dsl;
    private final ComboBox<AppointmentTypeRecord> typeSelect;
    private final ComboBox<PersonRecord> personSelect;
    private final ComboBox<RoomRecord> roomSelect;
    private final DateTimePicker startPicker;
    private final DateTimePicker endPicker;
    private final TextField customerName;
    private final TextField emailField;

    public BookingView(DSLContext dsl) {
        this.dsl = dsl;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H1("Book an Appointment"));

        FormLayout form = new FormLayout();
        form.setMaxWidth("600px");

        typeSelect = new ComboBox<>("Service");
        typeSelect.setItemLabelGenerator(AppointmentTypeRecord::getName);
        typeSelect.setItems(dsl.selectFrom(APPOINTMENT_TYPE).where(APPOINTMENT_TYPE.IS_ACTIVE.isTrue()).fetch());

        personSelect = new ComboBox<>("Specialist");
        personSelect.setItemLabelGenerator(PersonRecord::getName);
        personSelect.setEnabled(false);

        roomSelect = new ComboBox<>("Room");
        roomSelect.setItemLabelGenerator(RoomRecord::getName);
        roomSelect.setItems(dsl.selectFrom(ROOM).where(ROOM.IS_ACTIVE.isTrue()).fetch());
        roomSelect.setVisible(false);

        typeSelect.addValueChangeListener(e -> {
            AppointmentTypeRecord type = e.getValue();
            if (type != null) {
                // Filter persons
                personSelect.setItems(dsl.select(PERSON.fields())
                        .from(PERSON)
                        .join(PERSON_APPOINTMENT_TYPE).on(PERSON.ID.eq(PERSON_APPOINTMENT_TYPE.PERSON_ID))
                        .where(PERSON_APPOINTMENT_TYPE.APPOINTMENT_TYPE_ID.eq(type.getId()))
                        .and(PERSON.IS_ACTIVE.isTrue())
                        .fetchInto(PersonRecord.class));
                personSelect.setEnabled(true);

                boolean requiresRoom = Boolean.TRUE.equals(type.getRequiresRoom());
                roomSelect.setVisible(requiresRoom);
                roomSelect.setRequired(requiresRoom);
            } else {
                personSelect.setEnabled(false);
                roomSelect.setVisible(false);
            }
        });

        startPicker = new DateTimePicker("Start Time");
        endPicker = new DateTimePicker("End Time");

        customerName = new TextField("Your Name");

        emailField = new TextField("Your Email");
        emailField.setReadOnly(true);

        // Set email after attach to ensure security context is available (though Constructor usually works in Vaadin Spring)
        // We'll set it in constructor
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        emailField.setValue(username);

        form.add(typeSelect, personSelect, roomSelect, startPicker, endPicker, customerName, emailField);

        Button bookButton = new Button("Book", e -> book());

        add(form, bookButton);
    }

    private void book() {
        try {
            if (typeSelect.getValue() == null || startPicker.getValue() == null || endPicker.getValue() == null) {
                Notification.show("Please fill in required fields");
                return;
            }

            AppointmentRecord record = dsl.newRecord(APPOINTMENT);
            record.setAppointmentTypeId(typeSelect.getValue().getId());
            if (personSelect.getValue() != null) record.setPersonId(personSelect.getValue().getId());
            if (roomSelect.isVisible() && roomSelect.getValue() != null) record.setRoomId(roomSelect.getValue().getId());
            record.setStartTime(startPicker.getValue());
            record.setEndTime(endPicker.getValue());
            record.setCustomerName(customerName.getValue());
            record.setCustomerEmail(emailField.getValue());
            record.setStatus("CONFIRMED");
            record.setCreatedAt(LocalDateTime.now());

            // Insert will trigger the hook defined in ResourcePlannerConfig
            record.store();

            Notification.show("Appointment Booked Successfully!");
            typeSelect.clear();
            personSelect.clear();
            roomSelect.clear();
            startPicker.clear();
            endPicker.clear();
            customerName.clear();
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
