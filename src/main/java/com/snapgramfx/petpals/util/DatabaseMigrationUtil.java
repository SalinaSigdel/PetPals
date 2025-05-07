package com.snapgramfx.petpals.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for database migrations
 */
public class DatabaseMigrationUtil {
    private static final Logger LOGGER = Logger.getLogger(DatabaseMigrationUtil.class.getName());

    /**
     * Execute SQL script from resources
     * @param scriptPath Path to the SQL script in resources
     * @return true if successful, false otherwise
     */
    public static boolean executeSqlScript(String scriptPath) {
        Connection conn = null;
        Statement stmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();

            // Read SQL script from resources
            InputStream is = DatabaseMigrationUtil.class.getClassLoader().getResourceAsStream(scriptPath);
            if (is == null) {
                LOGGER.log(Level.SEVERE, "SQL script not found: " + scriptPath);
                return false;
            }

            // Read script content
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder scriptContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                scriptContent.append(line).append(" ");
            }

            // Split script into individual statements
            String[] statements = scriptContent.toString().split(";");

            // Execute each statement
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    try {
                        stmt.execute(statement);
                    } catch (SQLException e) {
                        // Log error but continue with other statements
                        LOGGER.log(Level.WARNING, "Error executing SQL statement: " + statement, e);
                    }
                }
            }

            success = true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing SQL script: " + scriptPath, e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database resources", e);
            }
        }

        return success;
    }
}
