package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admindashboard.jsp")
public class AdminAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        boolean isLoggedIn = (session != null && session.getAttribute("username") != null);
        boolean isAdmin = (isLoggedIn && "admin".equals(session.getAttribute("userRole")));
        
        if (isAdmin) {
            // User is authenticated and has admin role, continue processing
            chain.doFilter(request, response);
        } else {
            // User is not an admin, store the requested URL and redirect to login
            if (!isLoggedIn) {
                // Store intended URL for redirect after login
                httpRequest.getSession().setAttribute("intendedDestination", httpRequest.getRequestURI());
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            } else {
                // User is logged in but not an admin
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
            }
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
} 