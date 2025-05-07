package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.Shelter;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Shelter-related database operations
 */
public class ShelterRepository {

    /**
     * Find a shelter by ID
     * @param shelterId the shelter ID to search for
     * @return Shelter object if found, null otherwise
     */
    public Shelter findById(int shelterId) {
        String sql = "SELECT * FROM Shelters WHERE shelter_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Shelter shelter = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shelterId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                shelter = mapResultSetToShelter(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }
        
        return shelter;
    }
    
    /**
     * Save a new shelter to the database
     * @param shelter the shelter to save
     * @return true if successful, false otherwise
     */
    public boolean save(Shelter shelter) {
        String sql = "INSERT INTO Shelters (name, address, city, state, zip_code, phone, email, website, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, shelter.getName());
            pstmt.setString(2, shelter.getAddress());
            pstmt.setString(3, shelter.getCity());
            pstmt.setString(4, shelter.getState());
            pstmt.setString(5, shelter.getZipCode());
            pstmt.setString(6, shelter.getPhone());
            pstmt.setString(7, shelter.getEmail());
            pstmt.setString(8, shelter.getWebsite());
            pstmt.setString(9, shelter.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    shelter.setShelterId(rs.getInt(1));
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
     * Update an existing shelter
     * @param shelter the shelter to update
     * @return true if successful, false otherwise
     */
    public boolean update(Shelter shelter) {
        String sql = "UPDATE Shelters SET name = ?, address = ?, city = ?, state = ?, zip_code = ?, " +
                    "phone = ?, email = ?, website = ?, description = ? WHERE shelter_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, shelter.getName());
            pstmt.setString(2, shelter.getAddress());
            pstmt.setString(3, shelter.getCity());
            pstmt.setString(4, shelter.getState());
            pstmt.setString(5, shelter.getZipCode());
            pstmt.setString(6, shelter.getPhone());
            pstmt.setString(7, shelter.getEmail());
            pstmt.setString(8, shelter.getWebsite());
            pstmt.setString(9, shelter.getDescription());
            pstmt.setInt(10, shelter.getShelterId());
            
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
     * Delete a shelter by ID
     * @param shelterId the ID of the shelter to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int shelterId) {
        String sql = "DELETE FROM Shelters WHERE shelter_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shelterId);
            
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
     * Get all shelters
     * @return List of all shelters
     */
    public List<Shelter> findAll() {
        String sql = "SELECT * FROM Shelters ORDER BY name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Shelter> shelters = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                shelters.add(mapResultSetToShelter(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }
        
        return shelters;
    }
    
    /**
     * Find shelters by state
     * @param state The state to filter by
     * @return List of shelters in the specified state
     */
    public List<Shelter> findByState(String state) {
        String sql = "SELECT * FROM Shelters WHERE state = ? ORDER BY name";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Shelter> shelters = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, state);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                shelters.add(mapResultSetToShelter(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }
        
        return shelters;
    }
    
    /**
     * Count total number of shelters
     * @return Total number of shelters
     */
    public int countShelters() {
        String sql = "SELECT COUNT(*) FROM Shelters";
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
     * Map a ResultSet to a Shelter object
     * @param rs the ResultSet
     * @return Shelter object
     * @throws SQLException if a database error occurs
     */
    private Shelter mapResultSetToShelter(ResultSet rs) throws SQLException {
        Shelter shelter = new Shelter();
        shelter.setShelterId(rs.getInt("shelter_id"));
        shelter.setName(rs.getString("name"));
        shelter.setAddress(rs.getString("address"));
        shelter.setCity(rs.getString("city"));
        shelter.setState(rs.getString("state"));
        shelter.setZipCode(rs.getString("zip_code"));
        shelter.setPhone(rs.getString("phone"));
        shelter.setEmail(rs.getString("email"));
        shelter.setWebsite(rs.getString("website"));
        shelter.setDescription(rs.getString("description"));
        shelter.setCreatedAt(rs.getTimestamp("created_at"));
        return shelter;
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
