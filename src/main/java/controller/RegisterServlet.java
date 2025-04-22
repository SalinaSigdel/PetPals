package controller;

import Dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        
        // Simple validation
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            
            setError(request, "All fields are required");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Validate role (only allow "adopter" or "shelter")
        if (!role.equals("adopter") && !role.equals("shelter")) {
            role = "adopter"; // Default to adopter if invalid role
        }
        
        // Create user object
        User user = new User(name, email, username, password, role);
        
        // Save user to database
        int userId = userDAO.createUser(user);
        if (userId > 0) {
            // Registration successful - set session attributes and redirect to home
            HttpSession session = request.getSession();
            session.setAttribute("userId", String.valueOf(userId));
            session.setAttribute("username", username);
            session.setAttribute("userRole", role);
            
            response.sendRedirect("index.jsp");
        } else {
            // Registration failed - username or email already exists
            setError(request, "Username or email already exists");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    private void setError(HttpServletRequest request, String errorMessage) {
        request.setAttribute("registerError", errorMessage);
    }
} 