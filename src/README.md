# E-Commerce Dynamic Pricing & Inventory System
	
A Java console application built with JDBC and MySQL that simulates an e-commerce backend. The system manages products, customers, inventory, and orders while applying dynamic pricing rules based on stock levels and customer membership. It also demonstrates JDBC transaction management using commit and rollback.

## Features
- Product Management
- Customer Management
- Inventory Management
- Dynamic Pricing Engine
- Order Processing
- JDBC Transaction Management
- Commit & Rollback
- Menu-driven Console Interface
- MySQL Database Integration


## Tech Stack
- Java
- JDBC
- MySQL
- SQL
- Object-Oriented Programming (OOP)

ECommerceInventorySystem
│
├── src
│
├── app
│   └── Main.java
│
├── model
│   ├── Product.java
│   ├── Customer.java
│   └── Order.java
│
├── util
│   └── DBConnection.java
│
├── dao
│   ├── ProductDAO.java
│   ├── CustomerDAO.java
│   └── OrderDAO.java
│
├── service
│   ├── PricingEngine.java
│   ├── InventoryService.java
│   └── OrderService.java
│
├── menu
│   └── ConsoleMenu.java
│
└── exception
    
    └── InsufficientStockException.java


Database Schema
Products
---------
product_id
name
category
base_price
stock
Customers
---------
customer_id
name
membership
Orders
---------
order_id
customer_id
product_id
quantity
unit_price
total_price
order_date


System Workflow

Customer
↓

Select Product
↓

Check Inventory
↓

Calculate Dynamic Price
↓

Create Order
↓

Update Stock
↓

Commit Transaction
If an error occurs:

Rollback Transaction

Dynamic Pricing Rules
Pricing Rules
- Low stock → Increase price by 10%
- High stock → Decrease price by 15%
- Gold members → Additional 5% discount
- Bulk orders → Additional 10% discount

 Transaction Management
The project uses JDBC transactions to ensure data consistency.
Steps:
- Disable auto-commit
- Insert order
- Update inventory
- Commit on success
- Rollback on failure

OOP Concepts
- Encapsulation
- Abstraction
- Composition
- DAO Pattern
- Service Layer
- Strategy Pattern (Pricing Engine)

How to Run
1. Clone the repository.
2. Create the MySQL database.
3. Run schema.sql.
4. Run sample_data.sql.
5. Update database credentials.
6. Compile the project.
7. Run Main.java.

Sample Console Output
===== MENU =====
1. View Products
2. Buy Product
3. Add Product
4. View Orders
5. Exit
Enter Choice:

 Learning Outcomes
Concepts Learned
- Java OOP
- JDBC
- MySQL
- SQL Constraints
- DAO Pattern
- Transactions
- Exception Handling
- Business Logic Design
- Inventory Management
- Dynamic Pricing