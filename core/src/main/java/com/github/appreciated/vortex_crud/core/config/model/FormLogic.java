package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

import java.util.Map;

@FunctionalInterface
public interface FormLogic<ModelClass, FieldType, RepositoryType> {
    void init(Binder<Object> binder, Map<FieldType, Component> components, ModelClass entity, VortexCrudContext<ModelClass, FieldType, RepositoryType> context);
}
