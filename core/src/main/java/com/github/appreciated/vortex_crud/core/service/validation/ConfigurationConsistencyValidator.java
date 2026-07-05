package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Validates cross-references within the configuration at startup so misconfigurations fail
 * fast with actionable messages instead of surfacing as runtime exceptions on first use:
 * <ul>
 *     <li>Unknown or missing select keys (error — the application fails to start)</li>
 *     <li>Roles referenced by routes/fields but not declared in availableRoles (warning —
 *     they may be contextual roles resolved per entity)</li>
 * </ul>
 */
@Service
public class ConfigurationConsistencyValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationConsistencyValidator.class);

    /**
     * Validates the application configuration.
     *
     * @throws IllegalStateException if the configuration contains references that are
     *                               guaranteed to fail at runtime
     */
    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        Roles availableRoles = application.identityAndAccessManagement() != null
                ? application.identityAndAccessManagement().availableRoles()
                : null;
        ConsistencyValidationStrategy strategy = new ConsistencyValidationStrategy(application.selects(), availableRoles);
        application.validateWith(strategy);

        strategy.warnings().forEach(LOGGER::warn);

        if (!strategy.errors().isEmpty()) {
            throw new IllegalStateException("Invalid vortex-crud configuration ("
                    + strategy.errors().size() + " error(s)):\n - "
                    + String.join("\n - ", strategy.errors()));
        }
    }
}
