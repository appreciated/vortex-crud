package com.github.appreciated.vortex_crud.demo.projectmanagement.ui.fields;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.CustomFieldDefinitionRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.service.CustomFieldService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicCustomFields extends CustomField<String> {

    private final CustomFieldService customFieldService;
    private final String entityType;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormLayout layout = new FormLayout();
    private final Map<String, AbstractField<?, ?>> fieldMap = new HashMap<>();
    private Map<String, Object> values = new HashMap<>();
    private boolean ignoreUpdates = false;

    public DynamicCustomFields(CustomFieldService customFieldService, String entityType) {
        this.customFieldService = customFieldService;
        this.entityType = entityType;
        add(layout);
        buildFields();
    }

    private void buildFields() {
        layout.removeAll();
        fieldMap.clear();
        List<CustomFieldDefinitionRecord> definitions = customFieldService.getDefinitions(entityType);
        for (CustomFieldDefinitionRecord def : definitions) {
            Component field = createField(def);
            layout.add(field);
        }
    }

    private Component createField(CustomFieldDefinitionRecord def) {
        String fieldName = def.getFieldName();
        String label = def.getFieldLabel();
        String type = def.getFieldType();

        AbstractField<?, ?> field;

        if ("select".equalsIgnoreCase(type)) {
            Select<String> select = new Select<>();
            select.setLabel(label);
            if (def.getOptions() != null) {
                select.setItems(def.getOptions().split(","));
            }
            select.addValueChangeListener(e -> updateValue(fieldName, e.getValue()));
            field = select;
        } else {
            TextField textField = new TextField(label);
            textField.addValueChangeListener(e -> updateValue(fieldName, e.getValue()));
            field = textField;
        }

        fieldMap.put(fieldName, field);
        return field;
    }

    private void updateValue(String fieldName, Object value) {
        if (ignoreUpdates) return;
        values.put(fieldName, value);
        updateModel();
    }

    private void updateModel() {
        try {
            this.setModelValue(objectMapper.writeValueAsString(values), true);
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(DynamicCustomFields.class).error("Failed to serialize custom fields", e);
        }
    }

    @Override
    protected String generateModelValue() {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @Override
    protected void setPresentationValue(String json) {
        ignoreUpdates = true;
        try {
            if (json == null || json.isBlank()) {
                values = new HashMap<>();
            } else {
                values = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});
            }

            for (Map.Entry<String, AbstractField<?, ?>> entry : fieldMap.entrySet()) {
                String key = entry.getKey();
                AbstractField field = entry.getValue();
                Object val = values.get(key);

                if (field instanceof Select) {
                    ((Select) field).setValue(val != null ? val.toString() : null);
                } else if (field instanceof TextField) {
                    ((TextField) field).setValue(val != null ? val.toString() : "");
                }
            }
        } catch (JsonProcessingException e) {
             LoggerFactory.getLogger(DynamicCustomFields.class).error("Failed to parse custom fields", e);
             values = new HashMap<>();
        } finally {
            ignoreUpdates = false;
        }
    }
}
