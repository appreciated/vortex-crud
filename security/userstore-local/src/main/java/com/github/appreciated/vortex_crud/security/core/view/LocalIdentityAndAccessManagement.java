package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import com.github.appreciated.vortex_crud.security.core.strategy.RoleResolutionStrategy;
import com.vaadin.flow.component.Component;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> implements IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;
    private InternalFormElement<FieldType> username;
    private InternalFormElement<FieldType> password;
    private RoleResolutionStrategy<FieldType> roleResolutionStrategy;
    private List<InternalFormElement<FieldType>> signUpFields;
    private Class<? extends Component> loginView;
    private Class<? extends Component> signUpView;
    private Roles availableRoles;
    private List<String> defaultReadRoles;
    private List<String> defaultWriteRoles;
    private boolean signUpEnabled;


    @Override
    public List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity) {
        if (roleResolutionStrategy == null) {
            return Collections.emptyList();
        }
        // Pass null as targetEntity to get global roles only
        Collection<? extends GrantedAuthority> roles = roleResolutionStrategy.resolveRoles(reflectionService, userEntity, null);
        return roles.stream()
                .map(ga -> new SimpleGrantedAuthority(ga.getAuthority()))
                .toList();
    }

    @Override
    public List<? extends Serializable> resolveRolesForTarget(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        if (roleResolutionStrategy == null) {
            return Collections.emptyList();
        }

        // Get global roles (targetEntity = null)
        Collection<? extends GrantedAuthority> globalRoles = roleResolutionStrategy.resolveRoles(reflectionService, userEntity, null);

        // If no targetEntity provided, return global roles only
        if (targetEntity == null) {
            return globalRoles.stream()
                    .map(ga -> new SimpleGrantedAuthority(ga.getAuthority()))
                    .toList();
        }

        // Get entity-specific roles (with targetEntity)
        Collection<? extends GrantedAuthority> entityRoles = roleResolutionStrategy.resolveRoles(reflectionService, userEntity, targetEntity);

        // Combine global and entity-specific roles
        List<SimpleGrantedAuthority> combined = new ArrayList<>();
        for (GrantedAuthority ga : globalRoles) {
            combined.add(new SimpleGrantedAuthority(ga.getAuthority()));
        }
        for (GrantedAuthority ga : entityRoles) {
            combined.add(new SimpleGrantedAuthority(ga.getAuthority()));
        }
        return combined;
    }

    @Override
    public boolean isSignUpEnabled() {
        return signUpEnabled;
    }
}
