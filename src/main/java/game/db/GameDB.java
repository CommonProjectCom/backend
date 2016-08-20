package game.db;

import game.Goroda;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimePrinter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class GameDB {

    private static final String WRITE_OBJECT_SQL = "INSERT INTO current_games(name, object_value) VALUES (?, ?)";
    private static final String READ_OBJECT_SQL = "SELECT object_value FROM current_games WHERE id = ?";
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
                getConnection(properties.getProperty("url"),
                        properties.getProperty("username"),
                        properties.getProperty("password"));
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

        DateTime dateTime = new DateTime(date);
        dateTime.plusHours(7);
        return "ID:" + id + " \n " + dateTime + " \n " + "NAME:" + name;
    }

}