package game.core;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class GameDB {

    private static final String NEW_GAME_SQL = "INSERT INTO current_games(name) VALUES (?)";
    private static final String UPDATE_OBJECT_SQL = "UPDATE current_games SET object_value = ? WHERE id = ?";
    private static final String GET_URL_SQL = "SELECT url FROM cities WHERE name = ?";
    private static final String READ_OBJECT_SQL = "SELECT object_value FROM current_games WHERE id = ?";
    private static final String GET_LAST_INSERT_ID = "SELECT DISTINCT LAST_INSERT_ID() from current_games";

    private Connection connection;

    public GameDB() throws SQLException, IOException, ClassNotFoundException {
        setConnection();
    }

    private void setConnection() throws IOException, SQLException, ClassNotFoundException {
        Properties properties = loadProperties();
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager
                .getConnection(properties.getProperty("url"),
                        properties.getProperty("username"),
                        properties.getProperty("password"));
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        InputStream stream = getClass().getResourceAsStream("db.properties");
        properties.load(stream);

        return properties;
    }

    ArrayList<String> getData() throws SQLException {
        ArrayList<String> cities = new ArrayList<>();

        String sql = "SELECT name FROM cities";

        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            String city = resultSet.getString("name");
            cities.add(city.toUpperCase());
        }

        return cities;
    }

    public int createGame(String clientName) throws SQLException {
        int gameID = -1;

        PreparedStatement preparedStatement = connection.prepareStatement(NEW_GAME_SQL);
        preparedStatement.setString(1, clientName);
        preparedStatement.executeUpdate();

        preparedStatement.execute(GET_LAST_INSERT_ID);
        ResultSet resultSet = preparedStatement.getResultSet();
        if (resultSet.next()) {
            gameID = resultSet.getInt("LAST_INSERT_ID()");
        }

        resultSet.close();
        preparedStatement.close();

        return gameID;
    }

    public void updateGame(Game game) throws SQLException {
        int gameID = game.getGameID();

        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OBJECT_SQL);
        preparedStatement.setObject(1, game);
        preparedStatement.setInt(2, gameID);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }


    public Game getGame(int id) throws SQLException, IOException, ClassNotFoundException {
        Game deSerializedGame = null;

        PreparedStatement preparedStatement = connection.prepareStatement(READ_OBJECT_SQL);
        preparedStatement.setLong(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        byte[] buf = resultSet.getBytes(1);
        ObjectInputStream objectIn;
        if (buf != null) {
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
            deSerializedGame = (Game) objectIn.readObject();
        }
        resultSet.close();
        preparedStatement.close();

        return deSerializedGame;
    }

    public String getURL(String nameCity) {
        String url = "ERROR_IN_GameDB_getURL";
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_URL_SQL)){
            preparedStatement.setString(1, nameCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("preparedStatement:" + preparedStatement.toString());
            System.out.println("resultSet:" + resultSet.toString());
            if (resultSet.next()) {
                url = resultSet.getString("url");
                System.out.println("in while " + url);
            }
            resultSet.close();
            preparedStatement.close();
            System.out.println("in end of try");
        } catch (SQLException e) {
            System.out.println("in catch");
            e.printStackTrace();
            System.out.println(e.getSQLState());
            System.out.println("Error is GameDB/getURL()!");
        } finally {
            System.out.println("in finally");
            return url;
        }
    }

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