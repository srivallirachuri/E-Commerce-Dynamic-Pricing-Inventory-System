package app;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Customer;
import model.Order;
import model.Product;
import service.InventoryService;
import service.OrderService;
import service.PricingEngine;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        // 1. Dependency Wiring (The Composition Root)
        ProductDAO productDAO = new ProductDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        OrderDAO orderDAO = new OrderDAO();
        
        InventoryService inventoryService = new InventoryService(productDAO);
        PricingEngine pricingEngine = new PricingEngine();
        
        OrderService orderService = new OrderService(productDAO, customerDAO, inventoryService, pricingEngine);
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("===========================================");
        System.out.println("  WELCOME TO E-COMMERCE INVENTORY SYSTEM   ");
        System.out.println("===========================================");

        // 2. The Application Loop
        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. View All Products");
            System.out.println("2. Buy a Product");
            System.out.println("3. Add New Product");
            System.out.println("4. Update Stock");
            System.out.println("5. View All Orders");
            System.out.println("6. Search Product by ID");
            System.out.println("7. Add New Customer");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    // View Products
                    System.out.println("\n--- Product List ---");
                    List<Product> products = productDAO.getAllProducts();
                    for (Product p : products) {
                        System.out.println("ID: " + p.getProductId() + " | Name: " + p.getName() + 
                                           " | Price: $" + p.getBasePrice() + " | Stock: " + p.getStock());
                    }
                    break;

                case 2:
                    // Buy Product
                    System.out.println("\n--- Buy Product ---");
                    System.out.print("Enter Customer ID: ");
                    int custId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Product ID: ");
                    int prodId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Quantity: ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    
                    // Delegate to Service Layer
                    orderService.placeOrder(custId, prodId, qty);
                    break;

                case 3:
                    // Add Product
                    System.out.println("\n--- Add New Product ---");
                    Product newProduct = new Product();
                    System.out.print("Enter Name: ");
                    newProduct.setName(scanner.nextLine());
                    System.out.print("Enter Category: ");
                    newProduct.setCategory(scanner.nextLine());
                    System.out.print("Enter Base Price: ");
                    newProduct.setBasePrice(Double.parseDouble(scanner.nextLine()));
                    System.out.print("Enter Initial Stock: ");
                    newProduct.setStock(Integer.parseInt(scanner.nextLine()));
                    
                    productDAO.addProduct(newProduct);
                    break;

                case 4:
                    // Update Stock
                    System.out.println("\n--- Update Stock ---");
                    System.out.print("Enter Product ID: ");
                    int stockProdId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Quantity to ADD: ");
                    int addQty = Integer.parseInt(scanner.nextLine());
                    
                    inventoryService.addStock(stockProdId, addQty);
                    break;

                case 5:
                    // View Orders
                    System.out.println("\n--- Order History ---");
                    List<Order> orders = orderDAO.getAllOrders();
                    for (Order o : orders) {
                        // Notice the chained getters: o.getCustomer().getCustomerId()
                        System.out.println("Order ID: " + o.getOrderId() + 
                                           " | Customer ID: " + o.getCustomer().getCustomerId() + 
                                           " | Product ID: " + o.getProduct().getProductId() + 
                                           " | Qty: " + o.getQuantity() + 
                                           " | Total: $" + o.getTotalPrice() + 
                                           " | Date: " + o.getOrderDate());
                    }
                    break;
                case 6:
                    // Search Product
                    System.out.println("\n--- Search Product ---");
                    System.out.print("Enter Product ID: ");
                    int searchId = Integer.parseInt(scanner.nextLine());
                    Product found = productDAO.getProductById(searchId);
                    if (found != null) {
                        System.out.println("Found: " + found.getName() + " (" + found.getCategory() + ") - Stock: " + found.getStock());
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 7:
                    // Add Customer (Helper for testing)
                    System.out.println("\n--- Add New Customer ---");
                    Customer newCust = new Customer();
                    System.out.print("Enter Name: ");
                    newCust.setName(scanner.nextLine());
                    System.out.print("Enter Membership (Regular/Silver/Gold): ");
                    newCust.setMembership(scanner.nextLine());
                    
                    customerDAO.addCustomer(newCust);
                    break;

                case 8:
                    // Exit
                    System.out.println("Exiting system. Have a great day!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}