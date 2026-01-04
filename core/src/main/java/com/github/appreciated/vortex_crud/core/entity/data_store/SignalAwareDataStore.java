package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.signals.SignalFactory;
import com.vaadin.signals.ValueSignal;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SignalAwareDataStore<FieldType, ModelClass> implements VortexCrudDataStore<FieldType, ModelClass> {

    private final VortexCrudDataStore<FieldType, ModelClass> delegate;

    private void emitChange() {
        try {
            if (VaadinSession.getCurrent() != null) {
                // We use IN_MEMORY_SHARED to broadcast to all users (since we want real-time updates across clients)
                // However, Signals documentation says IN_MEMORY_SHARED returns the same signal instance for the same name within the same JVM.
                // This fits perfectly.
                ValueSignal<Long> signal = SignalFactory.IN_MEMORY_SHARED.value("entity-change:" + delegate.getModelClass().getName(), Long.class);
                signal.value(System.currentTimeMillis());
            }
        } catch (Exception e) {
            // Ignore if no session or signal issue, should not block data operation
        }
    }

    @Override
    public ModelClass newInstance() {
        return delegate.newInstance();
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        Object id = delegate.insertRecord(entity);
        emitChange();
        return id;
    }

    @Override
    public ModelClass getRecordById(Object id) {
        return delegate.getRecordById(id);
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return delegate.getRecordsFromTable(offset, limit);
    }

    @Override
    public void updateRecord(ModelClass entity) {
        delegate.updateRecord(entity);
        emitChange();
    }

    @Override
    public void updateRecordById(ModelClass entity) {
        delegate.updateRecordById(entity);
        emitChange();
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        delegate.deleteRecord(entity);
        emitChange();
    }

    @Override
    public void deleteRecordById(Object id) {
        delegate.deleteRecordById(id);
        emitChange();
    }

    @Override
    public int count() {
        return delegate.count();
    }

    @Override
    public Class<ModelClass> getModelClass() {
        return delegate.getModelClass();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(FieldType filterField, Object filterValue, int offset, int limit) {
        return delegate.getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(FieldType filterField, Object filterValue, FieldType orderField, int offset, int limit) {
        return delegate.getRecordsFromTableWhereColumnEqualsOrdered(filterField, filterValue, orderField, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(FieldType filterField, List<String> filterValue, int offset, int limit) {
        return delegate.getRecordsFromTableWhereColumnIn(filterField, filterValue, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(FieldType filterField, Object filterValue, int offset, int limit) {
        return delegate.getRecordsFromTableWhereColumnLike(filterField, filterValue, offset, limit);
    }

    @Override
    public int countWhereColumnLike(FieldType filterField, String filterValue) {
        return delegate.countWhereColumnLike(filterField, filterValue);
    }

    @Override
    public int countWhereFiltersEqual(List<RouteFilter<FieldType>> filters) {
        return delegate.countWhereFiltersEqual(filters);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereFiltersEqual(List<RouteFilter<FieldType>> filters, int offset, int limit) {
        return delegate.getRecordsFromTableWhereFiltersEqual(filters, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLikeAndFiltersEqual(FieldType searchField, Object searchValue, List<RouteFilter<FieldType>> filters, int offset, int limit) {
        return delegate.getRecordsFromTableWhereColumnLikeAndFiltersEqual(searchField, searchValue, filters, offset, limit);
    }

    @Override
    public int countWhereColumnLikeAndFiltersEqual(FieldType searchField, String searchValue, List<RouteFilter<FieldType>> filters) {
        return delegate.countWhereColumnLikeAndFiltersEqual(searchField, searchValue, filters);
    }
}
