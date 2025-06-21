package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaGridOrListRendererConfiguration extends GridOrListRendererConfiguration<JpaRepository<?, ?>, String> {

    public JpaGridOrListRendererConfiguration(Class<? extends VortexCrudItemFactory<String>> factory) {
        super(factory);
    }

    public static class Builder extends GridOrListRendererConfiguration.Builder<JpaRepository<?, ?>, String> {
        public Builder(JpaGridOrListRendererConfiguration product) {
            super(product);
        }
    }

    public static Builder of(Class<? extends VortexCrudItemFactory> factory) {
        return new Builder(new JpaGridOrListRendererConfiguration((Class<? extends VortexCrudItemFactory<String>>) factory));
    }
}
