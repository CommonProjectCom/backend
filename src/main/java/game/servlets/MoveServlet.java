package game.servlets;

import game.core.Game;
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
import java.sql.SQLException;

@WebServlet("/move")
public class MoveServlet extends HttpServlet {

    public MoveServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("This Servlet Works");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        GameDB bd = new GameDB();
        int gameID;
        Game game = null;

        try {
            int length = request.getContentLength();
            byte[] input = new byte[length];
            ServletInputStream sin = request.getInputStream();
            int c, count = 0;
            while ((c = sin.read(input, count, input.length - count)) != -1) {
                count += c;
            }
            sin.close();
            String inputString = new String(input);

            Parameter param = new Parameter(inputString);

            gameID = param.getGameID();

            if (gameID > 0) {

                try {
                    game = bd.getGame(gameID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (game == null) {
                    param.setMove("GAME NOT FOUND");
                } else {
                    param.setMove(game.setMove(param.getMove()));
                    bd.updateGame(game);
                }

            } else {
                param.setMove("ERROR ID");
            }

            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write(param.toString());
            writer.flush();
            writer.close();

        } catch (IOException | ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(e.getMessage());
            response.getWriter().close();
        }

        bd.disconnect();
    }
}
