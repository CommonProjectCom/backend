package game.servlets;

import game.core.GameDB;
import game.core.Parameter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

@WebServlet("/Info")
public class InformationServlet extends HttpServlet {

    public InformationServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String cityName = request.getParameter("name");
        System.out.println("name: " + cityName);
        String url = getUrl(cityName);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE HTML>");
        response.getWriter().println("<html>" + "<body>");
        response.getWriter().println("<p>" + "Info Servlet Works" + "</p>");
        response.getWriter().println("<p>" + "CityName: " + cityName + "</p>");
        response.getWriter().println("<p>" + "URL: " + url + "</p>");
        response.getWriter().println("</body>" + "</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;
        try {
            int length = request.getContentLength();
            byte[] input = new byte[length];
            request.setCharacterEncoding("UTF-8");
            ServletInputStream sin = request.getInputStream();
            int c, count = 0;
            while ((c = sin.read(input, count, input.length - count)) != -1) {
                count += c;
            }
            sin.close();
            String cityName = new String(input);

            url = getUrl(cityName);

            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write("Name:" + cityName + Parameter.SEPARATOR + "URL:" + url);

            writer.flush();
            writer.close();

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(e.getMessage());
            response.getWriter().close();
        }

    }

    private String getUrl(String cityName) {
        //Test
        System.out.println("Info/input: " + cityName);

        String url = "ERROR_IN_INFO_getURL";
        try {
            GameDB db = new GameDB();
            url = db.getURL(cityName);
            db.disconnect();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            return url;
        }
    }
}