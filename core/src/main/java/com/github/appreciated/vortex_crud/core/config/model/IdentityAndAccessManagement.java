package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

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
}