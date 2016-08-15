package game.db;

import game.Goroda;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class GameDB {

    static final String WRITE_OBJECT_SQL = "INSERT INTO current_games(name, object_value) VALUES (?, ?)";
    static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE id = ?";

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

        PreparedStatement preparedStatement = connection.prepareStatement(WRITE_OBJECT_SQL);

        preparedStatement.setString(1, clientName);
        preparedStatement.setObject(2, object);
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int id = -1;
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        resultSet.close();
        preparedStatement.close();

        return id;
    }

    public Goroda readGorodaFromBD(int id) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(READ_OBJECT_SQL);
        preparedStatement.setLong(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Goroda object = (Goroda) resultSet.getObject(1);

        resultSet.close();
        preparedStatement.close();

        return object;
    }

}