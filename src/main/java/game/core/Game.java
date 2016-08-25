package game.core;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String NO_SUCH = "НЕТ ТАКОГО ГОРОДА ИЛИ ОН УЖЕ УПОМИНАЛСЯ";

    private Set<Integer> history;
    private int gameID;
    private Map<Character, Set<String>> data;
    private String currentMove;


    public Game(int id, GameDB db) {
        this.history = new LinkedHashSet<>();
        this.gameID = id;
    }

    /*
    public boolean move(int move) {
        return !history.add(move);
    }
    */

    public String setMove(String move) {
        return NO_SUCH;
    }

    public int getGameID() {
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
}
