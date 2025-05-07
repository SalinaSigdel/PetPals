package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.repository.UserRepository;
import com.snapgramfx.petpals.util.SecurityUtil;

import java.util.List;

/**
 * Service class for User-related business logic
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Authenticate a user
     * @param username the username
     * @param password the password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Verify password
            if (SecurityUtil.verifyPassword(password, user.getPassword(), user.getSalt())) {
                return user;
            }
        }

        return null;
    }

    /**
     * Register a new user
     * @param user the user to register
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }

        // Set default role if not specified
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("adopter");
        }

        // Set default email notification preference if not specified
        if (user.isEmailNotificationsEnabled()) {
            user.setEmailNotificationsEnabled(true);
        }

        // Hash the password
        String salt = SecurityUtil.generateSalt();
        String hashedPassword = SecurityUtil.hashPassword(user.getPassword(), salt);

        // Set the hashed password and salt
        user.setPassword(hashedPassword);
        user.setSalt(salt);

        return userRepository.save(user);
    }

    /**
     * Get a user by ID
     * @param userId the user ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        return userRepository.findById(userId);
    }

    /**
     * Get a user by username
     * @param username the username
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get a user by email
     * @param email the email
     * @return User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Update a user's profile
     * @param user the user with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(User user) {
        // Ensure user exists
        User existingUser = userRepository.findById(user.getUserId());
        if (existingUser == null) {
            return false;
        }

        return userRepository.update(user);
    }

    /**
     * Delete a user
     * @param userId the ID of the user to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        return userRepository.delete(userId);
    }

    /**
     * Update a user's password
     * @param userId the user ID
     * @param newPassword the new password
     * @return true if update successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        // Get existing user
        User user = userRepository.findById(userId);

        if (user == null) {
            return false;
        }

        // Generate new salt
        String salt = SecurityUtil.generateSalt();

        // Hash the new password
        String hashedPassword = SecurityUtil.hashPassword(newPassword, salt);

        // Update password and salt
        user.setPassword(hashedPassword);
        user.setSalt(salt);

        return userRepository.update(user);
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all users with pagination
     * @param offset Starting index
     * @param limit Maximum number of users to return
     * @return List of users
     */
    public List<User> getAllUsersWithPagination(int offset, int limit) {
        return userRepository.findAllWithPagination(offset, limit);
    }

    /**
     * Get total number of users
     * @return Total number of users
     */
    public int getTotalUsersCount() {
        return userRepository.countAllUsers();
    }

    /**
     * Get users by role
     * @param role the role to filter by
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(String role) {
        List<User> allUsers = getAllUsers();
        return allUsers.stream()
                .filter(user -> role.equals(user.getRole()))
                .toList();
    }

    /**
     * Get users by role with pagination
     * @param role Role to filter by
     * @param offset Starting index
     * @param limit Maximum number of users to return
     * @return List of users with the specified role
     */
    public List<User> getUsersByRoleWithPagination(String role, int offset, int limit) {
        return userRepository.findByRoleWithPagination(role, offset, limit);
    }

    /**
     * Get count of users by role
     * @param role Role to count
     * @return Number of users with the specified role
     */
    public int getUsersCountByRole(String role) {
        return userRepository.countUsersByRole(role);
    }

    /**
     * Authenticate a user using remember me token
     * @param username the username
     * @param token the remember me token
     * @return User object if authentication successful, null otherwise
     */
    public User authenticateWithToken(String username, String token) {
        if (username == null || token == null) {
            return null;
        }

        // Create RememberMeTokenService
        RememberMeTokenService tokenService = new RememberMeTokenService();

        // Validate token
        int userId = tokenService.validateToken(token);

        if (userId > 0) {
            // Get user by ID
            User user = userRepository.findById(userId);

            // Verify username matches
            if (user != null && username.equals(user.getUsername())) {
                return user;
            }
        }

        return null;
    }
}