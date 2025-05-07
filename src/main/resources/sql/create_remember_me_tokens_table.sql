-- Create RememberMeTokens table if it doesn't exist
CREATE TABLE IF NOT EXISTS RememberMeTokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    INDEX (user_id),
    INDEX (token)
);

-- Add index on expiry_date for faster cleanup
CREATE INDEX IF NOT EXISTS idx_expiry_date ON RememberMeTokens(expiry_date);

-- If the database doesn't support IF NOT EXISTS for indexes, use this approach instead:
-- SET @indexExists = 0;
-- SELECT COUNT(*) INTO @indexExists FROM INFORMATION_SCHEMA.STATISTICS
-- WHERE TABLE_SCHEMA = 'petpals_db' AND TABLE_NAME = 'RememberMeTokens' AND INDEX_NAME = 'idx_expiry_date';

-- IF @indexExists = 0 THEN
--     ALTER TABLE RememberMeTokens ADD INDEX idx_expiry_date (expiry_date);
-- END IF;
