package game.tests;

import game.core.GameDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tests")
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
        String out = "";

        try {
            GameDB bd = new GameDB();

            for (String s : bd.getLastDateFromBD()) {
                out += s + "<br>";
            }

            bd.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }
}
