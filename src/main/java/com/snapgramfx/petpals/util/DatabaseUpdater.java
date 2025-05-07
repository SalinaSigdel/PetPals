package com.snapgramfx.petpals.util;

import java.sql.*;

/**
 * Utility class for updating the database schema
 */
public class DatabaseUpdater {
    
    /**
     * Update the database schema
     */
    public static void updateSchema() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            
            // Check if phone column exists
            if (!columnExists(conn, "users", "phone")) {
                // Add phone column
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN phone VARCHAR(20) NULL AFTER full_name");
                System.out.println("Added phone column to users table");
            }
            
            // Check if address column exists
            if (!columnExists(conn, "users", "address")) {
                // Add address column
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN address VARCHAR(255) NULL AFTER phone");
                System.out.println("Added address column to users table");
            }
            
            // Check if adoptions table exists
            if (!tableExists(conn, "adoptions")) {
                // Create adoptions table
                String createAdoptionsTable = "CREATE TABLE Adoptions (" +
                    "adoption_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "application_id INT NOT NULL, " +
                    "user_id INT NOT NULL, " +
                    "pet_id INT NOT NULL, " +
                    "adoption_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status ENUM('completed', 'returned') NOT NULL DEFAULT 'completed', " +
                    "notes TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (pet_id) REFERENCES Pets(pet_id) ON DELETE CASCADE)";
                
                stmt.executeUpdate(createAdoptionsTable);
                
                // Create indexes
                stmt.executeUpdate("CREATE INDEX idx_adoptions_user_id ON Adoptions(user_id)");
                stmt.executeUpdate("CREATE INDEX idx_adoptions_pet_id ON Adoptions(pet_id)");
                stmt.executeUpdate("CREATE INDEX idx_adoptions_application_id ON Adoptions(application_id)");
                
                System.out.println("Created Adoptions table with indexes");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) DatabaseUtil.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Check if a column exists in a table
     * @param conn the database connection
     * @param tableName the name of the table
     * @param columnName the name of the column
     * @return true if the column exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    private static boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
        boolean exists = rs.next();
        rs.close();
        return exists;
    }
    
    /**
     * Check if a table exists in the database
     * @param conn the database connection
     * @param tableName the name of the table
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
        boolean exists = rs.next();
        rs.close();
        return exists;
    }
}
