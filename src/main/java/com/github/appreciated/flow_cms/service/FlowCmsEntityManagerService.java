package com.github.appreciated.flow_cms.service;

import java.util.List;

public interface FlowCmsEntityManagerService {

    void insertRecord(String tableName, GenericEntity values) ;

    List<GenericEntity> getRecordsFromTable(String tableName, int offset, int limit) ;

    List<GenericEntity> getRecordsFromTableWhereColumnEquals(String tableName, String filterField, String filterValue) ;

    GenericEntity getRecordById(String tableName, Object id) ;

    void updateRecordById(String tableName, Object id, GenericEntity values);

    void deleteRecordById(String tableName, Object id);

    void deleteAllRecords(String tableName) ;

    int count(String table) ;
}
