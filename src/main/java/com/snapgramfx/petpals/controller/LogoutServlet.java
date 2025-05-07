package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.service.RememberMeTokenService;
import com.snapgramfx.petpals.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling user logout
 */
@WebServlet(name = "logoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    /**
     * Handles GET requests for logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session
        HttpSession session = request.getSession(false);

        // Get user ID from session if available
        Integer userId = null;
        if (session != null) {
            userId = (Integer) session.getAttribute("userId");
        }

        // Get remember me token from cookie
        String token = CookieUtil.getCookieValue(request, "remember_token");

        // Delete token from database if it exists
        if (token != null && !token.isEmpty()) {
            RememberMeTokenService tokenService = new RememberMeTokenService();
            tokenService.deleteToken(token);
        }

        // If user ID is available, delete all tokens for this user
        if (userId != null) {
            RememberMeTokenService tokenService = new RememberMeTokenService();
            tokenService.deleteUserTokens(userId);
        }

        // If session exists, invalidate it
        if (session != null) {
            session.invalidate();
        }

        // Clear remember me cookies
        CookieUtil.clearRememberMeCookies(response);

        // Redirect to home page
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
