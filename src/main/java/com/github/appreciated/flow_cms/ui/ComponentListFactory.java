package com.github.appreciated.flow_cms.ui;

    import com.vaadin.flow.component.Component;
    import com.vaadin.flow.component.textfield.TextField;
    import com.vaadin.flow.component.textfield.TextArea;
    import com.vaadin.flow.component.datepicker.DatePicker;
    import com.vaadin.flow.component.combobox.ComboBox;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.function.Supplier;

    public class ComponentListFactory {

        private static final Map<String, Supplier<Component>> componentMap =
  new HashMap<>();

        static {
            componentMap.put("text", TextField::new);
            componentMap.put("textarea", TextArea::new);
            componentMap.put("date", DatePicker::new);
            componentMap.put("dropdown", ComboBox::new);
        }

        public static Component createComponent(String type) {
            Supplier<Component> componentSupplier = componentMap.get(type);
            if (componentSupplier != null) {
                return componentSupplier.get();
            }
            throw new IllegalArgumentException("Unknown component type: " +
  type);
        }
    }
