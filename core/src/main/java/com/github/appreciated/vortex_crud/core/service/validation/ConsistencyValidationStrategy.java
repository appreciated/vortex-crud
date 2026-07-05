package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectValueField;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validation strategy for cross-references within the configuration:
 * <ul>
 *     <li>Select keys: every {@link SelectField}/{@link MultiSelectValueField} must reference a
 *     select config registered in {@code Application.selects()} — a missing key is a guaranteed
 *     runtime failure and is collected as an error.</li>
 *     <li>Roles: roles referenced in {@code writeRoles}/{@code readOnlyRoles}/
 *     {@code defaultReadRoles}/{@code defaultWriteRoles} that are not declared in
 *     {@code availableRoles} are collected as warnings — they may be contextual roles resolved
 *     per entity (e.g. a project 'owner'), but are often typos.</li>
 * </ul>
 * Findings are collected during traversal; the caller decides how to report them.
 */
public class ConsistencyValidationStrategy implements ValidationStrategy {

    private static final Set<String> ROLE_FIELD_NAMES =
            Set.of("writeRoles", "readOnlyRoles", "defaultReadRoles", "defaultWriteRoles");

    private final Set<String> knownSelectKeys;
    private final Set<String> declaredRoles;
    private final boolean rolesConfigured;

    private final List<String> errors = new ArrayList<>();
    private final Set<String> warnings = new LinkedHashSet<>();

    public ConsistencyValidationStrategy(Selects selects, Roles availableRoles) {
        this.knownSelectKeys = selects != null && selects.configs() != null
                ? selects.configs().keySet()
                : Set.of();
        this.rolesConfigured = availableRoles != null && availableRoles.roles() != null;
        this.declaredRoles = rolesConfigured ? Set.copyOf(availableRoles.roles()) : Set.of();
    }

    @Override
    public void validate(Object value, String fieldName, String context) {
        if (value instanceof SelectField<?, ?, ?> selectField) {
            validateSelectKey(selectField.values(), fieldName, context);
        } else if (value instanceof MultiSelectValueField<?, ?, ?> multiSelectField) {
            validateSelectKey(multiSelectField.values(), fieldName, context);
        } else if ("fields".equals(fieldName) && value instanceof Map<?, ?> fields) {
            fields.forEach((key, field) -> validate(field, String.valueOf(key), context));
        } else if (rolesConfigured && ROLE_FIELD_NAMES.contains(fieldName) && value instanceof Collection<?> roles) {
            for (Object role : roles) {
                if (role instanceof String roleName && !declaredRoles.contains(roleName)) {
                    warnings.add("Role '" + roleName + "' is referenced in " + context + "." + fieldName
                            + " but not declared in availableRoles " + declaredRoles
                            + " — if it is a contextual role resolved per entity this is fine, otherwise it is a typo"
                            + " and the permission can never be granted");
                }
            }
        }
    }

    private void validateSelectKey(String selectKey, String fieldName, String context) {
        if (selectKey == null) {
            errors.add("The select field '" + fieldName + "' in " + context
                    + " has no select key configured — set .values(\"<key>\") to the key of a select config"
                    + " registered via Application.selects()");
        } else if (!knownSelectKeys.contains(selectKey)) {
            errors.add("The select field '" + fieldName + "' in " + context + " references select config '"
                    + selectKey + "', but no select with that key is registered. Known keys: " + knownSelectKeys
                    + ". Register it via Application.selects(Selects.builder().configs(Map.of(\"" + selectKey + "\", ...)))");
        }
    }

    public List<String> errors() {
        return errors;
    }

    public Collection<String> warnings() {
        return warnings;
    }
}
