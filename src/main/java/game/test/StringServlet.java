package game.test;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

@WebServlet("/String")
public class StringServlet extends HttpServlet {

    public StringServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("This Servlet Works");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            /*int length = request.getContentLength();
            byte[] input = new byte[length];
            ServletInputStream sin = request.getInputStream();
            int c, count = 0;
            while ((c = sin.read(input, count, input.length - count)) != -1) {
                count += c;
            }
            sin.close();*/
            String recievedString = getInput(request);
//            String recievedString = new String(input);
            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());

            String output = recievedString.toUpperCase();

            writer.write(output);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(e.getMessage());
            response.getWriter().close();
        }
    }

    private String getInput(HttpServletRequest request) throws IOException {
        int length = request.getContentLength();
        byte[] input = new byte[length];
        ServletInputStream servletInputStream = request.getInputStream();
        int c, count = 0;
        while ((c = servletInputStream.read(input, count, input.length - count)) != -1) {
            count += c;
        }
        servletInputStream.close();
        return new String(input);
    }

}