package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JoinTableRoleResolutionStrategy<FieldType, JoinModel> implements RoleResolutionStrategy<FieldType> {

    private final VortexCrudDataStore<FieldType, JoinModel> joinDataStore;
    private final FieldType userRefField; // Field in JoinModel pointing to User
    private final FieldType targetRefField; // Field in JoinModel pointing to Target
    private final FieldType roleField; // Field in JoinModel storing the role
    private final FieldType userIdField; // Field in User entity (ID)
    private final FieldType targetIdField; // Field in Target entity (ID)

    public JoinTableRoleResolutionStrategy(
            VortexCrudDataStore<FieldType, JoinModel> joinDataStore,
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
    }

    @Override
    public Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (userEntity == null || targetEntity == null) {
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
            List<JoinModel> memberships = joinDataStore.getRecordsFromTableWhereColumnEquals(userRefField, userId, 0, 1000);

            return memberships.stream()
                    .filter(membership -> {
                        Object refTargetId = reflectionService.getValue(membership, targetRefField);
                        // Handle potential type mismatch (e.g. Long vs Integer) via toString if necessary,
                        // but Objects.equals is safest for same types.
                        // Assuming types match as per DataStore contract.
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
