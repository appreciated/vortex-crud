package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
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
    private InternalFormElement<ModelClass, FieldType, RepositoryType> username;
    private InternalFormElement<ModelClass, FieldType, RepositoryType> password;
    private FieldType rolesField;
    private RoleResolutionStrategy<FieldType> roleResolutionStrategy;
    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;
    private Class<? extends Component> loginView;
    private Class<? extends Component> signUpView;
    private List<String> availableRoles;
    private List<String> defaultReadRoles;
    private List<String> defaultWriteRoles;
    private boolean signUpEnabled;


    @Override
    public List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity) {
        if (userEntity == null || rolesField == null) {
            return Collections.emptyList();
        }
        List<VortexCrudRoleProvider> value = (List<VortexCrudRoleProvider>) reflectionService.getValue(userEntity, rolesField);
        if (value == null) {
            return Collections.emptyList();
        }
        return value.stream().map(VortexCrudRoleProvider::getRole).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public List<? extends Serializable> resolveRolesForTarget(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        List<SimpleGrantedAuthority> globalRoles = resolveRolesForEntity(reflectionService, userEntity);
        if (roleResolutionStrategy == null) {
            return globalRoles;
        }
        Collection<? extends GrantedAuthority> contextRoles = roleResolutionStrategy.resolveRoles(reflectionService, userEntity, targetEntity);

        if (contextRoles.isEmpty()) {
            return globalRoles;
        }

        List<SimpleGrantedAuthority> combined = new ArrayList<>(globalRoles);
        for (GrantedAuthority ga : contextRoles) {
            combined.add(new SimpleGrantedAuthority(ga.getAuthority()));
        }
        return combined;
    }

    @Override
    public boolean isSignUpEnabled() {
        return signUpEnabled;
    }
}
