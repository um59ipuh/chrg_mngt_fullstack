-- V3__Create_Users_Table.sql

-- Create users table
CREATE TABLE users (
   user_id    INT AUTO_INCREMENT PRIMARY KEY,
   first_name VARCHAR(255) NOT NULL,
   last_name  VARCHAR(255) NOT NULL,
   email      VARCHAR(255) NOT NULL UNIQUE,
   password   VARCHAR(255) NOT NULL
);
