package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.DateRange;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Collection;
import java.util.List;

public class DateRangeFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField) {
        return new DateRangePicker();
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of();
    }

    public static class DateRangePicker extends CustomField<DateRange> {
        private final DatePicker start = new DatePicker();
        private final DatePicker end = new DatePicker();

        public DateRangePicker() {
            start.setLabel("Start");
            end.setLabel("End");

            start.addValueChangeListener(e -> updateModel());
            end.addValueChangeListener(e -> updateModel());

            HorizontalLayout layout = new HorizontalLayout(start, end);
            layout.setSpacing(true);
            add(layout);
        }

        private void updateModel() {
            setModelValue(generateModelValue(), true);
        }

        @Override
        protected DateRange generateModelValue() {
            return new DateRange(start.getValue(), end.getValue());
        }

        @Override
        protected void setPresentationValue(DateRange newPresentationValue) {
            if (newPresentationValue != null) {
                start.setValue(newPresentationValue.getStart());
                end.setValue(newPresentationValue.getEnd());
            } else {
                start.clear();
                end.clear();
            }
        }
    }
}
