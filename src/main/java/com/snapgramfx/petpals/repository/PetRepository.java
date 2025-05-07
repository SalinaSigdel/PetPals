package com.snapgramfx.petpals.repository;

import com.snapgramfx.petpals.model.Pet;
import com.snapgramfx.petpals.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Pet-related database operations
 */
public class PetRepository {

    /**
     * Find a pet by ID
     * @param petId the pet ID to search for
     * @return Pet object if found, null otherwise
     */
    public Pet findById(int petId) {
        String sql = "SELECT * FROM Pets WHERE pet_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Pet pet = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, petId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pet = mapResultSetToPet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return pet;
    }

    /**
     * Save a new pet to the database
     * @param pet the pet to save
     * @return true if successful, false otherwise
     */
    public boolean save(Pet pet) {
        String sql = "INSERT INTO Pets (name, type, breed, age, gender, weight, description, image_url, status, badge) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getType());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getAge());
            pstmt.setString(5, pet.getGender());
            pstmt.setString(6, pet.getWeight());
            pstmt.setString(7, pet.getDescription());
            pstmt.setString(8, pet.getImageUrl());
            pstmt.setString(9, pet.getStatus());
            pstmt.setString(10, pet.getBadge());

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            // If the badge column doesn't exist yet, try without it
            if (e.getMessage().contains("Unknown column 'badge'")) {
                return saveWithoutBadge(pet);
            } else {
                e.printStackTrace();
            }
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Save a new pet to the database without the badge field
     * This is a fallback method in case the badge column doesn't exist yet
     * @param pet the pet to save
     * @return true if successful, false otherwise
     */
    private boolean saveWithoutBadge(Pet pet) {
        String sql = "INSERT INTO Pets (name, type, breed, age, gender, weight, description, image_url, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getType());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getAge());
            pstmt.setString(5, pet.getGender());
            pstmt.setString(6, pet.getWeight());
            pstmt.setString(7, pet.getDescription());
            pstmt.setString(8, pet.getImageUrl());
            pstmt.setString(9, pet.getStatus());

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
     * Update an existing pet
     * @param pet the pet to update
     * @return true if successful, false otherwise
     */
    public boolean update(Pet pet) {
        String sql = "UPDATE Pets SET name = ?, type = ?, breed = ?, age = ?, gender = ?, " +
                    "weight = ?, description = ?, image_url = ?, status = ?, badge = ? WHERE pet_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getType());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getAge());
            pstmt.setString(5, pet.getGender());
            pstmt.setString(6, pet.getWeight());
            pstmt.setString(7, pet.getDescription());
            pstmt.setString(8, pet.getImageUrl());
            pstmt.setString(9, pet.getStatus());
            pstmt.setString(10, pet.getBadge());
            pstmt.setInt(11, pet.getPetId());

            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            // If the badge column doesn't exist yet, try without it
            if (e.getMessage().contains("Unknown column 'badge'")) {
                return updateWithoutBadge(pet);
            } else {
                e.printStackTrace();
            }
        } finally {
            closeResources(null, pstmt, conn);
        }

        return success;
    }

    /**
     * Update an existing pet without the badge field
     * This is a fallback method in case the badge column doesn't exist yet
     * @param pet the pet to update
     * @return true if successful, false otherwise
     */
    private boolean updateWithoutBadge(Pet pet) {
        String sql = "UPDATE Pets SET name = ?, type = ?, breed = ?, age = ?, gender = ?, " +
                    "weight = ?, description = ?, image_url = ?, status = ? WHERE pet_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getType());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getAge());
            pstmt.setString(5, pet.getGender());
            pstmt.setString(6, pet.getWeight());
            pstmt.setString(7, pet.getDescription());
            pstmt.setString(8, pet.getImageUrl());
            pstmt.setString(9, pet.getStatus());
            pstmt.setInt(10, pet.getPetId());

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
     * Delete a pet by ID
     * @param petId the ID of the pet to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int petId) {
        String sql = "DELETE FROM Pets WHERE pet_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, petId);

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
     * Get all pets
     * @return List of all pets
     */
    public List<Pet> findAll() {
        String sql = "SELECT * FROM Pets";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pet> pets = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return pets;
    }

    /**
     * Find available pets with optional filters
     * @param search Optional search term for name or breed
     * @param petType Optional pet type filter
     * @param ageGroup Optional age group filter
     * @return List of matching pets
     */
    public List<Pet> findAvailablePets(String search, String petType, String ageGroup) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Pets WHERE status = 'available'");
        List<Object> params = new ArrayList<>();

        // Add search condition if provided
        if (search != null && !search.trim().isEmpty()) {
            sqlBuilder.append(" AND (name LIKE ? OR breed LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
        }

        // Add pet type filter if provided
        if (petType != null && !petType.trim().isEmpty()) {
            sqlBuilder.append(" AND type = ?");
            params.add(petType);
        }

        // Add age group filter if provided
        if (ageGroup != null && !ageGroup.trim().isEmpty()) {
            if (ageGroup.equals("puppy")) {
                sqlBuilder.append(" AND age < 1");
            } else if (ageGroup.equals("young")) {
                sqlBuilder.append(" AND age >= 1 AND age < 3");
            } else if (ageGroup.equals("adult")) {
                sqlBuilder.append(" AND age >= 3 AND age < 8");
            } else if (ageGroup.equals("senior")) {
                sqlBuilder.append(" AND age >= 8");
            }
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pet> pets = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sqlBuilder.toString());

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return pets;
    }

    /**
     * Map a ResultSet to a Pet object
     * @param rs the ResultSet
     * @return Pet object
     * @throws SQLException if a database error occurs
     */
    private Pet mapResultSetToPet(ResultSet rs) throws SQLException {
        Pet pet = new Pet();
        pet.setPetId(rs.getInt("pet_id"));
        pet.setName(rs.getString("name"));
        pet.setType(rs.getString("type"));
        pet.setBreed(rs.getString("breed"));
        pet.setAge(rs.getString("age"));
        pet.setGender(rs.getString("gender"));
        pet.setWeight(rs.getString("weight"));
        pet.setDescription(rs.getString("description"));
        pet.setImageUrl(rs.getString("image_url"));
        pet.setStatus(rs.getString("status"));
        pet.setCreatedAt(rs.getTimestamp("created_at"));

        // Try to get badge field if it exists in the database
        try {
            pet.setBadge(rs.getString("badge"));
        } catch (SQLException e) {
            // Badge field might not exist in the database yet, set default value
            pet.setBadge("");
        }

        return pet;
    }

    /**
     * Count all pets
     * @return Total number of pets
     */
    public int countAllPets() {
        String sql = "SELECT COUNT(*) FROM Pets";
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
     * Count available pets
     * @return Number of available pets
     */
    public int countAvailablePets() {
        String sql = "SELECT COUNT(*) FROM Pets WHERE status = 'available'";
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
     * Find recent pets
     * @param limit Maximum number of pets to return
     * @return List of recent pets
     */
    public List<Pet> findRecentPets(int limit) {
        String sql = "SELECT * FROM Pets ORDER BY pet_id DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pet> pets = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return pets;
    }

    /**
     * Find pets with pagination and optional filters
     * @param search Optional search term for name or breed
     * @param petType Optional pet type filter
     * @param ageGroup Optional age group filter
     * @param page Page number (1-based)
     * @param recordsPerPage Number of records per page
     * @return List of matching pets for the specified page
     */
    public List<Pet> findPetsWithPagination(String search, String petType, String ageGroup, int page, int recordsPerPage) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Pets WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Add search condition if provided
        if (search != null && !search.trim().isEmpty()) {
            sqlBuilder.append(" AND (name LIKE ? OR breed LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
        }

        // Add pet type filter if provided
        if (petType != null && !petType.trim().isEmpty() && !petType.equals("all")) {
            sqlBuilder.append(" AND type = ?");
            params.add(petType);
        }

        // Add age group filter if provided
        if (ageGroup != null && !ageGroup.trim().isEmpty() && !ageGroup.equals("all")) {
            if (ageGroup.equals("puppy")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Puppy%");
            } else if (ageGroup.equals("young")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Young%");
            } else if (ageGroup.equals("adult")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Adult%");
            } else if (ageGroup.equals("senior")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Senior%");
            }
        }

        // Add order by and pagination
        sqlBuilder.append(" ORDER BY pet_id DESC LIMIT ?, ?");

        // Calculate offset
        int offset = (page - 1) * recordsPerPage;
        params.add(offset);
        params.add(recordsPerPage);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pet> pets = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sqlBuilder.toString());

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return pets;
    }

    /**
     * Count pets with optional filters
     * @param search Optional search term for name or breed
     * @param petType Optional pet type filter
     * @param ageGroup Optional age group filter
     * @return Total number of matching pets
     */
    public int countPetsWithFilters(String search, String petType, String ageGroup) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM Pets WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Add search condition if provided
        if (search != null && !search.trim().isEmpty()) {
            sqlBuilder.append(" AND (name LIKE ? OR breed LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
        }

        // Add pet type filter if provided
        if (petType != null && !petType.trim().isEmpty() && !petType.equals("all")) {
            sqlBuilder.append(" AND type = ?");
            params.add(petType);
        }

        // Add age group filter if provided
        if (ageGroup != null && !ageGroup.trim().isEmpty() && !ageGroup.equals("all")) {
            if (ageGroup.equals("puppy")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Puppy%");
            } else if (ageGroup.equals("young")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Young%");
            } else if (ageGroup.equals("adult")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Adult%");
            } else if (ageGroup.equals("senior")) {
                sqlBuilder.append(" AND age LIKE ?");
                params.add("Senior%");
            }
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sqlBuilder.toString());

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

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
     * Find all pets with pagination
     * @param offset Starting index
     * @param limit Maximum number of pets to return
     * @return List of pets
     */
    public List<Pet> findAllWithPagination(int offset, int limit) {
        String sql = "SELECT * FROM Pets ORDER BY pet_id DESC LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pet> pets = new ArrayList<>();

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }

        return pets;
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