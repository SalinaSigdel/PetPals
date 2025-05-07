package com.snapgramfx.petpals.model;

import java.sql.Timestamp;

/**
 * Model class representing a password reset token
 */
public class PasswordResetToken {
    private int tokenId;
    private int userId;
    private String token;
    private Timestamp expiryDate;
    private boolean used;

    // Constructors
    public PasswordResetToken() {
    }

    public PasswordResetToken(int tokenId, int userId, String token, Timestamp expiryDate, boolean used) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
        this.used = used;
    }

    // Getters and Setters
    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Check if the token is expired
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.before(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Check if the token is valid (not expired and not used)
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return !isExpired() && !used;
    }

    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "tokenId=" + tokenId +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                ", used=" + used +
                '}';
    }
}
