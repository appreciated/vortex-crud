package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;

import java.util.List;

public interface VortexCrudDataStore<FieldId> {

    Object insertRecord(GenericEntity values);

    List<GenericEntity> getRecordsFromTable(int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnEquals(FieldId filterField, Object filterValue, int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnIn(FieldId filterField, List<String> filterValue, int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnLike(FieldId filterField, Object filterValue, int offset, int limit);

    GenericEntity getRecordById(Object id);

    void updateRecordById(Object id, GenericEntity values);

    void deleteRecordById(Object id);

    void deleteAllRecords();

    int count();

    int countWhereColumnLike(FieldId filterField, String filterValue);

    java.lang.reflect.Field getField(String foreignKeyField);
 
}
