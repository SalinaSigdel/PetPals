package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.PasswordResetToken;
import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.repository.PasswordResetTokenRepository;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Service class for password reset functionality
 */
public class PasswordResetService {
    private final PasswordResetTokenRepository tokenRepository;
    private final UserService userService;

    // Token expiry time in hours
    private static final int TOKEN_EXPIRY_HOURS = 24;

    public PasswordResetService() {
        this.tokenRepository = new PasswordResetTokenRepository();
        this.userService = new UserService();
    }

    /**
     * Create a password reset token for a user
     * @param email The user's email
     * @return The generated token string, or null if user not found
     */
    public String createToken(String email) {
        // Find user by email
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return null;
        }

        // Generate a unique token
        String tokenValue = UUID.randomUUID().toString();

        // Create token object
        PasswordResetToken token = new PasswordResetToken();
        token.setUserId(user.getUserId());
        token.setToken(tokenValue);
        token.setExpiryDate(calculateExpiryDate());
        token.setUsed(false);

        // Save token to database
        boolean success = tokenRepository.save(token);

        if (success) {
            return tokenValue;
        } else {
            return null;
        }
    }

    /**
     * Validate a password reset token
     * @param tokenValue The token value
     * @return User ID if valid, -1 otherwise
     */
    public int validateToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isEmpty()) {
            return -1;
        }

        // Find token in database
        PasswordResetToken token = tokenRepository.findByToken(tokenValue);

        if (token == null || !token.isValid()) {
            return -1;
        }

        return token.getUserId();
    }

    /**
     * Reset a user's password
     * @param tokenValue The token value
     * @param newPassword The new password
     * @return true if successful, false otherwise
     */
    public boolean resetPassword(String tokenValue, String newPassword) {
        // Validate token
        int userId = validateToken(tokenValue);

        if (userId == -1) {
            return false;
        }

        // Get user
        User user = userService.getUserById(userId);

        if (user == null) {
            return false;
        }

        // Update password
        boolean success = userService.updatePassword(userId, newPassword);

        if (success) {
            // Mark token as used
            tokenRepository.markAsUsed(tokenValue);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clean up expired tokens
     * @return Number of tokens deleted
     */
    public int cleanupExpiredTokens() {
        return tokenRepository.deleteExpiredTokens();
    }

    /**
     * Calculate expiry date for password reset token
     * @return Timestamp for token expiry
     */
    private Timestamp calculateExpiryDate() {
        // Calculate expiry date (current time + TOKEN_EXPIRY_HOURS hours)
        long expiryTimeMillis = System.currentTimeMillis() + (TOKEN_EXPIRY_HOURS * 60 * 60 * 1000L);
        return new Timestamp(expiryTimeMillis);
    }
}
