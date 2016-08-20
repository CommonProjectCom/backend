package game.servlets;

import game.db.GameDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    public TestServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("</head>");
        writer.println("<body>");

        writer.println("<h1>Test Servlet:</h1>");

        writer.println(getLastDateFromTable());

        writer.println("</body>");
        writer.println("</html>");
    }

    private String getLastDateFromTable() {
        String str = "";

        try {
            GameDB bd = new GameDB();
            str = bd.getLastDateFromBD();
            bd.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        str = "<p>" + str + "</p>";
        return str;
    }
}
