package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.core.ui.factories.route.view.CustomViewFactory;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.RepositoryRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.service.GitService;
import com.vaadin.flow.component.Component;
import org.jooq.DSLContext;
import org.jooq.TableRecord;

public class RepositoryDetailViewFactory implements CustomViewFactory<TableRecord<?>> {

    private final DSLContext dsl;
    private final GitService gitService;

    public RepositoryDetailViewFactory(DSLContext dsl, GitService gitService) {
        this.dsl = dsl;
        this.gitService = gitService;
    }

    @Override
    public Component create(TableRecord<?> record) {
        return new RepositoryDetailView((RepositoryRecord) record, dsl, gitService);
    }
}
