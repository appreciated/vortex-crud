package com.github.appreciated.vortex_crud.demo.resourceplanner.view;

import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.AppointmentRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jooq.DSLContext;

import static com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.Tables.APPOINTMENT;

@Route("cancel")
@AnonymousAllowed
public class CancellationView extends VerticalLayout {

    public CancellationView(DSLContext dsl) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H1("Cancel Appointment"));

        FormLayout form = new FormLayout();
        form.setMaxWidth("500px");

        IntegerField idField = new IntegerField("Appointment ID");
        TextField emailField = new TextField("Email Address");

        form.add(idField, emailField);

        Button cancelButton = new Button("Cancel Appointment", e -> {
            Integer id = idField.getValue();
            String email = emailField.getValue();

            if (id == null || email == null || email.isBlank()) {
                Notification.show("Please provide ID and Email");
                return;
            }

            AppointmentRecord record = dsl.selectFrom(APPOINTMENT)
                    .where(APPOINTMENT.ID.eq(id))
                    .and(APPOINTMENT.CUSTOMER_EMAIL.eq(email))
                    .fetchOne();

            if (record != null) {
                if ("CANCELLED".equals(record.getStatus())) {
                    Notification.show("Appointment is already cancelled.");
                } else {
                    record.setStatus("CANCELLED");
                    record.store();
                    Notification.show("Appointment cancelled successfully.");
                }
            } else {
                Notification.show("Appointment not found or email mismatch.");
            }
        });

        add(form, cancelButton);
    }
}
