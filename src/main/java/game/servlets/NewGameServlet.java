package game.servlets;

import game.db.GameDB;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;


@WebServlet("/NewGame")
public class NewGameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public NewGameServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("This Servlet Works");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        GameDB bd = GameDB.getInstance();

        int gameID = -1;

        try {
            int length = request.getContentLength();
            byte[] input = new byte[length];
            ServletInputStream sin = request.getInputStream();
            int c, count = 0;
            while ((c = sin.read(input, count, input.length - count)) != -1) {
                count += c;
            }
            sin.close();

            String recievedString = new String(input);

            try {
                gameID = bd.createGame(recievedString);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write("GameID:" + gameID);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(e.getMessage());
            response.getWriter().close();
        }
    }

}