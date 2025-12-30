package com.github.appreciated.vortex_crud.core.config.visitor;

import com.github.appreciated.vortex_crud.core.config.model.*;

public interface I18nConfigurationVisitor {
    void visit(Application<?, ?, ?> application);
    void visit(RouteRenderer<?, ?, ?> routeRenderer);
    void visit(DataStoreConfig<?, ?, ?> dataStoreConfig);
    void visit(Field<?, ?, ?> field);
    void visit(InternalFormElement<?, ?, ?> internalFormElement);
    void visit(Collection<?, ?, ?> collection);
    void visit(Object object);
}
