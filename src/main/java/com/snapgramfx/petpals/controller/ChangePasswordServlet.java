package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.service.UserService;
import com.snapgramfx.petpals.util.SecurityUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for handling password changes
 */
@WebServlet(name = "changePasswordServlet", urlPatterns = {"/ChangePasswordServlet"})
public class ChangePasswordServlet extends HttpServlet {
    
    private final UserService userService;
    
    public ChangePasswordServlet() {
        this.userService = new UserService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            out.print("{\"success\": false, \"message\": \"You must be logged in to change your password\"}");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        // Get form parameters
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (currentPassword == null || currentPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            
            out.print("{\"success\": false, \"message\": \"All fields are required\"}");
            return;
        }
        
        // Check if new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            out.print("{\"success\": false, \"message\": \"New password and confirmation do not match\"}");
            return;
        }
        
        // Validate password strength
        if (!SecurityUtil.isStrongPassword(newPassword)) {
            out.print("{\"success\": false, \"message\": \"Password must be at least 8 characters and contain at least one digit, one lowercase, and one uppercase letter\"}");
            return;
        }
        
        // Get current user
        User user = userService.getUserById(userId);
        if (user == null) {
            out.print("{\"success\": false, \"message\": \"User not found\"}");
            return;
        }
        
        // Verify current password
        if (!SecurityUtil.verifyPassword(currentPassword, user.getPassword(), user.getSalt())) {
            out.print("{\"success\": false, \"message\": \"Current password is incorrect\"}");
            return;
        }
        
        // Generate new salt and hash for the new password
        String newSalt = SecurityUtil.generateSalt();
        String hashedNewPassword = SecurityUtil.hashPassword(newPassword, newSalt);
        
        // Update user's password
        user.setPassword(hashedNewPassword);
        user.setSalt(newSalt);
        
        // Save changes
        boolean success = userService.updateUser(user);
        
        if (success) {
            // Update the user object in the session
            session.setAttribute("user", user);
            
            out.print("{\"success\": true, \"message\": \"Password updated successfully\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Failed to update password\"}");
        }
    }
}
