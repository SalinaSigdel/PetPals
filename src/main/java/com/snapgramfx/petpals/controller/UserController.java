package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.Adoption;
import com.snapgramfx.petpals.model.AdoptionApplication;
import com.snapgramfx.petpals.model.Notification;
import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.service.AdoptionApplicationService;
import com.snapgramfx.petpals.service.AdoptionService;
import com.snapgramfx.petpals.service.NotificationService;
import com.snapgramfx.petpals.service.RememberMeTokenService;
import com.snapgramfx.petpals.service.UserService;
import com.snapgramfx.petpals.util.SecurityUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.snapgramfx.petpals.util.CookieUtil;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling user-related HTTP requests
 */
@WebServlet(name = "userController", urlPatterns = {
    "/login",
    "/register",
    "/logout",
    "/profile",
    "/update-profile"
})
public class UserController extends HttpServlet {
    private final UserService userService;
    private final NotificationService notificationService;
    private final AdoptionApplicationService adoptionApplicationService;
    private final AdoptionService adoptionService;

    public UserController() {
        this.userService = new UserService();
        this.notificationService = new NotificationService();
        this.adoptionApplicationService = new AdoptionApplicationService();
        this.adoptionService = new AdoptionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/login":
                // Show login page
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                break;
            case "/register":
                // Show registration page
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                break;
            case "/logout":
                // Handle logout
                handleLogout(request, response);
                break;
            case "/profile":
                // Show user profile
                handleProfileView(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/login":
                // Handle login form submission
                handleLogin(request, response);
                break;
            case "/register":
                // Handle registration form submission
                handleRegistration(request, response);
                break;
            case "/update-profile":
                // Handle profile update
                handleProfileUpdate(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    /**
     * Handle user login
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("remember");

        // Validate input
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Sanitize input to prevent XSS
        username = SecurityUtil.sanitizeInput(username);
        // Don't sanitize password as it will be hashed

        // Authenticate user
        User user = userService.authenticate(username, password);

        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole());

            // Handle "Remember Me" functionality
            if (rememberMe != null && rememberMe.equals("on")) {
                // Create RememberMeTokenService
                RememberMeTokenService tokenService = new RememberMeTokenService();

                // Generate and save token
                String token = tokenService.createToken(user);

                if (token != null) {
                    // Set remember me cookies
                    CookieUtil.setRememberMeCookie(response, username, token);
                }
            }

            // Check if there's a redirect URL stored in the session from filters
            String redirectUrl = (String) session.getAttribute("redirectUrl");

            // If no redirect URL in session, check for redirect parameter
            if (redirectUrl == null || redirectUrl.isEmpty()) {
                redirectUrl = request.getParameter("redirect");
            }

            // Remove the redirect URL from session
            session.removeAttribute("redirectUrl");

            // Redirect based on role or redirect parameter
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                // For security, only redirect to internal URLs
                if (redirectUrl.startsWith(request.getContextPath()) || redirectUrl.startsWith("/")) {
                    response.sendRedirect(redirectUrl);
                    return;
                }
            }

            // Default redirects based on role
            if ("admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * Handle user registration
     */
    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!SecurityUtil.isValidEmail(email)) {
            request.setAttribute("errorMessage", "Invalid email format");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate username format
        if (!SecurityUtil.isValidUsername(username)) {
            request.setAttribute("errorMessage", "Username must be 3-20 characters and contain only letters, numbers, and underscores");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate password strength
        if (!SecurityUtil.isStrongPassword(password)) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters and contain at least one digit, one lowercase, and one uppercase letter");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Sanitize inputs to prevent XSS
        username = SecurityUtil.sanitizeInput(username);
        email = SecurityUtil.sanitizeInput(email);
        fullName = SecurityUtil.sanitizeInput(fullName);
        // Don't sanitize password as it will be hashed

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Will be hashed in the service layer
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole("adopter"); // Default role
        user.setEmailNotificationsEnabled(true); // Default setting

        // Register user
        boolean success = userService.registerUser(user);

        if (success) {
            // Redirect to login page with success message
            request.setAttribute("successMessage", "Registration successful! Please log in.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Username already exists or registration failed");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    /**
     * Handle user logout
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear remember me cookies
        CookieUtil.clearRememberMeCookies(response);

        response.sendRedirect(request.getContextPath() + "/");
    }

    /**
     * Handle user profile view
     */
    private void handleProfileView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        User user = userService.getUserById(userId);

        if (user != null) {
            // Get user's notifications
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            request.setAttribute("notifications", notifications);

            // Get user's adoption applications
            List<AdoptionApplication> applications = adoptionApplicationService.getApplicationsByUserId(userId);
            request.setAttribute("applications", applications);

            // Get user's completed adoptions
            List<Adoption> adoptions = adoptionService.getAdoptionsByUser(userId);
            request.setAttribute("adoptions", adoptions);

            // Set user attribute
            request.setAttribute("user", user);

            // Check for success message in session
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                // Remove from session after retrieving
                session.removeAttribute("successMessage");
            }

            // Forward to the view
            request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
        } else {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /**
     * Handle user profile update
     */
    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        User user = userService.getUserById(userId);

        if (user == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Update user information
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        String emailNotifications = request.getParameter("emailNotifications");

        // Validate input
        if (email == null || email.trim().isEmpty() || fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and full name are required");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!SecurityUtil.isValidEmail(email)) {
            request.setAttribute("errorMessage", "Invalid email format");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
            return;
        }

        // Sanitize inputs to prevent XSS
        email = SecurityUtil.sanitizeInput(email);
        fullName = SecurityUtil.sanitizeInput(fullName);

        // Update basic information
        user.setEmail(email);
        user.setFullName(fullName);
        user.setEmailNotificationsEnabled(emailNotifications != null && emailNotifications.equals("on"));

        // Update password if provided
        if (currentPassword != null && !currentPassword.trim().isEmpty() &&
            newPassword != null && !newPassword.trim().isEmpty() &&
            confirmNewPassword != null && !confirmNewPassword.trim().isEmpty()) {

            // Verify current password using SecurityUtil
            if (!SecurityUtil.verifyPassword(currentPassword, user.getPassword(), user.getSalt())) {
                request.setAttribute("errorMessage", "Current password is incorrect");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
                return;
            }

            // Check if new passwords match
            if (!newPassword.equals(confirmNewPassword)) {
                request.setAttribute("errorMessage", "New passwords do not match");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
                return;
            }

            // Validate password strength
            if (!SecurityUtil.isStrongPassword(newPassword)) {
                request.setAttribute("errorMessage", "Password must be at least 8 characters and contain at least one digit, one lowercase, and one uppercase letter");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
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
            request.setAttribute("successMessage", "Profile updated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to update profile");
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/userprofile.jsp").forward(request, response);
    }
}