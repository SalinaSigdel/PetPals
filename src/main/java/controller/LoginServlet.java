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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean rememberMe = "on".equals(request.getParameter("remember"));
        
        // Validate input
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            setErrorAndRedirect(request, response, "Username and password are required");
            return;
        }
        
        // Attempt to authenticate the user
        User user = userDAO.authenticateUser(username, password);
        
        if (user != null) {
            // Authentication successful
            HttpSession session = request.getSession();
            session.setAttribute("userId", String.valueOf(user.getId()));
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole());
            
            // Set session timeout (30 minutes by default, 7 days if "remember me" is checked)
            if (rememberMe) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days in seconds
            }
            
            // Redirect to home page or intended destination
            String redirectUrl = (String) session.getAttribute("intendedDestination");
            if (redirectUrl != null) {
                session.removeAttribute("intendedDestination");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("index.jsp");
            }
        } else {
            // Authentication failed
            setErrorAndRedirect(request, response, "Invalid username or password");
        }
    }

    private void setErrorAndRedirect(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("loginError", errorMessage);
        response.sendRedirect("login.jsp");
    }
} 