package com.github.appreciated.turbo_crud.ui.factories.entity_manager;

import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.AliasToEntityMapTupleTransformer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */

public class DefaultJpaEntityManagerService implements TurboCrudEntityManagerService {

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final String table;

    public DefaultJpaEntityManagerService(String table, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        this.table = table;
    }

    private String getTable() {
        return table.toUpperCase();
    }

    /**
     * Create (Insert) a new record into the given table with the provided values.
     *
     * @param values A map of column names and values to be inserted.
     */

    public void insertRecord(GenericEntity values) {
        transactionTemplate.executeWithoutResult(status -> {
            StringBuilder sql = new StringBuilder("INSERT INTO %s (".formatted(getTable()));
            StringBuilder placeholders = new StringBuilder(" VALUES (");
            List<Object> params = values.getProperties().values().stream().toList();

            values.getProperties().forEach((key, value) -> {
                sql.append(key).append(",");
                placeholders.append("?,");
            });

            sql.deleteCharAt(sql.length() - 1); // Remove last comma
            placeholders.deleteCharAt(placeholders.length() - 1); // Remove last comma
            sql.append(")").append(placeholders).append(")");

            Query query = entityManager.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                Object value = params.get(i);
                if (value instanceof LocalDateTime) {
                    value = new Date(((LocalDateTime) value).getNano());
                }
                query.setParameter(i + 1, value);
            }
            query.executeUpdate();
        });
    }

    /**
     * Read (Select) records from a table with pagination for lazy loading.
     *
     * @param offset The starting position of the first result.
     * @param limit  The maximum number of results to return.
     * @return A list of records (as a map of column names and values).
     */
    public List<GenericEntity> getRecordsFromTable(int offset, int limit) {
        String query = "SELECT * FROM %s LIMIT :limit OFFSET :offset".formatted(getTable());
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter("limit", limit)
                .setParameter("offset", offset);

        NativeQuery<GenericEntity> hibernateQuery = (NativeQuery<GenericEntity>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        return hibernateQuery.getResultList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnEquals(String filterField, String filterValue, int offset, int limit) {
        // Construct the SQL query to select records where the column equals the provided value
        String query = "SELECT * FROM %s WHERE %s = :filterValue".formatted(getTable(), filterField);

        // Create a native query using the entity manager
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("filterValue", filterValue);

        // Cast to NativeQuery to allow the use of setTupleTransformer
        NativeQuery<GenericEntity> hibernateQuery = (NativeQuery<GenericEntity>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        // Return the result list
        return hibernateQuery.getResultList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnLike(String filterField, String filterValue, int offset, int limit) {
        // Construct the SQL query to select records where the column equals the provided value
        String query = "SELECT * FROM %s WHERE %s LIKE :filterValue".formatted(getTable(), filterField);

        // Create a native query using the entity manager
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("filterValue", "%" + filterValue + "%");

        // Cast to NativeQuery to allow the use of setTupleTransformer
        NativeQuery<GenericEntity> hibernateQuery = (NativeQuery<GenericEntity>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        // Return the result list
        return hibernateQuery.getResultList();
    }

    /**
     * Read a specific record by ID (assuming the ID column is "id").
     *
     * @param id The ID of the record to fetch.
     * @return The record (as a map of column names and values) or null if not found.
     */
    public GenericEntity getRecordById(Object id) {
        String query = "SELECT * FROM %s WHERE id = ?".formatted(getTable());
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter(1, id);

        NativeQuery<GenericEntity> hibernateQuery = (NativeQuery<GenericEntity>) nativeQuery;
        hibernateQuery.setTupleTransformer(new AliasToEntityMapTupleTransformer());

        List<GenericEntity> result = hibernateQuery.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Update a record in the given table by ID.
     *
     * @param id     The ID of the record to update.
     * @param values A map of column names and new values to update.
     */
    public void updateRecordById(Object id, GenericEntity values) {
        transactionTemplate.executeWithoutResult(status -> {
            StringBuilder sql = new StringBuilder("UPDATE %s SET ".formatted(getTable()));
            List<Object> params = values.getProperties().values().stream().toList();

            values.getProperties().forEach((key, value) -> sql.append(key).append(" = ?,"));

            sql.deleteCharAt(sql.length() - 1); // Remove last comma
            sql.append(" WHERE id = ?");

            Query query = entityManager.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            query.setParameter(params.size() + 1, id);
            query.executeUpdate();
        });
    }

    /**
     * Delete a record from the table by ID.
     *
     * @param id The ID of the record to delete.
     */
    public void deleteRecordById(Object id) {
        transactionTemplate.executeWithoutResult(status -> {
            String sql = "DELETE FROM %s WHERE id = ?".formatted(getTable());
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, id);
            query.executeUpdate();
        });
    }

    /**
     * Delete all records from the table.
     */
    public void deleteAllRecords() {
        transactionTemplate.executeWithoutResult(status -> {
            String sql = "DELETE FROM %s".formatted(getTable());
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
        });
    }

    public int count() {
        return Math.toIntExact((long) entityManager.createNativeQuery("SELECT COUNT(*) FROM %s".formatted(getTable())).getSingleResult());
    }

    @Override
    public int countWhereColumnLike(String filterField, String filterValue) {
        String count = "SELECT COUNT(*) FROM %s WHERE %s LIKE '%%%s%%'".formatted(getTable(), filterField, filterValue);
        return Math.toIntExact((long) entityManager.createNativeQuery(count).getSingleResult());
    }

}
