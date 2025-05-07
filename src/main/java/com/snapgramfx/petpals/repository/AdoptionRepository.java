package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.Adoption;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Adoption-related database operations
 */
public class AdoptionRepository {

    /**
     * Find an adoption by ID
     * @param adoptionId the adoption ID to search for
     * @return Adoption object if found, null otherwise
     */
    public Adoption findById(int adoptionId) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.adoption_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Adoption adoption = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adoptionId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                adoption = mapResultSetToAdoption(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoption;
    }

    /**
     * Save a new adoption to the database
     * @param adoption the adoption to save
     * @return true if successful, false otherwise
     */
    public boolean save(Adoption adoption) {
        String sql = "INSERT INTO Adoptions (application_id, user_id, pet_id, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, adoption.getApplicationId());
            pstmt.setInt(2, adoption.getUserId());
            pstmt.setInt(3, adoption.getPetId());
            pstmt.setString(4, adoption.getStatus());
            pstmt.setString(5, adoption.getNotes());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    adoption.setAdoptionId(rs.getInt(1));
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
     * Update an existing adoption
     * @param adoption the adoption to update
     * @return true if successful, false otherwise
     */
    public boolean update(Adoption adoption) {
        String sql = "UPDATE Adoptions SET status = ?, notes = ? WHERE adoption_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, adoption.getStatus());
            pstmt.setString(2, adoption.getNotes());
            pstmt.setInt(3, adoption.getAdoptionId());

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
     * Get all adoptions
     * @return List of all adoptions
     */
    public List<Adoption> findAll() {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "ORDER BY a.adoption_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Adoption> adoptions = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                adoptions.add(mapResultSetToAdoption(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoptions;
    }

    /**
     * Find adoption by application ID
     * @param applicationId The application ID to search for
     * @return Adoption object if found, null otherwise
     */
    public Adoption findByApplicationId(int applicationId) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.application_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Adoption adoption = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                adoption = mapResultSetToAdoption(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoption;
    }

    /**
     * Find adoptions by user ID
     * @param userId The user ID to filter by
     * @return List of adoptions for the specified user
     */
    public List<Adoption> findByUserId(int userId) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.user_id = ? " +
                     "ORDER BY a.adoption_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Adoption> adoptions = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                adoptions.add(mapResultSetToAdoption(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoptions;
    }

    /**
     * Count total number of completed adoptions
     * @return Total number of completed adoptions
     */
    public int countCompletedAdoptions() {
        String sql = "SELECT COUNT(*) FROM Adoptions WHERE status = 'completed'";
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
     * Calculate adoption success rate
     * @return Percentage of successful adoptions (not returned)
     */
    public double calculateSuccessRate() {
        String sql = "SELECT " +
                     "(COUNT(CASE WHEN status = 'completed' THEN 1 END) * 100.0 / COUNT(*)) " +
                     "FROM Adoptions";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double rate = 0.0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                rate = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return rate;
    }

    /**
     * Find recent adoptions with limit
     * @param limit Maximum number of adoptions to return
     * @return List of recent adoptions
     */
    public List<Adoption> findRecentAdoptions(int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "ORDER BY a.adoption_date DESC " +
                     "LIMIT ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Adoption> adoptions = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                adoptions.add(mapResultSetToAdoption(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoptions;
    }

    /**
     * Map a ResultSet to an Adoption object
     * @param rs the ResultSet
     * @return Adoption object
     * @throws SQLException if a database error occurs
     */
    private Adoption mapResultSetToAdoption(ResultSet rs) throws SQLException {
        Adoption adoption = new Adoption();
        adoption.setAdoptionId(rs.getInt("adoption_id"));
        adoption.setApplicationId(rs.getInt("application_id"));
        adoption.setUserId(rs.getInt("user_id"));
        adoption.setPetId(rs.getInt("pet_id"));
        adoption.setAdoptionDate(rs.getTimestamp("adoption_date"));
        adoption.setStatus(rs.getString("status"));
        adoption.setNotes(rs.getString("notes"));

        // Set additional display fields if available
        try {
            adoption.setPetName(rs.getString("pet_name"));
            adoption.setUserName(rs.getString("user_name"));
        } catch (SQLException e) {
            // These fields might not be in the result set, ignore
        }

        return adoption;
    }

    /**
     * Find all adoptions with pagination
     * @param offset Starting index
     * @param limit Maximum number of adoptions to return
     * @return List of adoptions
     */
    public List<Adoption> findAllWithPagination(int offset, int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "ORDER BY a.adoption_date DESC " +
                     "LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Adoption> adoptions = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                adoptions.add(mapResultSetToAdoption(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoptions;
    }

    /**
     * Count total number of adoptions
     * @return Total number of adoptions
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Adoptions";
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
     * Find adoptions by status with pagination
     * @param status Status to filter by
     * @param offset Starting index
     * @param limit Maximum number of adoptions to return
     * @return List of adoptions with the given status
     */
    public List<Adoption> findByStatusWithPagination(String status, int offset, int limit) {
        String sql = "SELECT a.*, p.name as pet_name, u.full_name as user_name " +
                     "FROM Adoptions a " +
                     "JOIN Pets p ON a.pet_id = p.pet_id " +
                     "JOIN Users u ON a.user_id = u.user_id " +
                     "WHERE a.status = ? " +
                     "ORDER BY a.adoption_date DESC " +
                     "LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Adoption> adoptions = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                adoptions.add(mapResultSetToAdoption(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return adoptions;
    }

    /**
     * Count adoptions by status
     * @param status Status to count
     * @return Number of adoptions with the given status
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Adoptions WHERE status = ?";
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
            if (conn != null) DatabaseUtil.releaseConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
