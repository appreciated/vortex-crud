package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import com.vaadin.flow.component.Component;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

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
    private BiFunction<ReflectionService<FieldType>, ModelClass, List<SimpleGrantedAuthority>> roleResolver;
    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;
    private Class<? extends Component> loginView;
    private Class<? extends Component> signUpView;
    private Roles availableRoles;
    private List<String> defaultReadRoles;
    private List<String> defaultWriteRoles;
    private boolean signUpEnabled;


    @Override
    public List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity) {
        if (userEntity == null) {
            return Collections.emptyList();
        }
        if (roleResolver != null) {
            // Unchecked cast is safe because LocalIdentityAndAccessManagement is typed with ModelClass
            @SuppressWarnings("unchecked")
            ModelClass model = (ModelClass) userEntity;
            return roleResolver.apply(reflectionService, model);
        }
        if (rolesField == null) {
            return Collections.emptyList();
        }
        List<VortexCrudRoleProvider> value = (List<VortexCrudRoleProvider>) reflectionService.getValue(userEntity, rolesField);
        if (value == null) {
            return Collections.emptyList();
        }
        return value.stream().map(VortexCrudRoleProvider::getRole).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isSignUpEnabled() {
        return signUpEnabled;
    }
}
