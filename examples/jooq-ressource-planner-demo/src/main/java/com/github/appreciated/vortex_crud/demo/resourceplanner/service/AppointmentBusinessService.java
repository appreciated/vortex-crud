package com.github.appreciated.vortex_crud.demo.resourceplanner.service;

import com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.tables.records.AppointmentRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.github.appreciated.vortex_crud.demo.resourceplanner.jooq.Tables.*;

/**
 * Business logic service for appointment-related operations.
 * This component handles availability checks, recurrence processing, and notifications.
 */
@Component
public class AppointmentBusinessService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentBusinessService.class);
    private final DSLContext dsl;
    private JooqDataStore<AppointmentRecord> appointmentStore;

    @Autowired(required = false)
    private JavaMailSender emailSender;

    public AppointmentBusinessService(DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Sets the appointment data store instance.
     * This is called from the configuration after the store is created.
     */
    public void setAppointmentStore(JooqDataStore<AppointmentRecord> appointmentStore) {
        this.appointmentStore = appointmentStore;
    }

    /**
     * Validates that the room and person are available for the given appointment time.
     *
     * @param record the appointment record to validate
     * @throws RuntimeException if room or person is not available
     */
    public void availabilityCheck(AppointmentRecord record) {
        if (record.getStatus() != null && record.getStatus().equals("CANCELLED")) {
            return;
        }

        LocalDateTime start = record.getStartTime();
        LocalDateTime end = record.getEndTime();
        Integer id = record.getId();

        if (start == null || end == null) {
            return;
        }

        // Room Check
        if (record.getRoomId() != null) {
            var room = dsl.select(ROOM.WORKING_HOURS_START, ROOM.WORKING_HOURS_END)
                    .from(ROOM)
                    .where(ROOM.ID.eq(record.getRoomId()))
                    .fetchOne();

            if (room != null) {
                checkWorkingHours(start, end, room.value1(), room.value2(), "Room");
            }

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
            var person = dsl.select(PERSON.WORKING_HOURS_START, PERSON.WORKING_HOURS_END)
                    .from(PERSON)
                    .where(PERSON.ID.eq(record.getPersonId()))
                    .fetchOne();

            if (person != null) {
                checkWorkingHours(start, end, person.value1(), person.value2(), "Person");
            }

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

    private void checkWorkingHours(LocalDateTime start, LocalDateTime end, String startStr, String endStr, String entityName) {
        if (startStr == null || endStr == null) {
            return;
        }
        try {
            LocalTime workingStart = LocalTime.parse(startStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime workingEnd = LocalTime.parse(endStr, DateTimeFormatter.ofPattern("HH:mm"));

            if (start.toLocalTime().isBefore(workingStart) || end.toLocalTime().isAfter(workingEnd)) {
                throw new RuntimeException(entityName + " is not available during these hours (" + startStr + " - " + endStr + ").");
            }
        } catch (Exception e) {
             // invalid format, ignore or log
            log.warn("Invalid working hours format for {}: {} - {}", entityName, startStr, endStr);
        }
    }

    /**
     * Handles post-creation logic for appointments.
     * Processes recurrence and sends email notifications.
     *
     * @param record the newly created appointment record
     */
    public void onAppointmentCreated(AppointmentRecord record) {
        processRecurrence(record);
        sendEmailNotification(record);
    }

    /**
     * Processes recurring appointments by creating future instances.
     *
     * @param record the appointment record with recurrence settings
     */
    public void processRecurrence(AppointmentRecord record) {
        String freq = record.getRecurrenceFrequency();
        if (freq == null || "NONE".equals(freq) || record.getRecurrenceGroupId() != null) {
            return;
        }

        LocalDateTime endDate = record.getRecurrenceEndDate();
        if (endDate == null) {
            return;
        }

        int interval = record.getRecurrenceInterval() != null ? record.getRecurrenceInterval() : 1;
        LocalDateTime currentStart = record.getStartTime();
        LocalDateTime currentEnd = record.getEndTime();

        Integer groupId = record.getId();
        dsl.update(APPOINTMENT)
           .set(APPOINTMENT.RECURRENCE_GROUP_ID, groupId)
           .where(APPOINTMENT.ID.eq(groupId))
           .execute();

        LocalDateTime nextStart = nextDate(currentStart, freq, interval);
        LocalDateTime nextEnd = nextDate(currentEnd, freq, interval);

        while (nextStart.isBefore(endDate)) {
            AppointmentRecord newRecord = dsl.newRecord(APPOINTMENT);
            newRecord.from(record);
            newRecord.setId(null);
            newRecord.setStartTime(nextStart);
            newRecord.setEndTime(nextEnd);
            newRecord.setRecurrenceGroupId(groupId);
            newRecord.setRecurrenceFrequency("NONE");

            try {
                appointmentStore.insertRecord(newRecord);
            } catch (RuntimeException e) {
                log.warn("Could not create recurring appointment for {}: {}", nextStart, e.getMessage());
                break;
            }

            nextStart = nextDate(nextStart, freq, interval);
            nextEnd = nextDate(nextEnd, freq, interval);
        }
    }

    /**
     * Calculates the next date based on recurrence frequency and interval.
     *
     * @param date the current date
     * @param freq the frequency (DAILY, WEEKLY, MONTHLY, YEARLY)
     * @param interval the interval multiplier
     * @return the next date
     */
    public LocalDateTime nextDate(LocalDateTime date, String freq, int interval) {
        switch (freq) {
            case "DAILY": return date.plusDays(interval);
            case "WEEKLY": return date.plusWeeks(interval);
            case "MONTHLY": return date.plusMonths(interval);
            case "YEARLY": return date.plusYears(interval);
            default: return date;
        }
    }

    /**
     * Sends email notification to the customer about the appointment.
     * Currently logs the notification instead of actually sending email.
     *
     * @param record the appointment record
     */
    public void sendEmailNotification(AppointmentRecord record) {
        Integer customerId = record.getCustomerId();
        if (customerId == null) return;

        var customer = dsl.select(CUSTOMER.EMAIL, CUSTOMER.NAME)
                .from(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOne();

        if (customer == null) return;
        String email = customer.value1();
        String customerName = customer.value2();

        if (email != null && !email.isBlank()) {
            String subject = "Appointment Update";
            String body = String.format("Your appointment on %s is %s.", record.getStartTime(), record.getStatus());

            var settings = dsl.selectFrom(SETTINGS).fetchOne();
            if (settings != null && settings.getDefaultEmailTemplateId() != null) {
                var template = dsl.selectFrom(EMAIL_TEMPLATES)
                        .where(EMAIL_TEMPLATES.ID.eq(settings.getDefaultEmailTemplateId()))
                        .fetchOne();

                if (template != null) {
                    if (template.getSubject() != null) {
                        subject = replacePlaceholders(template.getSubject(), record, customerName);
                    }
                    if (template.getBody() != null) {
                        body = replacePlaceholders(template.getBody(), record, customerName);
                    }
                }
            }

            if (emailSender != null) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(email);
                    message.setSubject(subject);
                    message.setText(body);
                    emailSender.send(message);
                    log.info("📧 SENT EMAIL to: {}", email);
                } catch (Exception e) {
                    log.error("Failed to send email to {}", email, e);
                }
            } else {
                log.info("📧 [MOCKED] EMAIL NOTIFICATION: To: {}, Subject: {}, Body: {}", email, subject, body);
            }
        }
    }

    private String replacePlaceholders(String text, AppointmentRecord record, String customerName) {
        return text.replace("{{startTime}}", String.valueOf(record.getStartTime()))
                .replace("{{status}}", String.valueOf(record.getStatus()))
                .replace("{{customerName}}", customerName != null ? customerName : "");
    }
}
