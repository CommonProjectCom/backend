package game.servlets;

import game.core.Game;
import game.core.GameDB;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

//Information about city
@WebServlet("/Info")
public class InformationServlet extends HttpServlet {

    public InformationServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cityName = new String(request.getParameter("name").getBytes(request.getCharacterEncoding()),"cp1251");
        String url = getUrl(cityName);
        response.getOutputStream().println("Info Servlet Works");
        if (!url.isEmpty()) {
            response.getOutputStream().println(cityName + " : " + url);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;
        try {
            int length = request.getContentLength();
            byte[] input = new byte[length];
            ServletInputStream sin = request.getInputStream();
            int c, count = 0;
            while ((c = sin.read(input, count, input.length - count)) != -1) {
                count += c;
            }
            sin.close();
            String cityName = new String(input);

            //Test
            System.out.println("Info/input: " + cityName);

            url = getUrl(cityName);

            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write("URL:" + url);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(e.getMessage());
            response.getWriter().close();
        }

    }

    private String getUrl(String cityName) {
        String url = "Error";

        try {
            GameDB db = new GameDB();
            url = db.getURL(cityName);
            db.disconnect();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            return url;
        }
    }
}
