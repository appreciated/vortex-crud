package com.github.appreciated.turbo_crud.entity;

import com.github.appreciated.turbo_crud.config.model.Application;
import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.config.model.Repository;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Validates the database schema against the {@link Application}.
 * <p>
 * This class checks if tables and columns in the database match the expected
 * schema based on {@link Repository} and {@link Field} from
 * {@link TurboCrudConfigService}. It uses JPA's {@link EntityManager} to run
 * native SQL queries and validate table existence, column names, and data types.
 * </p>
 * <p>
 * Discrepancies throw a {@link PersistenceException} with detailed error messages.
 * </p>
 */

@Configuration
public class TurboCrudDatabaseSchemaValidator {

    private final EntityManager entityManager;
    private final HashMap<Object, Object> typeMappings;

    public TurboCrudDatabaseSchemaValidator(EntityManager entityManager, TurboCrudConfigService configService, TurboCrudTypeMappingConfiguration typeMappingConfiguration) {
        this.entityManager = entityManager;
        typeMappings = typeMappingConfiguration.getTypeMappings();
        Map<String, Repository> tablesConfig = configService.getConfiguration().getRepositories();
        for (Map.Entry<String, Repository> entry : tablesConfig.entrySet()) {
            checkTable(entry.getKey(), entry.getValue().getFields());
        }
    }

    public void checkTable(String tableName, Map<String, Field> expectedColumns) {
        if (!tableExists(tableName)) {
            throw new PersistenceException("Table " + tableName + " does not exist in the database.");
        }
        checkColumns(tableName, expectedColumns);
        checkPrimaryKey(tableName);
        checkForeignKeys(tableName, expectedColumns);
    }

    private boolean tableExists(String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name = :tableName";
        List<?> result = entityManager.createNativeQuery(query)
                .setParameter("tableName", tableName)
                .getResultList();
        return !result.isEmpty();
    }

    private void checkColumns(String tableName, Map<String, Field> expectedColumns) {
        String query = "PRAGMA table_info(" + tableName + ")";
        try {
            List<Object[]> columns = entityManager.createNativeQuery(query).unwrap(NativeQuery.class)
                    .addScalar("dflt_value", StandardBasicTypes.STRING)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .addScalar("type", StandardBasicTypes.STRING)
                    .getResultList();

            // Create a map for actual columns with names and types
            Map<String, String> actualColumns = new HashMap<>();
            for (Object[] column : columns) {
                String columnName = ((String) column[1]).toLowerCase(); // Assuming the second column is 'name'
                String columnType = ((String) column[2]).toLowerCase(); // Assuming the third column is 'type'
                actualColumns.put(columnName, columnType);
            }

            // Iterate over the expected columns
            for (Map.Entry<String, Field> entry : expectedColumns.entrySet()) {
                String expectedColumnName = entry.getKey().toLowerCase();
                Field fieldConfig = entry.getValue();

                String actualColumnType = actualColumns.get(expectedColumnName);
                if (actualColumnType == null) {
                    throw new PersistenceException("The expected column '" + expectedColumnName + "' was not found in table '" + tableName + "'.");
                }

                Collection<String> validColumnTypes = getValidDatabaseTypesForExpectedType(fieldConfig.getFactory().toLowerCase());
                String type = actualColumnType.contains("(") ? actualColumnType.substring(0, actualColumnType.indexOf("(")).toUpperCase() : actualColumnType.toUpperCase();
                if (!validColumnTypes.contains(type)) {
                    throw new PersistenceException("The type of the column '" + expectedColumnName + "' in table '" + tableName + "' does not match. Expected one of: " + validColumnTypes + ", Found: " + actualColumnType);
                }
            }
        } catch (GenericJDBCException e) {
            LoggerFactory.getLogger(TurboCrudDatabaseSchemaValidator.class).error("JDBC Error for table " + tableName , e);
        }
    }

    private Collection<String> getValidDatabaseTypesForExpectedType(String expectedType) {

        return (Collection<String>) typeMappings.getOrDefault(expectedType.toLowerCase(), Collections.emptyList());
    }

    private void checkPrimaryKey(String tableName) {
        // TODO
    }

    private void checkForeignKeys(String tableName, Map<String, Field> expectedColumns) {
        // TODO
    }
}
