package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.entity.DateTimeRange;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Collection;
import java.util.List;

public class DateTimeRangeFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        return new DateTimeRangePicker();
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of();
    }

    public static class DateTimeRangePicker extends CustomField<DateTimeRange> {
        private final DateTimePicker start = new DateTimePicker();
        private final DateTimePicker end = new DateTimePicker();

        public DateTimeRangePicker() {
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
        protected DateTimeRange generateModelValue() {
            return new DateTimeRange(start.getValue(), end.getValue());
        }

        @Override
        protected void setPresentationValue(DateTimeRange newPresentationValue) {
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
