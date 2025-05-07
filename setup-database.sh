#!/bin/bash

# Database credentials
DB_USER="root"
DB_PASSWORD="Belbari890"
DB_NAME="petpals_db"
SQL_FILE="src/main/webapp/database.sql"

# Create database if it doesn't exist
echo "Creating database $DB_NAME if it doesn't exist..."
mysql -u $DB_USER -p$DB_PASSWORD -e "CREATE DATABASE IF NOT EXISTS $DB_NAME;"

# Execute the SQL script
echo "Executing SQL script to create tables..."
mysql -u $DB_USER -p$DB_PASSWORD $DB_NAME < $SQL_FILE

echo "Database setup complete!"
