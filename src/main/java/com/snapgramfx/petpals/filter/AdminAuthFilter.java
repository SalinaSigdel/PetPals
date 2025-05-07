package com.snapgramfx.petpals.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to restrict access to admin pages to users with admin role only
 */
@WebFilter(filterName = "AdminAuthFilter", urlPatterns = {
    "/admin-dashboard", 
    "/admin-users", 
    "/admin-edit-user", 
    "/admin-delete-user", 
    "/admin-update-user",
    "/add-pet",
    "/edit-pet",
    "/delete-pet",
    "/approve-application",
    "/reject-application"
})
public class AdminAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get the session
        HttpSession session = httpRequest.getSession(false);
        
        // Check if user is logged in and is an admin
        boolean isAdmin = false;
        if (session != null) {
            Object userIdObj = session.getAttribute("userId");
            String userRole = (String) session.getAttribute("userRole");
            isAdmin = (userIdObj != null && "admin".equals(userRole));
        }
        
        if (isAdmin) {
            // User is an admin, allow access
            chain.doFilter(request, response);
        } else {
            // User is not an admin, redirect to login with error message
            httpRequest.setAttribute("errorMessage", "You need administrator privileges to access this page");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=unauthorized");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
