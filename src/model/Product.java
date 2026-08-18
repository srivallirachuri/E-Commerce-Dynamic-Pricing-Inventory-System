package model;
public class Product{
	//Instance variables
	private int productId;
	private String name;
	private String category;
	private double basePrice;
	private int stock;
	
	//Default constructor
	public Product() {
		
	}
	//Parameterizes constructor
	public Product(int productId,String name,String category, double basePrice,int stock) {
		this.productId=productId;
		this.name=name;
		this.category=category;
		this.basePrice=basePrice;
		this.stock=stock;
	}
	//Getters
	public int getProductId() {
		return productId;
	}
	public String getName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public int getStock() {
		return stock;
	}
	//Setters
	public void setProductid(int productId) {
		this.productId=productId;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setCategory(String category) {
		this.category=category;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice=basePrice;
	}
	public void setStock(int stock) {
		this.stock=stock;
	}
	
	//toString()
	@Override
	public String toString() {
		return "Product{" +
	           "productId=" + productId +
	           ", name='" + name + '\'' +
	           ", category='" + category + '\'' +
               ", basePrice=" + basePrice +
               ", stock=" + stock +
               '}';
	}
}