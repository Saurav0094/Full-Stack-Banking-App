# Full Stack Banking Application

A full stack online banking application built with Java Spring Boot, React Redux, JWT Authentication and MySQL. Features secure user authentication, account management, and real-time transaction history.

## 🎯 Project Overview

This project demonstrates a complete full stack banking system with secure JWT-based authentication, account management, and transaction processing. Built with Spring Boot REST API backend and React Redux frontend.

## 📸 Screenshots

![Sign In](screenshots/loginPage.png)

![Sign Up](screenshots/ss_2_singup.png)

![Dashboard 1](screenshots/ProjecPage1.png)

![Dashboard 2](screenshots/ProjectPage2.png)

![Dashboard 3](screenshots/ProjectPage3.png)

![Dashboard 4](screenshots/ProjectPage5.png)

## 🚀 Features

- Secure JWT authentication and authorization
- User registration and login
- Account creation and management
- Fund transfers between accounts
- Transaction history tracking
- Real-time balance updates
- Responsive React dashboard

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Frontend | React, Redux |
| Database | MySQL 8.0 |
| Authentication | JWT |
| Security | BCrypt Password Hashing |
| API | REST APIs |
| Tools | Git, GitHub, Maven |

## ⚙️ How to Run

**1. Clone the repository**
```bash
git clone https://github.com/Saurav0094/Full-Stack-Banking-App.git
cd Full-Stack-Banking-App
```

**2. Configure Database**
```sql
CREATE DATABASE IF NOT EXISTS demobankdb;
```

**3. Run Backend**
```bash
cd "Online Banking App Spring Boot"
mvnw.cmd spring-boot:run
```
**5. Open in browser**
http://localhost:3000

## 🧠 How It Works

1. User registers and logs in via React frontend
2. Spring Boot backend validates credentials and issues JWT token
3. JWT token is stored and sent with every API request
4. Backend processes transactions and updates MySQL database
5. React Redux manages frontend state in real-time

## 📌 Future Improvements

- Configure Gmail SMTP for email verification
- Move secrets to environment variables
- Complete placeholder pages (Orders, Customers, Reports)
- Add pagination for transaction history
- Write unit tests (JUnit + Jest)
- Dockerize the application
- Deploy on AWS

## 👨‍💻 Author

**Saurav Yadav**
B.Tech CSE | Kanpur Institute of Technology
[GitHub](https://github.com/Saurav0094) | [LinkedIn](https://www.linkedin.com/in/saurav-yadav-124293228/)
