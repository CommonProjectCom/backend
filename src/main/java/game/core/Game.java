package game.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<Integer> history;
    private int id;

    public Game(int id) {
        this.history = new HashSet<>();
        this.id = id;
    }

    public boolean move(int move) {
        return !history.add(move);
    }
}
