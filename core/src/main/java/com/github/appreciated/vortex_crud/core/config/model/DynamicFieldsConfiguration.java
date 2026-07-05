package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Declares database-defined (dynamic) fields for a {@link FormRoute}.
 * <p>
 * Unlike statically configured fields, dynamic fields are not part of the application
 * definition: their existence, type, label, options and ordering live as rows in a
 * definitions table (e.g. {@code custom_field_definition}) and can be changed at runtime
 * without a schema migration or redeploy. At render time each definition row is
 * materialized into the framework's actual {@link Field} model type (text, textarea,
 * number, date, select, multiselect, checkbox) and rendered through the standard field
 * factory pipeline. Values are surfaced with their actual Java types ({@code String},
 * {@code Double}, {@code LocalDate}, {@code Boolean}, {@code Set<String>}) and persisted
 * as JSON in a single storage column on the target entity.
 * <p>
 * Supported values of the type column: {@code text}, {@code textarea}, {@code number},
 * {@code date}, {@code select}, {@code multiselect}, {@code checkbox}.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DynamicFieldsConfiguration<ModelClass, FieldType, RepositoryType> {

    /**
     * Data store holding the field definition rows.
     */
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> definitionsDataStoreConfig;

    /**
     * Value used to filter the definitions table by {@link #entityTypeField},
     * e.g. {@code "task"}.
     */
    private String entityType;

    /**
     * Definition column: entity type discriminator.
     */
    private FieldType entityTypeField;

    /**
     * Definition column: technical field name, used as the key in the JSON storage column.
     */
    private FieldType fieldNameField;

    /**
     * Definition column: label shown to the user. This is user data, not an i18n key.
     */
    private FieldType fieldLabelField;

    /**
     * Definition column: field type ({@code text}, {@code textarea}, {@code number},
     * {@code date}, {@code select}, {@code multiselect}, {@code checkbox}).
     */
    private FieldType fieldTypeField;

    /**
     * Optional definition column: JSON array of options for {@code select} and
     * {@code multiselect} fields, e.g. {@code ["Low", "Medium", "High"]}.
     */
    private FieldType optionsField;

    /**
     * Optional definition column: truthy values mark the field as required.
     */
    private FieldType requiredField;

    /**
     * Optional definition column: numeric ordering of the fields within the form.
     */
    private FieldType orderField;

    /**
     * Optional definition column: truthy values mark the definition as active;
     * inactive definitions are not rendered.
     */
    private FieldType activeField;

    /**
     * Column on the target entity storing all dynamic field values as a JSON object.
     */
    private FieldType storageField;
}
