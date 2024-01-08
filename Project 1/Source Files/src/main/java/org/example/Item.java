/* Name: <Osman Ali>
Course: CNT 4714 – Fall 2023
Assignment title: Project 1 – Event-driven Enterprise Simulation
Date: Sunday September 17, 2023
*/

//the item class represents an item in the inventory. It stores information for the ID, name, price, and quantity for each item.
package org.example;

public class Item {
    private String id; // ID 
    private String name; //name
    private double price; //price 
    private int quantity; //quyantity of item in inventory

    //Constructor to create a new Item object with specified attributes.

    public Item(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
//getter for ID retrieval
    public String getId() {
        return id;
    }
//getter to recieve name 
    public String getName() {
        return name;
    }
//getter for price retrieval 
    public double getPrice() {
        return price;
    }
//Setter  to update the quantity of the item available in the inventory.
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
// Getter  to retrieve the quantity of the item available in the inventory

    public int getQuantity() {
        return quantity;
    }
}