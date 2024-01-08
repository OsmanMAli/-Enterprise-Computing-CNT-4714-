
/* Name: <Osman Ali>
Course: CNT 4714 – Fall 2023
Assignment title: Project 1 – Event-driven Enterprise Simulation
Date: Sunday September 17, 2023
*/
package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


  //NileDotComApp class represents a simple e-commerce application where users can find items, add them to a shopping cart, view the cart, and checkout.
 
public class NileDotComApp extends JFrame implements Serializable {
    private static final long serialVersionUID = 1L;
	// Lists to store inventory and the shopping cart
    private final List<Item> inventory;
    private final List<Item> shoppingCart;

    // Text fields for user input
    private final JTextField itemIdField;
    private final JTextField quantityField;
    private final JTextField itemDetailsField;
    private final JTextField subtotalField;

    // Buttons for various actions
    private final JButton addToCartButton;
    private final JButton findItemButton;
    private final JButton viewCartButton;
    private final JButton checkoutButton;

    // Label to display item number
    private final JLabel itemIdLabel;
    JLabel detailLabel;
    JLabel quantityLabel;
    JLabel subtotalLabel;

    // Tracks the current item number
    private int itemNumber = 1;
    private String currentOrderTimestamp = "";


   
     //Constructor for the NileDotComApp class.
      //Initializes the application's user interface.
     
    public NileDotComApp() {
        // Initialize lists for inventory and shopping cart
        inventory = new ArrayList<>();
        shoppingCart = new ArrayList<>();

        // Set up the main frame
        setTitle("Nile Dot Com - Fall 2023 (Osman Ali) ");
        setSize(780, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create button and text panels
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.decode("#31ccff"));

        JPanel textPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        textPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        textPanel.setBackground(Color.decode("#3f3f3f"));

        // Create labels and text fields
        itemIdLabel = new JLabel("Enter item ID for item #" + itemNumber + ":");
        quantityLabel = new JLabel("Enter Quantity for Item #" + itemNumber + ":");
        detailLabel = new JLabel("Details for Item #" + (itemNumber + ":"));
        subtotalLabel = new JLabel("Order subtotal for " + (itemNumber - 1) + " item(s):");
        itemIdField = createStyledTextField();
        quantityField = createStyledTextField();
        itemDetailsField = createStyledTextField();
        subtotalField = createStyledTextField();
        itemDetailsField.setEditable(false);
        subtotalField.setEditable(false);
        findItemButton = new JButton("Find Item #" + itemNumber);
        addToCartButton = new JButton("Add to Cart Item #" + itemNumber);
        viewCartButton = new JButton("View Cart");
        checkoutButton = new JButton("Checkout");
        JButton exitButton = new JButton("Exit (Close App)");
        JButton emptyCartButton = new JButton("Empty Cart - Start A New Order");

        // Add buttons to the button panel and apply styling
        addStyledButton(buttonPanel, findItemButton);
        addStyledButton(buttonPanel, addToCartButton);
        addStyledButton(buttonPanel, viewCartButton);
        addStyledButton(buttonPanel, checkoutButton);
        addStyledButton(buttonPanel, exitButton);
        addStyledButton(buttonPanel, emptyCartButton);

        // Set label text colors
        quantityLabel.setForeground(Color.WHITE);
        detailLabel.setForeground(Color.WHITE);
        itemIdLabel.setForeground(Color.WHITE);
        subtotalLabel.setForeground(Color.WHITE);

        // Add components to the text panel
        textPanel.add(itemIdLabel);
        textPanel.add(itemIdField);
        textPanel.add(quantityLabel);
        textPanel.add(quantityField);
        textPanel.add(detailLabel);
        textPanel.add(itemDetailsField);
        textPanel.add(subtotalLabel);
        textPanel.add(subtotalField);

        // Read inventory data from a file
        readInventoryFromFile();

        addToCartButton.setEnabled(false);

        findItemButton.addActionListener(e -> {
            boolean found = findItem();
            if (found) {
                addToCartButton.setEnabled(true);
            }
        });

        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());
        checkoutButton.addActionListener(e -> checkout());
        exitButton.addActionListener(e -> System.exit(0));
        emptyCartButton.addActionListener(e -> emptyCart());

