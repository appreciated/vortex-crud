package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Class-based role resolution strategy that routes to different strategies based on target entity type.
 * Optionally supports global role resolution when no target entity is provided.
 *
 * <p>When targetEntity is null: delegates to globalStrategy (if provided)
 * <p>When targetEntity is provided: routes to strategy based on entity class
 *
 * @param <FieldType> The field type (e.g., TableField for jOOQ, String for JPA)
 */
public class ClassBasedRoleResolutionStrategy<FieldType> implements RoleResolutionStrategy<FieldType> {

    private final Map<Class<?>, RoleResolutionStrategy<FieldType>> strategies;
    private final RoleResolutionStrategy<FieldType> globalStrategy;

    /**
     * Constructor with entity-specific strategies only (no global roles).
     */
    public ClassBasedRoleResolutionStrategy(Map<Class<?>, RoleResolutionStrategy<FieldType>> strategies) {
        this(strategies, null);
    }

    /**
     * Constructor with both entity-specific and global strategies.
     *
     * @param strategies Map of entity class to role resolution strategy
     * @param globalStrategy Strategy for resolving global roles (used when targetEntity is null)
     */
    public ClassBasedRoleResolutionStrategy(
            Map<Class<?>, RoleResolutionStrategy<FieldType>> strategies,
            RoleResolutionStrategy<FieldType> globalStrategy) {
        this.strategies = strategies;
        this.globalStrategy = globalStrategy;
    }

    @Override
    public Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        // If no target entity, delegate to global strategy
        if (targetEntity == null) {
            if (globalStrategy != null) {
                return globalStrategy.resolveRoles(reflectionService, userEntity, null);
            }
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
