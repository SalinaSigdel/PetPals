package com.snapgramfx.petpals.model;

import java.sql.Timestamp;

/**
 * Model class representing a remember me token
 */
public class RememberMeToken {
    private int tokenId;
    private int userId;
    private String username;
    private String token;
    private Timestamp expiryDate;

    // Constructors
    public RememberMeToken() {
    }

    public RememberMeToken(int tokenId, int userId, String username, String token, Timestamp expiryDate) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.username = username;
        this.token = token;
        this.expiryDate = expiryDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    /**
     * Check if the token is expired
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.before(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public String toString() {
        return "RememberMeToken{" +
                "tokenId=" + tokenId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
