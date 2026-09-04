# 🍕 Online Food Delivery System — TOMATO

> A console-based Java application simulating a full-stack food delivery platform, built on solid OOP principles, the SOLID design philosophy, and MySQL persistence via raw JDBC.

---

## ✨ Features

| Feature | Description |
|---|---|
| 👥 **Multi-Role Users** | `Admin`, `Customer`, `RestaurantOwner`, `DeliveryPerson` each with dedicated flows |
| 🔐 **Authentication** | Register and login with role-based access control |
| 🛒 **Cart & Ordering** | Browse menus, add `FoodItem`s to a `Cart`, and place an `Order` |
| 💳 **Payment Methods** | Pluggable payment system: `PaymentCard`, `PaymentCash`, `PaymentUPI` |
| 🚴 **Delivery Tracking** | Assign orders to a `DeliveryPerson` and track delivery status |
| 🍽️ **Restaurant Management** | Restaurant owners can add, edit, and remove menu items |
| 🗄️ **JDBC Repository Pattern** | All CRUD operations go through typed repository interfaces backed by JDBC implementations |

---

## 🗂️ Project Structure

```
Online Food Delivery System/
│
├── src/
│   ├── Main.java                  # Application entry point & interactive console UI
│   ├── InsertMenu.java            # Utility to seed menu data
│   │
│   ├── config/
│   │   └── DatabaseConfig.java    # MySQL connection constants (URL, USER, PASSWORD)
│   │
│   ├── model/                     # Core domain entities
│   │   ├── User.java
│   │   ├── Admin.java
│   │   ├── Customer.java
│   │   ├── RestaurantOwner.java
│   │   ├── DeliveryPerson.java
│   │   ├── Restaurant.java
│   │   ├── FoodItem.java
│   │   ├── Cart.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Delivery.java
│   │   ├── Payment.java
│   │   ├── PaymentCard.java
│   │   ├── PaymentCash.java
│   │   └── PaymentUPI.java
│   │
│   ├── interfaces/                # Abstraction contracts
│   │
│   ├── repository/                # Data access layer
│   │   ├── UserRepository.java
│   │   ├── JdbcUserRepository.java
│   │   ├── OrderRepository.java
│   │   ├── JdbcOrderRepository.java
│   │   ├── DeliveryRepository.java
│   │   └── JdbcDeliveryRepository.java
│   │
│   └── service/                   # Business logic layer
│       ├── AuthenticationService.java
│       ├── OrderService.java
│       ├── PaymentService.java
│       ├── DeliveryService.java
│       └── RestaurantService.java
│
├── SQL/                           # Database schema and seed scripts
├── mysql-connector-j-*.jar        # JDBC driver (not committed if .gitignore applied)
└── Online_Food_Delivery_System_Project_Documentation.docx
```

---

## 🧱 Architecture

The project follows a clean **layered architecture**:

```
Console UI (Main.java)
       ↓
  Service Layer  ←→  Interfaces / Abstractions
       ↓
 Repository Layer (JDBC Implementations)
       ↓
  MySQL Database
```

- **Models** are plain Java POJOs with no DB or UI logic.
- **Repositories** implement interfaces, making the data layer swappable.
- **Services** orchestrate business rules and call repositories.
- **`Main.java`** drives the console UI and wires everything together.

---

## 🛠️ Prerequisites

- **Java Development Kit (JDK)** — Version 8 or higher
- **MySQL Server** — Installed and running locally
- **MySQL Connector/J** — JDBC driver JAR (included as `mysql-connector-j-*.jar`)
- **IDE** — IntelliJ IDEA recommended (`.iml` project file included)

---

## 🚀 Setup & Running

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd "Online Food Delivery System"
```

### 2. Set Up the Database

Run the SQL schema scripts located in the `SQL/` directory against your MySQL instance:

```sql
-- Example (run via MySQL client or workbench)
SOURCE SQL/schema.sql;
```

This creates the `food_delivery_db` database with tables:
`users`, `customers`, `restaurants`, `food_items`, `orders`, `order_items`, `deliveries`

### 3. Configure Database Credentials

Open [`src/config/DatabaseConfig.java`](src/config/DatabaseConfig.java) and update:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/food_delivery_db";
private static final String USER     = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

> ⚠️ **Never commit real credentials.** Consider adding `DatabaseConfig.java` to `.gitignore` and distributing a `DatabaseConfig.java.example` template instead.

### 4. Add the JDBC Driver

Ensure `mysql-connector-j-*.jar` is on your **classpath**:
- **IntelliJ IDEA**: `File → Project Structure → Modules → Dependencies → + JAR`

### 5. (Optional) Seed Menu Data

Run `InsertMenu.java` once to populate sample restaurants and food items in the database.

### 6. Run the Application

Compile and execute `src/Main.java`. The console will guide you through:

- Registering / logging in as any role
- Browsing restaurant menus
- Adding items to your cart and placing an order
- Processing payment (Card / Cash / UPI)
- Tracking delivery status

---

## 🗃️ Database Schema (Overview)

| Table | Key Columns |
|---|---|
| `users` | `id`, `name`, `email`, `password`, `role` |
| `customers` | `id`, `user_id`, `address` |
| `restaurants` | `id`, `owner_id`, `name`, `address` |
| `food_items` | `id`, `restaurant_id`, `name`, `price`, `available` |
| `orders` | `id`, `customer_id`, `restaurant_id`, `status`, `total` |
| `order_items` | `id`, `order_id`, `food_item_id`, `quantity`, `price` |
| `deliveries` | `id`, `order_id`, `delivery_person_id`, `status` |

---

## 📐 Design Principles

- **OOP**: Inheritance (`User → Customer`, `Payment → PaymentCard`), Encapsulation, Polymorphism
- **SOLID**: Single Responsibility (one class, one job), Open/Closed (extend services, not modify), Dependency Inversion (depend on interfaces)
- **Repository Pattern**: Decouples business logic from data access; implementations can be swapped (e.g., in-memory for tests)

---

## 📄 Documentation

Extended architecture notes and UML diagrams are in:
[`Online_Food_Delivery_System_Project_Documentation.docx`](Online_Food_Delivery_System_Project_Documentation.docx)

---

## 👤 Author

**Shujatullah** — built as a Java OOP & JDBC learning project.

---

*TOMATO — Because every great food app needs a great name 🍅*
