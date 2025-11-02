package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
@Builder
public class LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> implements IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    private final RepositoryType repositoryKey;
    private final InternalFormElement<ModelClass, FieldType, RepositoryType> username;
    private final InternalFormElement<ModelClass, FieldType, RepositoryType> password;
    private final FieldType rolesField;
    private final List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;
    private final Class<? extends Component> loginView;
    private final Class<? extends Component> signUpView;
    private final Roles roles;
    @Builder.Default
    private final boolean signUpEnabled = false;

    @Override
    public List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity) {
        List<VortexCrudRoleProvider> value = (List<VortexCrudRoleProvider>) reflectionService.getValue(userEntity, rolesField);
        return value.stream().map(VortexCrudRoleProvider::getRole).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isSignUpEnabled() {
        return signUpEnabled;
    }

}
