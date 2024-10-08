package com.github.appreciated.flow_cms.config;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.config.model.TableConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class DatabaseSchemaChecker {

    private final EntityManager entityManager;
    private final HashMap<Object, Object> typeMappings;

    public DatabaseSchemaChecker(EntityManager entityManager, FlowCmsConfigService flowCmsConfigService) {
        this.entityManager = entityManager;

        typeMappings = new HashMap<>();
        typeMappings.put("number", List.of("INTEGER", "BIGINT", "SMALLINT", "DECIMAL", "NUMERIC"));
        typeMappings.put("id", List.of("UUID", "INTEGER", "CHAR", "VARCHAR"));
        typeMappings.put("text", List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "CLOB"));
        typeMappings.put("date", List.of("DATE"));
        typeMappings.put("datetime", List.of("TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "DATETIME"));
        typeMappings.put("boolean", List.of("BOOLEAN", "BIT"));
        typeMappings.put("select", List.of("VARCHAR", "CHARACTER VARYING"));

        Map<String, TableConfig> tablesConfig = flowCmsConfigService.getConfiguration().getTablesConfig();

        for (Map.Entry<String, TableConfig> entry : tablesConfig.entrySet()) {
            checkTable(entry.getKey(), entry.getValue().getFieldsConfig());
        }
    }

    public void checkTable(String tableName, Map<String, FieldConfig> expectedColumns) {
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

    private void checkColumns(String tableName, Map<String, FieldConfig> expectedColumns) {
        String query = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = 'PUBLIC'";
        List<Object[]> columns = entityManager.createNativeQuery(query)
                .setParameter("tableName", tableName.toUpperCase())
                .getResultList();

        for (Object[] column : columns) {
            String columnName = (String) column[0];
            String columnType = (String) column[1];

            FieldConfig expectedConfig = expectedColumns.get(columnName.toLowerCase());
            if (expectedConfig == null) {
                throw new PersistenceException("The column '" + columnName + "' was not found in table '" + tableName + "'.");
            }

            Collection<String> validColumnTypes = getValidDatabaseTypesForExpectedType(expectedConfig.getType().toUpperCase());

            if (!validColumnTypes.contains(columnType)) {
                throw new PersistenceException("The type of the column '" + columnName + "' in table '" + tableName + "' does not match. Expected one of: " + validColumnTypes + ", Found: " + columnType);
            }
        }
    }

    private Collection<String> getValidDatabaseTypesForExpectedType(String expectedType) {
        return (Collection<String>) typeMappings.getOrDefault(expectedType.toLowerCase(), Collections.emptyList());
    }

    private void checkPrimaryKey(String tableName) {
        // TODO
    }

    private void checkForeignKeys(String tableName, Map<String, FieldConfig> expectedColumns) {
        // TODO
    }
}
