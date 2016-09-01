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

    private char getFirstLetter(String s) {
        return s.charAt(0);
    }

    private char getLastLetter(String s) {
        return s.charAt(s.length() - 1);
    }

    public String setMove(String input) {

        if (input.equals(Message.GIVE_UP)) {
            System.out.println(Message.GAME_OVER);
            return Message.GAME_OVER;
        }

        String move = input.toUpperCase();
        char firstChar = getFirstLetter(move);
        char lastChar = getLastLetter(move);

        if (currentMove != null && getLastLetter(currentMove) != firstChar) {
            System.out.println(Message.FALSE_MOVE);
            return Message.FALSE_MOVE;
        }

        if (!data.get(firstChar).contains(move)) {
            System.out.println(Message.NO_CITY);
            return Message.NO_CITY;
        }

        data.get(firstChar).remove(move);

        //Move by server -->
        Set<String> cities = data.get(lastChar);

        if (cities.isEmpty()) {
            System.out.println(Message.YOU_WIN);
            return Message.YOU_WIN;
        }

        Random random = new Random();
        int counter = random.nextInt(5) + 1;
        String city = null;

        for (String next : cities) {
            city = next;
            if (counter < 0) {
                break;
            }
            counter--;
        }

        currentMove = city;
        cities.remove(city);
        if (data.get(getLastLetter(city)).isEmpty()) {
            return city + " > " + Message.YOU_LOSE;
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
