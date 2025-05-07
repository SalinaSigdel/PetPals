package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.Notification;
import com.snapgramfx.petpals.repository.NotificationRepository;

import java.util.List;

/**
 * Service class for Notification-related business logic
 */
public class NotificationService {
    private final NotificationRepository notificationRepository;
    
    public NotificationService() {
        this.notificationRepository = new NotificationRepository();
    }
    
    /**
     * Get a notification by ID
     * @param notificationId the notification ID
     * @return Notification object if found, null otherwise
     */
    public Notification getNotificationById(int notificationId) {
        return notificationRepository.findById(notificationId);
    }
    
    /**
     * Create a new notification
     * @param notification the notification to create
     * @return true if creation successful, false otherwise
     */
    public boolean createNotification(Notification notification) {
        // Set default read status if not specified
        notification.setRead(false);
        
        return notificationRepository.save(notification);
    }
    
    /**
     * Create a notification for application status change
     * @param userId the user ID to notify
     * @param petName the name of the pet
     * @param status the new application status
     * @param message additional message details
     * @return true if notification created successfully, false otherwise
     */
    public boolean createApplicationStatusNotification(int userId, String petName, String status, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        
        if ("approved".equals(status)) {
            notification.setTitle("Adoption Approved");
            notification.setType("success");
            notification.setMessage("Your application to adopt " + petName + " has been approved! " + message);
        } else if ("rejected".equals(status)) {
            notification.setTitle("Adoption Not Approved");
            notification.setType("rejected");
            notification.setMessage("Your application to adopt " + petName + " was not approved. " + message);
        } else {
            notification.setTitle("Application Update");
            notification.setType("pending");
            notification.setMessage("Your application to adopt " + petName + " has been updated. " + message);
        }
        
        notification.setRead(false);
        
        return notificationRepository.save(notification);
    }
    
    /**
     * Mark a notification as read
     * @param notificationId the ID of the notification to mark as read
     * @return true if update successful, false otherwise
     */
    public boolean markAsRead(int notificationId) {
        return notificationRepository.markAsRead(notificationId);
    }
    
    /**
     * Mark all notifications for a user as read
     * @param userId the ID of the user
     * @return true if update successful, false otherwise
     */
    public boolean markAllAsRead(int userId) {
        return notificationRepository.markAllAsRead(userId);
    }
    
    /**
     * Get all notifications for a user
     * @param userId the user ID
     * @return List of notifications for the user
     */
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationRepository.findByUserId(userId);
    }
    
    /**
     * Get unread notifications for a user
     * @param userId the user ID
     * @return List of unread notifications for the user
     */
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }
    
    /**
     * Delete a notification
     * @param notificationId the ID of the notification to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteNotification(int notificationId) {
        return notificationRepository.delete(notificationId);
    }
}