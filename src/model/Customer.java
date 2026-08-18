package model;
public class Customer{
	//Instance variables
	private int customerId;
	private String name;
	private String membership;
	
	//Default customer
	public Customer() {
		
	}
	//Parameterized customer
	public Customer(int customerId, String name, String membership) {
		this.customerId=customerId;
		this.name=name;
		this.membership=membership;
	}
	//getters
	public int getCustomerId() {
		return customerId;
	}
	public String getName() {
		return name;
	}
	public String getMembership() {
		return membership;
	}
	//setters
	public void setCustomerId(int customerId) {
		this.customerId=customerId;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setMembership(String membership) {
		this.membership=membership;
	}
	//toString()
	@Override
	public String toString() {
		return "Customer{" +
	           "customerId=" + customerId +
	           ", name='" + name + '\'' +
	           ",membership='" + membership + '\'' +
	           '}';
	}
}