package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class TCDateFieldFactory implements TurboCrudFieldFactory {

    @Override
    public Component createComponent(String table, String field, Field repositoryField) {
        DatePicker datePicker = new DatePicker();
        DatePicker.DatePickerI18n genericI18n = new DatePicker.DatePickerI18n();

        DateFormatSymbols symbols = new DateFormatSymbols(datePicker.getLocale());

        String[] monthNames = symbols.getMonths();
        genericI18n.setMonthNames(List.of(monthNames).subList(0, 12));

        String[] weekdays = symbols.getWeekdays();
        genericI18n.setWeekdays(List.of(
                weekdays[Calendar.SUNDAY],
                weekdays[Calendar.MONDAY],
                weekdays[Calendar.TUESDAY],
                weekdays[Calendar.WEDNESDAY],
                weekdays[Calendar.THURSDAY],
                weekdays[Calendar.FRIDAY],
                weekdays[Calendar.SATURDAY]
        ));

        String[] shortWeekdays = symbols.getShortWeekdays();
        genericI18n.setWeekdaysShort(List.of(
                shortWeekdays[Calendar.SUNDAY],
                shortWeekdays[Calendar.MONDAY],
                shortWeekdays[Calendar.TUESDAY],
                shortWeekdays[Calendar.WEDNESDAY],
                shortWeekdays[Calendar.THURSDAY],
                shortWeekdays[Calendar.FRIDAY],
                shortWeekdays[Calendar.SATURDAY]
        ));

        genericI18n.setToday(datePicker.getTranslation("button.today.title"));
        genericI18n.setCancel(datePicker.getTranslation("button.cancel.title"));
        datePicker.setI18n(genericI18n);
        return datePicker;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "DATETIME", "DATE");
    }
}
