package game.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class GameDB {

    private Connection connection;
    private static GameDB instance;

    private GameDB() {
        try {
            isConnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static GameDB getInstance() {
        if (instance == null)
            instance = new GameDB();
        return instance;
    }

    private boolean isConnect() throws IOException, SQLException, ClassNotFoundException {
        Properties properties = loadProperties();
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.
                getConnection(properties.getProperty("url"),
                        properties.getProperty("username"),
                        properties.getProperty("password"));
        return true;
    }

    private Properties loadProperties() throws IOException{
        Properties properties = new Properties();
        InputStream stream = getClass().getResourceAsStream("db.properties");
        properties.load(stream);
        return properties;
    }

    public int createGame() throws SQLException {
        int gameID = -1;

        String insert = "INSERT INTO current_games(name) VALUES('New game');";
        String select = "SELECT LAST_INSERT_ID() from current_games;";

        Statement statement = connection.createStatement();
        statement.execute(insert);
        statement.execute(select);

        ResultSet resultSet = statement.getResultSet();
        gameID = resultSet.getInt("id");

        return gameID;
    }
}