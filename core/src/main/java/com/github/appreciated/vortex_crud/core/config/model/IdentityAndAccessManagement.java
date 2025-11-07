package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public interface IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    RepositoryType getRepositoryKey();

    List<InternalFormElement<ModelClass, FieldType, RepositoryType>> getSignUpFields();

    Class<? extends Component> getLoginView();

    Class<? extends Component> getSignUpView();

    InternalFormElement<ModelClass, FieldType, RepositoryType> getUsername();

    InternalFormElement<ModelClass, FieldType, RepositoryType> getPassword();

    boolean isSignUpEnabled();

    FieldType getRolesField();

    Roles getRoles();

    List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity);

    /**
     * Gets the current authenticated user entity from the data store.
     * This method should check the current security context and fetch the user entity.
     *
     * @return The current user entity, or null if not authenticated
     */
    Object getCurrentUserEntity();

    /**
     * Gets the roles for the current authenticated user.
     *
     * @return Set of role names for the current user, or empty set if not authenticated
     */
    Set<String> getCurrentUserRoles();

    /**
     * Checks if the current user has a specific role.
     *
     * @param role The role name to check
     * @return true if the user has the role, false otherwise
     */
    boolean currentUserHasRole(String role);
}