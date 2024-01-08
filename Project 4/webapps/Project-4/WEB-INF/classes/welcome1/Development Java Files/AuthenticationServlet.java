/* Name: Osman Ali
Course: CNT 4714 – Fall 2023 – Project Three
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 5th, 2023
*/
package welcome1;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AuthenticationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inBoundUsername = request.getParameter("username");
        String inBoundPassword = request.getParameter("password");
        Path credentialsPath = Paths.get(getServletContext().getRealPath("/WEB-INF/lib/credentials.csv"));

        try (BufferedReader bufferedReader = Files.newBufferedReader(credentialsPath);
             CSVReader csvReader = new CSVReader(bufferedReader)) {

            String[] nextRecord;
            boolean userCredentialsOK = false;

            while ((nextRecord = csvReader.readNext()) != null) {
                String username = nextRecord[0];
                String password = nextRecord[1];

                if (inBoundUsername.equals(username) && inBoundPassword.equals(password)) {
                    userCredentialsOK = true;
                    break;
                }
            }

            if (userCredentialsOK) {
                HttpSession session = request.getSession();
                session.setAttribute("username", inBoundUsername);

                String targetPage = "errorpage.html"; // Default to error page
                switch (inBoundUsername) {
                    case "root":
                        targetPage = "rootHome.jsp";
                        break;
                    case "client":
                        targetPage = "clientHome.jsp";
                        break;
                    case "accountant":
                        targetPage = "accountantHome.jsp";
                        break;
                    case "dataentryuser":
                        targetPage = "dataentryHome.jsp";
                        break;
                }

                response.sendRedirect(targetPage);
            } else {
                response.sendRedirect("errorpage.html");
            }

        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h1>Error: " + e.getMessage() + "</h1></body></html>");
        }
    }
}

class CSVReader implements Closeable {
    private final BufferedReader br;
    private String line;

    public CSVReader(BufferedReader br) {
        this.br = br;
    }

    public String[] readNext() throws IOException {
        line = br.readLine();
        return (line != null) ? line.split(",") : null;
    }

    @Override
    public void close() throws IOException {
        if (br != null) {
            br.close();
        }
    }
}
