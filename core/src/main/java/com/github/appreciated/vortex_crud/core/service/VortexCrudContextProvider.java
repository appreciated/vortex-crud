package com.github.appreciated.vortex_crud.core.service;

import org.springframework.stereotype.Service;

@Service
public class VortexCrudContextProvider {

    private final VortexCrudContext context;

    public VortexCrudContextProvider(VortexCrudContext context) {
        this.context = context;
    }

    public VortexCrudContext getContext() {
        return context;
    }
}
