package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.Notification;
import com.snapgramfx.petpals.service.NotificationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling notification-related HTTP requests
 */
@WebServlet(name = "notificationController", urlPatterns = {
    "/notifications",
    "/mark-notification-read",
    "/mark-all-notifications-read",
    "/delete-notification"
})
public class NotificationController extends HttpServlet {
    private final NotificationService notificationService;
    
    public NotificationController() {
        this.notificationService = new NotificationService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        
        switch (path) {
            case "/notifications":
                // Show user's notifications
                handleNotificationsView(request, response);
                break;
            case "/mark-notification-read":
                // Mark a notification as read
                handleMarkAsRead(request, response);
                break;
            case "/mark-all-notifications-read":
                // Mark all notifications as read
                handleMarkAllAsRead(request, response);
                break;
            case "/delete-notification":
                // Delete a notification
                handleDeleteNotification(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }
    
    /**
     * Handle notifications view
     */
    private void handleNotificationsView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        // Get user's notifications
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        
        // Set attributes for the view
        request.setAttribute("notifications", notifications);
        
        // Forward to the view
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }
    
    /**
     * Handle marking a notification as read
     */
    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String notificationIdParam = request.getParameter("id");
        String redirectUrl = request.getParameter("redirect");
        
        if (notificationIdParam == null || notificationIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/notifications");
            return;
        }
        
        try {
            int notificationId = Integer.parseInt(notificationIdParam);
            notificationService.markAsRead(notificationId);
            
            // Redirect to the specified URL or to notifications page
            if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/notifications");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/notifications");
        }
    }
    
    /**
     * Handle marking all notifications as read
     */
    private void handleMarkAllAsRead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        // Mark all notifications as read
        notificationService.markAllAsRead(userId);
        
        // Redirect to notifications page
        response.sendRedirect(request.getContextPath() + "/notifications");
    }
    
    /**
     * Handle deleting a notification
     */
    private void handleDeleteNotification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String notificationIdParam = request.getParameter("id");
        
        if (notificationIdParam == null || notificationIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/notifications");
            return;
        }
        
        try {
            int notificationId = Integer.parseInt(notificationIdParam);
            notificationService.deleteNotification(notificationId);
            
            // Redirect to notifications page
            response.sendRedirect(request.getContextPath() + "/notifications");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/notifications");
        }
    }
}