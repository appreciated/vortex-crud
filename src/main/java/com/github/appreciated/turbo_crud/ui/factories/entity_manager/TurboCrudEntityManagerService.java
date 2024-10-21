package com.github.appreciated.turbo_crud.ui.factories.entity_manager;

import com.github.appreciated.turbo_crud.model.GenericEntity;

import java.util.List;

public interface TurboCrudEntityManagerService {

    void insertRecord(GenericEntity values);

    List<GenericEntity> getRecordsFromTable(int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnEquals(String filterField, String filterValue, int offset, int limit);

    List<GenericEntity> getRecordsFromTableWhereColumnLike(String filterField, String filterValue, int offset, int limit);

    GenericEntity getRecordById(Object id);

    void updateRecordById(Object id, GenericEntity values);

    void deleteRecordById(Object id);

    void deleteAllRecords();

    int count();

    int countWhereColumnLike(String filterField, String filterValue);
}
