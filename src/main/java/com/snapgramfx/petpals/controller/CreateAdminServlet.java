package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.repository.UserRepository;
import com.snapgramfx.petpals.util.SecurityUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet to create an admin user
 * IMPORTANT: Delete this servlet after using it once for security reasons
 */
@WebServlet(name = "createAdminServlet", urlPatterns = {"/setup-admin"})
public class CreateAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Delete any existing admin user
            UserRepository userRepository = new UserRepository();
            User existingAdmin = userRepository.findByUsername("admin");
            if (existingAdmin != null) {
                userRepository.delete(existingAdmin.getUserId());
            }

            // Create user object
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setFullName("System Administrator");
            adminUser.setEmail("admin@petpals.com");
            adminUser.setRole("admin");

            // Generate salt and hash password
            String password = "admin123";
            String salt = SecurityUtil.generateSalt();
            String hashedPassword = SecurityUtil.hashPassword(password, salt);

            // Set password and salt
            adminUser.setPassword(hashedPassword);
            adminUser.setSalt(salt);

            // Save user to database
            boolean success = userRepository.save(adminUser);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Admin User Setup</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
            out.println(".success { color: green; }");
            out.println(".error { color: red; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            if (success) {
                out.println("<h2 class='success'>Admin user created successfully!</h2>");
                out.println("<p>You can now login with:</p>");
                out.println("<ul>");
                out.println("<li><strong>Username:</strong> admin</li>");
                out.println("<li><strong>Password:</strong> admin123</li>");
                out.println("</ul>");
                out.println("<p>Please delete the CreateAdminServlet.java file after using it for security reasons.</p>");
            } else {
                out.println("<h2 class='error'>Failed to create admin user</h2>");
                out.println("<p>Please check the server logs for more information.</p>");
            }

            out.println("<p><a href='login.jsp'>Go to login page</a></p>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            out.println("<h2 class='error'>Error creating admin user</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}