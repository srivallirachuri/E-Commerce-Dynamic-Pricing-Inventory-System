package model;
import java.time.LocalDate;
public class Order{
	private int orderId;
	private Customer customer;
	private Product product;
	private int quantity;
	private double unitPrice;
	private double totalPrice;
	private LocalDate orderDate;
	
	//default constructor
	public Order() {
		
	}
	//parameterized construictor
	public Order(int orderId,
            Customer customer,
            Product product,
            int quantity,
            double unitPrice,
            double totalPrice,
            LocalDate orderDate) {
		this.orderId=orderId;
		this.customer=customer;
		this.product=product;
		this.quantity=quantity;
		this.unitPrice=unitPrice;
		this.totalPrice=totalPrice;
		this.orderDate=orderDate;
	}
	//getters
	public int getOrderId() {
		return orderId;
	}
	public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    // Setters

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    // toString()

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customer=" + customer +
                ", product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                '}';
    }
}

