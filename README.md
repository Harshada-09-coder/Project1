# Project1
# 🛍️ ShopSphere – E-Commerce & Inventory Management System

## 📌 Project Overview

**ShopSphere** is a Java-based E-Commerce and Inventory Management System developed using Java Swing and MySQL. It provides a desktop application where customers can browse products, add items to their shopping cart, place orders, and track their purchases.

The system also provides administrative functionalities to manage products, categories, inventory, customer information, and customer orders.

The main objective of this project is to simplify online shopping operations and improve inventory management through a centralized desktop application.

## 🎯 Project Objectives

- Develop a user-friendly e-commerce desktop application.
- Provide efficient product and inventory management.
- Allow customers to browse products and place orders.
- Maintain customer and order information.
- Integrate Java applications with MySQL using JDBC.
- Ensure data consistency and database integrity.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Graphical User Interface |
| MySQL | Database management |
| JDBC | Database connectivity |
| Maven | Project build and dependency management |
| JUnit | Unit testing |
| Git & GitHub | Version control |

## ✨ Key Features

### 👤 Customer Module
- Browse available products.
- View product details and prices.
- Add products to the shopping cart.
- Update product quantities.
- Remove products from the cart.
- Place customer orders.
- View order history.

### 🔐 Admin Module
- Manage product information.
- Add, update, and delete products.
- Manage product categories.
- Monitor available stock.
- View customer information.
- Manage customer orders.
- Update order status.

### 🗄️ Database Module
- MySQL database integration.
- JDBC-based database connectivity.
- Product and category management.
- Customer and order data storage.
- Data validation and integrity.
- Database connection testing.

## 🏗️ System Architecture

The application follows a layered architecture:

1. **Presentation Layer:** Java Swing forms and screens.
2. **Business Logic Layer:** Handles application operations and validations.
3. **Data Access Layer:** JDBC manages database communication.
4. **Database Layer:** MySQL stores application information.

## 📂 Project Structure

```text
ShopSphere/
│
├── .mvn/
├── bin/
├── database/
│
├── lib/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── Application source files
│   │
│   └── test/
│       └── java/
│           └── Database and service tests
│
├── target/
│
├── db.properties
├── mvn.cmd
├── pom.xml
├── shopsphere.sql
└── README.md
```

## ⚙️ Installation and Setup

### Prerequisites

Install the following software:

- JDK 17 or compatible Java version.
- MySQL Server.
- MySQL Workbench (optional).
- Apache Maven 3.9 or compatible version.
- Visual Studio Code or IntelliJ IDEA.

### Step 1: Clone the Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

### Step 2: Open the Project

Open the ShopSphere folder in Visual Studio Code or IntelliJ IDEA.

### Step 3: Configure MySQL

1. Start your MySQL Server.
2. Open MySQL Workbench.
3. Create the database or import the provided `shopsphere.sql` file.
4. Verify that the database is created successfully.

### Step 4: Configure Database Connection

Open `db.properties` and configure your database credentials.

```properties
db.url=jdbc:mysql://localhost:3306/shopsphere
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

Update the database name, username, and password according to your MySQL configuration.

**Note:** Do not upload actual database passwords to GitHub.

### Step 5: Build the Project

Open the terminal in the directory containing `pom.xml`.

```bash
mvn clean compile
```

### Step 6: Run Tests

```bash
mvn test
```

### Step 7: Run the Application

Run the configured main Java class through your IDE or Maven Exec Plugin.

```bash
java -cp "ShopSphere\target\classes;ShopSphere\lib\*" com.shopsphere.view.WelcomePage
```

Replace `YOUR_MAIN_CLASS` with the fully qualified name of the application's actual main class.

## 🗃️ Database

The application uses MySQL as its relational database.

The database manages information related to:

- Customers
- Administrators
- Products
- Categories
- Shopping Cart
- Orders
- Inventory

The `shopsphere.sql` file contains the database initialization script.

## 🧪 Testing

The project includes testing for database connectivity and application functionality.

Testing areas include:

- Database connection verification.
- Database configuration validation.
- Product management operations.
- Customer and order operations.
- Input validation.
- Error handling.
- Data integrity.

JUnit is used for unit testing where configured.

## 🔒 Security and Validation

- Validate user inputs before database operations.
- Maintain database constraints.
- Use secure database credential configuration.
- Handle database exceptions.
- Prevent invalid product prices and stock quantities.

## 🚀 Future Enhancements

- Online payment gateway integration.
- Product ratings and customer reviews.
- Email notifications for orders.
- Advanced inventory analytics.
- Product recommendations.
- Improved reporting functionality.
- Web-based application integration.

## 👩‍💻 Project Information

**Project Name:** ShopSphere – E-Commerce & Inventory Management System

**Domain:** Retail Technology / E-Commerce

**Project Type:** Java Desktop Application

**Backend:** Java, JDBC

**Frontend:** Java Swing

**Database:** MySQL

**Build Tool:** Maven

## 👥 Contributors

Add your team members and their respective contributions here.

## 📄 License

This project is developed for educational and academic purposes.

---

**ShopSphere – Simplifying Shopping and Inventory Management.** 🛒
:::
