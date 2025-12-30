package com.github.appreciated.vortex_crud.core.config.visitor;

public interface Visitable {
    void accept(ConfigurationVisitor visitor);
}
