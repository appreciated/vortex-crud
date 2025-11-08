package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    RepositoryType repositoryKey();


    List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields();

    Class<? extends Component> loginView();

    Class<? extends Component> signUpView();

    InternalFormElement<ModelClass, FieldType, RepositoryType> username();

    InternalFormElement<ModelClass, FieldType, RepositoryType> password();

    boolean isSignUpEnabled();

    FieldType rolesField();

    Roles availableRoles();

    List<String> defaultReadRoles();

    List<String> defaultWriteRoles();

    List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity);
}