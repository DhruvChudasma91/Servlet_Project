package login;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String LOAD_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/userdb";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Dhruv9193";

    private Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            // Load the JDBC driver
            Class.forName(LOAD_DRIVER);

            // Establish the connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("JDBC Driver not found.", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database connection failed.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String uname = request.getParameter("uname");
        String pword = request.getParameter("pword");

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO uinfo (fname, lname, uname, pword) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, uname);
            ps.setString(4, pword);
            ps.executeUpdate();

            // Respond to client
            PrintWriter pw = response.getWriter();
            pw.println("<html><body bgcolor=black text=white><center>");
            pw.println("<h2>Registration Successful...</h2>");
            pw.println("<a href='login.html'>Login</a>");
            pw.println("</center></body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database operation failed.");
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
