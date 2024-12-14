package login;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String LOAD_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/userdb";
    public static final String PASSWORD = "Dhruv9193";
    public static final String USERNAME = "root";

    private Connection connection;

    public LoginServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName(LOAD_DRIVER); // Load the MySQL JDBC driver
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
        String uname = request.getParameter("uname");
        String pword = request.getParameter("pword");

        try (PreparedStatement ps = connection.prepareStatement("SELECT pword FROM uinfo WHERE uname = ?")) {
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();

            PrintWriter pw = response.getWriter();
            pw.println("<html><body bgcolor=black text=white><center>");

            if (rs.next()) {
                String storedPassword = rs.getString("pword");
                if (storedPassword.equals(pword)) {
                    pw.println("<h2>Welcome, " + uname + "!</h2>");
                } else {
                    pw.println("<h2>Invalid password. Please try again.</h2>");
                }
            } else {
                pw.println("<h2>User not found. Please register first.</h2>");
            }

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
