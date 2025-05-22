# PetPals

PetPals is a web application designed to facilitate pet adoptions by connecting shelters with potential pet adopters. The platform enables users to browse available pets, submit adoption applications, and helps shelters manage their adoption processes efficiently.

## Features

- **User Authentication**: Secure login, registration and password recovery
- **Pet Browsing**: View available pets with detailed information
- **Adoption Applications**: Submit and track adoption requests
- **User Profiles**: Manage personal information and application history
- **Admin Dashboard**: Comprehensive tools for administrators to manage users, pets, applications, and adoptions
- **Shelter Management**: Tools for shelters to add and manage their pets

## Technologies Used

- **Backend**: Java Servlets, JSP
- **Frontend**: HTML, CSS, JavaScript
- **Database**: MySQL
- **Build Tool**: Maven
- **Authentication**: Custom authentication with Remember Me functionality

## Prerequisites

- JDK 17 or higher
- MySQL Server
- Maven
- Servlet container (like Apache Tomcat)

## Setup and Installation

1. **Clone the repository**
   `
   git clone https://github.com/yourusername/PetPals.git
   cd PetPals
   

2. **Database Setup**
    - run the sql or xammp
    - and add sql queries in mysql from database.sql file
    - and after that run the project 


3. **Build the project**
   
   mvn clean package


4. **Deploy the WAR file**
   - Deploy the generated WAR file from the `target` directory to your servlet container

## Project Structure

- `src/main/java/com/snapgramfx/petpals/model` - Data models
- `src/main/java/com/snapgramfx/petpals/repository` - Database access layer
- `src/main/java/com/snapgramfx/petpals/service` - Business logic layer
- `src/main/java/com/snapgramfx/petpals/controller` - Application controllers
- `src/main/java/com/snapgramfx/petpals/servlet` - Servlet definitions
- `src/main/webapp` - JSP pages, CSS, JavaScript, and web resources



## Acknowledgements

- Pet adoption concept inspired by real-world pet shelter needs
- All pet images and data are for demonstration purposes only 