package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityEnforcer<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker;

    public SecurityEnforcer(@Autowired(required = false) VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    public VortexCrudRbacPermissionChecker.FieldAccessLevel checkAccess(RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                                                        Field<ModelClass, FieldType, RepositoryType> field) {
        if (permissionChecker != null) {
            return permissionChecker.getUserFieldAccess(routeRenderer, field);
        }
        return null;
    }

    public void applyReadOnly(Component component, VortexCrudRbacPermissionChecker.FieldAccessLevel accessLevel) {
        if (accessLevel == VortexCrudRbacPermissionChecker.FieldAccessLevel.READ_ONLY) {
            if (component instanceof HasValue) {
                ((HasValue<?, ?>) component).setReadOnly(true);
            }
        }
    }
}
