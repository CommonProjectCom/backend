package game.core;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class GameDB {

    private static final String WRITE_OBJECT_SQL = "INSERT INTO current_games(name, object_value) VALUES (?, ?)";
//    private static final String READ_OBJECT_SQL = "SELECT object_value FROM current_games WHERE id = ?";
    private static final String GET_LAST_INSERT_ID = "SELECT DISTINCT LAST_INSERT_ID() from current_games";

    private Connection connection;

    public GameDB() {
        try {
            setConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setConnection() throws IOException, SQLException, ClassNotFoundException {
        Properties properties = loadProperties();
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.
                getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
                );
    }

    public void disconnect() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Properties loadProperties() throws IOException {

        Properties properties = new Properties();
        InputStream stream = getClass().getResourceAsStream("core.properties");
        properties.load(stream);

        return properties;
    }

    public int createGame(String clientName, Game object) {

        int gameID = -1;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(WRITE_OBJECT_SQL);
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

/*
    public Core readDataFromBD(int id) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(READ_OBJECT_SQL);
        preparedStatement.setLong(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Core object = (Core) resultSet.getObject(1);

        resultSet.close();
        preparedStatement.close();
        return object;
    }
*/

    public String[] getLastDateFromBD() throws Exception {

        String[] arr = new String[3];
        int id = -1;
        Timestamp date = null;
        String name = "noName";

        Statement statement = connection.createStatement();
        statement.execute("SELECT id, date, name FROM  current_games ORDER BY  id DESC LIMIT 1");
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            id = resultSet.getInt("id");
            date = resultSet.getTimestamp("date");
            name = resultSet.getString("name");
        }

        resultSet.close();
        statement.close();

        arr[0] = "ID:" + id;
        arr[1] = new DateTime(date).toDateTime(DateTimeZone.forOffsetHours(3)).toString();
        arr[2] = "NAME:" + name;

        return arr;
    }

}