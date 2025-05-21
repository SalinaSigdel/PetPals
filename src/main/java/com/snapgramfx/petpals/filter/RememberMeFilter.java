package com.snapgramfx.petpals.filter;

import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.service.UserService;
import com.snapgramfx.petpals.util.CookieUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to handle "Remember Me" functionality
 * This filter checks for remember me cookies and automatically logs in the user if valid
 */
@WebFilter(filterName = "RememberMeFilter", urlPatterns = {"/*"})
public class RememberMeFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = new UserService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the current session
        HttpSession session = httpRequest.getSession(false);

        // Check if user is already logged in
        boolean isLoggedIn = false;
        if (session != null) {
            Object userIdObj = session.getAttribute("userId");
            isLoggedIn = (userIdObj != null);
        }

        // If not logged in, check for remember me cookies
        if (!isLoggedIn) {
            String rememberedUsername = CookieUtil.getCookieValue(httpRequest, "remember_username");
            String rememberedToken = CookieUtil.getCookieValue(httpRequest, "remember_token");

            // Auto-login if remember me cookie is present
            if (rememberedUsername != null && rememberedToken != null) {
                User user = userService.authenticateWithToken(rememberedUsername, rememberedToken);

                if (user != null) {
                    // Create session
                    session = httpRequest.getSession(true);
                    session.setAttribute("user", user);
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("userRole", user.getRole());

                    // Get the requested URL
                    String requestedUrl = httpRequest.getRequestURI();
                    String contextPath = httpRequest.getContextPath();

                    // Check if this is the root or login page
                    if (requestedUrl.equals(contextPath + "/") ||
                            requestedUrl.equals(contextPath + "/index.jsp") ||
                            requestedUrl.equals(contextPath + "/login") ||
                            requestedUrl.equals(contextPath + "/login.jsp")) {

                        // Redirect based on role
                        if ("admin".equals(user.getRole())) {
                            httpResponse.sendRedirect(contextPath + "/admin-dashboard");
                            return;
                        } else {
                            // For regular users, let them continue to the home page
                            // No redirect needed
                        }
                    }
                }
            }
        }

        // Continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
