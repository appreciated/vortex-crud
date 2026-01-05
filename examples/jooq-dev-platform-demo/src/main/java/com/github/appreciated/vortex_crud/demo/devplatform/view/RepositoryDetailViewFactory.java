package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.core.ui.factories.route.view.CustomViewFactory;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.RepositoryRecord;
import com.vaadin.flow.component.Component;
import org.jooq.DSLContext;
import org.jooq.TableRecord;

public class RepositoryDetailViewFactory implements CustomViewFactory<TableRecord<?>> {

    private final DSLContext dsl;

    public RepositoryDetailViewFactory(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Component create(TableRecord<?> record) {
        return new RepositoryDetailView((RepositoryRecord) record, dsl);
    }
}
