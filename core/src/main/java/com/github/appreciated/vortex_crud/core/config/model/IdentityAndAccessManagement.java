package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;

import java.io.Serializable;
import java.util.List;

public interface IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> extends HasDataStore<FieldType, ModelClass>, ValidatableConfiguration {

    DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig();

    @Override
    default VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig() != null ? dataStoreConfig().dataStoreInstance() : null;
    }

    List<InternalFormElement<FieldType>> signUpFields();

    Class<? extends Component> loginView();

    Class<? extends Component> signUpView();

    InternalFormElement<FieldType> username();

    InternalFormElement<FieldType> password();

    boolean isSignUpEnabled();

    Roles availableRoles();

    List<String> defaultReadRoles();

    List<String> defaultWriteRoles();

    List<? extends Serializable> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity);

    /**
     * Resolves roles for a specific target entity (context).
     * By default delegates to global role resolution.
     *
     * @param reflectionService the reflection service
     * @param userEntity        the user entity
     * @param targetEntity      the target entity (context)
     * @return list of roles/authorities
     */
    default List<? extends Serializable> resolveRolesForTarget(ReflectionService<FieldType> reflectionService, Object userEntity, Object targetEntity) {
        return resolveRolesForEntity(reflectionService, userEntity);
    }
}
