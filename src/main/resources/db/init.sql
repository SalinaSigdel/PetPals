
CREATE DATABASE PetPals;
USE PetPals;

-- Create Users table
CREATE TABLE Users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,  -- Store the password directly as it will be hashed by the application
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