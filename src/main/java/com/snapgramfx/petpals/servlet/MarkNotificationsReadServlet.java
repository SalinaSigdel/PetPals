package com.snapgramfx.petpals.servlet;

import com.snapgramfx.petpals.service.NotificationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for marking all notifications as read
 */
@WebServlet("/MarkNotificationsReadServlet")
public class MarkNotificationsReadServlet extends HttpServlet {
    private final NotificationService notificationService;

    public MarkNotificationsReadServlet() {
        this.notificationService = new NotificationService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // Mark all notifications as read
        notificationService.markAllAsRead(userId);

        // Redirect back to the profile page
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
