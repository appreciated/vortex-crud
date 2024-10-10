package com.github.appreciated.flow_cms.ui.factories.fields.functions;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.ui.factories.fields.FlowCmsFieldFunction;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

public class DefaultDatePickerFunction implements FlowCmsFieldFunction {

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
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
}
