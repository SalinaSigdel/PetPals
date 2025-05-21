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
 * Servlet for dismissing (marking as read) a single notification
 */
@WebServlet("/DismissNotificationServlet")
public class DismissNotificationServlet extends HttpServlet {
    private final NotificationService notificationService;

    public DismissNotificationServlet() {
        this.notificationService = new NotificationService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Get notification ID from request
        String notificationIdParam = request.getParameter("notificationId");
        if (notificationIdParam == null || notificationIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int notificationId = Integer.parseInt(notificationIdParam);

            // Mark notification as read
            boolean success = notificationService.markAsRead(notificationId);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
