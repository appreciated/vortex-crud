package com.github.appreciated.flow_cms.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseSchemaChecker {

    private final Connection connection;

    public DatabaseSchemaChecker(Connection connection) {
        this.connection = connection;
    }

    public void checkSchema() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
    }

    private void checkTable(DatabaseMetaData metaData, String tableName,
                            Map<String, String> expectedColumns) throws SQLException {
        ResultSet tables = metaData.getTables(null, null, tableName,
                null);
        if (!tables.next()) {
            throw new SQLException("Table " + tableName + " does not exist  in the database.");
        }

        ResultSet columns = metaData.getColumns(null, null, tableName,
                null);
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            if (!expectedColumns.containsKey(columnName) ||
                !expectedColumns.get(columnName).equalsIgnoreCase(columnType)) {
                throw new SQLException("Column " + columnName + " in table  " + tableName + " does not match the expected definition.");
            }
        }

        checkPrimaryKey(metaData, tableName);
        checkForeignKeys(metaData, tableName, expectedColumns);
    }

    private void checkPrimaryKey(DatabaseMetaData metaData, String
            tableName) throws SQLException {
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null,
                tableName);
        if (!primaryKeys.next()) {
            throw new SQLException("Table " + tableName + " does not have  a primary key.");
        }
    }

    private void checkForeignKeys(DatabaseMetaData metaData, String
            tableName, Map<String, String> expectedColumns) throws SQLException {
        ResultSet foreignKeys = metaData.getImportedKeys(null, null,
                tableName);
        while (foreignKeys.next()) {
            String fkTableName = foreignKeys.getString("PKTABLE_NAME");
            String fkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
            String fkName = foreignKeys.getString("FKCOLUMN_NAME");
            if (!expectedColumns.containsKey(fkName) ||
                !expectedColumns.get(fkName).equalsIgnoreCase(fkColumnName)) {
                throw new SQLException("Foreign key " + fkName + " in  table " + tableName + " does not match the expected definition.");
            }
        }
    }

}
