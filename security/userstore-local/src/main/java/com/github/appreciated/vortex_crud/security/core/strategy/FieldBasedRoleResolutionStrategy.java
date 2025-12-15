package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FieldBasedRoleResolutionStrategy<FieldType> implements RoleResolutionStrategy<FieldType> {

    private final FieldType rolesField;

    public FieldBasedRoleResolutionStrategy(FieldType rolesField) {
        this.rolesField = rolesField;
    }

    @Override
    public Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (userEntity == null || rolesField == null) {
            return Collections.emptyList();
        }
        try {
            Object valueObj = reflectionService.getValue(userEntity, rolesField);
            if (valueObj == null) {
                return Collections.emptyList();
            }
            List<VortexCrudRoleProvider> value = (List<VortexCrudRoleProvider>) valueObj;
            return value.stream().map(VortexCrudRoleProvider::getRole).map(SimpleGrantedAuthority::new).toList();
        } catch (ClassCastException e) {
            // Log or ignore? Safest to return empty list if type doesn't match
            return Collections.emptyList();
        }
    }
}
