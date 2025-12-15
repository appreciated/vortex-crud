package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ClassBasedRoleResolutionStrategy<FieldType> implements RoleResolutionStrategy<FieldType> {

    private final Map<Class<?>, RoleResolutionStrategy<FieldType>> strategies;

    public ClassBasedRoleResolutionStrategy(Map<Class<?>, RoleResolutionStrategy<FieldType>> strategies) {
        this.strategies = strategies;
    }

    @Override
    public Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (targetEntity == null) {
            return Collections.emptyList();
        }

        // Try exact match
        RoleResolutionStrategy<FieldType> strategy = strategies.get(targetEntity.getClass());
        if (strategy != null) {
            return strategy.resolveRoles(reflectionService, userEntity, targetEntity);
        }

        // Try assignable types
        for (Map.Entry<Class<?>, RoleResolutionStrategy<FieldType>> entry : strategies.entrySet()) {
             if (entry.getKey().isInstance(targetEntity)) {
                 return entry.getValue().resolveRoles(reflectionService, userEntity, targetEntity);
             }
        }

        return Collections.emptyList();
    }
}
