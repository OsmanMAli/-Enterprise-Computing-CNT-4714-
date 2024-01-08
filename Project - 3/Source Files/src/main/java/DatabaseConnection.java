/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: <DatabaseConnection.java>
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


 //Class for managing database connections and executing SQL queries.
 
public class DatabaseConnection {
    static Connection conn;

    
     //Establishing a connection to the database using the Workbench URL, username, and password.
     
      // url represents the URL of the database.
       //userName represents the username for authentication.
       //pwd= represents the password for authentication.
     
    public static void connect(String url, String userName, String pwd){
        try {
            conn = DriverManager.getConnection(url, userName, pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
      //Executing a SQL query and returns the result set.
     
    public static ResultSet executeQuery(String sql){
        try {
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
      //Executing a SQL query for insert, update, or delete operations and returns the number of affected rows.
    
    public static int executeQuery1(String sql){
        try {
            return conn.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
