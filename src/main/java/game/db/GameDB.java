package game.db;

import game.Goroda;

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
        } catch (IOException | SQLException | ClassNotFoundException e) {
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

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        InputStream stream = getClass().getResourceAsStream("db.properties");
        properties.load(stream);
        return properties;
    }

    public int createGame(String clientName, Goroda object) throws SQLException {
        String insert = "INSERT INTO current_games(name, object_value) VALUES(?, ?)";
        String select = "SELECT DISTINCT LAST_INSERT_ID() FROM current_games";

        PreparedStatement prStatement = connection.prepareStatement(insert);
        prStatement.setString(1, clientName);
        prStatement.setObject(2, object);
        prStatement.executeUpdate();

        Statement statement = connection.createStatement();
        statement.execute(select);
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return resultSet.getInt("LAST_INSERT_ID()");
        }

        return 0;
    }
}