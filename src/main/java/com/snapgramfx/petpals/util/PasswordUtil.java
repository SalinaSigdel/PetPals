package com.snapgramfx.petpals.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtil {
    
    private static final int SALT_LENGTH = 16;
    private static final String HASH_ALGORITHM = "SHA-256";
    
    /**
     * Generate a random salt for password hashing
     * @return Base64 encoded salt string
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hash a password with the provided salt
     * @param password The password to hash
     * @param salt The salt to use
     * @return Hashed password
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify if a password matches a stored hash
     * @param password The password to verify
     * @param storedHash The stored hash to compare against
     * @param storedSalt The salt used for the stored hash
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash, String storedSalt) {
        String hashedPassword = hashPassword(password, storedSalt);
        return hashedPassword.equals(storedHash);
    }
}
