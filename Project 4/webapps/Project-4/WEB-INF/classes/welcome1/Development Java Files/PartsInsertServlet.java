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

public class PartsInsertServlet extends HttpServlet {
    private Connection connection;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pnum = request.getParameter("pnum");
        String pname = request.getParameter("pname");
        String color = request.getParameter("color");
        String weight = request.getParameter("weight");
        String city = request.getParameter("part_city");
        String message = "";

        try {
            if (connection == null || connection.isClosed()) {
                getDBConnection();
            }
            String sql = "INSERT INTO parts (pnum, pname, color, weight, city) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, pnum);
                preparedStatement.setString(2, pname);
                preparedStatement.setString(3, color);
                preparedStatement.setString(4, weight);
                preparedStatement.setString(5, city);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    message = "New parts record (" + pnum + ", " + pname + ", " + color + ", " + weight + ", " + city + ") successfully entered into the database.";
                } else {
                    message = "No new parts record was created.";
                }
            }
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
