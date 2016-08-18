package game.db;

import game.Goroda;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class GameDB {

    static final String WRITE_OBJECT_SQL = "INSERT INTO current_games(name, object_value) VALUES (?, ?)";
    static final String READ_OBJECT_SQL = "SELECT object_value FROM current_games WHERE id = ?";
    static final String GET_LAST_INSERT_ID = "SELECT DISTINCT LAST_INSERT_ID() from current_games";

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

    public int createGame(String clientName, Goroda object) {
        int gameID = -1;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(WRITE_OBJECT_SQL);
            preparedStatement.setString(1, clientName);
            preparedStatement.setObject(2, object);
            preparedStatement.executeUpdate();
            preparedStatement.execute(GET_LAST_INSERT_ID);

            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                gameID = resultSet.getInt("LAST_INSERT_ID()");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gameID;
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

    public String getLastDateFromBD() throws Exception {
        String str = "";
        Statement statement = connection.createStatement();

        statement.execute("SELECT id, date, name FROM  current_games ORDER BY  id DESC LIMIT 1");

        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            str += "ID:" + resultSet.getInt("id") + " / ";
            str += "DATE:" + resultSet.getDate("date") + " ";
            str += "DATE:" + resultSet.getTime("date") + " / ";
            str += "NAME:" + resultSet.getString("name");
        }

        resultSet.close();
        statement.close();

        return str;
    }

}