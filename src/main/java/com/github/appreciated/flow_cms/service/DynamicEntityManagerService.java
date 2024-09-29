package com.github.appreciated.flow_cms.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DynamicEntityManagerService {

    private EntityManager entityManager;

    public DynamicEntityManagerService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Create (Insert) a new record into the given table with the provided values.
     * @param tableName The name of the table.
     * @param values A map of column names and values to be inserted.
     */
    @Transactional
    public void insertRecord(String tableName, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder placeholders = new StringBuilder(" VALUES (");
        List<Object> params = values.values().stream().toList();

        values.forEach((key, value) -> {
            sql.append(key).append(",");
            placeholders.append("?,");
        });

        sql.deleteCharAt(sql.length() - 1); // Remove last comma
        placeholders.deleteCharAt(placeholders.length() - 1); // Remove last comma
        sql.append(")").append(placeholders).append(")");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        query.executeUpdate();
    }

    /**
     * Read (Select) records from a table with pagination for lazy loading.
     * @param tableName The name of the table.
     * @param offset The starting position of the first result.
     * @param limit The maximum number of results to return.
     * @return A list of records (as a map of column names and values).
     */
    public List<Map<String, Object>> getRecordsFromTable(String tableName, int offset, int limit) {
        String query = "SELECT * FROM " + tableName + " LIMIT :limit OFFSET :offset";
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter("limit", limit)
                .setParameter("offset", offset);

        NativeQuery<Map<String, Object>> hibernateQuery = (NativeQuery<Map<String, Object>>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        return hibernateQuery.getResultList();
    }

    /**
     * Read a specific record by ID (assuming the ID column is "id").
     * @param tableName The name of the table.
     * @param id The ID of the record to fetch.
     * @return The record (as a map of column names and values) or null if not found.
     */
    public Map<String, Object> getRecordById(String tableName, Object id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter(1, id);

        NativeQuery<Map<String, Object>> hibernateQuery = (NativeQuery<Map<String, Object>>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        List<Map<String, Object>> result = hibernateQuery.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Update a record in the given table by ID.
     * @param tableName The name of the table.
     * @param id The ID of the record to update.
     * @param values A map of column names and new values to update.
     */
    @Transactional
    public void updateRecordById(String tableName, Object id, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> params = values.values().stream().toList();

        values.forEach((key, value) -> {
            sql.append(key).append(" = ?,");
        });

        sql.deleteCharAt(sql.length() - 1); // Remove last comma
        sql.append(" WHERE id = ?");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        query.setParameter(params.size() + 1, id);
        query.executeUpdate();
    }

    /**
     * Delete a record from the table by ID.
     * @param tableName The name of the table.
     * @param id The ID of the record to delete.
     */
    @Transactional
    public void deleteRecordById(String tableName, Object id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, id);
        query.executeUpdate();
    }

    /**
     * Delete all records from the table.
     * @param tableName The name of the table.
     */
    @Transactional
    public void deleteAllRecords(String tableName) {
        String sql = "DELETE FROM " + tableName;
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    /**
     * Get column names for a given table using a native query and apply AliasToEntityMapResultTransformer.
     * @param tableName The name of the table.
     * @return A list of column names.
     */
    private List<String> getColumnNamesFromNativeQuery(String tableName) {
        String query = "SELECT * FROM " + tableName + " LIMIT 1";
        Query nativeQuery = entityManager.createNativeQuery(query);

        NativeQuery<Map<String, Object>> hibernateQuery = (NativeQuery<Map<String, Object>>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        List<Map<String, Object>> result = hibernateQuery.getResultList();
        return result.isEmpty() ? List.of() : result.get(0).keySet().stream().toList();
    }
}
