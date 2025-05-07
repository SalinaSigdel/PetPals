package com.snapgramfx.petpals.listener;

import com.snapgramfx.petpals.service.PasswordResetService;
import com.snapgramfx.petpals.service.RememberMeTokenService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener to schedule cleanup of expired remember me tokens
 */
@WebListener
public class TokenCleanupListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(TokenCleanupListener.class.getName());
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Initializing token cleanup scheduler");

        // Create scheduler with a single thread
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule token cleanup task to run every 24 hours
        scheduler.scheduleAtFixedRate(() -> {
            try {
                LOGGER.info("Running token cleanup task");

                // Clean up remember me tokens
                RememberMeTokenService rememberMeService = new RememberMeTokenService();
                int rememberMeCount = rememberMeService.cleanupExpiredTokens();
                LOGGER.info("Deleted " + rememberMeCount + " expired remember me tokens");

                // Clean up password reset tokens
                PasswordResetService passwordResetService = new PasswordResetService();
                int passwordResetCount = passwordResetService.cleanupExpiredTokens();
                LOGGER.info("Deleted " + passwordResetCount + " expired password reset tokens");

                LOGGER.info("Token cleanup completed successfully");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error in token cleanup task", e);
            }
        }, 0, 24, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Shutting down token cleanup scheduler");

        // Shutdown the scheduler
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