        // Create the main panel and add components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(mainPanel);

        setVisible(true);

        // Disables checkout and View Cart buttons initially
        checkoutButton.setEnabled(false);
        viewCartButton.setEnabled(false);
        addToCartButton.setEnabled(false);
    }

    //adds styled button  to pannel
    private void addStyledButton(JPanel panel, JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        panel.add(button);
    }

    //creates a styled text field
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        Font customFont = new Font("Arial", Font.PLAIN, 12);
        textField.setFont(customFont);
        textField.setPreferredSize(new Dimension(150, 10));
        return textField;
    }

    //finds an item based on the given ID
    private boolean findItem() {
        String itemId = itemIdField.getText().trim();
        String quantityText = quantityField.getText().trim();
        int enteredQuantity;

        try {
            enteredQuantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Nile Dot Com - Error", JOptionPane.ERROR_MESSAGE);
            quantityField.setText("");
            return false;
        }

        for (Item item : inventory) {
            if (item.getId().equals(itemId)) {
                if (enteredQuantity > 0 && enteredQuantity <= item.getQuantity()) {
                    String itemDetails =item.getId()+ "\n"  + item.getName() + "\n$" + item.getPrice();
                    double discountPercent = getDiscountPercent(enteredQuantity);
                    double discountedPrice = item.getPrice() * (1 - (discountPercent / 100.0));

                    itemDetails += "\n" + enteredQuantity;
                    itemDetails += "\n" + discountPercent + "%";
                    DecimalFormat df = new DecimalFormat("#.##");
                    itemDetails += "\n$" + df.format(discountedPrice);

                    itemDetailsField.setText(itemDetails);
                    detailLabel.setText("Details for Item #" + itemNumber + ":");
                    addToCartButton.setEnabled(true);
                    findItemButton.setEnabled(false);
                    return true;
                }
                else if (item.getQuantity() == 0){
                    JOptionPane.showMessageDialog(this, "Sorry...that item is out of stock, please try another item", "Nile Dot Com - Error", JOptionPane.ERROR_MESSAGE);
                    itemIdField.setText("");
                    quantityField.setText("");
                    return false;
                }
                else {
                    JOptionPane.showMessageDialog(this, "Insufficient Stock. Only " + item.getQuantity() + " on hand. Please reduce the quantity", "Nile Dot Com - Error", JOptionPane.ERROR_MESSAGE);
                    quantityField.setText("");
                    return false;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Item ID " + itemId + " not in file.", "Nile Dot Com - Error", JOptionPane.ERROR_MESSAGE);
        itemIdField.setText("");
        return false;
    }


   //adds items to shopping cart
    private void addToCart() {
        String itemId = itemIdField.getText().trim();
        String quantityText = quantityField.getText().trim();

        for (Item item : inventory) {
            if (item.getId().equals(itemId)) {
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Nile Dot Com - Error", JOptionPane.ERROR_MESSAGE);
                    quantityField.setText("");
                    return;
                }
                if (quantity <= item.getQuantity()) {
                    item.setQuantity(item.getQuantity() - quantity);
                    Item cartItem = new Item(item.getId(), item.getName(), item.getPrice(), quantity);
                    shoppingCart.add(cartItem);
                    JOptionPane.showMessageDialog(this, "Item #" + itemNumber + " accepted. Added to your cart.", "Nile Dot Com - Item Confirmed", JOptionPane.INFORMATION_MESSAGE);

                    itemNumber++;
                    findItemButton.setText("Find Item #" + itemNumber);
                    addToCartButton.setText("Add to Cart Item #" + itemNumber);
                    itemIdLabel.setText("Enter item ID for item #" + itemNumber + ":");
                    quantityLabel.setText("Enter Quantity for Item #" + itemNumber + ":");
                    subtotalLabel.setText("Order subtotal for " + (itemNumber - 1) + " item(s):");


                    double totalSubtotal = calculateTotalSubtotal();
                    subtotalField.setText(String.format("$%.2f", totalSubtotal));

                    checkoutButton.setEnabled(true);
                    viewCartButton.setEnabled(true);
                    findItemButton.setEnabled(true);

                    itemIdField.setText("");
                    quantityField.setText("");
                    addToCartButton.setEnabled(false);

                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Item not found.", "Nile Dot Com - Error", JOptionPane.ERROR_MESSAGE);
    }

    //calculating the subtotal for items in cart
    private double calculateTotalSubtotal() {
        double totalSubtotal = 0;
        for (Item item : shoppingCart) {
            totalSubtotal += item.getPrice() * item.getQuantity();
        }
        return totalSubtotal;
    }

//Displays contents of cart
    private void viewCart() {
        if (shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder cartMessage = new StringBuilder();
        cartMessage.append("Shopping Cart:\n");

        for (int i = 0; i < shoppingCart.size(); i++) {
            Item item = shoppingCart.get(i);
            double itemTotal = item.getPrice() * item.getQuantity();
            double discountPercent = getDiscountPercent(item.getQuantity());

            cartMessage.append(String.format("%d.%s \"%s\" $%.2f %d %s$%.2f",
                    i + 1, item.getId(), item.getName(), item.getPrice(),
                    item.getQuantity(),  discountPercent + "%", itemTotal));

            if (i < shoppingCart.size() - 1) {
                cartMessage.append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, cartMessage.toString(), "Nile Dot Com - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);
    }

//Preforms checkout
    private void checkout() {
        if (shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Empty Cart", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double subtotal = 0;
        double totalDiscount = 0;
        double discount = 0;
        StringBuilder cartMessage = new StringBuilder();
        cartMessage.append("Date: ").append(new SimpleDateFormat("MMMM dd, yyyy, hh:mm:ss a zzz").format(new Date())).append("\n");
        cartMessage.append("Number of line items: ").append(shoppingCart.size()).append("\n");
        cartMessage.append("\n");
        cartMessage.append("Item# / ID / Title / Price / Qty / Disc% / Subtotal:\n");
        for (int i = 0; i < shoppingCart.size(); i++) {
            Item item = shoppingCart.get(i);
            double itemTotal = item.getPrice() * item.getQuantity();
            double discountPercent = getDiscountPercent(item.getQuantity());
            double discountAmount = (itemTotal * discountPercent) / 100.0;
            itemTotal -= discountAmount;

            cartMessage.append(String.format("%2d. ", i + 1))
                    .append(item.getId()).append(" \"").append(item.getName()).append("\" ")
                    .append(String.format("$%.2f", item.getPrice())).append(" ")
                    .append(item.getQuantity()).append(" ")
                    .append(String.format("%.0f%%", discountPercent)).append(" ") // Display the discount percentage
                    .append(String.format("$%.2f", itemTotal)).append("\n");

            subtotal += itemTotal;
            discount =  discountPercent;
            totalDiscount += discountAmount;
        }

        double taxRate = 0.06;
        double taxRatePercent = taxRate * 100;
        double taxAmount = subtotal * taxRate;
        double totalCost = subtotal + taxAmount;

        cartMessage.append("\n");
        cartMessage.append("Order Subtotal: ").append(String.format("$%.2f", subtotal)).append("\n");
        cartMessage.append("Discount Percentage: ").append(String.format("%.2f", discount)).append("\n");
        cartMessage.append("Discount Total: ").append(String.format("$%.2f", totalDiscount)).append("\n");

        cartMessage.append("Tax Rate: ").append(String.format("%.2f%%", taxRatePercent)).append("\n");
        cartMessage.append("Tax Amount: ").append(String.format("$%.2f", taxAmount)).append("\n");
        cartMessage.append("Order Total: ").append(String.format("$%.2f", totalCost)).append("\n");
        cartMessage.append("\n");
        cartMessage.append("Thanks for shopping at Nile Dot Com!");

        appendTransactionToCSV(shoppingCart);

        shoppingCart.clear();
        itemDetailsField.setText("");
        subtotalField.setText("");
        itemNumber = 1;
        findItemButton.setText("Find Item #" + itemNumber);
        addToCartButton.setText("Add to Cart Item #" + itemNumber);
        itemIdLabel.setText("Enter item ID for item #" + itemNumber + ":");
        quantityLabel.setText("Enter Quantity for Item #" + itemNumber + ":");
        detailLabel.setText("Details for Item #" + (itemNumber - 1) + ":");
        subtotalLabel.setText("Order subtotal for " + (itemNumber - 1) + " item(s):");


        // Disable "Checkout" and "View Cart" buttons after checkout
        checkoutButton.setEnabled(false);
        viewCartButton.setEnabled(false);
        itemIdField.setEditable(false);
        quantityField.setEditable(false);
        findItemButton.setEnabled(false);

        JOptionPane.showMessageDialog(this, cartMessage.toString(), "Nile Dot Com - Final INVOICE", JOptionPane.INFORMATION_MESSAGE);
    }

    private void appendTransactionToCSV(List<Item> items) {
        if (items.isEmpty()) {
            return;
        }

        String fileName = "transactions.csv";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        StringBuilder transactionDetails = new StringBuilder();
        for (Item item : items) {
            String itemDetails = item.getId() + ", " + item.getName() + ", " + item.getPrice() +
                    ", " + item.getQuantity() + ", : " + getDiscountPercent(item.getQuantity()) + "%";

            transactionDetails.append(timestamp).append(", ").append(itemDetails).append(", ")
                    .append(new SimpleDateFormat("MMMM dd, yyyy").format(new Date())).append(", ")
                    .append(new SimpleDateFormat("hh:mm:ss a").format(new Date())).append("\n");
        }

        try (FileWriter writer = new FileWriter(fileName, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            // Check if the current order timestamp is different from the previous one
            if (!currentOrderTimestamp.equals(timestamp)) {
                if (!currentOrderTimestamp.isEmpty()) {
                    bufferedWriter.newLine(); // Add a newline separator if it's a new order
                }
                currentOrderTimestamp = timestamp;
            }

            bufferedWriter.write(transactionDetails.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //Calculates discount percentage based on quantity of items
    private double getDiscountPercent(int quantity) {
        if (quantity >= 1 && quantity <= 4) {
            return 0.0;
        } else if (quantity >= 5 && quantity <= 9) {
            return 10.0;
        } else if (quantity >= 10 && quantity <= 14) {
            return 15.0;
        } else {
            return 20.0;
        }
    }

    //Empties cart
    private void emptyCart() {
        shoppingCart.clear();
        itemDetailsField.setText("");
        subtotalField.setText("");
        itemNumber = 1;
        findItemButton.setText("Find Item #" + itemNumber);
        addToCartButton.setText("Add to Cart Item #" + itemNumber);
        itemIdLabel.setText("Enter item ID for item #" + itemNumber + ":");
        quantityLabel.setText("Enter Quantity for Item #" + itemNumber + ":");
        detailLabel.setText("Details for Item #" + (itemNumber - 1) + ":");
        subtotalLabel.setText("Order subtotal for " + (itemNumber - 1) + " item(s):");
        itemIdField.setEditable(true);
        quantityField.setEditable(true);
        findItemButton.setEnabled(true);
        JOptionPane.showMessageDialog(this, "Shopping Cart is Empty.", "Shopping Cart is Empty", JOptionPane.INFORMATION_MESSAGE);
    }

    
     //Reads inventory data from a file and populates the inventory list.
     
    private void readInventoryFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("inventory.csv")) {
            assert inputStream != null;
            try (InputStreamReader reader = new InputStreamReader(inputStream);
                 BufferedReader br = new BufferedReader(reader)) {

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        double price = Double.parseDouble(parts[4].trim());
                        int quantity = Integer.parseInt(parts[3].trim());
                        Item item = new Item(id, name, price, quantity);
                        inventory.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   //main function
    public static void main(String[] args) {
        new NileDotComApp();
    }
}
