# Online Food Delivery System

A robust, console-based Java application that simulates a complete food delivery platform. This project is built utilizing core Object-Oriented Programming (OOP) concepts, strictly adheres to SOLID design principles, and persists data using a MySQL database via raw JDBC.

## Features

* **Multi-Role User System:** Supports different user types including `Admin`, `Customer`, `RestaurantOwner`, and `DeliveryPerson`.


* **Authentication:** Secure user registration and login flows.
* **Order Management:** Browse restaurant menus, add `FoodItem` objects to a `Cart`, and place an `Order`.


* **Payment Processing:** Interface-driven payment system supporting `PaymentCard`, `PaymentCash`, and `PaymentUPI`.


* **Delivery Tracking:** Assigns orders to a `DeliveryPerson` and tracks delivery status.


* **Database Integration:** Utilizes the Repository design pattern (e.g., `JdbcUserRepository`, `JdbcOrderRepository`) to perform CRUD operations via JDBC.



## Project Structure

The codebase is organized into highly cohesive packages to separate database logic from business rules:

* **`src/config/`**: Contains `DatabaseConfig.java` for managing the MySQL database connection.


* **`src/model/`**: Holds core entities (`User`, `Customer`, `Restaurant`, `FoodItem`, `Order`, `Cart`, `Payment`, etc.).


* **`src/repository/`**: Contains repository interfaces and JDBC implementations for database interactions.


* **`src/service/`**: Houses the business logic (`AuthenticationService`, `OrderService`, `PaymentService`, `DeliveryService`, `RestaurantService`).


* **`src/Main.java`**: The main entry point featuring an interactive console UI.



## Prerequisites

* **Java Development Kit (JDK):** Version 8 or higher.
* **MySQL Server:** Installed and running locally.
* **MySQL Connector/J:** The JDBC driver JAR file must be added to your project's classpath.
* **IDE:** IntelliJ IDEA (the project contains an `.idea` directory) or any standard Java IDE.



## Setup Instructions

**1. Prepare the Database**
Execute the provided SQL schema in your MySQL environment to create the `food_delivery_db` database and its corresponding tables (`users`, `customers`, `restaurants`, `food_items`, `orders`, `order_items`). Add sample restaurants and food items directly to the database so they appear in the menu.

**2. Configure Database Credentials**
Navigate to `src/config/DatabaseConfig.java` and update the `URL`, `USER`, and `PASSWORD` constants to match your local MySQL configuration.

**3. Add the JDBC Driver**
Ensure the `mysql-connector-j-x.x.x.jar` file is downloaded and added to your project's build path/dependencies.

**4. Run the Application**
Compile and execute `src/Main.java`. Use the console prompts to register a new customer, log in, browse the menu, and place an order.

## Documentation

For extended details on project architecture and UML diagrams, refer to the included `Online_Food_Delivery_System_Project_Documentation.docx` file.
