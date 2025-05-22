package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user account deletion
 */
@WebServlet(name = "deleteAccountServlet", urlPatterns = {"/DeleteAccountServlet", "/delete-account", "/deleteAccount"})
public class DeleteAccountServlet extends HttpServlet {
    
    private final UserService userService;
    
    public DeleteAccountServlet() {
        this.userService = new UserService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        // Delete the user account
        boolean success = userService.deleteUser(userId);
        
        if (success) {
            // Invalidate the session
            session.invalidate();
            
            // Redirect to home page with success message
            response.sendRedirect(request.getContextPath() + "/login?success=Account+deleted+successfully");
        } else {
            // Redirect back to profile with error message
            response.sendRedirect(request.getContextPath() + "/userprofile?error=Failed+to+delete+account");
        }
    }
} 