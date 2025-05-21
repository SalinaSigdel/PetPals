package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for User-related database operations
 */
public class UserRepository {

    /**
     * Find a user by username
     * @param username the username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return user;
    }

    /**
     * Find a user by email
     * @param email the email to search for
     * @return User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return user;
    }

    /**
     * Find a user by ID
     * @param userId the user ID to search for
     * @return User object if found, null otherwise
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return user;
    }

    /**
     * Save a new user to the database
     * @param user the user to save
     * @return true if successful, false otherwise
     */
    public boolean save(User user) {
        // Check if the phone and address columns exist
        boolean hasPhoneAndAddress = checkIfColumnsExist("users", "phone", "address");

        String sql;
        if (hasPhoneAndAddress) {
            sql = "INSERT INTO Users (username, password, salt, email, full_name, phone, address, role, email_notifications_enabled) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Users (username, password, salt, email, full_name, role, email_notifications_enabled) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getSalt());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getFullName());

            if (hasPhoneAndAddress) {
                pstmt.setString(6, user.getPhone());
                pstmt.setString(7, user.getAddress());
                pstmt.setString(8, user.getRole());
                pstmt.setBoolean(9, user.isEmailNotificationsEnabled());
            } else {
                pstmt.setString(6, user.getRole());
                pstmt.setBoolean(7, user.isEmailNotificationsEnabled());
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
                rs.close();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Update an existing user
     * @param user the user to update
     * @return true if successful, false otherwise
     */
    public boolean update(User user) {
        // Check if the phone and address columns exist
        boolean hasPhoneAndAddress = checkIfColumnsExist("users", "phone", "address");

        String sql;
        if (hasPhoneAndAddress) {
            sql = "UPDATE Users SET username = ?, password = ?, salt = ?, email = ?, full_name = ?, " +
                    "phone = ?, address = ?, role = ?, email_notifications_enabled = ? WHERE user_id = ?";
        } else {
            sql = "UPDATE Users SET username = ?, password = ?, salt = ?, email = ?, full_name = ?, " +
                    "role = ?, email_notifications_enabled = ? WHERE user_id = ?";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getSalt());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getFullName());

            if (hasPhoneAndAddress) {
                pstmt.setString(6, user.getPhone());
                pstmt.setString(7, user.getAddress());
                pstmt.setString(8, user.getRole());
                pstmt.setBoolean(9, user.isEmailNotificationsEnabled());
                pstmt.setInt(10, user.getUserId());
            } else {
                pstmt.setString(6, user.getRole());
                pstmt.setBoolean(7, user.isEmailNotificationsEnabled());
                pstmt.setInt(8, user.getUserId());
            }

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Delete a user by ID
     * @param userId the ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM Users";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return users;
    }

    /**
     * Find all users with pagination
     * @param offset Starting index
     * @param limit Maximum number of users to return
     * @return List of users
     */
    public List<User> findAllWithPagination(int offset, int limit) {
        String sql = "SELECT * FROM Users ORDER BY user_id DESC LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return users;
    }

    /**
     * Count all users
     * @return Total number of users
     */
    public int countAllUsers() {
        String sql = "SELECT COUNT(*) FROM Users";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return count;
    }

    /**
     * Find users by role with pagination
     * @param role Role to filter by
     * @param offset Starting index
     * @param limit Maximum number of users to return
     * @return List of users with the specified role
     */
    public List<User> findByRoleWithPagination(String role, int offset, int limit) {
        String sql = "SELECT * FROM Users WHERE role = ? ORDER BY user_id DESC LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return users;
    }

    /**
     * Count users by role
     * @param role Role to count
     * @return Number of users with the specified role
     */
    public int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM Users WHERE role = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return count;
    }

    /**
     * Authenticate a user - DEPRECATED, use UserService.authenticate() instead
     * This method is kept for backward compatibility
     * @param username the username
     * @param password the password
     * @return User object if authentication successful, null otherwise
     * @deprecated Use UserService.authenticate() which handles password hashing
     */
    @Deprecated
    public User authenticate(String username, String password) {
        // This method is no longer secure and should not be used
        // It's kept for backward compatibility only
        return findByUsername(username);
    }

    /**
     * Map a ResultSet to a User object
     * @param rs the ResultSet
     * @return User object
     * @throws SQLException if a database error occurs
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setSalt(rs.getString("salt"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));

        // Check if phone and address columns exist
        try {
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
        } catch (SQLException e) {
            // Columns don't exist, set default values
            user.setPhone("");
            user.setAddress("");
        }

        user.setRole(rs.getString("role"));
        user.setEmailNotificationsEnabled(rs.getBoolean("email_notifications_enabled"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

    /**
     * Close database resources
     * @param rs ResultSet to close
     * @param stmt Statement to close
     * @param conn Connection to close
     */
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if specified columns exist in a table
     * @param tableName the name of the table
     * @param columnNames the column names to check
     * @return true if all columns exist, false otherwise
     */
    private boolean checkIfColumnsExist(String tableName, String... columnNames) {
        Connection conn = null;
        ResultSet rs = null;
        DatabaseMetaData metaData = null;
        boolean allColumnsExist = true;

        try {
            conn = DatabaseUtil.getConnection();
            metaData = conn.getMetaData();

            for (String columnName : columnNames) {
                rs = metaData.getColumns(null, null, tableName, columnName);
                if (!rs.next()) {
                    allColumnsExist = false;
                    break;
                }
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            allColumnsExist = false;
        } finally {
            closeResources(rs, null, conn);
        }

        return allColumnsExist;
    }
}