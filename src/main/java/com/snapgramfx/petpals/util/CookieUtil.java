package com.snapgramfx.petpals.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utility class for cookie operations
 */
public class CookieUtil {

    private static final int REMEMBER_ME_EXPIRY = 60 * 60 * 24 * 30; // 30 days in seconds

    /**
     * Set a remember me cookie
     * @param response The HttpServletResponse
     * @param username The username to remember
     * @param token The authentication token
     */
    public static void setRememberMeCookie(HttpServletResponse response, String username, String token) {
        // Create username cookie
        Cookie usernameCookie = new Cookie("remember_username", username);
        usernameCookie.setMaxAge(REMEMBER_ME_EXPIRY);
        usernameCookie.setPath("/");
        usernameCookie.setHttpOnly(true); // For security, not accessible via JavaScript

        // Create token cookie
        Cookie tokenCookie = new Cookie("remember_token", token);
        tokenCookie.setMaxAge(REMEMBER_ME_EXPIRY);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true); // For security, not accessible via JavaScript

        // Add cookies to response
        response.addCookie(usernameCookie);
        response.addCookie(tokenCookie);
    }

    /**
     * Clear remember me cookies
     * @param response The HttpServletResponse
     */
    public static void clearRememberMeCookies(HttpServletResponse response) {
        // Create username cookie with max age 0 to delete it
        Cookie usernameCookie = new Cookie("remember_username", "");
        usernameCookie.setMaxAge(0);
        usernameCookie.setPath("/");

        // Create token cookie with max age 0 to delete it
        Cookie tokenCookie = new Cookie("remember_token", "");
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");

        // Add cookies to response
        response.addCookie(usernameCookie);
        response.addCookie(tokenCookie);
    }

    /**
     * Get a cookie value by name
     * @param request The HttpServletRequest
     * @param name The name of the cookie
     * @return The cookie value, or null if not found
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Generate a remember me token for a user
     * @param username The username
     * @param userId The user ID
     * @param salt The user's salt
     * @return A token for remember me functionality
     */
    public static String generateRememberMeToken(String username, int userId, String salt) {
        // Create a unique token based on username, user ID, salt, and current time
        String tokenData = username + "|" + userId + "|" + System.currentTimeMillis() + "|" + SecurityUtil.generateSalt();
        return SecurityUtil.hashPassword(tokenData, salt);
    }

    /**
     * Calculate expiry date for remember me token
     * @return Timestamp for token expiry
     */
    public static java.sql.Timestamp calculateTokenExpiryDate() {
        // Calculate expiry date (current time + REMEMBER_ME_EXPIRY seconds)
        long expiryTimeMillis = System.currentTimeMillis() + (REMEMBER_ME_EXPIRY * 1000L);
        return new java.sql.Timestamp(expiryTimeMillis);
    }
}
