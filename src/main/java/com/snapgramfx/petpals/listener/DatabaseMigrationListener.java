package com.snapgramfx.petpals.listener;

import com.snapgramfx.petpals.util.DatabaseMigrationUtil;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener to run database migrations when the application starts
 */
@WebListener
public class DatabaseMigrationListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(DatabaseMigrationListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Running database migrations...");

        // Add badge column to Pets table
        boolean success1 = DatabaseMigrationUtil.executeSqlScript("sql/add_badge_column.sql");

        // Create RememberMeTokens table if it doesn't exist
        boolean success2 = DatabaseMigrationUtil.executeSqlScript("sql/create_remember_me_tokens_table.sql");

        // Create PasswordResetTokens table if it doesn't exist
        boolean success3 = DatabaseMigrationUtil.executeSqlScript("sql/create_password_reset_tokens_table.sql");

        if (success1 && success2 && success3) {
            LOGGER.info("Database migrations completed successfully");
        } else {
            LOGGER.log(Level.SEVERE, "Database migrations failed");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed
    }
}
