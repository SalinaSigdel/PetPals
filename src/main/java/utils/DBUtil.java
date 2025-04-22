package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/petpals_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // Get database connection
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Close resources
    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Password hashing (a simple method for demonstration - in production use a proper hashing library)
    public static String hashPassword(String password) {
        // In a real application, use a secure hashing algorithm like BCrypt
        // For this demo, we'll just do a simple hash
        int hash = 7;
        for (int i = 0; i < password.length(); i++) {
            hash = hash * 31 + password.charAt(i);
        }
        return String.valueOf(hash);
    }
} 