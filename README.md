# 🚗 Electric Vehicle Rental System

A full-stack web application for managing electric vehicle rentals (cars, bikes, and scooters).
The system is built as part of an academic project and simulates a real-world rental platform with multiple user roles and client applications.

---

## 🧰 Tech Stack

* **Backend:** Spring Boot (Java)
* **Frontend (Employee App):** Angular
* **Frontend (Client & Promotions Apps):** JSP
* **Database:** Relational Database (MySQL / PostgreSQL)
* **Architecture:** RESTful APIs, multi-client system

---

## 📌 Features

### 🔧 Backend (Spring Boot)

* RESTful API for all core functionalities
* Vehicle management (cars, bikes, scooters)
* Rental tracking system
* User management (clients & employees)
* Breakdown/failure tracking
* PDF invoice generation
* RSS feed for promotions and announcements

---

### 👨‍💼 Employee Application (Angular)

Role-based dashboard with different access levels:

* **Admin**

  * Manage vehicles (CRUD)
  * Upload vehicles via CSV
  * Manage manufacturers
  * Manage users

* **Operator**

  * View rentals
  * View vehicles on map
  * Manage client accounts
  * Report vehicle failures

* **Manager**

  * Access all admin/operator features
  * View statistics (revenue, failures, etc.)
  * Define pricing
  * Manage promotions

---

### 📱 Client Application (JSP)

* User registration and login
* Vehicle rental (cars, bikes, scooters)
* Location-based rental process
* Real-time ride tracking (time & cost)
* Payment simulation
* Profile management
* Rental history

---

### 📢 Promotions Application (JSP)

* Create and manage promotions
* Create announcements/news posts
* Search promotions and content
* Accessible by managers only

---

## 🗂️ Project Structure

```
/backend        -> Spring Boot application
/frontend       -> Angular employee dashboard
/client-app     -> JSP client application
/promotions     -> JSP promotions application
```

---

## ⚙️ Getting Started

### Backend

1. Navigate to `/backend`
2. Configure database in `application.properties`
3. Run the application

### Angular Frontend

1. Navigate to `/frontend`
2. Install dependencies:

   ```bash
   npm install
   ```
3. Run:

   ```bash
   ng serve
   ```

### JSP Applications

* Deploy on a servlet container (e.g., Apache Tomcat)

---

## 📊 Key Concepts

* Role-based access control (RBAC)
* Multi-application architecture
* REST API design
* Data validation (client & server side)
* File handling (CSV import, images)
* Report generation (PDF)

---

## 🎓 Project Info

This project was developed as part of the **Internet Programming** course
at the Faculty of Electrical Engineering, University of Banja Luka.

---

## 📌 Notes

* This is an academic project and not intended for production use.
* Some features (e.g., payments) are simulated.

---

## 📬 Contact

For questions or suggestions, feel free to open an issue or reach out.
