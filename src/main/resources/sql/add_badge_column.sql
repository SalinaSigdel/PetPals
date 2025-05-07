-- Add badge column to Pets table if it doesn't exist
ALTER TABLE Pets ADD COLUMN IF NOT EXISTS badge VARCHAR(50) DEFAULT '';

-- If the database doesn't support IF NOT EXISTS, use this approach instead:
-- First check if the column exists
-- SET @columnExists = 0;
-- SELECT COUNT(*) INTO @columnExists FROM INFORMATION_SCHEMA.COLUMNS 
-- WHERE TABLE_SCHEMA = 'petpals_db' AND TABLE_NAME = 'Pets' AND COLUMN_NAME = 'badge';

-- IF @columnExists = 0 THEN
--     ALTER TABLE Pets ADD COLUMN badge VARCHAR(50) DEFAULT '';
-- END IF;
