import game.db.GameDB;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class GameDBTest {
    @Test
    public void testDB() {
        GameDB bd = GameDB.getInstance();
        int gameID = 0;
        try {
            gameID = bd.createGame();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("gameID = " + gameID);
    }
}