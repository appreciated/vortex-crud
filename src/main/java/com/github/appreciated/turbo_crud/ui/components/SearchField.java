package com.github.appreciated.turbo_crud.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class SearchField extends TextField {
    public SearchField(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> componentValueChangeEventValueChangeListener) {
        setValueChangeMode(ValueChangeMode.LAZY);
        setPrefixComponent(VaadinIcon.SEARCH.create());
        setWidthFull();
        setMaxWidth("500px");
        setPlaceholder(getTranslation("search.caption"));
        addValueChangeListener(componentValueChangeEventValueChangeListener); // Suchfunktion hinzufügen
    }
}
