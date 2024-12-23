package com.github.appreciated.turbo_crud.core.entity.data_store;

import com.github.appreciated.turbo_crud.core.model.GenericEntity;

import java.util.List;

public interface TurboCrudDataStore {

    Object insertRecord(GenericEntity values);

    List<GenericEntity> getRecordsFromTable(int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnEquals(String filterField, String filterValue, int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValue, int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnLike(String filterField, String filterValue, int offset, int limit);

    GenericEntity getRecordById(Object id);

    void updateRecordById(Object id, GenericEntity values);

    void deleteRecordById(Object id);

    void deleteAllRecords();

    int count();

    int countWhereColumnLike(String filterField, String filterValue);
}
