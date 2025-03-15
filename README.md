# Policlinic Management System

This project is a comprehensive management system for a chain of polyclinics, developed using Java for backend logic and JavaFX with SceneBuilder for the graphical user interface. The system provides functionalities for handling users, appointments, medical services, financial transactions, and blood donations.

## Table of Contents
- **Features**
- **Technologies Used**
- **Database Design**
- **Usage**
- **Future Improvements**

---

## Features

- **User Roles**: Admin, Super Admin, HR Inspector, Financial Expert, Receptionist, Nurse, Doctor, and Patient.
- **Authentication**: Email and password-based login.
- **Role-Specific Functionalities**:
  - **Doctor**:
    - View appointments.
    - Create medical reports.
  - **Nurse**:
    - Operate independently.
    - Manage patient data.
  - **Receptionist**:
    - Handle appointment scheduling.
  - **Financial Expert**:
    - Manage clinic finances.
  - **Admin and Super Admin**:
    - Add, modify, and delete users.
- **Blood Donation Tracking**: Updates blood stock levels per clinic.
- **Financial Management**: Automatic revenue, expense, and profit calculation.
- **Comprehensive Database Management**: Supports CRUD operations for users, appointments, and medical services.

---

## Technologies Used

- **Programming Language**: Java
- **GUI**: JavaFX with SceneBuilder
- **Database**: MySQL

---

## Database Design

- The system uses a relational database with 14 interconnected tables, handling users, employees, medical services, appointments, financial transactions, and blood donations.
- It includes triggers to handle automatic updates for revenue, expenses, profits, and blood stock levels.
- Views provide summarized data access.

---

## Usage

- Run the application to access the login page.
- Based on user roles, different functionalities will be accessible, such as managing appointments, tracking blood donations, or handling clinic finances.

---

## Future Improvements

- Allow medical assistants to create medical reports.
- Enable doctors to work at multiple clinics.

---
