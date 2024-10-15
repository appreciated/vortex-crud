package com.github.appreciated.turbo_crud.service;

import java.util.List;

public interface TurboCrudEntityManagerService {

    void insertRecord(String tableName, GenericEntity values) ;

    List<GenericEntity> getRecordsFromTable(String tableName, int offset, int limit) ;

    List<GenericEntity> getRecordsFromTableWhereColumnEquals(String tableName, String filterField, String filterValue, int offset, int limit) ;

    List<GenericEntity> getRecordsFromTableWhereColumnLike(String tableName, String filterField, String filterValue, int offset, int limit) ;

    GenericEntity getRecordById(String tableName, Object id) ;

    void updateRecordById(String tableName, Object id, GenericEntity values);

    void deleteRecordById(String tableName, Object id);

    void deleteAllRecords(String tableName) ;

    int count(String table) ;

    int countWhereColumnLike(String tableName, String filterField, String filterValue);
}
