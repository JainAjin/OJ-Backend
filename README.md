Full-Stack Online Judge Platform

This is a comprehensive, full-stack Online Judge platform designed for competitive programming enthusiasts to practice and improve their coding skills.The application features a secure, scalable backend built
with Spring Boot and a modern, responsive frontend developed with React. The core of the platform is its robust judging engine, which securely compiles and executes user-submitted code in isolated Docker 
containers.


Key Features

* Secure User Authentication: JWT-based authentication with user registration and role-based access control (USER, ADMIN).

* Problem Management: Admins can create, update, and manage a database of programming problems and their associated test cases.

* Interactive Code Submission: A rich, in-browser editor for writing solutions in multiple languages (Java & C++ supported).

* Secure Code Execution: The backend uses Docker to create isolated, sandboxed containers for each submission, preventing malicious code and managing resource limits (time & memory).

* Real-time Submission Status: Users can view their submission history with detailed results like "Accepted," "Wrong Answer," or "Time Limit Exceeded."

* RESTful API: A well-defined API separates the backend logic from the frontend presentation.


Tech Stack

Backend   -   Java 17, Spring Boot, Spring Security, JPA/Hibernate, JWT, PostgreSQL, Maven

Judging  -  Docker

Database  -  Supabase (Cloud-hosted PostgreSQL)


