package service;

import dao.ProductDAO;
import model.Product;
import exception.InsufficientStockException;

public class InventoryService {

    private final ProductDAO productDAO;

    // Constructor Injection
    public InventoryService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // Check if the product has enough stock available
    public boolean isStockAvailable(int productId, int requiredQuantity) {
        Product product = productDAO.getProductById(productId);
        
        if (product == null) {
            System.out.println("Product not found.");
            return false;
        }
        
        return product.getStock() >= requiredQuantity;
    }

    // Safely reduce stock during a purchase
    public void reduceStock(int productId, int quantityToReduce) throws InsufficientStockException {
        Product product = productDAO.getProductById(productId);

        if (product == null) {
            throw new InsufficientStockException("Cannot reduce stock: Product ID " + productId + " does not exist.");
        }

        int currentStock = product.getStock();

        if (currentStock < quantityToReduce) {
            throw new InsufficientStockException(
                "Not enough stock for " + product.getName() + 
                ". Requested: " + quantityToReduce + ", Available: " + currentStock
            );
        }

        // Apply business logic: calculate new stock
        int newStock = currentStock - quantityToReduce;
        product.setStock(newStock);

        // Tell DAO to update the database
        boolean success = productDAO.updateProduct(product);
        
        if (!success) {
            // This catches database connection issues during the update
            throw new RuntimeException("Database error occurred while updating inventory.");
        }
    }

    // Admin function to add new inventory shipments
    public void addStock(int productId, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            System.out.println("Quantity to add must be greater than zero.");
            return;
        }

        Product product = productDAO.getProductById(productId);
        
        if (product != null) {
            int newStock = product.getStock() + quantityToAdd;
            product.setStock(newStock);
            
            if(productDAO.updateProduct(product)) {
                System.out.println("Successfully added " + quantityToAdd + " units to " + product.getName() + ". New Stock: " + newStock);
            }
        } else {
            System.out.println("Failed to add stock: Product not found.");
        }
    }
}