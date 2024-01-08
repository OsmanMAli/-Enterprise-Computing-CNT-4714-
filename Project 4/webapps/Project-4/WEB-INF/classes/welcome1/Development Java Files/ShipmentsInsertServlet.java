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

public class ShipmentsInsertServlet extends HttpServlet {
    private Connection connection;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Initialize message variable
        String message = "";

        String snum = request.getParameter("snum_ship");
        String pnum = request.getParameter("pnum_ship");
        String jnum = request.getParameter("jnum_ship");
        int quantity;

        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            message = "Quantity must be a number.";
            request.setAttribute("message", message);
            request.getRequestDispatcher("/dataentryHome.jsp").forward(request, response);
            return;
        }

        try {
            if (connection == null || connection.isClosed()) {
                getDBConnection();
            }
            String sql = "INSERT INTO shipments (snum, pnum, jnum, quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, snum);
                preparedStatement.setString(2, pnum);
                preparedStatement.setString(3, jnum);
                preparedStatement.setInt(4, quantity);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    message = "New shipment record successfully entered into the database.";
                    if (quantity >= 100) {
                        updateSupplierStatus();
                        message += " Business logic applied: Supplier status updated.";
                    }
                } else {
                    message = "No new shipment record was created.";
                }
            }
        } catch (SQLException e) {
            message = "Error executing the SQL statement: " + e.getMessage();
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

    private void updateSupplierStatus() throws SQLException {
        String sql = "UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT snum FROM shipments WHERE quantity >= 100)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
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
