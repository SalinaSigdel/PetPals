package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.service.PasswordResetService;
import com.snapgramfx.petpals.service.UserService;
import com.snapgramfx.petpals.util.SecurityUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for handling password reset requests
 */
@WebServlet(name = "passwordResetController", urlPatterns = {
        "/forgot-password",
        "/reset-password"
})
public class PasswordResetController extends HttpServlet {
    private final PasswordResetService passwordResetService;
    private final UserService userService;

    public PasswordResetController() {
        this.passwordResetService = new PasswordResetService();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/forgot-password".equals(path)) {
            // Show forgot password form
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
        } else if ("/reset-password".equals(path)) {
            // Show reset password form
            String token = request.getParameter("token");

            if (token == null || token.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/forgot-password");
                return;
            }

            // Validate token
            int userId = passwordResetService.validateToken(token);

            if (userId == -1) {
                // Invalid or expired token
                request.setAttribute("errorMessage", "Invalid or expired password reset link. Please request a new one.");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            // Token is valid, show reset password form
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/forgot-password".equals(path)) {
            // Handle forgot password form submission
            handleForgotPassword(request, response);
        } else if ("/reset-password".equals(path)) {
            // Handle reset password form submission
            handleResetPassword(request, response);
        }
    }

    /**
     * Handle forgot password form submission
     */
    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        // Validate input
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email is required");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        // Sanitize input
        email = SecurityUtil.sanitizeInput(email);

        // Create password reset token
        String token = passwordResetService.createToken(email);

        // Always show success message even if email not found (for security)
        request.setAttribute("successMessage", "If your email is registered, you will receive a password reset link shortly.");

        // In a real application, you would send an email with the reset link
        // For demonstration purposes, we'll just show the link on the page
        if (token != null) {
            String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                    request.getContextPath() + "/reset-password?token=" + token;
            request.setAttribute("resetLink", resetLink);
        }

        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }

    /**
     * Handle reset password form submission
     */
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        if (token == null || token.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }

        // Validate password strength
        if (!SecurityUtil.isStrongPassword(password)) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters and contain at least one digit, one lowercase, and one uppercase letter");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }

        // Reset password
        boolean success = passwordResetService.resetPassword(token, password);

        if (success) {
            // Password reset successful
            request.setAttribute("successMessage", "Your password has been reset successfully. You can now login with your new password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // Password reset failed
            request.setAttribute("errorMessage", "Failed to reset password. Please try again.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
        }
    }
}
