package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.VideoFieldFactory;
import com.vaadin.flow.data.binder.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class VideoField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;
    List<Validator<?>> validators;
    boolean required;
    List<String> writeRoles;
    List<String> readOnlyRoles;
    @SuppressWarnings("unchecked")
    @Builder.Default
    Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) VideoFieldFactory.class;
}