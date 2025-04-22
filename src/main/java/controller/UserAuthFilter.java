package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/userprofile.jsp")
public class UserAuthFilter implements Filter {
    
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
        
        if (isLoggedIn) {
            // User is authenticated, continue processing
            chain.doFilter(request, response);
        } else {
            // User is not logged in, store the requested URL and redirect to login
            httpRequest.getSession().setAttribute("intendedDestination", httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
} 