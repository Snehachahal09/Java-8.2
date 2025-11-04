import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/yourdbname";
    private static final String USER = "root";
    private static final String PASSWORD = "yourpassword";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empIdParam = request.getParameter("empid");
        String viewAll = request.getParameter("view");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            out.println("<h1>Employee Records</h1>");

            // If Show All button is clicked
            if ("all".equals(viewAll)) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");

                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Salary</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("EmpID") + "</td><td>" + rs.getString("Name") + "</td><td>"
                            + rs.getDouble("Salary") + "</td></tr>");
                }
                out.println("</table>");
            } 
            // If search by ID is used
            else if (empIdParam != null) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Employee WHERE EmpID=?");
                ps.setInt(1, Integer.parseInt(empIdParam));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<p><b>Employee Found:</b></p>");
                    out.println("ID: " + rs.getInt("EmpID") + "<br>");
                    out.println("Name: " + rs.getString("Name") + "<br>");
                    out.println("Salary: " + rs.getDouble("Salary") + "<br>");
                } else {
                    out.println("<p style='color:red;'>No employee found.</p>");
                }
            }

            out.println("<br><a href='index.html'>Back</a>");
            con.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
