package com.snapgramfx.petpals.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to restrict access to user pages to authenticated users only
 */
@WebFilter(filterName = "UserAuthFilter", urlPatterns = {
        "/profile",
        "/userprofile",
        "/update-profile",
        "/my-applications",
        "/application-details",
        "/submit-application",
        "/cancel-application",
        "/userprofile.jsp"
})
public class UserAuthFilter implements Filter {

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

        // Check if user is logged in
        boolean isLoggedIn = false;
        if (session != null) {
            Object userIdObj = session.getAttribute("userId");
            isLoggedIn = (userIdObj != null);
        }

        if (isLoggedIn) {
            // User is logged in, allow access
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to login
            String redirectUrl = httpRequest.getRequestURI();
            if (httpRequest.getQueryString() != null) {
                redirectUrl += "?" + httpRequest.getQueryString();
            }

            // Store the requested URL for redirect after login
            session = httpRequest.getSession(true);
            session.setAttribute("redirectUrl", redirectUrl);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=login_required");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
