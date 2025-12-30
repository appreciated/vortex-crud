package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Join table role resolution strategy that supports both global and entity-specific roles.
 *
 * <p>When targetEntity is null: resolves global user roles from a USER_ROLES join table
 * <p>When targetEntity is provided: resolves entity-specific roles from a join table (e.g., REPOSITORY_MEMBERS)
 *
 * <p>For global roles, use the constructor with separate userRolesDataStore and rolesDataStore.
 * <p>For entity-specific roles, use the constructor with a single joinDataStore.
 *
 * @param <FieldType> The field type (e.g., TableField for jOOQ, String for JPA)
 */
public class JoinTableRoleResolutionStrategy<FieldType> implements RoleResolutionStrategy<FieldType> {

    // For entity-specific roles
    private final VortexCrudQueryDataStore<FieldType, ?> joinDataStore;
    private final FieldType userRefField; // Field in JoinModel pointing to User
    private final FieldType targetRefField; // Field in JoinModel pointing to Target
    private final FieldType roleField; // Field in JoinModel storing the role
    private final FieldType userIdField; // Field in User entity (ID)
    private final FieldType targetIdField; // Field in Target entity (ID)

    // For global roles
    private final VortexCrudQueryDataStore<FieldType, Object> userRolesDataStore;
    private final VortexCrudQueryDataStore<FieldType, Object> rolesDataStore;
    private final FieldType userRolesUserIdField;
    private final FieldType userRolesRoleIdField;
    private final FieldType rolesNameField;

    /**
     * Constructor for entity-specific roles (e.g., repository members).
     * Use this when you need to resolve roles for a specific target entity.
     */
    public JoinTableRoleResolutionStrategy(
            VortexCrudQueryDataStore<FieldType, ?> joinDataStore,
            FieldType userRefField,
            FieldType targetRefField,
            FieldType roleField,
            FieldType userIdField,
            FieldType targetIdField) {
        this.joinDataStore = joinDataStore;
        this.userRefField = userRefField;
        this.targetRefField = targetRefField;
        this.roleField = roleField;
        this.userIdField = userIdField;
        this.targetIdField = targetIdField;

        // Not used for entity-specific roles
        this.userRolesDataStore = null;
        this.rolesDataStore = null;
        this.userRolesUserIdField = null;
        this.userRolesRoleIdField = null;
        this.rolesNameField = null;
    }

    /**
     * Constructor for global roles (e.g., USER_ROLES join table).
     * Use this when you need to resolve global roles from a USER_ROLES -> ROLES relationship.
     */
    public JoinTableRoleResolutionStrategy(
            VortexCrudQueryDataStore<FieldType, Object> userRolesDataStore,
            VortexCrudQueryDataStore<FieldType, Object> rolesDataStore,
            FieldType userRolesUserIdField,
            FieldType userRolesRoleIdField,
            FieldType rolesNameField,
            FieldType usersIdField) {
        this.userRolesDataStore = userRolesDataStore;
        this.rolesDataStore = rolesDataStore;
        this.userRolesUserIdField = userRolesUserIdField;
        this.userRolesRoleIdField = userRolesRoleIdField;
        this.rolesNameField = rolesNameField;
        this.userIdField = usersIdField;

        // Not used for global roles
        this.joinDataStore = null;
        this.userRefField = null;
        this.targetRefField = null;
        this.roleField = null;
        this.targetIdField = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (userEntity == null) {
            return Collections.emptyList();
        }

        // If targetEntity is null, resolve global roles
        if (targetEntity == null) {
            return resolveGlobalRoles(reflectionService, userEntity);
        }

        // Otherwise, resolve entity-specific roles
        return resolveEntitySpecificRoles(reflectionService, userEntity, targetEntity);
    }

    /**
     * Resolves global user roles from USER_ROLES join table.
     */
    private Collection<? extends GrantedAuthority> resolveGlobalRoles(ReflectionService<FieldType> reflectionService, Object userEntity) {
        if (userRolesDataStore == null || rolesDataStore == null) {
            return Collections.emptyList();
        }

        try {
            // Get user ID from user entity
            Object userId = reflectionService.getValue(userEntity, userIdField);
            if (userId == null) {
                return Collections.emptyList();
            }

            // Query USER_ROLES join table for this user
            List<Object> userRoleRecords = userRolesDataStore.getRecordsFromTableWhereColumnEquals(
                    userRolesUserIdField,
                    userId,
                    0,
                    1000
            );

            // Extract role IDs and fetch corresponding role names
            return userRoleRecords.stream()
                    .map(userRoleRecord -> reflectionService.getValue(userRoleRecord, userRolesRoleIdField))
                    .filter(Objects::nonNull)
                    .map(roleId -> {
                        try {
                            return rolesDataStore.getRecordById(roleId);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(roleRecord -> reflectionService.getValue(roleRecord, rolesNameField))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Resolves entity-specific roles from a join table (e.g., REPOSITORY_MEMBERS).
     */
    private Collection<? extends GrantedAuthority> resolveEntitySpecificRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (joinDataStore == null) {
            return Collections.emptyList();
        }

        try {
            // Extract IDs
            Object userId = reflectionService.getValue(userEntity, userIdField);
            Object targetId = reflectionService.getValue(targetEntity, targetIdField);

            if (userId == null || targetId == null) {
                return Collections.emptyList();
            }

            // Query Join Table by User ID
            List<?> memberships = joinDataStore.getRecordsFromTableWhereColumnEquals(userRefField, userId, 0, 1000);

            return memberships.stream()
                    .filter(membership -> {
                        Object refTargetId = reflectionService.getValue(membership, targetRefField);
                        return Objects.equals(refTargetId, targetId);
                    })
                    .map(membership -> reflectionService.getValue(membership, roleField))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
