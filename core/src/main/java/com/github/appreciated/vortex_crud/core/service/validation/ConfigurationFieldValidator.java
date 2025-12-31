package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.springframework.stereotype.Service;

/**
 * Validates field configurations to catch common errors at startup.
 * This helps detect misconfigurations early rather than encountering runtime errors.
 * Uses FieldValidationStrategy to check ReferenceField configurations.
 */
@Service
public class ConfigurationFieldValidator {

    /**
     * Validates all field configurations in the application.
     *
     * @param application The application configuration to validate
     * @param <ModelClass> The model class type
     * @param <FieldType> The field type
     * @param <RepositoryType> The repository type
     */
    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        // Use the FieldValidationStrategy to validate field configurations
        FieldValidationStrategy strategy = new FieldValidationStrategy();
        application.validateWith(strategy);
    }
}
