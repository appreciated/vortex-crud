package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.*;
import org.springframework.stereotype.Service;

import static jakarta.persistence.metamodel.Attribute.PersistentAttributeType.*;

@Service
public class JpaFieldTypeResolverService {
    private final EntityManager entityManager;

    public JpaFieldTypeResolverService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Class<?> resolveTargetClass(JpaRepositoryDataStore<?> dataStore, java.lang.reflect.Field entityField) {
        Metamodel metamodel = entityManager.getMetamodel();
        Class<?> model = dataStore.getModelClass();
        EntityType<?> entityType = metamodel.entity(model);
        Attribute<?, ?> attribute = entityType.getAttribute(entityField.getName());
        Attribute.PersistentAttributeType type = attribute.getPersistentAttributeType();
        Class<?> targetEntityClass = null;
        if ((type == MANY_TO_MANY || type == ONE_TO_MANY || type == MANY_TO_ONE)) {
            if (attribute instanceof PluralAttribute<?, ?, ?> pluralAttribute) {
                targetEntityClass = pluralAttribute.getElementType().getJavaType();
            } else if (attribute instanceof SingularAttribute<?, ?> singularAttribute) {
                targetEntityClass = singularAttribute.getJavaType();
            }

        } else {
            throw new IllegalStateException("Field '%s' of type '%s' is annotated with ReferenceFieldFactory but is not a ManyToMany or OneToMany relationship".formatted(entityField.getName(), model.getSimpleName()));
        }
        return targetEntityClass;
    }
}
