package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for handling user profile updates
 */
@WebServlet(name = "updateProfileServlet", urlPatterns = {"/UpdateProfileServlet"})
public class UpdateProfileServlet extends HttpServlet {

    private final UserService userService;

    public UpdateProfileServlet() {
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
            out.print("{\"success\": false, \"message\": \"You must be logged in to update your profile\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // Get form parameters
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Validate input
        if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            out.print("{\"success\": false, \"message\": \"Name and email are required\"}");
            return;
        }

        // Get current user
        User user = userService.getUserById(userId);
        if (user == null) {
            out.print("{\"success\": false, \"message\": \"User not found\"}");
            return;
        }

        // Update user information
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        // Save changes
        boolean success = userService.updateUser(user);

        if (success) {
            // Update the user object in the session
            session.setAttribute("user", user);

            out.print("{\"success\": true, \"message\": \"Profile updated successfully\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Failed to update profile\"}");
        }
    }
}
