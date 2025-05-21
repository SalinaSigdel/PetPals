package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.Notification;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Notification-related database operations
 */
public class NotificationRepository {

    /**
     * Find a notification by ID
     * @param notificationId the notification ID to search for
     * @return Notification object if found, null otherwise
     */
    public Notification findById(int notificationId) {
        String sql = "SELECT * FROM Notifications WHERE notification_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Notification notification = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, notificationId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                notification = mapResultSetToNotification(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return notification;
    }

    /**
     * Save a new notification to the database
     * @param notification the notification to save
     * @return true if successful, false otherwise
     */
    public boolean save(Notification notification) {
        String sql = "INSERT INTO Notifications (user_id, title, message, type, is_read) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, notification.getUserId());
            pstmt.setString(2, notification.getTitle());
            pstmt.setString(3, notification.getMessage());
            pstmt.setString(4, notification.getType());
            pstmt.setBoolean(5, notification.isRead());

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
     * Update an existing notification
     * @param notification the notification to update
     * @return true if successful, false otherwise
     */
    public boolean update(Notification notification) {
        String sql = "UPDATE Notifications SET title = ?, message = ?, type = ?, is_read = ? " +
                "WHERE notification_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, notification.getTitle());
            pstmt.setString(2, notification.getMessage());
            pstmt.setString(3, notification.getType());
            pstmt.setBoolean(4, notification.isRead());
            pstmt.setInt(5, notification.getNotificationId());

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
     * Delete a notification by ID
     * @param notificationId the ID of the notification to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int notificationId) {
        String sql = "DELETE FROM Notifications WHERE notification_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, notificationId);

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
     * Find notifications by user ID
     * @param userId the user ID to search for
     * @return List of notifications for the user
     */
    public List<Notification> findByUserId(int userId) {
        String sql = "SELECT * FROM Notifications WHERE user_id = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Notification> notifications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return notifications;
    }

    /**
     * Find unread notifications by user ID
     * @param userId the user ID to search for
     * @return List of unread notifications for the user
     */
    public List<Notification> findUnreadByUserId(int userId) {
        String sql = "SELECT * FROM Notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Notification> notifications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return notifications;
    }

    /**
     * Mark a notification as read
     * @param notificationId the ID of the notification to mark as read
     * @return true if successful, false otherwise
     */
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notifications SET is_read = TRUE WHERE notification_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, notificationId);

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
     * Mark all notifications for a user as read
     * @param userId the ID of the user
     * @return true if successful, false otherwise
     */
    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE Notifications SET is_read = TRUE WHERE user_id = ?";
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
     * Map a ResultSet to a Notification object
     * @param rs the ResultSet
     * @return Notification object
     * @throws SQLException if a database error occurs
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setUserId(rs.getInt("user_id"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreatedAt(rs.getTimestamp("created_at"));
        return notification;
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