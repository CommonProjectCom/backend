package game.core;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<Integer> history;
    private int gameID;
    private Map<Character, Set<String>> data;
    private String currentMove;

    public Game(int id, GameDB db) {
        this.history = new LinkedHashSet<>();
        this.gameID = id;
        this.data = loadData(db);
    }

    public String setMove(String input) {
        String move = input.toUpperCase();
        char firstChar = move.charAt(0);
        char lastChar = move.charAt(move.length() - 1);

        if (currentMove != null && currentMove.charAt(currentMove.length() - 1) != firstChar)
            return Message.FALSE_MOVE;

        if (!data.get(firstChar).contains(move))
            return Message.NO_CITY;

        if (input.equals(Message.GIVE_UP))
            return Message.GAME_OVER;

        data.get(firstChar).remove(move);

        for (String city : data.get(lastChar)) {
            data.get(lastChar).remove(city);
            currentMove = city;
        }
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
