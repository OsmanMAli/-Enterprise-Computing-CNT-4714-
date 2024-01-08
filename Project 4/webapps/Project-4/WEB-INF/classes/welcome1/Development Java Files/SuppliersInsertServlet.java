/* Name: Osman Ali
Course: CNT 4714 – Fall 2023 – Project Three
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 5th, 2023
*/
package welcome1;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class SuppliersInsertServlet extends HttpServlet {
    private Connection connection;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String snum = request.getParameter("snum");
        String sname = request.getParameter("sname");
        String city = request.getParameter("city");
        String message = "";

        try {
            int status = Integer.parseInt(request.getParameter("status")); // Parse status as an integer

            if (connection == null || connection.isClosed()) {
                getDBConnection();
            }
            String sql = "INSERT INTO suppliers (snum, sname, status, city) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, snum);
                preparedStatement.setString(2, sname);
                preparedStatement.setInt(3, status);
                preparedStatement.setString(4, city);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    message = "New supplier record (" + snum + ", " + sname + ", " + status + ", " + city + ") successfully entered into the database.";
                } else {
                    message = "No new supplier record was created.";
                }
            }
        } catch (NumberFormatException e) {
            message = "This is an invalid request.";
            e.printStackTrace();
        } catch (SQLException e) {
            message = "Error executing the SQL statement: " + e.getMessage();
            e.printStackTrace();
        }

        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/dataentryHome.jsp");
        dispatcher.forward(request, response);
    }
    private void getDBConnection() throws ServletException {
        Properties props = new Properties();
        try {
            props.load(getServletContext().getResourceAsStream("/WEB-INF/lib/dataentryuser.properties"));
            String driver = props.getProperty("MYSQL_DB_DRIVER_CLASS");
            String url = props.getProperty("MYSQL_DB_URL");
            String dbUsername = props.getProperty("MYSQL_DB_USERNAME");
            String dbPassword = props.getProperty("MYSQL_DB_PASSWORD");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, dbUsername, dbPassword);
        } catch (Exception e) {
            throw new ServletException("Error connecting to database: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
