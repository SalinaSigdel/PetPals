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
import java.util.List;

/**
 * Controller for handling admin user management
 */
@WebServlet(name = "adminUserManagementServlet", urlPatterns = {
    "/admin-users",
    "/admin-edit-user",
    "/admin-delete-user",
    "/admin-update-user"
})
public class AdminUserManagementServlet extends HttpServlet {
    private final UserService userService;

    public AdminUserManagementServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

        if (!isAdmin) {
            response.sendRedirect("login?error=unauthorized");
            return;
        }

        String path = request.getServletPath();

        switch (path) {
            case "/admin-users":
                // Show all users
                showAllUsers(request, response);
                break;
            case "/admin-edit-user":
                // Show edit user form
                showEditUserForm(request, response);
                break;
            default:
                response.sendRedirect("admin-dashboard");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

        if (!isAdmin) {
            response.sendRedirect("login?error=unauthorized");
            return;
        }

        String path = request.getServletPath();

        switch (path) {
            case "/admin-update-user":
                // Handle user update
                handleUserUpdate(request, response);
                break;
            case "/admin-delete-user":
                // Handle user deletion
                handleUserDeletion(request, response);
                break;
            default:
                response.sendRedirect("admin-dashboard");
                break;
        }
    }

    /**
     * Show all users
     */
    private void showAllUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get users with pagination
        int page = 1;
        int pageSize = 10;
        String role = request.getParameter("role");

        // Default to all if no role specified
        if (role == null || role.isEmpty()) {
            role = "all";
        }

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            // Use default page value
        }

        List<User> users;
        int totalUsersCount;

        if ("all".equals(role)) {
            users = userService.getAllUsersWithPagination((page - 1) * pageSize, pageSize);
            totalUsersCount = userService.getTotalUsersCount();
        } else {
            users = userService.getUsersByRoleWithPagination(role, (page - 1) * pageSize, pageSize);
            totalUsersCount = userService.getUsersCountByRole(role);
        }

        int totalPages = (int) Math.ceil((double) totalUsersCount / pageSize);

        request.setAttribute("users", users);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentRole", role);
        request.setAttribute("totalUsers", totalUsersCount);

        // Forward to the JSP
        request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
    }

    /**
     * Show edit user form
     */
    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request
        String userIdParam = request.getParameter("id");

        if (userIdParam == null || userIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "User ID is required");
            response.sendRedirect("admin-users");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);

            // Get user by ID
            User user = userService.getUserById(userId);

            if (user == null) {
                request.setAttribute("errorMessage", "User not found");
                response.sendRedirect("admin-users");
                return;
            }

            // Set user attribute
            request.setAttribute("user", user);

            // Forward to the JSP
            request.getRequestDispatcher("/admin-edit-user.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect("admin-users");
        }
    }

    /**
     * Handle user update
     */
    private void handleUserUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request
        String userIdParam = request.getParameter("userId");

        if (userIdParam == null || userIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "User ID is required");
            response.sendRedirect("admin-users");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);

            // Get user by ID
            User user = userService.getUserById(userId);

            if (user == null) {
                request.setAttribute("errorMessage", "User not found");
                response.sendRedirect("admin-users");
                return;
            }

            // Get form data
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String role = request.getParameter("role");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            String emailNotifications = request.getParameter("emailNotifications");

            // Validate form data
            if (username == null || username.isEmpty() || email == null || email.isEmpty() ||
                fullName == null || fullName.isEmpty() || role == null || role.isEmpty()) {
                request.setAttribute("errorMessage", "All fields are required");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin-edit-user.jsp").forward(request, response);
                return;
            }

            // Validate email format
            if (!SecurityUtil.isValidEmail(email)) {
                request.setAttribute("errorMessage", "Invalid email format");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin-edit-user.jsp").forward(request, response);
                return;
            }

            // Sanitize inputs to prevent XSS
            username = SecurityUtil.sanitizeInput(username);
            email = SecurityUtil.sanitizeInput(email);
            fullName = SecurityUtil.sanitizeInput(fullName);

            // Update user object
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRole(role);
            user.setEmailNotificationsEnabled(emailNotifications != null && emailNotifications.equals("on"));

            // Update password if provided
            if (newPassword != null && !newPassword.isEmpty() && confirmPassword != null && !confirmPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("errorMessage", "Passwords do not match");
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/admin-edit-user.jsp").forward(request, response);
                    return;
                }

                // Hash the new password
                String salt = SecurityUtil.generateSalt();
                String hashedPassword = SecurityUtil.hashPassword(newPassword, salt);

                // Update password and salt
                user.setPassword(hashedPassword);
                user.setSalt(salt);
            }

            // Save changes
            boolean success = userService.updateUser(user);

            if (success) {
                request.setAttribute("successMessage", "User updated successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to update user");
            }

            // Redirect to users list
            response.sendRedirect("admin-users");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect("admin-users");
        }
    }

    /**
     * Handle user deletion
     */
    private void handleUserDeletion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request
        String userIdParam = request.getParameter("id");

        if (userIdParam == null || userIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "User ID is required");
            response.sendRedirect("admin-users");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);

            // Check if user is trying to delete themselves
            HttpSession session = request.getSession();
            Object currentUserIdObj = session.getAttribute("userId");
            int currentUserId = (currentUserIdObj != null) ? (Integer) currentUserIdObj : 0;

            if (userId == currentUserId) {
                request.setAttribute("errorMessage", "You cannot delete your own account");
                response.sendRedirect("admin-users");
                return;
            }

            // Delete user
            boolean success = userService.deleteUser(userId);

            if (success) {
                request.setAttribute("successMessage", "User deleted successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to delete user");
            }

            // Redirect to users list
            response.sendRedirect("admin-users");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect("admin-users");
        }
    }
}
