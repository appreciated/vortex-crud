package com.github.appreciated.vortex_crud.security.core.service;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for resolving user context and roles in a local storage scenario.
 * This service provides runtime user context resolution methods that were
 * originally part of LocalIdentityAndAccessManagement.
 */
@Service
public class LocalStorageUserContextService<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final ReflectionService<FieldType> reflectionService;

    public LocalStorageUserContextService(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            ReflectionService<FieldType> reflectionService) {
        this.configService = configService;
        this.reflectionService = reflectionService;
    }

    /**
     * Resolves roles for a given user entity.
     *
     * @param userEntity The user entity to resolve roles for
     * @return List of granted authorities for the user
     */
    public List<SimpleGrantedAuthority> resolveRolesForEntity(Object userEntity) {
        if (userEntity == null) {
            return Collections.emptyList();
        }

        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> iam = getIdentityAndAccessManagement();
        if (iam == null) {
            return Collections.emptyList();
        }

        List<? extends java.io.Serializable> roles = iam.resolveRolesForEntity(reflectionService, userEntity);
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .filter(r -> r instanceof SimpleGrantedAuthority)
                .map(r -> (SimpleGrantedAuthority) r)
                .toList();
    }

    /**
     * Resolves roles for a given user entity and target entity (context).
     *
     * @param userEntity The user entity
     * @param targetEntity The target entity
     * @return List of granted authorities for the user in context
     */
    public List<SimpleGrantedAuthority> resolveRolesForTarget(Object userEntity, Object targetEntity) {
        if (userEntity == null) {
            return Collections.emptyList();
        }

        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> iam = getIdentityAndAccessManagement();
        if (iam == null) {
            return Collections.emptyList();
        }

        List<? extends java.io.Serializable> roles = iam.resolveRolesForTarget(reflectionService, userEntity, targetEntity);
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .filter(r -> r instanceof SimpleGrantedAuthority)
                .map(r -> (SimpleGrantedAuthority) r)
                .toList();
    }

    /**
     * Gets the current authenticated user entity from the data store.
     *
     * @return The current user entity, or null if not authenticated
     */
    public Object currentUserEntity() {
        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> iam = getIdentityAndAccessManagement();
        if (iam == null) {
            return null;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        String currentUsername = auth.getName();
        if (currentUsername == null) {
            return null;
        }

        try {
            VortexCrudDataStore<FieldType, Object> dataStore =
                    (VortexCrudDataStore<FieldType, Object>) iam.dataStoreInstance();

            FieldType usernameField = iam.username().field();
            List<Object> users = dataStore.getRecordsFromTableWhereColumnEquals(usernameField, currentUsername, 0, 1);

            return users.isEmpty() ? null : users.getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the roles for the current authenticated user.
     *
     * @return Set of role names for the current user, or empty set if not authenticated
     */
    public Set<String> currentUserRoles() {
        Object userEntity = currentUserEntity();
        if (userEntity == null) {
            return Collections.emptySet();
        }

        List<SimpleGrantedAuthority> authorities = resolveRolesForEntity(userEntity);
        return authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> getIdentityAndAccessManagement() {
        if (configService == null || configService.configuration() == null) {
            return null;
        }
        return configService.configuration().identityAndAccessManagement();
    }
}
