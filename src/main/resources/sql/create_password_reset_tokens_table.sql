-- Create PasswordResetTokens table if it doesn't exist
CREATE TABLE IF NOT EXISTS PasswordResetTokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    INDEX (user_id),
    INDEX (token)
);

-- Add index on expiry_date for faster cleanup
CREATE INDEX IF NOT EXISTS idx_reset_expiry_date ON PasswordResetTokens(expiry_date);

-- If the database doesn't support IF NOT EXISTS for indexes, use this approach instead:
-- SET @indexExists = 0;
-- SELECT COUNT(*) INTO @indexExists FROM INFORMATION_SCHEMA.STATISTICS
-- WHERE TABLE_SCHEMA = 'petpals_db' AND TABLE_NAME = 'PasswordResetTokens' AND INDEX_NAME = 'idx_reset_expiry_date';

-- IF @indexExists = 0 THEN
--     ALTER TABLE PasswordResetTokens ADD INDEX idx_reset_expiry_date (expiry_date);
-- END IF;
