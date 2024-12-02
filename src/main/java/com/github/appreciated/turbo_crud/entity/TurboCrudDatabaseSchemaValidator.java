package com.github.appreciated.turbo_crud.entity;

import com.github.appreciated.turbo_crud.config.model.Application;
import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.config.model.Repository;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
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
        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = 'PUBLIC'";
        List<?> result = entityManager.createNativeQuery(query)
                .setParameter("tableName", tableName.toUpperCase()) // H2 speichert Tabellen normalerweise in Großbuchstaben
                .getResultList();
        return !result.isEmpty();
    }

    private void checkColumns(String tableName, Map<String, Field> expectedColumns) {
        String query = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = 'PUBLIC'";
        List<Object[]> columns = entityManager.createNativeQuery(query)
                .setParameter("tableName", tableName.toUpperCase())
                .getResultList();

        // Eine Map erstellen, die die tatsächlichen Spaltennamen und Typen aus der Datenbank enthält
        Map<String, String> actualColumns = new HashMap<>();
        for (Object[] column : columns) {
            String columnName = ((String) column[0]).toLowerCase(); // Kleinbuchstaben zur besseren Vergleichbarkeit
            String columnType = (String) column[1];
            actualColumns.put(columnName, columnType);
        }

        // Jetzt über die erwarteten Spalten iterieren
        for (Map.Entry<String, Field> entry : expectedColumns.entrySet()) {
            String expectedColumnName = entry.getKey().toLowerCase();
            Field fieldConfig = entry.getValue();

            String actualColumnType = actualColumns.get(expectedColumnName);
            if (actualColumnType == null) {
                throw new PersistenceException("The expected column '" + expectedColumnName + "' was not found in table '" + tableName + "'.");
            }

            Collection<String> validColumnTypes = getValidDatabaseTypesForExpectedType(fieldConfig.getFactory().toUpperCase());

            if (!validColumnTypes.contains(actualColumnType)) {
                throw new PersistenceException("The type of the column '" + expectedColumnName + "' in table '" + tableName + "' does not match. Expected one of: " + validColumnTypes + ", Found: " + actualColumnType);
            }
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
