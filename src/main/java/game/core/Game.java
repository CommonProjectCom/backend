package game.core;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String NO_SUCH = "НЕТ ТАКОГО ГОРОДА ИЛИ ОН УЖЕ УПОМИНАЛСЯ";
    private static final String ERROR_MOVE = "НЕ ВЕРНЫЙ ХОД";

    private Set<Integer> history;
    private int gameID;
    private Map<Character, Set<String>> data;
    private String currentMove;


    public Game(int id, GameDB db) {
        this.history = new LinkedHashSet<>();
        this.gameID = id;
        this.data = loadData(db);
    }

    /*
    public boolean move(int move) {
        return !history.add(move);
    }
    */

    public String setMove(String move) {
        //Test
        System.out.println("Move: " + move);
        System.out.println("in Game.setMove begin: " + this.toString());

        char firstChar = move.charAt(0);
        char lastChar = move.charAt(move.length() - 1);

        if (currentMove != null && currentMove.charAt(currentMove.length() - 1) != firstChar)
            return ERROR_MOVE;

        if (!data.get(firstChar).contains(move))
            return NO_SUCH;

        data.get(firstChar).remove(move);

        for (String city : data.get(lastChar)) {
            data.get(lastChar).remove(city);
            currentMove = city;
        }
        System.out.println("in Game.setMove end: " + this.toString());
        return currentMove;
    }

    int getGameID() {
        return gameID;
    }

    private Map<Character, Set<String>> loadData(GameDB db) {
        Map<Character, Set<String>> data = new HashMap<>();

        for (String city : db.getData()) {
            char ch = city.charAt(0);
            Set<String> value = data.get(ch);
            if (value == null) {
                value = new HashSet<>();
            }
            value.add(city);
            data.put(ch, value);
        }

        return data;
    }

    @Override
    public String toString() {
        return "Game{" +
                "history=" + history +
                ", gameID=" + gameID +
                ", data=" + data +
                ", currentMove='" + currentMove + '\'' +
                '}';
    }
}
