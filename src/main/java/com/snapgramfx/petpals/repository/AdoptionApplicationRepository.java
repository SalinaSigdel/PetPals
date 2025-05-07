package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.AdoptionApplication;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for AdoptionApplication-related database operations
 */
public class AdoptionApplicationRepository {

    /**
     * Find an application by ID
     * @param applicationId the application ID to search for
     * @return AdoptionApplication object if found, null otherwise
     */
    public AdoptionApplication findById(int applicationId) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.application_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AdoptionApplication application = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                application = mapResultSetToApplication(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return application;
    }

    /**
     * Save a new application to the database
     * @param application the application to save
     * @return true if successful, false otherwise
     */
    public boolean save(AdoptionApplication application) {
        String sql = "INSERT INTO Adoption_Applications (user_id, pet_id, status, applicant_name, email, phone, " +
                    "city_state, reason_for_adoption) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, application.getUserId());
            pstmt.setInt(2, application.getPetId());
            pstmt.setString(3, application.getStatus());
            pstmt.setString(4, application.getApplicantName());
            pstmt.setString(5, application.getEmail());
            pstmt.setString(6, application.getPhone());
            pstmt.setString(7, application.getCityState());
            pstmt.setString(8, application.getReasonForAdoption());

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;

            // If successful, update the pet status to 'pending'
            if (success) {
                updatePetStatus(application.getPetId(), "pending", conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Update an existing application
     * @param application the application to update
     * @return true if successful, false otherwise
     */
    public boolean update(AdoptionApplication application) {
        String sql = "UPDATE Adoption_Applications SET status = ?, rejection_reason = ? WHERE application_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, application.getStatus());
            pstmt.setString(2, application.getRejectionReason());
            pstmt.setInt(3, application.getApplicationId());

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;

            // If successful and status is 'approved' or 'rejected', update the pet status
            if (success) {
                if ("approved".equals(application.getStatus())) {
                    updatePetStatus(application.getPetId(), "adopted", conn);
                } else if ("rejected".equals(application.getStatus())) {
                    updatePetStatus(application.getPetId(), "available", conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Delete an application by ID
     * @param applicationId the ID of the application to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int applicationId) {
        String sql = "DELETE FROM Adoption_Applications WHERE application_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);

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
     * Get all applications
     * @return List of all applications
     */
    public List<AdoptionApplication> findAll() {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Find applications by user ID
     * @param userId the user ID to search for
     * @return List of applications for the user
     */
    public List<AdoptionApplication> findByUserId(int userId) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Find applications by user ID with pagination
     * @param userId User ID to filter by
     * @param offset Starting index
     * @param limit Maximum number of applications to return
     * @return List of applications for the specified user
     */
    public List<AdoptionApplication> findByUserIdWithPagination(int userId, int offset, int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.user_id = ? " +
                     "ORDER BY a.created_at DESC " +
                     "LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Count applications by user ID
     * @param userId User ID to count applications for
     * @return Number of applications for the specified user
     */
    public int countByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM Adoption_Applications WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
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
     * Find applications by status
     * @param status the status to search for
     * @return List of applications with the given status
     */
    public List<AdoptionApplication> findByStatus(String status) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.status = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Update the status of a pet
     * @param petId the ID of the pet
     * @param status the new status
     * @param conn the database connection to use
     */
    private void updatePetStatus(int petId, String status, Connection conn) {
        String sql = "UPDATE Pets SET status = ? WHERE pet_id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, petId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Find pending applications with limit
     * @param limit Maximum number of applications to return
     * @return List of pending applications
     */
    public List<AdoptionApplication> findPendingApplications(int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.status = 'pending' " +
                     "ORDER BY a.created_at DESC " +
                     "LIMIT ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Count pending applications
     * @return Number of pending applications
     */
    public int countPendingApplications() {
        String sql = "SELECT COUNT(*) FROM Adoption_Applications WHERE status = 'pending'";
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
     * Map a ResultSet to an AdoptionApplication object
     * @param rs the ResultSet
     * @return AdoptionApplication object
     * @throws SQLException if a database error occurs
     */
    private AdoptionApplication mapResultSetToApplication(ResultSet rs) throws SQLException {
        AdoptionApplication application = new AdoptionApplication();
        application.setApplicationId(rs.getInt("application_id"));
        application.setUserId(rs.getInt("user_id"));
        application.setPetId(rs.getInt("pet_id"));
        application.setStatus(rs.getString("status"));
        application.setApplicantName(rs.getString("applicant_name"));
        application.setEmail(rs.getString("email"));
        application.setPhone(rs.getString("phone"));
        application.setCityState(rs.getString("city_state"));
        application.setReasonForAdoption(rs.getString("reason_for_adoption"));
        application.setRejectionReason(rs.getString("rejection_reason"));
        application.setCreatedAt(rs.getTimestamp("created_at"));

        // Set related information if available
        try {
            application.setPetName(rs.getString("pet_name"));
        } catch (SQLException e) {
            // Column might not exist, ignore
        }

        try {
            application.setUserName(rs.getString("user_name"));
        } catch (SQLException e) {
            // Column might not exist, ignore
        }

        return application;
    }

    /**
     * Find all applications with pagination
     * @param offset Starting index
     * @param limit Maximum number of applications to return
     * @return List of applications
     */
    public List<AdoptionApplication> findAllWithPagination(int offset, int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "ORDER BY a.created_at DESC " +
                     "LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Count total number of applications
     * @return Total number of applications
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Adoption_Applications";
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
     * Find applications by status with pagination
     * @param status Status to filter by
     * @param offset Starting index
     * @param limit Maximum number of applications to return
     * @return List of applications with the given status
     */
    public List<AdoptionApplication> findByStatusWithPagination(String status, int offset, int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoption_Applications a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.status = ? " +
                     "ORDER BY a.created_at DESC " +
                     "LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                applications.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return applications;
    }

    /**
     * Count applications by status
     * @param status Status to count
     * @return Number of applications with the given status
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Adoption_Applications WHERE status = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
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