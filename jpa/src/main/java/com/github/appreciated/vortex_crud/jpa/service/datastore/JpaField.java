package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

class JpaField extends Field<JpaRepository<?, ?>, String, JpaRepository<?,?>> {

    public JpaField(Class<? extends VortexCrudFieldFactory> factory) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?,?>>>) factory);
    }

    public static Builder<JpaRepository<?, ?>, String, JpaRepository<?,?>> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?> >>) factory, primary, required));
    }

    public static Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?> > of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?> >>) factory, primary, required, validation));
    }

    public static Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?> > of(Class<? extends VortexCrudFieldFactory> factory, String field, String filterField, JpaRepository<?, ?> dataStore, List<String> children) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?> >>) factory, field, filterField, dataStore, children));
    }

    public static Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?> > of(Class<? extends VortexCrudFieldFactory> factory, String values) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?> >>)factory, values));
    }

}
