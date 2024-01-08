/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: <DatabaseLogger.java>
*/



import java.sql.*;


 //Class for logging database operations and updating operation counters.
 
public class DatabaseLogger {
    private static final String username = "project3app";
    private static final String password = "project3app";
    private static final String dbURL = "jdbc:mysql://localhost:3306/operationslog";
    private static Connection connection;

    
     //Establishing a connection to the database using  username and password set in workbench step.
     
    public static void connect() {
        try {
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
     //Retrieving the current total number of operations for a specific user and incrementing it by one
      //Then, updating the database with the new value. If the user does not exist in the table, a new row is inserted.
     
     
    public static void totalOperations(String user) {
        // Check if the user exists in the table
        boolean userExists = checkUserExists(user);

        if (userExists) {
            // Retrieve the current value for the user from the database
            String selectSql = "SELECT num_queries FROM operationslog.operationscount WHERE login_username = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setString(1, user);
                ResultSet resultSet = selectStatement.executeQuery();

                int currentValue = 0;
                if (resultSet.next()) {
                    currentValue = resultSet.getInt("num_queries");
                }

                // Increment the current value by one
                int updatedValue = currentValue + 1;

                // Update the database with the new value
                String updateSql = "UPDATE operationslog.operationscount SET num_queries = ? WHERE login_username = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, updatedValue);
                    updateStatement.setString(2, user);
                    updateStatement.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            // User does not exist, insert a new row
            String insertSql = "INSERT INTO operationslog.operationscount (login_username, num_queries) VALUES (?, 1)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, user);
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    
     // Retrieving the current total number of updates for a specific user and incrementing by one.
      //Then, updating the database with the new value. If the user does not exist in the table, a new row is inserted.
     
     
    public static void totalUpdates(String user) {
        // Check if the user exists in the table
        boolean userExists = checkUserExists(user);

        if (userExists) {
            // Retrieve the current value for the user from the database
            String selectSql = "SELECT num_updates FROM operationslog.operationscount WHERE login_username = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setString(1, user);
                ResultSet resultSet = selectStatement.executeQuery();

                int currentValue = 0;
                if (resultSet.next()) {
                    currentValue = resultSet.getInt("num_updates");
                }

                // Increment the current value by one
                int updatedValue = currentValue + 1;

                // Update the database with the new value
                String updateSql = "UPDATE operationslog.operationscount SET num_updates = ? WHERE login_username = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, updatedValue);
                    updateStatement.setString(2, user);
                    updateStatement.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            // User does not exist, insert a new row
            String insertSql = "INSERT INTO operationslog.operationscount (login_username, num_updates) VALUES (?, 1)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, user);
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean checkUserExists(String user) {
        String selectSql = "SELECT login_username FROM operationslog.operationscount WHERE login_username = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
            selectStatement.setString(1, user);
            ResultSet resultSet = selectStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

}
