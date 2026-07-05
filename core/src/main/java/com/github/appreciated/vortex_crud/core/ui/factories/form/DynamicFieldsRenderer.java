package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.DynamicFieldsConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.fields.CheckboxField;
import com.github.appreciated.vortex_crud.core.config.model.fields.DateField;
import com.github.appreciated.vortex_crud.core.config.model.fields.DoubleField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Materializes the database-defined field definitions of a {@link DynamicFieldsConfiguration}
 * into the framework's actual {@link Field} model types, renders them through the standard
 * field factory pipeline and binds them with their actual Java value types. All values of an
 * entity are persisted together as a JSON object in the configured storage column.
 */
public class DynamicFieldsRenderer<ModelClass, FieldType, RepositoryType> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void bindAndAddToLayout(DynamicFieldsConfiguration<ModelClass, FieldType, RepositoryType> configuration,
                                   RepositoryType dataStoreKey,
                                   Object entity,
                                   VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                   Binder<Object> binder,
                                   FormLayout form) {
        ReflectionService<FieldType> reflectionService = context.reflectionService();
        VortexCrudDataStore<FieldType, ModelClass> definitionsStore =
                configuration.definitionsDataStoreConfig().dataStoreInstance();

        List<ModelClass> definitions = definitionsStore.getRecordsFromTableWhereColumnEquals(
                        configuration.entityTypeField(), configuration.entityType(), 0, Integer.MAX_VALUE)
                .stream()
                .filter(definition -> configuration.activeField() == null
                        || isTruthy(reflectionService.getValue(definition, configuration.activeField())))
                .sorted(Comparator.comparingInt(definition -> configuration.orderField() != null
                        ? asInt(reflectionService.getValue(definition, configuration.orderField()))
                        : 0))
                .toList();

        if (definitions.isEmpty()) {
            return;
        }

        // Shared, mutable view of the JSON storage column. Every binding setter updates it and
        // writes the serialized result back to the entity, so a single save persists all values.
        Map<String, Object> values = parseJsonObject(reflectionService.getString(entity, configuration.storageField()));

        for (ModelClass definition : definitions) {
            String name = reflectionService.getString(definition, configuration.fieldNameField());
            String label = reflectionService.getString(definition, configuration.fieldLabelField());
            String type = reflectionService.getString(definition, configuration.fieldTypeField());
            boolean required = configuration.requiredField() != null
                    && isTruthy(reflectionService.getValue(definition, configuration.requiredField()));
            List<String> options = configuration.optionsField() != null
                    ? parseJsonArray(reflectionService.getString(definition, configuration.optionsField()))
                    : List.of();

            switch (type == null ? "text" : type) {
                case "number" -> {
                    Component component = createComponent(DoubleField.<ModelClass, FieldType, RepositoryType>builder().build(),
                            configuration, dataStoreKey, context, label, form);
                    bind(binder, component, required, configuration, reflectionService, values, name,
                            value -> value instanceof Number number ? number.doubleValue() : null,
                            value -> value);
                }
                case "date" -> {
                    Component component = createComponent(DateField.<ModelClass, FieldType, RepositoryType>builder().build(),
                            configuration, dataStoreKey, context, label, form);
                    bind(binder, component, required, configuration, reflectionService, values, name,
                            value -> value instanceof String string && !string.isBlank() ? LocalDate.parse(string) : null,
                            value -> value != null ? value.toString() : null);
                }
                case "checkbox" -> {
                    Component component = createComponent(CheckboxField.<ModelClass, FieldType, RepositoryType>builder().build(),
                            configuration, dataStoreKey, context, label, form);
                    bind(binder, component, false, configuration, reflectionService, values, name,
                            Boolean.TRUE::equals,
                            value -> value);
                }
                case "select" -> {
                    // The regular SelectFieldFactory resolves its options from the application's
                    // static Selects registry; dynamic options come from the definition row instead.
                    Select<String> select = new Select<>();
                    select.setItems(options);
                    addToForm(select, label, form);
                    bind(binder, select, required, configuration, reflectionService, values, name,
                            value -> value instanceof String string ? string : null,
                            value -> value);
                }
                case "multiselect" -> {
                    CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
                    checkboxGroup.setItems(options);
                    addToForm(checkboxGroup, label, form);
                    bind(binder, checkboxGroup, required, configuration, reflectionService, values, name,
                            value -> value instanceof List<?> list
                                    ? list.stream().map(String::valueOf).collect(Collectors.toSet())
                                    : Set.<String>of(),
                            value -> value != null ? List.copyOf((Set<String>) value) : List.of());
                }
                case "textarea" -> {
                    Component component = createComponent(TextAreaField.<ModelClass, FieldType, RepositoryType>builder().build(),
                            configuration, dataStoreKey, context, label, form);
                    bind(binder, component, required, configuration, reflectionService, values, name,
                            value -> value instanceof String string ? string : null,
                            value -> value);
                }
                default -> {
                    Component component = createComponent(TextField.<ModelClass, FieldType, RepositoryType>builder().build(),
                            configuration, dataStoreKey, context, label, form);
                    bind(binder, component, required, configuration, reflectionService, values, name,
                            value -> value instanceof String string ? string : null,
                            value -> value);
                }
            }
        }
    }

    private Component createComponent(Field<ModelClass, FieldType, RepositoryType> field,
                                      DynamicFieldsConfiguration<ModelClass, FieldType, RepositoryType> configuration,
                                      RepositoryType dataStoreKey,
                                      VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                      String label,
                                      FormLayout form) {
        Component component = field.factory().createComponent(dataStoreKey, configuration.storageField(), field, context);
        addToForm(component, label, form);
        return component;
    }

    private void addToForm(Component component, String label, FormLayout form) {
        if (component instanceof HasSize hasSize) {
            hasSize.setWidthFull();
        }
        // The label is user data from the definitions table, not an i18n key.
        if (component instanceof HasLabel hasLabel) {
            hasLabel.setLabel(label);
            form.add(component);
        } else {
            form.addFormItem(component, label);
        }
    }

    @SuppressWarnings("unchecked")
    private <PresentationType> void bind(Binder<Object> binder,
                                         Component component,
                                         boolean required,
                                         DynamicFieldsConfiguration<ModelClass, FieldType, RepositoryType> configuration,
                                         ReflectionService<FieldType> reflectionService,
                                         Map<String, Object> values,
                                         String name,
                                         Function<Object, PresentationType> fromJson,
                                         Function<PresentationType, Object> toJson) {
        Binder.BindingBuilder<Object, PresentationType> builder =
                binder.forField((HasValue<?, PresentationType>) component);
        if (required) {
            builder = builder.asRequired();
        }
        builder.bind(
                bean -> fromJson.apply(values.get(name)),
                (bean, value) -> {
                    values.put(name, toJson.apply(value));
                    reflectionService.setValue(bean, configuration.storageField(), MAPPER.writeValueAsString(values));
                });
    }

    private static boolean isTruthy(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.intValue() != 0;
        }
        if (value instanceof String string) {
            return string.equalsIgnoreCase("true") || string.equals("1");
        }
        return false;
    }

    private static int asInt(Object value) {
        return value instanceof Number number ? number.intValue() : 0;
    }

    private static Map<String, Object> parseJsonObject(String json) {
        if (json == null || json.isBlank()) {
            return new LinkedHashMap<>();
        }
        try {
            return new LinkedHashMap<>(MAPPER.readValue(json, Map.class));
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    private static List<String> parseJsonArray(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return ((List<?>) MAPPER.readValue(json, List.class)).stream().map(String::valueOf).toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
