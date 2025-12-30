package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Field-based role resolution strategy.
 * Resolves roles by reading from a field on the user entity (e.g., JPA @ManyToMany relationship).
 * Returns the same roles regardless of targetEntity (global roles).
 */
public class FieldBasedRoleResolutionStrategy<FieldType> implements RoleResolutionStrategy<FieldType> {

    private final FieldType rolesField;

    public FieldBasedRoleResolutionStrategy(FieldType rolesField) {
        this.rolesField = rolesField;
    }

    @Override
    public Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (userEntity == null) {
            return Collections.emptyList();
        }

        try {
            Object value = reflectionService.getValue(userEntity, rolesField);
            if (value == null) {
                return Collections.emptyList();
            }

            // Check if the value is actually a List before casting
            if (!(value instanceof List)) {
                return Collections.emptyList();
            }

            @SuppressWarnings("unchecked")
            List<VortexCrudRoleProvider> roles = (List<VortexCrudRoleProvider>) value;

            return roles.stream()
                    .map(VortexCrudRoleProvider::getRole)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
