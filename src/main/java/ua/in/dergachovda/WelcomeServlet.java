package ua.in.dergachovda;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<!doctype html>");
        writer.println("<html lang=\"en\">");
        writer.println("<head>");
        writer.println("<title>Welcome Servlet</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h1>Hello!</h1>");
        writer.println("<br><a href=\"#\" onclick=\"history.back();\"><- Back</a>");
        writer.println("</body>");
        writer.println("</html>");
    }
}