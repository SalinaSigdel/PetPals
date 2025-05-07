-- PetPals Database Schema
-- Created: April 21, 2025
-- Description: Database schema for PetPals pet adoption platform

-- Drop existing tables if they exist
DROP TABLE IF EXISTS Notifications;
DROP TABLE IF EXISTS Adoption_Applications;
DROP TABLE IF EXISTS Adoptions;
DROP TABLE IF EXISTS Pets;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Shelters;

-- Create Users table
CREATE TABLE Users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,  -- Hashed password
                       salt VARCHAR(64) NOT NULL,       -- Salt for password hashing
                       email VARCHAR(100) UNIQUE NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       role ENUM('adopter', 'admin') NOT NULL DEFAULT 'adopter',
                       email_notifications_enabled BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Pets table
CREATE TABLE Pets (
                      pet_id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(50) NOT NULL,
                      type ENUM('Dog', 'Cat', 'Bird', 'Rabbit', 'Other') NOT NULL,
                      breed VARCHAR(50),
                      age VARCHAR(20),
                      gender ENUM('Male', 'Female') NOT NULL,
                      weight VARCHAR(20),
                      description TEXT,
                      image_url VARCHAR(255),
                      status ENUM('available', 'pending', 'adopted') NOT NULL DEFAULT 'available',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Adoption Applications table
CREATE TABLE Adoption_Applications (
                                       application_id INT PRIMARY KEY AUTO_INCREMENT,
                                       user_id INT NOT NULL,
                                       pet_id INT NOT NULL,
                                       status ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
                                       applicant_name VARCHAR(100) NOT NULL,
                                       email VARCHAR(100) NOT NULL,
                                       phone VARCHAR(20) NOT NULL,
                                       city_state VARCHAR(100) NOT NULL,
                                       reason_for_adoption TEXT,
                                       rejection_reason TEXT,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
                                       FOREIGN KEY (pet_id) REFERENCES Pets(pet_id) ON DELETE CASCADE
);

-- Create Notifications table
CREATE TABLE Notifications (
                               notification_id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT NOT NULL,
                               title VARCHAR(100) NOT NULL,
                               message TEXT NOT NULL,
                               type ENUM('success', 'pending', 'rejected') NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Create Shelters table
CREATE TABLE Shelters (
                          shelter_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          city VARCHAR(50) NOT NULL,
                          state VARCHAR(50) NOT NULL,
                          zip_code VARCHAR(20) NOT NULL,
                          phone VARCHAR(20) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          website VARCHAR(255),
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Adoptions table (for completed adoptions)
CREATE TABLE Adoptions (
                           adoption_id INT PRIMARY KEY AUTO_INCREMENT,
                           application_id INT NOT NULL,
                           user_id INT NOT NULL,
                           pet_id INT NOT NULL,
                           adoption_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           status ENUM('completed', 'returned') NOT NULL DEFAULT 'completed',
                           notes TEXT,
                           FOREIGN KEY (application_id) REFERENCES Adoption_Applications(application_id) ON DELETE CASCADE,
                           FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
                           FOREIGN KEY (pet_id) REFERENCES Pets(pet_id) ON DELETE CASCADE
);

-- Create necessary indexes for better performance
CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_users_username ON Users(username);
CREATE INDEX idx_pets_status ON Pets(status);
CREATE INDEX idx_pets_type ON Pets(type);
CREATE INDEX idx_applications_user_id ON Adoption_Applications(user_id);
CREATE INDEX idx_applications_pet_id ON Adoption_Applications(pet_id);
CREATE INDEX idx_applications_status ON Adoption_Applications(status);
CREATE INDEX idx_notifications_user_id ON Notifications(user_id);
CREATE INDEX idx_notifications_is_read ON Notifications(is_read);
CREATE INDEX idx_adoptions_user_id ON Adoptions(user_id);
CREATE INDEX idx_adoptions_pet_id ON Adoptions(pet_id);
CREATE INDEX idx_adoptions_application_id ON Adoptions(application_id);
CREATE INDEX idx_shelters_city ON Shelters(city);
CREATE INDEX idx_shelters_state ON Shelters(state);

-- Insert sample admin user with hashed password (admin123)
-- In a real application, this would be properly hashed with salt
INSERT INTO Users (username, password, salt, email, full_name, role)
VALUES ('admin', 'admin123', 'defaultsalt', 'admin@petpals.com', 'System Administrator', 'admin');

-- Insert sample pets
INSERT INTO Pets (name, type, breed, age, gender, weight, description, status)
VALUES
    ('Max', 'Dog', 'Golden Retriever', '2 years', 'Male', '30 kg', 'Friendly and energetic Golden Retriever who loves playing fetch.', 'available'),
    ('Luna', 'Cat', 'British Shorthair', '1 year', 'Female', '4 kg', 'Calm and affectionate British Shorthair who enjoys lounging around.', 'available'),
    ('Rio', 'Bird', 'African Grey Parrot', '3 years', 'Male', '400 g', 'Highly intelligent African Grey Parrot who loves to mimic sounds.', 'available');

-- Insert sample shelters
INSERT INTO Shelters (name, address, city, state, zip_code, phone, email, website, description)
VALUES
    ('Happy Tails Rescue', '123 Main St', 'Springfield', 'IL', '62701', '555-123-4567', 'info@happytails.org', 'www.happytails.org', 'No-kill shelter dedicated to finding homes for dogs and cats.'),
    ('Feathered Friends Sanctuary', '456 Oak Ave', 'Riverside', 'CA', '92501', '555-987-6543', 'contact@featheredfriends.org', 'www.featheredfriends.org', 'Specialized in rescuing and rehabilitating birds of all species.'),
    ('Small Paws Rescue', '789 Pine Rd', 'Portland''OR', '97201', '555-456-7890', 'help@smallpaws.org', 'www.smallpaws.org', 'Focused on small animals including rabbits, guinea pigs, and hamsters.');