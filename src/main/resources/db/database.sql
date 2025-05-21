-- Create database
CREATE DATABASE IF NOT EXISTS petpals_db;
USE petpals_db;

-- Drop all tables if they exist (in reverse FK order)
DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS remember_me_tokens;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS adoptions;
DROP TABLE IF EXISTS adoption_applications;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS shelters;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       salt VARCHAR(64) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       full_name VARCHAR(100) NOT NULL,
                       phone VARCHAR(20),
                       address VARCHAR(255),
                       role ENUM('adopter', 'admin') NOT NULL DEFAULT 'adopter',
                       email_notifications_enabled BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Pets table
CREATE TABLE pets (
                      pet_id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      type ENUM('Dog', 'Cat', 'Bird', 'Rabbit', 'Other') NOT NULL,
                      breed VARCHAR(50),
                      age VARCHAR(20),
                      gender ENUM('Male', 'Female') NOT NULL,
                      weight VARCHAR(20),
                      description TEXT,
                      image_url VARCHAR(255),
                      status ENUM('available', 'pending', 'adopted') NOT NULL DEFAULT 'available',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      badge VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Shelters table
CREATE TABLE shelters (
                          shelter_id INT AUTO_INCREMENT PRIMARY KEY,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Adoption applications table
CREATE TABLE adoption_applications (
                                       application_id INT AUTO_INCREMENT PRIMARY KEY,
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
                                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                       FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Adoptions table
CREATE TABLE adoptions (
                           adoption_id INT AUTO_INCREMENT PRIMARY KEY,
                           application_id INT NOT NULL,
                           user_id INT NOT NULL,
                           pet_id INT NOT NULL,
                           adoption_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           status ENUM('completed', 'returned') NOT NULL DEFAULT 'completed',
                           notes TEXT,
                           FOREIGN KEY (application_id) REFERENCES adoption_applications(application_id) ON DELETE CASCADE,
                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                           FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Notifications table
CREATE TABLE notifications (
                               notification_id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               title VARCHAR(100) NOT NULL,
                               message TEXT NOT NULL,
                               type ENUM('success', 'pending', 'rejected') NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Remember me tokens table
CREATE TABLE remember_me_tokens (
                                    token_id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL,
                                    username VARCHAR(50) NOT NULL,
                                    token VARCHAR(255) NOT NULL UNIQUE,
                                    expiry_date TIMESTAMP NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Password reset tokens table
CREATE TABLE password_reset_tokens (
                                       token_id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL,
                                       token VARCHAR(255) NOT NULL UNIQUE,
                                       expiry_date TIMESTAMP NOT NULL,
                                       used BOOLEAN NOT NULL DEFAULT FALSE,
                                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes (only needed if not already covered by keys)
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_pets_status ON pets(status);
CREATE INDEX idx_pets_type ON pets(type);
CREATE INDEX idx_applications_user_id ON adoption_applications(user_id);
CREATE INDEX idx_applications_pet_id ON adoption_applications(pet_id);
CREATE INDEX idx_applications_status ON adoption_applications(status);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_adoptions_user_id ON adoptions(user_id);
CREATE INDEX idx_adoptions_pet_id ON adoptions(pet_id);
CREATE INDEX idx_adoptions_application_id ON adoptions(application_id);
CREATE INDEX idx_shelters_city ON shelters(city);
CREATE INDEX idx_shelters_state ON shelters(state);
CREATE INDEX idx_expiry_date ON remember_me_tokens(expiry_date);
CREATE INDEX idx_reset_expiry_date ON password_reset_tokens(expiry_date);
