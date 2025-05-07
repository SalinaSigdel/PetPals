package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.RememberMeToken;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for RememberMeToken-related database operations
 */
public class RememberMeTokenRepository {

    /**
     * Save a new token
     * @param token The token to save
     * @return true if successful, false otherwise
     */
    public boolean save(RememberMeToken token) {
        String sql = "INSERT INTO RememberMeTokens (user_id, username, token, expiry_date) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, token.getUserId());
            pstmt.setString(2, token.getUsername());
            pstmt.setString(3, token.getToken());
            pstmt.setTimestamp(4, token.getExpiryDate());

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
     * Find a token by its value
     * @param tokenValue The token value
     * @return RememberMeToken if found, null otherwise
     */
    public RememberMeToken findByToken(String tokenValue) {
        String sql = "SELECT * FROM RememberMeTokens WHERE token = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        RememberMeToken token = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tokenValue);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                token = mapResultSetToToken(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return token;
    }

    /**
     * Find tokens by user ID
     * @param userId The user ID
     * @return List of tokens for the user
     */
    public List<RememberMeToken> findByUserId(int userId) {
        String sql = "SELECT * FROM RememberMeTokens WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<RememberMeToken> tokens = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tokens.add(mapResultSetToToken(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return tokens;
    }

    /**
     * Delete a token by its value
     * @param tokenValue The token value
     * @return true if successful, false otherwise
     */
    public boolean deleteByToken(String tokenValue) {
        String sql = "DELETE FROM RememberMeTokens WHERE token = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tokenValue);

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
     * Delete all tokens for a user
     * @param userId The user ID
     * @return true if successful, false otherwise
     */
    public boolean deleteByUserId(int userId) {
        String sql = "DELETE FROM RememberMeTokens WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected >= 0; // Success even if no rows affected
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Delete expired tokens
     * @return Number of tokens deleted
     */
    public int deleteExpiredTokens() {
        String sql = "DELETE FROM RememberMeTokens WHERE expiry_date < ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsAffected = 0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return rowsAffected;
    }

    /**
     * Map a ResultSet to a RememberMeToken object
     * @param rs The ResultSet
     * @return RememberMeToken object
     * @throws SQLException if a database error occurs
     */
    private RememberMeToken mapResultSetToToken(ResultSet rs) throws SQLException {
        RememberMeToken token = new RememberMeToken();
        token.setTokenId(rs.getInt("token_id"));
        token.setUserId(rs.getInt("user_id"));
        token.setUsername(rs.getString("username"));
        token.setToken(rs.getString("token"));
        token.setExpiryDate(rs.getTimestamp("expiry_date"));
        return token;
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
}
