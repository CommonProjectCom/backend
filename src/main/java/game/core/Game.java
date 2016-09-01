package game.core;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<Integer> history;
    private int gameID;
    private Map<Character, Set<String>> data;
    private String currentMove = null;

    public Game(int id, GameDB db) {
        this.history = new LinkedHashSet<>();
        this.gameID = id;
        this.data = loadData(db);
    }

    public String setMove(String input) {
        System.out.println("currentMove - " + currentMove);
        String move = input.toUpperCase();
        char firstChar = move.charAt(0);
        char lastChar = move.charAt(move.length() - 1);
        System.out.println("move: " + move + " fc=" + firstChar + " lc=" + lastChar);

        if (input.equals(Message.GIVE_UP)) {
            System.out.println(Message.GAME_OVER);
            return Message.GAME_OVER;
        }

        if (currentMove != null && currentMove.charAt(currentMove.length() - 1) != firstChar) {
            System.out.println(Message.FALSE_MOVE);
            return Message.FALSE_MOVE;
        }


        if (!data.get(firstChar).contains(move)) {
            System.out.println(Message.NO_CITY);
            return Message.NO_CITY;
        }

        data.get(firstChar).remove(move);
        System.out.println("data.get(firstChar).remove(move);");

        Set<String> cities = data.get(lastChar);

        if (cities.isEmpty()) {
            System.out.println(Message.YOU_WIN);
            return Message.YOU_WIN;
        }

        for (String city : cities) {
            data.get(lastChar).remove(city);
            if (data.get(city.charAt(city.length() - 1)).isEmpty()) {
                return Message.YOU_LOSE;
            }
            currentMove = city;
            break;
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
