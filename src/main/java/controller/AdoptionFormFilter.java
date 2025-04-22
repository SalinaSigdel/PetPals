package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/adoptionfrom.jsp")
public class AdoptionFormFilter implements Filter {
    
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
        
        // Check if this is just viewing the form or submitting it
        String method = httpRequest.getMethod();
        boolean isSubmitting = "POST".equalsIgnoreCase(method);
        
        if (isSubmitting) {
            // Only require login for form submission
            boolean isLoggedIn = (session != null && session.getAttribute("username") != null);
            
            if (isLoggedIn) {
                // User is authenticated, continue processing
                chain.doFilter(request, response);
            } else {
                // User is not logged in, store the requested URL and redirect to login
                String petId = httpRequest.getParameter("petId");
                String redirectUrl = "adoptionfrom.jsp";
                if (petId != null && !petId.isEmpty()) {
                    redirectUrl += "?petId=" + petId;
                }
                
                httpRequest.getSession().setAttribute("intendedDestination", redirectUrl);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            }
        } else {
            // Viewing the form is allowed for everyone
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
} 