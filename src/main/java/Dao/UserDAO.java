package Dao;

import model.User;
import utils.DBUtil;

import java.sql.*;

public class UserDAO {
    
    /**
     * Create a new user in the database
     * 
     * @param user The user to create
     * @return The ID of the created user, or -1 if creation failed
     */
    public int createUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int userId = -1;
        
        try {
            conn = DBUtil.getConnection();
            
            // Check if username or email already exists
            pstmt = conn.prepareStatement("SELECT id FROM users WHERE username = ? OR email = ?");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Username or email already exists
                return -1;
            }
            
            // Close resources from the first query
            rs.close();
            pstmt.close();
            
            // Insert the new user
            String sql = "INSERT INTO users (name, email, username, password, role, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, DBUtil.hashPassword(user.getPassword()));
            pstmt.setString(5, user.getRole());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, pstmt, rs);
        }
        
        return userId;
    }
    
    /**
     * Authenticate a user with username/email and password
     * 
     * @param usernameOrEmail The username or email
     * @param password The password
     * @return The authenticated user, or null if authentication failed
     */
    public User authenticateUser(String usernameOrEmail, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            pstmt.setString(3, DBUtil.hashPassword(password));
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, pstmt, rs);
        }
        
        return user;
    }
    
    /**
     * Get a user by ID
     * 
     * @param userId The user ID
     * @return The user, or null if not found
     */
    public User getUserById(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, pstmt, rs);
        }
        
        return user;
    }
    
    /**
     * Update a user's profile
     * 
     * @param user The user to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * Delete a user
     * 
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * Change a user's password
     * 
     * @param userId The ID of the user
     * @param newPassword The new password
     * @return true if password change was successful, false otherwise
     */
    public boolean changePassword(int userId, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, DBUtil.hashPassword(newPassword));
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeResources(conn, pstmt, null);
        }
    }
} 