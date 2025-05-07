package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.PasswordResetToken;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for PasswordResetToken-related database operations
 */
public class PasswordResetTokenRepository {

    /**
     * Save a new token
     * @param token The token to save
     * @return true if successful, false otherwise
     */
    public boolean save(PasswordResetToken token) {
        String sql = "INSERT INTO PasswordResetTokens (user_id, token, expiry_date, used) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, token.getUserId());
            pstmt.setString(2, token.getToken());
            pstmt.setTimestamp(3, token.getExpiryDate());
            pstmt.setBoolean(4, token.isUsed());

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
     * @return PasswordResetToken if found, null otherwise
     */
    public PasswordResetToken findByToken(String tokenValue) {
        String sql = "SELECT * FROM PasswordResetTokens WHERE token = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PasswordResetToken token = null;

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
    public List<PasswordResetToken> findByUserId(int userId) {
        String sql = "SELECT * FROM PasswordResetTokens WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PasswordResetToken> tokens = new ArrayList<>();

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
     * Mark a token as used
     * @param tokenValue The token value
     * @return true if successful, false otherwise
     */
    public boolean markAsUsed(String tokenValue) {
        String sql = "UPDATE PasswordResetTokens SET used = true WHERE token = ?";
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
     * Delete expired tokens
     * @return Number of tokens deleted
     */
    public int deleteExpiredTokens() {
        String sql = "DELETE FROM PasswordResetTokens WHERE expiry_date < ?";
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
     * Map a ResultSet to a PasswordResetToken object
     * @param rs The ResultSet
     * @return PasswordResetToken object
     * @throws SQLException if a database error occurs
     */
    private PasswordResetToken mapResultSetToToken(ResultSet rs) throws SQLException {
        PasswordResetToken token = new PasswordResetToken();
        token.setTokenId(rs.getInt("token_id"));
        token.setUserId(rs.getInt("user_id"));
        token.setToken(rs.getString("token"));
        token.setExpiryDate(rs.getTimestamp("expiry_date"));
        token.setUsed(rs.getBoolean("used"));
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
