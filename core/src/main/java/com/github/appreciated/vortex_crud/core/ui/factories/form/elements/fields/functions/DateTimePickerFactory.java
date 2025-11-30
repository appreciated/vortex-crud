package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class DateTimePickerFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        DateTimePicker datePicker = new DateTimePicker();
        DatePicker.DatePickerI18n datePickerI18n = new DatePicker.DatePickerI18n();

        DateFormatSymbols symbols = new DateFormatSymbols(datePicker.getLocale());

        String[] monthNames = symbols.getMonths();
        datePickerI18n.setMonthNames(List.of(monthNames).subList(0, 12));

        String[] weekdays = symbols.getWeekdays();
        datePickerI18n.setWeekdays(List.of(
                weekdays[Calendar.SUNDAY],
                weekdays[Calendar.MONDAY],
                weekdays[Calendar.TUESDAY],
                weekdays[Calendar.WEDNESDAY],
                weekdays[Calendar.THURSDAY],
                weekdays[Calendar.FRIDAY],
                weekdays[Calendar.SATURDAY]
        ));

        String[] shortWeekdays = symbols.getShortWeekdays();
        datePickerI18n.setWeekdaysShort(List.of(
                shortWeekdays[Calendar.SUNDAY],
                shortWeekdays[Calendar.MONDAY],
                shortWeekdays[Calendar.TUESDAY],
                shortWeekdays[Calendar.WEDNESDAY],
                shortWeekdays[Calendar.THURSDAY],
                shortWeekdays[Calendar.FRIDAY],
                shortWeekdays[Calendar.SATURDAY]
        ));

        datePickerI18n.setToday(datePicker.getTranslation("button.today.title"));
        datePickerI18n.setCancel(datePicker.getTranslation("button.cancel.title"));
        datePicker.setDatePickerI18n(datePickerI18n);
        return datePicker;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "DATETIME");
    }
}
