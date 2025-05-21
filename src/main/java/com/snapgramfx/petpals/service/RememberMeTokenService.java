package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.RememberMeToken;
import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.repository.RememberMeTokenRepository;
import com.snapgramfx.petpals.util.CookieUtil;
import com.snapgramfx.petpals.util.SecurityUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service class for RememberMeToken-related business logic
 */
public class RememberMeTokenService {
    private final RememberMeTokenRepository tokenRepository;

    public RememberMeTokenService() {
        this.tokenRepository = new RememberMeTokenRepository();
    }

    /**
     * Create a new remember me token for a user
     * @param user The user
     * @return The generated token string
     */
    public String createToken(User user) {
        // Delete any existing tokens for this user
        tokenRepository.deleteByUserId(user.getUserId());

        // Generate a new token
        String tokenValue = CookieUtil.generateRememberMeToken(user.getUsername(), user.getUserId(), user.getSalt());

        // Create token object
        RememberMeToken token = new RememberMeToken();
        token.setUserId(user.getUserId());
        token.setUsername(user.getUsername());
        token.setToken(tokenValue);
        token.setExpiryDate(CookieUtil.calculateTokenExpiryDate());

        // Save token to database
        boolean success = tokenRepository.save(token);

        if (success) {
            return tokenValue;
        } else {
            return null;
        }
    }

    /**
     * Validate a remember me token
     * @param tokenValue The token value
     * @return User ID if valid, -1 otherwise
     */
    public int validateToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isEmpty()) {
            return -1;
        }

        // Find token in database
        RememberMeToken token = tokenRepository.findByToken(tokenValue);

        if (token == null || token.isExpired()) {
            // Token not found or expired
            if (token != null) {
                // Delete expired token
                tokenRepository.deleteByToken(tokenValue);
            }
            return -1;
        }

        return token.getUserId();
    }

    /**
     * Delete a token
     * @param tokenValue The token value
     * @return true if successful, false otherwise
     */
    public boolean deleteToken(String tokenValue) {
        return tokenRepository.deleteByToken(tokenValue);
    }

    /**
     * Delete all tokens for a user
     * @param userId The user ID
     * @return true if successful, false otherwise
     */
    public boolean deleteUserTokens(int userId) {
        return tokenRepository.deleteByUserId(userId);
    }

    /**
     * Clean up expired tokens
     * @return Number of tokens deleted
     */
    public int cleanupExpiredTokens() {
        return tokenRepository.deleteExpiredTokens();
    }
}
