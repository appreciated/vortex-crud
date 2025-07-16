package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCollection extends Collection<JpaRepository<?, ?>, String, JpaRepository<?, ?> > {

    public JpaCollection(Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?> >> factory) {
        super(factory);
    }

    public static class Builder extends Collection.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?> > {
        public Builder(Collection<JpaRepository<?, ?>, String, JpaRepository<?, ?> > product) {
            super(product);
        }
    }

    public static JpaCollection.Builder of(Class<? extends VortexCrudDialogFactory> factory) {
        return new Builder(new Collection<>((Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?> >>) factory));
    }
}
