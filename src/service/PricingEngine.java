package service;

import model.Customer;
import model.Product;

public class PricingEngine {

    /**
     * Calculates the final unit price based on dynamic business rules.
     * 
     * @param product  The product being purchased (contains base price and current stock)
     * @param customer The customer making the purchase (contains membership level)
     * @param quantity The amount the customer wants to buy
     * @return The dynamically calculated unit price
     */
    public double calculateFinalUnitPrice(Product product, Customer customer, int quantity) {
        
        double currentPrice = product.getBasePrice();

        // 1. Stock-based Dynamic Pricing (Supply & Demand)
        // If stock is critically low (< 10), increase price by 10%
        if (product.getStock() < 10) {
            currentPrice = currentPrice + (currentPrice * 0.10);
            System.out.println("High demand alert: Applied 10% surge pricing.");
        } 
        // If we have too much stock (> 100), decrease price by 15% (Clearance)
        else if (product.getStock() > 100) {
            currentPrice = currentPrice - (currentPrice * 0.15);
            System.out.println("Overstock sale: Applied 15% discount.");
        }

        // 2. Membership Discount
        // Assuming your ENUM was 'Regular', 'Silver', 'Gold'
        if (customer != null && customer.getMembership() != null) {
            if (customer.getMembership().equalsIgnoreCase("Gold")) {
                currentPrice = currentPrice - (currentPrice * 0.05); // 5% off
                System.out.println("Gold Member benefit: Applied extra 5% discount.");
            } else if (customer.getMembership().equalsIgnoreCase("Silver")) {
                currentPrice = currentPrice - (currentPrice * 0.02); // 2% off
            }
        }

        // 3. Bulk Purchase Discount
        // If buying more than 5 items, apply an extra 10% discount
        if (quantity > 5) {
            currentPrice = currentPrice - (currentPrice * 0.10);
            System.out.println("Bulk purchase: Applied 10% volume discount.");
        }

        return currentPrice;
    }
}