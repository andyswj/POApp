package edu.nus.iss.POApp.model;

import java.util.Map;

public class Order {
    private String name;
    private String address;
    private String email;
    private Map<String, Integer> lineItems;
    
    public Order() {
    }

    public Order(String name, String address, String email, Map<String, Integer> lineItems) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.lineItems = lineItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Integer> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Map<String, Integer> lineItems) {
        this.lineItems = lineItems;
    }

    public Integer getQuantity(String item) {
        return this.getLineItems().get(item);
    }
 
}
